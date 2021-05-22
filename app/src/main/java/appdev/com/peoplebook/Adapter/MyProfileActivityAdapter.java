package appdev.com.peoplebook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.DeletePostActivity;
import appdev.com.peoplebook.R;

public class MyProfileActivityAdapter extends RecyclerView.Adapter<MyProfileActivityAdapter.MyProfileActivityViewHolder> {
    ArrayList<String> images = new ArrayList<String>();
    ArrayList<String> childnames = new ArrayList<String>();
    String username;
    Context context;

    public MyProfileActivityAdapter(ArrayList<String> images, ArrayList<String> childnames, String username){
        this.images = images;
        this.childnames = childnames;
        this.username = username;
    }

    @NonNull
    @Override
    public MyProfileActivityAdapter.MyProfileActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_profile_activity_recyclerview_layout, parent, false);
        return new MyProfileActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileActivityAdapter.MyProfileActivityViewHolder holder, int position) {
        String imageurl = images.get(position);
        String childnode = childnames.get(position);
        Picasso.get().load(imageurl).into(holder.postImage);
        holder.Postlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.equals("")){
                    Intent intent = new Intent(context, DeletePostActivity.class);
                    intent.putExtra("key",childnode);
                    intent.putExtra("image",imageurl);
                    intent.putExtra("username", username);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyProfileActivityViewHolder extends RecyclerView.ViewHolder{
        ImageView postImage;
        RelativeLayout Postlayout;
        public MyProfileActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = (ImageView) itemView.findViewById(R.id.myprofile_activity_postimage_imageview);
            Postlayout = (RelativeLayout) itemView.findViewById(R.id.post_image_layout);
            context = itemView.getContext();
        }
    }


}
