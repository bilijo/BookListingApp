package com.example.android.booklistingapp;

/**
 * Created by dam on 05.07.2017.
 */

public class BooksData {
    // Variables of the BooksData class

    private String mBookTitle;
    private String mAuthor;
    private String mPublisher;

    // Constructor of the BooksData class
    public BooksData(String vTitle,String vAuthor,String vPublisher) {

     mBookTitle = vTitle;
     mAuthor = vAuthor;
     mPublisher = vPublisher;

    }

    // getters
    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }
}
