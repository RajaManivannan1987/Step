package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.QuestionData;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.TimerClock;
import com.imaginet.ventures.step.utility.constant.ConstantValues;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class TestTutorialActivity extends YouTubeBaseActivity {
    private static String TAG = ForgotPasswordActivity.class.getSimpleName();
    private YouTubePlayerView youTubePlayerView;
    private Button submitButton;
    private int RECOVERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tutorial);

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.testTutorialActivityYouTubePlayerView);
        submitButton = (Button) findViewById(R.id.testTutorialActivitySubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServices.getInstance(TestTutorialActivity.this).startAttempt(new VolleyResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) throws JSONException {
                        if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                            QuestionData.getInstance().setJsonObject(response.getJSONObject("data"));
                            TimerClock.getInstance().setSecondRemaining(QuestionData.getInstance().getRemainingTime());
                            Intent intent = new Intent(TestTutorialActivity.this, TestActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            AlertDialogManager.showAlertDialog(TestTutorialActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                        }
                    }

                    @Override
                    public void onError(String message, String title) {
                        AlertDialogManager.showAlertDialog(TestTutorialActivity.this, title, message, false);
                    }
                }, TAG, TestTutorialActivity.this);
            }
        });
        youTubePlayerView.initialize(ConstantValues.GOOGLE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    youTubePlayer.loadVideo(ConstantValues.TUTORIAL_URL);
                    youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {

                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onStopped() {

                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {
                            Log.d(TAG, i + "");
                        }
                    });
                    youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {

                        }

                        @Override
                        public void onLoaded(String s) {
                            Log.d(TAG, s + "");
                        }

                        @Override
                        public void onAdStarted() {

                        }

                        @Override
                        public void onVideoStarted() {

                        }

                        @Override
                        public void onVideoEnded() {

                        }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {

                        }
                    });
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(TestTutorialActivity.this, RECOVERY_REQUEST).show();
                } else {

                    Toast.makeText(TestTutorialActivity.this, "Error in Video", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
