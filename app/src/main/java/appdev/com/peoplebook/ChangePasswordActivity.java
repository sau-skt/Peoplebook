package appdev.com.peoplebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    String username, u;
    EditText newpasswordedittext, retypepasswordedittext;
    TextView changepasswordtextview;
    DatabaseReference usernamedatabasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Widgets();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        changepasswordtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordinDatabase();
            }
        });
    }

    private void ChangePasswordinDatabase() {
        if (newpasswordedittext.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter your new password", Toast.LENGTH_SHORT).show();
        } else if (retypepasswordedittext.getText().toString().equals("")) {
            Toast.makeText(this, "Please retype your password", Toast.LENGTH_SHORT).show();
        } else {
            if (newpasswordedittext.getText().toString().equals(retypepasswordedittext.getText().toString())){
                u = newpasswordedittext.getText().toString();
                usernamedatabasereference.child("password").setValue(u);
                finish();
            } else {
                Toast.makeText(this, "Passwords didn't matched. Please recheck", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Widgets() {
        newpasswordedittext = (EditText) findViewById(R.id.change_password_activity_newpassword);
        retypepasswordedittext = (EditText) findViewById(R.id.change_password_activity_retypepassword);
        changepasswordtextview = (TextView) findViewById(R.id.change_password_activity_changepassword_textview);
    }
}