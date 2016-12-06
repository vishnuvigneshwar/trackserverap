package com.triton.aquadrops;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.triton.aquadrops.adapter.ServiceHandler;
import com.triton.aquadrops.util.URL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.triton.aquadrops.util.URL.SELECTED_VENDOR;

/**
 * Created by vishnu on 24-11-2016.
 */

public class IncomingSms extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String latitude;
    String longitude;
    String Mobile;
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,
                            "senderNum: "+ senderNum + ", message: " + message, duration);
                    JSONObject jsonObject=new JSONObject(message);
                    JSONArray jsonArray=jsonObject.getJSONArray("List");
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    latitude =jsonObject1.getString("Lat");
                    longitude =jsonObject1.getString("Lan");
                    toast.show();
                    Mobile = senderNum+"";
                    new mainlistasync().execute();

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }


    public class mainlistasync extends AsyncTask<Void,Void,String> {

        String response;
        String error;
        HashMap<String,String> hashMap;
        List<NameValuePair> list=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {
            ServiceHandler sh = new ServiceHandler();


            try {

                list.add(new BasicNameValuePair("latitude",""+latitude));
                list.add(new BasicNameValuePair("longitude",""+longitude));
                list.add(new BasicNameValuePair("phone_no",""+Mobile));
                list.add(new BasicNameValuePair("UserName",""));

                response= sh.makeServiceCall(URL.VENDOR_DETAILS ,ServiceHandler.GET,list);


            }catch (Exception e){
                error = ""+e;

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);


        }
    }

}
