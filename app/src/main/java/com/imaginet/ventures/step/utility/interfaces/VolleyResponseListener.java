package com.imaginet.ventures.step.utility.interfaces;


import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyResponseListener {

    void onResponse(JSONObject response) throws JSONException;

    void onError(String message, String title);
}
