package com.armpits.nice.networking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;

/**
 * To download a file, use getFileList first to get the link, then call getClient to get the session.
 * For example, in Normal OKHttp, we use "OkHttpClient client = new OkHttpClient();" to init a session,
 *  but here you have to call "OkHttpClient client = Parser.getClient" to get the session.  
 */
public class Parser {

    /**
     * Login in
     * If login failed, the status will be "Failed" and session key will be empty, Otherwise status will be "Succeed"
     * @param account account
     * @param passwd password
     * @return String[2]{Status, session key}
     */
    public static String[] login(String account, String passwd) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        return networkingHandler.login(token, account, passwd);
    }

    /**
     * Produce courses list
     * @param account account
     * @param passwd password
     * @return A list of String[Course name, unique id of course]
     */
    public static ArrayList<String[]> getCoursesList(String account, String passwd) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        String sesskey = SoupSoup.parseSessionKey(networkingHandler.login(token, account, passwd)[1]);
        return networkingHandler.getCourses(sesskey);
    }

    /**
     * Produce due list
     * @param account account
     * @param passwd password
     * @return A list of String[Title, submission link, due date]
     */
    public static ArrayList<String[]> getDueList(String id, String account, String passwd) {
        ArrayList<String[]> result = new ArrayList<>();
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        networkingHandler.login(token, account, passwd);
        ArrayList<String[]> linksList = SoupSoup.parseLinks(networkingHandler.loadCoursePage(id));
        for (String[] item: linksList) {
            if (item[0].equals("Submission")) {
                result.add(new String[]
                        {item[1], item[2], SoupSoup.parseDueDate(networkingHandler.loadSubmissionPage(item[2]))});
            }
        }
        return result;
    }

    /**
     * Produce file list
     * @param account account
     * @param passwd password
     * @return A list of String[file title, links, description, parent]
     */
    public static ArrayList<String[]> getFileList(String id, String account, String passwd) {
        ArrayList<String[]> result = new ArrayList<>();
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        networkingHandler.login(token, account, passwd);
        ArrayList<String[]> linksList = SoupSoup.parseLinks(networkingHandler.loadCoursePage(id));
        for (String[] item: linksList) {
            if (item[0].equals("File")) {
                String[] newItem = networkingHandler.parseRealFileLink(item);
                result.add(new String[]{item[1], newItem[2], item[3], item[4]});
            }
        }
        return result;
    }

    /**
     * Get client object
     * @param account account
     * @param passwd password
     * @return Client object
     */
    public static OkHttpClient getClient(String account, String passwd) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        networkingHandler.login(token, account, passwd);
        return networkingHandler.client;
    }

    /**
     * Download file into /Documents/nICE/module/parent/file.name.
     * Sample is down below.
     * @param module Module title.
     * @param parent Parent title.
     * @param url URL.
     * @param account account.
     * @param passwd password.
     * @param listener OnDownloadListener.
     */
    public static void download(String module, String parent, String filename, String url, String account,
                                String passwd, Networking.OnDownloadListener listener) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        networkingHandler.login(token, account, passwd);
        networkingHandler.download(module, parent, filename, url, listener);
    }

    public static Date parseDate(String dateString) {
        SimpleDateFormat ft = new SimpleDateFormat("E, dd MMMM yyyy, hh:mm", Locale.US);
        Date date = new Date();
        try {
            date = ft.parse(dateString);
        } catch (ParseException e) {
        }
        return date;
    }

    //new Thread(() -> {
    //    Parser.download("CSE305", "Presentation",
    //            "https://ice.xjtlu.edu.cn/pluginfile.php/8714/mod_resource/content/3/Designspec%20report%20-%20sample%203.pdf",
    //            "ACC.OUNT16", "PASSWORD", new Networking.OnDownloadListener() {
    //                @Override
    //                public void onDownloadSuccess() {
    //                    System.out.println("finished");
    //                }
    //
    //                @Override
    //                public void onDownloading(int progress) {
    //                    System.out.println(progress);
    //                }
    //
    //                @Override
    //                public void onDownloadFailed() {
    //                    System.out.println("FAILED");
    //                }
    //            });
    //}).start();
}