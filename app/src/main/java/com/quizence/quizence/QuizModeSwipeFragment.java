package com.quizence.quizence;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quizence.quizence.model.MCQmodel;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mustapha Adeyosola on 30-Mar-20.
 */

public class QuizModeSwipeFragment extends Fragment {

    private ViewPager mViewPager;
    private List<MCQmodel> mMCQquestions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMCQquestions = QuizenceDataHolder.get().getCourse().getCurrentQuestions();
        Collections.shuffle(mMCQquestions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_mode_swipe, container, false);
        mViewPager = view.findViewById(R.id.fragment_quiz_mode_swipe_viewpager);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public SwipeFragment getItem(int position) {
                return SwipeFragment.newInstance(mMCQquestions.get(position));
            }

            @Override
            public int getCount() {
                return mMCQquestions.size();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
