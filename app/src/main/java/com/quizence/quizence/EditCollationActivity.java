package com.quizence.quizence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

public class EditCollationActivity extends AppCompatActivity implements EditCollationFragment.FragmentSwapTriggerListener {

    private CollationModel mCollationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collation);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        mCollationModel = QuizenceDataHolder.get().getCurrentCollation();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(mCollationModel.getSubPosting().concat(" Questions"));
            actionBar.setSubtitle("Create or edit questions");
        }
        manageFragments(new EditCollationFragment());
    }

    private void manageFragments(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
//        Fragment foundFragment = manager.findFragmentById(R.id.edit_collation_fragment);
        manager.beginTransaction().replace(R.id.edit_collation_fragment, fragment).commit();
    }

    @Override
    public void onSwapTriggered(MCQmodel model) {
//        manageFragments();
        //swap the editable fragment here
        Log.v(EditCollationActivity.class.getSimpleName(), "Click reached activity");
        manageFragments(EditCollationSwipeFragment.newInstance(model));
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof EditCollationFragment){
            EditCollationFragment collationFragment = (EditCollationFragment) fragment;
            collationFragment.setSwapTriggerListener(this);
        }
    }
}
