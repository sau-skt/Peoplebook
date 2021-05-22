package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.Adapter.MyProfileActivityAdapter;
import appdev.com.peoplebook.Models.PostModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {
    CircleImageView currentprofileimage;
    TextView nametextview, usernametextview, addfriendtextview, knowmoreaboutmetextview;
    RecyclerView recyclerView;
    DatabaseReference usernamedatabasereference, postimagereference, frienddatabasereference;
    String username, currentusername;
    ArrayList<String> postimages = new ArrayList<String>();
    ArrayList<String> childnames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        
        Widgets();
        username = getIntent().getStringExtra("username");
        currentusername = getIntent().getStringExtra("currentusername");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");
        postimagereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts").child(username);
        frienddatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Friends");
        FillProfileDetails();
        LoadImagesPosted();
        CheckStatus();

        addfriendtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addfriendtextview.getText().equals("Add Friend")){
                    frienddatabasereference.child(username).child(currentusername).child("username").setValue(currentusername);
                    frienddatabasereference.child(username).child(currentusername).child("status").setValue("Request Sent");
                    usernamedatabasereference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String name = snapshot.child(currentusername).child("name").getValue().toString();
                            String image = snapshot.child(currentusername).child("profileimage").getValue().toString();
                            frienddatabasereference.child(username).child(currentusername).child("name").setValue(name);
                            frienddatabasereference.child(username).child(currentusername).child("profileimage").setValue(image);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else if (addfriendtextview.getText().equals("Cancel Request")){
                    frienddatabasereference.child(username).child(currentusername).removeValue();
                    addfriendtextview.setText("Add Friend");
                } else if (addfriendtextview.getText().equals("Unfriend")){
                    frienddatabasereference.child(currentusername).child(username).removeValue();
                    frienddatabasereference.child(username).child(currentusername).removeValue();
                    addfriendtextview.setText("Add Friend");
                }
            }
        });
        knowmoreaboutmetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenKnowMoreAboutMeActivity();
            }
        });

    }

    private void OpenKnowMoreAboutMeActivity() {
        Intent intent = new Intent(OtherProfileActivity.this,KnowMoreAboutMeActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void CheckStatus() {
        frienddatabasereference.child(username).child(currentusername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("status").exists()){
                    String status = snapshot.child("status").getValue().toString();
                    if (status.equals("Request Sent")){
                        addfriendtextview.setText("Cancel Request");
                    } else if (status.equals("Accepted")){
                        addfriendtextview.setText("Unfriend");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        currentprofileimage = (CircleImageView) findViewById(R.id.otherprofile_activity_profileimage_imageview);
        nametextview = (TextView) findViewById(R.id.otherprofile_activity_name_textview);
        usernametextview = (TextView) findViewById(R.id.otherprofile_activity_username_textview);
        addfriendtextview = (TextView) findViewById(R.id.otherprofile_activity_friendstatus_textview);
        recyclerView = (RecyclerView) findViewById(R.id.other_profile_activity_recyclerview);
        knowmoreaboutmetextview = (TextView) findViewById(R.id.otherprofile_activity_knowmore_textview);
    }

    private void FillProfileDetails() {
        usernamedatabasereference.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                nametextview.setText(name);
                usernametextview.setText(username);
                String profileimageurl = snapshot.child("profileimage").getValue().toString();
                Picasso.get().load(profileimageurl).into(currentprofileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadImagesPosted() {
        postimagereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postimages.clear();
                childnames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostModel postModel = snapshot.getValue(PostModel.class);
                    postimages.add(postModel.getImage());
                    childnames.add(postModel.getFilename());
                }
                recyclerView.setLayoutManager(new GridLayoutManager(OtherProfileActivity.this,3));
                recyclerView.setAdapter(new MyProfileActivityAdapter(postimages, childnames,""));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}