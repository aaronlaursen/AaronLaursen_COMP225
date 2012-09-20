package edu.macalester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

public class DroidHash extends Activity implements LocationListener, OnClickListener {
    private LocationManager lmgr;
    private TextView locout;
    private TextView curloc;
    private TextView djiav;
    private TextView destloc;
    private String best;
    private Double lat;
    private Double lon;
    private URL djiaurl;
    private String djia;
    private String destLat;
    private String destLon;
    private Button mapbut;
    private TextView status;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        locout = (TextView) findViewById(R.id.locout);
        curloc = (TextView) findViewById(R.id.curloc);
        djiav = (TextView) findViewById(R.id.djiav);
        destloc = (TextView) findViewById(R.id.destloc);
        mapbut = (Button) findViewById(R.id.mapbut);
        //mapbut.setVisibility(0);
        mapbut.setOnClickListener(this);
        status = (TextView) findViewById(R.id.status);
        curloc.setTextColor(Color.BLUE);
        djiav.setTextColor(Color.BLUE);
        destloc.setTextColor(Color.BLUE);
        locout.setTextColor(Color.BLUE);
        locout.setText("Getting coords...");
        lmgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lmgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10*1000, 1000, this);
        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,90*1000, 1000, this);
        Criteria crit=new Criteria();
        best=lmgr.getBestProvider(crit, true);
        if (best == null){
            curloc.setTextColor(Color.RED);
            locout.setTextColor(Color.RED);
            locout.setText("Could not get coords...");
        } else{

            //The next 2 lines hardcode some gps
            lat= 44.1;
            lon= -93.2;
            //FIXME
            //The following 3 lines get the gps location. Unfortunately the emulator doesn't work well...
            //loc = lmgr.getLastKnownLocation(best);
            //lat = loc.getLatitude();
            //lon = loc.getLongitude();


            locout.setText("Getting DJIA...");

            curloc.setTextColor(Color.GREEN);
            curloc.append(lat.toString()+", "+lon.toString());

            Date today = new Date();

            try {
                djiaurl = new URL("http://geo.crox.net/djia/" + Integer.toString(today.getYear() + 1900) + "/" + Integer.toString(today.getMonth()+1) + "/" + Integer.toString(today.getDate()));
///*
                BufferedReader in = new BufferedReader(new InputStreamReader(djiaurl.openStream()));
                djia=in.readLine();
                locout.setText(djia);
                in.close();//*/
                //djia="13540.40";
                djiav.setTextColor(Color.GREEN);
                djiav.append(djia);

            } catch (Exception e) {
                djiav.setTextColor(Color.RED);
                locout.setTextColor(Color.RED);
                locout.setText("Failed to fetch djia...");
                return;
            }
            if (djia != null){
                String prehash = Integer.toString(today.getYear()+1900) + "-" + Integer.toString(today.getMonth()+1) +"-" +Integer.toString(today.getDate())+"-"+djia;
                MessageDigest md;
                try{
                    md = MessageDigest.getInstance("MD5");

                    md.update(prehash.getBytes());
                    byte byteData[] = md.digest();
                    StringBuffer sb[] = new StringBuffer[2];
                    sb[0]=new StringBuffer();
                    sb[1]=new StringBuffer();
                    int i;
                    //locout.setText("meh...");
                    for (i = 0; i < byteData.length/2; i++) {
                        sb[0].append(Integer.toString((byteData[i] & 0xff) + 0x100, 10).substring(1));
                    }

                    for (i = i; i < byteData.length; i++) {
                        sb[1].append(Integer.toString((byteData[i] & 0xff) + 0x100, 10).substring(1));
                    }


                    locout.setText("|"+sb[1].toString()+"|");
                    //locout.setText("guh...");
                    destLat = Integer.toString(lat.intValue())+"."+sb[0];
                    destLon = Integer.toString(lon.intValue())+"."+sb[1];
                    locout.setTextColor(Color.GREEN);
                    locout.setText("Geohash found!");
                    destloc.setTextColor(Color.GREEN);
                    destloc.append(destLat + ", " + destLon);
                    mapbut.setVisibility(1);
                    Double r = Math.abs(lat-Float.parseFloat(destLat))+Math.abs(lon-Float.parseFloat(destLon));
                    if (r>=1){
                        status.setTextColor(Color.RED);
                        status.append("really, really far...");
                    } else if ( 1 > r && r >= 0.00000001){
                        status.append("really far...");
                    } else if (r >= 0.00000000001){
                        status.append("far...");
                    } else if (r >= 0.00000000000001){
                        status.append("getting close");
                    } else if (r >= 0.0000000000000001){
                        status.append("getting warm...");
                    } else if (r >= 0) {
                        status.append("your burning up!");
                    } else {
                        status.setTextColor(Color.GREEN);
                        status.append("YOU'RE HERE!!!!!");
                    }
                } catch (Exception e){
                    destloc.setTextColor(Color.RED);
                    locout.setTextColor(Color.RED);
                    locout.setText("Calculation Failed...");
                }

            }

        }
    }

    public void onLocationChanged(Location location) {
        lmgr.removeUpdates(this);
    }

    public void onProviderEnabled(String Provider){}
    public void onStatusChanged(String provider,int status, Bundle extras){}
    public void onProviderDisabled(String provider){}

    public void onClick(View v){
        switch (v.getId()){
            case R.id.mapbut:
                locout.setTextColor(Color.BLUE);
                locout.setText("Opening Browser...");
                String url = "http://maps.google.com/maps?q="+lat.toString()+",+"+lon.toString()+"+to+"+destLat+",+"+destLon;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
        }

    }

}
