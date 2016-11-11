package com.imaginet.ventures.step.utility.webservice;

import android.content.Context;


import com.imaginet.ventures.step.utility.constant.ConstantValues;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IM028 on 8/2/16.
 */
public class WebServices {


    private VolleyClass volleyClass;
    private String controller = "step";
    private static WebServices webServices;

    public static WebServices getInstance(Context context) {
        if (webServices == null) {
            webServices = new WebServices(context);
        }
        return webServices;
    }

    private WebServices(Context context) {

        volleyClass = new VolleyClass(context);
    }

    public void login(String email, String password, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "login");
        prams.put("email", email);
        prams.put("pass", password);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void logout(VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "logout");
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void validate(VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "validateUser");
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void validate(String token, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "validateUser");
        prams.put("token", token);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void googlePlusLogin(VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "loginSocial");
        prams.put("socialtype", "GooglePlus");
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void registration(String email, String password, String confirmPass, String phoneNo, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "register");
        prams.put("email", email);
        prams.put("pass", password);
        prams.put("confirmpass", confirmPass);
        prams.put("phone", phoneNo);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void forgotPassword(String email, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "getPasswordResetCode");
        prams.put("email", email);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void startAttempt(VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "startAttempt");
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void restartAttempt(String attemptId, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "restartAttempt");
        prams.put("attemptID", attemptId);
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void submit(String[] questionID, String[] useranswer, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "submitAnswers");
        for (int i = 0; i < questionID.length; i++) {
            prams.put("questionID[" + i + "]", questionID[i]);
        }
        for (int i = 0; i < useranswer.length; i++) {
            prams.put("useranswer[" + i + "]", useranswer[i]);
        }
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void resetPassword(String email, String code, String pass, String confirmpass, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "resetPassword");
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        prams.put("email", email);
        prams.put("code", code);
        prams.put("pass", pass);
        prams.put("confirmpass", confirmpass);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void applyLicenseKey(String licensekey, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "applyLicenseKey");
        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        prams.put("licensekey", licensekey);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }

    public void editUser(String phone, String fullname, String gender, String birthdate, String photoURL, String yearsofschooling,
                         String mostcommonmediumofinstruction, String homelanguage, String numberoflangspeakorunderstand,
                         String numberoflanguagesreadandrewrite, String SES, String[] themes, VolleyResponseListener listener, String TAG, Context context) {
        Map<String, String> prams = new HashMap<String, String>();
        prams.put("controller", controller);
        prams.put("action", "editUser");

        prams.put("token", Session.getInstance(context, "WebServices").getToken());
        prams.put("phone", phone);
        prams.put("fullname", fullname);
        prams.put("gender", gender);
        prams.put("birthdate", birthdate);
        prams.put("photoURL", photoURL);
        prams.put("yearsofschooling", yearsofschooling);
        prams.put("mostcommonmediumofinstruction", mostcommonmediumofinstruction);
        prams.put("homelanguage", homelanguage);
        prams.put("numberoflangsspeakorunderstand", numberoflangspeakorunderstand);
        prams.put("numberoflanguagesreadandwrite", numberoflanguagesreadandrewrite);
        prams.put("SES", SES);
        for (int i = 0; i < themes.length; i++)
            prams.put("themes[" + i + "]", themes[i]);
        volleyClass.volleyFormPost(ConstantValues.SERVER_URL, prams, listener, TAG + " WebServices", context);
    }
}
