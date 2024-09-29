package com.example.login_signup.SQLiteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

        try {
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseError", "Error inserting user", e);
            return false;
        }
    }



    public Boolean checkUserCredentials(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_PHONE + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone, password});

        boolean isValidUser = cursor.moveToFirst();
        cursor.close();
        return isValidUser;
    }


    public boolean ResetPin(String phone, String newPin) {

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_USERS +
                " SET " + COLUMN_PIN + " = ?" +
                " WHERE " + COLUMN_PHONE + " = ? ";
        db.execSQL(sql, new String[]{newPin, phone});
        Cursor cursor = db.rawQuery("SELECT changes()", null);
        cursor.moveToFirst();
        int rowsAffected = cursor.getInt(0);
        cursor.close();
        return rowsAffected > 0;
    }


    public boolean verifyOtpAndResetPassword(String phone, String otp, String newPassword) {
        //OTP
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_USERS +
                " SET " + COLUMN_PASSWORD + " = ?" +
                " WHERE " + COLUMN_PHONE + " = ?";
        db.execSQL(sql, new String[]{newPassword, phone});
        Cursor cursor = db.rawQuery("SELECT changes()", null);
        cursor.moveToFirst();
        int rowsAffected = cursor.getInt(0);
        cursor.close();
        return rowsAffected > 0;
    }


    public boolean getStoredPin(String pin,String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_PIN + " = ? AND " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{pin,phone});
        boolean isCorrect = cursor.getCount() > 0;
        cursor.close();
        return isCorrect;
    }

    public boolean isPinAlreadyUsed(String pin) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlpin = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_PIN + " = ?";
        Cursor cursor = db.rawQuery(sqlpin, new String[]{pin});
        boolean isUsed = cursor.getCount() > 0;
        cursor.close();
        return isUsed;
    }

    public boolean isPhoneAlreadyUsed(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlphone = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(sqlphone, new String[]{phone});
        boolean isUsed=cursor.getCount() > 0;
        cursor.close();
        return isUsed;
    }

    public String getFirstNameByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String firstName = null,surname=null;
        String sql = "SELECT " + COLUMN_FIRST_NAME +","+COLUMN_SURNAME + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone});

        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(COLUMN_FIRST_NAME);
            int index2 = cursor.getColumnIndex(COLUMN_SURNAME);
            if (index1 != -1) {
                firstName = cursor.getString(index1)+" ";
                surname= cursor.getString(index2);
            }
        }

        cursor.close();
        return firstName+surname;
    }

    public String getGender(String phone)
    {
        String gender=null;
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + COLUMN_GENDER + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_PHONE + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{phone});

        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(COLUMN_GENDER);

            if (index1 != -1) {
                gender = cursor.getString(index1);
                            }
        }

        cursor.close();
        return gender;
    }

    public int DeleteAccount(String phone,Context context) {

        FinancialDB financialDB=new FinancialDB(context);
        financialDB.DeleteAccount(phone);

        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_PHONE + " = ?";

        String[] selectionArgs = { phone };

        int deletedRows = db.delete(TABLE_USERS, selection, selectionArgs);

        db.close();
        return deletedRows;
    }

}
