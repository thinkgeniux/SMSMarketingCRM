package com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sms.thinkgeniux.sms_marketingCMS.R;

/**
 * Created by Neophyte Coder on 20/12/16.
 */
public class LogBrandHolder extends RecyclerView.ViewHolder {
    TextView to,from,message,time,number;

    private Context mContext;

    public LogBrandHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;


        to =(TextView) itemView.findViewById(R.id.to);
        from =(TextView) itemView.findViewById(R.id.from);
        message =(TextView) itemView.findViewById(R.id.message);
        time =(TextView) itemView.findViewById(R.id.time);
//        number =(TextView) itemView.findViewById(R.id.number);
    }

    public void InflatingItems( String tostring,String fromstring,String messagestring,String timestring)
    {
//        Glide.with(mContext).load(item).into(imgFruits);
//        tvFruitsName.setText(s);
        to.setText(tostring);
        from.setText(fromstring);
        message.setText(messagestring);
        time.setText(timestring);
    }
}
