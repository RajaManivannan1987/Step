package com.imaginet.ventures.step.utility;

import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.util.Log;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.utility.interfaces.TimerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 9/13/16.
 */
public class TimerClock {
    private static String TAG = TimerClock.class.getSimpleName();
    private int secondsRemaining = 0, secondsTicking = 0;
    private CountDownTimer countDownTimer;
    private static TimerClock ourInstance = new TimerClock();
    private TimerInterface listener;
    private String timeString;
    private int timeDrawable;
    private int[] drawable = {
            R.drawable.timer_green,
            R.drawable.timer_grey,
            R.drawable.timer_orange,
            R.drawable.timer_red
    };

    public static TimerClock getInstance() {
        return ourInstance;
    }

    private TimerClock() {
    }

    public void addTimerInterface(TimerInterface timerInterface) {
        listener = timerInterface;
    }

    public void setSecondRemaining(int second) {
        Log.d(TAG, second + "");
        this.secondsRemaining = second;
        triggerCount();
    }


    private void triggerCount() {
        timeString = getTimeString(secondsRemaining);
        if (countDownTimer != null)
            countDownTimer.cancel();
        countDownTimer = new CountDownTimer(secondsRemaining * 1000, 1000) {
            @Override
            public void onTick(long l) {
                secondsTicking = (int) l / 1000;
                timeString = getTimeString(secondsTicking);
//                Log.d(TAG, timeString);
                if (listener != null) {
                    timeString = getTimeString(secondsTicking);
                    timeDrawable = getDrawable(secondsTicking);
//                    Log.d(TAG, timeString);
                    listener.timeRemaining(timeString, timeDrawable);
                }
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "Finished");
                secondsTicking = 0;
                timeString = getTimeString(secondsTicking);
                timeDrawable = getDrawable(secondsTicking);
                if (listener != null)
                    listener.timeRemaining(timeString, timeDrawable);
            }
        };
        countDownTimer.start();
    }

    private String getTimeString(int remainingTime) {
        int minutes = (remainingTime % 3600) / 60;
        int seconds = remainingTime % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    @DrawableRes
    private int getDrawable(int seconds) {
        if (seconds >= (20 * 60)) {
            return R.drawable.timer_green;
        }
        if (seconds >= (10 * 60)) {
            return R.drawable.timer_green;
        }
        if (seconds >= (5 * 60)) {
            return R.drawable.timer_orange;
        }
        return R.drawable.timer_red;

    }

    public String getTimeString() {
        return timeString;
    }

}
