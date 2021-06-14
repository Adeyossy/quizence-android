package com.quizence.quizence;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.quizence.quizence.model.MCQmodel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class SelectionResultFragment extends Fragment {

    private List<CourseModel> mAllQuestions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuizenceDataHolder quizenceDataHolder = QuizenceDataHolder.get();
        int currentCourseIndex = quizenceDataHolder.getCurrentCourseIndex();
        mAllQuestions = QuizenceDataHolder.get().getCourses()
                .get(currentCourseIndex).getAllQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection_result, container, false);
//        Log.v(SelectionResultFragment.class.getSimpleName(), "Size of mAllQuestions: " + mAllQuestions.size());

        if(mAllQuestions.size() == 0){
            view = inflater
                    .inflate(R.layout.empty_results_layout, container, false);
            Toast.makeText(getActivity(), "Empty List", Toast.LENGTH_SHORT).show();
            return view;
        }

        //Listview and Adapter for the questions gotten from backend
        ListView listView = view.findViewById(R.id.fragment_selection_result_listview);
        listView.setAdapter(new ResultItemsAdapter());
        return view;
    }

    private class ResultItemsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mAllQuestions.size();
        }

        @Override
        public CourseModel getItem(int position) {
            return mAllQuestions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.fragment_selection_result_items, parent, false);
            }

            RelativeLayout relativeLayout = convertView.findViewById(R.id.item_relativelayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuizenceDataHolder.get().getCourse().setCurrentQuestions(position);
                    Intent modeActivityIntent = new Intent(getActivity(), QuizModeActivity.class);
                    startActivity(modeActivityIntent);
                }
            });

            //textview for the description
            TextView headerTextView = convertView.findViewById(R.id.item_description);
            headerTextView.setText(getItem(position).getDescription());

            //TextView for the question count
            TextView countTextView = convertView.findViewById(R.id.item_count_number);
            countTextView.setText(String.valueOf(getItem(position).getQuestions().size()).concat(
                    " questions"
            ));

            TextView typeTextView = convertView.findViewById(R.id.item_type);
            String type = "";
            if(getItem(position).getType().equals("boole")) type = "True of False";
            typeTextView.setText(type);

            TextView yearTextView = convertView.findViewById(R.id.item_year);
            yearTextView.setText(String.valueOf(getItem(position).getYear()));

            return convertView;
        }
    }
}
