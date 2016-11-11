package com.imaginet.ventures.step.adapter.fragementpageadapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.imaginet.ventures.step.fragment.QuestionFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IM028 on 9/12/16.
 */
public class QuestionsFragmentPageAdapter extends FragmentPagerAdapter {
    private Activity activity;
    private JSONArray jsonArray;
    private List<QuestionFragment> questionFragments = new ArrayList<>();


    public QuestionsFragmentPageAdapter(FragmentManager fm, Activity activity, JSONArray jsonArray) {
        super(fm);
        this.activity = activity;
        this.jsonArray = jsonArray;

    }

    @Override
    public Fragment getItem(int position) {
        QuestionFragment questionFragment = QuestionFragment.getInstance(position, jsonArray.length());
        questionFragments.add(questionFragment);
        return questionFragment;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    public String[] getAnswer() {
        String[] result = new String[questionFragments.size()];
        for (int i = 0; i < questionFragments.size(); i++) {
            result[i] = questionFragments.get(i).getSelected();
        }
        return result;
    }
}
