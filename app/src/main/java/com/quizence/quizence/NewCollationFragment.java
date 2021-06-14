package com.quizence.quizence;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class NewCollationFragment extends Fragment {

    private String mTest;
    private static final int VIEW_TYPES = 2;
    private static final int VIEW_TYPE_TITLE = 1;
    private static final int VIEW_TYPE_MAIN = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_collation, container, false);

        if(QuizenceDataHolder.get().getCollations().size() == 0){
            view = inflater.inflate(R.layout.empty_results_layout, container, false);
            TextView textView = view.findViewById(R.id.empty_layout_textview);
            textView.setText(R.string.oops_no_collation);
            return view;
        }

        ListView listView = view.findViewById(R.id.fragment_new_collation_list);
        NewCollationAdapter collationAdapter = new NewCollationAdapter(getActivity());
        listView.setAdapter(collationAdapter);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        QuizenceDataHolder.get().clearCollations();
    }

    private static class NewCollationAdapter extends BaseAdapter{

        private Context mContext;
        private List<CollationModel> mCollationModels = new ArrayList<>();

        NewCollationAdapter(Context context) {
            mContext = context;
            //get the available collations for the course
            mCollationModels = QuizenceDataHolder.get().getCollations();
        }

        @Override
        public int getCount() {
            return mCollationModels.size() + 1;
        }

        @Override
        public CollationModel getItem(int position) {
            return mCollationModels.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPES;
        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0) {
                return VIEW_TYPE_TITLE;
            }
            return VIEW_TYPE_MAIN;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            if(getItemViewType(position) == VIEW_TYPE_MAIN){
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext).
                            inflate(R.layout.collation_subposting_item, parent, false);
                }

                handleMainLayout(position, convertView, parent);
            }

            if(getItemViewType(position) == VIEW_TYPE_TITLE){
                if(convertView == null){
                    convertView = LayoutInflater.from(mContext)
                            .inflate(R.layout.title_subtitle, parent, false);
                }

                TextView textView = convertView.findViewById(R.id.title_subtitle_title);
                textView.setText(mCollationModels.get(0).getPosting());

                //edit subtitle
                TextView subtitleTextView = convertView.findViewById(R.id.title_subtitle_subtitle);
                subtitleTextView.setText(R.string.available_collations_description);
            }

            return convertView;
        }

        private void handleMainLayout(final int initialPosition, View convertView, final ViewGroup parent) {
            final int position = initialPosition - 1;
            CardView cardView = convertView.findViewById(R.id.collation_subposting_cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //create the new activity for editing the questions
                    //TODO: Set the current collation in ViewModel
                    QuizenceDataHolder.get().setCurrentCollationIndex(position);
                    QuizenceDataHolder.get().setCurrentCollation(getItem(position));
                    Intent intent = new Intent(parent.getContext(), EditCollationActivity.class);
                    parent.getContext().startActivity(intent);
                }
            });

            TextView postingTextView = convertView.findViewById(R.id.collation_subposting_title);
            postingTextView.setText(getItem(position).getSubPosting());

            TextView subPostingTextView = convertView.
                    findViewById(R.id.collation_subposting_date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(getItem(position).getCollationDate());
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            subPostingTextView.setText(day.concat("/").concat(month).concat("/").concat(year));

            TextView typeTextView = convertView.findViewById(R.id.collation_subposting_type);
            typeTextView.setText(getItem(position).getQuestionType());

            TextView setTextView = convertView.findViewById(R.id.collation_subposting_set);
            setTextView.setText(getItem(position).getSet());

            TextView groupTextView = convertView.findViewById(R.id.collation_subposting_group);
            groupTextView.setText(getItem(position).getGroup());

            TextView timeTextView = convertView.findViewById(R.id.collation_subposting_time);
            DateFormat format = DateFormat.getTimeInstance();
            format.setCalendar(calendar);
//            String hour = calendar.get(Calendar.HOUR)
            timeTextView.setText("");
        }
    }
}
