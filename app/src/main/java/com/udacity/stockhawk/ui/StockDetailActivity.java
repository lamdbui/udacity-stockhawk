package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.common.collect.Lists;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.StockHistoryLoader;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import yahoofinance.histquotes.HistoricalQuote;

public class StockDetailActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<HistoricalQuote>> {

    public static final String LOG_TAG = StockDetailActivity.class.getSimpleName();

    public static final int LOADER_STOCK_HISTORY = 0;

    public static final String EXTRA_STOCK_SYMBOL = "com.udacity.stockhawk.extra_stock_symbol";
    public static final String ARG_STOCK_SYMBOL = "stock_symbol";

    private String mStockSymbol;

    private List<String> mDateHistory;
    private List<Float> mPriceHistory;

    @BindView(R.id.stock_chart)
    public LineChart mChart;
    @BindView(R.id.stock_symbol)
    public TextView mStockSymbolTextView;

    @Override
    public Loader<List<HistoricalQuote>> onCreateLoader(int id, Bundle args) {
        return new StockHistoryLoader(this, args.getString(ARG_STOCK_SYMBOL));
    }

    @Override
    public void onLoadFinished(Loader<List<HistoricalQuote>> loader, List<HistoricalQuote> data) {
        if(data != null) {
            for(HistoricalQuote h : data) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM");
                Calendar calendar = h.getDate();
                String formattedDate = formatter.format(calendar.getTime());

                mDateHistory.add(formattedDate);
                mPriceHistory.add(h.getClose().floatValue());
            }

            // reverse the order so it is ascending
            mDateHistory = Lists.reverse(mDateHistory);
            mPriceHistory = Lists.reverse(mPriceHistory);

            updateUI();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<HistoricalQuote>> loader) {

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

        Bundle args = new Bundle();
        args.putString(ARG_STOCK_SYMBOL, mStockSymbol);
        getSupportLoaderManager().initLoader(LOADER_STOCK_HISTORY, args, this);
    }

    private void updateUI() {
        List<Entry> entries = new ArrayList<>();

        for(int i = 0; i < mPriceHistory.size(); i++) {
            entries.add(new Entry(i, mPriceHistory.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.chart_price_history));
        dataSet.setCircleColor(Color.CYAN);
        dataSet.setDrawValues(false);

        // turn off extra labels
        mChart.getAxisRight().setDrawLabels(false);
        mChart.setExtraTopOffset(16.0f);
        mChart.setExtraRightOffset(26.0f);
        Description description = new Description();
        description.setText("");
        mChart.setDescription(description);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTextSize(12.0f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12.0f);
        //xAxis.setValueFormatter(new DateLabelFormatter(mDateHistory));
        //xAxis.setGranularity(4.0f);
        //xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(6);

        Legend legend = mChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12.0f);

        LineData lineData = new LineData(dataSet);
        mChart.setData(lineData);
        mChart.invalidate();

    }
}
