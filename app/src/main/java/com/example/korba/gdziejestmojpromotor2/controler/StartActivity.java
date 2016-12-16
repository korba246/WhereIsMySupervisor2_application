package com.example.korba.gdziejestmojpromotor2.controler;


import android.content.Intent;
import android.os.AsyncTask;
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
import com.example.korba.gdziejestmojpromotor2.service.RouterService;
import com.example.korba.gdziejestmojpromotor2.service.UserService;


public class StartActivity extends AppCompatActivity {
    private UpdateUserTask task = null;
    private DatabaseHandler db = new DatabaseHandler(this);
    private  RouterService routerService = new RouterService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.home);

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

        System.out.println(routerService.getRouterBSSID(getApplicationContext()));
        routerService.Reconnect(getApplicationContext());
            String ans = routerService.getRouterBSSID(getApplicationContext());
            if(ans.equals("NoConnection")){
                Toast.makeText(StartActivity.this, "Jeśle możesz połącz się z siecią PWR-WiFi", Toast.LENGTH_LONG).show();
            }else if(ans.equals("WiFiDisabled")){
                Toast.makeText(StartActivity.this, "Aby aplikacja działała poprawnie włącz WiFi", Toast.LENGTH_LONG).show();
            }else{

            }
        }
    /*
    @Override
    public void onStop(){
        super.onStop();
        LogoutUser(1);
    }*/


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

