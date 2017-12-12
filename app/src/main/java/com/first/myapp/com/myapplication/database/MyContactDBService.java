package com.first.myapp.com.myapplication.database;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.first.myapp.com.myapplication.database.dbmodel.MyContactBModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauvard on 1/20/17.
 */
public class MyContactDBService extends DBService {


    public static final String TABLE_NAME = "contact_table";

    public MyContactDBService(Context context) {
        super(context);
    }

    public List<MyContactBModel> findAllContact() {
        List<MyContactBModel> list = new ArrayList<>();
        ArrayList<HashMap<String, Object>> all = findAll(TABLE_NAME, MyContactBModel.DBKey);
        for (HashMap hashMap : all) {
            list.add(new MyContactBModel(hashMap));
        }
        return list;
    }

    public void refreshTable() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            List<String[]> nameAndPhoneList = SMSDBService.shareInstance(mContext).getNameAndPhoneList();
            delete(TABLE_NAME);
            for (String[] strings:nameAndPhoneList) {
                insertOrUpdate(TABLE_NAME,new String[]{MyContactBModel.USER_NAME,MyContactBModel.PHONE},strings);
            }
        }
    }
}
