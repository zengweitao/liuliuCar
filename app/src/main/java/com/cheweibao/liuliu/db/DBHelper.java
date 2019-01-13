package com.cheweibao.liuliu.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VER = 2;
    private static DBHelper currentUserDBHelper = null;

    public synchronized static DBHelper getInstance(Context context) {
        if (currentUserDBHelper == null) {
            String name = "cheweibao.db";
            currentUserDBHelper = new DBHelper(context, name, DB_VER);
        }
        return currentUserDBHelper;
    }


    private DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LocalCityTable.getInstance().createTable(db);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LocalCityTable.getInstance().dropTable(db);
        onCreate(db);
    }

    public synchronized void closeHelper() {
        currentUserDBHelper = null;
    }
}

