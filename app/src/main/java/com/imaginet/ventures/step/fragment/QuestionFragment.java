package com.imaginet.ventures.step.fragment;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.activity.ProfileActivity;
import com.imaginet.ventures.step.activity.TestActivity;

import com.imaginet.ventures.step.dialog.TestAnswerActivity;
import com.imaginet.ventures.step.singleton.QuestionData;
import com.imaginet.ventures.step.utility.constant.ConstantValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment implements View.OnClickListener {
    private static String TAG = QuestionFragment.class.getSimpleName();
    private TextView questionNumberTextView, questionDetailsTextView, questionDetail1TextView,questionTextView;
    private LinearLayout questionLinearLayout;
    private int dotSize, dotSizeMargin;
    private EditText answerEditText;
    private RadioGroup answerRadioGroup;
    private int position, totalPosition;
    private int ANSWER = 1;
    private int[] radioButtonArray = {
            R.id.itemQuestionDetail1RadioButton,
            R.id.itemQuestionDetail2RadioButton,
            R.id.itemQuestionDetail3RadioButton,
            R.id.itemQuestionDetail4RadioButton,
            R.id.itemQuestionDetail5RadioButton,
            R.id.itemQuestionDetail6RadioButton,
            R.id.itemQuestionDetail7RadioButton,
            R.id.itemQuestionDetail8RadioButton
    };
    private int[] cardViewArray = {
            R.id.itemQuestionDetail1RadioButtonCardView,
            R.id.itemQuestionDetail2RadioButtonCardView,
            R.id.itemQuestionDetail3RadioButtonCardView,
            R.id.itemQuestionDetail4RadioButtonCardView,
            R.id.itemQuestionDetail5RadioButtonCardView,
            R.id.itemQuestionDetail6RadioButtonCardView,
            R.id.itemQuestionDetail7RadioButtonCardView,
            R.id.itemQuestionDetail8RadioButtonCardView
    };
    private List<RadioButton> radioButtonList = new ArrayList<>();
    private JSONObject question;
    private CardView answerEditTextCardView;

    public QuestionFragment() {
    }

    public static QuestionFragment getInstance(int position, int totalPosition) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ConstantValues.fragmentPosition, position);
        bundle.putInt(ConstantValues.totalPosition, totalPosition);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question, container, false);
        totalPosition = getArguments().getInt(ConstantValues.totalPosition, 0);
        position = getArguments().getInt(ConstantValues.fragmentPosition, 0);

        questionLinearLayout = (LinearLayout) view.findViewById(R.id.itemQuestionDetailQuestionPositionLinearLayout);
        questionNumberTextView = (TextView) view.findViewById(R.id.itemQuestionDetailQuestionNumberTextView);
        questionDetail1TextView = (TextView) view.findViewById(R.id.itemQuestionDetail1TextView);
        questionDetailsTextView = (TextView) view.findViewById(R.id.itemQuestionDetailTextView);
        answerEditText = (EditText) view.findViewById(R.id.itemQuestionDetailEditText);
        questionTextView=(TextView)view.findViewById(R.id.itemQuestionDetailQuestionMainTextTextView);

        answerEditTextCardView = (CardView) view.findViewById(R.id.itemQuestionDetailEditTextCardView);
        answerRadioGroup = (RadioGroup) view.findViewById(R.id.itemQuestionDetailRadioGroup);
        dotSize = (int) getResources().getDimension(R.dimen.dot_size);
        dotSizeMargin = (int) getResources().getDimension(R.dimen.dot_size_margin);
        if (totalPosition != 1)
            for (int i = 0; i < totalPosition; i++) {
                View view1 = new View(view.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dotSize, dotSize);
                params.setMargins(dotSizeMargin, dotSizeMargin, dotSizeMargin, dotSizeMargin);
                view1.setLayoutParams(params);

                if (i <= position) {
                    view1.setBackgroundResource(R.drawable.circle_black);
                } else {
                    view1.setBackgroundResource(R.drawable.circle_black_light);
                }
                questionLinearLayout.addView(view1);
            }
        try {
            question = QuestionData.getInstance().getQuestion().getJSONObject(position);
            int currentQuestionNo = QuestionData.getInstance().getCountAnswerSubmitted() + position + 1;
            questionNumberTextView.setText("Question " + currentQuestionNo);
            questionDetailsTextView.setText(question.getString("question"));
            if (!question.getString("prompt").equalsIgnoreCase("")) {
                questionDetail1TextView.setVisibility(View.VISIBLE);
                questionDetail1TextView.setText(question.getString("prompt"));
            } else
                questionDetail1TextView.setVisibility(View.GONE);
            Log.d(TAG, "Question Type : " + question.getString("type"));
            switch (question.getString("type")) {
                case "MCQ":
                    Log.d(TAG, "Question Inside : MCQ");
                    answerEditText.setVisibility(View.GONE);
                    answerRadioGroup.setVisibility(View.VISIBLE);
                    for (int i = 0; i < question.getJSONArray("choices").length(); i++) {
                        CardView cardView = (CardView) view.findViewById(cardViewArray[i]);
                        cardView.setVisibility(View.VISIBLE);
                        RadioButton radioButton = (RadioButton) view.findViewById(radioButtonArray[i]);
                        radioButton.setText(ConstantValues.startArray[i] + ". " + question.getJSONArray("choices").getString(i));
                        radioButtonList.add(radioButton);
                        radioButton.setOnClickListener(this);
                    }
                    break;
                case "LA":
                    Log.d(TAG, "Question Inside : MCQ");
                    answerEditText.setVisibility(View.VISIBLE);
                    answerEditTextCardView.setVisibility(View.VISIBLE);
                    answerRadioGroup.setVisibility(View.GONE);
                    break;
                case "SA":
                    answerEditText.setVisibility(View.VISIBLE);
                    answerEditTextCardView.setVisibility(View.VISIBLE);
                    answerRadioGroup.setVisibility(View.GONE);
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        try {
            if (QuestionData.getInstance().getTextQuestion() != null) {
                Log.d(TAG, "Text:" + QuestionData.getInstance().getTextQuestion());
                questionTextView.setVisibility(View.VISIBLE);
                questionTextView.setText(Html.fromHtml(QuestionData.getInstance().getTextQuestion()));
            } else {
                questionTextView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        answerEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               startActivity(new Intent(getActivity(), ProfileActivity.class));
                startActivityForResult(new Intent(getActivity(), TestAnswerActivity .class).putExtra(ConstantValues.testAnswer, answerEditText.getText().toString()), ANSWER);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ANSWER) {
            if (resultCode == Activity.RESULT_OK) {
                answerEditText.setText(data.getExtras().getString(ConstantValues.testAnswer));
                ((TestActivity) getActivity()).onResume();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        for (RadioButton radioButton : radioButtonList)
            radioButton.setChecked((radioButton.getId() == view.getId()));
    }

    public String getSelected() {
        try {
            switch (question.getString("type")) {
                case "MCQ":
                    for (int i = 0; i < radioButtonList.size(); i++)
                        if (radioButtonList.get(i).isChecked())
                            return question.getJSONArray("choices").getString(i);
                    break;
                case "LA":
                    return answerEditText.getText().toString();
                case "SA":
                    return answerEditText.getText().toString();
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}
