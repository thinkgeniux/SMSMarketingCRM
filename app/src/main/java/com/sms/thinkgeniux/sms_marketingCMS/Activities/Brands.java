package com.sms.thinkgeniux.sms_marketingCMS.Activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.GroupBrandsAdapter;
import com.sms.thinkgeniux.sms_marketingCMS.Constants;
import com.sms.thinkgeniux.sms_marketingCMS.DataBase.DbHelper;
import com.sms.thinkgeniux.sms_marketingCMS.AdaptersHolderItemToch.RecyclerItemTouch;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketingCMS.R;

import java.util.ArrayList;

public class Brands extends AppCompatActivity implements RecyclerItemTouch.OnItemClickListener  {
    RecyclerView  BrandsRecyclerView;
    GroupBrandsAdapter BrandsRecylcerAdapter;

    Button addGroup,submit;
    EditText name;
    ArrayList<GroupItem> arrayList=new ArrayList<>();
    DbHelper SQLite = new DbHelper(this);
    String GroupName;
    ConstraintLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);


        BrandsRecyclerView =findViewById(R.id.rec);
        BrandsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        name=(EditText)findViewById(R.id.name);
        name.setVisibility(View.GONE);
        addGroup=(Button)findViewById(R.id.addBrand);
        submit=(Button)findViewById(R.id.submit);
        submit.setVisibility(View.GONE);
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



        getAllDataBrands();
        initAnimation();
        getWindow().setAllowEnterTransitionOverlap(false);
    }

    private void getAllDataBrands()
    {
        arrayList = SQLite.getAllDataBrands();

//        for (int i = 0; i < row.size(); i++) {
////            String id = row.get(i).get(TAG_ID);
////            String poster = row.get(i).get(TAG_NAME);
////            String title = row.get(i).get(TAG_ADDRESS);
////
////            Data data = new Data();
////
////            data.setId(id);
////            data.setName(poster);
////            data.setAddress(title);
//            arrayList.add(new GroupItem(r))
//
//            itemList.add(data);
//        }

//        adaptor.notifyDataSetChanged();


        BrandsRecylcerAdapter =new GroupBrandsAdapter(arrayList,this);
        BrandsRecyclerView.setAdapter(BrandsRecylcerAdapter);

        BrandsRecylcerAdapter.notifyDataSetChanged();
//        BrandsRecylcerAdapter.setLayoutManager(new GridLayoutManager(this, 2));
//        BrandsRecyclerView.setAdapter(new GroupBrandsAdapter(arrayList, this));

        BrandsRecyclerView.addOnItemTouchListener(new RecyclerItemTouch(Brands.this, BrandsRecyclerView, this));

    }
    private void recyclerSetup(RecyclerView recyclerView) {




    }

    private void AddGroup()
    {
        SQLite.insertBrands(GroupName.trim());
        blank();

    }

    private void blank()
    {
        submit.setVisibility(View.GONE);
        name.setText(null);
        name.setVisibility(View.GONE);
        getAllDataBrands();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onLongItemClick(View view, final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Brands.this);
        alertDialogBuilder.setMessage("Are you Sure You want to Delete");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//                        arrayList.remove(position);
//


                        SQLite.deleteBrand(Integer.parseInt(arrayList.get(position).getGroup_Id()));

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Brands.this);
                        Intent i = new Intent(Brands.this, Brands.class);
                        i.putExtra(Constants.KEY_ANIM_TYPE, Constants.TransitionType.SlideJava);
                        i.putExtra(Constants.KEY_TITLE, "Slide By Java Code");
                        startActivity(i, options.toBundle());



//                        Intent intent= new Intent(Brands.this, Brands.class);
//                        startActivity(intent);
//                        finish();

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
    private  void initAnimation()
    {

        Slide enterTransition = new Slide();
        enterTransition.setSlideEdge(Gravity.TOP);
        enterTransition.setDuration(1000);
        enterTransition.setInterpolator(new AnticipateOvershootInterpolator());
        getWindow().setEnterTransition(enterTransition);
    }
}
