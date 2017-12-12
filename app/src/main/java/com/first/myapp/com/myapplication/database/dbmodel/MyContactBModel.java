package com.first.myapp.com.myapplication.database.dbmodel;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by chauvard on 1/20/17.
 */
public class MyContactBModel  implements Serializable, IDBModel {

    public static final String USER_ID = "_id";
    public static final String USER_NAME = "user_name";
    public static final String PHONE = "phone";

    public static String[] DBKey = {USER_ID, USER_NAME,PHONE};


    private int userId;
    private String userName;
    private String phone;

    public MyContactBModel(int userId, String userName, String phone) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
    }
    public MyContactBModel(HashMap<String, Object> map) {
        setUserId((int) map.get(USER_ID));
        setUserName((String) map.get(USER_NAME));
        setPhone((String) map.get(PHONE));
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(USER_ID, userId);
        map.put(USER_NAME, userName);
        map.put(PHONE, phone);
        return map;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
