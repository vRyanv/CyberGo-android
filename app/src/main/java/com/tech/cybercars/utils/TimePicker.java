package com.tech.cybercars.utils;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickerCallback time_picker_callback;
    private final FragmentManager fragment_manager;
    public TimePicker(FragmentManager fragment_manager) {
        this.fragment_manager = fragment_manager;
    }
    public void SetTimePickerCallback(TimePickerCallback time_picker_callback){
        this.time_picker_callback = time_picker_callback;
    }

    public void Run(){
        this.show(fragment_manager, "TimePicker");
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        time_picker_callback.OnTimePicked(hourOfDay, minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(requireActivity(), this, hour, minute, true);
    }

    public interface TimePickerCallback{
        void OnTimePicked(int hourOfDay, int minute);
    }
}
