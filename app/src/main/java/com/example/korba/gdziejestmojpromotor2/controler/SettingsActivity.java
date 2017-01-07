package com.example.korba.gdziejestmojpromotor2.controler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHandler db = new DatabaseHandler(this);
    private ProgressDialog progressDialog;
    private UpdateUserTask task2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setIcon(R.drawable.settings_icon);
        Button ChangePassword = (Button) findViewById(R.id.password);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, Change_PasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button DeleteAccount = (Button) findViewById(R.id.delete);
        DeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DeleteAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button UpdateUser = (Button) findViewById(R.id.update);
        UpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UpdateUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.action_logout:
                LogoutUser(0);
                return true;

            case R.id.action_close:
                LogoutUser(1);
                return true;

            case R.id.action_home:
                intent = new Intent(SettingsActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void CloseApplication(){
        this.finishAffinity();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(SettingsActivity.this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    public void LogoutUser(int type) {
        if (task2 != null) {
            return;
        }
        String email = db.getUser(1).get_email();
        task2 = new UpdateUserTask(email, "Logout", type);
        task2.execute((Void) null);
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
            registerBody.setEmail(mEmail);;
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
            task2 = null;
            if (success) {
                if(mType==0) {
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    CloseApplication();
                }
            } else {
                if (responseBody == null||(responseBody.getStatus().equals("Error"))) {
                    Toast.makeText(SettingsActivity.this, "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            ;task2 = null;
        }
    }
}
