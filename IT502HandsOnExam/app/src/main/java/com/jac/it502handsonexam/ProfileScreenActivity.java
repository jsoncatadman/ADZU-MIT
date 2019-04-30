package com.jac.it502handsonexam;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

public class ProfileScreenActivity extends AppCompatActivity {
    private static final String TAG = "Profile Screen";
    private int selectedID;
    private Button btn_save_changes, btn_log_out;
    private String fname, un, pw, date;
    private EditText full_name_txt, birth_date_txt, username_txt, password_txt;
    AppDatabaseHandler dbhandler;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        dbhandler = new AppDatabaseHandler(this);
        //CREATE GUI (id's are from the xml files)
        btn_log_out = findViewById(R.id.btn_log_out);
        btn_save_changes = findViewById(R.id.btn_save_changes);
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
                picker = new DatePickerDialog(ProfileScreenActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birth_date_txt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        //GET DATA from the Previous Screen
        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        fname = receivedIntent.getStringExtra("full_name");
        date = receivedIntent.getStringExtra("date");
        un = receivedIntent.getStringExtra("username");
        pw = receivedIntent.getStringExtra("password");
        // SET DATA to the EditText
        full_name_txt.setText(fname);
        birth_date_txt.setText(date);
        username_txt.setText(un);
        password_txt.setText(pw);
        //Button Actions (Click or Tap)
        btn_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = full_name_txt.getText().toString();
                String date = birth_date_txt.getText().toString();
                String un = username_txt.getText().toString();
                String pw = password_txt.getText().toString();
                if(!name.equals("") && !date.equals("") && !un.equals("") && !pw.equals("")) {
                    dbhandler.updateData(name, date, un, pw, selectedID);
                    toastMessage("Successfully Updated.");
                }
                else {
                    toastMessage("Please fill in all text fields.");
                }
            }
        });
        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = username_txt.getText().toString();
                String pw = password_txt.getText().toString();
                if(!un.equals("") && !pw.equals("")) {
                    boolean log = dbhandler.logOut(un, pw);
                    if(log) {
                        toastMessage("Log out");
                        Intent intent = new Intent(ProfileScreenActivity.this, LoginScreenActivity.class);
                        // set the new task and clear flags
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Log.d(TAG, "Account Logging out.");
                    }
                }
                else {
                    toastMessage("Please fill in all text fields.");
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
