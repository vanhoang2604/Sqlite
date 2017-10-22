package com.example.admin.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/22/2017.
 */

public class MyDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Contact_Manager";
    private static final String TABLE_NAME = "Contact";
    private static final String ID = "Contact_Id";
    private static final String NAME = "Contact_Name";
    private static final String PHONE = "Contact_Phone";
    private static final String ADDRESS = "Contact_Adress";
    private static final String DATE = "Contact_Date";
    private static final String TIME = "Contact_Time";
    private static final String GENDER = "Contact_Gender";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String script = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                PHONE + " TEXT, " +
                ADDRESS + " TEXT, " +
                DATE + " TEXT, " +
                TIME + " TEXT, " +
                GENDER + " TEXT" + ")";
        db.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public int addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getPhone());
        values.put(ADDRESS, contact.getAddress());
        values.put(DATE, contact.getDate());
        values.put(TIME, contact.getTime());
        values.put(GENDER, contact.getGender());

        db.insert(TABLE_NAME, null, values);

        db = this.getReadableDatabase();
        String id = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(id, null);
        if (cursor != null)
            cursor.moveToFirst();
        db.close();
        return Integer.parseInt(cursor.getString(0));
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, NAME, PHONE, ADDRESS, DATE, TIME, GENDER},
                ID + " =?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                , cursor.getString(4), cursor.getString(5), cursor.getString(6));

        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone(cursor.getString(2));
                contact.setAddress(cursor.getString(3));
                contact.setDate(cursor.getString(4));
                contact.setTime(cursor.getString(5));
                contact.setGender(cursor.getString(6));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int getNotesCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getPhone());
        values.put(ADDRESS, contact.getAddress());
        values.put(DATE, contact.getDate());
        values.put(TIME, contact.getTime());
        values.put(GENDER, contact.getGender());

        return db.update(TABLE_NAME, values, ID + " =?", new String[]{String.valueOf(contact.getId())});

    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " =?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public void deleteAllContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
