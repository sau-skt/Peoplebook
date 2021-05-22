package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import appdev.com.peoplebook.Adapter.MessageAdapter;
import appdev.com.peoplebook.Models.MessageModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    String username, friendusername, filename, friendimage, friendname;
    DatabaseReference messagedatabasereference, usernamedatabasereference;
    RecyclerView recyclerView;
    EditText messageedittext;
    TextView sendtextview, friendnametextview;
    CircleImageView friendprofileimage;
    ArrayList<String> messages = new ArrayList<String>();
    ArrayList<String> keys = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> sender = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Widgets();
        username = getIntent().getStringExtra("currentusername");
        friendusername = getIntent().getStringExtra("username");
        friendimage = getIntent().getStringExtra("image");
        friendname = getIntent().getStringExtra("friendname");
        messagedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Messages");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Username").child(username);
        LoadMessages();
        LoadProfileDetails();
        sendtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessagetoDatabase();
            }
        });
    }

    private void LoadProfileDetails() {
        Picasso.get().load(friendimage).into(friendprofileimage);
        friendnametextview.setText(friendname);
    }

    private void SendMessagetoDatabase() {
        String msg = messageedittext.getText().toString();
        if (!msg.equals(null) && !msg.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
            Date date = new Date();
            String filenamewithcolon = formatter.format(date);
            filename = filenamewithcolon.replace(":", "");
            messagedatabasereference.child(username).child(friendusername).child(filename).child("message").setValue(msg);
            messagedatabasereference.child(username).child(friendusername).child(filename).child("messagekey").setValue(filename);
            messagedatabasereference.child(username).child(friendusername).child(filename).child("sender").setValue(username);
            messagedatabasereference.child(friendusername).child(username).child(filename).child("message").setValue(msg);
            messagedatabasereference.child(friendusername).child(username).child(filename).child("messagekey").setValue(filename);
            messagedatabasereference.child(friendusername).child(username).child(filename).child("sender").setValue(username);
            usernamedatabasereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String profileimage = snapshot.child("profileimage").getValue().toString();
                    messagedatabasereference.child(username).child(friendusername).child(filename).child("profileimage").setValue(profileimage);
                    messagedatabasereference.child(friendusername).child(username).child(filename).child("profileimage").setValue(profileimage);
                    LoadMessages();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            messageedittext.setText(null);
        }
    }

    private void LoadMessages() {
        messagedatabasereference.child(username).child(friendusername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                keys.clear();
                image.clear();
                sender.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    String msg = messageModel.getMessage();
                    String key = messageModel.getMessagekey();
                    String img = messageModel.getProfileimage();
                    String send = messageModel.getSender();
                    messages.add(msg);
                    keys.add(key);
                    image.add(img);
                    sender.add(send);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                recyclerView.setAdapter(new MessageAdapter(messages, keys, image, sender));
                recyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        recyclerView = (RecyclerView) findViewById(R.id.activity_message_recyclerview);
        messageedittext = (EditText) findViewById(R.id.activity_message_message_edittext);
        sendtextview = (TextView) findViewById(R.id.activity_message_send_textview);
        friendnametextview = (TextView) findViewById(R.id.activity_message_friendname);
        friendprofileimage = (CircleImageView) findViewById(R.id.activity_message_profileimage);
    }
}