package com.first.myapp.com.myapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.first.myapp.com.myapplication.Constant;
import com.first.myapp.com.myapplication.MyViews.LoadingProgressDialog;
import com.first.myapp.com.myapplication.R;
import com.first.myapp.com.myapplication.SmsDetailInfo;
import com.first.myapp.com.myapplication.SmsListAdapter;
import com.first.myapp.com.myapplication.database.MyContactDBService;
import com.first.myapp.com.myapplication.database.SMSDBService;
import com.first.myapp.com.myapplication.util.AsyncTaskExecutorUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 101;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 102;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 103;
    private static final int MY_RUNTIME_PERMISSIONS = 100;

    private static final int MSG_SHOW_LOADING_DIALOG = 0;
    private static final int MSG_HIDE_LOADING_DIALOG = 1;
    private static final int MSG_REFRESH_LIST_VIEW = 2;
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
    private SMSDBService SMSDBService;
    private ListView sms_list;
    private Button bt_query;
    private List<SmsDetailInfo> list;

    private View bt_edit;
    private View bt_hide;
    private View bt_receive_sms;
    private MyContactDBService contactDBService;
    private SmsListAdapter mSmsListAdapter;
    private View mInsertSmsButton;
    private View mModifySmsButton;
    private LoadingProgressDialog mDialog;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mActivity = this;
        //如果API高于或等于23,校验是否已获取权限
        getPermissionAndSetDefaultApp();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("kkkk", "onResume: ");
        getPermissionAndSetDefaultApp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void getPermissionAndSetDefaultApp() {
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
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                stringsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (stringsList.size() > 0) {
                String[] strings = new String[stringsList.size()];
                for (int i = 0; i < stringsList.size(); i++) {
                    strings[i] = stringsList.get(i);
                }

                ActivityCompat.requestPermissions(this, strings, MY_RUNTIME_PERMISSIONS);
            }
            final String myPackageName = getPackageName();
            //判断是否默认应用
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                //to do 先跳dialog  告知用户必须要同意
                Intent intent =
                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                        myPackageName);
                startActivity(intent);
            }
        }
    }

    //获取权限后回调,确认权限是否都已取得,如未全部取得,就不执行逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_RUNTIME_PERMISSIONS:
                Log.e("kkk", "执行doGetSms();");
                contactDBService.refreshTable();
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
        bt_receive_sms = findViewById(R.id.bt_receive_sms);
        bt_receive_sms.setOnClickListener(this);
        mInsertSmsButton = findViewById(R.id.bt_insert_sms);
        mInsertSmsButton.setOnClickListener(this);
        mModifySmsButton = findViewById(R.id.bt_modify_sms);
        mModifySmsButton.setOnClickListener(this);
        SMSDBService = SMSDBService.shareInstance(getApplicationContext());
        contactDBService = new MyContactDBService(this);
        contactDBService.refreshTable();
//        doGetSms();


    }

    private void doGetSms() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            LoadSmsTask loadSmsTask = new LoadSmsTask();
            AsyncTaskExecutorUtil.executeAsyncTask(loadSmsTask);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_sms:
                getPermissionAndSetDefaultApp();
//                TimePickerDialog timePickerDialog = new TimePickerDialog(this);
//                timePickerDialog.show();
//                SMSDBService.insertSmsInfo();
                doGetSms();
//                SMSDBService.modifySmsInfo();
                break;
            case R.id.go_to_edit_page:
                Intent intent = new Intent(this, NewSmsActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_hide_app:
                PackageManager manager = getPackageManager();
                manager.setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                break;
            case R.id.bt_receive_sms:
                getPermissionAndSetDefaultApp();
//                Intent receiveBroadcast = new Intent();
//                receiveBroadcast.setAction("android.provier.Telephony.SMS_RECEIVED");
//                sendBroadcast(receiveBroadcast);
                SMSDBService.insertSmsInfo();
                break;
            case R.id.bt_modify_sms:
                getPermissionAndSetDefaultApp();
                SMSDBService.modifySmsInfo();
                break;
            case R.id.bt_insert_sms:
                getPermissionAndSetDefaultApp();
                SMSDBService.insertSmsInfo();
                break;
            default:
                break;
        }
    }


    private class LoadSmsTask extends AsyncTask<Object, Object, Integer> {
        @Override
        protected void onPreExecute() {
            mHandler.sendEmptyMessageDelayed(MSG_SHOW_LOADING_DIALOG, 200);
        }

        @Override
        protected Integer doInBackground(Object... params) {
            list = SMSDBService.getSmsDetailInfoList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < list.size(); i++) {
                Log.e("kkkk", "doGetSms: " + list.get(i));
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            mHandler.removeMessages(MSG_SHOW_LOADING_DIALOG);
            mHandler.sendEmptyMessage(MSG_REFRESH_LIST_VIEW);
            mHandler.sendEmptyMessage(MSG_HIDE_LOADING_DIALOG);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_LOADING_DIALOG:
                    showLoadingDialog();
                    break;
                case MSG_HIDE_LOADING_DIALOG:
                    hideLoadingDialog();
                    break;
                case MSG_REFRESH_LIST_VIEW:
                    refreshListVIew();
                    break;
            }
        }
    };

    private void refreshListVIew() {
        if (mSmsListAdapter == null) {
            mSmsListAdapter = new SmsListAdapter(list, MainActivity.this);
            sms_list.setAdapter(mSmsListAdapter);
        } else {
            mSmsListAdapter.setList(list);
            mSmsListAdapter.notifyDataSetChanged();
        }
    }

    private void hideLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void showLoadingDialog() {
        mDialog = LoadingProgressDialog.showDialog(this, "Loading");
    }
}
