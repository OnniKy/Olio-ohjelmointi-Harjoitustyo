package com.example.harjoitustyo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


public class DatabaseHelper extends SQLiteOpenHelper {

    JSONFileControl jsonFileControl;

    static String dbname = "database";
    static int version = 1;


    String createTable = "CREATE TABLE if not exists user (\n" +
            "\t\"id\"\tTEXT,\n" +
            "\t\"name\"\tTEXT,\n" +
            "\t\"username\"\tTEXT,\n" +
            "\t\"password\"\tTEXT,\n" +
            "\t\"municipality\"\tTEXT,\n" +
            "\t\"height\"\tINTEGER,\n" +
            "\t\"weight\"\tINTEGER,\n" +
            "\t\"gender\"\tTEXT,\n" +
            "\t\"birthyear\"\tINTEGER,\n" +
            "\tPRIMARY KEY(\"id\")\n" +
            ")";

    public DatabaseHelper(Context context) {
        super(context, dbname, null, version);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(createTable);

    }

    public boolean isLoginValid(String username, String password){
        String sql = "Select count(*) from user where username = '" + username + "' and password = '" + password + "'";
        SQLiteStatement statement = getReadableDatabase().compileStatement(sql);
        long l = statement.simpleQueryForLong();
        statement.close();
        return l == 1;
    }

    /* Takes user object and context,
    inserts user to database
     */
    public void insertUser(User user, Context context){
        jsonFileControl = new JSONFileControl();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", user.getSaltId());
        contentValues.put("username", user.getUsername());
        contentValues.put("name", user.getName());
        contentValues.put("password", user.getPassword());
        contentValues.put("municipality", user.getMunicipality());
        contentValues.put("height", user.getHeight());
        contentValues.put("weight", user.getWeight());
        contentValues.put("gender", user.getGender());
        contentValues.put("birthyear", user.getBirthyear());

        System.out.println(getWritableDatabase());

        getWritableDatabase().insert("user", "", contentValues);

        //Adding first weight to Weight Data File
        String weightValue = String.valueOf(user.getWeight());
        jsonFileControl.writeLog(weightValue, context, user.getName(), "Weight");

    }

    /* Takes new password and username,
    updates password to database
     */
    public void updatePassword(String newPassword, String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues data = new ContentValues();
        data.put("password", newPassword);
        myDB.update("user", data, "username = '" + username +"'",null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /* Takes user's username,
    check's if username exists in database
    returns boolean
     */
    public boolean checkUsername(String username){
        SQLiteDatabase myDB = this.getWritableDatabase();
        try (Cursor cursor = myDB.rawQuery("Select * from user where username = ?", new String[]{username})) {

            return cursor.getCount() > 0;
        }

    }

    // Takes username and returns height
    public String getHeight(String username){
        String sql = "Select height FROM user WHERE username = '"+ username + "'";
        String height;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            height = cursor.getString(0);
        }

        return height;
    }


    // Takes username and returns name
    public String getName(String username) {
        String sql = "Select name from user where username = '" + username + "'";
        String name;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            name = cursor.getString(0);
        }
        return name;
    }

    // Takes username and returns weight
    public int getWeight(String username){
        String sql = "Select weight FROM user WHERE username = '"+ username + "'";
        String weight;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            weight = cursor.getString(0);
        }

        return Integer.parseInt(weight);
    }

    // Takes username and returns salt
    public byte[] getId(String username){
        String sql = "Select id FROM user WHERE username = '"+ username + "'";
        byte[] id;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            id = cursor.getBlob(0);
        }

        return id;
    }

    // Takes username and returns password
    public String getPassword(String username){
        String sql = "Select password FROM user WHERE username = '"+ username + "'";
        String password;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            password = cursor.getString(0);
        }

        return password;
    }

    // Takes username and returns municipality
    public String getMunicipality(String username){
        String sql = "Select municipality FROM user WHERE username = '"+ username + "'";
        String municipality;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            municipality = cursor.getString(0);
        }

        return municipality;
    }

    // Takes username and returns gender
    public String getGender(String username){
        String sql = "Select municipality FROM user WHERE username = '"+ username + "'";
        String gender;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            gender = cursor.getString(0);
        }

        return gender;
    }

    // Takes username and returns birthyear
    public int getBirthyear(String username){
        String sql = "Select birthyear FROM user WHERE username = '"+ username + "'";
        String byear;
        try (Cursor cursor = getReadableDatabase().rawQuery(sql, null)) {
            cursor.moveToFirst();
            byear = cursor.getString(0);
        }

        return Integer.parseInt(byear);
    }

}
