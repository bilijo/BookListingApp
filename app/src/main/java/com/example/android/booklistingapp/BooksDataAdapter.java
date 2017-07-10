package com.example.android.booklistingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dam on 05.07.2017.
 */

public class BooksDataAdapter extends ArrayAdapter<BooksData>{

    //Allows access to application-specific resources and classes, as well as up-calls
    private Context context;

    // Constructor
      /* @param context The current context. Used to inflate the layout file.
      * @param booksDatasArrayList A List of BooksData objects to display in a list
     */
    public BooksDataAdapter(Context context, ArrayList<BooksData> booksDatasArrayList) {
        super(context, 0, booksDatasArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            // use the layout of the data items to be displayed
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_book_data, parent, false);
        }
        // Get the {@link BooksData} object located at this position in the list
        BooksData currentBooksData = getItem(position);

        // Add book title with getter getmBookTitle()
        TextView titleTexView = (TextView) listItemView.findViewById(R.id.text_title_book);
        titleTexView.setText(currentBooksData.getmBookTitle());

        // Add book author with getter getmAuthor()
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.text_author_book);
        authorTextView.setText(currentBooksData.getmAuthor());

        // Add book publisher with getter getmPublisher()
        TextView publisherTextView = (TextView) listItemView.findViewById(R.id.text_publisher_book);
        publisherTextView.setText(currentBooksData.getmPublisher());
    // return the populated listView to show in the UI
    return listItemView;
    }



}
