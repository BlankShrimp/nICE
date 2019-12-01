package com.armpits.nice.utils;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

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
     * @return A list of String[file title, links, description]
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
                result.add(new String[]{item[1], newItem[2], item[3]});
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
}
