package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SelectionActivity extends AppCompatActivity implements CourseSelectionFragment.OnCourseSelectedListener {

    private static QuizenceDataHolder sQuizenceDataHolder;
    private int mCurrentPosition;

    private ViewPager mSelectionPager;
    private ImageView mLeftImageView;
    private ImageView mRightImageView;
    private Button mButton;
    private String mCourseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Toolbar quizenceToolbar = findViewById(R.id.quizence_appbar);
        setSupportActionBar(quizenceToolbar);

        sQuizenceDataHolder = QuizenceDataHolder.get();

        mSelectionPager = findViewById(R.id.selection_fragment_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSelectionPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return CourseSelectionFragment.newInstance(sQuizenceDataHolder.getCourses().get(position));
            }

            @Override
            public int getCount() {
                return sQuizenceDataHolder.getCourses().size();
            }
        });

        mSelectionPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mButton.setEnabled(false);
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) mLeftImageView.setVisibility(View.INVISIBLE);
                else{
                    mLeftImageView.setVisibility(View.VISIBLE);
                }

                if(position == (sQuizenceDataHolder.getCourses().size() - 1)){
                    mRightImageView.setVisibility(View.INVISIBLE);
                }else{
                    mRightImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLeftImageView = findViewById(R.id.activity_selection_left);
        createAnimation(mLeftImageView, 500).start();

        mLeftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = mSelectionPager.getCurrentItem();
                mSelectionPager.setCurrentItem(--mCurrentPosition);
            }
        });

        mRightImageView = findViewById(R.id.activity_selection_right);
        createAnimation(mRightImageView, 1000).start();

        mRightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = mSelectionPager.getCurrentItem();
                mSelectionPager.setCurrentItem((++mCurrentPosition));
            }
        });

        mButton = findViewById(R.id.activity_selection_proceed_button);
        mButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sQuizenceDataHolder.setCurrentCourseIndex(mSelectionPager.getCurrentItem());
                ModeSelectionDialog modeSelectionDialog = ModeSelectionDialog.newInstance(mCourseTitle);
                modeSelectionDialog.show(getSupportFragmentManager(), "modeSelectionDialog");
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private ObjectAnimator createAnimation(View view, long delay){
        PropertyValuesHolder prop1 = PropertyValuesHolder.ofFloat(View.ALPHA, 0.5f, 0.8f, 0.5f);
        PropertyValuesHolder prop2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, 0f, 12f, 0f);
        PropertyValuesHolder prop3 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f);
        PropertyValuesHolder prop4 = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, prop1, prop2, prop3, prop4);
        objectAnimator.setDuration(2000).setStartDelay(delay);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);

        return  objectAnimator;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if(fragment instanceof CourseSelectionFragment){
            CourseSelectionFragment selectionFragment = (CourseSelectionFragment) fragment;
            selectionFragment.setOnCourseSelectedListener(this);
        }
    }

    @Override
    public void onCourseSelected(boolean state, String courseTitle) {
        mButton.setEnabled(state);
        mCourseTitle = courseTitle;
    }
}
