package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.imaginet.ventures.step.common.CommonActivity;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.adapter.fragementpageadapter.DashboardFragmentPageAdapter;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.TimerClock;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.TimerInterface;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends CommonActivity {
    private static String TAG = "DashboardActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DashboardFragmentPageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_dashboard);

        tabLayout = (TabLayout) findViewById(R.id.dashboardActivityTabLayout);
        viewPager = (ViewPager) findViewById(R.id.dashboardActivityViewPager);


    }

    @Override
    protected void onResume() {
        if (Session.getInstance(this, TAG).getIsLogin())
            WebServices.getInstance(this).validate(new VolleyResponseListener() {
                @Override
                public void onResponse(JSONObject response) throws JSONException {
                    if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                        ConstantFunction.toast(DashboardActivity.this, response.getJSONObject("data").getString("resultmessage"));
                        UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                        TimerClock.getInstance().setSecondRemaining(UserData.getInstance().getRemainingTime());
                        TimerClock.getInstance().addTimerInterface(null);
                        adapter = new DashboardFragmentPageAdapter(getSupportFragmentManager());
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                    } else {
                        AlertDialogManager.showAlertDialog(DashboardActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                    }
                }

                @Override
                public void onError(String message, String title) {
                    AlertDialogManager.showAlertDialog(DashboardActivity.this, title, message, false);
                }
            }, TAG, DashboardActivity.this);
        else {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

}
