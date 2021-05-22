package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static java.lang.Long.parseLong;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText usernameedittext, phoneedittext, dateofbirthedittext;
    DatabaseReference usernamedatabasereference;
    DatePickerDialog picker;
    TextView verifytextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Widgets();
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");
        verifytextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });
        dateofbirthedittext.setInputType(InputType.TYPE_NULL);
        dateofbirthedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender();
            }
        });
    }

    private void OpenCalender() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(ForgotPasswordActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateofbirthedittext.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private void Validate() {
        if (usernameedittext.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (phoneedittext.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your registered phone number", Toast.LENGTH_SHORT).show();
        } else if (dateofbirthedittext.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
        } else {
            Checkdetails();
        }
    }

    private void Checkdetails() {
        usernamedatabasereference.child(usernameedittext.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                String phone = snapshot.child("phone").getValue().toString();
                String dateofbirth = snapshot.child("dateofbirth").getValue().toString();
                if (username.equals(usernameedittext.getText().toString()) && phone.equals(phoneedittext.getText().toString()) && dateofbirth.equals(dateofbirthedittext.getText().toString())) {
                    OpenChangePasswordActivity();
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this, "Please recheck your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void OpenChangePasswordActivity() {
        Intent intent = new Intent(this,ChangePasswordActivity.class);
        intent.putExtra("username",usernameedittext.getText().toString());
        startActivity(intent);
        finish();
    }

    private void Widgets() {
        usernameedittext = (EditText) findViewById(R.id.forgot_password_activity_username_edittext);
        phoneedittext = (EditText) findViewById(R.id.forgot_password_phone_edittext);
        dateofbirthedittext = (EditText) findViewById(R.id.forgot_password_dateofbirth_deittext);
        verifytextview = (TextView) findViewById(R.id.forgot_password_verify_textview);
    }
}