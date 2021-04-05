package com.quizence.quizence;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quizence.quizence.model.MCQmodel;

import java.util.List;

/**
 * Created by Mustapha Adeyosola on 30-Mar-20.
 */

public class QuizModeListFragment extends Fragment {

    private List<MCQmodel> mMCQquestions;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMCQquestions = QuizenceDataHolder.get().getCourse().getCurrentQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz_mode_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_quiz_mode_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(new QuizModeListAdapter(mMCQquestions, getActivity()));

        return view;
    }
}
