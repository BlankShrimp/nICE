package com.armpits.nice.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.armpits.nice.calendar.CalendarHandler;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Log;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;
import com.armpits.nice.networking.Networking;
import com.armpits.nice.networking.Parser;
import com.armpits.nice.notifications.Notify;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleService extends LifecycleService {

    private LiveData<List<Module>> modulesLiveData;
    private List<Module> modules;
    private LiveData<List<Material>> materialsLiveData;
    private List<Material> materials;
    private List<Material> downloadList;
    private List<Material> updateList;
    private LiveData<List<Deadline>> deadlineLiveData;
    private List<Deadline> deadlines;
    private List<Deadline> addingDueList;
    private List<Deadline> updateDueList;

    private String username;
    private String password;

    private Thread thread;

    public ScheduleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int statrtID) {
        username = SharedPreferencesManager.get(Const.SP_USERNAME, this);
        password = SharedPreferencesManager.get(Const.SP_PASSWORD, this);

        // TODO: Get update schedule
        modules = new ArrayList<>();
        Handler handler = new Handler(Looper.getMainLooper());
        modulesLiveData = NiceDatabase.getAllModules();
        handler.post(() -> modulesLiveData.observeForever(newModules -> {
            modules.clear();
            modules.addAll(newModules);
        }));

        materials = new ArrayList<>();
        downloadList = new ArrayList<>();
        updateList = new ArrayList<>();
        materialsLiveData = NiceDatabase.getAllMaterials();
        handler.post(() -> materialsLiveData.observeForever(newMaterials -> {
            materials.clear();
            materials.addAll(newMaterials);
        }));

        deadlines = new ArrayList<>();
        addingDueList = new ArrayList<>();
        updateDueList = new ArrayList<>();
        deadlineLiveData = NiceDatabase.getAllDeadlines();
        handler.post(() -> deadlineLiveData.observeForever(newMaterials -> {
            deadlines.clear();
            deadlines.addAll(newMaterials);
        }));

        if (thread == null) {
            thread = new Thread(new ServiceThread(this));
            thread.start();
        } else
            thread.run();


        return super.onStartCommand(intent, flags, statrtID);
    }

    private class ServiceThread implements Runnable {
        private Context context;

        ServiceThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            while (true) {
                if (Thread.interrupted())
                    return;

                sleep(3000);

                int ddlID = Notify.displayNotification(context, 2, true, "Fetching Deadlines",
                        "Click to cancel (Not realized yet). ");
                // Update dues
                if (!modules.isEmpty()) {
                    for (Module module : modules) {
                        if (!module.addDDLsToCalendar)
                            continue;
                        String id = module.code;
                        List<String[]> dueList = Parser.getDueList(id, username, password);
                        sleep(2000);

                        for (String[] due : dueList) {
                            System.out.println("flag1");
                            Boolean added = false;
                            Deadline deadline1 = new Deadline(id, module.title, Parser.parseDate(due[2]),
                                    due[0], true);
                            if (deadlines.isEmpty()) {
                                addingDueList.add(deadline1);
                                added = true;
                            }
                            for (Deadline deadline2 : deadlines) {
                                if (deadline1.equalsTo(deadline2)) {
                                    if (deadline2.shouldNotify)
                                        addingDueList.add(deadline1);
                                    System.out.println("flag2");
                                    added = true;
                                    break;
                                }
                            }
                            if (!added)
                                addingDueList.add(deadline1);
                        }
                    }

                    // Add to calendar
                    for (Deadline deadline : addingDueList) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR)
                                != PackageManager.PERMISSION_GRANTED)
                            break;
                        System.out.println(deadline.date);
                        CalendarHandler.addCalendarEvent(context, deadline.title, deadline.moduleTitle, deadline.date.getTime());
                        deadline.shouldNotify = false;
                        updateDueList.add(deadline);
                    }

                    // Update memory data
                    for (Deadline deadline : updateDueList) {
                        if (!deadline.shouldNotify) {
                            NiceDatabase.update(deadline);
                            addingDueList.remove(deadline);
                            boolean added = false;
                            for (Deadline existedDDL : deadlines) {
                                if (existedDDL.equalsTo(deadline)) {
                                    existedDDL.shouldNotify = false;
                                    added = true;
                                }
                            }
                            if (!added)
                                deadlines.add(deadline);
                            NiceDatabase.insert(new Log(new Date(), deadline.title + "(" +
                                    deadline.date + "): Succeeded. "));
                            Notify.displayNotification(context, 2, false, "New deadline",
                                    deadline.title + ": " + deadline.date.toString());
                        } else {
                            NiceDatabase.insert(new Log(new Date(), deadline.title + "(" +
                                    deadline.date + "): Failed. "));
                            Notify.displayNotification(context, 2, false, "Deadline detected",
                                    "Failed to add: " + deadline.title + ": " + deadline.date.toString());
                        }
                    }
                    updateDueList.clear();
                }
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.cancel(ddlID);

                // Update file list & Download files
                int fileID = Notify.displayNotification(context, 1, true, "Fetching Files",
                        "Click to cancel (Not realized yet). ");
                if (!modules.isEmpty()) {
                    for (Module module : modules) {
                        if (!module.enableDownloads)
                            continue;
                        String id = module.code;
                        List<String[]> fileList = Parser.getFileList(id, username, password);

                        sleep(2000);

                        for (String[] material : fileList) {
                            boolean added = false;
                            Material material1 = new Material(module.title, id, material[0], material[3],
                                    material[1], material[2], new Date(), true);
                            if (materials.isEmpty()) {
                                downloadList.add(material1);
                                added = true;
                            }
                            for (Material material2 : materials) {
                                if (material1.equalsTo(material2)) {
                                    if (material2.shouldDownload)
                                        downloadList.add(material1);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added)
                                downloadList.add(material1);
                        }
                    }

                    //Download
                    for (Material material : downloadList) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED)
                            break;
                        Parser.download(material.moduleTitle, material.parentPath, material.filename,
                                material.url, username, password, new Networking.OnDownloadListener() {
                                    @Override
                                    public void onDownloadSuccess() {
                                        material.shouldDownload = false;
                                        updateList.add(material);
                                        Notify.displayNotification(context, 1, false, "New file",
                                                material.moduleTitle + ": " + material.filename);
                                    }

                                    @Override
                                    public void onDownloading(int progress) {
                                        //Add notification
                                    }

                                    @Override
                                    public void onDownloadFailed() {
                                        updateList.add(material);
                                        Notify.displayNotification(context, 1, false, "File detected",
                                                "Failed to download: " + material.moduleTitle + ": " + material.filename);
                                    }
                                });

                    }

                    // Update memory data
                    for (Material material : updateList) {
                        if (!material.shouldDownload) {
                            NiceDatabase.update(material);
                            downloadList.remove(material);
                            boolean added = false;
                            for (Material existedMaterial : materials) {
                                if (existedMaterial.equalsTo(material)) {
                                    existedMaterial.shouldDownload = false;
                                    added = true;
                                }
                            }
                            if (!added)
                                materials.add(material);
                            NiceDatabase.insert(new Log(new Date(), material.filename + "(" +
                                    material.description + "): Succeeded. "));
                        } else {
                            NiceDatabase.insert(new Log(new Date(), material.filename + "(" +
                                    material.description + "): Failed. "));
                        }
                    }
                    updateList.clear();
                    notificationManager.cancel(fileID);
                }
                sleepUntilNextUpdate();
            }
        }

        private void sleepUntilNextUpdate() {
            String sleepTime = SharedPreferencesManager.get(Const.SP_UPDATE_FREQUENCY, context);
            final long HOUR = 1000 * 60 * 60;
            long time;

            switch (sleepTime) {
                case "30 minutes":
                    time = HOUR / 2;
                    break;
                case "hour":
                    time = HOUR;
                    break;
                case "8 hours":
                    time = HOUR * 8;
                    break;
                case "day":
                    time = HOUR * 24;
                    break;
                case "week":
                    time = HOUR * 24 * 7;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + sleepTime);
            }

            sleep(time);
        }

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ignore) {
            }
        }
    }
}
