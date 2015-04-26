package com.example.jaishmael.bookerapp;

import java.util.ArrayList;

/**
 * Created by Evea on 4/23/2015.
 */
public class Book {
    private String title;
    private String author;
    private String isbn;
    private String year;
    private String coverid;
    private ArrayList tags;


    public Book(String title, String author, String isbn, String year, String coverid, ArrayList tags) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.year = year;
        this.coverid = coverid;
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

    public String getCoverid(){return coverid;}

    public ArrayList getTags() {
        return tags;
    }
}
