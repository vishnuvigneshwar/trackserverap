package com.triton.aquadrops.util;

/**
 * Created by PHP2 on 10/25/2016.
 */


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.triton.aquadrops.util.URL.caddress1;
import static com.triton.aquadrops.util.URL.caddress2;
import static com.triton.aquadrops.util.URL.carea;
import static com.triton.aquadrops.util.URL.cdoorno;
import static com.triton.aquadrops.util.URL.clat;
import static com.triton.aquadrops.util.URL.clon;
import static com.triton.aquadrops.util.URL.cpin;
import static com.triton.aquadrops.util.URL.nploc;
import static com.triton.aquadrops.util.URL.setcdata;

public class LocationAddress {
    private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        if(address.getMaxAddressLineIndex() >=2){
                        cdoorno = address.getAddressLine(0);
                        caddress1 = address.getAddressLine(1);
                        for (int i = 2; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                            caddress2 = sb.toString() + " , " +address.getLocality();
                            cpin = address.getPostalCode();
                            carea = address.getCountryName();
                        } else if(address.getMaxAddressLineIndex() >=1){
                            cdoorno = address.getAddressLine(0);
                            caddress1 = address.getAddressLine(1);
                            for (int i = 2; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }
                            caddress2 = sb.toString();
                            carea = address.getLocality() +", "+address.getCountryName();
                            cpin = address.getPostalCode();

                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                        setcdata = 2;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                        /*clat  = Float.parseFloat(latitude+"");
                        clon  = Float.parseFloat(longitude+"");*/

                        nploc =2;
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}