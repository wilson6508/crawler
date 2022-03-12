package com.tool;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocalDateTool {

    private final DateTimeFormatter mdy = DateTimeFormat.forPattern("MMM dd, yyyy").withLocale(Locale.US);

    public LocalDate getObjByString(String string) {
        return mdy.parseLocalDate(string);
    }

}
