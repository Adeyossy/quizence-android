package com.quizence.quizence;

import android.content.Context;
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

public class QuizModeListAdapter extends RecyclerView.Adapter<QuizModeListAdapter.QuizModeListViewHolder> {

    private List<MCQmodel> mMCQquestions;
    private Context mContext;

    public QuizModeListAdapter(List<MCQmodel> questions, Context context){
        mMCQquestions = questions;
        mContext = context;
    }

    @Override
    public QuizModeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_card_layout, parent, false);
        return new QuizModeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizModeListViewHolder holder, int position) {
        MCQmodel mcqquestion = mMCQquestions.get(position);
        holder.mQuestionTextView.setText(mcqquestion.getQuestionTitle());

        List<MCQOptionModel> options = mcqquestion.getOptions();

        for(int i = 0; i < options.size(); i++){
            TextView textView = holder.mOptionTextViews[i];
            textView.setText(options.get(i).getOptionText());
        }

        for(int j = options.size(); j < 5; j++){
            holder.mOptionLayouts[j].setVisibility(View.GONE);
        }

//        OptionsListAdapter adapter = new OptionsListAdapter(mContext, mcqquestion.getOptions());
//        holder.mOptionsListView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return mMCQquestions.size();
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
        public Button[] mTrueButtons = new Button[]{ mTrueButton1, mTrueButton2, mTrueButton3, mTrueButton4, mTrueButton5 },
                mFalseButtons = new Button[]{ mFalseButton1, mFalseButton2, mFalseButton3, mFalseButton4, mFalseButton5 };

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

            mFalseButton1 = itemView.findViewById(R.id.options_listview_layout_false1);
            mFalseButton2 = itemView.findViewById(R.id.options_listview_layout_false2);
            mFalseButton3 = itemView.findViewById(R.id.options_listview_layout_false3);
            mFalseButton4 = itemView.findViewById(R.id.options_listview_layout_false4);
            mFalseButton5 = itemView.findViewById(R.id.options_listview_layout_false5);

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
}
