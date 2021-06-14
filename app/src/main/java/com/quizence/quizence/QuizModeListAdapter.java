package com.quizence.quizence;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

import java.util.List;

/**
 * Created by Mustapha Adeyosola on 02-Apr-20.
 */

public class QuizModeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int VIEW_TYPE_ONE = 0; //view type code for the header
    static final int VIEW_TYPE_TWO = 1; //view type code for the main layout
    private List<MCQmodel> mMCQquestions;

    public QuizModeListAdapter(List<MCQmodel> questions){
        mMCQquestions = questions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_TWO){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_card_layout, parent, false);
            return new QuizModeListViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.title_subtitle, parent, false);
            return new TitleSubtitleHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_TWO && position <= mMCQquestions.size()){
            QuizModeListViewHolder listViewHolder = (QuizModeListViewHolder) holder;
            MCQmodel mcqquestion = mMCQquestions.get(position - 1);
            listViewHolder.mQuestionTextView.setText(mcqquestion.getQuestionTitle());

            List<MCQOptionModel> options = mcqquestion.getOptions();

            for(int i = 0; i < options.size(); i++){
                TextView textView = listViewHolder.mOptionTextViews[i];
                textView.setText(options.get(i).getOptionText());
            }

            for(int j = options.size(); j < 5; j++){
                listViewHolder.mOptionLayouts[j].setVisibility(View.GONE);
            }
        }

        if(getItemViewType(position) == VIEW_TYPE_ONE) {
            TitleSubtitleHolder titleSubtitleHolder = (TitleSubtitleHolder) holder;
            titleSubtitleHolder.mTitleTextView.setText(QuizenceDataHolder.get()
                    .getCourse().getCourseName());
            titleSubtitleHolder.mSubTitleTextView.setText(R.string.instruction);
        }

//        OptionsListAdapter adapter = new OptionsListAdapter(mContext, mcqquestion.getOptions());
//        holder.mOptionsListView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mMCQquestions.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) return VIEW_TYPE_ONE;
        return VIEW_TYPE_TWO;
    }

    public static class QuizModeListViewHolder extends RecyclerView.ViewHolder{

        public TextView mQuestionTextView;
        public ListView mOptionsListView;
        public LinearLayout mLinearLayout, mOptionLayout1, mOptionLayout2, mOptionLayout3, mOptionLayout4, mOptionLayout5;
        public Button mTrueButton1, mTrueButton2, mTrueButton3, mTrueButton4, mTrueButton5;
        public Button mFalseButton1, mFalseButton2, mFalseButton3, mFalseButton4, mFalseButton5;
        public TextView mOptionText1, mOptionText2, mOptionText3, mOptionText4, mOptionText5;

        public LinearLayout[] mOptionLayouts = { mOptionLayout1, mOptionLayout2, mOptionLayout3, mOptionLayout4, mOptionLayout5 };
        public TextView[] mOptionTextViews = new TextView[5];
        public Button[] mTrueButtons = new Button[5],
                mFalseButtons = new Button[5];


        public QuizModeListViewHolder(View itemView) {
            super(itemView);
            mQuestionTextView = itemView.findViewById(R.id.recyclerview_card_layout_question);
//            mOptionsListView = itemView.findViewById(R.id.recyclerview_card_layout_options);

            mLinearLayout = itemView.findViewById(R.id.recyclerview_card_layout_linearlayout);

            mOptionLayout1 = itemView.findViewById(R.id.options_listview_layout_option1);
            mOptionLayout2 = itemView.findViewById(R.id.options_listview_layout_option2);
            mOptionLayout3 = itemView.findViewById(R.id.options_listview_layout_option3);
            mOptionLayout4 = itemView.findViewById(R.id.options_listview_layout_option4);
            mOptionLayout5 = itemView.findViewById(R.id.options_listview_layout_option5);

            mTrueButton1 = itemView.findViewById(R.id.options_listview_layout_true1);
            mTrueButton2 = itemView.findViewById(R.id.options_listview_layout_true2);
            mTrueButton3 = itemView.findViewById(R.id.options_listview_layout_true3);
            mTrueButton4 = itemView.findViewById(R.id.options_listview_layout_true4);
            mTrueButton5 = itemView.findViewById(R.id.options_listview_layout_true5);

            mTrueButtons[0] = mTrueButton1;
            mTrueButtons[1] = mTrueButton2;
            mTrueButtons[2] = mTrueButton3;
            mTrueButtons[3] = mTrueButton4;
            mTrueButtons[4] = mTrueButton5;

            mFalseButton1 = itemView.findViewById(R.id.options_listview_layout_false1);
            mFalseButton2 = itemView.findViewById(R.id.options_listview_layout_false2);
            mFalseButton3 = itemView.findViewById(R.id.options_listview_layout_false3);
            mFalseButton4 = itemView.findViewById(R.id.options_listview_layout_false4);
            mFalseButton5 = itemView.findViewById(R.id.options_listview_layout_false5);

            mFalseButtons[0] = mFalseButton1;
            mFalseButtons[1] = mFalseButton2;
            mFalseButtons[2] = mFalseButton3;
            mFalseButtons[3] = mFalseButton4;
            mFalseButtons[4] = mFalseButton5;

            mOptionText1 = itemView.findViewById(R.id.options_listview_layout_option_text1);
            mOptionText2 = itemView.findViewById(R.id.options_listview_layout_option_text2);
            mOptionText3 = itemView.findViewById(R.id.options_listview_layout_option_text3);
            mOptionText4 = itemView.findViewById(R.id.options_listview_layout_option_text4);
            mOptionText5 = itemView.findViewById(R.id.options_listview_layout_option_text5);

            mOptionTextViews[0] = mOptionText1;
            mOptionTextViews[1] = mOptionText2;
            mOptionTextViews[2] = mOptionText3;
            mOptionTextViews[3] = mOptionText4;
            mOptionTextViews[4] = mOptionText5;

            mOptionLayouts[0] = mOptionLayout1;
            mOptionLayouts[1] = mOptionLayout2;
            mOptionLayouts[2] = mOptionLayout3;
            mOptionLayouts[3] = mOptionLayout4;
            mOptionLayouts[4] = mOptionLayout5;
        }
    }

    public static class TitleSubtitleHolder extends RecyclerView.ViewHolder{

        TextView mTitleTextView, mSubTitleTextView;

        public TitleSubtitleHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.title_subtitle_title);
            mSubTitleTextView = itemView.findViewById(R.id.title_subtitle_subtitle);
        }
    }
}
