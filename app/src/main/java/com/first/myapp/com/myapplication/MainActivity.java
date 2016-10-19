package com.first.myapp.com.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 102;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 103;
    private static final int MY_RUNTIME_PERMISSIONS = 100;
    /**
     * ContactsContract.Contacts.CONTENT_URI;//联系人的Uri对象
     * content://com.android.contacts.raw_contacts
     * <p>
     * ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//获取联系人电话的Uri
     * content://com.android.contacts/data/phones
     * <p>
     * ContactsContract.CommonDataKinds.Email.CONTENT_URI;//获取联系人邮箱的Uri
     * content://com.android.contacts/data/emails
     */
    private DBService dbService;
    private ListView sms_list;
    private Button bt_query;
    private List<SmsDetailInfo> list;

    private View bt_edit;
    private View bt_hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //如果API高于或等于23,校验是否已获取权限
        if (Build.VERSION.SDK_INT >= Constant.API_M) {
            List<String> stringsList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                stringsList.add(Manifest.permission.CALL_PHONE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                stringsList.add(Manifest.permission.READ_CONTACTS);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                stringsList.add(Manifest.permission.READ_SMS);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                stringsList.add(Manifest.permission.RECEIVE_SMS);
            }
            if (stringsList.size() > 0) {
                String[] strings = new String[stringsList.size()];
                for (int i = 0; i < stringsList.size(); i++) {
                    strings[i] = stringsList.get(i);
                }
                ActivityCompat.requestPermissions(this, strings, MY_RUNTIME_PERMISSIONS);
            }

            final String myPackageName = getPackageName();

            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                //to do 先跳dialog  告知用户必须要同意
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);
                startActivity(intent);
            }
        }

        initView();

    }

    //获取权限后回调,确认权限是否都已取得,如未全部取得,就不执行逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_RUNTIME_PERMISSIONS:
                Log.e("kkk", "执行doGetSms();");
                doGetSms();
                break;
            default:
                break;
        }
    }


    private void initView() {

        sms_list = (ListView) findViewById(R.id.sms_tab);
        bt_query = (Button) findViewById(R.id.query_sms);
        bt_query.setOnClickListener(this);
        bt_edit = findViewById(R.id.go_to_edit_page);
        bt_edit.setOnClickListener(this);
        bt_hide = findViewById(R.id.bt_hide_app);
        bt_hide.setOnClickListener(this);
        dbService = DBService.shareInstance(getApplicationContext());
        doGetSms();


    }

    private void doGetSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            list = dbService.getSmsDetailInfoList();
            SmsListAdapter smsListAdapter = new SmsListAdapter(list, MainActivity.this);
            sms_list.setAdapter(smsListAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_sms:
//                TimePickerDialog timePickerDialog = new TimePickerDialog(this);
//                timePickerDialog.show();
                dbService.insertSmsInfo();
                break;
            case R.id.go_to_edit_page:
                Intent intent = new Intent(this, NewSmsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_hide_app:
                PackageManager manager = getPackageManager();
                manager.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                break;
            default:
                break;
        }
    }

//    private void insertInfo() {
//        ContentValues values = new ContentValues();
//        //发送时间
//        values.put("date", System.currentTimeMillis() + 86400000);
//        //阅读状态
//        values.put("read", 0);
//        //1为收 2为发
//        values.put("type", 1);
//        //送达号码
//        values.put("address", "58888");
//        //送达内容
//        values.put("body", "lalala test");
//
//        values.put("person", getContactsIdByPhoneNum("58888"));
//        resolver.insert(Uri.parse(uriSms), values);
//    }

//    private String getContactsIdByPhoneNum(String phoneNum) {
//        String id = null;
//        Cursor cursor = null;
//        try {
//            cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER},
//                    ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
//                            + phoneNum + "'", // WHERE clause.
//                    null,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                while (!cursor.isAfterLast()) {
//                    if (PhoneNumberUtils.compare(phoneNum, cursor.getString(1))) {
//                        id = cursor.getString(0);
//                        Log.e("kkk", "getContactsIdByPhoneNum: contactsid" + id);
//                    }
//                    cursor.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            Log.e("kkk", "getContactId error:", e);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return id;
//    }

//
//    private String getContactsNameByContactsId(String ContactsId) {
//        Cursor c = null;
//        String contactsName = null;
//        try {
//            c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    new String[]{ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.PhoneLookup.DISPLAY_NAME},
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = '"
//                            + ContactsId + "'", // WHERE clause.
//                    null,
//                    null);
//            if (c != null && c.moveToFirst()) {
//                while (!c.isAfterLast()) {
//                    contactsName = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//                    c.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            Log.e("kkk", "getContactId error:", e);
//        } finally {
//            if (c != null) {
//                c.close();
//            }
//        }
//        return contactsName;
//    }
}
