package com.example.korba.gdziejestmojpromotor2.controler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.UserService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class StartActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private UpdateUserTask task = null;
    private DatabaseHandler db = new DatabaseHandler(this);
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.home);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        ImageButton Help = (ImageButton) findViewById(R.id.help);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton Settings = (ImageButton) findViewById(R.id.settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton Logout = (ImageButton) findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUser(0);
            }
        });

        ImageButton Close = (ImageButton) findViewById(R.id.close);
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutUser(1);
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(getApplicationContext() , "Problemy z połączeniem GPS" , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        System.out.print("SSSSSSSSSSSDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        Toast.makeText(StartActivity.this, "localization", Toast.LENGTH_LONG).show();
    }
/*
    @Override
    public void onStop(){
        super.onStop();
        LogoutUser(1);
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void CloseApplication(){
        this.finish();
    }

    public void LogoutUser(int type) {
        if (task != null) {
            return;
        }
        String email = db.getUser(1).get_email();
        task = new UpdateUserTask(email, "Logout", type);
        task.execute((Void) null);
    }

    public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mStatus;
        private final String mEmail;
        private final int mType;
        ResponseBody responseBody;

        RegisterBody registerBody = new RegisterBody();
        UpdateUserTask(String email, String status, int type) {
            mStatus = status;
            mEmail = email;
            mType = type;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            registerBody.setStatus(mStatus);
            registerBody.setEmail(mEmail);
            try {

                responseBody = UserService.UpdateUser(registerBody);
            } catch (Exception e) {
                return false;
            }
            if(responseBody.getStatus().equals("Updated"))
                return true;
            else return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            if (success) {
                if(mType==0) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    CloseApplication();
                }
            } else {
                if (responseBody == null||(responseBody.getStatus().equals("Error"))) {
                    Toast.makeText(StartActivity.this, "Żeby sie wylogować musisz być podłączony do sieci", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }
}

