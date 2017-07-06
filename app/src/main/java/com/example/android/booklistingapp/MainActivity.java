package com.example.android.booklistingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";
   /*public String google_books_Api_url = "https://www.googleapis.com/books/v1/volumes?q=";
    String isbnToFind = "ISBN	2820515193, 9782820515193";
   String google_books_Api_url2  = google_books_Api_url + isbnToFind; */

    // Make adapter and listview instances as global variable
    private BooksDataAdapter mAdapter;
    private ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //When the button search is pressed, set a click listener to launch the search
        final Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "searchButton", Toast.LENGTH_LONG).show();

            }
        });

        // Find a reference to the {@link ListView} in the layout
        booksListView = (ListView) findViewById(R.id.listView_books);

        // Create a new adapter that takes an empty list of book as input
        mAdapter = new BooksDataAdapter(this, new ArrayList<BooksData>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(mAdapter);

        // Start the AsyncTask to fetch the book data
        BookListAsyncTask task = new BookListAsyncTask();
        task.execute(BOOKS_API_URL);

    }

    /*The three types used by an asynchronous task are the following:
    Params, the type of the parameters sent to the task upon execution.
      Progress, the type of the progress units published during the background computation.
       Result, the type of the result of the background computation.
    Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void: */

    private class BookListAsyncTask extends AsyncTask<String, Void, BooksData> {

        @Override
        protected BooksData doInBackground(String... Urls) {
            // Perform the HTTP request for book data and process the response.
            BooksData result = Utils.fetchBookData(Urls[0]);
            Log.d("MainActivity", "doInBackground: " + result);

            return result;
        }

        @Override
        protected void onPostExecute(BooksData result) {
            Log.d("MainActivity", "onPostExecute: " + result);
            mAdapter.add(result);
   booksListView.setAdapter(mAdapter);



        }

    }


}
