package com.imaginet.ventures.step.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class GooglePlusActivity extends AppCompatActivity {
    private static String TAG = GooglePlusActivity.class.getSimpleName();
    private WebView webView;
    private WebServices webServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_plus);

        webServices = WebServices.getInstance(this);
        webView = (WebView) findViewById(R.id.googlePlusActivityWebView);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                GooglePlusActivity.this.setProgress(progress * 1000);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(GooglePlusActivity.this, "Try again" + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("WEBVIEW", url);
                webView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "('<body>'+document.getElementsByTagName('html')[0].innerHTML+'</body>');");
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
        webServices.googlePlusLogin(new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                webView.loadUrl(response.getJSONObject("data").getString("authURL"));
            }

            @Override
            public void onError(String message, String title) {

            }
        }, TAG, this);


    }

    class MyJavaScriptInterface {
        private Context ctx;
        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }
        @JavascriptInterface
        public void showHTML(String html) {
            String json = html.replaceAll("</body></body>", "").replaceAll("<body><head></head><body>", "");
            Log.d(GooglePlusActivity.TAG, json);
            try {
                final JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean("success") && jsonObject.getJSONObject("data").getInt("resultcode") == 200) {
                    WebServices.getInstance(GooglePlusActivity.this).validate(jsonObject.getJSONObject("data").getString("token"), new VolleyResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                ConstantFunction.toast(GooglePlusActivity.this, response.getJSONObject("data").getString("resultmessage"));
                                Session.getInstance(GooglePlusActivity.this, TAG).createSession(response.getJSONObject("data").getJSONObject("user").getString("fullname"), jsonObject.getJSONObject("data").getString("token"));
                                UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                                Intent intent = new Intent(GooglePlusActivity.this, DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialogManager.showAlertDialog(GooglePlusActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(GooglePlusActivity.this, title, message, false);
                        }
                    }, TAG, GooglePlusActivity.this);
                }
            } catch (Exception e) {
                Log.e(GooglePlusActivity.TAG, e.toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            webView.clearCache(true);
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        super.onDestroy();
    }
}

