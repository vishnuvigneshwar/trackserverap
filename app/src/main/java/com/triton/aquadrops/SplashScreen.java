package com.triton.aquadrops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by TritonDev on 8/2/2016.
 */
public class SplashScreen extends Activity {

    private static int SPLASH_TIME=3000;
    Intent intent;
    String mobile,name;

    public static final String MyPREFERENCESUSER = "MyUser" ;
    public static final String UserName = "usernameKey";
    public static final String Phone = "phoneKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferencesuser;

   // String education;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean net = isNetworkAvailable();
        startAnimation();

        sharedpreferencesuser=getSharedPreferences(MyPREFERENCESUSER,Context.MODE_PRIVATE);

        name=sharedpreferencesuser.getString(UserName,null);


        if (net) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                   if (name==null){
                        intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                   }
                   else if (name.equals("")){
                       intent = new Intent(SplashScreen.this, MainActivity.class);
                       startActivity(intent);
                       finish();
                   }
                   else {
                        intent = new Intent(SplashScreen.this, LocateShop.class);
                        startActivity(intent);
                        finish();
                    }
                    }

            }, SPLASH_TIME);

        }
        else {
            AlertDialog dialog=new AlertDialog.Builder(SplashScreen.this).create();
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

    private boolean isNetworkAvailable(){
        ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        return info !=null && info.isConnected();
    }


    private void startAnimation(){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.ll_layout);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView)findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

    }
}

