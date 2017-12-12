package com.first.myapp.com.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chauvard on 10/8/16.
 */

public class SmsListAdapter extends BaseAdapter {
    private List<SmsDetailInfo> mList;
    private Context mContext;

    public SmsListAdapter(List<SmsDetailInfo> list, Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
        setList(list);
    }

    public void setList(List<SmsDetailInfo> list) {
        mList.clear();
        mList.addAll(list);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = View.inflate(mContext, R.layout.item_for_sms_list, null);
            holder.id = (TextView) convertView.findViewById(R.id.id_list_item);
            holder.phoneNum = (TextView) convertView.findViewById(R.id.sms_phone_num);
            holder.name = (TextView) convertView.findViewById(R.id.sms_contacts_name);
            holder.data = (TextView) convertView.findViewById(R.id.data_lv_item);
            holder.type = (TextView) convertView.findViewById(R.id.type_lv_item);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.id.setText(String.valueOf(position + 1));
        holder.phoneNum.setText(mList.get(position).getPhoneNum());
        holder.name.setText(mList.get(position).getContactsName());
        holder.data.setText(mList.get(position).getData());
        holder.type.setText(mList.get(position).getType());


        return convertView;
    }

    class Holder {
        private TextView id;
        private TextView phoneNum;
        private TextView name;
        private TextView data;
        private TextView type;
    }
}
