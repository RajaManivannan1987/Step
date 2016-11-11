package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.Validation;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class EmailLoginActivity extends AppCompatActivity {
    private static String TAG = "EmailLoginActivity";
    private EditText emailEditText, passwordEditText;
    private Button submitButton;
    private WebServices webServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        webServices = WebServices.getInstance(this);

        emailEditText = (EditText) findViewById(R.id.emailLoginActivityEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.emailLoginActivityPasswordEditText);
        submitButton = (Button) findViewById(R.id.emailLoginActivitySubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.emailValidation(emailEditText.getText().toString())) {
                    emailEditText.setError(null);
                    if (Validation.passwordValidation(passwordEditText.getText().toString())) {
                        passwordEditText.setError(null);
                        submit(emailEditText.getText().toString(), passwordEditText.getText().toString());
                    } else {
                        passwordEditText.setError("Please enter password");
                        passwordEditText.requestFocus();
                    }
                } else {
                    emailEditText.setError("Please enter valid Email ID");
                    emailEditText.requestFocus();
                }
            }
        });
    }

    private void submit(String email, String password) {
        webServices.login(email, password, new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                    ConstantFunction.toast(EmailLoginActivity.this, response.getJSONObject("data").getString("resultmessage"));
                    Session.getInstance(EmailLoginActivity.this, TAG).createSession(response.getJSONObject("data").getJSONObject("user").getString("fullname"), response.getJSONObject("data").getString("token"));
                    UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                    Intent intent = new Intent(EmailLoginActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    AlertDialogManager.showAlertDialog(EmailLoginActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(EmailLoginActivity.this, title, message, false);

            }
        }, TAG, EmailLoginActivity.this);
    }
}
