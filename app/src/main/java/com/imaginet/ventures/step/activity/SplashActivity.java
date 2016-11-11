package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private static String TAG = SplashActivity.class.getSimpleName();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Session.getInstance(SplashActivity.this, "").getIsLogin())
                    WebServices.getInstance(SplashActivity.this).validate(new VolleyResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                ConstantFunction.toast(SplashActivity.this, response.getJSONObject("data").getString("resultmessage"));
                                UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                AlertDialogManager.showAlertDialog(SplashActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(SplashActivity.this, title, message, false);
                        }
                    }, TAG, SplashActivity.this);
                else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
