package com.tech.cybercars.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    public static String D_M_Y = "dd/MM/yyyy";
    public static String Y_M_D = "yyyy/MM/dd/";
    public static String M_D_Y = "MM/dd/yyyy";
    private String date_format;
    private DateTimePickerCallback date_time_picker_callback;
    private final FragmentManager fragment_manager;
    public DateTimePicker(FragmentManager fragment_manager, String date_format){
        this.date_format = date_format;
        this.fragment_manager = fragment_manager;
    }

    public void Run(){
        this.show(fragment_manager, "DateTimePicker");
    }

    public void SetFormatDate(String date_format){
        this.date_format = date_format;
    }
    public void SetOnDateTimePicked(DateTimePickerCallback date_time_picker_callback){
        this.date_time_picker_callback = date_time_picker_callback;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i,i1,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format, Locale.getDefault());
        String date_time_format = simpleDateFormat.format(calendar.getTime());
        date_time_picker_callback.OnDateTimePicked(calendar, date_time_format);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    public interface DateTimePickerCallback{
        void OnDateTimePicked(Calendar calendar, String date_time_format);
    }
}