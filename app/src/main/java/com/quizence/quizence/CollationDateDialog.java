package com.quizence.quizence;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CollationDateDialog extends DialogFragment {

    private OnDatePickedListener mOnDatePickedListener;
    private CollationModel mCollationModel;

    CollationDateDialog(){
        mCollationModel = QuizenceDataHolder.get().getCurrentCollation();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //set the values in the model
                        Calendar adjustedCalendar = Calendar.getInstance();
                        adjustedCalendar.set(year, month, dayOfMonth);
                        mCollationModel.setCollationDate(year, month, dayOfMonth);

                        String monthString = String.valueOf(month + 1);
                        String dayString = String.valueOf(dayOfMonth);

                        //communicate with parent UI
                        if(month / 10 == 0){
                            monthString = "0".concat(monthString);
                        }

                        if(dayOfMonth / 10 == 0){
                            dayString = "0".concat(dayString);
                        }

                        mOnDatePickedListener.onDatePicked(String.valueOf(year), monthString,
                                dayString);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        return datePickerDialog;
    }

    public void setOnDatePickedListener(OnDatePickedListener datePickedListener){
        mOnDatePickedListener = datePickedListener;
    }

    public interface OnDatePickedListener{
        public void onDatePicked(String year, String month, String dayOfMonth);
    }
}
