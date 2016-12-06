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
import java.util.List;

import static com.triton.aquadrops.util.URL.mobilenumberforotp;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferencesuser;
    boolean net;
    EditText edt_user_name,edt_user_mobile_no,passwordf;
    Button btn_verify;
    static int logreg = 1;
    String struser,strmobile,password;
    public static final String MyPREFERENCESUSER = "MyUser" ;
    public static final String UserName = "usernameKey";
    public static final String Phone = "phoneKey";
    public static final String Id = "idKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


                        SharedPreferences.Editor editoruser = sharedpreferencesuser.edit();
                        editoruser.putString(Id,"0");
                        editoruser.putString(UserName, edt_user_name.getText().toString());
                        editoruser.putString(Phone, edt_user_mobile_no.getText().toString());
                        editoruser.commit();

                        if(edt_user_name.getText().toString().equals(new StringBuilder(passwordf.getText().toString()).reverse().toString())) {
                            Intent i = new Intent(MainActivity.this, LocateShop.class);
                            startActivity(i);
                            finish();
                        }

                        //  new UserRegisterAsync().execute();
                    }else {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();
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
        });
    }

    public void onClick(View v) {

        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public class UserRegisterAsync extends AsyncTask<Void,Void,String> {
        ProgressDialog progressDialog;
        String response;
        String status = "";
        String id,fn,phn;
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            struser  = edt_user_name.getText().toString().trim();
            strmobile  = edt_user_mobile_no.getText().toString().trim();
            password  = passwordf.getText().toString().trim();
            String urlstruser= null;
            try {
                urlstruser = URLEncoder.encode(struser, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            list.add(new BasicNameValuePair("username", urlstruser));
            list.add(new BasicNameValuePair("phoneno", strmobile));
            list.add(new BasicNameValuePair("password", password));
        }

        @Override
        protected String doInBackground(Void... params) {

            ServiceHandler sh=new ServiceHandler();

           // String restUrl = URLEncoder.encode("You url parameter value", "UTF-8");
            // System.out.println("URL VALUES-------->" + url);
            if(logreg == 2)
            try {

                //String url = URL.USER_REGISTER + "&username=" +urlstruser + "&phoneno=" + strmobile+ "&password=" + password;
                response = sh.makeServiceCall(URL.USER_REGISTER1, ServiceHandler.GET,list);
                JSONObject jsonObject=new JSONObject(response);
                JSONArray array=jsonObject.getJSONArray("data");
                for (int i=0;i<array.length();i++){
                    JSONObject jsonObject1=array.getJSONObject(i);

                    status=jsonObject1.optString("status");
                }
            }
            catch (Exception e){
            }
            else if(logreg == 1)
            try {

            String url = URL.USER_LOGIN1 +"phoneno=" + strmobile+ "&password=" + password;
                String urlstruser= URLEncoder.encode(url, "UTF-8");
                Log.e("loginurl",""+urlstruser);
                response = sh.makeServiceCall(URL.USER_LOGIN11, ServiceHandler.GET,list);
                Log.e("response",""+response);
                JSONObject jsonObject=new JSONObject(response);
                JSONArray array=jsonObject.getJSONArray("List");
                for (int i=0;i<array.length();i++){
                    JSONObject jsonObject1=array.getJSONObject(i);
                    status=jsonObject1.optString("status" + "");
                    id=jsonObject1.optString("id");
                    fn=jsonObject1.optString("fn");
                    phn=jsonObject1.optString("phn");
                }
            }
            catch (Exception e){
                Log.e("ERROR 1",""+e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            //Toast.makeText(getApplicationContext(), struser, Toast.LENGTH_SHORT).show();
            if ((status.equals("1"))&&(logreg == 2)){
             //   Intent i = new Intent(MainActivity.this, OneTimeOtp.class);
                mobilenumberforotp = ""+strmobile;
              //  startActivity(i);
                finish();
            } else if ((status.equals("1"))&&(logreg == 1)){

                SharedPreferences.Editor editoruser = sharedpreferencesuser.edit();
                editoruser.putString(Id,id);
                editoruser.putString(UserName, fn);
                editoruser.putString(Phone, phn);
                editoruser.commit();

          //      Intent i = new Intent(MainActivity.this, LocateAddressMain.class);
          //      startActivity(i);
                finish();
            }
            else {
                if(logreg == 1)
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
            }
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
