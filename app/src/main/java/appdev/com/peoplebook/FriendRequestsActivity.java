package appdev.com.peoplebook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import appdev.com.peoplebook.Adapter.FriendRequestsAdapter;
import appdev.com.peoplebook.Models.FriendRequestReceiveModel;

public class FriendRequestsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String username;
    DatabaseReference frienddatabasereference;
    ArrayList<String> friendnames = new ArrayList<String>();
    ArrayList<String> friendusernames = new ArrayList<String>();
    ArrayList<String> friendstatus = new ArrayList<String>();
    ArrayList<String> friendprofileimage = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_requests);

        Widgets();
        username = getIntent().getStringExtra("username");
        frienddatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        DisplayRequestsReceived();
    }

    private void Widgets() {
        recyclerView = (RecyclerView) findViewById(R.id.friends_requests_activity_recyclerview);
    }

    private void DisplayRequestsReceived() {
        frienddatabasereference.child("Friends").child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                friendnames.clear();
                friendstatus.clear();
                friendprofileimage.clear();
                friendusernames.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    FriendRequestReceiveModel friendRequestReceiveModel = snapshot.getValue(FriendRequestReceiveModel.class);
                    String name = friendRequestReceiveModel.getName();
                    String image = friendRequestReceiveModel.getProfileimage();
                    String status = friendRequestReceiveModel.getStatus();
                    String friendusername = friendRequestReceiveModel.getUsername();
                    if (!status.equals(null)){
                        if (!status.equals("Accepted")){
                            friendnames.add(name);
                            friendstatus.add(status);
                            friendprofileimage.add(image);
                            friendusernames.add(friendusername);

                        }
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendRequestsActivity.this));
                recyclerView.setAdapter(new FriendRequestsAdapter(friendnames,friendstatus, friendprofileimage, friendusernames, username));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}