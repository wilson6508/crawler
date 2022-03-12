package com.tool;

import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

}
