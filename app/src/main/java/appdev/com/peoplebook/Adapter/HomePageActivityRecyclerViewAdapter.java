package appdev.com.peoplebook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.OtherProfileActivity;
import appdev.com.peoplebook.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivityRecyclerViewAdapter extends RecyclerView.Adapter<HomePageActivityRecyclerViewAdapter.HomePageActivityRecyclerViewViewHolder> {
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> reverseimage = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayList<String> reversenames = new ArrayList<String>();
    ArrayList<String> profileimages = new ArrayList<String>();
    ArrayList<String> reverseprofileimages = new ArrayList<String>();
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> reverseusernames = new ArrayList<String>();
    String username;
    Context context;

    public HomePageActivityRecyclerViewAdapter(ArrayList<String> images, ArrayList<String> names, ArrayList<String> profileimages, ArrayList<String> usernames, String username) {
        this.images = images;
        this.names = names;
        this.profileimages = profileimages;
        this.usernames = usernames;
        this.username = username;
    }

    @NonNull
    @Override
    public HomePageActivityRecyclerViewAdapter.HomePageActivityRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.homepage_activity_recyclerview, parent, false);
        return new HomePageActivityRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageActivityRecyclerViewAdapter.HomePageActivityRecyclerViewViewHolder holder, int position) {
        for (int j = images.size() - 1; j >= 0; j--) {
            reverseimage.add(images.get(j));
        }
        for (int j = names.size() - 1; j >= 0; j--) {
            reversenames.add(names.get(j));
        }
        for (int j = profileimages.size() - 1; j >= 0; j--) {
            reverseprofileimages.add(profileimages.get(j));
        }
        for (int j = usernames.size() - 1; j >= 0; j--) {
            reverseusernames.add(usernames.get(j));
        }
        Picasso.get().load(reverseimage.get(position)).into(holder.homepageimage);
        Picasso.get().load(reverseprofileimages.get(position)).into(holder.profileimg);
        holder.nametextview.setText(reversenames.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!reverseusernames.get(position).equals(username)){
                    Intent intent = new Intent(context, OtherProfileActivity.class);
                    intent.putExtra("username",reverseusernames.get(position));
                    intent.putExtra("currentusername",username);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class HomePageActivityRecyclerViewViewHolder extends RecyclerView.ViewHolder {
        ImageView homepageimage;
        CircleImageView profileimg;
        TextView nametextview;
        RelativeLayout layout;
        public HomePageActivityRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            homepageimage = itemView.findViewById(R.id.homepageactivity_imageview);
            profileimg = itemView.findViewById(R.id.homepageactivity_profileimage);
            nametextview = itemView.findViewById(R.id.homepageactivity_name);
            context = itemView.getContext();
            layout = itemView.findViewById(R.id.homepageacticity_layout);
        }
    }
}
