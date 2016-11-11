package com.imaginet.ventures.step.singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 9/12/16.
 */
public class QuestionData {
    private JSONObject jsonObject;
    private static QuestionData ourInstance = new QuestionData();

    public static QuestionData getInstance() {
        return ourInstance;
    }

    private QuestionData() {
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getVideoURL() throws JSONException {
        if (jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo") != null && !jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo").equalsIgnoreCase(""))
            return jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo");
        else
            return null;
    }

    public int getStartTime() throws JSONException {
        if (jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo") != null && !jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo").equalsIgnoreCase(""))
            return jsonObject.getJSONArray("questions").getJSONObject(0).getInt("youtubeStart") * 1000;
        else
            return 0;
    }

    public int getEndTime() throws JSONException {
        if (jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo") != null && !jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceYoutubeVideo").equalsIgnoreCase(""))
            return jsonObject.getJSONArray("questions").getJSONObject(0).getInt("youtubeEnd") * 1000;
        else
            return 0;
    }

    public String getImageURL() throws JSONException {
        if (jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceImageURL") != null && !jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceImageURL").equalsIgnoreCase(""))
            return jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourceImageURL");
        else
            return null;
    }

    public String getTextQuestion() throws JSONException {
        if (jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourcetextOrHTML") != null && !jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourcetextOrHTML").equalsIgnoreCase(""))
            return jsonObject.getJSONArray("questions").getJSONObject(0).getString("sourcetextOrHTML");
        else
            return null;
    }

    public JSONArray getQuestion() throws JSONException {
        return jsonObject.getJSONArray("questions");
    }

    public int getCountAnswerSubmitted() {
        try {
            return jsonObject.getJSONObject("attempt").getJSONArray("answers").length();
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getRemainingTime() {
        try {
            return jsonObject.getJSONObject("attempt").getInt("timeremaininginseconds");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getAttemptId() {
        try {
            return jsonObject.getJSONObject("attempt").getString("attemptID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String[] getQuestionId() {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            String[] result = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                result[i] = jsonArray.getJSONObject(i).getString("questionID");
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String[0];
    }
}
