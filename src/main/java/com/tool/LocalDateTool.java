package com.tool;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Locale;

@Service
public class LocalDateTool {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMM dd, yyyy").withLocale(Locale.US);

    /**
     * 轉換日期
     * @param string "Mar 25, 2022"
     * @return "2022-03-25"
     */
    public String getDateByString(String string) {
        return dateTimeFormatter.parseLocalDate(string).toString();
    }

    /**
     * 轉換日期
     * @param date "10/01"
     * @return "2021-10-01"
     */
    public String getDate(String date) {
        LocalDate localDate = new LocalDate();
        if (date.startsWith("12")) {
            localDate = new LocalDate().plusMonths(-1);
        }
        String temp = localDate.getYear() + "/" + date;
        return temp.replace("/", "-");
    }

    /**
     * 取得當下時間
     * @param type
     * @return
     */
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
