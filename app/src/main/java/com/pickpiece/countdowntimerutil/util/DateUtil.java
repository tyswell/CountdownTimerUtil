package com.pickpiece.countdowntimerutil.util;

import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by chokechaic on 6/30/2017.
 */

public class DateUtil {

    public static Long getNow() {
        TimeZone timeZone1 = TimeZone.getTimeZone("GMT+07:00");
        Calendar c = Calendar.getInstance(timeZone1);
        return c.getTimeInMillis() / 1000;
    }

}
