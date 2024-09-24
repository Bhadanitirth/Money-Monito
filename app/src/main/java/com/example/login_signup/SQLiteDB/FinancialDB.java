package com.example.login_signup.SQLiteDB;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;

public class FinancialDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FinancialData.db";
    private static final int DATABASE_VERSION = 3; // Incremented version for schema change


    public static final String TABLE_FINANCIAL = "FinancialRecords";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_ATTACHMENT = "attachment";
    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_INCOME_CATEGORY = "income_category";
    public static final String COLUMN_INCOME_PAYMENT_METHOD = "income_payment_method";

    public static final String COLUMN_EXPENSE_CATEGORY = "expense_category";
    public static final String COLUMN_EXPENSE_PAYMENT_METHOD = "expense_payment_method";

    public static final String COLUMN_FROM_ACCOUNT = "from_account";
    public static final String COLUMN_TO_ACCOUNT = "to_account";
    public static final String COLUMN_TYPE = "type";

    public FinancialDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FINANCIAL_TABLE = "CREATE TABLE " + TABLE_FINANCIAL + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_NOTE + " TEXT DEFAULT NULL,"
                + COLUMN_ATTACHMENT + " TEXT DEFAULT NULL,"
                + COLUMN_PHONE + " TEXT DEFAULT NULL,"
                + COLUMN_INCOME_CATEGORY + " TEXT DEFAULT NULL,"
                + COLUMN_INCOME_PAYMENT_METHOD + " TEXT DEFAULT NULL,"
                + COLUMN_EXPENSE_CATEGORY + " TEXT DEFAULT NULL,"
                + COLUMN_EXPENSE_PAYMENT_METHOD + " TEXT DEFAULT NULL,"
                + COLUMN_FROM_ACCOUNT + " TEXT DEFAULT NULL,"
                + COLUMN_TO_ACCOUNT + " TEXT DEFAULT NULL,"
                + COLUMN_TYPE + " TEXT DEFAULT NULL"
                + ")";
        db.execSQL(CREATE_FINANCIAL_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FINANCIAL);
            onCreate(db);

    }

    public long insertIncome(String date, String time, double amount, String category, String paymentMethod, String note, String attachment, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_INCOME_CATEGORY, category);
        values.put(COLUMN_INCOME_PAYMENT_METHOD, paymentMethod);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_ATTACHMENT, attachment);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_TYPE, "income");

        return db.insert(TABLE_FINANCIAL, null, values);
    }

    public long insertExpense(String date, String time, double amount, String category, String paymentMethod, String note, String attachment, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_EXPENSE_CATEGORY, category);
        values.put(COLUMN_EXPENSE_PAYMENT_METHOD, paymentMethod);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_ATTACHMENT, attachment);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_TYPE, "expense");

        return db.insert(TABLE_FINANCIAL, null, values);
    }

    public long insertTransfer(String date, String time, double amount, String fromAccount, String toAccount, String note, String attachment, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_FROM_ACCOUNT, fromAccount);
        values.put(COLUMN_TO_ACCOUNT, toAccount);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_ATTACHMENT, attachment);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_TYPE, "transfer");

        return db.insert(TABLE_FINANCIAL, null, values);
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUsers(String phone, int month, int year, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = null;
        if (type.equals("income")) {
            query = "SELECT amount, income_category, income_payment_method, note ,date FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ? AND "
                    + COLUMN_INCOME_CATEGORY + " IS NOT NULL";
        } else if (type.equals("expense")) {
            query = "SELECT amount, expense_category, expense_payment_method, note ,date FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ? AND "
                    + COLUMN_EXPENSE_CATEGORY + " IS NOT NULL";
        } else if (type.equals("transfer")) {
            query = "SELECT amount, from_account, to_account, note ,date FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ? AND "
                    + COLUMN_FROM_ACCOUNT + " IS NOT NULL AND " + COLUMN_TO_ACCOUNT + " IS NOT NULL";
        }

        Cursor cursor = db.rawQuery(query, new String[]{phone, monthStr, yearStr});

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();

            if (type.equals("income")) {
                user.put("amount", cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                user.put("note", cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                user.put("income_category", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_CATEGORY)));
                user.put("income_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_PAYMENT_METHOD)));
            } else if (type.equals("expense")) {
                user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                user.put("amount", cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                user.put("note", cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                user.put("expense_category", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY)));
                user.put("expense_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_PAYMENT_METHOD)));
            } else if (type.equals("transfer")) {
                user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                user.put("amount", cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                user.put("note", cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                user.put("from_account", cursor.getString(cursor.getColumnIndex(COLUMN_FROM_ACCOUNT)));
                user.put("to_account", cursor.getString(cursor.getColumnIndex(COLUMN_TO_ACCOUNT)));
            }

            recordList.add(user);
        }

        cursor.close();
        db.close();
        return recordList;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetAllUsers(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();

        String query = null;

            query = "SELECT amount, income_category, income_payment_method, expense_category, expense_payment_method, note ,date ,from_account, to_account FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_PHONE + " = ? " ;

            Cursor cursor = db.rawQuery(query, new String[]{phone});

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();

                user.put("amount", cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                user.put("note", cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
                user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                user.put("income_category", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_CATEGORY)));
                user.put("income_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_PAYMENT_METHOD)));

                user.put("expense_category", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY)));
                user.put("expense_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_PAYMENT_METHOD)));

                user.put("from_account", cursor.getString(cursor.getColumnIndex(COLUMN_FROM_ACCOUNT)));
                user.put("to_account", cursor.getString(cursor.getColumnIndex(COLUMN_TO_ACCOUNT)));


            recordList.add(user);
        }

        cursor.close();
        db.close();
        return recordList;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUsers(String phone, int month, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = null;
        query = "SELECT id, amount, income_category, income_payment_method, expense_category, expense_payment_method, note ,date ,from_account, to_account, type, time FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?" ;

        Cursor cursor = db.rawQuery(query, new String[]{phone, monthStr, yearStr});

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();

            user.put("amount", cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
            user.put("note", cursor.getString(cursor.getColumnIndex(COLUMN_NOTE)));
            user.put("time", cursor.getString(cursor.getColumnIndex(COLUMN_TIME)));
            user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            user.put("income_category", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_CATEGORY)));
            user.put("income_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_PAYMENT_METHOD)));

            user.put("expense_category", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_CATEGORY)));
            user.put("expense_payment_method", cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_PAYMENT_METHOD)));

            user.put("from_account", cursor.getString(cursor.getColumnIndex(COLUMN_FROM_ACCOUNT)));
            user.put("to_account", cursor.getString(cursor.getColumnIndex(COLUMN_TO_ACCOUNT)));

            user.put("type", cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));

            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            user.put("id", String.valueOf(id));

            recordList.add(user);
        }

        cursor.close();
        db.close();
        return recordList;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> recordList = new ArrayList<>();

        String query = "SELECT amount, income_category, income_payment_method, expense_category, expense_payment_method, note, date, from_account, to_account, type, time FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{id});

        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();

            // Retrieve data using column indices
            user.put("amount", cursor.getString(0));  // amount
            user.put("income_category", cursor.getString(1));  // income_category
            user.put("income_payment_method", cursor.getString(2));  // income_payment_method
            user.put("expense_category", cursor.getString(3));  // expense_category
            user.put("expense_payment_method", cursor.getString(4));  // expense_payment_method
            user.put("note", cursor.getString(5));  // note
            user.put("date", cursor.getString(6));  // date
            user.put("from_account", cursor.getString(7));  // from_account
            user.put("to_account", cursor.getString(8));  // to_account
            user.put("type", cursor.getString(9));  // type
            user.put("time", cursor.getString(10));  // time

            recordList.add(user);
        }

        cursor.close();
        db.close();
        return recordList;
    }


    public int updateList(int id, String date, String time, double amount, String category, String paymentMethod, String note, String attachment, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_ATTACHMENT, attachment);

        // Check if it's income or expense and update the corresponding columns
        if (type.equals("income")) {
            values.put(COLUMN_INCOME_CATEGORY, category);
            values.put(COLUMN_INCOME_PAYMENT_METHOD, paymentMethod);
        } else if (type.equals("expense")) {
            values.put(COLUMN_EXPENSE_CATEGORY, category);
            values.put(COLUMN_EXPENSE_PAYMENT_METHOD, paymentMethod);
        }else if (type.equals("expense")) {
            values.put(COLUMN_FROM_ACCOUNT, category);
            values.put(COLUMN_TO_ACCOUNT, paymentMethod);
        }

        // Update the record where ID matches
        return db.update(TABLE_FINANCIAL, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }



    public int delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = COLUMN_ID + " = ?";

        String[] selectionArgs = { String.valueOf(id) };

        int deletedRows = db.delete(TABLE_FINANCIAL, selection, selectionArgs);

        db.close();
        return deletedRows;
    }



    public double getTotalForMonth(int month, int year, String phone, boolean isIncome) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalAmount = 0;

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ? AND "
                + COLUMN_PHONE + " = ? AND " + (isIncome ? COLUMN_INCOME_CATEGORY + " IS NOT NULL" : COLUMN_EXPENSE_CATEGORY + " IS NOT NULL");

        Cursor cursor = db.rawQuery(query, new String[]{monthStr, yearStr, phone});

        if (cursor.moveToFirst()) {
            totalAmount = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalAmount;
    }

    public HashMap<String, Double> getTotalIncomeByCategory(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Double> incomeByCategory = new HashMap<>();

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT " + COLUMN_INCOME_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?"
                + " AND " + COLUMN_INCOME_CATEGORY + " IS NOT NULL GROUP BY " + COLUMN_INCOME_CATEGORY;

        Cursor cursor = db.rawQuery(query, new String[]{phone, monthStr, yearStr});

        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            double total = cursor.getDouble(1);
            incomeByCategory.put(category, total);
        }

        cursor.close();
        db.close();
        return incomeByCategory;
    }

    public HashMap<String, Double> getTotalExpenseByCategory(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<String, Double> expenseByCategory = new HashMap<>();

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT " + COLUMN_EXPENSE_CATEGORY + ", SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?"
                + " AND " + COLUMN_EXPENSE_CATEGORY + " IS NOT NULL GROUP BY " + COLUMN_EXPENSE_CATEGORY;

        Cursor cursor = db.rawQuery(query, new String[]{phone, monthStr, yearStr});

        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            double total = cursor.getDouble(1);
            expenseByCategory.put(category, total);
        }

        cursor.close();
        db.close();
        return expenseByCategory;
    }

    public double getSpendingForMode(String paymentMode, String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalSpending = 0;

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_EXPENSE_PAYMENT_METHOD + " = ? AND "
                + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?";

        Cursor cursor = db.rawQuery(query, new String[]{paymentMode, phone, monthStr, yearStr});

        if (cursor.moveToFirst()) {
            totalSpending = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalSpending;
    }

    public double getIncomeForMode(String paymentMode, String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = 0;

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_INCOME_PAYMENT_METHOD + " = ? AND "
                + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?";

        Cursor cursor = db.rawQuery(query, new String[]{paymentMode, phone, monthStr, yearStr});

        if (cursor.moveToFirst()) {
            totalIncome = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalIncome;
    }

    public double getTransfersForMode(String transferMode, String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalTransfers = 0;

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "";
        String[] args;

        if (transferMode.equals("Cash -> Bank Account")) {
            query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_FROM_ACCOUNT + " = 'Cash' AND " + COLUMN_TO_ACCOUNT + " = 'Bank Account' AND "
                    + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?";
            args = new String[]{phone, monthStr, yearStr};
        } else if (transferMode.equals("Bank Account -> Cash")) {
            query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                    + " WHERE " + COLUMN_FROM_ACCOUNT + " = 'Bank Account' AND " + COLUMN_TO_ACCOUNT + " = 'Cash' AND "
                    + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?";
            args = new String[]{phone, monthStr, yearStr};
        } else {
            return totalTransfers;
        }

        Cursor cursor = db.rawQuery(query, args);

        if (cursor.moveToFirst()) {
            totalTransfers = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return totalTransfers;
    }


    public int getNumberOfTransactions(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfTransactions = 0;

        String monthStr = String.format("%02d", month);
        String yearStr = String.valueOf(year);

        String query = "SELECT COUNT(*) FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_PHONE + " = ? AND substr(" + COLUMN_DATE + ", 4, 2) = ? AND substr(" + COLUMN_DATE + ", 7, 4) = ?";

        Cursor cursor = db.rawQuery(query, new String[]{phone, monthStr, yearStr});

        if (cursor.moveToFirst()) {
            numberOfTransactions = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return numberOfTransactions;
    }

    public double getAverageSpending(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double averageSpending = 0;
        double totalSpending = getSpendingForMode("Cash", phone, month, year) + getSpendingForMode("Bank Account", phone, month, year);
        int numberOfTransactions = getNumberOfTransactions(phone, month, year);

        if (numberOfTransactions > 0) {
            averageSpending = totalSpending / numberOfTransactions;
        }

        db.close();
        return averageSpending;
    }

    public double getSpendingPerDay(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalSpending = getSpendingForMode("Cash", phone, month, year) + getSpendingForMode("Bank Account", phone, month, year);
        int daysInMonth = getDaysInMonth(month, year);

        db.close();
        return totalSpending / daysInMonth;
    }

    public double getIncomePerDay(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = getIncomeForMode("Cash", phone, month, year) + getIncomeForMode("Bank Account", phone, month, year);
        int daysInMonth = getDaysInMonth(month, year);

        db.close();
        return totalIncome / daysInMonth;
    }

    public double getAverageIncomePerTransaction(String phone, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalIncome = getIncomeForMode("Cash", phone, month, year) + getIncomeForMode("Bank Account", phone, month, year);
        int numberOfTransactions = getNumberOfTransactions(phone, month, year);

        db.close();
        if (numberOfTransactions > 0) {
            return totalIncome / numberOfTransactions;
        }
        return 0;
    }

    private int getDaysInMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public double getTotal(String phone, boolean isBankAccount) {
        SQLiteDatabase db = this.getReadableDatabase();

        double totalIncome = 0, totalExpense = 0, totalTransferTo = 0, totalTransferFrom = 0, totalAmount;
        String payType = isBankAccount ? "Bank Account" : "Cash";

        totalIncome = getTotalAmount(db, phone, COLUMN_INCOME_PAYMENT_METHOD, payType);

        totalExpense = getTotalAmount(db, phone, COLUMN_EXPENSE_PAYMENT_METHOD, payType);

        totalTransferTo = getTotalAmount(db, phone, COLUMN_FROM_ACCOUNT, payType);

        totalTransferFrom = getTotalAmount(db, phone, COLUMN_TO_ACCOUNT, payType);

        db.close();

        totalAmount = (totalIncome + totalTransferFrom) - (totalExpense + totalTransferTo);

        return totalAmount;
    }

    private double getTotalAmount(SQLiteDatabase db, String phone, String column, String payType) {
        double sum = 0;
        String query = "SELECT SUM(" + COLUMN_AMOUNT + ") FROM " + TABLE_FINANCIAL
                + " WHERE " + COLUMN_PHONE + " = ? AND " + column + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{phone, payType});

        if (cursor != null && cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }

        if (cursor != null) {
            cursor.close();
        }

        return sum;
    }

}

