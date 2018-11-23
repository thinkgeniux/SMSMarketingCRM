package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.ContactAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.Sms_LogAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.ContactItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.Sms_Log_Pojo;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sms_Log extends AppCompatActivity implements RecyclerItemTouch.OnItemClickListener {
    ArrayList<Sms_Log_Pojo> arrayListsmslog=new ArrayList<>();
    RecyclerView logRecyclerView;
    Sms_LogAdapter logRecylcerAdapter;
    TextView to,form,message,time;
    Button add;
    String To,From,Message,Time;
    DbHelper SQLite = new DbHelper(this);
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__log);
        logRecyclerView =findViewById(R.id.logsms);
        to=(TextView)findViewById(R.id.to);
        form=(TextView)findViewById(R.id.from);
        message=(TextView)findViewById(R.id.messag);
        time=(TextView)findViewById(R.id.time);
        add=(Button)findViewById(R.id.submit);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                To=to.getText().toString().trim();
//                From=to.getText().toString().trim();
//                Message=to.getText().toString().trim();
//                Time=to.getText().toString().trim();
//                SQLite.insertSmsLog(To,To,To,To);
//                getAllDataLogSms();
//            }
//        });
        logRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllDataLogSms();
        initAnimation();
    }

    private void getAllDataLogSms()
    {
        loading = ProgressDialog.show(Sms_Log.this,"Getting Groups...","Please wait...",false,false);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Config.SMS_REPORT_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    JSONArray arrayJson=new JSONArray(response);

                    for (int i=0;i<arrayJson.length();i++)
                    {
                        JSONObject jsonObject=arrayJson.getJSONObject(i);
                        arrayListsmslog.add(new Sms_Log_Pojo(jsonObject.getString("Sender")
                                ,jsonObject.getString("Reciever"),jsonObject.getString("Message"),jsonObject.getString("Time")));

                    }
//                    arrayListsmslog = SQLite.getAllDataSmsLog();
                    logRecylcerAdapter =new Sms_LogAdapter(arrayListsmslog,Sms_Log.this);
                    logRecyclerView.setAdapter(logRecylcerAdapter);

                    logRecylcerAdapter.notifyDataSetChanged();
                    logRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Sms_Log.this,
                            logRecyclerView, Sms_Log.this));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Sms_Log.this, response , Toast.LENGTH_SHORT).show();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Sms_Log.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

//                params.put("users_id", Id);
//                params.put("id", GroupId);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Sms_Log.this.getApplicationContext());
        requestQueue.add(request);





//        arrayListsmslog = SQLite.getAllDataSmsLog();
//        logRecylcerAdapter =new Sms_LogAdapter(arrayListsmslog,this);
//        logRecyclerView.setAdapter(logRecylcerAdapter);
//
//        logRecylcerAdapter.notifyDataSetChanged();
////        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
////        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));
//
//        logRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Sms_Log.this, logRecyclerView, this));

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, final int position) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sms_Log.this);
//        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        SQLite.deleteLog(Integer.parseInt(arrayListsmslog.get(position).getId()));
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms_Log.this);
//                        Intent i = new Intent(Sms_Log.this, Sms_Log.class);
//                        i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
//                        i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
//                        startActivity(i, options.toBundle());
//                        finish();
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });
//
//        //Showing the alert dialog
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }
}
