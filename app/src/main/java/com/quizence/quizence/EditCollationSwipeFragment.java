package com.quizence.quizence;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quizence.quizence.model.MCQOptionModel;
import com.quizence.quizence.model.MCQmodel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditCollationSwipeFragment extends Fragment implements TextWatcher, QuizenceNetworker.StringResponseListener {

    private MCQmodel mMCQmodel;
    private EditText mOptionEditText1;
    private EditText mOptionEditText2;
    private EditText mOptionEditText3;
    private EditText mOptionEditText4;
    private EditText mOptionEditText5;
    private String mUnique; //this field indicates presence of ID and not newness

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMCQmodel = getArguments().getParcelable(EditCollationSwipeFragment.class.getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_collation_swipe, container, false);
        //find the views
        TextView numberTextView = view.findViewById(R.id.fragment_edit_collation_swipe_textview);
        EditText questionEditText = view.findViewById(R.id.fragment_edit_collation_swipe_title);
        mOptionEditText1 = view.findViewById(R.id.edit_collation_options_adapter_view_edittext1);
        mOptionEditText2 = view.findViewById(R.id.edit_collation_options_adapter_view_edittext2);
        mOptionEditText3 = view.findViewById(R.id.edit_collation_options_adapter_view_edittext3);
        mOptionEditText4 = view.findViewById(R.id.edit_collation_options_adapter_view_edittext4);
        mOptionEditText5 = view.findViewById(R.id.edit_collation_options_adapter_view_edittext5);
        Button button = view.findViewById(R.id.fragment_edit_collation_submit_button);
        EditText[] options = {mOptionEditText1, mOptionEditText2, mOptionEditText3, mOptionEditText4, mOptionEditText5};

//        option1.getText().toString();

        for(int i = 0; i < options.length; i++){
            EditText option = options[i];
            String text = mMCQmodel.getOptionsNoShuffle().get(i).getOptionText();
            if(text != null) option.setText(text);
        }

        mOptionEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMCQmodel.getOptionsNoShuffle().get(0).setOption(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mOptionEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMCQmodel.getOptionsNoShuffle().get(1).setOption(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mOptionEditText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMCQmodel.getOptionsNoShuffle().get(2).setOption(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mOptionEditText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMCQmodel.getOptionsNoShuffle().get(3).setOption(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mOptionEditText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMCQmodel.getOptionsNoShuffle().get(4).setOption(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //set and handle the text for the question
        String questionText = mMCQmodel.getQuestionTitle();
        if(questionText != null){
            questionEditText.setText(questionText);
        }
        questionEditText.addTextChangedListener(this);

        //set and handle the text for the question number
        List<MCQmodel> questions = QuizenceDataHolder.get().getCurrentCollation()
                .getQuestions();
        int indexInList = questions.indexOf(mMCQmodel);
        String numberText = "";
        int position = 0;
        if(indexInList == -1){
            //this means it is a new question
            //call private method for that
//            position = questions.size();
            mUnique = "newbie";
            numberText = "New Question";
        }else {
            //It is an existing question
            //call private method for editing a current question
            mUnique = "unique";
            position = indexInList + 1;
            numberText = "Question ".concat(String.valueOf(position));
            button.setText(R.string.update);
        }

        numberTextView.setText(numberText);

        handleSubmission(button, options);

        return view;
    }

    private void handleSubmission(Button button, final EditText[] options){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < options.length; i++){
                    EditText option = options[i];
                    mMCQmodel.getOptionsNoShuffle().get(i).setOption(option.getText().toString());
                }
                //handle the submission and communication with the server
                Gson gson = new Gson();
                String questionJSON = gson.toJson(mMCQmodel);
                String url = QuizenceDataHolder.get().getCurrentCollation().getPosting()
                        .toLowerCase();
                url = url.concat(QuizenceNetworker.COLLATION_APPEND).concat("/").concat(mUnique);
                Log.v(EditCollationSwipeFragment.class.getSimpleName(), url);
                Log.v(EditCollationSwipeFragment.class.getSimpleName(), questionJSON);
                QuizenceNetworker networker = new QuizenceNetworker(getActivity());
                networker.postCollation(EditCollationSwipeFragment.class.getSimpleName(),
                        url, questionJSON, EditCollationSwipeFragment.this);
            }
        });
    }

    public static EditCollationSwipeFragment newInstance(MCQmodel mcQmodel){
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditCollationSwipeFragment.class.getSimpleName(), mcQmodel);
        EditCollationSwipeFragment swipeFragment = new EditCollationSwipeFragment();
        swipeFragment.setArguments(bundle);
        return  swipeFragment;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mMCQmodel.setQuestionTitle(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onStringResponse(String response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(response.equals("200")){
            builder.setMessage("Successfully posted").setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(getActivity() != null){
                                getActivity().finish();
                            }
                        }
                    });
        }else {
            builder.setMessage("An error occurred! Try again").setPositiveButton(R.string.try_again,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static class EditCollationSwipeAdapter extends BaseAdapter {

        private List<MCQOptionModel> mOptions;

        EditCollationSwipeAdapter(List<MCQOptionModel> options){
            mOptions = options;
        }

        @Override
        public int getCount() {
            return mOptions.size();
        }

        @Override
        public MCQOptionModel getItem(int position) {
            return mOptions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.edit_collation_options_adapter_view, parent, false
                );
            }

            EditText editText = convertView.findViewById(
                    R.id.edit_collation_options_adapter_view_edittext);
            editText.setTag(position);
            String optionText = mOptions.get(position).getOptionText();
            if(optionText != null){
                editText.setText(optionText);
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mOptions.get(position).setOption(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            return convertView;
        }
    }
}
