package com.quizence.quizence;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mustapha Adeyosola on 04-Apr-20.
 */

public class OptionsListAdapter extends BaseAdapter {

    private final MCQmodel mMCQModel;
    private List<MCQOptionModel> mMCQOptionsList;
    private Context mContext;
    private boolean[] mTrueButtonStates, mFalseButtonStates;
    private Drawable mRoundButtonDrawable;

    public OptionsListAdapter(Context context, MCQmodel mcQmodel){
        mContext = context;
        mMCQModel = mcQmodel;
        mMCQOptionsList = mcQmodel.getOptions();
//        Collections.shuffle(mMCQOptionsList);
        mTrueButtonStates = new boolean[mMCQOptionsList.size()];
        mFalseButtonStates = new boolean[mMCQOptionsList.size()];
        Arrays.fill(mTrueButtonStates, false);
        Arrays.fill(mFalseButtonStates, false);
    }

    @Override
    public int getCount() {
        return mMCQOptionsList.size();
    }

    @Override
    public MCQOptionModel getItem(int position) {
//        if(position == mMCQOptionsList.size()) return null;
        return mMCQOptionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);

            //TODO: remove the conditional once it is sure it is no longer needed
            if(position == mMCQOptionsList.size()){
            }
            else {
                convertView = inflater.inflate(R.layout.options_listview_layout, parent, false);

                LinearLayout linearLayout = convertView.findViewById(R.id.options_listview_layout_option);
                if(mMCQModel.getMarked()){
                    Resources resources = mContext.getResources();

                    final Drawable drawable = resources.getDrawable(R.drawable.round_rectangle);
                    final Drawable drawableCorrect = resources.getDrawable(R.drawable.correct_answer);
                    final Drawable drawableWrong = resources.getDrawable(R.drawable.wrong_answer);

                    //drawable for showing the correct answer to the user

                    switch (mMCQOptionsList.get(position).getOptionScore()){
                        case 0:
                            linearLayout.setBackground(drawableWrong);
                            break;

                        case 1:
                            linearLayout.setBackground(drawableCorrect);
                            break;

                            default:
                                linearLayout.setBackground(drawable);
                                break;
                    }
                }

                final TextView optionTextView = convertView.findViewById(R.id.options_listview_layout_option_text);
                final MCQOptionModel optionModel = mMCQOptionsList.get(position);
                optionTextView.setText(optionModel.getOptionText());

                final Button trueButton = convertView.findViewById(R.id.options_listview_layout_true);
                final Button falseButton = convertView.findViewById(R.id.options_listview_layout_false);
                switch (optionModel.getUserAnswer()){
                    case 1:
                        buttonManager(true, trueButton, falseButton, optionModel, 1);
                        break;
                    case 2:
                        buttonManager(true, falseButton, trueButton, optionModel, 2);
                    default:
                        break;
                }
                Drawable answerDrawable = mContext.getResources().getDrawable(R.drawable.round_button);
                answerDrawable.setTint(mContext.getResources().getColor(R.color.colorAccent));

                if(mMCQModel.getAnswerShown()){
                    if(optionModel.getAnswer()){
                        trueButton.setBackground(answerDrawable);
                        trueButton.setTextColor(mContext.getResources().getColor(R.color.colorMain));
                        trueButton.setElevation(2f);
                        QuizenceUtilities.playAnimation(trueButton).start();
                        falseButton.setVisibility(View.INVISIBLE);
                    }else{
                        falseButton.setBackground(answerDrawable);
                        falseButton.setTextColor(mContext.getResources().getColor(R.color.colorMain));
                        falseButton.setElevation(2f);
                        QuizenceUtilities.playAnimation(falseButton).start();
                        trueButton.setVisibility(View.INVISIBLE);
                    }
                }

                trueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTrueButtonStates[position] = !mTrueButtonStates[position];
                        buttonManager(mTrueButtonStates[position], trueButton, falseButton, optionModel, 1);
                        mFalseButtonStates[position] = false;
                    }
                });

                falseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFalseButtonStates[position] = !mFalseButtonStates[position];
                        buttonManager(mFalseButtonStates[position], falseButton, trueButton, optionModel, 2);
                        mTrueButtonStates[position] = false;
                    }
                });
            }
        }

        return convertView;
    }

    private void buttonManager(boolean buttonState, Button activeButton, Button passiveButton,
                               MCQOptionModel optionModel, int userAnswer) {
        if(!mMCQModel.getAnswerShown()){
            if(buttonState){
                mRoundButtonDrawable = mContext.getResources().getDrawable(R.drawable.round_button);
                mRoundButtonDrawable.setTint(mContext.getResources().getColor(R.color.colorPrimaryDark));
                activeButton.setBackground(mRoundButtonDrawable);
                activeButton.setTextColor(mContext.getResources().getColor(android.R.color.white));
                playAnimation(activeButton);
                passiveButton.setBackground(null);
                passiveButton.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                optionModel.setUserAnswer(userAnswer);
            }else{
                activeButton.setBackground(null);
                activeButton.setTextColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
                playAnimation(activeButton);
                optionModel.setUserAnswer(0);
            }
        }
    }

    private void playAnimation(Button activeButton) {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(activeButton, scaleX, scaleY).setDuration(200);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }
}
