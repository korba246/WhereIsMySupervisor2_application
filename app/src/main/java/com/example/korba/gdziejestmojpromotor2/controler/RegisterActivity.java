package com.example.korba.gdziejestmojpromotor2.controler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.RegisterBody;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    Spinner Faculty;
    Spinner FieldOfStudy;
    private EditText Name;
    private EditText Surname;
    private EditText Email;
    private EditText Password;
    private EditText RPassword;
    private View ProgressView;
    private View RegcomplitView;
    private View RegView;
    private ResponseBody responseBody;
    private RegisterTask task = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setIcon(R.drawable.user_icon);
        Name =(EditText) findViewById(R.id.Name);
        Surname =(EditText) findViewById(R.id.Surname);
        Email = (EditText) findViewById(R.id.Email);

        Faculty = (Spinner) findViewById(R.id.Faculty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Faculties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Faculty.setAdapter(adapter);

        FieldOfStudy = (Spinner) findViewById(R.id.FOS);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.FOSes, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FieldOfStudy.setAdapter(adapter3);

        Password = (EditText) findViewById(R.id.Password);
        RPassword=(EditText) findViewById(R.id.RPassword);

        Button Register = (Button) findViewById(R.id.BRegister);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckData();
            }
        });

        Button Cancle = (Button) findViewById(R.id.BCancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button Login = (Button) findViewById(R.id.button);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                showView(RegcomplitView,false);
                finish();
            }
        });

        RegView = findViewById(R.id.RegView);
        ProgressView = findViewById(R.id.Register_progress);
        RegcomplitView = findViewById(R.id.Regcomplit);
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
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() { Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void CheckData() {

        // Reset errors.
        Name.setError(null);
        Surname.setError(null);
        Email.setError(null);
        Password.setError(null);
        RPassword.setError(null);

        String name = Name.getText().toString();
        String surname = Surname.getText().toString();
        String email = Email.getText().toString();
        String faculty = Faculty.getSelectedItem().toString();
        String fieldOfStudy = FieldOfStudy.getSelectedItem().toString();
        String password = Password.getText().toString();
        String rpassword = RPassword.getText().toString();

        View focusView = null;

        focusView = isFieldValid(rpassword,RPassword,focusView);
        focusView = isrepasslValid(rpassword,password,focusView);
        focusView = isFieldValid(password,Password,focusView);
        focusView = ispasslValid(password,focusView);
        if(fieldOfStudy.equals("Kierunek")){ focusView = FieldOfStudy;
            Toast.makeText(getApplicationContext(), "Wybierz kierunek", Toast.LENGTH_LONG).show();}
        if(faculty.equals("Wydział")){ focusView = Faculty;Toast.makeText(getApplicationContext(), "Wybierz wydział", Toast.LENGTH_LONG).show();}
        focusView = isFieldValid(email,Email,focusView);
        focusView = isEmailValid(email,focusView);
        focusView = isFieldValid(surname,Surname,focusView);
        focusView = isFieldValid(name,Name,focusView);


        if (focusView!=null) {
            focusView.requestFocus();
        } else {
            showView(ProgressView,true);
            task = new RegisterTask(name,surname,email,password,faculty,"-",fieldOfStudy,"T","Logout");
            task.execute();
        }
    }

    private View isEmailValid(String email,View focusView) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "pwr.edu.pl$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()){
            Email.setError("Musisz użyć adresu PWR");
            focusView = Email;
        }
        return focusView;
    }
    private View ispasslValid(String Pass, View focusView) {

        if(Pass.length()<7) {
            Password.setError("Hasło musi mieć minimum 7 znaków ");
            focusView = Password;
        }
        return focusView;
    }

    private View isrepasslValid(String Rpass,String Pass, View focusView) {

        if(!Rpass.equals(Pass)) {
            RPassword.setError("Hasła różnią się");
            focusView = RPassword;
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

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showView(final View toShow, final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            RegView.setVisibility(show ? View.GONE : View.VISIBLE);
            RegView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    RegView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            toShow.setVisibility(show ? View.VISIBLE : View.GONE);
            toShow.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    toShow.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {

            toShow.setVisibility(show ? View.VISIBLE : View.GONE);
            RegView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mSurname;
        private final String mEmail;
        private final String mPassword;
        private final String mFaculty;
        private final String mDegree;
        private final String mFieldOfStudy;
        private final String mType;
        private final String mStatus;
        int pas;

        RegisterBody registerBody = new RegisterBody();
        RegisterTask(String name,String surname,String email, String password, String faculty, String degree, String fieldOfStudy, String type, String status) {
            mName = name;
            mSurname = surname;
            mEmail = email;
            pas = password.hashCode();
            mPassword = String.valueOf(pas);
            mFaculty = faculty;
            mDegree = degree;
            mFieldOfStudy = fieldOfStudy;
            mType = type;
            mStatus = status;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            registerBody.setName(mName);
            registerBody.setSurname(mSurname);
            registerBody.setEmail(mEmail);
            registerBody.setPassword(mPassword);
            registerBody.setFaculty(mFaculty);
            registerBody.setDegree(mDegree);
            registerBody.setFieldOfStudy(mFieldOfStudy);
            registerBody.setType(mType);
            registerBody.setStatus(mStatus);
            try {
                responseBody = UserService.Register(registerBody);
            } catch (Exception e) {
                return false;
            }
            if(responseBody.getStatus().equals("Registered"))
                return true;
            else return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            task = null;
            showView(ProgressView, false);
            if (success) {
                showView(RegcomplitView, true);
            } else {
                System.out.println(responseBody.getStatus());
                if (responseBody == null) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                } else if (responseBody.getStatus().equals("Exists")) {
                    Toast.makeText(getApplicationContext(), "Użytkownik o takim adresie email już istnieje", Toast.LENGTH_LONG).show();
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
