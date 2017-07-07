package com.example.android.booklistingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.widget.Toast.LENGTH_SHORT;
import static com.example.android.booklistingapp.R.id.listView_books;

public class MainActivity extends AppCompatActivity {

    // url to perform search book by author name
    public String google_books_Api_url = "https://www.googleapis.com/books/v1/volumes?q=authors%20";
    String google_books_Api_url2;

 /** TextView that is displayed when the list is empty */
  private TextView mEmptyStateTextView;



    // Make adapter and listview instances as global variable
    private BooksDataAdapter mAdapter;
    private ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




       final EditText editTextView = (EditText) findViewById(R.id.editText_toSearch);

        //When the button search is pressed, set a click listener to launch the search
        final Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(editTextView.getText().toString().isEmpty()) {
                    // editText is empty
                    Toast.makeText(MainActivity.this, "editText is empty ", LENGTH_SHORT).show();

                } else {
                // editText is not empty

                    String stringToSearch = String.valueOf(editTextView.getText());
                    stringToSearch = stringToSearch.replace(" ", "%20");

                    google_books_Api_url2 = google_books_Api_url + stringToSearch;


                    // Start the AsyncTask to fetch the book data
                    BookListAsyncTask task = new BookListAsyncTask();
                    task.execute(google_books_Api_url2);
            }

            }
        });

        // Find a reference to the {@link ListView} in the layout
        booksListView = (ListView) findViewById(listView_books);

        // Create a new adapter that takes an empty list of book as input
        mAdapter = new BooksDataAdapter(this, new ArrayList<BooksData>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        //booksListView.setAdapter(mAdapter);

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
            // ArrayList<BooksData> result = new  ArrayList<BooksData>;

            Log.d("MainActivity", "urlLength: " + urlLength);
            // Perform the HTTP request for book data and process the response.

            if (urlLength < 1 || Urls[0] == null) {
                return null;
            }
            ArrayList<BooksData> result = (ArrayList<BooksData>) Utils.fetchBookData(Urls[0]);
           // mAdapter.notifyDataSetChanged();
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<BooksData> result) {
            mEmptyStateTextView = (TextView) findViewById(R.id.text_notfound);
            mAdapter.clear();


            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (result != null && !result.isEmpty()) {
                mEmptyStateTextView.setVisibility(View.GONE);
                mAdapter.addAll(result);
            } else {


                mEmptyStateTextView.setVisibility(View.VISIBLE);
                booksListView.setEmptyView(mEmptyStateTextView);
            }

            //mAdapter.add(result);
            booksListView.setAdapter(mAdapter);


        }

    }


}
