package com.develop.xdk.xl.nfc.attendacemachinergata.entity.MyList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.develop.xdk.xl.nfc.attendacemachinergata.Activity.AttendsListActivity;
import com.develop.xdk.xl.nfc.attendacemachinergata.R;
import com.develop.xdk.xl.nfc.attendacemachinergata.constant.C;
import com.develop.xdk.xl.nfc.attendacemachinergata.entity.BaseAttendRecord;
import com.google.gson.Gson;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<BaseAttendRecord> mdata;
    private AttendsListActivity activity;

    public ListAdapter(Context context, List<BaseAttendRecord> mdata) {
        inflater = LayoutInflater.from(context);
        this.mdata = mdata;
        activity = (AttendsListActivity) context;
        Log.e("adapter", "ListAdapter: " + new Gson().toJson(mdata) + "::::");
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (convertView == null){
        convertView = inflater.inflate(R.layout.item_list_accont, null);
        holder = new ViewHolder();
        holder.itemNumber = (TextView) convertView.findViewById(R.id.item_list_numb);
        holder.itemName = (TextView) convertView.findViewById(R.id.item_list_nume);
        holder.itemAttendsMode = (TextView) convertView.findViewById(R.id.item_list_attends_mode);
        holder.itemAttendsinORout = convertView.findViewById(R.id.item_list_attends_inORout);
        holder.itemDate = (TextView) convertView.findViewById(R.id.item_list_date);
        try {
            BaseAttendRecord user = mdata.get(position);
            holder.itemNumber.setText(Integer.toString(position + 1));
            holder.itemName.setText(user.getA_name());
            if (user.getA_attendMode() == C.SCHOOL_GATA_MODE) {
                holder.itemAttendsMode.setText(C.SCHOOL_GATA);
            } else if (user.getA_attendMode() == C.DOOR_MODE) {
                holder.itemAttendsMode.setText(C.DOOR);
            } else if (user.getA_attendMode() == C.LESSON_OR_EXAMINE_MODE) {
                holder.itemAttendsMode.setText("教室考勤");
            } else {
                holder.itemAttendsMode.setText(C.TEACHING_STAFF);
            }
            if (user.getA_inOrOutMode() == C.IN_MODE) {
                holder.itemAttendsinORout.setText(C.IN_MODE_NAME);
            } else {
                holder.itemAttendsinORout.setText(C.OUT_MODE_NAME);
            }

            holder.itemDate.setText(user.getA_attendDate());
        } catch (Exception e) {
            Log.e("adapter", "getView: is erro ");
            return convertView;
        }
        return convertView;
    }

    /**
     * 更新数据
     */
    public void onDataChange(List<BaseAttendRecord> list) {
        this.mdata = list;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView itemName;
        TextView itemNumber;
        TextView itemAttendsMode;
        TextView itemAttendsinORout;
        TextView itemDate;
    }
}
