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

        float[] stockHistory = getStockHistory("MRVL");

        float[] valuesX = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //float[] valuesY = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
        float[] valuesY = stockHistory;

        List<Entry> entries = new ArrayList<Entry>();

        for (int i=0 ; i< valuesX.length ; i++) {
            entries.add(new Entry(valuesX[i], valuesY[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setCircleColor(Color.CYAN);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.YELLOW);


        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.invalidate(); // refresh
    }


    private float[] getStockHistory(String stockSymbol) {
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
            //Log.d("LOLWUT: " + cursor.getString(Contract.Quote.POSITION_SYMBOL));
            //Date date = new Date()
            String[] historyArray = history.split("\n");
            float[] priceHistory = new float[historyArray.length];

            //for(String stockEntry : historyArray) {
            for(int i = 0; i < historyArray.length; i++) {
                String stockEntry = historyArray[i];
                String[] stockDateAndPrice = stockEntry.split(",");
                String dateInMillis = stockDateAndPrice[0];
                String price = stockDateAndPrice[1];

                priceHistory[i] = Float.parseFloat(price);

                SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(dateInMillis));
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String formattedDate = formatter.format(calendar.getTime());

                //Log.d(LOG_TAG, "date/price: " + year + "/" + month + "/" + day + "/" + price);
                Log.d(LOG_TAG, "date/price" + formattedDate + "/" + price);
            }

            return priceHistory;
        }
        else {
            Log.d(LOG_TAG, "Invalid Stock Symbol selected: " + stockSymbol);
        }

        return null;
    }
}
