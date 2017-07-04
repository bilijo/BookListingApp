package com.example.android.booklistingapp;

/**
 * Created by dam on 03.07.2017.
 */

public class Event {

    /** Title of the earthquake event */
    public final String title;

    /** Number of people who felt the earthquake and reported how strong it was */
    public final String authorName;

    /** Perceived strength of the earthquake from the people's responses */
    public final String publisher;

    /**
     * Constructs a new {@link Event}.
     *
     * @param bookTitle is the title of the earthquake event
     * @param vAuthorName is the number of people who felt the earthquake and reported how
     *                         strong it was
     * @param nameOfPublisher is the perceived strength of the earthquake from the responses
     */
    public Event(String bookTitle, String vAuthorName, String nameOfPublisher) {
        title = bookTitle;
        authorName = vAuthorName;
        publisher = nameOfPublisher;
    }
}
