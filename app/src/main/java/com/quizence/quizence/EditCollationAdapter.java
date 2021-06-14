package com.quizence.quizence;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quizence.quizence.model.MCQmodel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class EditCollationAdapter extends QuizModeListAdapter {

    private List<MCQmodel> mQuestions;
    private EditCollationFragment.FragmentSwapTriggerListener mFragmentSwapTriggerListener;

    public EditCollationAdapter(List<MCQmodel> questions){
        super(questions);
        //instantiate field(s)
        mQuestions = questions;
    }

    public EditCollationAdapter(List<MCQmodel> mQuestions,
                                EditCollationFragment.FragmentSwapTriggerListener triggerListener){
        this(mQuestions);
        mFragmentSwapTriggerListener = triggerListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == QuizModeListAdapter.VIEW_TYPE_TWO){
            QuizModeListViewHolder holder = (QuizModeListViewHolder) super.onCreateViewHolder(parent,
                    viewType);
            TextView textView = new TextView(parent.getContext());
            textView.setText(R.string.click_to_edit);
            textView.setPadding(8, 16, 8, 16);
            textView.setTextColor(textView.getResources().getColor(R.color.colorAccent));
            holder.mLinearLayout.addView(textView);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if(getItemViewType(position) == QuizModeListAdapter.VIEW_TYPE_TWO && position
                <= mQuestions.size()){
            QuizModeListViewHolder listViewHolder = (QuizModeListViewHolder) holder;
            int listLength = mQuestions.get(position - 1).getOptionsNoShuffle().size();
            char baseChar = 97;
            if(listLength > 0){
                for(int k = 0; k < listLength; k++){
                    char newChar = (char) (baseChar + k);
                    Button trueButton = listViewHolder.mTrueButtons[k];
                    Button falseButton = listViewHolder.mFalseButtons[k];
                    if(trueButton != null) {
                        trueButton.setText(String.valueOf(newChar));
                    }
                    if(falseButton != null) falseButton.setVisibility(View.GONE);
                }
            }

            listViewHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentSwapTriggerListener.onSwapTriggered(mQuestions.get(position - 1));
                }
            });
        }

        if(getItemViewType(position) == VIEW_TYPE_ONE) {
            TitleSubtitleHolder titleSubtitleHolder = (TitleSubtitleHolder) holder;
            titleSubtitleHolder.mTitleTextView.setText(QuizenceDataHolder.get()
                    .getCurrentCollation().getSubPosting());
            titleSubtitleHolder.mSubTitleTextView.setText(R.string.choose_to_edit);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
