package appdev.com.peoplebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import appdev.com.peoplebook.Models.RegisterActivityModel;

public class LoginActivity extends AppCompatActivity {
    //Variables for Widgets
    TextView register, LoginBtn, ForgotPassword;
    EditText username, password;
    DatabaseReference usernamedatabasereference;
    int matchusername = 0;
    String currentpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Widgets();

        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsernameandPassword();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });

        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenForgotPasswordActivity();
            }
        });
    }

    private void OpenForgotPasswordActivity() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void checkUsernameandPassword() {
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
                if (matchusername == 1) {
                    usernamedatabasereference.child(username.getText().toString()).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            currentpassword = snapshot.getValue().toString();
                            if (password.getText().toString().equals(currentpassword)) {
                                openHomePageActivity();
                            } else {
                                Toast.makeText(LoginActivity.this, "Please re-enter your password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    matchusername = 0;
                } else {
                    Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Widgets() {
        LoginBtn = (TextView) findViewById(R.id.login_activity_login_button);
        register = (TextView) findViewById(R.id.login_activity_create_account_textview);
        username = (EditText) findViewById(R.id.login_activity_username_edittext);
        password = (EditText) findViewById(R.id.login_activity_password_edittext);
        ForgotPassword = (TextView) findViewById(R.id.login_activity_forgot_password_textview);
    }

    public void openHomePageActivity() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
        finish();
    }

    public void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}