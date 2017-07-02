package com.pickpiece.countdowntimerutil.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;

/**
 * Created by chokechaic on 6/30/2017.
 */

public class CountDownUtil {

    private enum TimerState {
        STOPPED,
        RUNNING
    }

    private OnCountEvent onCountEvent;

    private TimerState state;
    private long timeToGo;
    private Long timerLength;
    private CountDownTimer countDownTimer;
    private Activity activity;
    private String packagePref;
    private Intent expireReciever;

    PrefUtils mPreferences;

    public CountDownUtil(Long timerLength, Activity activity, String packagePref, Intent expireReciever) {
        this.timerLength = timerLength;
        this.activity = activity;
        this.packagePref = packagePref;
        mPreferences = new PrefUtils(activity);
        this.expireReciever = expireReciever;
    }

    public void initTimer() {
        long startTime = mPreferences.getStartedTime(packagePref);
        if (startTime > 0) {
            timeToGo = (timerLength - (DateUtil.getNow() - startTime));
            if (timeToGo <= 0) { // TIMER EXPIRED
                timeToGo = timerLength;
                state = TimerState.STOPPED;
                onCountEvent.onTimerFinish();
            } else {
                startTimer();
                state = TimerState.RUNNING;
            }
        } else {
            timeToGo = timerLength;
            state = TimerState.STOPPED;
        }
    }

    public void startTimer() {
        mPreferences.setStartedTime(packagePref, DateUtil.getNow());
        state = TimerState.RUNNING;
        countDownTimer = new CountDownTimer(timeToGo * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                timeToGo -= 1;
                onCountEvent.onTickEvent();
            }
            public void onFinish() {
                state = TimerState.STOPPED;
                onCountEvent.onTimerFinish();
            }
        }.start();
    }

    public void pauseTimer() {
        if (state == TimerState.RUNNING) {
            countDownTimer.cancel();
            setAlarm();
        }
    }

    public long getTimeToGo() {
        return timeToGo;
    }

    public void setOnCountEvent(OnCountEvent onCountEvent) {
        this.onCountEvent = onCountEvent;
    }

    public interface  OnCountEvent {
        void onTimerFinish();

        void onTickEvent();
    }

    public void removeAlarm() {
        PendingIntent sender = PendingIntent.getBroadcast(activity, 0, expireReciever, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    private void setAlarm() {
        long wakeUpTime = (mPreferences.getStartedTime(packagePref) + timerLength) * 1000;
        final int _id = (int) System.currentTimeMillis();
        AlarmManager am = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(activity, _id, expireReciever, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            am.setAlarmClock(new AlarmManager.AlarmClockInfo(wakeUpTime, sender), sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
        }
    }

}
