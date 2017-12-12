package com.first.myapp.com.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.first.myapp.com.myapplication.database.dbmodel.MyContactBModel;

/**
 * Created by nan.liu on 2/2/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "my_first_black_technology.db";
    private Context mContext;

    public MySQLiteHelper(Context context, CursorFactory factory, int version) {
        super(context, DB_NAME, factory, version);
        mContext = context;
    }

    public MySQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //region version 1
        // version 1.1
        createContactDB(db);
        //endregion

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            default:
                break;
        }
    }


    private void createContactDB(SQLiteDatabase db) {
        String createContactDB = "CREATE TABLE IF NOT EXISTS "
                + MyContactDBService.TABLE_NAME + "("
                + MyContactBModel.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MyContactBModel.USER_NAME + " TEXT,"
                + MyContactBModel.PHONE + " TEXT"
                + ")";

        db.execSQL(createContactDB);
    }

    private void dropTable(SQLiteDatabase db, String tableName) {
        String strDelTable = "DROP TABLE  " + tableName;

        db.execSQL(strDelTable);
    }
}
