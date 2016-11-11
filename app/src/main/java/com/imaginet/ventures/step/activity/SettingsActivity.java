package com.imaginet.ventures.step.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.adapter.fragementpageadapter.DashboardFragmentPageAdapter;
import com.imaginet.ventures.step.common.CommonActivity;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.TimerClock;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends CommonActivity {
    private static String TAG = SettingsActivity.class.getSimpleName();
    private TextView licenceKeyTextView, organisationTextView, expiryDateTextView;
    private Button addLicenceKeyButton;
    private EditText addLicenceKeyEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_settings);
        setTitle("Settings");

        licenceKeyTextView = (TextView) findViewById(R.id.settingsActivityLicenceKeyTextView);
        organisationTextView = (TextView) findViewById(R.id.settingsActivityOrganisationTextView);
        expiryDateTextView = (TextView) findViewById(R.id.settingsActivityExpiryDateTextView);
        addLicenceKeyButton = (Button) findViewById(R.id.settingsActivityNewLicenceButton);
        addLicenceKeyEditText = (EditText) findViewById(R.id.settingsActivityNewLicenceEditText);
        addLicenceKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addLicenceKeyEditText.getText().toString().length() > 0) {
                    addLicenceKeyEditText.setError(null);
                    submit();
                } else {
                    addLicenceKeyEditText.setError("Enter Valid Licence Key");
                    addLicenceKeyEditText.requestFocus();
                }
            }
        });

    }

    private void update() {
        licenceKeyTextView.setText(UserData.getInstance().getLicenceKey());
        organisationTextView.setText(UserData.getInstance().getOrganizationLicenceKey());
        expiryDateTextView.setText(UserData.getInstance().getExpiryLicenceKey());
    }

    @Override
    protected void onResume() {
        update();
        super.onResume();
    }

    private void submit() {

        WebServices.getInstance(SettingsActivity.this).applyLicenseKey(addLicenceKeyEditText.getText().toString(), new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                    ConstantFunction.toast(SettingsActivity.this, response.getJSONObject("data").getString("resultmessage"));
                    WebServices.getInstance(SettingsActivity.this).validate(new VolleyResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {

                                UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                                update();
                            } else {
                                AlertDialogManager.showAlertDialog(SettingsActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(SettingsActivity.this, title, message, false);
                        }
                    }, TAG, SettingsActivity.this);
                } else {
                    AlertDialogManager.showAlertDialog(SettingsActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(SettingsActivity.this, title, message, false);
            }
        }, TAG, SettingsActivity.this);
    }
}
