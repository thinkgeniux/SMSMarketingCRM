package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.CharacterPickerDialog;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    Button btn1,btn2,btn3;
    DbHelper SQLite = new DbHelper(this);
    Constants.TransitionType type;
    CardView cvSinglesms,cvmultisms,cvgroups,cvbrands,cvreports,cvexit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cvSinglesms=(CardView)findViewById(R.id.cvsinglemessage);
        cvmultisms=(CardView)findViewById(R.id.cvmulmessage);
        cvgroups=(CardView)findViewById(R.id.cvgroups);
        cvbrands=(CardView)findViewById(R.id.cvbrands);
        cvreports=(CardView)findViewById(R.id.cvreports);
        cvexit=(CardView)findViewById(R.id.cvexit);
        cvSinglesms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,Sms.class);
//                startActivity(intent);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent i = new Intent(MainActivity.this, Sms.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());

            }
        });
        cvmultisms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent i = new Intent(MainActivity.this, Bulk_Message.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
            }
        });
        cvgroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent i = new Intent(MainActivity.this, Groups.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
            }
        });
        cvbrands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent i = new Intent(MainActivity.this, Brands.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
            }
        });
        cvreports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                Intent i = new Intent(MainActivity.this, Sms_Log.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
            }
        });
        cvexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

//        btn1=findViewById(R.id.btn1);
//        btn2=findViewById(R.id.btn2);
//        btn3=findViewById(R.id.btn3);
//        SQLite = new DbHelper(getApplicationContext());
//        //showing Toast
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,Sms.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"Sms has been Sent",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //coding for Bulk sms send
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,Bulk_Message.class);
//                startActivity(intent);
//                Toast.makeText(MainActivity.this, "Bulk sms has been sent", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //coding for exit button
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//                System.exit(0);
//                Toast.makeText(MainActivity.this, "Exit pressed", Toast.LENGTH_SHORT).show();
//            }
//        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
        getPermissions();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            Snackbar.make(cvSinglesms, "Press Exit to leave this Application", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

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

            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            finish();


//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            Intent i = new Intent(MainActivity.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            Intent i = new Intent(MainActivity.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            Intent i = new Intent(MainActivity.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

     //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            Intent i = new Intent(MainActivity.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.logout) {
            logout();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new
                            String[]{android.Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");
                        editor.putString(Config.ID_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.clear();
                        editor.apply();
                        //Starting login activity
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                        Intent i = new Intent(MainActivity.this, SigniIn.class);
                        i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                        i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                        startActivity(i, options.toBundle());
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
