package com.jac.it502handsonexam;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreenActivity extends AppCompatActivity {
    private static final String TAG = "Login Screen";
    private Button btn_login;
    private EditText username_txt, password_txt;
    AppDatabaseHandler dbhandler;
    Cursor cursor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        dbhandler = new AppDatabaseHandler(this);
        btn_login = findViewById(R.id.btn_login);
        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.password_txt);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username_txt.getText().toString();
                String pw = password_txt.getText().toString();
                if(username_txt.length() != 0 && password_txt.length() != 0) {
                    //Checking if the account is already existing.
                    boolean log = dbhandler.logIn(un, pw);
                    if(log) {
                        // Getting the data of the account logging in the app.
                        cursor = dbhandler.getAccountInformation(un, pw);
                        cursor.moveToFirst();
                        int id = cursor.getInt(0);
                        String full_name = cursor.getString(1);
                        String date = cursor.getString(2);
                        toastMessage("Login");
                        Intent intent = new Intent(LoginScreenActivity.this, ProfileScreenActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("full_name",full_name);
                        intent.putExtra("date", date);
                        intent.putExtra("username", un);
                        intent.putExtra("password", pw);
                        // set the new task and clear flags
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Log.d(TAG, "Logging in.");
                    }
                    else {
                        toastMessage("Account does not exist.");
                    }
                }
                else {
                    toastMessage("Please fill in all text fields.");
                }
            }
        });
    }

    public void gotoSignUpScreen(View view) {
        Intent intent = new Intent(this, SignUpScreenActivity.class);
        // set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.d(TAG, "Go to Sign Up Screen");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
