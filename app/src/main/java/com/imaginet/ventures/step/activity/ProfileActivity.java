package com.imaginet.ventures.step.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.common.CommonActivity;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.constant.ConstantValues;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;

public class ProfileActivity extends CommonActivity {
    private static String TAG = ProfileActivity.class.getSimpleName();
    private EditText nameEditText, phoneEditText, emailEditText, dobEditText, homeLangEditText, yearSchoolEditText, noLangSpeakEditText, noLangWriteReadEditText, commonLangEditText;
    private RadioButton maleRadioButton, femaleRadioButton, otherRadioButton;
    private Button submitButton;
    private String dob = "";
    private DatePickerDialog dobDatePickerDialog;
    private Calendar dobCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_profile);
        setTitle("Profile");


        nameEditText = (EditText) findViewById(R.id.profileActivityNameEditText);
        phoneEditText = (EditText) findViewById(R.id.profileActivityPhoneEditText);
        emailEditText = (EditText) findViewById(R.id.profileActivityEmailEditText);
        dobEditText = (EditText) findViewById(R.id.profileActivityDobEditText);
        homeLangEditText = (EditText) findViewById(R.id.profileActivityHomeLanguageEditText);
        yearSchoolEditText = (EditText) findViewById(R.id.profileActivityYearOfSchoolingEditText);
        noLangSpeakEditText = (EditText) findViewById(R.id.profileActivityNumberOfLanguageEditText);
        noLangWriteReadEditText = (EditText) findViewById(R.id.profileActivityNumberOfLanguageReadWriteEditText);
        commonLangEditText = (EditText) findViewById(R.id.profileActivityCommonMediumEditText);

        maleRadioButton = (RadioButton) findViewById(R.id.profileActivityMaleRadioButton);
        femaleRadioButton = (RadioButton) findViewById(R.id.profileActivityFemaleRadioButton);
        otherRadioButton = (RadioButton) findViewById(R.id.profileActivityOthersRadioButton);
        submitButton = (Button) findViewById(R.id.profileActivitySubmitButton);
        dobCalendar = Calendar.getInstance();
        if (!UserData.getInstance().getUserDOB().equalsIgnoreCase("")) {
            try {
                dobCalendar.setTime(ConstantValues.serverFormat1.parse(UserData.getInstance().getUserDOB()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dob = ConstantValues.serverFormat1.format(dobCalendar.getTime());
            dobEditText.setText(ConstantValues.appDate.format(dobCalendar.getTime()));
        }
        dobDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                dobCalendar.set(i, i1, i2);
                dob = ConstantValues.serverFormat1.format(dobCalendar.getTime());
                dobEditText.setText(ConstantValues.appDate.format(dobCalendar.getTime()));
            }
        }, dobCalendar.get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH), dobCalendar.get(Calendar.DAY_OF_MONTH));

        nameEditText.setText(UserData.getInstance().getUserName());
        phoneEditText.setText(UserData.getInstance().getUserPhone());
        emailEditText.setText(UserData.getInstance().getUserEmail());

        homeLangEditText.setText(UserData.getInstance().getUserHomeLanguage());
        yearSchoolEditText.setText(UserData.getInstance().getUserYearsOfSchooling());
        noLangSpeakEditText.setText(UserData.getInstance().getUserNumberOfLangsSpeakOrUnderstand());
        noLangWriteReadEditText.setText(UserData.getInstance().getUserNumberOfLanguagesReadAndWrite());
        commonLangEditText.setText(UserData.getInstance().getUserCommonMedium());

        switch (UserData.getInstance().getUserGender()) {
            case "M":
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);
                otherRadioButton.setChecked(false);
                break;
            case "F":
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
                otherRadioButton.setChecked(false);
                break;
            case "O":
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(false);
                otherRadioButton.setChecked(true);
                break;
        }
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobDatePickerDialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServices.getInstance(ProfileActivity.this).editUser(
                        phoneEditText.getText().toString(),
                        nameEditText.getText().toString(),
                        getGender(),
                        dob,
                        "",
                        yearSchoolEditText.getText().toString(),
                        commonLangEditText.getText().toString(),
                        homeLangEditText.getText().toString(),
                        noLangSpeakEditText.getText().toString(),
                        noLangWriteReadEditText.getText().toString(),
                        "", new String[0],
                        new VolleyResponseListener() {
                            @Override
                            public void onResponse(JSONObject response) throws JSONException {
                                if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                    ConstantFunction.toast(ProfileActivity.this, response.getJSONObject("data").getString("resultmessage"));
                                    UserData.getInstance().setJsonObject(response.getJSONObject("data"));
                                } else {
                                    AlertDialogManager.showAlertDialog(ProfileActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                                }
                            }

                            @Override
                            public void onError(String message, String title) {
                                AlertDialogManager.showAlertDialog(ProfileActivity.this, title, message, false);
                            }
                        }, TAG, ProfileActivity.this);
            }
        });
    }

    private String getGender() {
        if (maleRadioButton.isChecked())
            return "M";
        if (femaleRadioButton.isChecked())
            return "F";
        if (otherRadioButton.isChecked())
            return "O";
        return "";
    }
}
