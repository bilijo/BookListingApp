package com.example.android.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.booklistingapp.R.id.listView_books;

public class MainActivity extends AppCompatActivity {

    // Variable to use in the saving state method
    static final String SEARCH_NAME = "string To Search";

    public String stringToSearch = "";

    // url to perform search book by author name
    public String google_books_Api_url = "https://www.googleapis.com/books/v1/volumes?q=authors%20";

    // variable which will concatenate the url abobe with the author name given by the user
    String google_books_Api_url2;

    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;

    // Make adapter and listview instances as global variable
    private BooksDataAdapter mAdapter;
    private ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = this;



     
        // hide keyboard on the UI device then didn't needed
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final EditText editTextView = (EditText) findViewById(R.id.editText_toSearch);

        // Query the active network and determine if it has Internet connectivity.
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
        /* When the button search is pressed, set a click listener to launch the search
             only if there is a value in the EditText view
         */
            final Button searchButton = (Button) findViewById(R.id.button_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (editTextView.getText().toString().isEmpty()) {
                        // Inform user when EditText is empty
                        Toast.makeText(MainActivity.this, "EditText is empty ", LENGTH_SHORT).show();

                    } else {
                        // When EditText is not populated with chars replace blank space by %20 to perform search
                        stringToSearch = String.valueOf(editTextView.getText());
                        stringToSearch = stringToSearch.replace(" ", "%20");
                        // Link the author name given by the user to the google API url
                        google_books_Api_url2 = google_books_Api_url + stringToSearch;

                        // Start the AsyncTask to fetch the book's data
                        BookListAsyncTask task = new BookListAsyncTask();
                        task.execute(google_books_Api_url2);

                        // Set the adapter on the {@link ListView}
                        // so the list can be diplayed in the user interface
                        booksListView.setAdapter(mAdapter);
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Internet isn't connected,\n then please connect and try again  ", LENGTH_SHORT).show();
            mEmptyStateTextView = (TextView) findViewById(R.id.text_emptyView);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Create a reference to the {@link listView_books} in the layout
        booksListView = (ListView) findViewById(listView_books);

        // Create a new adapter instance that takes an empty list of book as input
        mAdapter = new BooksDataAdapter(this, new ArrayList<BooksData>());


    }

    // Saving and restoring activity state
    protected void onSaveInstanceState (Bundle savedInstanceState){
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        // Save custom values into the bundle
        savedInstanceState.putString(SEARCH_NAME, stringToSearch);

    }

    public void onRestoreInstanceState (Bundle savedInstanceState){
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state search from saved instance
        stringToSearch = savedInstanceState.getString(SEARCH_NAME);
        // Start the AsyncTask to fetch the book's data
        BookListAsyncTask task = new BookListAsyncTask();
        task.execute(google_books_Api_url2);
        booksListView.setAdapter(mAdapter);

    }


    /*The three types used by an asynchronous task are the following:
    Params, the type of the parameters sent to the task upon execution.
      Progress, the type of the progress units published during the background computation.
       Result, the type of the result of the background computation.
    Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void: */

    private class BookListAsyncTask extends AsyncTask<String, Void, ArrayList<BooksData>> {
        @Override
        // Here we can pass several variables like url of strings datas
        // these intput variables are stored into an Array called Urls
        protected ArrayList<BooksData> doInBackground(String... Urls) {

            int urlLength = Urls.length;
            // Check an available url
            if (urlLength < 1 || Urls[0] == null) {
                return null;
            }
            ArrayList<BooksData> result = (ArrayList<BooksData>) Utils.fetchBookData(Urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<BooksData> result) {
            // Clear previous list
            mAdapter.clear();
            mEmptyStateTextView = (TextView) findViewById(R.id.text_emptyView);
            // If there is a valid list of {@link BooksData}, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (result != null && !result.isEmpty()) {
                mEmptyStateTextView.setVisibility(View.GONE);
                mAdapter.addAll(result);

            } else {
                // Create a reference to the emptyView

                mEmptyStateTextView.setVisibility(View.VISIBLE);
                booksListView.setEmptyView(mEmptyStateTextView);
            }


        }
    }
}
