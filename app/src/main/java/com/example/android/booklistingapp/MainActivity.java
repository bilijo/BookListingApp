package com.example.android.booklistingapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Update the information displayed to the user.
        BookListAsyncTask task = new BookListAsyncTask();
        task.execute(BOOKS_API_URL);
    }

    /**
     * Update the UI with the given bookFind information.
     */
    public void updateUi(Event bookFind) {
        TextView titleTexView = (TextView) findViewById(R.id.text_title_book);
        titleTexView.setText(bookFind.title);

        TextView authorTextView = (TextView) findViewById(R.id.text_author_book);
        authorTextView.setText(bookFind.authorName);

        TextView publisherTextView = (TextView) findViewById(R.id.text_publisher_book);
        publisherTextView.setText(bookFind.publisher);
    }

    /*The three types used by an asynchronous task are the following:
    Params, the type of the parameters sent to the task upon execution.
      Progress, the type of the progress units published during the background computation.
       Result, the type of the result of the background computation.
    Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void: */
    private class BookListAsyncTask extends AsyncTask<String, Void, Event> {

        @Override
        protected Event doInBackground(String... Urls) {
            // Perform the HTTP request for earthquake data and process the response.
            Event result = Utils.fetchBookData(Urls[0]);
            Log.i("MainActivity", "doInBackground: " + result);
            return result;
        }

        @Override
        protected void onPostExecute(Event result) {
            updateUi(result);
        }
    }


}
