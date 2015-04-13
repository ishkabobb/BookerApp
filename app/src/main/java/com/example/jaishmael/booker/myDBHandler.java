package com.example.jaishmael.booker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    //Add new row to table
    public void addProduct(Author a){
        ContentValues values = new ContentValues();
        values.put(COLUMN_AUTHORNAME, a.getmName());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_AUTHORS, null, values);

        db.close();
    }

    //Delete row from the database
    public void deleteProduct(String authorName){
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_AUTHORS + " WHERE " +
                COLUMN_AUTHORNAME + "=\"" + authorName + "\";");
    }

    //toString method
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_AUTHORS + " WHERE 1;";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("authorname")) != null) {
                dbString += c.getString(c.getColumnIndex("authorname"));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
}
