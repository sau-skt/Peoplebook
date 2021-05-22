package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class MyProfileActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView myprofileinformation, nametextview, usernametextview, postimage;
    String username;
    DatabaseReference usernamedatabasereference, postimagereference;
    CircleImageView currentprofileimage;
    ArrayList<String> postimages = new ArrayList<String>();
    ArrayList<String> childnames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Widgets();

        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        postimagereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts").child(username);
        FillProfileDetails();
        LoadImagesPosted();

        myprofileinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyProfileSettingsActivity();
            }
        });

        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPostActivity();
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
                recyclerView.setLayoutManager(new GridLayoutManager(MyProfileActivity.this,3));
                recyclerView.setAdapter(new MyProfileActivityAdapter(postimages,childnames, username));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openPostActivity() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    private void FillProfileDetails() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
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

    private void Widgets() {
        myprofileinformation = (TextView) findViewById(R.id.myprofile_activity_changeinformation_textview);
        recyclerView = (RecyclerView) findViewById(R.id.my_profile_activity_recyclerview);
        currentprofileimage = (CircleImageView) findViewById(R.id.myprofile_activity_profileimage_imageview);
        nametextview = (TextView) findViewById(R.id.myprofile_activity_name_textview);
        usernametextview = (TextView) findViewById(R.id.myprofile_activity_username_textview);
        postimage = (TextView) findViewById(R.id.myprofile_activity_postaimage_textview);
    }

    public void openMyProfileSettingsActivity(){
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}