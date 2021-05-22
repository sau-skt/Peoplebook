package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class KnowMoreAboutMeActivity extends AppCompatActivity {
    String username;
    DatabaseReference usernamedatabasereference;
    TextView nametv, usernametv, hobbiestv, aboutmetv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more_about_me);

        Widgets();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);

        SetDetails();
    }

    private void SetDetails() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                String hobbies = snapshot.child("hobbies").getValue().toString();
                String aboutme = snapshot.child("aboutme").getValue().toString();
                nametv.setText(name);
                usernametv.setText(username);
                hobbiestv.setText(hobbies);
                aboutmetv.setText(aboutme);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        nametv = (TextView) findViewById(R.id.knowmorename);
        usernametv = (TextView) findViewById(R.id.knowmoreusername);
        hobbiestv = (TextView) findViewById(R.id.knowmorehobbies);
        aboutmetv = (TextView) findViewById(R.id.knowmoreaboutme);
    }
}