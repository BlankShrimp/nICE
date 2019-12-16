package com.armpits.nice.service;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.LiveData;

import com.armpits.nice.calendar.CalendarHandler;
import com.armpits.nice.db.NiceDatabase;
import com.armpits.nice.models.Deadline;
import com.armpits.nice.models.Material;
import com.armpits.nice.models.Module;
import com.armpits.nice.networking.Networking;
import com.armpits.nice.networking.Parser;
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
        handler.post(new Runnable() {
            @Override
            public void run() {
                modulesLiveData.observeForever(newModules -> {
                    modules.clear();
                    modules.addAll(newModules);
                });
            }
        });

        materials = new ArrayList<>();
        downloadList = new ArrayList<>();
        updateList = new ArrayList<>();
        materialsLiveData = NiceDatabase.getAllMaterials();
        handler.post(new Runnable() {
            @Override
            public void run() {
                materialsLiveData.observeForever(newMaterials -> {
                    materials.clear();
                    materials.addAll(newMaterials);
                });
            }
        });

        deadlines = new ArrayList<>();
        addingDueList = new ArrayList<>();
        updateDueList = new ArrayList<>();
        deadlineLiveData = NiceDatabase.getAllDeadlines();
        handler.post(new Runnable() {
            @Override
            public void run() {
                deadlineLiveData.observeForever(newMaterials -> {
                    deadlines.clear();
                    deadlines.addAll(newMaterials);
                });
            }
        });


        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            // TODO: Update dues
            if (!modules.isEmpty()) {
                for (Module module: modules) {
                    if (!module.addDDLsToCalendar)
                        continue;
                    String id = module.code;
                    List<String[]> dueList = Parser.getDueList(id, username, password);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    for (String[] due: dueList) {
                        Deadline deadline1 = new Deadline(id, module.title, Parser.parseDate(due[2]),
                                due[0], true);
                        if (deadlines.isEmpty())
                            addingDueList.add(deadline1);
                        for (Deadline deadline2: deadlines) {
                            if (deadline1.equalsTo(deadline2)) {
                                if (deadline2.shouldNotify)
                                    addingDueList.add(deadline2);
                            } else
                                addingDueList.add(deadline2);
                        }
                    }
                }

                //Add to calendar
                for (Deadline deadline: addingDueList) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED)
                        break;
                    System.out.println(deadline.date);
                    CalendarHandler.addCalendarEvent(this, deadline.title, deadline.moduleTitle, deadline.date.getTime());
                    deadline.shouldNotify = false;

                }

                for (Deadline deadline: updateDueList) {
                    NiceDatabase.update(deadline);
                    if (!deadline.shouldNotify)
                        addingDueList.remove(deadline);
                }
                updateDueList.clear();
            }

            // Update file list & Download files
            if (!modules.isEmpty()) {
                for (Module module : modules) {
                    if (!module.enableDownloads)
                        continue;
                    String id = module.code;
                    List<String[]> fileList = Parser.getFileList(id, username, password);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    for (String[] material : fileList) {
                        Material material1 = new Material(module.title, id, material[0], material[3],
                                material[1], material[2], new Date(), true);
                        if (materials.isEmpty())
                            downloadList.add(material1);
                        for (Material material2 : materials) {
                            if (material1.equalsTo(material2)) {
                                if (material2.shouldDownload)
                                    downloadList.add(material2);
                            } else
                                downloadList.add(material2);
                        }
                    }
                }

                //Download
                for (Material material : downloadList) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED)
                        break;
                        Parser.download(material.moduleTitle, material.parentPath, material.filename,
                                material.url, username, password, new Networking.OnDownloadListener() {
                                    @Override
                                    public void onDownloadSuccess() {
                                        material.shouldDownload = false;
                                        updateList.add(material);
                                    }

                                    @Override
                                    public void onDownloading(int progress) {
                                        //Add notification
                                    }

                                    @Override
                                    public void onDownloadFailed() {
                                        updateList.add(material);
                                    }
                                });

                }

                for (Material material : updateList) {
                    NiceDatabase.update(material);
                    if (!material.shouldDownload)
                        downloadList.remove(material);
                }
                updateList.clear();
            }
        }).start();


        return super.onStartCommand(intent, flags, statrtID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
