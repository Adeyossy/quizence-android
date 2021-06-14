package com.quizence.quizence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateCollationActivity extends AppCompatActivity implements CollationTimeDialog.OnTimePickedListener,
        CollationDateDialog.OnDatePickedListener, AdapterView.OnItemSelectedListener,
        QuizenceNetworker.StringResponseListener {

    private CollationModel mCollationModel;
    private static final String[] mMedicine = { "Medicine I", "Medicine II", "Medicine III" },
        mSurgery = { "Surgery I", "Surgery II", "Surgery III" },
        mPsychiatry = { "Psychiatry I", "Psychiatry II" },
        mPaediatrics = { "Paediatrics I", "Paediatrics II" },
        mObgyn = { "Obstetrics and Gynaecology I", "Obstetrics and Gynaecology II" },
        mFM = { "Family Medicine I", "Family Medicine II" };
    private EditText mHourTimeEditText;
    private EditText mMinuteTimeEditText;
    private EditText mAmPmEditText;
    private EditText mDayEditText, mMonthEditText, mYearEditText;
    private Spinner mSubPostingSpinner;
    private Spinner mQuestionTypeSpinner;
    private Spinner mSetSpinner;
    private Spinner mGroupSpinner;
    private Spinner mDurationSpinner;
    private EditText mNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collation);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Collate Questions");
            getSupportActionBar().setSubtitle("Available Courses to Collate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCollationModel = QuizenceDataHolder.get().getCurrentCollation();

        TextView titleTextView = findViewById(R.id.create_collation_title);
        titleTextView.setText(mCollationModel.getPosting());

        populateSubPostingSpinner();

        mQuestionTypeSpinner = findViewById(R.id.create_collation_question_type);
        final String[] questionType = getResources().getStringArray(R.array.create_collation_question_type);
        CollationSpinnerAdapter baseQuestionAdapter = new CollationSpinnerAdapter(questionType,
                this);
        mQuestionTypeSpinner.setOnItemSelectedListener(this);
        mQuestionTypeSpinner.setAdapter(baseQuestionAdapter);

        mSetSpinner = findViewById(R.id.create_collation_group);
        final String[] sets = getResources().getStringArray(R.array.create_collation_set);
        CollationSpinnerAdapter spinnerAdapter = new CollationSpinnerAdapter(sets, this);
        mSetSpinner.setAdapter(spinnerAdapter);
        mSetSpinner.setOnItemSelectedListener(this);

        mGroupSpinner = findViewById(R.id.create_collation_set);
        final String[] groups = getResources().getStringArray(R.array.create_collation_group);
        CollationSpinnerAdapter groupAdapter = new CollationSpinnerAdapter(groups, this);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(this);

        mNumberEditText = findViewById(R.id.create_collation_time_number);
        mNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCollationModel.setTestDurationNumber(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //populate duration-of-test-units spinner
        mDurationSpinner = findViewById(R.id.create_collation_time_unit);
        String[] durations = getResources().getStringArray(R.array.create_collation_duration_unit);
        CollationSpinnerAdapter durationAdapter = new CollationSpinnerAdapter(durations, this);
        mDurationSpinner.setAdapter(durationAdapter);
        mDurationSpinner.setOnItemSelectedListener(this);

        //create time picker dialog
        ImageButton timeImageButton = findViewById(R.id.create_collation_time_picker);
        final CollationTimeDialog timeDialog = new CollationTimeDialog();
        timeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeDialog.show(getSupportFragmentManager(), CollationTimeDialog.class.getSimpleName());
            }
        });

        //create date picker dialog
        ImageButton dateImageButton = findViewById(R.id.create_collation_date_picker);
        final CollationDateDialog dateDialog = new CollationDateDialog();
        dateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.show(getSupportFragmentManager(), CollationDateDialog.class.getSimpleName());
            }
        });

        mHourTimeEditText = findViewById(R.id.create_collation_time_hour);
        mMinuteTimeEditText = findViewById(R.id.create_collation_time_minute);
        mAmPmEditText = findViewById(R.id.create_collation_time_ampm);
        mDayEditText = findViewById(R.id.create_collation_date_day);
        mMonthEditText = findViewById(R.id.create_collation_date_month);
        mYearEditText = findViewById(R.id.create_collation_date_year);
    }

    private void populateSubPostingSpinner() {
        String posting = mCollationModel.getPosting().toLowerCase();
        String[] subPosting = {};

        switch (posting) {
            case "medicine":
                subPosting = mMedicine;
                break;

            case "surgery":
                subPosting = mSurgery;
                break;

            case "psychiatry":
                subPosting = mPsychiatry;
                break;

            case "paediatrics":
                subPosting = mPaediatrics;
                break;

            case "obgyn":
                subPosting = mObgyn;
                break;

            case "fm":
                subPosting = mFM;
                break;

            default:
                break;
        }

        mSubPostingSpinner = findViewById(R.id.create_collation_sub_posting);
        CollationSpinnerAdapter subPostingAdapter = new CollationSpinnerAdapter(subPosting,
                this);
        mSubPostingSpinner.setAdapter(subPostingAdapter);
        mSubPostingSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onTimePicked(String hourOfDay, String minute, String ampm) {
        //add time results to the UI
        mHourTimeEditText.setText(hourOfDay);
        mHourTimeEditText.setTextSize(28f);

        mMinuteTimeEditText.setText(minute);
        mMinuteTimeEditText.setTextSize(28f);

        mAmPmEditText.setText(ampm);
        mAmPmEditText.setTextSize(28f);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof CollationTimeDialog){
            CollationTimeDialog dialog = (CollationTimeDialog) fragment;
            dialog.setOnTimePickedListener(this);
        }

        if(fragment instanceof CollationDateDialog) {
            CollationDateDialog collationDateDialog = (CollationDateDialog) fragment;
            collationDateDialog.setOnDatePickedListener(this);
        }
    }

    @Override
    public void onDatePicked(String year, String month, String dayOfMonth) {
        mDayEditText.setText(dayOfMonth);
        mDayEditText.setTextSize(28f);

        mMonthEditText.setText(month);
        mMonthEditText.setTextSize(28f);

        mYearEditText.setText(year);
        mYearEditText.setTextSize(28f);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //
//        if(parent.getId() == R.id.create_collation_sub_posting);
        String optionSelected = parent.getItemAtPosition(position).toString();
        switch (parent.getId()){
            case R.id.create_collation_sub_posting:
                mCollationModel.setSubPosting(optionSelected);
                break;

            case R.id.create_collation_question_type:
                mCollationModel.setQuestionType(optionSelected);
                break;

            case R.id.create_collation_set:
                mCollationModel.setSet(optionSelected);
                break;

            case R.id.create_collation_group:
                mCollationModel.setGroup(optionSelected);
                break;

            case R.id.create_collation_time_unit:
                mCollationModel.setTestDurationUnit(optionSelected);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //
    }

    public void submitCollation(View view) throws JSONException {
        //verify and validate

        boolean validForm = true;
        if(mCollationModel.getSubPosting() == null){
            validForm = false;
            mSubPostingSpinner.requestFocus();
        }

        if(mCollationModel.getQuestionType() == null){
            validForm = false;
            mQuestionTypeSpinner.requestFocus();
        }

        if(mCollationModel.getSet() == null){
            validForm = false;
            mSetSpinner.requestFocus();
        }

        if(mCollationModel.getGroup() == null){
            validForm = false;
            mGroupSpinner.requestFocus();
        }

//        if(mCollationModel.getTestDurationNumber() == null && Integer.parseInt(mCollationModel.getTestDurationNumber()) == 0){
//            validForm = false;
//            mNumberEditText.requestFocus();
//        }

        if(mCollationModel.getTestDurationUnit() == null){
            validForm = false;
            mDurationSpinner.requestFocus();
        }

        if(validForm){
            //connect to the backend and post the collation as a form
            //get any errors that may occur
            mCollationModel.setCollationDate();
            JSONObject jsonObject = new JSONObject();
            Gson gson = new Gson();
            String collationJSON = gson.toJson(mCollationModel);
            Log.v(CreateCollationActivity.class.getSimpleName(), collationJSON);
            QuizenceNetworker networker = new QuizenceNetworker(this);
            String url = mCollationModel.getPosting().toLowerCase()
                    .concat(QuizenceNetworker.COLLATION_APPEND);
            Log.v(CreateCollationActivity.class.getSimpleName(), url);
            networker.postCollation(CreateCollationActivity.class.getSimpleName(), url,
                    collationJSON, this);
        }
    }

    @Override
    public void onStringResponse(String response) {
        //handle the response
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String dialogMessage = "";
        if(response.equals("200")){
            dialogMessage = "Collation Successfully Created";
        }else{
            dialogMessage = "An error occurred";
        }
        builder.setMessage(dialogMessage).setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CreateCollationActivity.this,
                                CollateActivity.class));
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
