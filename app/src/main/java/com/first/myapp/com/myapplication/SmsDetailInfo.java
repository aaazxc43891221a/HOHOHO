package com.first.myapp.com.myapplication;

/**
 * Created by chauvard on 10/8/16.
 */

public class SmsDetailInfo {
    private String phoneNum;
    private String contactsName;
    private String type;
    private String data;


    public SmsDetailInfo(String phoneNum, String contactsName, String type, String data) {
        this.phoneNum = phoneNum;
        this.contactsName = contactsName;
        this.type = type;
        this.data = data;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
