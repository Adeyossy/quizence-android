package com.quizence.quizence;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class QuizModeActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Button mMultiViewButton;
    private Button mSingleViewButton;
    private QuizModeSwipeFragment mQuizModeSwipeFragment;
    private QuizModeListFragment mQuizModeListFragment;
    private int mClickCount;
    private boolean mIsSwipeMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mode);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(QuizenceDataHolder.get().getCourse().getCourseName());
        }

//        mSingleViewButton = findViewById(R.id.activity_quiz_mode_singleview);
//        mMultiViewButton = findViewById(R.id.activity_quiz_mode_multiview);

//        TextView logger = findViewById(R.id.logger_textview);
//        logger.setText(QuizenceDataHolder.get().getQuestions("obsgyn.json", this).get(0).getQuestionTitle());
//
//        FrameLayout fragmentParent = findViewById(R.id.activity_quiz_mode_fragment_slot);
//
//        mFragmentManager = getSupportFragmentManager();
        mQuizModeSwipeFragment = new QuizModeSwipeFragment();
        mQuizModeListFragment = new QuizModeListFragment();
//        mFragmentTransaction = mFragmentManager.beginTransaction();
//
//        Fragment fragment = mFragmentManager.findFragmentById(R.id.activity_quiz_mode_fragment_slot);
//        if(fragment == null){
//            mFragmentTransaction.add(R.id.activity_quiz_mode_fragment_slot, mQuizModeSwipeFragment);
//        }else mFragmentTransaction.replace(R.id.activity_quiz_mode_fragment_slot, mQuizModeSwipeFragment);
//
//        mFragmentTransaction.commit();

        changeQuizMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.change_view:
                changeQuizMode();
                mIsSwipeMode = !mIsSwipeMode;
                int drawableRes = mIsSwipeMode ? R.drawable.ic_view_agenda_black_24dp
                        : R.drawable.ic_view_array_black_24dp;
                item.setIcon(getResources().getDrawable(drawableRes));
                return true;

            case R.id.cancel_quiz:
                Intent intent = new Intent(this, SelectionActivity.class);
                startActivity(intent);
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void changeQuizMode(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_quiz_mode_fragment_slot);
        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();

        if((mClickCount % 2) == 0){
            if(fragment != null) fragmentTransaction1.remove(fragment);
            fragmentTransaction1.replace(R.id.activity_quiz_mode_fragment_slot, new QuizModeSwipeFragment()).commit();
//            Toast.makeText(this, "even clickcount= " + mClickCount , Toast.LENGTH_SHORT).show();
        }else{
            if(fragment != null) fragmentTransaction1.remove(fragment);
            fragmentTransaction1.replace(R.id.activity_quiz_mode_fragment_slot, mQuizModeListFragment).commit();
//            Toast.makeText(this, "odd clickcount= " + mClickCount, Toast.LENGTH_SHORT).show();
        }

        mClickCount++;
    }

    @Override
    protected void onStop() {
        super.onStop();
        QuizenceDataHolder.get().clearQuestionsList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        QuizenceDataHolder.get().clearQuestionsList();
    }
}