package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DeletePostActivity extends AppCompatActivity {
    String image, key, username;
    ImageView postimage;
    TextView deleteposttv;
    DatabaseReference homepostreference, postreference;
    StorageReference postimagestorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_post);
        Widgets();
        key = getIntent().getStringExtra("key");
        image = getIntent().getStringExtra("image");
        username = getIntent().getStringExtra("username");
        postreference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts").child(username).child(key);
        postimagestorage = FirebaseStorage.getInstance("gs://peoplebook-65c7c.appspot.com").getReference().child(username);
        homepostreference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("HomepagePosts").child(key);
        SetImage();
        deleteposttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String filename = snapshot.child("filename").getValue().toString();
                        postimagestorage.child(filename + ".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                postreference.removeValue();
                                homepostreference.removeValue();
                                OpenMyProfileActivity();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void OpenMyProfileActivity() {
        finish();
    }

    private void SetImage() {
        Picasso.get().load(image).into(postimage);
    }

    private void Widgets() {
        postimage = findViewById(R.id.activity_delete_post_imageview);
        deleteposttv = findViewById(R.id.activity_delete_post_textview);
    }
}