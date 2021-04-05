package com.quizence.quizence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class SelectionResultActivity extends AppCompatActivity implements LoadingFragment.NetworkUpdateListener {

    private static final String SELECTED_COURSE = "Course";
    private static final String SELECTED_TYPE = "type";

    private String mSelectedCourse, mSelectedType;
    private static final String BACKEND_URL = "https://quizence.herokuapp.com/";

    private LoadingFragment mLoadingFragment;
    private FrameLayout mFrameLayout;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_result);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        mFrameLayout = findViewById(R.id.fragment_root);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_root);
        fragment = new LoadingFragment();
        mTransaction.replace(R.id.fragment_root, fragment).commit();

        //get Intent extras put in ModeSelectionDialog
        Intent intent = getIntent();

        //the null variable is meant to be a placeholder for a default value in the future
        mSelectedCourse = intent.hasExtra(SELECTED_COURSE) ? intent.getStringExtra(SELECTED_COURSE) : null;
        if(mSelectedCourse != null) mSelectedCourse = mSelectedCourse.substring(0, 1)
                .toUpperCase().concat(mSelectedCourse.substring(1));
        getSupportActionBar().setTitle(mSelectedCourse);

        mSelectedType = intent.hasExtra(SELECTED_TYPE) ? intent.getStringExtra(SELECTED_TYPE) : null;
        getSupportActionBar().setSubtitle(mSelectedType);

        //fetch questions from backend
        connectToBackend();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof LoadingFragment){
            mLoadingFragment = (LoadingFragment) fragment;
            mLoadingFragment.setNetworkUpdateListener(this);
        }
    }

    private void connectToBackend(){

        //Volley data fetch
        String backendUrl = BACKEND_URL.concat(mSelectedCourse.toLowerCase());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, backendUrl,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(mLoadingFragment != null) mLoadingFragment.updateTextView("Data Fetched");
                    Log.v(SelectionResultActivity.class.getSimpleName(), response.toString());
                    Log.v(SelectionResultActivity.class.getSimpleName(), mSelectedCourse);
                    QuizenceDataHolder.get().parseData(response);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_root,
                            new SelectionResultFragment()).commit();
                } catch (JSONException e) {
                    e.getMessage();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SelectionResultActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
                mLoadingFragment.setTryAgainButtonVisibility(View.VISIBLE);
            }
        });

        jsonArrayRequest.setTag(SelectionResultActivity.class.getSimpleName());

        QuizenceDataHolder.get().addToRequestQueue(jsonArrayRequest, this);
    }

    @Override
    public void onNetworkUpdate() {
        QuizenceDataHolder.get().cancelRequestQueue(SelectionResultActivity.class.getSimpleName());
        connectToBackend();
    }

    @Override
    protected void onStop() {
        super.onStop();
        QuizenceDataHolder.get().cancelRequestQueue(SelectionResultActivity.class.getSimpleName());
    }
}
