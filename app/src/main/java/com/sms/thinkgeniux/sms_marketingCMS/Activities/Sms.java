package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
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

import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.Spinner_Pojo;
import com.sms.thinkgeniux.sms_marketingCMS.R;
import com.sms.thinkgeniux.sms_marketingCMS.VolleySMS.SMS_Send;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Sms extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    EditText et1,mes;
    Button go;
//    Spinner from;
    String fromId="";
    String fromName="demo";
//    String[] name=new String[]{"Home","Demo","About"};
//    ArrayAdapter adapter;
    DbHelper SQLite = new DbHelper(this);
    SMS_Send send_SMS;
    private ProgressDialog loading;
//    ArrayList<Spinner_Pojo> arrayListFrom= new ArrayList<>();
    ArrayList<GroupItem> arrayListBrands=new ArrayList<>();
    ConstraintLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        et1=findViewById(R.id.et1);
//        from=(Spinner)findViewById(R.id.from);
        mes=findViewById(R.id.mes);
        go=findViewById(R.id.go);
//        spin=findViewById(R.id.spin);
////        adapter=new ArrayAdapter(getApplicationContext(),R.layout.apk,name);
//        adapter.setDropDownViewResource(R.layout.apk);
//        spin.setAdapter(adapter);
        send_SMS=new SMS_Send();
        final DbHelper SQLite = new DbHelper(Sms.this);
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
//        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if(position > 0){
//                    // Notify the selected item text
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
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et1.getText().length()==0) {
                    et1.requestFocus();
                    et1.setError(Html.fromHtml("<font color='red'>Please Write Sender Number</font>"));
                }
                else if (mes.getText().length()==0) {
                    mes.requestFocus();
                    mes.setError(Html.fromHtml("<font color='red'>Please Write Message</font>"));
                }
                else
                if (view==go)
                {
                    String to=et1.getText().toString();
                    String message=mes.getText().toString();
                    loading = ProgressDialog.show(Sms.this,"Sending...","Please wait...",false,false);
                    send_SMS.CallAPi(Sms.this,fromName,to,message);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
                    Intent in = new Intent(Sms.this, MainActivity.class);
                    in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                    in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                    startActivity(in, options.toBundle());

                    loading.dismiss();
                }




//                SQLite.insertSmsLog(to);
//                String url = "https://portal.smsbundles.com/sendsms_url.html?Username=03454014792&Password=bramerz792&From=" + from + "&To=" + to + "&Message=" + message;
//                String response = null;
//
//                try {
//                    response = new sendMessage().execute(url).get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                if (response.contains("Success"))
//                {
//                    et1.setText("");
//                    et2.setText("");
//                    mes.setText("");
//                }
//                //String res=sms.toString();
//                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
//                //readJSONFeed(url);





            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        getSenderList();
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.sms, menu);
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


            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
            Intent i = new Intent(Sms.this, MainActivity.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());


//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
            Intent i = new Intent(Sms.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
            Intent i = new Intent(Sms.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
            Intent i = new Intent(Sms.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());
            finish();

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Sms.this);
            Intent i = new Intent(Sms.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
//        from.setPrompt("Brand From");
//        from.setAdapter(spinnerArrayAdapter);
//    }
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }
}
