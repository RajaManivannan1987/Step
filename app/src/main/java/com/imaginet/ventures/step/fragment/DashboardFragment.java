package com.imaginet.ventures.step.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.activity.DashboardActivity;
import com.imaginet.ventures.step.activity.TestActivity;
import com.imaginet.ventures.step.activity.TestTutorialActivity;
import com.imaginet.ventures.step.singleton.QuestionData;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.TimerClock;
import com.imaginet.ventures.step.utility.interfaces.DialogBoxInterface;
import com.imaginet.ventures.step.utility.interfaces.TimerInterface;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.VideoPlayed;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {
    private static String TAG = "DashboardFragment";
    private ArcProgress overAllArcProgress, readArcProgress, writeArcProgress, listenArcProgress, speakArcProgress;
    private NumberProgressBar numberProgressBar;
    private Button continueButton;
    private View view;
    private TextView overAllTextView, readArcTextView, writeArcTextView, listenArcTextView;
    private ImageView readArcImageView, writeArcImageView, listenArcImageView;

    public DashboardFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        overAllArcProgress = (ArcProgress) view.findViewById(R.id.dashboardFragmentOverallArcProgress);
        overAllTextView = (TextView) view.findViewById(R.id.dashboardFragmentOverallTextView);
        readArcProgress = (ArcProgress) view.findViewById(R.id.dashboardFragmentReadArcProgress);
        readArcTextView = (TextView) view.findViewById(R.id.dashboardFragmentReadArcProgressTextView);
        readArcImageView = (ImageView) view.findViewById(R.id.dashboardFragmentReadArcProgressImageView);
        writeArcProgress = (ArcProgress) view.findViewById(R.id.dashboardFragmentWriteArcProgress);
        writeArcTextView = (TextView) view.findViewById(R.id.dashboardFragmentWriteArcProgressTextView);
        writeArcImageView = (ImageView) view.findViewById(R.id.dashboardFragmentWriteArcProgressImageView);
        listenArcProgress = (ArcProgress) view.findViewById(R.id.dashboardFragmentListenArcProgress);
        listenArcTextView = (TextView) view.findViewById(R.id.dashboardFragmentListenArcProgressTextView);
        listenArcImageView = (ImageView) view.findViewById(R.id.dashboardFragmentListenArcProgressImageView);
        speakArcProgress = (ArcProgress) view.findViewById(R.id.dashboardFragmentSpeakArcProgress);
        numberProgressBar = (NumberProgressBar) view.findViewById(R.id.dashboardFragmentNumberProgressBar);
        continueButton = (Button) view.findViewById(R.id.dashboardFragmentContinueButton);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    switch (UserData.getInstance().getTestRequest()) {
                        case BEGIN:
                            startActivity(new Intent(getActivity(), TestTutorialActivity.class));
                            break;
                        case INPROGRESS:
                            final AlertDialog.Builder dialog = AlertDialogManager.listenerDialogBox(getActivity(), "Test in Progress", "Time left:" + TimerClock.getInstance().getTimeString() + ".", "Continue", "Cancel & Take New",
                                    new DialogBoxInterface() {
                                        @Override
                                        public void yes() {
                                            restartAttempt();
                                        }

                                        @Override
                                        public void no() {
                                            startAttempt();
                                        }
                                    });

                            break;
                        case COMPLETED:
                            AlertDialogManager.listenerDialogBox(getActivity(), "", "You currently have a score of " + String.format("%.1f", UserData.getInstance().getOverAllLevel()) + ". You can retake the test to try and improve your score, or, view more detailed reports below.", "Retake the Test", null,
                                    new DialogBoxInterface() {
                                        @Override
                                        public void yes() {
                                            startAttempt();
                                        }

                                        @Override
                                        public void no() {
                                        }
                                    });
                            break;
                        case INCOMPLETED:
                            AlertDialogManager.listenerDialogBox(getActivity(), "Test in Progress", "Time left: Time Up. Click Continue to answer the last question", "Continue", "Cancel & Take New",
                                    new DialogBoxInterface() {
                                        @Override
                                        public void yes() {
                                            restartAttempt();
                                        }

                                        @Override
                                        public void no() {
                                            startAttempt();
                                        }
                                    });
                            break;
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        });
        return view;
    }

    private void updateData() {
        overAllArcProgress.setProgress((int) UserData.getInstance().getOverAllLevel());
        overAllTextView.setText(UserData.getInstance().getOverAllLevelText());
        if (UserData.getInstance().getReadLevel() > 0) {
            readArcImageView.setVisibility(View.INVISIBLE);
            readArcTextView.setVisibility(View.VISIBLE);
        } else {
            readArcImageView.setVisibility(View.VISIBLE);
            readArcTextView.setVisibility(View.INVISIBLE);
        }
        readArcProgress.setProgress((int) UserData.getInstance().getReadLevel());
        readArcTextView.setText(UserData.getInstance().getReadLevelText());
        if (UserData.getInstance().getWriteLevel() > 0) {
            writeArcImageView.setVisibility(View.INVISIBLE);
            writeArcTextView.setVisibility(View.VISIBLE);
        } else {
            writeArcImageView.setVisibility(View.VISIBLE);
            writeArcTextView.setVisibility(View.INVISIBLE);
        }
        writeArcProgress.setProgress((int) UserData.getInstance().getWriteLevel());
        writeArcTextView.setText(UserData.getInstance().getWriteLevelText());
        if (UserData.getInstance().getListenLevel() > 0) {
            listenArcImageView.setVisibility(View.INVISIBLE);
            listenArcTextView.setVisibility(View.VISIBLE);
        } else {
            listenArcImageView.setVisibility(View.VISIBLE);
            listenArcTextView.setVisibility(View.INVISIBLE);
        }
        listenArcProgress.setProgress((int) UserData.getInstance().getListenLevel());
        listenArcTextView.setText(UserData.getInstance().getListenLevelText());

        numberProgressBar.setProgress(UserData.getInstance().getProgressBar());
        continueButton.setText(UserData.getInstance().checkButtonBeginContinue());
    }

    @Override
    public void onResume() {
        updateData();
        super.onResume();
    }

    private void restartAttempt() {
        WebServices.getInstance(getActivity()).restartAttempt(UserData.getInstance().getLastAttempt(), new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                    QuestionData.getInstance().setJsonObject(response.getJSONObject("data"));
                    TimerClock.getInstance().setSecondRemaining(QuestionData.getInstance().getRemainingTime());
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialogManager.showAlertDialog(getActivity(), "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(getActivity(), title, message, false);
            }
        }, TAG, getActivity());
    }

    private void startAttempt() {
        WebServices.getInstance(getActivity()).startAttempt(new VolleyResponseListener() {
            @Override
            public void onResponse(JSONObject response) throws JSONException {
                if (response.getBoolean("success") && (response.getJSONObject("data").getInt("resultcode") == 200)) {
                    QuestionData.getInstance().setJsonObject(response.getJSONObject("data"));
                    TimerClock.getInstance().setSecondRemaining(QuestionData.getInstance().getRemainingTime());
                    VideoPlayed.getInstance(getActivity(), TAG).clear();
                    Intent intent = new Intent(getActivity(), TestActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialogManager.showAlertDialog(getActivity(), "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                }
            }

            @Override
            public void onError(String message, String title) {
                AlertDialogManager.showAlertDialog(getActivity(), title, message, false);
            }
        }, TAG, getActivity());
    }
}
