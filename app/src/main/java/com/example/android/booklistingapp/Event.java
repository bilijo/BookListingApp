package com.example.android.booklistingapp;

/**
 * Created by dam on 03.07.2017.
 */

public class Event {

    /** Title of the book event */
    public final String title;

    /** Author who wrote the book  */
    public final String authorName;

    /** Perceived strength of the earthquake from the people's responses */
    public final String publisher;

    /**
     * Constructs a new {@link Event}.
     *
     * @param bookTitle is the title of the book event
     * @param vAuthorName is the author of the book
     *
     * @param nameOfPublisher is the publisher of the book
     */
    public Event(String bookTitle, String vAuthorName, String nameOfPublisher) {
        title = bookTitle;
        authorName = vAuthorName;
        publisher = nameOfPublisher;
    }
}
