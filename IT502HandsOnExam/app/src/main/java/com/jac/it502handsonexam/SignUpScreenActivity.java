package com.jac.it502handsonexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import java.util.Calendar;
import android.text.InputType;

public class SignUpScreenActivity extends AppCompatActivity {
    private static final String TAG = "Sign Up Screen";
    private Button btn_signup;
    private EditText full_name_txt, birth_date_txt, username_txt, password_txt;
    AppDatabaseHandler dbhandler;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        dbhandler = new AppDatabaseHandler(this);
        btn_signup = findViewById(R.id.btn_signup);
        full_name_txt = findViewById(R.id.full_name_txt);
        birth_date_txt = findViewById(R.id.birth_date_txt);
        birth_date_txt.setInputType(InputType.TYPE_NULL);
        username_txt = findViewById(R.id.username_txt);
        password_txt = findViewById(R.id.password_txt);
        //Show Calendar
        birth_date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUpScreenActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birth_date_txt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = full_name_txt.getText().toString();
                String date = birth_date_txt.getText().toString();
                String un = username_txt.getText().toString();
                String pw = password_txt.getText().toString();
                if(!name.equals("") && !date.equals("") && !un.equals("") && !pw.equals("")) {
                    dbhandler.insertData(name, date, un, pw);
                    toastMessage("Successfully Sign Up.");
                    Intent intent = new Intent(SignUpScreenActivity.this, LoginScreenActivity.class);
                    // set the new task and clear flags
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.d(TAG, "Account Created.");
                }
                else {
                    toastMessage("Please fill in all text fields.");
                }

            }
        });
    }

    public void gotoLoginScreen(View view) {
        Intent intent = new Intent(this, LoginScreenActivity.class);
        // set the new task and clear flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.d(TAG, "Go to Log in Screen");
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
