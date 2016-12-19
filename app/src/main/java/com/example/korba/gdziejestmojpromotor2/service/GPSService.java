package com.example.korba.gdziejestmojpromotor2.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.korba.gdziejestmojpromotor2.controler.StartActivity;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by korba on 16.12.16.
 */

public class GPSService {
/*
    LocationManager lm;
    Criteria kr;
    Location loc;
    String najlepszyDostawca;

    private void odswiez(Context context){
        najlepszyDostawca=lm.getBestProvider(kr, true);
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            loc = lm.getLastKnownLocation(najlepszyDostawca);
        }
    }

    public GPSService(Context context) {

        kr=new Criteria();
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        odswiez(context);
        lm.requestLocationUpdates(najlepszyDostawca, 1000, 1, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        odswiez();
        t1.setText("najlepszy dostawca: "+najlepszyDostawca);
        t2.setText("długość geograficzna: "+loc.getLongitude());
        t3.setText("szerokość geograficzna: "+loc.getLatitude());
        t4.setText(t4.getText()+""+loc.getLongitude()+"/"+loc.getLatitude()+"\n");

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }*/

}

