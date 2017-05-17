package com.example.android.booklisting;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from Google Books.
 */

public class QueryUtils {

    /** Keys for JSON parsing */
    private static final String KEY_VOLUMEINFO = "volumeInfo";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHORS = "authors";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PAGECOUNT = "pageCount";
    private static final String KEY_IMAGELINKS = "imageLinks";
    private static final String KEY_SMALLTHUMBNAIL = "smallThumbnail";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_PREVIEWLINK = "previewLink";

    public static String errorMessage = null;

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static Context mContext = MainActivity.mContext;
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<BookItem> fetchBookData(String requestURL) {
        // Create URL object
        URL url = createURL(requestURL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        // Parse JSON string and create an {@ArrayList<BookItem>} object
        ArrayList<BookItem> bookItems = extractBookData(jsonResponse);

        return bookItems;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    public static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Creating URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHTTPRequest(URL url) throws IOException {

        // If the url is empty, return early
        String jsonResponse = null;
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200), then read the input stream and
            // parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Google Books JSON results", e);
        } finally {
            // Close connection
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            // Close stream
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputstream) throws IOException {
        StringBuilder streamOutput = new StringBuilder();
        if (inputstream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputstream, Charset
                    .forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                streamOutput.append(line);
                line = reader.readLine();
            }
        }
        return streamOutput.toString();
    }

    private static ArrayList<BookItem> extractBookData(String bookDataJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookDataJSON)) {
            return null;
        }

        // Create empty ArrayList in which the parsed data will be added
        ArrayList<BookItem> bookItems = new ArrayList<BookItem>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            // Build a list of BookItem Objects
            JSONObject baseJSONObject = new JSONObject(bookDataJSON);
            JSONArray bookArray = baseJSONObject.getJSONArray("items");

            // Variables for JSON parsing
            String title;
            JSONArray authors;
            String author;
            String description;
            int pageCount;
            JSONObject imageLinks;
            String smallThumbnail;
            String language;
            String previewLink;

            for (int i = 0; i < bookArray.length(); i++) {
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject(KEY_VOLUMEINFO);
                // Check if key "title" exists and if yes, return value
                if (volumeInfo.has(KEY_TITLE)) {
                    title = volumeInfo.getString(KEY_TITLE);
                } else {
                    title = null;
                }
                // Check if key "authors" exists and if yes, return value
                if (volumeInfo.has(KEY_AUTHORS)) {
                    authors = volumeInfo.getJSONArray(KEY_AUTHORS);
                    author = authors.getString(0);
                } else {
                    author = null;
                }

                // Check if key "description" exists and if yes, return value
                if (volumeInfo.has(KEY_DESCRIPTION)) {
                    description = volumeInfo.getString(KEY_DESCRIPTION);
                } else {
                    description = null;
                }

                // Check if key "pageCount" exists and if yes, return value
                if (volumeInfo.has(KEY_PAGECOUNT)) {
                    pageCount = volumeInfo.getInt(KEY_PAGECOUNT);
                } else {
                    pageCount = 0;
                }

                // Check if key "imageLinks" exists and if yes, return value
                if (volumeInfo.has(KEY_IMAGELINKS)) {
                    imageLinks = volumeInfo.getJSONObject(KEY_IMAGELINKS);
                    smallThumbnail = imageLinks.getString(KEY_SMALLTHUMBNAIL);
                } else {
                    smallThumbnail = null;
                }

                // Check if key "language" exists and if yes, return value
                if (volumeInfo.has(KEY_LANGUAGE)) {
                    language = volumeInfo.getString(KEY_LANGUAGE);
                } else {
                    language = null;
                }

                // Check if key "previewLink" exists and if yes, return value
                if (volumeInfo.has(KEY_PREVIEWLINK)) {
                    previewLink = volumeInfo.getString(KEY_PREVIEWLINK);
                } else {
                    previewLink = null;
                }

                // Create the BookItem object and add it to the ArrayList
                BookItem bookItem = new BookItem(smallThumbnail, previewLink, title, author,
                        pageCount, language, description);
                bookItems.add(bookItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the BookItem JSON results", e);
            errorMessage = "Problem parsing the BookItem JSON results. JSONExcelption: " + e;
        }

        // Return the list of BookItems
        return bookItems;
    }
}
