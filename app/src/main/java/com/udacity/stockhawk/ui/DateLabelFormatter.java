package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lamdbui on 2/16/17.
 */

public class DateLabelFormatter implements IAxisValueFormatter {

    private SimpleDateFormat mFormatter;
    private String[] mFormattedDates;

//    SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTimeInMillis(Long.parseLong(dateInMillis));
//    int year = calendar.get(Calendar.YEAR);
//    int month = calendar.get(Calendar.MONTH);
//    int day = calendar.get(Calendar.DAY_OF_MONTH);
//    String formattedDate = formatter.format(calendar.getTime());

    public DateLabelFormatter(String[] formattedDates) {
        mFormatter = new SimpleDateFormat("YYYY-MM-dd");
        mFormattedDates = formattedDates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis((long)timeInMillis);
        //return mFormatter.format(calendar.getTime());
        return mFormattedDates[(int)value];
    }
}
