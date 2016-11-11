package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class SignUpActivity extends AppCompatActivity {
    private static String TAG = SignUpActivity.class.getSimpleName();
    private EditText emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    private Button submitButton;
    private TextView alreadyAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailEditText = (EditText) findViewById(R.id.signUpActivityEmailEditText);
        phoneEditText = (EditText) findViewById(R.id.signUpActivityPhoneEditText);
        passwordEditText = (EditText) findViewById(R.id.signUpActivityPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.signUpActivityConfirmPasswordEditText);
        submitButton = (Button) findViewById(R.id.signUpActivitySubmitButton);
        alreadyAccountTextView = (TextView) findViewById(R.id.signUpActivityAlreadyTextView);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.emailValidation(emailEditText.getText().toString())) {
                    emailEditText.setError(null);
                    if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                        if (Validation.passwordValidation(passwordEditText.getText().toString())) {
                            WebServices.getInstance(SignUpActivity.this).registration(emailEditText.getText().toString(),
                                    passwordEditText.getText().toString(),
                                    confirmPasswordEditText.getText().toString(),
                                    phoneEditText.getText().toString(),
                                    new VolleyResponseListener() {
                                        @Override
                                        public void onResponse(JSONObject response) throws JSONException {
                                            if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                                emailEditText.setText("");
                                                passwordEditText.setText("");
                                                confirmPasswordEditText.setText("");
                                                phoneEditText.setText("");
                                                AlertDialogManager.showAlertDialog(SignUpActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), true);
                                            } else {
                                                AlertDialogManager.showAlertDialog(SignUpActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                                            }
                                        }

                                        @Override
                                        public void onError(String message, String title) {
                                            AlertDialogManager.showAlertDialog(SignUpActivity.this, title, message, false);

                                        }
                                    }, TAG, SignUpActivity.this);
                        } else {
                            confirmPasswordEditText.setError("Enter valid password");
                            passwordEditText.setError("Enter valid password");
                            passwordEditText.requestFocus();
                        }
                    } else {
                        confirmPasswordEditText.setError("Password not matched");
                        passwordEditText.setError("Password not matched");
                        confirmPasswordEditText.requestFocus();
                    }
                } else {
                    emailEditText.setError("Enter valid email");
                    emailEditText.requestFocus();
                }
            }
        });
        alreadyAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
