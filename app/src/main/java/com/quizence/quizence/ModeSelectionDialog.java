package com.quizence.quizence;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by Mustapha Adeyosola on 27-Mar-20.
 */

public class ModeSelectionDialog extends DialogFragment {

    private static final String SELECTED_COURSE = "Course";
    private static final String SELECTED_TYPE = "type";
    private String mSelectedCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null) mSelectedCourse = bundle.getString(SELECTED_COURSE);
        Toast.makeText(getActivity(), mSelectedCourse, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setTitle(R.string.mode_selection_dialog_title).setItems(R.array.mode_selection_dialog_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
//                        Toast.makeText(getActivity(), "Case 0 selected", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), SelectionResultActivity.class);
                        intent.putExtra(SELECTED_COURSE, mSelectedCourse);
                        String itemAtPosition = getActivity().getResources()
                                .getStringArray(R.array.mode_selection_dialog_array)[which];
                        intent.putExtra(SELECTED_TYPE, itemAtPosition);
                        startActivity(intent);
                        break;
                    case 1:
//                        Toast.makeText(getActivity(), "Case 1 selected", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                    case 3:
                        default: dialog.dismiss();
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static ModeSelectionDialog newInstance(String course){
        ModeSelectionDialog modeSelectionDialog = new ModeSelectionDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SELECTED_COURSE, course);
        modeSelectionDialog.setArguments(bundle);
        return modeSelectionDialog;
    }
}
