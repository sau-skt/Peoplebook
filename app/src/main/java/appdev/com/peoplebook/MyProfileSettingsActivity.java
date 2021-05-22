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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Long.parseLong;

public class MyProfileSettingsActivity extends AppCompatActivity {
    DatePickerDialog picker;
    EditText currentdateofbirth, currentname, currentphone, currentaboutme, currenthobbies;
    CircleImageView currentprofileimage;
    String username;
    DatabaseReference usernamedatabasereference;
    TextView saveTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_settings);

        Widgets();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        InputUserDetails();

        currentdateofbirth.setInputType(InputType.TYPE_NULL);
        currentdateofbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCalender();
            }
        });

        saveTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUserDetails();
            }
        });

        currentprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileImageChangeActivity();
            }
        });
    }

    private void openProfileImageChangeActivity() {
        Intent intent = new Intent(this, ProfileImageChangeActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }

    private void ChangeUserDetails() {
        String name = currentname.getText().toString();
        usernamedatabasereference.child("name").setValue(name);
        long phone = parseLong(currentphone.getText().toString());
        usernamedatabasereference.child("phone").setValue(phone);
        String aboutme = currentaboutme.getText().toString();
        usernamedatabasereference.child("aboutme").setValue(aboutme);
        String hobbies = currenthobbies.getText().toString();
        usernamedatabasereference.child("hobbies").setValue(hobbies);
        String dateofbirth = currentdateofbirth.getText().toString();
        usernamedatabasereference.child("dateofbirth").setValue(dateofbirth);
        finish();
    }

    private void InputUserDetails() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                currentname.setText(name);
                String phone = snapshot.child("phone").getValue().toString();
                currentphone.setText(phone);
                String aboutme = snapshot.child("aboutme").getValue().toString();
                currentaboutme.setText(aboutme);
                String hobbies = snapshot.child("hobbies").getValue().toString();
                currenthobbies.setText(hobbies);
                String dateofbirth = snapshot.child("dateofbirth").getValue().toString();
                currentdateofbirth.setText(dateofbirth);
                String profileimageurl = snapshot.child("profileimage").getValue().toString();
                Picasso.get().load(profileimageurl).into(currentprofileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        currentdateofbirth = (EditText) findViewById(R.id.myprofilesettings_activity_dateofbirth_textview);
        currentname = (EditText) findViewById(R.id.myprofilesettings_activity_name_textview);
        currentphone = (EditText) findViewById(R.id.myprofilesettings_activity_phone_textview);
        currentaboutme = (EditText) findViewById(R.id.myprofilesettings_activity_aboutme_textview);
        currenthobbies = (EditText) findViewById(R.id.myprofilesettings_activity_hobbies_textview);
        currentprofileimage = (CircleImageView) findViewById(R.id.myprofilesettings_activity_profileimage_imageview);
        saveTextview = (TextView) findViewById(R.id.myprofilesettings_activity_save_textview);
    }

    private void OpenCalender() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(MyProfileSettingsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        currentdateofbirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }
}