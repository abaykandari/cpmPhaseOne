package com.incture.cpm.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateFormatter {
    public static String formatDateWithSuffix(LocalDate date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("d")
                .appendLiteral(' ')
                .appendPattern("MMMM, yyyy")
                .toFormatter();

        int day = date.getDayOfMonth();
        String dayWithSuffix = addDaySuffix(day);
        String formattedDate = date.format(formatter);

        return dayWithSuffix + " " + formattedDate.split(" ", 2)[1];
    }

    private static String addDaySuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1:
                return day + "st";
            case 2:
                return day + "nd";
            case 3:
                return day + "rd";
            default:
                return day + "th";
        }
    }
}
