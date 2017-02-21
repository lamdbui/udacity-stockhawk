package com.udacity.stockhawk.sync;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by lamdbui on 2/19/17.
 */

public class FetchStockHistoryTask extends AsyncTask<String, Void, List<HistoricalQuote>> {

    public static final String LOG_TAG = FetchStockHistoryTask.class.getSimpleName();

    private OnCompletedFetchStockHistoryTaskListener mCallback;

    // Calling Activity or Fragment must implement this!
    public interface OnCompletedFetchStockHistoryTaskListener {
        public void completedFetchStockHistoryTask(List<HistoricalQuote> stockHistory);
    }

    public FetchStockHistoryTask(OnCompletedFetchStockHistoryTaskListener callback) {
        super();
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(List<HistoricalQuote> historicalQuotes) {
        super.onPostExecute(historicalQuotes);

        mCallback.completedFetchStockHistoryTask(historicalQuotes);
    }

    @Override
    protected List<HistoricalQuote> doInBackground(String... params) {

        String stockSymbol = params[0];

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, - 2);

        try {
            Stock stock = YahooFinance.get(stockSymbol);

            return stock.getHistory(from, to, Interval.MONTHLY);
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "Error fetching stock history for: " + stockSymbol);
        }

        return null;
    }
}
