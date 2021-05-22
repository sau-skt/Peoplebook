package appdev.com.peoplebook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import appdev.com.peoplebook.Models.RegisterActivityModel;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class RegisterActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText dateofbirth, name, username, password, phone, aboutme, hobbies;
    TextView nexttextview;
    DatabaseReference usernamedatabasereference;
    int matchusername = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Widgets();

        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");
        dateofbirth.setInputType(InputType.TYPE_NULL);
        dateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender();
            }
        });

        nexttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });

    }

    private void Validate() {
        if (name.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (username.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        } else if (phone.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (aboutme.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter something about you", Toast.LENGTH_SHORT).show();
        } else if (hobbies.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your hobbies", Toast.LENGTH_SHORT).show();
        } else if (dateofbirth.getText().toString().equals("")) {
            Toast.makeText(this, "Select your date of birth", Toast.LENGTH_SHORT).show();
        } else {
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
            String str = username.getText().toString();
            Matcher matcher = pattern.matcher(str);
            if (!matcher.matches()) {
                Toast.makeText(this, "Username cannot contain special characters", Toast.LENGTH_SHORT).show();
            } else {
                CheckforUniqueUsername();
            }
        }
    }

    private void CheckforUniqueUsername() {
        usernamedatabasereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegisterActivityModel registerActivityModel = snapshot.getValue(RegisterActivityModel.class);
                    String usedusernames = registerActivityModel.getUsername();
                    if (username.getText().toString().equals(usedusernames)) {
                        matchusername = 1;
                    }
                }
                if (matchusername==1){
                    Toast.makeText(RegisterActivity.this, "Username is unavailable", Toast.LENGTH_SHORT).show();
                    matchusername = 0;
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Congratulations! username is available", Toast.LENGTH_SHORT).show();
                    OpenUploadProfileImageActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void OpenUploadProfileImageActivity() {
        Intent intent = new Intent(this, UploadProfileImageActivity.class);
        intent.putExtra("username",username.getText().toString());
        intent.putExtra("name",name.getText().toString());
        intent.putExtra("password",password.getText().toString());
        intent.putExtra("phone",phone.getText().toString());
        intent.putExtra("aboutme",aboutme.getText().toString());
        intent.putExtra("hobbies",hobbies.getText().toString());
        intent.putExtra("dateofbirth",dateofbirth.getText().toString());
        startActivity(intent);
        finish();
    }

    private void OpenCalender() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(RegisterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateofbirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private void Widgets() {
        dateofbirth = (EditText) findViewById(R.id.register_activity_dateofbirth_edittext);
        nexttextview = (TextView) findViewById(R.id.activity_register_next_textview);
        name = (EditText) findViewById(R.id.activity_register_name_edittext);
        username = (EditText) findViewById(R.id.activity_register_username_edittext);
        password = (EditText) findViewById(R.id.activity_register_password_edittext);
        phone = (EditText) findViewById(R.id.activity_register_phone_edittext);
        aboutme = (EditText) findViewById(R.id.activity_register_aboutme_edittext);
        hobbies = (EditText) findViewById(R.id.activity_register_hobbies_edittext);
    }
}