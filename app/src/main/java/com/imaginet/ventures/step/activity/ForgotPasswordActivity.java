package com.imaginet.ventures.step.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.Validation;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static String TAG = ForgotPasswordActivity.class.getSimpleName();
    private EditText emailEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.forgotPasswordActivityEmailEditText);
        submitButton = (Button) findViewById(R.id.forgotPasswordActivitySubmitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.emailValidation(emailEditText.getText().toString())) {
                    emailEditText.setError(null);
                    WebServices.getInstance(ForgotPasswordActivity.this).forgotPassword(emailEditText.getText().toString(), new VolleyResponseListener() {
                        @Override
                        public void onResponse(JSONObject response) throws JSONException {
                            if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), true);
                                emailEditText.setText("");
                            } else {
                                AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                            }
                        }

                        @Override
                        public void onError(String message, String title) {
                            AlertDialogManager.showAlertDialog(ForgotPasswordActivity.this, title, message, false);
                        }
                    }, TAG, ForgotPasswordActivity.this);
                } else {
                    emailEditText.setError("Enter valid email");
                    emailEditText.requestFocus();
                }
            }
        });


    }
}
