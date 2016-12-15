package com.example.korba.gdziejestmojpromotor2.controler;

import android.content.Intent;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
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
import com.example.korba.gdziejestmojpromotor2.model.LoginBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.model.Users;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.LocalizationService;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class LoginActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    private EditText Email;
    private EditText Password;
    private View ProgressView;
    private View LoginView;
    private ResponseBody responseBody;
    private LoginTask task = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.login_icon);
        getSupportActionBar().setTitle("Zaloguj");
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);

        Button Login = (Button) findViewById(R.id.Blogin);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LogInActivity.this, StartActivity.class);
                //startActivity(intent);
                CheckData();
            }
        });

        Button Register = (Button) findViewById(R.id.Bregister);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        LoginView = findViewById(R.id.loginfield);
        ProgressView = findViewById(R.id.login_progress);
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
                intent = new Intent(LoginActivity.this,HelpActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() { }

    private void CheckData() {

        if (task != null) {
            return;
        }
        Email.setError(null);
        Password.setError(null);


        String email = Email.getText().toString();
        String password = Password.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(password)) {
            Password.setError("Pole hasło nie może być puste");
            focusView = Password;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            Email.setError("Pole email nie może być puste");
            focusView = Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Email.setError("Musisz użyć adresu studenckiego PWR");
            focusView = Email;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            task = new LoginTask(email, password);
            task.execute((Void) null);

        }
    }

    private boolean isEmailValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "pwr.edu.pl$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            LoginView.setVisibility(show ? View.GONE : View.VISIBLE);
            LoginView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    LoginView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            ProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            LoginView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        LoginBody loginBody = new LoginBody();
        int pas;

        LoginTask(String email, String password) {
            mEmail = email;
            pas = password.hashCode();
            mPassword = String.valueOf(pas);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            loginBody.setEmail(mEmail);
            loginBody.setPassword(mPassword);
            try {
                responseBody = UserService.Login(loginBody);
            } catch (Exception e) {
                return false;
            }
            if(responseBody.getStatus().equals("Logout"))
                return true;
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            showProgress(false);
            if (success) {
                Users user = new Users();
                user.set_id(1);
                user.set_email(mEmail);
                user.set_password(mPassword);
                if(db.getUsersCount()>0){
                    db.updateUser(user);
                }else db.addUser(user);
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                   startActivity(intent);
                    finish();
            } else {
                if (responseBody == null) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }else if (responseBody.getStatus().equals("Logged")) {
                    Toast.makeText(getApplicationContext(), "Użytkownik zalogowany na innym urządzeniu", Toast.LENGTH_LONG).show();
                } else if (responseBody.getStatus().equals("NotFound")) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowy adres email", Toast.LENGTH_LONG).show();

                } else if (responseBody.getStatus().equals("WrongPassword")) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowe hasło", Toast.LENGTH_LONG).show();
                } else if (responseBody.getStatus().equals("Error")) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
            showProgress(false);
        }
    }
}
