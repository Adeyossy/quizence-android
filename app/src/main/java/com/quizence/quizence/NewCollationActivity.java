package com.quizence.quizence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;

public class NewCollationActivity extends AppCompatActivity implements
        LoadingFragment.NetworkUpdateListener, QuizenceNetworker.NetworkResponseListener {

    public static final String COURSE_NAME_EXTRA = "course";
    private LoadingFragment mLoadingFragment;
    private FrameLayout mFragmentContainer;
    private String mCourseToCollate;
    private static final String mCollationURL = "/collation";
    private FragmentTransaction mTransaction;
    private QuizenceNetworker mQuizenceNetworker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collation);

        Intent intent = getIntent();
        mCourseToCollate = intent.getStringExtra(COURSE_NAME_EXTRA);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Collate Questions");
            getSupportActionBar().setSubtitle("Available Courses to Collate");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        mFragmentContainer = findViewById(R.id.new_collation_fragment);
    }

    void getCollations() throws JSONException {
        //form backend url
        String backendUrl = mCourseToCollate.toLowerCase().concat(mCollationURL);
        if(backendUrl.contains(" ")) backendUrl = backendUrl.replace(" ", "");
        //backend operation for collations
        mQuizenceNetworker = new QuizenceNetworker(this);
        mQuizenceNetworker.initiateNetworkActivity(NewCollationActivity.class.getSimpleName(),
                backendUrl, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.new_collation_fragment);
        fragment = new LoadingFragment();
        mTransaction.replace(R.id.new_collation_fragment, fragment).commit();
        try {
            getCollations();
        } catch (JSONException e) {
            e.getMessage();
        }

        //only senators can create a new collation
        Snackbar.make(findViewById(R.id.coordinatorLayout),
                "Only class senators should create a new collation",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof LoadingFragment){
            mLoadingFragment = (LoadingFragment) fragment;
            mLoadingFragment.setNetworkUpdateListener(this);
        }
    }

    public void createCollationActivity(View view) {
        CollationModel collationModel = new CollationModel(mCourseToCollate);
        QuizenceDataHolder.get().addCollation(collationModel);
        Intent intent = new Intent(this, CreateCollationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkUpdate() {
        //handle network change shenanigans
        mLoadingFragment.setTryAgainButtonVisibility(View.VISIBLE);
        try {
            getCollations();
        } catch (JSONException e) {
            e.getMessage();
            e.getCause();
        }
    }

    @Override
    public void onNetworkResponseListener(JSONArray array) {
        //handle network response
        Log.v(NewCollationActivity.class.getSimpleName(), array.toString());
        try {
            QuizenceDataHolder.get().parseCollationData(array);
        } catch (JSONException e) {
            Log.v(NewCollationActivity.class.getSimpleName(), e.getMessage());
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.new_collation_fragment,
                new NewCollationFragment()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        QuizenceDataHolder.get().cancelRequestQueue(NewCollationActivity.class.getSimpleName());
    }
}
