package com.pimo.thea.vaccinesschedulemvp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by thea on 7/6/2017.
 */

public class DateTimeHelper {
    public static final int NOT_WEEK = 0;
    public static final int ONE_WEEK = 1;
    public static final int TWO_WEEKS = 2;
    public static final int THREE_WEEKS = 3;
    public static final int ONE_MONTH = 4;
    public static final int TWO_MONTHS = 8;
    public static final int THREE_MONTHS = 12;
    public static final int FOUR_MONTHS = 16;
    public static final int FIVE_MONTHS = 20;
    public static final int NINE_MONTHS = 36;           // 9 months
    public static final int ONE_YEAR = 48;              // 12 months
    public static final int ONE_YEAR_ONE_MONTH = 52;    // 13 months
    public static final int ONE_YEAR_TWO_MONTH = 56;    // 14 months
    public static final int ONE_YEAR_SIX_MONTHS = 72;   // 18 months
    public static final int TWO_YEARS = 96;             // 24 months
    public static final int THREE_YEARS = 144;          // 36 months
    public static final int FIVE_YEARS = 240;           // 60 months

    public static SimpleDateFormat sdfTimeHHmm = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static SimpleDateFormat sdfDateFull = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    public static SimpleDateFormat sdfTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);

    public static long getCurrentToDayAt7Clock() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 7);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long convertToAt7ClockBeforeThatADay(long dateTime) {
        Calendar cld = Calendar.getInstance();
        cld.setTimeInMillis(dateTime);
        int beforeThatADay = cld.get(Calendar.DAY_OF_YEAR) - 1;
        cld.set(Calendar.DAY_OF_YEAR, beforeThatADay);
        return cld.getTimeInMillis();
    }

    public static long setTimeForDayInjection(long inputDateTime, int condition) {
        long dayInjection = 0;
        switch (condition) {
            // New born = 0 weeks
            case NOT_WEEK:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 0);
                break;
            // 1 week
            case ONE_WEEK:
                break;
            // 2 weeks
            case TWO_WEEKS:
                dayInjection = calculatorDayInjectionFollowWeek(inputDateTime, 2);
                break;
            // 3 weeks
            case THREE_WEEKS:
                // 2 months = 8 weeks
            case TWO_MONTHS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 2);
                break;
            // 3 months = 12 weeks
            case THREE_MONTHS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 3);
                break;
            // 4 months = 16 weeks
            case FOUR_MONTHS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 4);
                break;
            // 5 months = 20 weeks
            case FIVE_MONTHS:
                break;
            // 9 months = 36 weeks
            case NINE_MONTHS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 9);
                break;
            // 1 year = 12 months = 48 weeks
            case ONE_YEAR:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 12);
                break;
            // 1 year 6 moths = 18 months = 72 weeks
            case ONE_YEAR_SIX_MONTHS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 18);
                break;
            // 2 years = 24 months = 96 weeks
            case TWO_YEARS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 24);
                break;
            // 3 years = 36 months = 144 weeks
            case THREE_YEARS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 36);
                break;
            // 5 years = 60 months = 240 weeks
            case FIVE_YEARS:
                dayInjection = calculatorDayInjectionFollowMonthAndYear(inputDateTime, 60);
                break;
        }
        return dayInjection;
    }

    private static long calculatorDayInjectionFollowWeek(long inputDateTime, int addToNextWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputDateTime);
        int currentWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        int nextWeek = currentWeek + addToNextWeek;
        calendar.set(Calendar.WEEK_OF_MONTH, nextWeek);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private static long calculatorDayInjectionFollowMonthAndYear(long inputDateTime, int addToNextMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputDateTime);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // The month between 0-11.
        int month = calendar.get(Calendar.MONTH);
        int nextMonth = month + addToNextMonth;
        int nextDay = currentDay;

        if (currentDay == 31) {
            if (nextMonth == 3 || nextMonth == 5 || nextMonth == 8 || nextMonth == 10) {
                nextDay = 30;
            } else if (nextMonth == 1) {
                nextDay = 28;
            } else {
                nextDay = currentDay;
            }
        } else if (currentDay == 30) {
            if (nextMonth == 1) {
                nextDay = 28;
            } else {
                nextDay = currentDay;
            }
        }

        calendar.set(Calendar.DAY_OF_MONTH, nextDay);
        calendar.set(Calendar.MONTH, nextMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    // Method is used for filter injection schedules in injection_schedule table
    // so using childCode instead of dateOfBirth
    public static int calculatorAgeMonthFollowDayInjection(long dataOfBirth, long dayInjection) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dataOfBirth);
        int dobYear = calendar.get(Calendar.YEAR);
        //the month between 0-11.
        int dobMonth = calendar.get(Calendar.MONTH) + 1;
        int dobDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(dayInjection);
        int djYear = calendar.get(Calendar.YEAR);
        //the month between 0-11.
        int djMonth = calendar.get(Calendar.MONTH) + 1;
        int djDay = calendar.get(Calendar.DAY_OF_MONTH);

        int offSetYear = djYear - dobYear;
        if (offSetYear < 0) {
            return -1;
        } else if (offSetYear == 0) {
            int offSetMonth = djMonth - dobMonth;
            if (offSetMonth < 0) {
                return -1;
            } else if (offSetMonth == 0) {
                return NOT_WEEK;
            } else {
                return offSetMonth;
            }
        } else {
            int offSetMonth = 12 * (offSetYear - 1);
            return (12 - dobMonth + offSetMonth + djMonth);
        }
    }
}
