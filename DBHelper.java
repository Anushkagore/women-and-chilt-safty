package com.example.womenchildsafetyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

//    public static final String TABLE_EMERGENCY_CONTACTS ="emergency_contacts" ;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "User Manager.db";

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USER_NAME = "user_name";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_USER_PHONE = "user_phone";
    public static final String COL_USER_GENDER = "user_gender";
    public static final String COL_USER_PASSWORD = "user_password";

    // Emergency contacts table
    public static final String TABLE_EMERGENCY_CONTACTS = "emergency_contacts";

    public static final String COL_CONTACT_ID = "contact_id";
    public static final String COL_CONTACT_USER_ID = "user_id";
    public static final String COL_CONTACT_NAME = "contact_name";
    public static final String COL_CONTACT_NUMBER = "contact_number";

    // Complaints table
    public static final String TABLE_COMPLAINTS = "complaints";
    public static final String COL_COMPLAINT_ID = "complaint_id";
    public static final String COL_COMPLAINT_USER_ID = "user_id";
    public static final String COL_COMPLAINT_TEXT = "complaint_text";

    // Create table SQL queries
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_USER_NAME + " TEXT NOT NULL,"
            + COL_USER_EMAIL + " TEXT NOT NULL UNIQUE,"
            + COL_USER_PHONE + " TEXT NOT NULL,"
            + COL_USER_GENDER + " TEXT NOT NULL,"
            + COL_USER_PASSWORD + " TEXT NOT NULL" + ")";

    private static final String CREATE_EMERGENCY_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EMERGENCY_CONTACTS + " (" +
            COL_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CONTACT_USER_ID + " INTEGER, " +
            COL_CONTACT_NAME + " TEXT NOT NULL, " +
            COL_CONTACT_NUMBER + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + COL_CONTACT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

    private static final String CREATE_COMPLAINTS_TABLE = "CREATE TABLE " + TABLE_COMPLAINTS + " (" +
            COL_COMPLAINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_COMPLAINT_USER_ID + " INTEGER, " +
            COL_COMPLAINT_TEXT + " TEXT NOT NULL, " +
            "FOREIGN KEY (" + COL_COMPLAINT_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EMERGENCY_CONTACTS_TABLE);
        db.execSQL(CREATE_COMPLAINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMERGENCY_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Create tables again
        onCreate(db);
    }

    // Method to insert user data
    public boolean insertUser(String name, String email, String phone, String gender, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_NAME, name);
        contentValues.put(COL_USER_EMAIL, email);
        contentValues.put(COL_USER_PHONE, phone);
        contentValues.put(COL_USER_GENDER, gender);
        contentValues.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Method to check if user exists by email and password
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?", new String[]{email, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    // Method to check if user email already exists
    public boolean checkUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USER_EMAIL + " = ?",
                new String[]{email},
                null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Method to get emergency contacts for a user
    public List<EmergencyContact> getEmergencyContacts(int userId) {
        List<EmergencyContact> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMERGENCY_CONTACTS,
                new String[]{COL_CONTACT_NAME, COL_CONTACT_NUMBER},
                COL_CONTACT_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NUMBER));
                contacts.add(new EmergencyContact(name, number));
            }
            cursor.close();
        }
        db.close();
        return contacts;
    }

    // Method to add an emergency contact
    public boolean addEmergencyContact(int userId, String contactName, String contactNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTACT_USER_ID, userId);
        contentValues.put(COL_CONTACT_NAME, contactName);
        contentValues.put(COL_CONTACT_NUMBER, contactNumber);
        long result = db.insert(TABLE_EMERGENCY_CONTACTS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Method to delete an emergency contact by contact ID
    public boolean deleteEmergencyContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_EMERGENCY_CONTACTS, COL_CONTACT_ID + "=?", new String[]{String.valueOf(contactId)});
        db.close();
        return rows > 0;
    }

    // Method to add complaint for a user
    public boolean addComplaint(int userId, String complaintText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_COMPLAINT_USER_ID, userId);
        contentValues.put(COL_COMPLAINT_TEXT, complaintText);
        long result = db.insert(TABLE_COMPLAINTS, null, contentValues);
        db.close();
        return result != -1;
    }

    // Method to get complaints for a user
    public List<String> getComplaints(int userId) {
        List<String> complaints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPLAINTS,
                new String[]{COL_COMPLAINT_TEXT},
                COL_COMPLAINT_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String complaint = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMPLAINT_TEXT));
                complaints.add(complaint);
            }
            cursor.close();
        }
        db.close();
        return complaints;
    }

    // Method to get all users as formatted string list
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_USER_ID, COL_USER_NAME, COL_USER_EMAIL, COL_USER_PHONE, COL_USER_GENDER},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_GENDER));
                String userRecord = "ID: " + id + ", Name: " + name + ", Email: " + email +
                        ", Phone: " + phone + ", Gender: " + gender;
                users.add(userRecord);
            }
            cursor.close();
        }
        db.close();
        return users;
    }

    // Method to get all emergency contacts as formatted string list
    public List<String> getAllEmergencyContacts() {
        List<String> contacts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMERGENCY_CONTACTS,
                new String[]{COL_CONTACT_ID, COL_CONTACT_USER_ID, COL_CONTACT_NAME, COL_CONTACT_NUMBER},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CONTACT_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CONTACT_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_NUMBER));
                String contactRecord = "ID: " + id + ", UserID: " + userId + ", Name: " + name + ", Number: " + number;
                contacts.add(contactRecord);
            }
            cursor.close();
        }
        db.close();
        return contacts;
    }

    // Method to get all complaints as formatted string list
    public List<String> getAllComplaints() {
        List<String> complaints = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COMPLAINTS,
                new String[]{COL_COMPLAINT_ID, COL_COMPLAINT_USER_ID, COL_COMPLAINT_TEXT},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPLAINT_ID));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_COMPLAINT_USER_ID));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(COL_COMPLAINT_TEXT));
                String complaintRecord = "ID: " + id + ", UserID: " + userId + ", Complaint: " + text;
                complaints.add(complaintRecord);
            }
            cursor.close();
        }
        db.close();
        return complaints;
    }
    public List<String> getAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String tableName = cursor.getString(0);
                tableNames.add(tableName);
            }
            cursor.close();
        }
        db.close();
        return tableNames;
    }

}
