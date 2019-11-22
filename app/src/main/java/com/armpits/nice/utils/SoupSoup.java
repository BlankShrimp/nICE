package com.armpits.nice.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoupSoup {

    public static String parseLoginToken(String input) {
        Document doc = Jsoup.parse(input);
        Element element = doc.selectFirst("input[name=logintoken]");
        return element.attr("value");
    }

    public static String parseSessionKey(String input) {
        ArrayList<String[]> result = new ArrayList<>();
        Document doc = Jsoup.parse(input);
        Element element = doc.selectFirst("div.logininfo").selectFirst("a:contains(Log out)");
        return element.attr("href").split("=")[1];
    }

    public static ArrayList<String[]> parseCourses(String input) {
        ArrayList<String[]> result = new ArrayList<>();
        Pattern r1 = Pattern
                .compile("\"fullnamedisplay\":\"(.*?)\",\"viewurl\":\"https:\\\\/\\\\/ice\\.xjtlu\\.edu\\.cn\\\\/course\\\\/view\\.php\\?id=(.*?)\",\"");
        Matcher m = r1.matcher(input);
        while (m.find()) {
            result.add(new String[]{m.group(1), m.group(2)});
        }
        return result;
    }

    public static ArrayList<String[]> parseLinks(String input) {
        ArrayList<String[]> result = new ArrayList<>();
        Document doc = Jsoup.parse(input);
        Elements elements = doc.select("div.activityinstance");
        for (Element e: elements) {
            String[] assembled = new String[4];
            Element element1 = e.selectFirst("a");
            if (!element1.attr("onclick").isEmpty() &&
                    e.selectFirst("span.resourcelinkdetails") != null) {
                assembled[0] = "File";
                assembled[2] = element1.attr("onclick").split("'")[1];
                assembled[3] = e.selectFirst("span.resourcelinkdetails").text().split("Modified ")[0];
            } else if (element1
                    .selectFirst("img[src=https://ice.xjtlu.edu.cn/theme/image.php/boost_campus/assign/1574237233/icon]")
                    != null){
                assembled[0] = "Submission";
                assembled[2] = element1.attr("href");
            } else
                continue;
            assembled[1] = element1.selectFirst("span.instancename").text();
            result.add(assembled);
        }
        return result;
    }

    public static String parseDueDate(String input) {
        Pattern r = Pattern
                .compile("<td class=\"cell c0\" style=\"\">Due date</td>\n<td class=\"cell c1 lastcol\" style=\"\">(.*?)</td>");
        Matcher m = r.matcher(input);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    public static ArrayList<Map<String, String[]>> parseFiles(String input) {
        ArrayList<Map<String, String[]>> result = new ArrayList<>();
        return result;
    }
}
