package com.domzky.gymbooking.Helpers.FieldSyntaxes;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimeHelper {

    public String getNowDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public String getNowTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
        return sdf.format(date);
    }

    public String getNowDateTime() {
        Date datetime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.getDefault());
        return sdf.format(datetime);
    }

    public Boolean dateHasBeenPassed(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date thisdate = null;
        try {
            thisdate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().after(thisdate);
    }

    public Boolean datetimeHasBeenPassed(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.getDefault());
        Date thisdatetime = null;
        try {
            thisdatetime = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().after(thisdatetime);
    }

    public Boolean frstDateHasBeenPassedThanSecondOne(String date1,String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date thisdate1 = null;
        Date thisdate2 = null;
        try {
            thisdate1 = sdf.parse(date1);
            thisdate2 = sdf.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisdate1.after(thisdate2);
    }

    public Boolean frstDatetimeHasBeenPassedThanSecondOne(String datetime1,String datetime2) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.getDefault());
        Date thisdatetime1 = null;
        Date thisdatetime2 = null;
        try {
            thisdatetime1 = sdf.parse(datetime1);
            thisdatetime2 = sdf.parse(datetime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisdatetime1.after(thisdatetime2);
    }

    public Date convertDateStringToDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        Date thisdate = null;
        try {
            thisdate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisdate;
    }

    public Date convertDatetimeStringToDatetime(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.getDefault());
        Date thisdate = null;
        try {
            thisdate = sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return thisdate;
    }

}
