package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SigniIn extends AppCompatActivity {
 CardView carduser,cardpass;
 EditText username,userpass;
 String stringUsername,stringpass;
 TextView signIn,register;
 ConstraintLayout parent;
    Constants.TransitionType type;
    private ProgressDialog loading;
    private boolean loggedIn = false;
 float elevation= (float) 10.0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        super.onCreate(savedInstanceState);

        //Remove notification bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signi_in);
        carduser=(CardView)findViewById(R.id.carduser);
        username=(EditText)findViewById(R.id.username);
        cardpass=(CardView)findViewById(R.id.cardpass);
        userpass=(EditText)findViewById(R.id.password);
        signIn=(TextView)findViewById(R.id.signin);
        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigniIn.this);
                Intent i = new Intent(SigniIn.this, Register.class);
                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                startActivity(i, options.toBundle());
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
        type = (Constants.TransitionType) getIntent().getSerializableExtra(Constants.KEY_ANIM_TYPE);
        carduser.setCardElevation(0);
        cardpass.setCardElevation(0);
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                carduser.setMinimumWidth(350);
                carduser.setMinimumHeight(100);
                carduser.setCardElevation(20);
                return false;
            }
        });
        userpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                cardpass.setMinimumWidth(350);
                cardpass.setMinimumHeight(100);
                cardpass.setCardElevation(20);
                return false;
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                byte[] encodedbytuser=Base64.encode(username.getText().toString().getBytes(), Base64.DEFAULT);
//                byte[] encodedbytpass=Base64.encode(userpass.getText().toString().getBytes(), Base64.DEFAULT);
//                stringUsername= Base64.encodeToString(encodedbytuser, Base64.DEFAULT);
//                stringpass=Base64.encodeToString(encodedbytpass, Base64.DEFAULT);

                stringUsername=username.getText().toString().trim();
                stringpass=userpass.getText().toString().trim();
                SigniInFuction();
//                Intent intent=new Intent(SigniIn.this, MainActivity.class);
//                startActivity(intent);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigniIn.this);
//                Intent i = new Intent(SigniIn.this, MainActivity.class);
//                i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.FadeJava);
//                i.putExtra(Constants.KEY_TITLE, "Fade By Java");
//                startActivity(i, options.toBundle());
//                if (stringUsername.equals("admin")&&stringpass.equals("admin123")) {
//
//
//
//                }
//                else
//                    {
//                        Snackbar.make(parent, "User Name and Password Incorrect", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//                    }
            }
        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void SigniInFuction()
    {
        loading = ProgressDialog.show(SigniIn.this,"Checking Credentials...","Please wait...",false,false);
        final StringRequest request = new StringRequest(Request.Method.POST, Config.LOGIN_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                try {
                    JSONObject object=new JSONObject(response);
                    String id=object.getString("id");
                    SharedPreferences sharedPreferences =SigniIn.this.getApplicationContext()
                            .getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                    //Creating editor to store values to shared preferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    //Adding values to editor
                    editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                    editor.putString(Config.EMAIL_SHARED_PREF, stringUsername);
                    editor.putString(Config.ID_SHARED_PREF, id);

                    //Saving values to editor
                    editor.commit();
                    MainActivitygoing();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SigniIn.this, response , Toast.LENGTH_SHORT).show();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(SigniIn.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", stringUsername);
                params.put("password", stringpass);
//                params.put("From", from);
//                params.put("To", to);
//                params.put("Message", message);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(SigniIn.this.getApplicationContext());
        requestQueue.add(request);
    }

    private void MainActivitygoing()
    {

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SigniIn.this);
        Intent i = new Intent(SigniIn.this, MainActivity.class);
        i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
        i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
        startActivity(i, options.toBundle());
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


    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true

        if (loggedIn) {
            //We will start the Profile Activity

            MainActivitygoing();

        }
}
}
