package appdev.com.peoplebook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.MessageActivity;
import appdev.com.peoplebook.OtherProfileActivity;
import appdev.com.peoplebook.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivityAdapter extends RecyclerView.Adapter<ChatActivityAdapter.ChatActivityViewHolder> {
    ArrayList<String> friendnames = new ArrayList<String>();
    ArrayList<String> friendstatus = new ArrayList<String>();
    ArrayList<String> friendprofileimage = new ArrayList<String>();
    ArrayList<String> friendusernames = new ArrayList<String>();
    String username;
    Context context;
    DatabaseReference frienddatabasereference, usernamedatabasereference;

    public ChatActivityAdapter(ArrayList<String> friendnames, ArrayList<String> friendstatus, ArrayList<String> friendprofileimage, ArrayList<String> friendusernames, String username) {
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
    public ChatActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.find_friends_activity_recyclerview, parent, false);
        return new ChatActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatActivityViewHolder holder, int position) {
        String name = friendnames.get(position);
        String imageurl = friendprofileimage.get(position);
        holder.currentname.setText(name);
        Picasso.get().load(imageurl).into(holder.currentimage);
        holder.openprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("username",friendusernames.get(position));
                intent.putExtra("currentusername",username);
                intent.putExtra("image",imageurl);
                intent.putExtra("friendname",name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendnames.size();
    }

    public class ChatActivityViewHolder extends RecyclerView.ViewHolder{
        TextView currentname;
        CircleImageView currentimage;
        LinearLayout openprofile;
        public ChatActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            currentname = (TextView) itemView.findViewById(R.id.find_friends_activity_name_textview);
            currentimage = (CircleImageView) itemView.findViewById(R.id.find_friends_activity_profileimage_imageview);
            openprofile = (LinearLayout) itemView.findViewById(R.id.find_friends_profile_open);
            context = itemView.getContext();
        }
    }

}
