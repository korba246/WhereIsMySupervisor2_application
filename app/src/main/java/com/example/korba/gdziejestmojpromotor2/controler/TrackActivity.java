package com.example.korba.gdziejestmojpromotor2.controler;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.LecturersLocalization;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.model.Router;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.LocalizationService;
import com.example.korba.gdziejestmojpromotor2.service.RouterService;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TrackActivity extends AppCompatActivity implements LocationListener {

    private long time = 10000;
    private float min =5;
    private LocationManager lm;
    private Criteria criteria;
    private Location location;
    private String bestProvider;
    private String x;
    private String y;
    private String building;
    private String floor;
    private UpdateLocalizationTask task = null;
    private GetRouterDataTask task2 = null;
    private UpdateUserTask task3 = null;
    private DatabaseHandler db = new DatabaseHandler(this);
    private RouterService routerService = new RouterService();

    private boolean refresh(){
        bestProvider=lm.getBestProvider(criteria, true);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(bestProvider);
            if(location==null){
                bestProvider = "network";
                location = lm.getLastKnownLocation(bestProvider);
            }
            if(location == null)return false;
            return true;
        }else return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        getSupportActionBar().setTitle("Śledzenie");
        criteria=new Criteria();
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        if(refresh()) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(bestProvider, time, min, this);
                y = String.valueOf(location.getLongitude());
                x = String.valueOf(location.getLatitude());
                GetRouter();
            }
        }else Toast.makeText(getApplicationContext(), "W celu poprawnego działania aplikacji uruchom Lokalizacje GPS", Toast.LENGTH_LONG).show();

        ImageButton Help = (ImageButton) findViewById(R.id.help);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackActivity.this,HelpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton Settings = (ImageButton) findViewById(R.id.settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrackActivity.this,SettingsActivity.class);
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

        ImageButton StopTracking = (ImageButton) findViewById(R.id.StopTracking);
        StopTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopGPS();
                Intent intent = new Intent(TrackActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void stopGPS()
    {

        try
        {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                lm.removeUpdates(this);
            }
            lm=null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(TrackActivity.this,StartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        if(refresh()) {
            y = String.valueOf(location.getLongitude());
            x = String.valueOf(location.getLatitude());
        }else Toast.makeText(getApplicationContext(), "W celu poprawnego działania aplikacji uruchom Lokalizacje GPS", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), x+"  "+ y, Toast.LENGTH_LONG).show();
        GetRouter();
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "W celu poprawnego działania aplikacji uruchom Lokalizacje GPS", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void UpdateLocalization(){
        if (task != null) {
            return;
        }
        String email = db.getUser(1).get_email();
        task = new UpdateLocalizationTask(email);
        task.execute((Void) null);
    }

    public class UpdateLocalizationTask extends AsyncTask<Void, Void, Boolean> {

        private ResponseBody responseBody;
        private final String mEmail;
        private LecturersLocalization lecturersLocalization = new LecturersLocalization();
        private LocalizationService localizationService = new LocalizationService();

        UpdateLocalizationTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            DateFormat dateFormat2 = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            lecturersLocalization.setEmail(mEmail);
            lecturersLocalization.setBuilding(building);
            lecturersLocalization.setFloor(floor);
            lecturersLocalization.setX(x);
            lecturersLocalization.setY(y);
            lecturersLocalization.setDate(dateFormat.format(date));
            lecturersLocalization.setTime(dateFormat2.format(date));
            try {
                responseBody = localizationService.UpdateLocalization(lecturersLocalization);
            } catch (Exception e) {
                return false;
            }if(responseBody == null)
                return false;
            if(responseBody.getStatus().equals("Updated"))
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            if (success) {
            } else {
                if (responseBody == null) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                } else if (responseBody.getStatus().equals("Error")) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }
    }


    public void GetRouter() {
        routerService.Reconnect(getApplicationContext());
            String ans = routerService.getRouterBSSID(getApplicationContext());
            if(ans.equals("NoConnection")){
                Toast.makeText(TrackActivity.this, "Jeśle możesz połącz się z siecią PWR-WiFi", Toast.LENGTH_LONG).show();
            }else if(ans.equals("WiFiDisabled")){
                Toast.makeText(TrackActivity.this, "Aby aplikacja działała poprawnie włącz WiFi", Toast.LENGTH_LONG).show();
            }else {
                if (task2 != null) {
                    return;
                }
                task2 = new GetRouterDataTask();
                task2.execute((Void) null);
            }
    }

    public class GetRouterDataTask extends AsyncTask<Void, Void, Boolean> {

        private final String mAddres;
        Router router;
        String addres = routerService.getRouterBSSID(getApplicationContext());
        GetRouterDataTask() {
            mAddres = addres;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                router = RouterService.GetRouter(mAddres);
            } catch (Exception e) {
                return false;
            }
            if(router!=null)
                return true;
            else return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            task2 = null;
            if (success) {
                building = router.getBuilding();
                floor = router.getFloor();
                UpdateLocalization();

            } else {
                building = "-";
                floor = "-";
                UpdateLocalization();
                Toast.makeText(TrackActivity.this, "Brak routera o tym adresie w bazie", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            task2 = null;
        }
    }

    public void LogoutUser(int type) {
        if (task3 != null) {
            return;
        }
        String email = db.getUser(1).get_email();
        task3 = new UpdateUserTask(email, "Logout", type);
        task3.execute((Void) null);
    }

    public void CloseApplication(){
        this.finishAffinity();
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
            task3 = null;
            if (success) {
                if(mType==0) {
                    Intent intent = new Intent(TrackActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    CloseApplication();
                }
            } else {
                if (responseBody == null||(responseBody.getStatus().equals("Error"))) {
                    Toast.makeText(TrackActivity.this, "Żeby sie wylogować musisz być podłączony do sieci", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task3 = null;
        }
    }
}
