package com.triton.aquadrops;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.triton.aquadrops.adapter.ServiceHandler;
import com.triton.aquadrops.util.URL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.triton.aquadrops.util.URL.SELECTED_VENDOR;
import static com.triton.aquadrops.util.URL.mobilenumberforotp;


public class adduser extends AppCompatActivity {
    SharedPreferences sharedpreferencesuser;
    boolean net;
    EditText edt_user_name,edt_user_mobile_no,passwordf;
    Button btn_verify;
    Button btn_verify2;
    static int logreg = 1;
    String struser,strmobile,password;
    public static final String MyPREFERENCESUSER = "MyUser" ;
    public static final String UserName = "usernameKey";
    public static final String Phone = "phoneKey";
    public static final String Id = "idKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adduser_activity);
        final TextView textView = (TextView)findViewById(R.id.textView);
        final TextView Login = (TextView)findViewById(R.id.Login);
        sharedpreferencesuser = getSharedPreferences(MyPREFERENCESUSER, Context.MODE_PRIVATE);


        net=isNetworkAvailable();
        edt_user_name=(EditText)findViewById(R.id.edt_user_name);
        edt_user_mobile_no=(EditText)findViewById(R.id.edt_user_mobile_no);
        passwordf=(EditText)findViewById(R.id.edt_user_passwword);
        btn_verify=(Button)findViewById(R.id.btn_verify);
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                struser = edt_user_name.getText().toString();
                strmobile=edt_user_mobile_no.getText().toString().trim();
                if (edt_user_name.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter user name", Toast.LENGTH_SHORT).show();
                }
                else if(edt_user_mobile_no.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Enter mobile number", Toast.LENGTH_SHORT).show();
                }
                else if(edt_user_mobile_no.getText().toString().trim().length()<10){
                    Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (net){


                       /* SharedPreferences.Editor editoruser = sharedpreferencesuser.edit();
                        editoruser.putString(Id,"0");
                        editoruser.putString(UserName, edt_user_name.getText().toString());
                        editoruser.putString(Phone, edt_user_mobile_no.getText().toString());
                        editoruser.commit();
*/
                        new mainlistasync().execute();



                        //  new UserRegisterAsync().execute();
                    }else {
                        AlertDialog dialog = new AlertDialog.Builder(adduser.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
                        dialog.setTitle("Internet Connection");
                        dialog.setMessage("Now Internet connection is not available.Check your Network");
                        dialog.setIcon(R.drawable.fail);
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                }
            }
        }); btn_verify2=(Button)findViewById(R.id.Locationadd);
        btn_verify2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                struser = edt_user_name.getText().toString();
                strmobile=edt_user_mobile_no.getText().toString().trim();


                    if (net){


                       /* SharedPreferences.Editor editoruser = sharedpreferencesuser.edit();
                        editoruser.putString(Id,"0");
                        editoruser.putString(UserName, edt_user_name.getText().toString());
                        editoruser.putString(Phone, edt_user_mobile_no.getText().toString());
                        editoruser.commit();
*/
                        new mainlistasync2().execute();



                        //  new UserRegisterAsync().execute();
                    }else {
                        AlertDialog dialog = new AlertDialog.Builder(adduser.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
                        dialog.setTitle("Internet Connection");
                        dialog.setMessage("Now Internet connection is not available.Check your Network");
                        dialog.setIcon(R.drawable.fail);
                        dialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dialog.show();
                    }
                           }
        });
    }

    public void onClick(View v) {

        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
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

                list.add(new BasicNameValuePair("latitude",""+LocateShop.latitude));
                list.add(new BasicNameValuePair("longitude",""+LocateShop.longitude));
                list.add(new BasicNameValuePair("phone_no",""+strmobile));
                list.add(new BasicNameValuePair("UserName",""+struser));

                response= sh.makeServiceCall(URL.VENDOR_DETAILS ,ServiceHandler.GET,list);



            }catch (Exception e){
                error = ""+e;

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Intent i = new Intent(adduser.this, LocateShop.class);
            startActivity(i);
            finish();

        }
    }    public class mainlistasync2 extends AsyncTask<Void,Void,String> {

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

                list.add(new BasicNameValuePair("latitude",""+LocateShop.latitude));
                list.add(new BasicNameValuePair("longitude",""+LocateShop.longitude));
                list.add(new BasicNameValuePair("phone_no",""+strmobile));
                list.add(new BasicNameValuePair("UserName",""+struser));

                response= sh.makeServiceCall(URL.VENDOR_DETAILS2 ,ServiceHandler.GET,list);



            }catch (Exception e){
                error = ""+e;

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            Intent i = new Intent(adduser.this, LocateShop.class);
            startActivity(i);
            finish();

        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info !=null && info.isConnected();
    }
}
