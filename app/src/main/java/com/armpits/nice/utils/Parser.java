package com.armpits.nice.utils;

import java.util.ArrayList;

public class Parser {

    public static String[] login(String account, String passwd) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        return networkingHandler.login(token, account, passwd);
    }

    public static ArrayList<String[]> getCoursesList(String account, String passwd) {
        Networking networkingHandler = new Networking();
        String token = networkingHandler.fetchToken();
        String sesskey = SoupSoup.parseSessionKey(networkingHandler.login(token, account, passwd)[1]);
        return networkingHandler.getCourses(sesskey);
    }

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
}
