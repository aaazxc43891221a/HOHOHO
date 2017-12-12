package com.first.myapp.com.myapplication.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.first.myapp.com.myapplication.SmsDetailInfo;
import com.first.myapp.com.myapplication.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by chauvard on 10/13/16.
 */

public class SMSDBService {

    private String uriSms = "content://sms";//短信的Uri
    private Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
    private Context mContext;
    private static SMSDBService SMSDBService;
    private ContentResolver resolver;//内容解析者  获取ContentProvider提供的数据

    private SMSDBService(Context context) {
        mContext = context;
        resolver = mContext.getContentResolver();
    }

    public static synchronized SMSDBService shareInstance(Context context) {
        if (SMSDBService == null) {
            SMSDBService = new SMSDBService(context);
        }
        return SMSDBService;
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
        insertSmsInfo("lalala test");
    }

    //to delete
    public void insertSmsInfo(String content) {
        ContentValues values = new ContentValues();
        //发送时间
        values.put("date", System.currentTimeMillis());
        //阅读状态
        values.put("read", 0);
        //1为收 2为发
        values.put("type", 1);
        //送达号码
        values.put("address", "58888");
        //送达内容
        values.put("body", content);

        values.put("person", getContactsIdByPhoneNum("58888"));
        resolver.insert(Uri.parse(uriSms), values);
    }

    public void modifySmsInfo() {
        ContentValues values = new ContentValues();
        //发送时间
        values.put("date", System.currentTimeMillis() + 5000);
        //阅读状态
        values.put("read", 0);
        //1为收 2为发
        values.put("type", 1);
        //送达号码
        values.put("address", "58888");
        //送达内容
        values.put("body", "修改后的短信");

        values.put("person", getContactsIdByPhoneNum("58888"));
        resolver.update(Uri.parse(uriSms), values, "address=? and body=?", new String[]{"58888", "lalala test"});
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

        Cursor cursorContent = resolver.query(contactsUri, null, null, null, null);
        HashMap<String, String> map = new HashMap<>();
        while (cursorContent != null && cursorContent.getCount() > 0 && cursorContent.moveToNext()) {
            // 联系人的id
            int contactid = cursorContent.getInt(cursorContent
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获取联系人的name
            String contactName = cursorContent.getString(cursorContent
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Log.e("kkk", "contactid: " + contactid + "    contactName: " + contactName);

//            //获取联系人号码
//            String phoneNum = cursorContent.getString(
//                    cursorContent.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Log.e("kkk", "phoneNum: "+phoneNum );
//            map.put(phoneNum, contactName);

            Cursor phoneNumbers = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                    + contactid, null, null);
            // 取得电话号码(可能存在多个号码)
            while (phoneNumbers.moveToNext()) {
                String originalPhoneNumber = phoneNumbers.getString(phoneNumbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.e("rrrr", "originalPhoneNumber: " + originalPhoneNumber);
                String formatPhoneNumber = formatPhoneNumber(originalPhoneNumber);
                map.put(formatPhoneNumber, contactName);
//                sbLog.append("Phone=" + originalPhoneNumber + ";");
            }
            phoneNumbers.close();
        }
        cursorContent.close();
        Cursor cursorSms = resolver.query(Uri.parse(uriSms), null, null, null, null);
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
//            Log.e("kkk", "contentId: " + contentId);
            String contactsName = map.get(formatPhoneNumber(phoneNumber));
            if (contactsName == null) {
                contactsName = "null";
            }
            list.add(new SmsDetailInfo(0, phoneNumber, contactsName, smsType, data));
        }
        cursorSms.close();
        Log.e("kkkk", "getSmsDetailInfoList: list: " + list);
        return list;

    }

    private String formatPhoneNumber(String originalPhoneNumber) {
        if (StringUtil.isEmpty(originalPhoneNumber)) return "";
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < originalPhoneNumber.length(); i++) {
            char c = originalPhoneNumber.charAt(i);
            int parseInt = 0;
            try {
                parseInt = Integer.parseInt("" + c);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }
            if (parseInt >= 0 && parseInt <= 9) {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
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

    public static String getContactNameByPhoneNumber(Context context, String address) {
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + address + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor == null) {
            Log.e("kkk", "getPeople null");
            return null;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            return name;
        }
        return null;
    }


    public List<String[]> getNameAndPhoneList() {
        List<String[]> list = new ArrayList<>();
        //使用ContentResolver查找联系人数据
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //遍历查询结果，找到所需号码
        while (cursor.moveToNext()) {
            //获取联系人ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的名字
            String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //使用ContentResolver查找联系人的电话号码
            Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                list.add(new String[]{contactName, phoneNumber});
            }
        }
        return list;
    }


}
