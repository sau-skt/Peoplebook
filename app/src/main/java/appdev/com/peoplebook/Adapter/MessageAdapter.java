package appdev.com.peoplebook.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import appdev.com.peoplebook.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    ArrayList<String> messages = new ArrayList<String>();
    ArrayList<String> keys = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> sender = new ArrayList<String>();

    public MessageAdapter(ArrayList<String> messages,ArrayList<String> keys,ArrayList<String> image,ArrayList<String> sender) {
        this.messages = messages;
        this.keys = keys;
        this.image = image;
        this.sender = sender;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message_activity_recycler_view, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String proimg = image.get(position);
        String msg = messages.get(position);
        Picasso.get().load(proimg).into(holder.profileimge);
        holder.message.setText(msg);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileimge;
        TextView message;
        RelativeLayout layout;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimge = itemView.findViewById(R.id.message_activity_profileimage);
            message = itemView.findViewById(R.id.message_activity_message_textview);
            layout = itemView.findViewById(R.id.message_activity_recyclerview_layout);
        }
    }
}
