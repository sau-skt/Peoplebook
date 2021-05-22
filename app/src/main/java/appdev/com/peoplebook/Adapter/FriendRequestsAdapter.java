package appdev.com.peoplebook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import appdev.com.peoplebook.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.FriendRequestActivityViewHolder> {
    ArrayList<String> friendnames = new ArrayList<String>();
    ArrayList<String> friendstatus = new ArrayList<String>();
    ArrayList<String> friendprofileimage = new ArrayList<String>();
    ArrayList<String> friendusernames = new ArrayList<String>();
    String username;
    DatabaseReference frienddatabasereference, usernamedatabasereference;

    public FriendRequestsAdapter(ArrayList<String> friendnames, ArrayList<String> friendstatus, ArrayList<String> friendprofileimage, ArrayList<String> friendusernames, String username) {
        this.friendnames = friendnames;
        this.friendstatus = friendstatus;
        this.friendprofileimage = friendprofileimage;
        this.friendusernames = friendusernames;
        this.username = username;
        frienddatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Friends");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Username");
        }

    @NonNull
    @Override
    public FriendRequestActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friend_request_activity_recyclerview, parent, false);
        return new FriendRequestActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestActivityViewHolder holder, int position) {
        String name = friendnames.get(position);
        String imageurl = friendprofileimage.get(position);
        String friendusername = friendusernames.get(position);
        Picasso.get().load(imageurl).into(holder.profileimage);
        holder.name.setText(name);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frienddatabasereference.child(username).child(friendusername).child("status").setValue("Accepted");
                frienddatabasereference.child(username).child(friendusername).child("name").setValue(name);
                frienddatabasereference.child(username).child(friendusername).child("profileimage").setValue(imageurl);
                frienddatabasereference.child(username).child(friendusername).child("username").setValue(friendusername);
                usernamedatabasereference.child(username).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.child("name").getValue().toString();
                        String image = snapshot.child("profileimage").getValue().toString();
                        frienddatabasereference.child(friendusername).child(username).child("name").setValue(name);
                        frienddatabasereference.child(friendusername).child(username).child("profileimage").setValue(image);
                        frienddatabasereference.child(friendusername).child(username).child("status").setValue("Accepted");
                        frienddatabasereference.child(friendusername).child(username).child("username").setValue(username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendnames.size();
    }

    public class FriendRequestActivityViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileimage;
        TextView name,accept;
        public FriendRequestActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.friend_request_activity_profileimage_imageview);
            name = itemView.findViewById(R.id.friend_request_activity_name_textview);
            accept = itemView.findViewById(R.id.friend_request_activity_accept_textview);
        }
    }
}
