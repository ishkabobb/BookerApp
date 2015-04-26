package com.example.jaishmael.booker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Evea on 4/13/2015.
 */
public class myDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myAuthors.db";
    public static final String TABLE_AUTHORS = "Authors";
    public static final String COLUMN_ID = "mName";
    public static final String COLUMN_AUTHORNAME = "authorname";

    public myDBHandler(Context context, String name,
                       SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_AUTHORS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AUTHORNAME + " TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORS + ";");
        onCreate(db);
    }


    public boolean addAuthor(Author a){
        SQLiteDatabase db = getWritableDatabase();
        String authorName = a.getmName();
        String query = "SELECT * FROM " + TABLE_AUTHORS + " WHERE " + COLUMN_AUTHORNAME +"=\"" + authorName + "\";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if(c.getCount()==0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AUTHORNAME, a.getmName());
            db.insert(TABLE_AUTHORS, null, values);
            db.close();
            return true;
        }
        else{
            db.close();
            return false;
        }
    }


    public void deleteAuthor(String authorName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AUTHORS + " WHERE " +
                COLUMN_AUTHORNAME + "=\"" + authorName + "\";");
    }


    public ArrayList<String> databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> al = new ArrayList<String>();
        String query = "SELECT * FROM " + TABLE_AUTHORS + " WHERE 1 ORDER BY " + COLUMN_AUTHORNAME + " ASC;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("authorname")) != null) {
                al.add(c.getString(c.getColumnIndex("authorname")));
            }
            c.moveToNext();
        }
        db.close();
        return al;
    }
}
