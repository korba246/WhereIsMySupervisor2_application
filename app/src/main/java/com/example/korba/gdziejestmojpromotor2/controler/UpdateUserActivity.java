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
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

public class UpdateUserActivity extends AppCompatActivity {

    Spinner Faculty;
    Spinner Degree;
    Spinner FieldOfStudy;
    DatabaseHandler db = new DatabaseHandler(this);
    private UpdateUserTask task = null;
    private GetUserDataTask task2 = null;
    private EditText Name;
    private EditText Surname;
    private View ProgressView;
    private View UpdateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.user_set);
        UpdateView = findViewById(R.id.update);
        ProgressView = findViewById(R.id.update_progress);
        Name =(EditText) findViewById(R.id.Name);
        Surname =(EditText) findViewById(R.id.Surname);
        Faculty = (Spinner) findViewById(R.id.Faculty);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Faculties, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Faculty.setAdapter(adapter);

        FieldOfStudy = (Spinner) findViewById(R.id.FOS);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                this, R.array.FOSes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FieldOfStudy.setAdapter(adapter3);

        Button Change = (Button) findViewById(R.id.BChange);
        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });

        Button Cancle = (Button) findViewById(R.id.BCancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateUserActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        GetUserData();
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(UpdateUserActivity.this,SettingsActivity.class);
        startActivity(intent);
        finish();
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            UpdateView.setVisibility(show ? View.GONE : View.VISIBLE);
            UpdateView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    UpdateView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            UpdateView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void UpdateUser(){
        if (task != null) {
            return;
        }
        showProgress(true);
        String name = Name.getText().toString();
        String surname = Surname.getText().toString();
        String email = db.getUser(1).get_email() ;
        String faculty = Faculty.getSelectedItem().toString();
        String degree = Degree.getSelectedItem().toString();
        String fos = FieldOfStudy.getSelectedItem().toString();

        task = new UpdateUserTask(name,surname,email,faculty,degree,fos);
        task.execute((Void) null);
    }

    public void GetUserData(){
        if (task2 != null) {
            return;
        }
        showProgress(true);
        String email = db.getUser(1).get_email();
        System.out.println(email);
        task2 = new GetUserDataTask(email);
        task2.execute((Void) null);
    }

    public class UpdateUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mSurname;
        private final String mEmail;
        private final String mFaculty;
        private final String mDegree;
        private final String mFieldOfStudy;
        ResponseBody responseBody;

        RegisterBody registerBody = new RegisterBody();
        UpdateUserTask(String name,String surname,String email, String faculty, String degree, String fieldOfStudy) {
            mName = name;
            mSurname = surname;
            mEmail = email;
            mFaculty = faculty;
            mDegree = degree;
            mFieldOfStudy = fieldOfStudy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            registerBody.setName(mName);
            registerBody.setSurname(mSurname);
            registerBody.setEmail(mEmail);
            registerBody.setFaculty(mFaculty);
            registerBody.setDegree(mDegree);
            registerBody.setFieldOfStudy(mFieldOfStudy);
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
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Dane zmienione", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UpdateUserActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            } else {
                showProgress(false);
                if (responseBody == null||(responseBody.getStatus().equals("Error"))) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false) ;task = null;
        }
    }

    public class GetUserDataTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        RegisterBody registerBody = new RegisterBody();
        GetUserDataTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                registerBody = UserService.GetUser(mEmail);
            } catch (Exception e) {
                return false;
            }
            if(registerBody==null || registerBody.getName().equals("Error") )
                return false;
            else if(registerBody.getEmail().equals(db.getUser(1).get_email())) return true;
            return false;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            task2 = null;
            if (success) {
                Name.setText(registerBody.getName());
                Surname.setText(registerBody.getSurname());
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        UpdateUserActivity.this, R.array.Faculties, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Faculty.setAdapter(adapter);
                Faculty.setSelection(adapter.getPosition(registerBody.getFaculty()));
                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(
                        UpdateUserActivity.this, R.array.FOSes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                FieldOfStudy.setAdapter(adapter3);
                FieldOfStudy.setSelection(adapter3.getPosition(registerBody.getFieldOfStudy()));
                showProgress(false);
            } else {
                showProgress(false);
                if (registerBody == null) {
                } else if (registerBody.getName().equals("Error")) {
                    Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);task2 = null;
        }
    }
}

