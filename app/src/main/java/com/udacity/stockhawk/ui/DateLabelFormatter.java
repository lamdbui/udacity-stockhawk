package com.udacity.stockhawk.ui;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lamdbui on 2/16/17.
 */

public class DateLabelFormatter implements IAxisValueFormatter {

    private List<String> mFormattedDates;

    public DateLabelFormatter(List<String> formattedDates) {
        // show only year and month for now
        mFormattedDates = formattedDates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormattedDates.get((int)value);
    }
}
