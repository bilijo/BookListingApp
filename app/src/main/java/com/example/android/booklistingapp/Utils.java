package com.example.android.booklistingapp;

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
import java.util.List;

/**
 * Created by dam on 03.07.2017.
 */

public class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();
    // Constant used to check items to be displayed : title, author and publisher
    static String TITLE_KEY = "title";
    static String AUTHOR_KEY = "author";
    static String PUBLISHER_KEY = "publisher";

    /**
     * Query the Books API dataset and return an {@link BooksData} object to represent a single book data.
     */
    public static List<BooksData> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link BooksData} object
        List<BooksData> book = extractFeatureFromJson(jsonResponse);

        // Return the {@link BooksData}
        Log.d(LOG_TAG, "extractFeatureFromJson" + book);
        return book;
    }

    /*
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
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

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book api JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /*
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /*
     * Return an {@link BooksData} object by parsing out information
     * about the first book from the input bookApiJSON string.
     */
    private static ArrayList<BooksData> extractFeatureFromJson(String bookApiJSON) {
        ArrayList<BooksData> listOfBooks = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookApiJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(bookApiJSON);
            if (baseJsonResponse.has("items")) {

                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

                // If there are results in the features array
                if ((itemsArray.length()) > 0 && (baseJsonResponse.has("items"))) {
                    // Retrieve all items what are needed to be shown
                    for (int i = 0; i < itemsArray.length(); i++) {

                        // Extract out the first feature (which is a book info)
                        JSONObject firstFeature = itemsArray.getJSONObject(i);
                        JSONObject properties = firstFeature.getJSONObject("volumeInfo");

                        String title = " ";
                        // Extract out the title, author, and publisher  values
                        if (properties.has(TITLE_KEY)) {
                            if (!properties.getString(TITLE_KEY).isEmpty()) {
                                title = properties.getString(TITLE_KEY);
                            }
                        } else title = "N/A";


                        String authorName = " ";
                        if (properties.has(AUTHOR_KEY)) {
                            if (!properties.getString(AUTHOR_KEY).isEmpty()) {
                                authorName = properties.getString(AUTHOR_KEY).replaceAll("[\\[\\]]", "");
                                authorName = authorName.replaceAll("\"", "");

                            }
                        } else authorName = "N/A";

                        String publisher = " ";
                        if (properties.has(PUBLISHER_KEY)) {
                            if (!properties.getString(PUBLISHER_KEY).isEmpty()) {
                                publisher = properties.getString(PUBLISHER_KEY);
                            }
                        } else publisher = "N/A";

                        // Create a new {@link BooksData} object
                        BooksData bookObject = new BooksData(title, authorName, publisher);
                        listOfBooks.add(bookObject);
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        // return null;
        Log.d(LOG_TAG, "listOfBooks  " + listOfBooks);
        return listOfBooks;
    }
}
