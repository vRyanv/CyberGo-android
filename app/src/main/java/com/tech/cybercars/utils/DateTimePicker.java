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
    private TextView text_view;
    private EditText edit_text;
    private String date_format;
    private FragmentManager fragmentManager;
    private String tag;

    public DateTimePicker(FragmentManager fragmentManager, TextView text_view, String date_format,  String tag){
        this.text_view = text_view;
        this.date_format = date_format;
        this.fragmentManager = fragmentManager;
        this.tag=tag;
    }

    public DateTimePicker(FragmentManager fragmentManager, EditText edit_text, String date_format, String tag){
        this.edit_text = edit_text;
        this.date_format = date_format;
        this.fragmentManager = fragmentManager;
        this.tag = tag;
    }

    public void Run(){
        this.show(this.fragmentManager, this.tag);
    }

    public void SetFormatDate(String date_format){
        this.date_format = date_format;
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i,i1,i2);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.date_format, Locale.US);
        String date_picked = simpleDateFormat.format(calendar.getTime());
        if (this.edit_text != null){
            this.edit_text.setText(date_picked);
        }else if(this.text_view != null){
            this.text_view.setText(date_picked);
        }
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
}