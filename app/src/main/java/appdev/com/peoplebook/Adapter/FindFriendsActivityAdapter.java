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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.OtherProfileActivity;
import appdev.com.peoplebook.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsActivityAdapter extends RecyclerView.Adapter<FindFriendsActivityAdapter.FindFriendsActivityViewHolder> {

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> usernames = new ArrayList<String>();
    String username;
    Context context;

    public FindFriendsActivityAdapter(ArrayList<String> names, ArrayList<String> images, ArrayList<String> usernames, String username){
        this.names = names;
        this.images = images;
        this.usernames = usernames;
        this.username = username;
    }

    @NonNull
    @Override
    public FindFriendsActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.find_friends_activity_recyclerview, parent, false);
        return new FindFriendsActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendsActivityViewHolder holder, int position) {
        String name = names.get(position);
        String imageurl = images.get(position);
        holder.currentname.setText(name);
        Picasso.get().load(imageurl).into(holder.currentimage);
        holder.openprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OtherProfileActivity.class);
                intent.putExtra("username",usernames.get(position));
                intent.putExtra("currentusername",username);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class FindFriendsActivityViewHolder extends RecyclerView.ViewHolder{
        TextView currentname;
        CircleImageView currentimage;
        LinearLayout openprofile;
        public FindFriendsActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            currentname = (TextView) itemView.findViewById(R.id.find_friends_activity_name_textview);
            currentimage = (CircleImageView) itemView.findViewById(R.id.find_friends_activity_profileimage_imageview);
            openprofile = (LinearLayout) itemView.findViewById(R.id.find_friends_profile_open);
            context = itemView.getContext();
        }
    }
}
