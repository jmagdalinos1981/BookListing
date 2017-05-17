package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;


/**
 * Loads a list of BookItems by using an AsyncTask to perform the network request to the given URL.
 */

public class BookItemLoader extends AsyncTaskLoader<ArrayList<BookItem>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BookItemLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookItemLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookItemLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public ArrayList<BookItem> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        ArrayList<BookItem> bookItems = QueryUtils.fetchBookData(mUrl);
        return bookItems;
    }
}
