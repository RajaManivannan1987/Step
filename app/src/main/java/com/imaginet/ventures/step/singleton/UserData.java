package com.imaginet.ventures.step.singleton;

import android.util.Log;

import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.constant.ConstantValues;
import com.imaginet.ventures.step.utility.identifier.TestRequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IM028 on 9/8/16.
 */
public class UserData {
    private static String TAG = "UserData";
    private static UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    private JSONObject jsonObject;

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public float getOverAllLevel() {
        try {
            return Float.parseFloat(jsonObject.getJSONObject("user").getString("Olevel"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String getOverAllLevelText() {
        try {
            if (Float.parseFloat(jsonObject.getJSONObject("user").getString("Olevel")) == 0) {
                return "GO";
            }
            return String.format("%.1f", Float.parseFloat(jsonObject.getJSONObject("user").getString("Olevel")));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "0";
        }
    }

    public String getAccountType() {
        try {
            return jsonObject.getJSONObject("user").getString("accounttype");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public float getReadLevel() {
        try {
            return Float.parseFloat(jsonObject.getJSONObject("user").getString("Rlevel"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String getReadLevelText() {
        try {
            return String.format("%.1f", Float.parseFloat(jsonObject.getJSONObject("user").getString("Rlevel")));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "0";
        }
    }

    public float getWriteLevel() {
        try {
            return Float.parseFloat(jsonObject.getJSONObject("user").getString("Wlevel"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String getWriteLevelText() {
        try {
            return String.format("%.1f", Float.parseFloat(jsonObject.getJSONObject("user").getString("Wlevel")));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "0";
        }
    }

    public float getListenLevel() {
        try {
            return Float.parseFloat(jsonObject.getJSONObject("user").getString("Llevel"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String getListenLevelText() {
        try {
            return String.format("%.1f", Float.parseFloat(jsonObject.getJSONObject("user").getString("Llevel")));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "0";
        }
    }

    public float getSpeakLevel() {
        try {
            return Float.parseFloat(jsonObject.getJSONObject("user").getString("Slevel"));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public int getProgressBar() {
        try {
            return jsonObject.getJSONObject("user").getInt("progressbar");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public float getAverage() {
        try {
            return Float.parseFloat(String.format("%.2f", Float.parseFloat(jsonObject.getJSONObject("user").getJSONObject("snapshot").getString("overallaverage"))));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return 0;
        }
    }

    public String getRank() {
        try {
            return jsonObject.getJSONObject("user").getString("rank");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String getRankText() {
        try {
            return jsonObject.getJSONObject("user").getString("ranktext");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String getOverAllText() {
        try {
            return jsonObject.getJSONObject("user").getString("overalltext");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String getStrengthText() {
        try {
            return jsonObject.getJSONObject("user").getString("strengthtext");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String getWeaknessText() {
        try {
            return jsonObject.getJSONObject("user").getString("weaknesstext");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "";
        }
    }

    public String checkButtonBeginContinue() {
        try {
            return (jsonObject.getJSONObject("user").getJSONArray("attempts").length() > 0) ? "Continue" : "Begin";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Begin";
    }

    public TestRequestType getTestRequest() throws Exception {
        JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONArray("attempts");
        if (jsonArray.length() == 0) {
            return TestRequestType.BEGIN;
        }
        if (jsonArray.getJSONObject(jsonArray.length() - 1).getString("submittimestamp").equalsIgnoreCase("0000-00-00 00:00:00")) {
            if (jsonArray.getJSONObject(jsonArray.length() - 1).getInt("timeremaininginseconds") > 0)
                return TestRequestType.INPROGRESS;
            else
                return TestRequestType.INCOMPLETED;
        } else {
            return TestRequestType.COMPLETED;
        }
    }

    public String getLastAttempt() {
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONArray("attempts");
            return jsonArray.getJSONObject(jsonArray.length() - 1).getString("attemptID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getRemainingTime() {
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONArray("attempts");
            return jsonArray.getJSONObject(jsonArray.length() - 1).getInt("timeremaininginseconds");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getLicenceKey() {

        try {
            JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONArray("licensekeys");
            return jsonArray.getJSONObject(jsonArray.length() - 1).getString("key");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getOrganizationLicenceKey() {

        try {
            JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONArray("licensekeys");
            return jsonArray.getJSONObject(jsonArray.length() - 1).getString("organisation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getExpiryLicenceKey() {
        try {
            String expirydate = jsonObject.getJSONObject("user").getString("expirydate");
            Date date = ConstantValues.serverFormat.parse(expirydate);
            return ConstantValues.appDate.format(date);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getUserName() {
        try {
            if (jsonObject.getJSONObject("user").getString("fullname") != null)
                return jsonObject.getJSONObject("user").getString("fullname");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserPhone() {
        try {
            if (jsonObject.getJSONObject("user").getString("phone") != null)
                return jsonObject.getJSONObject("user").getString("phone");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserEmail() {
        try {
            return jsonObject.getJSONObject("user").getString("email");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }


    public String getUserDOB() {
        try {
            if (!jsonObject.getJSONObject("user").getString("birthdate").equalsIgnoreCase("0000-00-00"))
                return jsonObject.getJSONObject("user").getString("birthdate");
            else
                return "";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserGender() {
        try {
            return jsonObject.getJSONObject("user").getString("gender");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "M";
    }

    public String getUserHomeLanguage() {
        try {
            if (jsonObject.getJSONObject("user").getString("homelanguage") != null)
                return jsonObject.getJSONObject("user").getString("homelanguage");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserYearsOfSchooling() {
        try {
            if (jsonObject.getJSONObject("user").getString("yearsofschooling") != null)
                return jsonObject.getJSONObject("user").getString("yearsofschooling");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserNumberOfLangsSpeakOrUnderstand() {
        try {
            if (jsonObject.getJSONObject("user").getString("numberoflangsspeakorunderstand") != null)
                return jsonObject.getJSONObject("user").getString("numberoflangsspeakorunderstand");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserNumberOfLanguagesReadAndWrite() {
        try {
            if (jsonObject.getJSONObject("user").getString("numberoflanguagesreadandwrite") != null)
                return jsonObject.getJSONObject("user").getString("numberoflanguagesreadandwrite");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

    public String getUserCommonMedium() {
        try {
            if (jsonObject.getJSONObject("user").getString("mostcommonmediumofinstruction") != null)
                return jsonObject.getJSONObject("user").getString("mostcommonmediumofinstruction");
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return "";
    }

}
