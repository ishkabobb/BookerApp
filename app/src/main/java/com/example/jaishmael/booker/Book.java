package com.example.jaishmael.booker;

import java.util.ArrayList;

/**
 * Created by Evea on 4/23/2015.
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private String year;
    private ArrayList tags;


    public Book(String title, String author, String isbn, String year, ArrayList tags) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getYear() {
        return year;
    }

    public ArrayList getTags() {
        return tags;
    }
}
