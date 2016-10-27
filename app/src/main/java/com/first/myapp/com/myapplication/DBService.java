package com.first.myapp.com.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by chauvard on 10/13/16.
 */

public class DBService {

    private String uriSms = "content://sms";//短信的Uri
    private Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
    private Context mContext;
    private static DBService dbService;
    private ContentResolver resolver;//内容解析者  获取ContentProvider提供的数据

    private DBService(Context context) {
        mContext = context;
        resolver = mContext.getContentResolver();
    }

    public static synchronized DBService shareInstance(Context context) {
        if (dbService == null) {
            dbService = new DBService(context);
        }
        return dbService;
    }

    private void insertSmsInfo(ContentValues values) {
//        ContentValues values = new ContentValues();
        //发送时间
        values.put("date", System.currentTimeMillis() - 86400000);
        //阅读状态
        values.put("read", 0);
        //1为收 2为发
        values.put("type", 1);
        //送达号码
        values.put("address", "58888");
        //送达内容
        values.put("body", "lalala test");

        values.put("person", getContactsIdByPhoneNum("58888"));
        resolver.insert(Uri.parse(uriSms), values);
    }

//to delete
    public void insertSmsInfo() {
        ContentValues values = new ContentValues();
        //发送时间
        values.put("date", System.currentTimeMillis() );
        //阅读状态
        values.put("read", 0);
        //1为收 2为发
        values.put("type", 1);
        //送达号码
        values.put("address", "58888");
        //送达内容
        values.put("body", "lalala test");

        values.put("person", getContactsIdByPhoneNum("58888"));
        resolver.insert(Uri.parse(uriSms), values);
    }

    public void updateSmsInfo() {
        ContentValues values = new ContentValues();
        //发送时间
        values.put("date", System.currentTimeMillis() + 120000);
        //阅读状态
        values.put("read", 0);
        //1为收 2为发
        values.put("type", 1);
        //送达号码
        values.put("address", "58888");
        //送达内容
        values.put("body", "修改后的短信");

        values.put("person", getContactsIdByPhoneNum("58888"));
        resolver.update(Uri.parse(uriSms),values,"address=? and body=?",new String[]{"58888","lalala test"});
//                (Uri.parse(uriSms), values);
    }


    private String getContactsIdByPhoneNum(String phoneNum) {
        String id = null;
        Cursor cursor = null;
        try {
            cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                            + phoneNum + "'", // WHERE clause.
                    null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    if (PhoneNumberUtils.compare(phoneNum, cursor.getString(1))) {
                        id = cursor.getString(0);
                        Log.e("kkk", "getContactsIdByPhoneNum: contactsid" + id);
                    }
                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("kkk", "getContactId error:", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return id;
    }

    public List<SmsDetailInfo> getSmsDetailInfoList() {
        Cursor cursorSms = resolver.query(Uri.parse(uriSms), null, null, null, null);
        Cursor cursorContent = resolver.query(contactsUri, null, null, null, null);
        HashMap<Integer, String> map = new HashMap<>();
        while (cursorContent != null && cursorContent.getCount() > 0 && cursorContent.moveToNext()) {
            // 联系人的id
            int contactid = cursorContent.getInt(cursorContent
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的name
            String contactName = cursorContent.getString(cursorContent
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            map.put(contactid, contactName);
        }
        cursorContent.close();
        List<SmsDetailInfo> list = new ArrayList<>();
        while (cursorSms != null && cursorSms.getCount() > 0 && cursorSms.moveToNext()) {
            //短信的号码
            String phoneNumber = cursorSms.getString(cursorSms.getColumnIndex("address"));
            //发送短信的时间
            long longDate = cursorSms.getLong(cursorSms.getColumnIndex("date"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String data = format.format(new Date(longDate));

            //短信类型  1接受 2发出
            int type = cursorSms.getInt(cursorSms.getColumnIndex("type"));
            String smsType = null;
            if (1 == type) {
                //接受
                smsType = "接受";
            } else {
                //发送
                smsType = "发送";
            }

            //联系人列表里的序号
            int contentId = cursorSms.getInt(cursorSms.getColumnIndex("person"));
            String contactsName = map.get(contentId);
            if (contactsName == null) {
                contactsName = "null";
            }
            list.add(new SmsDetailInfo(phoneNumber, contactsName, smsType, data));
        }
        cursorSms.close();
        return list;

    }

    private String getContactsNameByContactsId(String ContactsId) {
        Cursor c = null;
        String contactsName = null;
        try {
            c = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.PhoneLookup.DISPLAY_NAME},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = '"
                            + ContactsId + "'", // WHERE clause.
                    null,
                    null);
            if (c != null && c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    contactsName = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    c.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("kkk", "getContactId error:", e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return contactsName;
    }


}
