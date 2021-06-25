package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import appdev.com.peoplebook.Adapter.FindFriendsActivityAdapter;
import appdev.com.peoplebook.Models.FindFriendsModel;
import appdev.com.peoplebook.Models.PostModel;

public class
FindFriendsActivity extends AppCompatActivity {
    //Variables for Widgets
    RecyclerView recyclerView;
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> currentusernames = new ArrayList<String>();
    String username;
    DatabaseReference usernamedatabasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        Widgets();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");
        GetListOfNamesAndImages();
    }

    private void GetListOfNamesAndImages() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                names.clear();
                images.clear();
                currentusernames.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    FindFriendsModel findFriendsModel = snapshot.getValue(FindFriendsModel.class);
                    String currentusername = findFriendsModel.getUsername();
                    if (!currentusername.equals(username)){
                        names.add(findFriendsModel.getName());
                        images.add(findFriendsModel.getProfileimage());
                        currentusernames.add(findFriendsModel.getUsername());
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(FindFriendsActivity.this));
                recyclerView.setAdapter(new FindFriendsActivityAdapter(names,images,currentusernames,username));
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        recyclerView = (RecyclerView) findViewById(R.id.find_friends_activity_recyclerview);
    }
}