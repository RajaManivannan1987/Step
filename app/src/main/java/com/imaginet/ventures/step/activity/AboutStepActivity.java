package com.imaginet.ventures.step.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.common.CommonActivity;

public class AboutStepActivity extends CommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_about_step);
        setTitle("About");

    }
}
