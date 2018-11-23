package com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sms.thinkgeniux.sms_marketingCMS.R;

/**
 * Created by Neophyte Coder on 20/12/16.
 */
public class ContactGroupBrandHolder extends RecyclerView.ViewHolder {
    TextView gtv,iditem;

    private Context mContext;

    public ContactGroupBrandHolder(View itemView, Context context) {
        super(itemView);

        mContext = context;

        gtv =(TextView) itemView.findViewById(R.id.gtv);
        iditem =(TextView) itemView.findViewById(R.id.iditem);
    }

    public void InflatingItems(String item, String s)
    {
//        Glide.with(mContext).load(item).into(imgFruits);
//        tvFruitsName.setText(s);
        gtv.setText(s);
        iditem.setText(item);
    }
}
