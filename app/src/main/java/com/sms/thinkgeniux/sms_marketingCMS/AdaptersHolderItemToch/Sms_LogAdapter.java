package com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.Sms_Log_Pojo;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import java.util.ArrayList;

/**
 * Created by Neophyte Coder on 20/12/16.
 */
public class Sms_LogAdapter extends RecyclerView.Adapter<LogBrandHolder> {
    ArrayList<Sms_Log_Pojo> arrayList=new ArrayList<>();
    Activity activity;

    public Sms_LogAdapter(ArrayList<Sms_Log_Pojo> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public LogBrandHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_log_item, parent, false);

        return new LogBrandHolder(view, activity);
    }

    @Override
    public void onBindViewHolder(LogBrandHolder holder, int position) {
        holder.InflatingItems(arrayList.get(position).getTo(),arrayList.get(position).getFrom(),
                arrayList.get(position).getMessage(),arrayList.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
