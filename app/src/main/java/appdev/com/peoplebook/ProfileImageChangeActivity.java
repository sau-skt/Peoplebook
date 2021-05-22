package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileImageChangeActivity extends AppCompatActivity {
    String username;
    DatabaseReference usernamedatabasereference;
    CircleImageView currentprofileimage;
    TextView changeprofileimage, save, discard;
    Uri ImageUri;
    StorageReference profileimagestoragelocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image_change);

        Widgets();
        username = getIntent().getStringExtra("username");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username").child(username);
        profileimagestoragelocation = FirebaseStorage.getInstance("gs://peoplebook-65c7c.appspot.com").getReference().child(username).child(username + ".jpg");

        LoadCurrentImage();
        changeprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveImage();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyProfileSettingsActivity();
            }
        });
    }

    private void SaveImage() {
        UploadTask uploadTask = profileimagestoragelocation.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ProfileImageChangeActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileimagestoragelocation.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String profileImageUrl=task.getResult().toString();
                        usernamedatabasereference.child("profileimage").setValue(profileImageUrl);
                    }
                });
                Toast.makeText(ProfileImageChangeActivity.this, "Profile Image Changed Successfully", Toast.LENGTH_SHORT).show();
                openMyProfileSettingsActivity();
            }
        });
    }

    private void openMyProfileSettingsActivity() {
        finish();
    }

    private void LoadCurrentImage() {
        usernamedatabasereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profileimageurl = snapshot.child("profileimage").getValue().toString();
                Picasso.get().load(profileimageurl).into(currentprofileimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Widgets() {
        currentprofileimage = (CircleImageView) findViewById(R.id.activity_profileimagechange_imageview);
        changeprofileimage = (TextView) findViewById(R.id.activity_profileimagechange_changeimage_textview);
        save = (TextView) findViewById(R.id.activity_profileimagechange_save_textview);
        discard = (TextView) findViewById(R.id.activity_profileimagechange_discard_textview);
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
            currentprofileimage.setImageURI(ImageUri);
        }
    }
}