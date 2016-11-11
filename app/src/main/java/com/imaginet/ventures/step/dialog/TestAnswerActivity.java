package com.imaginet.ventures.step.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.utility.constant.ConstantValues;

public class TestAnswerActivity extends AppCompatActivity {
    private EditText editText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_answer);
        editText = (EditText) findViewById(R.id.testAnswerActivityEditText);
        submitButton = (Button) findViewById(R.id.testAnswerActivityButton);
        editText.setText(getIntent().getExtras().getString(ConstantValues.testAnswer, ""));
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent().putExtra(ConstantValues.testAnswer, editText.getText().toString()));
                finish();
            }
        });

    }
}
