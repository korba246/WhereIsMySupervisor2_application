package com.example.korba.gdziejestmojpromotor2.controler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.model.Users;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

public class Change_PasswordActivity extends AppCompatActivity {

    DatabaseHandler db = new DatabaseHandler(this);
    private EditText OldPassword;
    private EditText NewPassword;
    private EditText RNewPassword;
    private ChangePasswordTask task = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.password);

        Button OK = (Button) findViewById(R.id.ok);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });

        Button Cancle = (Button) findViewById(R.id.cancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Change_PasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_help:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void ChangePassword(){

        if (task != null) {
            return;
        }

        OldPassword = (EditText) findViewById(R.id.old_password);
        NewPassword = (EditText) findViewById(R.id.new_password);
        RNewPassword = (EditText) findViewById(R.id.new_password2);
        String email = db.getUser(1).get_email();

        OldPassword.setError(null);
        NewPassword.setError(null);
        RNewPassword.setError(null);

        String oldPassword = OldPassword.getText().toString();
        String newPassword = NewPassword.getText().toString();
        String rnewPassword = RNewPassword.getText().toString();

        View focusView = null;

        focusView = isFieldValid(rnewPassword,RNewPassword,focusView);
        focusView = isrepasslValid(rnewPassword,newPassword,focusView);
        focusView = isFieldValid(newPassword,NewPassword,focusView);
        focusView = ispasslValid(newPassword,focusView);
        focusView = isFieldValid(oldPassword,OldPassword,focusView);
        focusView = isOldpasslValid(oldPassword,focusView);

        if (focusView!=null) {
            focusView.requestFocus();
        } else {
            task = new ChangePasswordTask(email, newPassword);
            task.execute();
        }

    }

    private View isOldpasslValid(String Pass, View focusView) {
        if(!Pass.equals(db.getUser(1).get_password())) {
            OldPassword.setError("Stare hasło nieprawidłowe");
            focusView = OldPassword;
        }
        return focusView;
    }

    private View ispasslValid(String Pass, View focusView) {

        if(Pass.length()<7) {
            NewPassword.setError("Hasło musi mieć minimum 7 znaków");
            focusView = NewPassword;
        }
        return focusView;
    }

    private View isrepasslValid(String Rpass,String Pass, View focusView) {

        if(!Rpass.equals(Pass)) {
            RNewPassword.setError("Hasła różnią się");
            focusView = RNewPassword;
        }
        return focusView;
    }
    private View isFieldValid(String Field,EditText field,View focusView){
        if (TextUtils.isEmpty(Field)) {
            field.setError("Pole nie może być puste");
            focusView = field;
        }
        return focusView;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Change_PasswordActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public class ChangePasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        RegisterBody Body = new RegisterBody();
        ResponseBody responseBody = new ResponseBody();
        int pas;

        ChangePasswordTask(String email, String password) {
            mEmail = email;
            pas = password.hashCode();
            mPassword = String.valueOf(pas);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Body.setEmail(mEmail);
            Body.setPassword(mPassword);
            try {
                responseBody = UserService.UpdateUser(Body);
            } catch (Exception e) {
                return false;
            }
            if(responseBody.getStatus().equals("Updated"))
                return true;
            else return false;
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(Change_PasswordActivity.this);
            progressDialog.setMessage("Przetwarzanie...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progressDialog.dismiss();
            task = null;
            if (success) {
                Toast.makeText(getApplicationContext(), "Hasło zmienione", Toast.LENGTH_LONG).show();
                Users user = new Users();
                user.set_id(1);
                user.set_email(mEmail);
                user.set_password(mPassword);
                db.updateUser(user);
                Intent intent = new Intent(Change_PasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();

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
}

