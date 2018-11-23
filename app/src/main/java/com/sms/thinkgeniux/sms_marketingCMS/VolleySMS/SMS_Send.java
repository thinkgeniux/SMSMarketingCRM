package com.sms.thinkgeniux.sms_marketingCMS.VolleySMS;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sms.thinkgeniux.sms_marketingCMS.Config;

import java.util.HashMap;
import java.util.Map;

public class SMS_Send
{
//    String url = "https://portal.smsbundles.com/sendsms_url.html?Username=03454014792&Password=bramerz792&From="
//            + from + "&To=" + to + "&Message=" + message;
//    public  static final   String BASE_SMS_URl="https://portal.smsbundles.com/sendsms_url.html";
//    public  static final String Username="03454014792";
//    public  static final String Password="bramerz792";
    //    String message;
    public void   CallAPi(final Context context, final String from, final String to, final String message){
    StringRequest request = new StringRequest(Request.Method.POST, Config.SEND_SMS_URL, new com.android.volley.Response.Listener<String>()
    {

        @Override
        public void onResponse(String response) {


            Toast.makeText(context, response , Toast.LENGTH_SHORT).show();
//            done=done+1;
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
            //  Log.e("Error",error.printStackTrace());
            Toast.makeText(context, "Volley Error"+error , Toast.LENGTH_SHORT).show();
//            onBackPressed();

        }
    }

    ){
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
//
//            params.put("Username", Username);
//            params.put("Password", Password);
            params.put("message_from", from);
            params.put("message_to", to);
            params.put("message", message);
            return params;
        }
    };    request.setRetryPolicy(new DefaultRetryPolicy(
            0,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    requestQueue.add(request);
    }
}
