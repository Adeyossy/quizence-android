package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

import java.util.Objects;

/**
 * Created by Mustapha Adeyosola on 04-Apr-20.
 */

public class SwipeFragment extends Fragment {

    private static final String QUESTION_NUMBER = "Question ";
    private static final String SWIPEFRAGMENT_KEY = "swipeFragment";
    private MCQmodel mMCQmodel;
    private boolean isMarked;
    private Drawable mDrawableCorrect;
    private Drawable mDrawableWrong;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMCQmodel = Objects.requireNonNull(getArguments()).getParcelable(SWIPEFRAGMENT_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_layout, container, false);

        TextView courseTextView = view.findViewById(R.id.fragment_swipe_textview);
        courseTextView.setText(QUESTION_NUMBER.concat(String.valueOf(mMCQmodel.getSerialNumber())
                .concat("/").concat(String.valueOf(
                        QuizenceDataHolder.get().getCourse().getCurrentQuestions().size()
                ))));

        TextView questionTextView = view.findViewById(R.id.fragment_swipe_title);
        questionTextView.setText(mMCQmodel.getQuestionTitle());

        final ListView optionsListView = view.findViewById(R.id.fragment_swipe_listview);
        final OptionsListAdapter adapter = new OptionsListAdapter(getActivity(), mMCQmodel);
        optionsListView.setAdapter(adapter);

        final Resources resources = Objects.requireNonNull(getActivity()).getResources();

        mDrawableCorrect = resources.getDrawable(R.drawable.correct_answer);
        mDrawableWrong = resources.getDrawable(R.drawable.wrong_answer);

        final Drawable drawableRightButton = resources.getDrawable(R.drawable.ic_check_circle_black_24dp);
        drawableRightButton.setTint(resources.getColor(R.color.right_answer));

        final Drawable drawableWrongButton = resources.getDrawable(R.drawable.ic_cancel_black_24dp);
        drawableWrongButton.setTint(resources.getColor(R.color.wrong_answer));

        //listening for clicks from the mark button
        Button button = view.findViewById(R.id.mark_button);
        if(!mMCQmodel.getAnswered()) {
            button.setEnabled(false);
        }

        if(mMCQmodel.getAnswerShown()){
            showAnswers(optionsListView);
            button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswers(optionsListView);
                v.setEnabled(false);
            }
        });

        return view;
    }

    private void showAnswers(ListView optionsListView){
        for(int i = 0; i < optionsListView.getChildCount(); i++){
            View currentView = optionsListView.getChildAt(i);

            Button trueButton = currentView.findViewById(R.id.options_listview_layout_true);
            Button falseButton = currentView.findViewById(R.id.options_listview_layout_false);
            MCQOptionModel option = mMCQmodel.getOptionsNoShuffle().get(i);

            Drawable answerDrawable = getResources().getDrawable(R.drawable.round_button);
            answerDrawable.setTint(getResources().getColor(R.color.colorAccent));

            if(option.getAnswer()){
                trueButton.setBackground(answerDrawable);
                trueButton.setTextColor(getResources().getColor(R.color.colorMain));
                trueButton.setElevation(2f);
                QuizenceUtilities.playAnimation(trueButton).start();
                falseButton.setVisibility(View.INVISIBLE);
            }else{
                falseButton.setBackground(answerDrawable);
                falseButton.setTextColor(getResources().getColor(R.color.colorMain));
                falseButton.setElevation(2f);
                QuizenceUtilities.playAnimation(falseButton).start();
                trueButton.setVisibility(View.INVISIBLE);
            }

            trueButton.setEnabled(false);
            falseButton.setEnabled(false);
        }

        mMCQmodel.setAnswerShown(true);
    }

    private void markAnswers(ListView optionsListView){
        mMCQmodel.setMarked(true);

        for(int i = 0; i < optionsListView.getChildCount(); i++){
            View currentView = optionsListView.getChildAt(i);

            LinearLayout linearLayout = currentView.findViewById(R.id.options_listview_layout_option);
            TextView optionTextView = currentView.findViewById(R.id.options_listview_layout_option_text);
            Button trueButton = currentView.findViewById(R.id.options_listview_layout_true);
            Button falseButton = currentView.findViewById(R.id.options_listview_layout_false);

            long duration = 200 * (i + 1);

            //define animator
            ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, View.SCALE_Y, 0f, 1f);
            animator.setDuration(duration);
            animator.setInterpolator(new OvershootInterpolator());

            MCQOptionModel mcqOptionModel = mMCQmodel.getOptionsNoShuffle().get(i);
            if(mcqOptionModel.getOptionScore() == 1){
                animator.start();
                linearLayout.setBackground(mDrawableCorrect);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            falseButton.setForeground(drawableRightButton);
//                        }
//                        optionTextView.setCompoundDrawables(null, null, drawableRightButton, null);
//                        optionTextView.setTextColor(getResources().getColor(R.color.colorMain));
//                        if(mcqOptionModel.getAnswer()){
//                            trueButton.setBackground(drawableRightButton);
//                        }else{
//                            falseButton.setBackground(drawableRightButton);
//                        }
            }else{
//                        animator.start();
                linearLayout.setBackground(mDrawableWrong);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            falseButton.setForeground(drawableWrongButton);
//                        }
//                        if(mcqOptionModel.getAnswer()){
//                            falseButton.setBackground(drawableWrongButton);
//                        }else {
//                            trueButton.setBackground(drawableWrongButton);
//                        }
            }
        }
    }

    public static SwipeFragment newInstance(MCQmodel mcqmodel){
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SWIPEFRAGMENT_KEY, mcqmodel);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }
}
