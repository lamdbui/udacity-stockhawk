package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;

public class StockDetailActivity extends AppCompatActivity {

    public static final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    @BindView(R.id.stock_chart)
    LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        ButterKnife.bind(this);

        updateChart("AAPL");

//        float[] stockHistory = getStockHistory("MRVL");
//
//        float[] valuesX = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//        //float[] valuesY = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
//        float[] valuesY = stockHistory;
//
//        List<Entry> entries = new ArrayList<Entry>();
//
//        for (int i=0 ; i< valuesX.length ; i++) {
//            entries.add(new Entry(valuesX[i], valuesY[i]));
//        }
//
//        LineDataSet dataSet = new LineDataSet(entries, "Label");
//        dataSet.setCircleColor(Color.CYAN);
//
//        YAxis yAxis = mChart.getAxisLeft();
//        yAxis.setTextColor(Color.WHITE);
//
//        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTextColor(Color.YELLOW);
//
//
//        LineData lineData = new LineData(dataSet);
//        mChart.setData(lineData);
//        mChart.invalidate(); // refresh
    }

    private void updateChart(String stockSymbol) {

        final int MAX_POINTS = 6;

        Cursor cursor = getContentResolver().query(
                Contract.Quote.URI,
                null,
                Contract.Quote.COLUMN_SYMBOL + " = ?",
                new String[] { stockSymbol },
                null
        );

        //Date[] dates = new Date
        cursor.moveToFirst();

        if(cursor != null) {
            String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
            String history = cursor.getString(Contract.Quote.POSITION_HISTORY);

            String[] historyArray = history.split("\n");
            float[] dateHistory = new float[historyArray.length];
            float[] priceHistory = new float[historyArray.length];
            String[] formattedDateHistory = new String[historyArray.length];

            //for(String stockEntry : historyArray) {
            for(int i = 0; i < historyArray.length; i++) {
                String stockEntry = historyArray[i];
                String[] stockDateAndPrice = stockEntry.split(",");
                String dateInMillis = stockDateAndPrice[0];
                String price = stockDateAndPrice[1];

                priceHistory[i] = Float.parseFloat(price);
                dateHistory[i] = Float.parseFloat(dateInMillis);
                //SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(dateInMillis));
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String formattedDate = formatter.format(calendar.getTime());

                //formattedDateHistory[i] = formattedDate;

                //Log.d(LOG_TAG, "date/price: " + year + "/" + month + "/" + day + "/" + price);
                //Log.d(LOG_TAG, "date/price" + formattedDate + "/" + price);
            }

            //float[] stockHistory = getStockHistory("MRVL");

            float[] valuesX = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
            //float[] valuesX = dateHistory;
            //float[] valuesX = { dateHistory[0]/ 1000000F, dateHistory[1] / 1000000F, dateHistory[2] / 1000000F, dateHistory[5] / 1000000F};
            //float[] valuesY = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
            //float[] valuesY = {130, 100, 25, 45, 122, 100, 78, 89, 99, 102, 111, 120, 130};
            float[] valuesY = priceHistory;

            List<Entry> entries = new ArrayList<>();

            //Entry e = new Entry()

            for (int i = 0 ; i < valuesX.length ; i++) {
                Log.d(LOG_TAG, "MAX ITEM: " + (i * (valuesY.length / valuesX.length)));

                float dateInMillis = dateHistory[i * (valuesY.length / valuesX.length)];

                entries.add(new Entry(valuesX[i], valuesY[i * (valuesY.length / valuesX.length)]));

                SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long)dateInMillis);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String formattedDate = formatter.format(calendar.getTime());

                formattedDateHistory[i] = formattedDate;
            }

            //entries.add(new Entry(valuesX[0] / 1000000F, valuesY[0]));
            //entries.add(new Entry(valuesX[1] / 1000000F, valuesY[1]));
            //entries.add(new Entry(valuesX[2] / 1000000F, valuesY[2]));

//            entries.add(new Entry(1486972.8F, valuesY[0]));
//            entries.add(new Entry(1486368.0F, valuesY[1]));
//            entries.add(new Entry(1485763.2F, valuesY[2]));

//            entries.add(new Entry(1486972, valuesY[0]));
//            entries.add(new Entry(1486368, valuesY[1]));
//            entries.add(new Entry(1485763, valuesY[2]));

            //entries.add(new Entry(148697, valuesY[0]));
            //entries.add(new Entry(148636, valuesY[1]));
            //entries.add(new Entry(148576, valuesY[2]));

            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setCircleColor(Color.CYAN);
            dataSet.setValueTextColor(Color.WHITE);

            YAxis yAxis = mChart.getAxisLeft();
            yAxis.setTextColor(Color.WHITE);
            //yAxis.setGranularity(10f);
            //yAxis.setGranularityEnabled(true);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTextColor(Color.YELLOW);
            xAxis.setValueFormatter(new DateLabelFormatter(formattedDateHistory));
            //xAxis.setLabelCount(6);
            //xAxis.setGranularity(10f);


            LineData lineData = new LineData(dataSet);
            mChart.setData(lineData);
            mChart.invalidate(); // refresh

        }
        else {
            Log.d(LOG_TAG, "Invalid Stock Symbol selected: " + stockSymbol);
        }

    }


//    private float[] getStockHistory(String stockSymbol) {
//        Cursor cursor = getContentResolver().query(
//                Contract.Quote.URI,
//                null,
//                Contract.Quote.COLUMN_SYMBOL + " = ?",
//                new String[] { stockSymbol },
//                null
//                );
//
//        //Date[] dates = new Date
//        cursor.moveToFirst();
//
//        if(cursor != null) {
//            String symbol = cursor.getString(Contract.Quote.POSITION_SYMBOL);
//            String history = cursor.getString(Contract.Quote.POSITION_HISTORY);
//            //Log.d("LOLWUT: " + cursor.getString(Contract.Quote.POSITION_SYMBOL));
//            //Date date = new Date()
//            String[] historyArray = history.split("\n");
//            float[] priceHistory = new float[historyArray.length];
//
//            //for(String stockEntry : historyArray) {
//            for(int i = 0; i < historyArray.length; i++) {
//                String stockEntry = historyArray[i];
//                String[] stockDateAndPrice = stockEntry.split(",");
//                String dateInMillis = stockDateAndPrice[0];
//                String price = stockDateAndPrice[1];
//
//                priceHistory[i] = Float.parseFloat(price);
//
//                SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(Long.parseLong(dateInMillis));
//                int year = calendar.get(Calendar.YEAR);
//                int month = calendar.get(Calendar.MONTH);
//                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                String formattedDate = formatter.format(calendar.getTime());
//
//                //Log.d(LOG_TAG, "date/price: " + year + "/" + month + "/" + day + "/" + price);
//                Log.d(LOG_TAG, "date/price" + formattedDate + "/" + price);
//            }
//
//            return priceHistory;
//        }
//        else {
//            Log.d(LOG_TAG, "Invalid Stock Symbol selected: " + stockSymbol);
//        }
//
//        return null;
//    }
}
