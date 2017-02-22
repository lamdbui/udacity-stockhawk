package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by lamdbui on 2/21/17.
 */

public class StockWidgetRemoteViewsService extends RemoteViewsService {

    private static final String[] STOCK_COLUMNS = {
            Contract.Quote._ID,
            Contract.Quote.COLUMN_SYMBOL,
            Contract.Quote.COLUMN_PRICE,
            Contract.Quote.COLUMN_ABSOLUTE_CHANGE,
            Contract.Quote.COLUMN_PERCENTAGE_CHANGE
    };

    // these must match the projection
    public static final int POSITION_ID = 0;
    public static final int POSITION_SYMBOL = 1;
    public static final int POSITION_PRICE = 2;
    public static final int POSITION_ABSOLUTE_CHANGE = 3;
    public static final int POSITION_PERCENTAGE_CHANGE = 4;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor mData = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if(mData != null) {
                    mData.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                mData = getContentResolver().query(
                        Contract.Quote.URI,
                        STOCK_COLUMNS,
                        null,
                        null,
                        null
                );
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if(mData != null) {
                    mData.close();
                    mData = null;
                }
            }

            @Override
            public int getCount() {
                return (mData == null) ? 0 : mData.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i) {
                // get our layout for the item
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stock_list_item);

                if(mData.moveToPosition(i)) {
                    String stockSymbol = mData.getString(POSITION_SYMBOL);
                    float stockPrice = mData.getFloat(POSITION_PRICE);
                    float stockAbsoluteChange = mData.getFloat(POSITION_ABSOLUTE_CHANGE);
                    float stockPercentChange = mData.getFloat(POSITION_PERCENTAGE_CHANGE);
                    String stockPriceString = String.format("$%.2f", stockPrice);
                    String stockAbsoluteChangeString = Float.toString(stockAbsoluteChange);
                    String stockPercentChangeString = String.format("%.2f%%", stockPercentChange);

                    // bind our data
                    views.setTextViewText(R.id.widget_stock_symbol, stockSymbol);
                    views.setTextViewText(R.id.widget_price, stockPriceString);
                    views.setTextViewText(R.id.widget_change, stockPercentChangeString);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
                //return new RemoteViews(getPackageName(), R.layout.widget_stock_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
//                if(mData != null && mData.moveToPosition(i)) {
//                    return mData.getInt(POSITION_ID);
//                }
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
