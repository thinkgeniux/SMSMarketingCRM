package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity {

    EditText nameaccount,password,numberaccount,numbervarify,codevarify;
    String snameaccount,spassword,snumberaccount,snumbervarify,scodevarify;
    Button accountdetail,varifyaccount;
    ConstraintLayout accountdetaillayout,varifyaccountlayout,parent;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameaccount=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        numberaccount=(EditText)findViewById(R.id.numberacc);
        numbervarify=(EditText)findViewById(R.id.numbervarify);
        codevarify=(EditText)findViewById(R.id.codevarify);

        accountdetail=(Button)findViewById(R.id.enteraccount);
        varifyaccount=(Button)findViewById(R.id.varify);

        accountdetaillayout=(ConstraintLayout)findViewById(R.id.accountdetail);
        varifyaccountlayout=(ConstraintLayout)findViewById(R.id.varifyaccount);
        parent=(ConstraintLayout)findViewById(R.id.parent);

        varifyaccountlayout.setVisibility(View.GONE);
        accountdetail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        snameaccount=nameaccount.getText().toString().trim();
        spassword=password.getText().toString().trim();
        snumberaccount=numberaccount.getText().toString().trim();
        RegisterAccount();
    }
    });
        varifyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snumbervarify=numbervarify.getText().toString().trim();
                scodevarify=codevarify.getText().toString().trim();

                Varifyaccountfun();
            }
        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void Varifyaccountfun()
    {
        loading = ProgressDialog.show(Register.this,"Checking Credentials...","Please wait...",false,false);
        final StringRequest request = new StringRequest(Request.Method.POST, Config.VARIFY_ACC_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                if (response.equals("success"))

                {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Register.this);
                    Intent i = new Intent(Register.this, MainActivity.class);
                    i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                    i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                    startActivity(i, options.toBundle());

                }
                else {
                    varifyaccountlayout.setVisibility(View.GONE);
                    accountdetaillayout.setVisibility(View.VISIBLE);

                    Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Register.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number", snumbervarify);
                params.put("code", scodevarify);
//                params.put("password ", spassword);
//                params.put("To", to);
//                params.put("Message", message);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this.getApplicationContext());
        requestQueue.add(request);
    }

    private void RegisterAccount()
    {
        loading = ProgressDialog.show(Register.this,"Checking Credentials...","Please wait...",false,false);
        final StringRequest request = new StringRequest(Request.Method.POST, Config.REGISTER_ACC_URL, new com.android.volley.Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                loading.dismiss();
                if (response.equals("please enter valid code which u get in ur phone"))

                {
                    varifyaccountlayout.setVisibility(View.VISIBLE);
                    accountdetaillayout.setVisibility(View.GONE);

                }
                else {

                    Toast.makeText(Register.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Register.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("number", snumberaccount);
                params.put("username", snameaccount);
                params.put("password ", spassword);
//                params.put("To", to);
//                params.put("Message", message);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Register.this.getApplicationContext());
        requestQueue.add(request);
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
