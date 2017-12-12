package com.first.myapp.com.myapplication.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

import com.first.myapp.com.myapplication.SmsDetailInfo;

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
            String contactsName = map.get(phoneNumber);
            if (contactsName == null) {
                contactsName = "null";
            }
            list.add(new SmsDetailInfo(0, phoneNumber, contactsName, smsType, data));
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


    public static List<SmsDetailInfo> getAllContact(Context context) {
        List<SmsDetailInfo> list = new ArrayList<SmsDetailInfo>();
        //利用系统的ContactProvider来查询联系人信息
        //数据来自contacts,data表
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.PHOTO_ID};
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, projection,
                null, null, null);
        //遍历从contacts数据表中的数据集
        while (cursor.moveToNext()) {
            SmsDetailInfo contact = new SmsDetailInfo();
//            contact.set(cursor.getInt(0));
            contact.setId(cursor.getInt(1));
            //利用联系人的_id,到Data表中继续查询
            ContentResolver cr2 = context.getContentResolver();
            Cursor cursor2 = cr2.query(ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.Data.DATA1},
                    ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=?", new String[]{String.valueOf(contact.getId())},
                    null);
            //从data表中取回的，特定id联系人的具体数据
            while (cursor2.moveToNext()) {
                String mimeTypeString = cursor2.getString(0);
//                if (mimeTypeString.equals("vnd.android.cursor.item/email_v2")) {
//                    contact.setEmail(cursor2.getString(1));
//                }
                if (mimeTypeString.equals("vnd.android.cursor.item/name")) {
                    contact.setContactsName(cursor2.getString(1));
                }
                if (mimeTypeString.equals("vnd.android.cursor.item/phone_v2")) {
                    contact.setPhoneNum(cursor2.getString(1));
                }
//                if (mimeTypeString.equals("vnd.android.cursor.item/postal-address_v2")) {
//                    contact.setAddress(cursor2.getString(1));
//                }
                if (mimeTypeString.equals("vnd.android.cursor.item/photo")) {
                    //contact.setPhoto_id(cursor2.getInt(1));
                    //int photo=contact.get_id();
                    //long i=photo;
                    /*InputStream input=ContactsContract.Contacts.openContactPhotoInputStream(cr2,
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    (long)contact.get_id()
                                    //Long.parseLong(String.valueOf(contact.getPhoto_id()))
                                    //photo
                                    ));
                    contact.setPhotoBitmap(BitmapFactory.decodeStream(input));*/
                }
            }
            cursor2.close();
            list.add(contact);
        }
        cursor.close();
        Log.e("kkkk", "getAllContact: list: "+list );
        return list;
    }
}
