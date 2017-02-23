package com.udacity.stockhawk.sync;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by lamdbui on 2/23/17.
 */

public class StockHistoryLoader extends AsyncTaskLoader<List<HistoricalQuote>> {

    public static final String LOG_TAG = StockHistoryLoader.class.getSimpleName();

    private String mStockSymbol;
    private List<HistoricalQuote> mStockHistoricalQuotes;

    public StockHistoryLoader(Context context, String stockSymbol) {
        super(context);

        mStockSymbol = stockSymbol;
        mStockHistoricalQuotes = new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        if(!mStockHistoricalQuotes.isEmpty()) {
            deliverResult(mStockHistoricalQuotes);
        }

        if(takeContentChanged() || mStockHistoricalQuotes.isEmpty()) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        mStockHistoricalQuotes = null;
        super.onReset();
    }

    @Override
    public List<HistoricalQuote> loadInBackground() {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, - 2);

        try {
            Stock stock = YahooFinance.get(mStockSymbol);

            mStockHistoricalQuotes = stock.getHistory(from, to, Interval.MONTHLY);

            return mStockHistoricalQuotes;
        }
        catch(IOException e) {
            Timber.e(LOG_TAG, "Error fetching stock history for: " + mStockSymbol);
        }

        return null;
    }
}
