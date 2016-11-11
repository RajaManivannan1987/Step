package com.imaginet.ventures.step.utility.webservice;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class VolleyClass {
    private Context act;
//    String networkErrorMessage = "Network error – please try again.";
    String networkErrorMessage = "This application need an internet connection.";
    String poorNetwork = "Your data connection is too slow – please try again when you have a better network connection";
    String timeout = "Response timed out – please try again.";
    String authorizationFailed = "Authorization failed – please try again.";
    String serverNotResponding = "Server not responding – please try again.";
    String parseError = "Data not received from server – please try again.";

    String networkErrorTitle = "Network error";
    String poorNetworkTitle = "Poor Network Connection";
    String timeoutTitle = "Network Error";
    String authorizationFailedTitle = "Network Error";
    String serverNotRespondingTitle = "Server Error";
    String parseErrorTitle = "Network Error";
    RequestQueue queue;

    public VolleyClass(Context context) {
        this.act = context;
        queue = Volley.newRequestQueue(context);
    }

    public void volleyFormPost(String url, final Map<String, String> params, final VolleyResponseListener listener,final String TAG,Context context) {
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Log.d(TAG, "volleyPostData request url - " + url);
        Log.d(TAG, "volleyPostData request data - " + params.toString());
        if (isOnline()) {
            try {
                pDialog.show();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "volleyPostData response - " + response);
                            try {
                                listener.onResponse(new JSONObject(response));
                            } catch (JSONException e) {
                                Log.d(TAG, e.getMessage());
                            }
                            try {
                                pDialog.dismiss();
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        pDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (error instanceof TimeoutError) {
                        listener.onError(timeout, timeoutTitle);
                    } else if (error instanceof NoConnectionError) {
                        listener.onError(poorNetwork, poorNetworkTitle);
                    } else if (error instanceof AuthFailureError) {
                        listener.onError(authorizationFailed, authorizationFailedTitle);
                    } else if (error instanceof ServerError) {
                        listener.onError(serverNotResponding, serverNotRespondingTitle);
                    } else if (error instanceof NetworkError) {
                        listener.onError(networkErrorMessage, networkErrorTitle);
                    } else if (error instanceof ParseError) {
                        listener.onError(parseError, parseErrorTitle);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            int MY_SOCKET_TIMEOUT_MS = 30000;

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjReq);
        } else {
            Log.d(TAG, "volleyPostData response - No Internet");
            listener.onError(networkErrorMessage, networkErrorMessage);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
