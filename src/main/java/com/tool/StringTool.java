package com.tool;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

@Service
public class StringTool {

    // date 10/01 , return 2021-10-01
    public String transDate(String date) {
        LocalDate localDate = new LocalDate();
        if (date.startsWith("12")) {
            localDate = new LocalDate().plusMonths(-1);
        }
        String temp = localDate.getYear() + "/" + date;
        return temp.replace("/", "-");
    }

    public String getLastMonth() {
        LocalDate localDate = new LocalDate().plusMonths(-1);
        return localDate.getYear() + "-" + localDate.getMonthOfYear();
    }

    public List<String> getPlaySport(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByClass("predictgame-table");
            Elements trArray = elements.get(0).getElementsByTag("tr");
            List<String> list = new ArrayList<>();
            for (int i = 0 ; i < trArray.size() ; i++) {
                if (i < 2) {
                    continue;
                }
                Element trTags = trArray.get(i);
                String team = trTags.getElementsByTag("h3").text();
                String data = trTags.getElementsByTag("strong").text();
                list.add(team + data);
            }
            list.remove("預測賽事請先登入");
            list.removeAll(list.stream().filter(e->e.equals("")).collect(toList()));
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSportDate(boolean boo) {
        LocalDate target = new LocalDate().plusDays(boo ? 1 : 0);
        HashMap<Integer, String> hashMap = new HashMap<>();
        hashMap.put(1, " (一)");
        hashMap.put(2, " (二)");
        hashMap.put(3, " (三)");
        hashMap.put(4, " (四)");
        hashMap.put(5, " (五)");
        hashMap.put(6, " (六)");
        hashMap.put(7, " (七)");
        return target.toString().substring(5).replace("-", "/") + hashMap.get(target.getDayOfWeek());
    }

    public String getCurrentTime(String type) {
        String current = "";
        LocalDate localDate = new LocalDate();
        DateTime dateTime = new DateTime();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        switch (type) {
            case "date": {
                current = localDate.toString(); // 2022-03-22
                break;
            }
            case "time": {
                current = timestamp.toString().substring(0, 19); // 2022-03-22 13:56:56
                break;
            }
            case "dayTw": {
                current = localDate + " " + dateTime.dayOfWeek().getAsText(); // 2022-03-22 星期二
                break;
            }
            case "dayUsShort": {
                current = localDate + " " + dateTime.dayOfWeek().getAsShortText(Locale.US); // 2022-03-22 Tue
                break;
            }
            case "dayUsLong": {
                current = localDate + " " + dateTime.dayOfWeek().getAsText(Locale.US); // 2022-03-22 Tuesday
                break;
            }
        }
        return current;
    }

}
