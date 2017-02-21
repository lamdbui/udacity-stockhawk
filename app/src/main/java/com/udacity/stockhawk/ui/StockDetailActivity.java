package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.collect.Lists;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;
import com.udacity.stockhawk.sync.FetchStockHistoryTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class StockDetailActivity extends AppCompatActivity
    implements FetchStockHistoryTask.OnCompletedFetchStockHistoryTaskListener {

    public static final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    public static final String EXTRA_STOCK_SYMBOL = "com.udacity.stockhawk.extra_stock_symbol";

    private String mStockSymbol;

    private List<String> mDateHistory;
    private List<Float> mPriceHistory;

    @BindView(R.id.stock_chart)
    public LineChart mChart;
    @BindView(R.id.stock_symbol)
    public TextView mStockSymbolTextView;

    @Override
    public void completedFetchStockHistoryTask(List<HistoricalQuote> stockHistory) {
        for(HistoricalQuote h : stockHistory) {

            SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MMM");
            Calendar calendar = h.getDate();
            String formattedDate = formatter.format(calendar.getTime());

            mDateHistory.add(formattedDate);
            mPriceHistory.add(h.getClose().floatValue());
        }

        // reverse the order so it is ascending
        mDateHistory = Lists.reverse(mDateHistory);
        mPriceHistory = Lists.reverse(mPriceHistory);

        updateUi(mStockSymbol);
    }

    public static Intent newIntent(Context packageContext, String stockSymbol) {
        Intent intent = new Intent(packageContext, StockDetailActivity.class);
        intent.putExtra(EXTRA_STOCK_SYMBOL, stockSymbol);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        ButterKnife.bind(this);

        mStockSymbol = getIntent().getStringExtra(EXTRA_STOCK_SYMBOL);

        mDateHistory = new ArrayList<>();
        mPriceHistory = new ArrayList<>();

        mStockSymbolTextView.setText(mStockSymbol);

        FetchStockHistoryTask fetchStockHistoryTask = new FetchStockHistoryTask(this);
        fetchStockHistoryTask.execute(mStockSymbol);
    }

    private void updateUi(String stockSymbol) {
        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < mPriceHistory.size(); i++) {
            entries.add(new Entry(i, mPriceHistory.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "PRICE HISTORY");
        dataSet.setCircleColor(Color.CYAN);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12.0f);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12.0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.YELLOW);
        xAxis.setTextSize(12.0f);
        xAxis.setValueFormatter(new DateLabelFormatter(mDateHistory));
        xAxis.setGranularity(2.0f);
        xAxis.setGranularityEnabled(true);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.invalidate();

    }
}
