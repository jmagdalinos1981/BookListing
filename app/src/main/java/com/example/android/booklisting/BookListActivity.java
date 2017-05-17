package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The {@link BookListActivity} is called by the search button to show the list of books the
 * search returned.
 */

public class BookListActivity extends AppCompatActivity implements
        LoaderCallbacks<ArrayList<BookItem>> {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /** Adapter for the List */
    private BookItemAdapter bookAdapter;

    /**
     * Constant value for the book loader ID.
     */
    private static final int BOOK_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** Progress Bar to use while running query */
    private ProgressBar progressBar;

    /** String to store the user's input */
    private String userAuthorQuery;
    private String userTitleQuery;

    /** String used to store number of results as per user's preference */
    private String maxResults;

    /** Final Query */
    private String finalQuery;

    /** Initial Query which will be combined with the user's input*/
    private final String API_INITIAL_QUERY = "https://www.googleapis" +
            ".com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list);

        // Get the user's input
        userTitleQuery = MainActivity.userSearchInputTitle;
        userAuthorQuery = MainActivity.userSearchInputAuthor;
        // Get the number of results as per user's preference
        maxResults = MainActivity.resultNumber;

        // Call method to combine the user's input with the API's initial Query
        finalQuery = returnFinalQuery(userTitleQuery, userAuthorQuery, maxResults);

        // Find the {@link ListView} object in the layout
        ListView listView = (ListView) findViewById(R.id.list);

        // Find the {@link EmptyView} object in the layout
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Create a new bookAdapter that takes the list of books as input
        bookAdapter = new BookItemAdapter(this, new ArrayList<BookItem>());

        // Make the {@link ListView} use the {@link BookItemAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link BookItem} in the list.
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current book that was clicked on
                BookItem currentBookItem = bookAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookURL = Uri.parse(currentBookItem.getmPreviewURL());

                // Create new intent to view the book's URL
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookURL);

                // Start the intent
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Disable progressBar
            View progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // Display error
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    public String returnFinalQuery(String titleInput, String authorInput, String maxResults) {
        // Split title input to words since spaces are not allowed in the Query
        String[] wordsTitle = titleInput.split("\\s+");
        String wordsToTitleQuery = null;
        for (int i = 0; i < wordsTitle.length; i++) {
            if (i == 0) {
                wordsToTitleQuery = wordsTitle[i];
            } else {
                wordsToTitleQuery = wordsToTitleQuery + "+" + wordsTitle[i];
            }
        }
        Log.v("BookListActivity", "Title: " + wordsToTitleQuery);

        // If the author is not empty, add parameters as per API
        if (TextUtils.isEmpty(wordsToTitleQuery)) {
            wordsToTitleQuery = "";
        } else {
            wordsToTitleQuery = "intitle:" + wordsToTitleQuery;
        }

        // Split author input to words since spaces are not allowed in the Query
        String[] wordsAuthor = authorInput.split("\\s+");
        String wordsToAuthorQuery = null;
        for (int i = 0; i < wordsAuthor.length; i++) {
            if (i == 0) {
                wordsToAuthorQuery = wordsAuthor[i];
            } else {
                wordsToAuthorQuery = wordsToAuthorQuery + "+" + wordsAuthor[i];
            }
        }
        // If the author is not empty, add parameters as per API
        if (TextUtils.isEmpty(wordsToAuthorQuery)) {
            wordsToAuthorQuery = "";
        } else if (TextUtils.isEmpty(wordsToTitleQuery)) {
            wordsToAuthorQuery = "inauthor:" + wordsToAuthorQuery;
        } else {
            wordsToAuthorQuery = "&inauthor:" + wordsToAuthorQuery;
        }
        Log.v("BookListActivity", "Author: " + wordsToAuthorQuery);

        finalQuery = API_INITIAL_QUERY + wordsToTitleQuery + wordsToAuthorQuery + "&maxResults=" + maxResults;
        Log.v("BookListActivity", "Query: " + finalQuery);
        return finalQuery;
    }

    @Override
    public Loader<ArrayList<BookItem>> onCreateLoader(int id, Bundle args) {
        // Create new loader for the given URL
        return new BookItemLoader(this, finalQuery);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<BookItem>> loader, ArrayList<BookItem> bookItems) {
        // Clear the adapter of previous earthquake data
        bookAdapter.clear();

        // If there is a valid list of {@link BookItem}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookItems != null && !bookItems.isEmpty()) {
            bookAdapter.addAll(bookItems);

            // Hide loading indicator because the data has been loaded
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

        } else {
            // Hide loading indicator because the data has been loaded
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);

            // Set empty state text to display "No books found!"
            mEmptyStateTextView.setText(R.string.no_books);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<BookItem>> loader) {
        // Loader reset, so we can clear out our existing data.
        bookAdapter.clear();
    }
}
