package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.ContactAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketingCMS.Config;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.CSVPojo;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.ContactItem;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group_Details extends AppCompatActivity implements RecyclerItemTouch.OnItemClickListener  {
    String GroupId,GroupName;
    ArrayList<CSVPojo> arrayListCSV=new ArrayList<>();
    ArrayList<CSVPojo> arrayListToAdd=new ArrayList<>();
    ArrayList<ContactItem> arrayListContacts=new ArrayList<>();
//    Button getCSV,saveCSV;
//    TextView statusCSV;
    DbHelper SQLite = new DbHelper(this);
    boolean exist;
    ConstraintLayout coordinatorLayout;

    RecyclerView  BrandsRecyclerView;
    ContactAdapter BrandsRecylcerAdapter;
    private ProgressDialog loading;
    String Id;
    int GroupIdInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__details);
        final Intent intent = getIntent();
        GroupId = intent.getStringExtra("id");
        GroupName = intent.getStringExtra("name");
//        getCSV=(Button)findViewById(R.id.getCSV);
//        statusCSV=(TextView)findViewById(R.id.statuscsv);
//        saveCSV=(Button)findViewById(R.id.saveCSV);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        Id = sharedPreferences.getString(Config.ID_SHARED_PREF,null);
        exist=false;
        coordinatorLayout = (ConstraintLayout) findViewById(R.id
                .coordinatorLayout);
//        GroupIdInt = Integer.parseInt(GroupId);
        BrandsRecyclerView =findViewById(R.id.recycler_all_contacts);
        BrandsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllDataGroupContacts();
//        blank();
//        getCSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectCSVFile();
//            }
//        });
//        saveCSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SaveCSVFILETOSQLLITE();
//            }
//        });
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);

    }

//    private void blank()
//    {
//        saveCSV.setVisibility(View.GONE);
//        statusCSV.setVisibility(View.GONE);
//    }
//
//    private void SaveCSVFILETOSQLLITE()
//    {
//        loading = ProgressDialog.show(Group_Details.this,"Saving...","Please wait...",false,false);
//        int j=arrayListCSV.size();
//        for (int i=0;i<j;i++)
//        {
//            for (int k=0;k<arrayListContacts.size();k++)
//            {
//                if (arrayListCSV.get(i).getNumber().equals(arrayListContacts.get(k).getContact_Name()));
//                {
//                   exist=true;
//                }
//
//            }
//            if (!exist)
//            {
//            SQLite.insertContacts(arrayListCSV.get(i).getNumber(),GroupName,GroupIdInt);
//            exist=false;
//            }
//        }
//        loading.dismiss();
//        getAllDataGroupContacts();
//        blank();
//    }

    private void getAllDataGroupContacts()
    {
        loading = ProgressDialog.show(Group_Details.this,"Getting Groups...","Please wait...",false,false);
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
                        arrayListContacts.add(new ContactItem(jsonObject.getString("Groupname"),jsonObject.getString("Phone_number")));

                    }

                    BrandsRecylcerAdapter =new ContactAdapter(arrayListContacts,Group_Details.this);
                    BrandsRecyclerView.setAdapter(BrandsRecylcerAdapter);
                    BrandsRecylcerAdapter.notifyDataSetChanged();

                    BrandsRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Group_Details.this, BrandsRecyclerView, Group_Details.this));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Group_Details.this, response , Toast.LENGTH_SHORT).show();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                //  Log.e("Error",error.printStackTrace());
                Toast.makeText(Group_Details.this, "Network Error" , Toast.LENGTH_SHORT).show();
//            onBackPressed();

            }
        }

        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("users_id", Id);
                params.put("id", GroupId);
                return params;
            }
        };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(Group_Details.this.getApplicationContext());
        requestQueue.add(request);





//        arrayListContacts = SQLite.getAllDataContacts(GroupIdInt);
////        ContactRecylcerAdapter =new ContactsItemAdaptor(arrayListContacts,Group_Details.this);
////        ContactRecyclerView.setAdapter(ContactRecylcerAdapter);
////        ContactRecylcerAdapter.notifyDataSetChanged();
//
//        BrandsRecylcerAdapter =new ContactAdapter(arrayListContacts,this);
//        BrandsRecyclerView.setAdapter(BrandsRecylcerAdapter);
//
//        BrandsRecylcerAdapter.notifyDataSetChanged();
////        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
////        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));
//
//        BrandsRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Group_Details.this, BrandsRecyclerView, this));

    }

//    private void selectCSVFile(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("text/csv");
////        intent.addCategory(Intent.CATEGORY_OPENABLE);
////        intent.setType("*/*");
//        Intent i = Intent.createChooser(intent, "File");
////        startActivityForResult(Intent.createChooser(intent, "Open CSV"), 1);
//        startActivityForResult(i, 1);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode==1 && resultCode== Activity.RESULT_OK)
//            try {
//                final Uri imageUri=data.getData();
//                File initialFile = new File(data.getData().getPath());
////                InputStream targetStream = new FileInputStream(initialFile);
//                InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                proImportCSV(inputStream);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//    }
//    private void proImportCSV(InputStream from) {
////        BufferedReader reader = new BufferedReader(new InputStreamReader(from, Charset.forName("UTF-8")));
////        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
////        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
////        byte[] imgBytes=from.toByteArray();
//
////
////        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
//    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, final int position) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Group_Details.this);
//        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
//        alertDialogBuilder.setPositiveButton("Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
////                        SQLite.deleteContact(Integer.parseInt(arrayListContacts.get(position).getContact_Id()));
////                        Intent intent= new Intent(Group_Details.this,Group_Details.class);
////                        intent.putExtra("id",GroupId);
////                        intent.putExtra("name",GroupName);
////                        startActivity(intent);
////                        finish();
//
//                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Group_Details.this);
//                        Intent in = new Intent(Group_Details.this, MainActivity.class);
//                        in.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
//                        in.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
//                        in.putExtra("id",GroupId);
//                        in.putExtra("name",GroupName);
//                        startActivity(in, options.toBundle());
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
