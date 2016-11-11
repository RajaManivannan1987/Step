package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.utility.webservice.WebServices;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "LoginActivity";
    /*private static final int RC_GET_AUTH_CODE = 9003;*/
    private Button emailButton, googleButton;
    private TextView signUpTextView, forgotPasswordTextView;
    /*private GoogleApiClient mGoogleApiClient;*/
    private WebServices webServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        webServices = WebServices.getInstance(this);

        emailButton = (Button) findViewById(R.id.loginActivityEmailButton);
        googleButton = (Button) findViewById(R.id.loginActivityGooglePlusButton);
        signUpTextView = (TextView) findViewById(R.id.loginActivityNewUserTextView);
        forgotPasswordTextView = (TextView) findViewById(R.id.loginActivityForgotPasswordTextView);

        //Google + integration starts
//        validateServerClientID();
//        String serverClientId = getString(R.string.server_client_id);
     /*   GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed:" + connectionResult);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, GooglePlusActivity.class));
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, EmailLoginActivity.class));
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* try {
            revokeAccess();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }*/
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_GET_AUTH_CODE) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            Log.d(TAG, "onActivityResult:GET_AUTH_CODE:success:" + result.getStatus().isSuccess());
//
//            if (result.isSuccess()) {
//                // [START get_auth_code]
//                GoogleSignInAccount acct = result.getSignInAccount();
//                String authCode = acct.getIdToken();
//                Log.d(TAG, "Token :" + acct.getIdToken());
//                // Show signed-in UI.
//
//
//                // [END get_auth_code]
//            } else {
//                // Show signed-out UI.
//
//            }
//        }
//    }

    //Google + integration starts
    /*private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void getAuthCode() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d(TAG, "revokeAccess:onResult:" + status);
                    }
                });
    }*/
    //Google + integration ends
}
