package com.example.majorassignment_2_studentinfo;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database and Table Name Strings.
    public static final String DATABASE_NAME = "studentInfo.db";
    public static final String TABLE_NAME = "students";

    public DatabaseHelper( Context context) { super(context, DATABASE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(name TEXT, number INTEGER, email TEXT PRIMARY KEY, course TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // Function to Insert Data.
    public Boolean Insert(String name, String number, String email, String course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("number", number);
        contentValues.put("email", email);
        contentValues.put("course", course);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1 ){
            return false;
        } else { return  true; }
    }

    // Function to check if email already exists.
    public Boolean CheckEmailExist(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email=?", new String[]{email});
        if(cursor.getCount() > 0){
            // Already exists.
            return true;
        }else{
            return false;
        }
    }

    // Function to check if Student Number already exists.
    public Boolean CheckNumberExist(String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE number=?", new String[]{number});
        if(cursor.getCount() > 0){
            // Already exists.
            return true;
        }else{
            return false;
        }
    }

    // Function to Delete Data
    public Boolean deleteOne(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE email = \"" + email + "\"";

        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return false;
        } else { return true; }
    }


    public List<StudentModel> getStudentList() {

        List<StudentModel> returnList = new ArrayList<>();

        String queryString = "SELECT name, number, email, course FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                String email = cursor.getString(2);
                String course = cursor.getString(3);

                StudentModel newStudent = new StudentModel(name, number, email, course);
                returnList.add(newStudent);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }
}
