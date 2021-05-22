package appdev.com.peoplebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.Adapter.HomePageActivityRecyclerViewAdapter;
import appdev.com.peoplebook.Models.PostModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout mdrawer;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    String username;
    TextView currentname;
    CircleImageView currentprofileimage;
    DatabaseReference usernamedatabasereference, homepagepostdatabasereference;
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();
    ArrayList<String> usernames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Widgets();
        HamburgerIcon();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        homepagepostdatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("HomepagePosts");
        navigationView.setNavigationItemSelectedListener(this);

        Setupnameandprofileimage();
        GetHomePagePosts();
    }

    private void GetHomePagePosts() {
        homepagepostdatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                names.clear();
                profileimages.clear();
                usernames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    PostModel postModel = snapshot.getValue(PostModel.class);
                    String image = postModel.getImage();
                    String postusername = postModel.getName();
                    String postuserimage = postModel.getProfileimage();
                    String postname = postModel.getUsername();
                    images.add(image);
                    names.add(postusername);
                    profileimages.add(postuserimage);
                    usernames.add(postname);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
                recyclerView.setAdapter(new HomePageActivityRecyclerViewAdapter(images, names, profileimages, usernames, username));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Setupnameandprofileimage() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue().toString();
                currentname.setText(name);
                String profileimageurl = snapshot.child("profileimage").getValue().toString();
                Picasso.get().load(profileimageurl).into(currentprofileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HamburgerIcon() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = setupDrawerToggle();
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mdrawer.addDrawerListener(drawerToggle);
    }

    private void Widgets() {
        navigationView = findViewById(R.id.homepage_navigation);
        View header = navigationView.getHeaderView(0);
        recyclerView = (RecyclerView) findViewById(R.id.homepage_rv);
        toolbar = (Toolbar) findViewById(R.id.homepageactivity_toolbar);
        mdrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        currentname = (TextView) header.findViewById(R.id.activity_homepage_username_textview);
        currentprofileimage = (CircleImageView) header.findViewById(R.id.activity_homepage_profileimage_imageview);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mdrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_one:
                openMyProfileActivity();
                break;
            case R.id.nav_item_two:
                openFriendsActivity();
                break;
            case R.id.nav_item_three:
                openFindFriendsActivity();
                break;
            case R.id.nav_item_four:
                openChatActivity();
                break;
            case R.id.nav_item_five:
                openLoginActivity();
                break;
            case R.id.nav_item_six:
                openFriendRequestActivity();
                break;
        }
        return false;
    }

    private void openFriendRequestActivity() {
        Intent intent = new Intent(this, FriendRequestsActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void openMyProfileActivity(){
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void openFriendsActivity(){
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void openFindFriendsActivity(){
        Intent intent = new Intent(this, FindFriendsActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void openChatActivity(){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}