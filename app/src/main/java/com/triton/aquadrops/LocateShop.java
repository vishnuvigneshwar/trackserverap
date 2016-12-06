package com.triton.aquadrops;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.triton.aquadrops.adapter.ServiceHandler;
import com.triton.aquadrops.util.URL;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocateShop extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private int shop_position = 0;
    ArrayList<HashMap<String,String>> mainlistarray;
    TextView address_panel;
    TextView contacts_panel;
    TextView leftCount;
    TextView rightCount;
    Button next;
    Button previous;
    Button mapviewbutton;
    Button tableviewbutton;
    Button selectedvendor;
    static Double latitude = 0d;
    static Double longitude = 0d;
    Double[] lats = {12.43209092,12.43265946,12.432809,12.43313228,12.43316732,12.43378348,12.43435056,12.43454511,12.4344763,12.43436658,12.43433319,12.43424511,12.4342332,12.43416397,12.43422488,12.43391116,12.43326668,12.43305611,12.43313846,12.43295683,12.43238123,12.43257929};
    Double[] lons = {79.82977692,79.82992619,79.82973629,79.82962796,79.82951399,79.82974984,79.82989843,79.83009365,79.83059972,79.83122674,79.83154638,79.83173285,79.83167602,79.83169041,79.83147594,79.83126275,79.83089561,79.83090652,79.83093838,79.83101425,79.83067335,79.83054456};
    String[] Names = {"GATE 10 main","waiting hall","waiting hall near line","aq07","AQ09","aq13","AQ15","AQ20","AQ24","AQ29","coffey lf","coffee lb","coffee rb","coffee center","coffee rf","rice building near coffee shop","coconut edge","MASM FRONT NEAR RICE","MASM BACK NEAR  RISE","masm center","MASM FRONT NEAR TMPL THATA","THTA GATE NEAR MASM"};

    int cancel = 1;
    int firsttime = 1;
    ListView Addresslist;
    LinearLayout maplayout;
    LinearLayout bottum;
    ProgressDialog progressDialog;
    public static final String MyPREFERENCESUSER = "MyUser" ;
    public static final String UserName = "usernameKey";
    public static final String Phone = "phoneKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferencesuser;
    String Mobile;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_shop);
        TextView logout = (TextView)findViewById(R.id.logout);
        TextView refref = (TextView)findViewById(R.id.refref);
        refref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new LongOperation2().execute();
               /* SmsManager smsManager = SmsManager.getDefault();
                String m = "Locacion-Sync";
                for(int i = 0; i < mainlistarray.size(); i++ )
                smsManager.sendTextMessage(mainlistarray.get(i).get("Contact").toString(), null, ""+m, null, null);
       */


            }
        });
        TextView refref1 = (TextView)findViewById(R.id.refref1);
        refref1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LocateShop.this, adduser.class);
                startActivity(i);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new logout().execute();


                sharedpreferencesuser=getSharedPreferences(MyPREFERENCESUSER, Context.MODE_PRIVATE);
                SharedPreferences.Editor editoruser = sharedpreferencesuser.edit();
                editoruser.putString(Id,"");
                editoruser.putString(UserName,"");
                editoruser.putString(Phone, "");
                editoruser.clear();
                editoruser.commit();
                Intent intentlogout = new Intent(getBaseContext(), MainActivity.class);
                intentlogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentlogout);
                finish();
                Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();

            }
        });
        sharedpreferencesuser=getSharedPreferences(MyPREFERENCESUSER, Context.MODE_PRIVATE);
        Mobile=sharedpreferencesuser.getString(Phone,null);
        name=sharedpreferencesuser.getString(UserName,null);
        address_panel = (TextView)findViewById(R.id.address_panel);
        contacts_panel = (TextView)findViewById(R.id.contacts_panel);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationregister();
        maplayout = (LinearLayout)findViewById(R.id.maplayout);
        bottum = (LinearLayout)findViewById(R.id.bottum);
        Addresslist = (ListView)findViewById(R.id.addresslist);
        previous = (Button)findViewById(R.id.previous);
        next = (Button)findViewById(R.id.next);
        mapviewbutton = (Button)findViewById(R.id.mapviewbutton);
        tableviewbutton = (Button)findViewById(R.id.tableviewbutton);
        selectedvendor = (Button) findViewById(R.id.selectedvendor);


        leftCount = (TextView) findViewById(R.id.leftcount);
        rightCount = (TextView) findViewById(R.id.rightcount);
        leftCount.setText("");
        rightCount.setText("");
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousShop();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextShop();
            }
        });
        //mapviewbutton.setVisibility(View.GONE);
        mapviewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapviewbutton.setTextColor(getResources().getColor(R.color.text_color));
                mapviewbutton.setBackgroundColor(getResources().getColor(R.color.layout_background));
                tableviewbutton.setTextColor(getResources().getColor(R.color.text_color));
                tableviewbutton.setBackgroundColor(getResources().getColor(R.color.PrimaryColor1));
                bottum.setVisibility(View.VISIBLE);
                maplayout.setVisibility(View.VISIBLE);
                Addresslist.setVisibility(View.GONE);
            }
        });
        tableviewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapviewbutton.setTextColor(getResources().getColor(R.color.text_color));
                mapviewbutton.setBackgroundColor(getResources().getColor(R.color.PrimaryColor1));
                tableviewbutton.setTextColor(getResources().getColor(R.color.text_color));
                tableviewbutton.setBackgroundColor(getResources().getColor(R.color.layout_background));
                maplayout.setVisibility(View.GONE);
                Addresslist.setVisibility(View.VISIBLE);
                bottum.setVisibility(View.GONE);

            }
        });
        mapviewbutton.setTextColor(getResources().getColor(R.color.text_color));
        mapviewbutton.setBackgroundColor(getResources().getColor(R.color.PrimaryColor1));
        tableviewbutton.setTextColor(getResources().getColor(R.color.text_color));
        tableviewbutton.setBackgroundColor(getResources().getColor(R.color.layout_background));
        maplayout.setVisibility(View.GONE);
        Addresslist.setVisibility(View.VISIBLE);
        bottum.setVisibility(View.GONE);


        selectedvendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new mainlistasync().execute();
                /*SELECTED_VENDOR = mainlistarray.get(shop_position).get("id");
                Intent i = new Intent(LocateShop.this, MainScreen.class);
                startActivity(i);*/
            }
        });
        locationregister();



        progressDialog=new ProgressDialog(LocateShop.this);
        progressDialog.setMessage("Getting Your Location ");
        progressDialog.setCancelable(true);
        progressDialog.show();

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                new mainlistasync().execute();
                cancel = 2;
            }
        });
        new mainlistasync().execute();
        firsttime = 2;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        locationregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationregister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationregister();
    }

    public void locationregister(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
       // Toast.makeText(getApplicationContext(), "Locatoin request Requested", Toast.LENGTH_SHORT).show();
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 100, 0, this);

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
       // Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_SHORT).show();
        if(firsttime == 1){
        new mainlistasync().execute();
            firsttime = 2;
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        PolylineOptions rectOptions = new PolylineOptions(); // Closes the polyline.

        for(int i = 0; i < lats.length; i++ )
        rectOptions.add(new LatLng(lats[i],lons[i]));

// Get back the mutable Polyline
        Polyline polyline = mMap.addPolyline(rectOptions);

    }

    public class mainlistasync extends AsyncTask<Void,Void,String> {

        String response;
        String error;
        HashMap<String,String> hashMap;
        List<NameValuePair> list=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainlistarray =new ArrayList<HashMap<String, String>>();

            Toast.makeText(getApplicationContext(), "name"+name, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ServiceHandler sh = new ServiceHandler();
            mainlistarray =new ArrayList<HashMap<String, String>>();

            try {

                    list.add(new BasicNameValuePair("latitude",""+latitude));
                    list.add(new BasicNameValuePair("longitude",""+longitude));
                    list.add(new BasicNameValuePair("phone_no",""+Mobile));
                    list.add(new BasicNameValuePair("UserName",""+name));

                    response= sh.makeServiceCall(URL.VENDOR_DETAILS ,ServiceHandler.GET,list);

                    JSONObject jsonObject=new JSONObject(response);
                    Log.i("NOTIFIResponse--->",response);
                    //status=jsonObject.getString("STATUS");
                    JSONArray jsonArray=jsonObject.getJSONArray("olist");
                    for(int i=0;i<jsonArray.length();i++){
                        hashMap=new HashMap<String,String>();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.optString("vendor_id");
                        String Name=jsonObject1.optString("Name");
                        String Address=jsonObject1.optString("Address");
                        String latitude=jsonObject1.optString("latitude");
                        String longitude=jsonObject1.optString("longitude");
                        String Contact=jsonObject1.optString("Contact");
                        String Email=jsonObject1.optString("Email");
                        String Ratings =jsonObject1.optString("Ratings");
                        String rupees =jsonObject1.optString("rupees");
//                        String Cost=jsonObject1.optString("Email");
                        hashMap.put("id",id);
                        hashMap.put("Name",Name);
                        hashMap.put("Address",Address);
                        hashMap.put("latitude",latitude);
                        hashMap.put("longitude",longitude);
                        hashMap.put("Contact",Contact);
                        hashMap.put("Email",Email);
                        hashMap.put("Ratings",Ratings);
                        hashMap.put("rupees",rupees);
                        mainlistarray.add(hashMap);
                    }
            }catch (Exception e){
                error = ""+e;

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            if(cancel == 1)
            progressDialog.dismiss();
            if(mainlistarray.size() > 0){
            shop_position = -1;
            nextShop();
            }
            try {

                Addresslist.setAdapter(new Today());
                firsttime = 2;
                Addresslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       // SELECTED_VENDOR = mainlistarray.get(position).get("id");
                        //Intent i = new Intent(LocateShop.this, MainScreen.class);
                      //  startActivity(i);
                    }//
                });
            }catch (Exception e){

            }
        }
    }

    public void nextShop(){
        if(shop_position < mainlistarray.size()-1){
            shop_position = shop_position + 1;
            leftCount.setText(""+ (0 + shop_position));
            rightCount.setText(""+ (mainlistarray.size() - shop_position - 1));
            Double lat = Double.parseDouble(mainlistarray.get(shop_position).get("latitude").toString());
            Double lon = Double.parseDouble(mainlistarray.get(shop_position).get("longitude").toString());
            String shop_name = ""+mainlistarray.get(shop_position).get("Name").toString();
            String Address = ""+mainlistarray.get(shop_position).get("Address").toString();
            String Contact = ""+mainlistarray.get(shop_position).get("Contact").toString();
            LatLng Location = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(Location).title(shop_name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Location,15));
            address_panel.setText(""+Address);

            Double lat1,lon1;
            Double dist;
            String distancemessage = "";
            try {
                lat1 = Double.parseDouble(mainlistarray.get(shop_position).get("latitude").toString());
                lon1 = Double.parseDouble(mainlistarray.get(shop_position).get("longitude").toString());
                dist = distance(latitude, longitude, lat1, lon1, "K");
                distancemessage = "Shop is at "+new DecimalFormat("##.##").format(dist)+ "Km";

            }catch (Exception e){
                distancemessage = " ";
            }
            contacts_panel.setText(""+Contact);
        }
    }
    public void previousShop(){
        if(shop_position > 0){
            shop_position = shop_position - 1;
            leftCount.setText(""+ (0 + shop_position));
            rightCount.setText(""+ (mainlistarray.size() - shop_position - 1));
            Double lat = Double.parseDouble(mainlistarray.get(shop_position).get("latitude").toString());
            Double lon = Double.parseDouble(mainlistarray.get(shop_position).get("longitude").toString());
            String shop_name = ""+mainlistarray.get(shop_position).get("Name").toString();
            String Address = ""+mainlistarray.get(shop_position).get("Address").toString();
            String Contact = ""+mainlistarray.get(shop_position).get("Contact").toString();
            LatLng Location = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(Location).title(shop_name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Location,15));
            address_panel.setText(""+Address);

            Double lat1,lon1;
            Double dist;
            String distancemessage = "";
            try {
                lat1 = Double.parseDouble(mainlistarray.get(shop_position).get("latitude").toString());
                lon1 = Double.parseDouble(mainlistarray.get(shop_position).get("longitude").toString());
                dist = distance(latitude, longitude, lat1, lon1, "K");
                distancemessage = "Shop is at "+new DecimalFormat("##.##").format(dist)+ "Km";

            }catch (Exception e){
                distancemessage = " ";
            }
            contacts_panel.setText(""+Contact);
        }
    }


    public class Today extends BaseAdapter {


        @Override
        public int getCount() {
            return mainlistarray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater2.inflate(R.layout.adapter_shop_list, null);
            }

            TextView shopname = (TextView) convertView.findViewById(R.id.shopname);
            TextView shopaddress = (TextView) convertView.findViewById(R.id.shopaddress);
            TextView rupees = (TextView) convertView.findViewById(R.id.canprice);
            Button booknow = (Button) convertView.findViewById(R.id.booknow);
            TextView distance = (TextView) convertView.findViewById(R.id.distance);
            Double lat,lon;
            Double dist = 0d;
            Double dist2 = 0d;
            Double dist3 = 0d;
            String near = "";
            String near2 = "";
            String near3 = "";
            String distancemessage = "";
            if(cancel == 2){}else {
try {
    lat = Double.parseDouble(mainlistarray.get(position).get("latitude").toString());
    lon = Double.parseDouble(mainlistarray.get(position).get("longitude").toString());
if((lat != 0)&&(lon != 0))
    for (int i = 0; i < lats.length; i++) {
        if (i == 0) {
            dist = distance(lat, lon, lats[i], lons[i], "K");
        } else {
            if (distance(lat, lon, lats[i], lons[i], "K") < dist) {
                dist3 = dist2;
                dist2 = dist;
                near3 = near2;
                near2 = near;
                dist = distance(lat, lon, lats[i], lons[i], "K");
                near = Names[i];
            }
        }

    }

   // if (dist < 50) {
    if (dist != 0)
    distancemessage = "Person is at " + new DecimalFormat("##.####").format(dist) + "m from" + near;
    if (dist2 != 0)
        distancemessage += " and " + new DecimalFormat("##.####").format(dist2) + "m from" + near2;
    if (dist3 != 0)
        distancemessage += " and " + new DecimalFormat("##.####").format(dist3) + "m from" + near3 + ".";
//}
   /* else
        distancemessage= "Location not set";*/

}catch (Exception e){
    distancemessage = " ";
}}

            shopname.setText(mainlistarray.get(position).get("Name"));
            shopaddress.setText(""+mainlistarray.get(position).get("Ratings"));
           // rupees.setText("Rs "+mainlistarray.get(position).get("rupees")+"");
           // booknow.setText("Book Now");
            final int i = position;
           /* booknow.setVisibility(View.GONE);
            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SELECTED_VENDOR = mainlistarray.get(i).get("id");
                    SELECTED_COST = Integer.parseInt(mainlistarray.get(i).get("rupees")+"");
                    Intent i = new Intent(LocateShop.this, MainScreen.class);
                    startActivity(i);
                }
            });*/
            distance.setText( distancemessage);
            return convertView;

        }


    }

    public double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
//        Double latDistance = Math.toRadians(lat1 - lat2);
//        Double lonDistance = Math.toRadians(lon1 - lon2);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0; //el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
/*
        double theta = lon1 - lon2;
            if((lon1 - lon2)<0){
                theta = lon2 - lon1;
            }
        else
            {
                theta = lon1 - lon2;
            }

        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
*/

        //return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public class logout extends AsyncTask<Void,Void,String> {

        String response;
        String error;
        HashMap<String,String> hashMap;
        List<NameValuePair> list=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainlistarray =new ArrayList<HashMap<String, String>>();

            Toast.makeText(getApplicationContext(), "name"+name, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            ServiceHandler sh = new ServiceHandler();
            mainlistarray =new ArrayList<HashMap<String, String>>();

            try {

                list.add(new BasicNameValuePair("latitude",""+0));
                list.add(new BasicNameValuePair("longitude",""+0));
                list.add(new BasicNameValuePair("phone_no",""+Mobile));
                list.add(new BasicNameValuePair("UserName",""+name));

                response= sh.makeServiceCall(URL.VENDOR_DETAILS ,ServiceHandler.GET,list);

                JSONObject jsonObject=new JSONObject(response);
                Log.i("NOTIFIResponse--->",response);
                //status=jsonObject.getString("STATUS");
                JSONArray jsonArray=jsonObject.getJSONArray("olist");
                for(int i=0;i<jsonArray.length();i++){
                    hashMap=new HashMap<String,String>();
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String id=jsonObject1.optString("vendor_id");
                    String Name=jsonObject1.optString("Name");
                    String Address=jsonObject1.optString("Address");
                    String latitude=jsonObject1.optString("latitude");
                    String longitude=jsonObject1.optString("longitude");
                    String Contact=jsonObject1.optString("Contact");
                    String Email=jsonObject1.optString("Email");
                    String Ratings =jsonObject1.optString("Ratings");
                    String rupees =jsonObject1.optString("rupees");
//                        String Cost=jsonObject1.optString("Email");
                    hashMap.put("id",id);
                    hashMap.put("Name",Name);
                    hashMap.put("Address",Address);
                    hashMap.put("latitude",latitude);
                    hashMap.put("longitude",longitude);
                    hashMap.put("Contact",Contact);
                    hashMap.put("Email",Email);
                    hashMap.put("Ratings",Ratings);
                    hashMap.put("rupees",rupees);
                    mainlistarray.add(hashMap);
                }
            }catch (Exception e){
                error = ""+e;

            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            if(cancel == 1)
                progressDialog.dismiss();
            if(mainlistarray.size() > 0){
                shop_position = -1;
                nextShop();
            }
            try {

                Addresslist.setAdapter(new Today());
                firsttime = 2;
                Addresslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       // SELECTED_VENDOR = mainlistarray.get(position).get("id");
                      //  Intent i = new Intent(LocateShop.this, MainScreen.class);
                      //  startActivity(i);
                    }
                });
            }catch (Exception e){

            }
        }
    }

    class LongOperation2  extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;
        String response, ststus;
        HashMap<String, String> hashMap;
        String Name;


        ServiceHandler sh=new ServiceHandler();

        // TextView uiUpdate = (TextView) findViewById(R.id.eror);

        protected void onPreExecute() {
            // NOTE: You can call UI Element here.

            progressDialog = new ProgressDialog(LocateShop.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            //UI Element
            // uiUpdate.setText("Output : ");
            //  Dialog.setMessage("Downloading source..");
            // Dialog.show();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            try {

                // Call long running operations here (perform background computation)
                // NOTE: Don't call UI Element here.

                // Server url call by GET method
                //response=sh.makeServiceCall("http://whitehousehub.in/demo/mototaxo/MotoUrl/sendtrackeveryonetoken.php", ServiceHandler.GET);
                response=sh.makeServiceCall("http://jbugas.com/url/sendtrackeveryonetoken.php", ServiceHandler.GET);

           /* HttpGet httpget = new HttpGet(URLS.Baseurl+"viewbalocation.php?student_id="+Fid);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            Content = Client.execute(httpget, responseHandler);*/

            } catch (Exception e) {
                //Error = e.getMessage();
                //cancel(true);
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

           // fun(response);
            progressDialog.dismiss();
        }
    }

}
