package com.example.artify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Test_DB";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_REGISTER = "register";
    public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_PHONE = "phone";

    public static final String TABLE_ARTS = "arts";
    public static final String COL_ART_ID = "artId"; // Added column name
    public static final String COL_ART_NAME = "artName";
    public static final String COL_ART_PRICE = "artPrice";
    public static final String COL_ART_QUANTITY = "artQuantity";
    public static final String COL_ART_IMAGE_URI = "artImageUri";
    public static final String COL_ART_IMAGE_URI2 = "artImageUri2";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_REGISTER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_PHONE + " TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ARTS + " (" +
                COL_ART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ART_NAME + " TEXT, " +
                COL_ART_PRICE + " REAL, " +
                COL_ART_QUANTITY + " INTEGER, " +
                COL_ART_IMAGE_URI + " BLOB, " +
                COL_ART_IMAGE_URI2 + " BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTS);
        onCreate(sqLiteDatabase);
    }

    public boolean insertUser(String username, String email, String password, String phone) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_PHONE, phone);

        long result = sqLiteDatabase.insert(TABLE_REGISTER, null, contentValues);

        sqLiteDatabase.close(); // Close the database instance

        return result != -1;
    }

    public boolean checkUserByUsername(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_REGISTER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        sqLiteDatabase.close(); // Close the database instance
        return exists;
    }

    public void insertArt(String name, double price, int quantity, byte[] imageByteArray) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ART_NAME, name);
        values.put(COL_ART_PRICE, price);
        values.put(COL_ART_QUANTITY, quantity);
        values.put(COL_ART_IMAGE_URI, imageByteArray);

        sqLiteDatabase.insert(TABLE_ARTS, null, values);
        sqLiteDatabase.close();
    }

    public Cursor getAllArts() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT id AS _id, * FROM " + TABLE_ARTS, null);
    }

    public Cursor getArtByName(String artName) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_ARTS + " WHERE " + COL_ART_NAME + " = ? ", new String[] {artName});
    }

    public void updateArt(int artId, String artName, double price, int quantity, byte[] artImageByteArray) {
         SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
         ContentValues values = new ContentValues();

         values.put(COL_ART_NAME, artName);
         values.put(COL_ART_PRICE, price);
         values.put(COL_ART_QUANTITY, quantity);
         values.put(COL_ART_IMAGE_URI, artImageByteArray);

         sqLiteDatabase.update(TABLE_ARTS, values, COL_ID + " = ?", new String[]{String.valueOf(artId)});
         sqLiteDatabase.close();

    }

    public void deleteArt(String artName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_ARTS, COL_ART_NAME + " = ?", new String[]{artName});
        sqLiteDatabase.close();
    }

}
