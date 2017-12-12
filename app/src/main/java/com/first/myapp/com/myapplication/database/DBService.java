package com.first.myapp.com.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.first.myapp.com.myapplication.MyApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nan.liu on 2/2/15.
 */
public class DBService {
    protected MySQLiteHelper mySQLiteHelper;
    protected Context mContext;

    public DBService(Context context) {
        if (context == null)
            context = MyApplication.getInstance().getApplicationContext();
        mContext = context;

        mySQLiteHelper = new MySQLiteHelper(context);
    }

    public void insertOrUpdate(String tableName, String[] volume,
                               List<HashMap<String, Object>> value) {
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getReadableDatabase();
        StringBuffer volumeBuffer = new StringBuffer();
        volumeBuffer.append("replace into " + tableName + " (");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append(volume[i] + ",");
        }

        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(") values(");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append("?,");
        }

        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(");");
        sqLiteDatabase.beginTransaction();
        for (int i = 0; i < value.size(); i++) {
            Object[] valueString = new Object[volume.length];
            for (int j = 0; j < volume.length; j++) {
                valueString[j] = value.get(i).get(volume[j]) == null ? ""
                        : value.get(i).get(volume[j]);
            }
            sqLiteDatabase.execSQL(volumeBuffer.toString(), valueString);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    public void insertOrUpdate(String tableName, String[] volume, Object[] value) {
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
        StringBuffer volumeBuffer = new StringBuffer();
        volumeBuffer.append("replace into " + tableName + " (");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append(volume[i]);
            Log.e("kkkk", "volume[i]: "+volume[i] );
            if (i != volume.length-1){
                volumeBuffer.append( ", ");
            }
        }

//        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(") values(");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append("?,");
        }

        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(");");
        sqLiteDatabase.beginTransaction();
        Log.e("kkkk", "volumeBuffer.toString(): " +volumeBuffer.toString());
        sqLiteDatabase.execSQL(volumeBuffer.toString(), value);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    public void insert(String tableName, String[] volume, String[] value) {
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getReadableDatabase();
        StringBuffer volumeBuffer = new StringBuffer();
        volumeBuffer.append("insert into " + tableName + " (");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append(volume[i]);
            if (i!=volume.length-1){
                volumeBuffer.append(",");
            }
        }
//        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(") values(");
        for (int i = 0; i < volume.length; i++) {
            volumeBuffer.append("?,");
        }
        volumeBuffer.deleteCharAt(volumeBuffer.length() - 1);
        volumeBuffer.append(");");
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL(volumeBuffer.toString(), value);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    public void delete(String tableName) {
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getReadableDatabase();
        StringBuffer deleteSQL = new StringBuffer();
        deleteSQL.append("delete from " + tableName + ";");
        sqLiteDatabase.execSQL(deleteSQL.toString(), new String[]{});
        sqLiteDatabase.close();
    }

    public ArrayList<HashMap<String, Object>> findAll(String tableName, String[] volume) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getReadableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("select * from " + tableName,
                    null);
            while (cursor.moveToNext()) {
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                for (int i = 0; i < volume.length; i++) {
                    if (cursor.getType(i) == Cursor.FIELD_TYPE_INTEGER) {
                        hashMap.put(volume[i], cursor.getInt(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_STRING) {
                        hashMap.put(volume[i], cursor.getString(i));
                    } else if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                        hashMap.put(volume[i], cursor.getBlob(i));
                    }

                }
                list.add(hashMap);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }
        return list;
    }
}
