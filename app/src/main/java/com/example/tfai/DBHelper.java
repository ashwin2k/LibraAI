package com.example.tfai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String MYDATABASE_TABLE = "CONTACTS";

    public DBHelper(@Nullable Context context, @Nullable String name) {
        super(context, name, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table CONTACTS " +
                        "(id INTEGER primary key AUTOINCREMENT, contact VARCHAR(10))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean insertValue(String contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact",contact);
        db.insert("CONTACTS",null,contentValues);
        return true;
    }

    public ArrayList<String> getAllContacts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + MYDATABASE_TABLE, null);

        ArrayList<String> contacts = new ArrayList<>();
        if (cursor.moveToFirst()){
            contacts.add(cursor.getString(cursor.getColumnIndex("contact")));
            while(cursor.moveToNext()){
                contacts.add(cursor.getString(cursor.getColumnIndex("contact")));
            }
        }
        cursor.close();
        db.close();
        Log.d("Sqliteacti", contacts.toString());
        return contacts;
    }
}
