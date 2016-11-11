package com.imaginet.ventures.step.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.singleton.QuestionData;
import com.imaginet.ventures.step.utility.constant.ConstantValues;

import org.json.JSONException;

public class YouTubeFragment extends Fragment {
    private String url;
    private int startTime, endTime;

    public YouTubeFragment() {
    }

    public static YouTubeFragment getInstance(String url, int start, int end) {
        YouTubeFragment fragment = new YouTubeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantValues.youtubeURL, url);
        bundle.putInt(ConstantValues.youtubeStartTime, start);
        bundle.putInt(ConstantValues.youtubeEndTime, end);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you_tube, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtubeFragmentFrameLayout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(ConstantValues.GOOGLE_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                    try {
                        player.loadVideo(QuestionData.getInstance().getVideoURL());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    player.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });
        return view;
    }

}
