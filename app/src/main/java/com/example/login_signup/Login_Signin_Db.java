package com.example.login_signup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Login_Signin_Db extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "user_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PIN = "pin";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_GENDER = "gender";

    public Login_Signin_Db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_PHONE + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PIN + " TEXT,"
                + COLUMN_FIRST_NAME + " TEXT,"
                + COLUMN_SURNAME + " TEXT,"
                + COLUMN_DOB + " TEXT,"
                + COLUMN_GENDER + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String firstName, String surname, String phone, String password, String dob, String gender, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_PIN, pin);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_SURNAME, surname);
        values.put(COLUMN_DOB, dob);
        values.put(COLUMN_GENDER, gender);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUserCredentials(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PHONE};
        String selection = COLUMN_PHONE + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {phone, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean verifyOtpAndResetPin(String phone, String otp, String newPin) {
        // Implement OTP verification logic here
        // For simplicity, let's assume OTP is always correct
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PIN, newPin);

        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};

        int rowsAffected = db.update(TABLE_USERS, values, selection, selectionArgs);
        return rowsAffected > 0;
    }

    public boolean verifyOtpAndResetPassword(String phone, String otp, String newPassword) {
        // Implement OTP verification logic here
        // For simplicity, let's assume OTP is always correct
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        String selection = COLUMN_PHONE + " = ?";
        String[] selectionArgs = {phone};

        int rowsAffected = db.update(TABLE_USERS, values, selection, selectionArgs);
        return rowsAffected > 0;
    }

    public Boolean getStoredPin(String enteredPin) {
        SQLiteDatabase db = this.getReadableDatabase();
        String storedPin = null;
        Cursor cursor = db.rawQuery("SELECT pin FROM users WHERE pin = ?", new String[]{enteredPin});
        if (cursor.moveToFirst()) {
            storedPin = cursor.getString(0);
        }
        cursor.close();
        return enteredPin.equals(storedPin);
    }
}
