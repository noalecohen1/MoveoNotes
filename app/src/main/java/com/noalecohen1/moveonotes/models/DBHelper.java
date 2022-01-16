package com.noalecohen1.moveonotes.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoveoNotesDB.db";
    public static final String NOTES_TABLE_NAME = "Notes";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " (id TEXT PRIMARY KEY, userId TEXT NOT NULL, title TEXT, body TEXT, date TEXT NOT NULL, latitude REAL NOT NULL, longitude REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
        onCreate(db);
    }

    public void addNote(Note note, String userId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", note.getId());
        contentValues.put("userId", userId);
        contentValues.put("title", note.getTitle());
        contentValues.put("body", note.getBody());
        String date = getStringDate(note);
        contentValues.put("date", date);
        contentValues.put("latitude", note.getLatitude());
        contentValues.put("longitude", note.getLongitude());

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.insert(NOTES_TABLE_NAME, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void updateNote(Note note){
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("body", note.getBody());
        String date = getStringDate(note);
        contentValues.put("date", date);
        contentValues.put("latitude", note.getLatitude());
        contentValues.put("longitude", note.getLongitude());

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.update(NOTES_TABLE_NAME,contentValues,"id=?",new String[]{note.getId()});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteNote(String noteId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete(NOTES_TABLE_NAME,"id=?", new String[]{noteId});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Note> getAllNotesByUserId(String userId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] columns = {"id","title","body","date","latitude","longitude"};
        Cursor cursor = db.query(NOTES_TABLE_NAME,columns,"userId=?",new String[]{userId},null,null,"date DESC");

        ArrayList<Note> result = new ArrayList<Note>();

        if(cursor.getCount() == 0){
            db.setTransactionSuccessful();
            db.endTransaction();
            return result;
        }

        Integer idColumn = cursor.getColumnIndex("id");
        Integer titleColumn = cursor.getColumnIndex("title");
        Integer bodyColumn = cursor.getColumnIndex("body");
        Integer dateColumn = cursor.getColumnIndex("date");
        Integer latitudeColumn = cursor.getColumnIndex("latitude");
        Integer longitudeColumn = cursor.getColumnIndex("longitude");

        while(cursor.moveToNext()){
            Note note = new Note();
            note.setId(cursor.getString(idColumn));
            note.setTitle(cursor.getString(titleColumn));
            note.setBody(cursor.getString(bodyColumn));
            Calendar calendar = Calendar.getInstance();
            Date date = null;
            try{
                date = new SimpleDateFormat("yyyy/MM/dd").parse(cursor.getString(dateColumn));
                calendar.setTime(date);
            }catch (Exception e){
                Log.e("ERROR", e.getMessage());
            }
            note.setCalender(calendar);
            note.setLatitude(cursor.getDouble(latitudeColumn));
            note.setLongitude(cursor.getDouble(longitudeColumn));
            result.add(note);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        return result;
    }

    public String getStringDate(Note note){
        Calendar calendar = note.getCalender();
        StringBuilder sb = new StringBuilder();
        return sb.append(String.format("%04d",calendar.get(Calendar.YEAR)))
                .append("/").append(String.format("%02d",calendar.get(Calendar.MONTH)+1))
                .append("/").append(String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH)))
                .toString();
    }

}
