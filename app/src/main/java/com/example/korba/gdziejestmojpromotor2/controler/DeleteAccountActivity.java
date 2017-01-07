package com.example.korba.gdziejestmojpromotor2.controler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.korba.gdziejestmojpromotor2.R;
import com.example.korba.gdziejestmojpromotor2.model.ResponseBody;
import com.example.korba.gdziejestmojpromotor2.model.Users;
import com.example.korba.gdziejestmojpromotor2.service.DatabaseHandler;
import com.example.korba.gdziejestmojpromotor2.service.UserService;

public class DeleteAccountActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    DatabaseHandler db = new DatabaseHandler(this);
    private DeleteUserTask task = null;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);
        getSupportActionBar().setIcon(R.drawable.delete_set);

        Button Cancle = (Button) findViewById(R.id.cancle);
        Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeleteAccountActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button Delete = (Button) findViewById(R.id.delete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteUser();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(DeleteAccountActivity.this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void DeleteUser() {

        if (task != null) {
            return;
        }
        View focusView = null;
        password = (EditText) findViewById(R.id.password);
        String Password = password.getText().toString();
        Users user = db.getUser(1);
        if(password.equals(user.get_password())){
            focusView = password;
        }
        if (focusView!=null) {
            focusView.requestFocus();
        } else {
            task = new DeleteUserTask(user.get_email());
            task.execute((Void) null);
        }
    }

    public class DeleteUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        ResponseBody response;
        DeleteUserTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                response = UserService.DeleteUser(mEmail);
            } catch (Exception e) {
                return false;
            }
            if ((response.getStatus().equals("Error")) || response == null){
                return false;
            }

            else {
                return true;
            }
        }

        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(DeleteAccountActivity.this);
            progressDialog.setMessage("Usuwanie..");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            progressDialog.dismiss();
            task = null;
            if (success) {
                Users user = db.getUser(1);
                db.deleteUser(user);
                Toast.makeText(getApplicationContext(), "Konto usunięte", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DeleteAccountActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Brak połączenia z serwerem", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            progressDialog.dismiss();
            task = null;
        }
    }
}
