package com.quizence.quizence;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CollationTimeDialog extends DialogFragment {

    private OnTimePickedListener mTimePickedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //set the values in the model
                        Calendar adjustedCalendar = Calendar.getInstance();
                        adjustedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        adjustedCalendar.set(Calendar.MINUTE, minute);
                        QuizenceDataHolder.get().getCurrentCollation()
                                .adjustCollationTime(hourOfDay, minute);

                        String ampm = "am";
                        if(hourOfDay > 12){
                            hourOfDay = hourOfDay - 12;
                            ampm = "pm";
                        }
                        String hourString = String.valueOf(hourOfDay);
                        String minuteString = String.valueOf(minute);
                        //send details to the parent activity UI
                        if(hourOfDay / 10 == 0) {
                            hourString = "0".concat(hourString);
                        }

                        if(minute / 10 == 0){
                            minuteString = "0".concat(minuteString);
                        }
                        mTimePickedListener.onTimePicked(hourString, minuteString, ampm);
                    }
                }, 8, 0, false);

        return timePickerDialog;
    }

    public void setOnTimePickedListener(OnTimePickedListener timePickedListener){
        mTimePickedListener = timePickedListener;
    }

    public interface OnTimePickedListener{
        public void onTimePicked(String hourOfDay, String minute, String ampm);
    }
}
