package com.imaginet.ventures.step.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.adapter.fragementpageadapter.QuestionsFragmentPageAdapter;
import com.imaginet.ventures.step.singleton.QuestionData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.TimerClock;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.constant.ConstantValues;
import com.imaginet.ventures.step.utility.interfaces.TimerInterface;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.VideoPlayed;
import com.imaginet.ventures.step.utility.webservice.WebServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestActivity extends AppCompatActivity {
    private static String TAG = TestActivity.class.getSimpleName();
    private YouTubePlayerSupportFragment youtubeFragment;
    private LinearLayout youtubeFragmentLinearLayout;
    private ImageView questionImageView, playImageView;
    private TextView questionTextView;
    private ViewPager questionViewPager;
    private QuestionsFragmentPageAdapter pageAdapter;
    private int RECOVERY_REQUEST = 1;
    private YouTubePlayer youtubePlayer;
    private TextView timerTextView;
    private ImageView timerImageView;
    private Button submitButton;
    private Handler handler = new Handler();
    private int youtubeStartTime, youtubeEndTime;
    private int timeVideoPlayed = 0;
    private boolean played = false;
    private Runnable runnable;
    private ContentLoadingProgressBar contentLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        youtubeFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.testActivityQuestionFragment);
        playImageView = (ImageView) findViewById(R.id.testActivityQuestionPlayImageView);
        youtubeFragmentLinearLayout = (LinearLayout) findViewById(R.id.testActivityQuestionFragmentLinearLayout);
        questionImageView = (ImageView) findViewById(R.id.testActivityQuestionImageView);
        contentLoadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.testActivityQuestionImageViewContentLoadingProgressBar);
        questionTextView = (TextView) findViewById(R.id.testActivityQuestionTextView);
        questionViewPager = (ViewPager) findViewById(R.id.testActivityQuestionViewPager);
        timerTextView = (TextView) findViewById(R.id.dashboardActivityTimeLeftTextView);
        timerImageView = (ImageView) findViewById(R.id.dashboardActivityTimeLeftImageView);
        submitButton = (Button) findViewById(R.id.dashboardActivitySubmitButton);
        TimerClock.getInstance().addTimerInterface(new TimerInterface() {
            @Override
            public void timeRemaining(String time, @DrawableRes int drawable) {
                timerImageView.setImageResource(drawable);
                timerTextView.setText(time);
            }
        });

        playImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    Log.d(TAG, VideoPlayed.getInstance(TestActivity.this, TAG).getAttemptId() + " "+QuestionData.getInstance().getAttemptId());
                    if (!VideoPlayed.getInstance(TestActivity.this, TAG).getAttemptId().equalsIgnoreCase(QuestionData.getInstance().getAttemptId()) ||
                            !VideoPlayed.getInstance(TestActivity.this, TAG).getUrl().equalsIgnoreCase(QuestionData.getInstance().getVideoURL()) ||
                            VideoPlayed.getInstance(TestActivity.this, TAG).getTime() < 2) {
                        youtubePlayer.play();
                        playImageView.setVisibility(View.GONE);
                    } else {
                        ConstantFunction.toast(TestActivity.this, "Video can be played only twice");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(TestActivity.this, ProfileActivity.class));
                String[] answer = pageAdapter.getAnswer();
                for (int i = 0; i < answer.length; i++) {
                    if (answer[i] == null) {
                        questionViewPager.setCurrentItem(i, true);
                        ConstantFunction.toast(TestActivity.this, "Answer this question");
                        return;
                    }
                }
                submit();
            }
        });
        JSONArray questionJsonArray;
        try {
            questionJsonArray = QuestionData.getInstance().getQuestion();
            questionViewPager.setAdapter(pageAdapter = new QuestionsFragmentPageAdapter(getSupportFragmentManager(), TestActivity.this, questionJsonArray));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        updateQuestion1();
        youtubeFragment.onResume();
        super.onResume();
    }

    private void updateQuestion1() {
        try {
            if (QuestionData.getInstance().getImageURL() != null) {
                Log.d(TAG, "Image:" + QuestionData.getInstance().getImageURL());
                questionImageView.setVisibility(View.VISIBLE);
                contentLoadingProgressBar.setVisibility(View.VISIBLE);
                Picasso.with(TestActivity.this)
                        .load(ConstantValues.BASE_URL + QuestionData.getInstance().getImageURL())
                        .into(questionImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                contentLoadingProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            } else {
                questionImageView.setVisibility(View.GONE);
                contentLoadingProgressBar.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

        try {
            if (youtubePlayer != null)
                youtubePlayer.release();
            if (QuestionData.getInstance().getVideoURL() != null) {
                Log.d(TAG, "Video:" + QuestionData.getInstance().getVideoURL());
                youtubeFragmentLinearLayout.setVisibility(View.VISIBLE);
                videoStart();
            } else {
                youtubeFragmentLinearLayout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private void submit() {
        WebServices.getInstance(TestActivity.this).submit(QuestionData.getInstance().getQuestionId(), pageAdapter.getAnswer(), new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getBoolean("success")) {
                    switch (response.getJSONObject("data").getInt("resultcode")) {
                        case 200:
                            QuestionData.getInstance().setJsonObject(response.getJSONObject("data"));
                            TimerClock.getInstance().setSecondRemaining(QuestionData.getInstance().getRemainingTime());
                            VideoPlayed.getInstance(TestActivity.this, TAG).clear();
                            Intent intent = new Intent(TestActivity.this, TestActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 204:
                            ConstantFunction.toast(TestActivity.this, response.getJSONObject("data").getString("resultmessage"));
                            finish();
                            break;
                        default:
                            finish();
                    }
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(TestActivity.this, title, message, false);
            }
        }, TAG, TestActivity.this);
    }

    private void videoStart() {
        youtubeFragment.onResume();
        youtubeFragment.initialize(ConstantValues.GOOGLE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                try {
                    youtubeStartTime = QuestionData.getInstance().getStartTime();
                    youtubeEndTime = QuestionData.getInstance().getEndTime();
                    Log.d(TAG, "Youtube Start:" + youtubeStartTime + " End:" + youtubeEndTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                youtubePlayer = player;
                try {
                    youtubePlayer.loadVideo(QuestionData.getInstance().getVideoURL(), youtubeStartTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

                youtubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {
                        playImageView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPaused() {
                        playImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onStopped() {
                        playImageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onBuffering(boolean b) {

                    }

                    @Override
                    public void onSeekTo(int i) {

                    }
                });
                youtubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(String s) {
                        Log.d(TAG, s);
                        youtubePlayer.pause();
                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {
                        try {
                            if (!VideoPlayed.getInstance(TestActivity.this, TAG).getAttemptId().equalsIgnoreCase(QuestionData.getInstance().getAttemptId()) ||
                                    !VideoPlayed.getInstance(TestActivity.this, TAG).getUrl().equalsIgnoreCase(QuestionData.getInstance().getVideoURL())) {
//                                    Log.d(TAG, "New Video");
                                VideoPlayed.getInstance(TestActivity.this, TAG).setVideo(QuestionData.getInstance().getVideoURL(), 1, QuestionData.getInstance().getAttemptId());
                            } else {
//                                    Log.d(TAG, "Old Video " + VideoPlayed.getInstance(TestActivity.this, TAG).getTime());
                                VideoPlayed.getInstance(TestActivity.this, TAG).setVideo(QuestionData.getInstance().getVideoURL(), VideoPlayed.getInstance(TestActivity.this, TAG).getTime() + 1, QuestionData.getInstance().getAttemptId());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {

                    }
                });
                handler.postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
//                                Log.d(TAG, "youtube play seconds:" + youtubePlayer.getCurrentTimeMillis());
                        try {
                            if (youtubePlayer.getCurrentTimeMillis() <= youtubeEndTime) {
//                                    Log.d(TAG, "True");
                                if (played) {
                                    player.pause();
                                    played = false;
                                }
                                handler.postDelayed(this, 500);
                            } else {
//                                    Log.d(TAG, "False");
                                youtubePlayer.seekToMillis(youtubeStartTime);
                                if (!VideoPlayed.getInstance(TestActivity.this, TAG).getAttemptId().equalsIgnoreCase(QuestionData.getInstance().getAttemptId()) ||
                                        !VideoPlayed.getInstance(TestActivity.this, TAG).getUrl().equalsIgnoreCase(QuestionData.getInstance().getVideoURL())) {
//                                    Log.d(TAG, "New Video");
                                    VideoPlayed.getInstance(TestActivity.this, TAG).setVideo(QuestionData.getInstance().getVideoURL(), 1, QuestionData.getInstance().getAttemptId());
                                } else {
//                                    Log.d(TAG, "Old Video " + VideoPlayed.getInstance(TestActivity.this, TAG).getTime());
                                    VideoPlayed.getInstance(TestActivity.this, TAG).setVideo(QuestionData.getInstance().getVideoURL(), VideoPlayed.getInstance(TestActivity.this, TAG).getTime() + 1, QuestionData.getInstance().getAttemptId());
                                }
                                youtubePlayer.pause();
                                handler.postDelayed(this, 500);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                }, 1000);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub
                Log.e(TAG, "Video Player Error " + arg1.toString());
            }
        });
    }

}
