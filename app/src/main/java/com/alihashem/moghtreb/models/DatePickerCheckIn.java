package com.alihashem.moghtreb.models;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.alihashem.moghtreb.R;

import java.util.Calendar;

public class DatePickerCheckIn extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
        datepickerdialog.setOnShowListener(dialog -> {
            datepickerdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.blue));
            datepickerdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
            datepickerdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(12);
            datepickerdialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.blue));
            datepickerdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white));
        });
        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        calendar.add(Calendar.YEAR, 1);
        datepickerdialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        return datepickerdialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Button checkIn = getActivity().findViewById(R.id.btnCheckIn);
        checkIn.setText(day + ":" + (month + 1) + ":" + year);
    }
}
