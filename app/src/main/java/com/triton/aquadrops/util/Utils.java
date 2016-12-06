package com.triton.aquadrops.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.regex.Pattern;

/**
 * Created by Triton-PC on 01-09-2016.
 */
public class Utils {

    AlertDialog dialog;

    public String getUdId(Context context) {
        // This will get the Real Device ID (IMEI NO)
        TelephonyManager TelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String strDeviceId = TelephonyMgr.getDeviceId(); // Requires

        return strDeviceId;

    }

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // boitealerte(this.getString(R.string.alert),
            // "getSystemService rend null");

        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Validating the Email
    public boolean isValidEmail(String strEmail) {
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                        + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

        if (EMAIL_ADDRESS_PATTERN.matcher(strEmail).matches()) {

            return true;
        } else {

            return false;
        }

    }

    public boolean isLocationAvailable(Context context) {

        if (ConstantFiles.LATITUDE > 0 && ConstantFiles.LONGITUDE > 0) {

            return true;

        } else {
            return false;
        }
    }

    public void NetFailAlert(Context ncontext){
        dialog=new AlertDialog.Builder(ncontext).create();
        dialog.setTitle("Internet Connection");
        dialog.setMessage("Now Internet connection is not available.Check your Network");
//        dialog.setIcon(R.drawable.fail);
        dialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void NetFailAlertDismiss(Context ncontext){
        dialog.dismiss();
    }


}
