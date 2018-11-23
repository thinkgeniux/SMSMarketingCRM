package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.GroupBrandsAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Groups extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerItemTouch.OnItemClickListener  {
    RecyclerView GroupRecyclerView;
    GroupBrandsAdapter BrandsRecylcerAdapter;

    Button addGroup,submit;
    EditText name;
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    String GroupName;
    ConstraintLayout parent;
    String Id;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GroupRecyclerView =findViewById(R.id.rec);
        GroupRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        name=(EditText)findViewById(R.id.name);
        name.setVisibility(View.GONE);
        addGroup=(Button)findViewById(R.id.addGroup);
        submit=(Button)findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        Id = sharedPreferences.getString(Config.ID_SHARED_PREF,null);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                name.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          GroupName=name.getText().toString();
                if (GroupName.length()==0) {
                    name.requestFocus();
                    name.setError(Html.fromHtml("<font color='red'>Please Enter Valid Name</font>"));
                }
                else if (v == submit) {
                    //RegisterOrder();
                    AddGroup();
                }
            }
        });

//        String[]countries=new String[]{"Saudi -Arabia","Pakistan"};

//        GroupRecyclerView.setAdapter(new GroupItemAdaptor(countries));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getAllDataGroup();
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
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void AddGroup()
    {
        AddGroupOnline();
        blank();

    }
    ProgressDialog progress;
    private void AddGroupOnline()
    {
        new MaterialFilePicker()
                .withActivity(Groups.this)
                .withRequestCode(10)
                .withFilter(Pattern.compile(".*.csv$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();

    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(Groups.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("groupname",GroupName)
                            .addFormDataPart("users_id",Id)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("file",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .build();

                    Request request = new Request.Builder()
                            .url(Config.ADDGROUP_URL)
                            .post(request_body)
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        Snackbar.make(parent,"Group Added", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();




        }
    }
    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private void blank()
    {
        submit.setVisibility(View.GONE);
        name.setText(null);
        name.setVisibility(View.GONE);
        getAllDataGroup();
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
//        getMenuInflater().inflate(R.menu.groups, menu);
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


            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, MainActivity.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());


//        } else if (id == R.id.sendmessage) {
//            Intent intent=new Intent(MainActivity.this,Sms.class);
//            startActivity(intent);

        } else if (id == R.id.bulk) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Bulk_Message.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.message) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Sms_Log.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

        } else if (id == R.id.groups) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Groups.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());
            finish();

            //   } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.brands) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
            Intent i = new Intent(Groups.this, Brands.class);
            i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
            i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
            startActivity(i, options.toBundle());

            //   } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void getAllDataGroup()
    {
        loading = ProgressDialog.show(Groups.this,"Getting Groups...","Please wait...",false,false);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, Config.ViewGroups_URL, new com.android.volley.Response.Listener<String>()
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
                        arrayList.add(new GroupItem(jsonObject.getString("groupid"),jsonObject.getString("Groupname")));

                    }
                    BrandsRecylcerAdapter =new GroupBrandsAdapter(arrayList,Groups.this);
                    GroupRecyclerView.setAdapter(BrandsRecylcerAdapter);
                    BrandsRecylcerAdapter.notifyDataSetChanged();
                    GroupRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Groups.this, GroupRecyclerView, Groups.this));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Groups.this, response , Toast.LENGTH_SHORT).show();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Groups.this, "Network Error" , Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Groups.this.getApplicationContext());
        requestQueue.add(request);


    }

    @Override
    public void onItemClick(View view, int position)
    {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
        Intent in = new Intent(Groups.this, Group_Details.class);
        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
        in.putExtra("id",arrayList.get(position).getGroup_Id());
        in.putExtra("name",arrayList.get(position).getGroup_Name());
        startActivity(in, options.toBundle());

    }

    @Override
    public void onLongItemClick(View view, final int position)
    {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Groups.this);
//        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
////                        arrayList.remove(position);
////                        notifyDataSetChanged();
//                        SQLite.deleteGroup(Integer.parseInt(arrayList.get(position).getGroup_Id()));
////                        Intent intent= new Intent(Groups.this, Groups.class);
////                        startActivity(intent);
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Groups.this);
//                        Intent in = new Intent(Groups.this, Groups.class);
//                        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
//                        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
////                        in.putExtra("id",arrayList.get(position).getGroup_Id());
////                        in.putExtra("name",arrayList.get(position).getGroup_Name());
//                        startActivity(in, options.toBundle());
//
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
