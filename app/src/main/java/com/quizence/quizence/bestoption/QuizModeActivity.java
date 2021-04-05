package com.quizence.quizence.bestoption;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.quizence.quizence.QuizModeListFragment;
import com.quizence.quizence.QuizModeSwipeFragment;
import com.quizence.quizence.R;

/**
 * Created by Mustapha Adeyosola on 08-Apr-20.
 */

public class QuizModeActivity extends AppCompatActivity {

//    private Button mMultiViewButton;
//    private Button mSingleViewButton;
    private QuizModeSwipeFragment mQuizModeSwipeFragment;
    private QuizModeListFragment mQuizModeListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mode);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

//        mSingleViewButton = findViewById(R.id.activity_quiz_mode_singleview);
//        mMultiViewButton = findViewById(R.id.activity_quiz_mode_multiview);

//        TextView logger = findViewById(R.id.logger_textview);
//        logger.setText(QuizenceDataHolder.get().getQuestions("obsgyn.json", this).get(0).getQuestionTitle());

//        FrameLayout fragmentParent = findViewById(R.id.activity_quiz_mode_fragment_slot);

        FragmentManager mFragmentManager = getSupportFragmentManager();
        mQuizModeSwipeFragment = new QuizModeSwipeFragment();
        mQuizModeListFragment = new QuizModeListFragment();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();

        Fragment fragment = mFragmentManager.findFragmentById(R.id.activity_quiz_mode_fragment_slot);
        if(fragment == null){
            mFragmentTransaction.add(R.id.activity_quiz_mode_fragment_slot, mQuizModeSwipeFragment);
        }else mFragmentTransaction.replace(R.id.activity_quiz_mode_fragment_slot, mQuizModeSwipeFragment);

        mFragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    public void changeQuizMode(View v){
//
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_quiz_mode_fragment_slot);
//        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
////        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//
//        switch (v.getId()){
//            case R.id.activity_quiz_mode_singleview:
//                if(fragment != null) fragmentTransaction1.remove(fragment);
//                fragmentTransaction1.replace(R.id.activity_quiz_mode_fragment_slot, mQuizModeSwipeFragment).commit();
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//                v.setEnabled(false);
//                mMultiViewButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                mMultiViewButton.setEnabled(true);
//                break;
//
//            case R.id.activity_quiz_mode_multiview:
//                if(fragment != null) fragmentTransaction1.remove(fragment);
//                fragmentTransaction1.replace(R.id.activity_quiz_mode_fragment_slot, mQuizModeListFragment).commit();
//                v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
//                v.setEnabled(false);
//                mSingleViewButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                mSingleViewButton.setEnabled(true);
//                break;
//
//            default:
//                Toast.makeText(this, "The questions are not displaying", Toast.LENGTH_SHORT).show();
//        }
//    }
}
