package com.armpits.nice.networking;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Networking {

    public OkHttpClient client;

    public Networking() {
        client = new TrustedClient().createTrustedClient();
    }

    /**
     * Prepare for logging in
     * @return
     */
    public String fetchToken() {
        String loginToken = null;
        Request request = new Request.Builder().url("https://ice.xjtlu.edu.cn/login/index.php").build();
        try {
            Response response = client.newCall(request).execute();
            loginToken = SoupSoup.parseLoginToken(response.body().string());
        } catch (IOException e) {
            System.out.println(e);
        }

        return loginToken;
    }

    /**
     * Login in
     * If login failed, the status will be "Failed" and session key will be empty, Otherwise status will be "Succeed"
     * @param token loginToken
     * @param account account
     * @param passwd password
     * @return String[2]{Status, session key}
     */
    public String[] login(String token, String account, String passwd) {
        String body = null;
        try {
            //first connection - verification
            RequestBody formBody = new FormBody.Builder()
                    .add("anchor", "")
                    .add("logintoken", token)
                    .add("username", account)
                    .add("password", passwd).build();
            Request firstRequest = new Request.Builder()
                    .url("https://ice.xjtlu.edu.cn/login/index.php")
                    .post(formBody)
                    .build();
            Response firstResponse = client.newCall(firstRequest).execute();


            //second connection - jump to 13748
            if (!firstResponse.body().string().contains("https://ice.xjtlu.edu.cn/login/index.php?testsession=13748")) {
                return new String[]{"Failed", "Wrong passwd maybe"};
            }
            Request secondRequest = new Request.Builder()
                    .url("https://ice.xjtlu.edu.cn/login/index.php?testsession=13748")
                    .build();
            Response secondResponse = client.newCall(secondRequest).execute();

            //third connection - jump to dashboard
            if (!secondResponse.body().string().contains("https://ice.xjtlu.edu.cn/my/")) {
                return new String[]{"Failed", "Bad connection maybe"};
            }
            Request thirdRequest = new Request.Builder()
                    .url("https://ice.xjtlu.edu.cn/my/")
                    .build();
            Response thirdResponse = client.newCall(thirdRequest).execute();
            body = thirdResponse.body().string();
        } catch (IOException e) {
            System.out.println(e);
        }
        return new String[]{"Succeed", body};
    }

    /**
     * Produce courses list
     * @param sessionKey session key discovered by login
     * @return A list of String[Course name, unique id of course]
     */
    public ArrayList<String[]> getCourses(String sessionKey) {
        ArrayList<String[]> result = new ArrayList<>();
        MediaType jsonType = MediaType.parse("application/json; charset=utf-8");
        String jsonString = "[{\"index\":0,\"methodname\":\"core_course_get_enrolled_courses_by_timeline_classification" +
                "\",\"args\":{\"offset\":0,\"limit\":24,\"classification\":\"all\",\"sort\":\"fullname\"}}]";
        RequestBody formBody =  RequestBody.create(jsonString,jsonType);
        Request request = new Request.Builder()
                .url("https://ice.xjtlu.edu.cn/lib/ajax/service.php?sesskey=" + sessionKey +
                        "&info=core_course_get_enrolled_courses_by_timeline_classification")
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            result = SoupSoup.parseCourses(response.body().string());
        } catch (IOException e) {
            System.out.println(e);
        }

        return result;
    }

    /**
     * Read assignment page
     * @param id id of assignment
     * @return
     */
    public String loadCoursePage(String id) {
        String result = "";
        Request request = new Request.Builder().url("https://ice.xjtlu.edu.cn/course/view.php?id=" + id).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            System.out.println(e);
        }
        return result;
    }

    /* TODO add download mechanism*/

    /**
     * Redirect to real file link
     * @param bundle original parsed type-title-fake link-date pair
     * @return redirected type-title-fake link-date pair
     */
    public String[] parseRealFileLink(String[] bundle) {
        Request firstRequest = new Request.Builder().url(bundle[2]).build();
        try {
            Response firstResponse = client.newCall(firstRequest).execute();

            bundle[2] = firstResponse.headers().get("Location");
        } catch (IOException e) {
            System.out.println(e);
        }
        return bundle;
    }

    /**
     * Download assignment page html file
     * @param url
     * @return html String
     */
    public String loadSubmissionPage(String url) {
        String result = "";
        Request firstRequest = new Request.Builder().url(url).build();
        try {
            Response firstResponse = client.newCall(firstRequest).execute();
            result = firstResponse.body().string();
        } catch (IOException e) {
            System.out.println(e);
        }
        return result;
    }
}
