package com.imaginet.ventures.step.utility.sharedpreferrence;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IM028 on 9/14/16.
 */
public class VideoPlayed {
    private String TAG;
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static String PREF_NAME = "stepVideoPlayed";
    private static String videoPlayedKey = "videoPlayedKey";
    private static String videoPlayedUrl = "videoPlayedUrl";
    private static String attemptId = "attemptId";

    private VideoPlayed(Context context, String TAG) {
        this.context = context;
        this.TAG = "Session " + TAG;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    private static VideoPlayed videoPlayed;

    public static VideoPlayed getInstance(Context context, String TAG) {
        if (videoPlayed == null) {
            videoPlayed = new VideoPlayed(context, TAG);
        }
        return videoPlayed;
    }

    public void setVideo(String url, int time, String attemptId) {
        editor.putString(videoPlayedUrl, url);
        editor.putString(this.attemptId, attemptId);
        editor.putInt(videoPlayedKey, time);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public int getTime() {
        return pref.getInt(videoPlayedKey, 0);
    }

    public String getUrl() {
        return pref.getString(videoPlayedUrl, "");
    }

    public String getAttemptId() {
        return pref.getString(attemptId, "");
    }
}
