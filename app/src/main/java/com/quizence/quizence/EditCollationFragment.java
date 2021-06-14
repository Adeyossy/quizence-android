package com.quizence.quizence;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.quizence.quizence.model.MCQmodel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditCollationFragment extends Fragment implements View.OnClickListener {

    private FragmentSwapTriggerListener mSwapTriggerListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_collation, container, false);

        List<MCQmodel> questions = QuizenceDataHolder.get().getCurrentCollation().getQuestions();
        if(questions.size() == 0){
            view = inflater.inflate(R.layout.empty_results_layout_with_fab, container, false);
            TextView textView = view.findViewById(R.id.empty_layout_textview);
            textView.setText(R.string.first_poster);
            FloatingActionButton button = view.findViewById(R.id.quizence_fab);
            button.setOnClickListener(this);
            return view;
        }
        //find recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.fragment_edit_collation_recyclerview);
        EditCollationAdapter adapter = new EditCollationAdapter(questions, mSwapTriggerListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        //find the fab and handle it
        FloatingActionButton actionButton = view.findViewById(R.id.fragment_edit_collation_fab);
        actionButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        //TODO: Launch the editable equivalent of the QuizModeSwipeFragment
        MCQmodel model = new MCQmodel();
        model.setSourceId(QuizenceDataHolder.get().getCurrentCollation().getCollationId());
        mSwapTriggerListener.onSwapTriggered(model);
        Log.v(EditCollationFragment.class.getSimpleName(), "Fab clicked");
    }

    public void setSwapTriggerListener(FragmentSwapTriggerListener triggerListener){
        mSwapTriggerListener = triggerListener;
    }

    //the purpose of this interface is to trigger a fragment swap in the activity
    public interface FragmentSwapTriggerListener{
        public void onSwapTriggered(MCQmodel model);
    }
}
