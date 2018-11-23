package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.ContactAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.GroupBrandsAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.CSVPojo;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.ContactItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.Spinner_Pojo;
import com.sms.thinkgeniux.sms_marketingCMS.R;
import com.sms.thinkgeniux.sms_marketingCMS.VolleySMS.SMS_Send;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Bulk_Message extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//    EditText et1,et2,mes;
    EditText message;
    Button sendSms,addcsv;
    Spinner tospinner,fromspinner;

    String toName="",fromName="demo";
    boolean thread =true;
    String tId="",fromId="",messagestring,totalnumberString,donnumberString;

    private ProgressDialog loading;
    ConstraintLayout parent;
    boolean CSV=false;
    int GroupIdInt,totalnumberInteger,donenumberInteger=0;
//    String color_name="",size_name="";
//    String[] names=new String[]{"Home","Demo","List"};
    ArrayAdapter<String> adapter;
    ArrayList<ContactItem> arrayListContactsfromSqlite=new ArrayList<>();
    ArrayList<CSVPojo> arrayListContactsfromCsv=new ArrayList<>();
    ArrayList<Spinner_Pojo> arrayListTo= new ArrayList<>();
//    ArrayList<Spinner_Pojo> arrayListFrom= new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    ArrayList<GroupItem> arrayListGroups=new ArrayList<>();
    ArrayList<GroupItem> arrayListBrands=new ArrayList<>();
String Id;
//    SMS_Send send_SMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_bulk__message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



//
//        et1=findViewById(R.id.et1);
//     //   et2=findViewById(R.id.et2);
//        mes=findViewById(R.id.mes);
        sendSms=findViewById(R.id.go);
        message=(EditText)findViewById(R.id.message) ;
        addcsv=(Button)findViewById(R.id.adcsv);
        addcsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCSVFile();
            }
        });
//        send_SMS=new SMS_Send();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        Id = sharedPreferences.getString(Config.ID_SHARED_PREF,null);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)


            {
                if (message.getText().length()==0) {
                    message.requestFocus();
                    message.setError(Html.fromHtml("<font color='red'>Please Write Message</font>"));
                }else
                    if (fromName.equals(""))
                    {
                        Snackbar.make(parent, "Please Select Brand", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                }
                else if (toName.equals("")&&arrayListContactsfromCsv.size()==0)

                {
                    Snackbar.make(parent, "Please Chose Phone Book To Send", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (v==sendSms)
                {
                    loading = ProgressDialog.show(Bulk_Message.this,"Sending...","Please wait...",false,false);
                    messagestring=message.getText().toString().trim();
                    if (!CSV) {
                        getStartSending();
                    }else if (CSV)
                    {
                        SendMessagesFromCSVFile();
                    }
                }



            }
        });
        parent=(ConstraintLayout)findViewById(R.id.parent);
        parent.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });
//        spin1=findViewById(R.id.from);

        tospinner = (Spinner)findViewById(R.id.tospinner);
//        fromspinner = (Spinner)findViewById(R.id.from);
        // Initializing an ArrayAdapter

        tospinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position > 0){
                    // Notify the selected item text
                    Spinner_Pojo group = (Spinner_Pojo) parent.getSelectedItem();
                    toName = group.getName().toString();
                    tId = group.getId().toString();
                    addcsv.setEnabled(false);
                    addcsv.setBackground(getResources().getDrawable(R.color.greyTextColor));
                    getAllDataGroupContactsAndSending();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        fromspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position > 0) {
//                    Spinner_Pojo brand = (Spinner_Pojo) parent.getSelectedItem();
//                    fromName = brand.getName().toString();
//                    fromId = brand.getId().toString();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        adapter=new ArrayAdapter<String>(this,R.layout.apk,names);
//        adapter.setDropDownViewResource(R.layout.apk);
////        spin1.setAdapter(adapter);

//      go.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//            //  String from=et2.getText().toString();
//              String message=mes.getText().toString();
//          }
//      });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        getSenderList();
        getGroupsList();
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void getStartSending()
    {
        for (int i=0;i<arrayListContactsfromSqlite.size();i++)
        {
            String sendingNumber="0"+arrayListContactsfromSqlite.get(i).getContact_Number();
//            send_SMS.CallAPi(Bulk_Message.this,fromName,sendingNumber,messagestring);
            CallSMSAPi(sendingNumber);
//            Toast.makeText(Bulk_Message.this,sendingNumber,Toast.LENGTH_LONG).show();
        }
        TrackingMessages();
    }

    private void SendMessagesFromCSVFile()
    {
        for (int i=0;i<arrayListContactsfromCsv.size();i++)
        {
            String sendingNumber="0"+arrayListContactsfromCsv.get(i).getNumber();
//            send_SMS.CallAPi(Bulk_Message.this,fromName,sendingNumber,messagestring);
            CallSMSAPi(sendingNumber);
//            Toast.makeText(Bulk_Message.this,sendingNumber,Toast.LENGTH_LONG).show();
        }
        loading.dismiss();
        TrackingMessages();
    }

    private void getAllDataGroupContactsAndSending()
    {
        loading = ProgressDialog.show(Bulk_Message.this,"Getting Groups...","Please wait...",false,false);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Config.DetailGroup_URL, new com.android.volley.Response.Listener<String>()
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
                        arrayListContactsfromSqlite.add(new ContactItem(jsonObject.getString("Groupname"),jsonObject.getString("Phone_number")));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Bulk_Message.this, response , Toast.LENGTH_SHORT).show();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Bulk_Message.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("users_id", Id);
                params.put("id", tId);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Bulk_Message.this.getApplicationContext());
        requestQueue.add(request);
        loading.dismiss();
//        TrackingMessages();
    }

    private void CallSMSAPi(final String sendingNumberString )
    {
        StringRequest request = new StringRequest(Request.Method.POST, Config.SEND_SMS_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                if (response.equals("success")){
                 donenumberInteger=donenumberInteger+1;}
//                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
//                    String date = df.format(Calendar.getInstance().getTime());
//                    SQLite.insertSmsLog(sendingNumberString,fromName,messagestring,date);

                else {
                    donenumberInteger = donenumberInteger + 1;
                    Snackbar.make(parent, response, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


//
//            {
////                loading.dismiss();
////                JSONObject abc = new JSONObject(response);
////                int j=abc.length();
////                for(int i=j; i>= 1; i--) {
////                    String num = String.valueOf(i);
////                    JSONObject data = abc.getJSONObject(num);
////                    arrayList.add(new Products_pojo(data.getString("product_id"), data.getString("pro_name")
////                            , data.getString("img_url").replace("localhost", Config.ip),data.getString("sku")
////                            ,data.getString("product_quantity"),data.getString("price").replace(".0000","Rs")));
////
////                }
////                adapter=new All_Products_Adapter(arrayList,All_Products.this);
////                recyclerView.setAdapter(adapter);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
////                Toast.makeText(All_Products.this,"Nothing is Available For Time Being",Toast.LENGTH_LONG).show();
//////                loading.dismiss();
//////                onBackPressed();
//            }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                donenumberInteger=donenumberInteger+1;
                Snackbar.make(parent, "Network Error", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

//                params.put("Username", SMS_Send.Username);
//                params.put("Password", SMS_Send.Password);
                params.put("message_from", fromName);
                params.put("message_to", sendingNumberString);
                params.put("message", messagestring);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Bulk_Message.this.getApplicationContext());
        requestQueue.add(request);

    }

    private void getGroupsList()
    {
        loading = ProgressDialog.show(Bulk_Message.this,"Getting Groups...","Please wait...",false,false);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Config.ViewGroups_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    JSONArray arrayJson=new JSONArray(response);
                    arrayListTo.add(new Spinner_Pojo("0","ADD PHONE BOOK"));
                    for (int i=0;i<arrayJson.length();i++)
                    {
                        JSONObject jsonObject=arrayJson.getJSONObject(i);
                        arrayListTo.add(new Spinner_Pojo(jsonObject.getString("groupid"),jsonObject.getString("Groupname")));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Bulk_Message.this, response , Toast.LENGTH_SHORT).show();
                }

                final ArrayAdapter<Spinner_Pojo> spinnerArrayAdapter = new ArrayAdapter<Spinner_Pojo>(
                        Bulk_Message.this,R.layout.spinner_item,arrayListTo){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };

                tospinner.setPrompt("Sending Group");
                tospinner.setAdapter(spinnerArrayAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Bulk_Message.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("users_id", Id);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Bulk_Message.this.getApplicationContext());
        requestQueue.add(request);

    }

//    private void getSenderList()
//    {
//        arrayListBrands = SQLite.getAllDataBrands();
//        arrayListFrom.add(new Spinner_Pojo("0","BRAND NAME"));
//        for (int i=0;i<arrayListBrands.size();i++)
//        {
//            arrayListFrom.add(new Spinner_Pojo(arrayListBrands.get(i).getGroup_Id(),arrayListBrands.get(i).getGroup_Name()));
//        }
////        ArrayAdapter<Spinner_Pojo> adapter = new
////                ArrayAdapter<Spinner_Pojo>(Bulk_Message.this,
////                android.R.layout.simple_spinner_dropdown_item, arrayListFrom);
//
//        final ArrayAdapter<Spinner_Pojo> spinnerArrayAdapter = new ArrayAdapter<Spinner_Pojo>(
//                this,R.layout.spinner_item,arrayListFrom){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view;
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//
//        fromspinner.setPrompt("Brand From");
//        fromspinner.setAdapter(spinnerArrayAdapter);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.bulk__message, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.home) {
//
//            Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
//            startActivity(intent);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, MainActivity.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

//            Intent intent=new Intent(Bulk_Message.this,Bulk_Message.class);
//            startActivity(intent);
//            finish();
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
//            Intent intent=new Intent(Bulk_Message.this,Sms_Log.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
//            Intent intent=new Intent(Bulk_Message.this,Groups.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
//            Intent intent=new Intent(Bulk_Message.this,Brands.class);
//            startActivity(intent);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
            Intent i = new Intent(Bulk_Message.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void TrackingMessages()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Bulk_Message.this);
        LayoutInflater inflater = Bulk_Message.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.tranking_message_dialog, null);
        builder.setView(dialogView);
////        Button shop,chek;
//        shop=(Button)dialogView.findViewById(R.id.shop);
//        chek=(Button)dialogView.findViewById(R.id.chek);
        final TextView totalnumber,donenumber;
         final Button donbtn;

         donbtn=(Button)dialogView.findViewById(R.id.dimissbutton);

        totalnumber=(TextView)dialogView.findViewById(R.id.total);
        donenumber=(TextView)dialogView.findViewById(R.id.done);
        builder.setTitle("Message Sending");
        if (!CSV) {
            totalnumber.setText(String.valueOf(arrayListContactsfromSqlite.size()));
        }else if(CSV) {
            totalnumber.setText(String.valueOf(arrayListContactsfromCsv.size()));

        }
        donbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CSV){
                    if (donenumberInteger>=arrayListContactsfromSqlite.size())
                    {
                        AfterDimmissDialog();
                                finish();

                    }else
                    {
                        Toast.makeText(Bulk_Message.this, "Some Messages are Left" , Toast.LENGTH_SHORT).show();
                    }
                }else if (CSV)
                {
                    if (donenumberInteger>=arrayListContactsfromCsv.size())
                    {
                        AfterDimmissDialog();
                                finish();

                    }else
                    {
                        Toast.makeText(Bulk_Message.this, "Some Messages are Left" , Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        final Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (thread) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                donenumber.setText(String.valueOf(donenumberInteger));
                                if (!CSV){
                                    if (donenumberInteger>=arrayListContactsfromSqlite.size())
                                    {
                                        thread=false;
                                      Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
                                      startActivity(intent);
                                      finish();

                                    }
                                }else if (CSV)
                                {
                                    if (donenumberInteger>=arrayListContactsfromCsv.size())
                                    {
                                        thread=false;
                                        Intent intent=new Intent(Bulk_Message.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }

                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
//        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//                if (!CSV){
//                if (donenumberInteger>=arrayListContactsfromSqlite.size())
//                {
//                    AfterDimmissDialog();
//
//            }else
//                {
//                    Toast.makeText(Bulk_Message.this, "Some Messages are Left" , Toast.LENGTH_SHORT).show();
//                }
//            }else if (CSV)
//            {
//                if (donenumberInteger>=arrayListContactsfromCsv.size())
//                {
//                    AfterDimmissDialog();
//
//                }else
//                {
//                    Toast.makeText(Bulk_Message.this, "Some Messages are Left" , Toast.LENGTH_SHORT).show();
//                }
//
//            }
//            }
//
//        });
        builder.setCancelable(false);
        builder.show();
    }

    private void AfterDimmissDialog()
    {
        thread=false;
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Bulk_Message.this);
        Intent in = new Intent(Bulk_Message.this, MainActivity.class);
        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
        startActivity(in, options.toBundle());
        finish();

    }

    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }
    private void selectCSVFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
        Intent i = Intent.createChooser(intent, "File");
//        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode== Activity.RESULT_OK)
            try {
                final Uri imageUri=data.getData();
                File initialFile = new File(data.getData().getPath());
//                InputStream targetStream = new FileInputStream(initialFile);
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                proImportCSV(inputStream);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
    }
    private void proImportCSV(InputStream from) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(from, Charset.forName("UTF-8")));
        String line="";
        try{
            //Read each line
            while ((line = reader.readLine()) != null) {
                arrayListContactsfromCsv.add(new CSVPojo(line));
                tospinner.setEnabled(false);
                CSV=true;

//            cur.setCapital(RowData[1]);

                //Add the State object to the ArrayList (in this case we are the ArrayList)
            }
        }catch (IOException e) {
            Log.v("Main Activity", "Error Reading File on Line " + line, e);
            e.printStackTrace();
        }
    }
}
