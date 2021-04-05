package com.quizence.quizence;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Mustapha Adeyosola on 05-Apr-20.
 */

public class OptionsListFragment extends Fragment {

    private static final String POSITION = "position";
    private int mPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getArguments().getInt(POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.options_listview, container, false);
        ListView listView = view.findViewById(R.id.options_listview);
        listView.setAdapter(new OptionsListAdapter(getActivity(),
                QuizenceDataHolder.get().getQuestions("obsgyn.json", getActivity()).get(mPosition)));

        return view;
    }

    public static OptionsListFragment newInstance(int position){
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        OptionsListFragment optionsListFragment = new OptionsListFragment();
        optionsListFragment.setArguments(bundle);
        return optionsListFragment;
    }
}
