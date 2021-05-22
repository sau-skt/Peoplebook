package appdev.com.peoplebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostActivity extends AppCompatActivity {
    String username, filename, postuserimage, postusername, postname;
    ImageView currentimagetopost;
    TextView selectimage, postimage, discard;
    Uri ImageUri;
    DatabaseReference postimagereference, homepageimagereference, usernamedatabasereference;
    StorageReference postimagestorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Widgets();
        username = getIntent().getStringExtra("username");
        postimagereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Posts").child(username);
        homepageimagereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("HomepagePosts");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
        Date date = new Date();
        String filenamewithcolon = formatter.format(date);
        filename = filenamewithcolon.replace(":","");
        postimagestorage = FirebaseStorage.getInstance("gs://peoplebook-65c7c.appspot.com").getReference().child(username).child(filename + ".jpg");
        LoadPostUserImageandName();
        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUri!=null) {
                    PostImage();
                }
                else {
                    Toast.makeText(PostActivity.this, "Please select image to post", Toast.LENGTH_SHORT).show();
                }
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenMyProfileActivity();
            }
        });
    }

    private void LoadPostUserImageandName() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postuserimage = snapshot.child("profileimage").getValue().toString();
                postusername = snapshot.child("username").getValue().toString();
                postname = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PostImage() {
        UploadTask uploadTask = postimagestorage.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PostActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                postimagestorage.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String profileImageUrl=task.getResult().toString();
                        postimagereference.child(filename).child("image").setValue(profileImageUrl);
                        postimagereference.child(filename).child("filename").setValue(filename);
                        postimagereference.child(filename).child("profileimage").setValue(postuserimage);
                        postimagereference.child(filename).child("name").setValue(postname);
                        postimagereference.child(filename).child("username").setValue(postusername);
                        homepageimagereference.child(filename).child("image").setValue(profileImageUrl);
                        homepageimagereference.child(filename).child("filename").setValue(filename);
                        homepageimagereference.child(filename).child("profileimage").setValue(postuserimage);
                        homepageimagereference.child(filename).child("name").setValue(postname);
                        homepageimagereference.child(filename).child("username").setValue(postusername);
                    }
                });
                Toast.makeText(PostActivity.this, "Image Posted Successfully", Toast.LENGTH_SHORT).show();
                OpenMyProfileActivity();
            }
        });
    }

    private void OpenMyProfileActivity() {
        finish();
    }

    private void Widgets() {
        currentimagetopost = (ImageView) findViewById(R.id.activity_post_imageview);
        selectimage = (TextView) findViewById(R.id.activity_post_selectimage_textview);
        postimage = (TextView) findViewById(R.id.activity_post_postimage_textview);
        discard = (TextView) findViewById(R.id.activity_post_discard_textview);
    }

    private void OpenGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            currentimagetopost.setImageURI(ImageUri);
        }
    }
}