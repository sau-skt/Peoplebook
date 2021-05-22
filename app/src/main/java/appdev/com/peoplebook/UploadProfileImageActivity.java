package appdev.com.peoplebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.lang.Long.parseLong;

public class UploadProfileImageActivity extends AppCompatActivity {
    CircleImageView profileimage;
    TextView uploadimage, createaccount;
    Uri ImageUri;
    StorageReference profileimagestoragelocation;
    DatabaseReference usernamedatabasereference;
    String username, name, password, phone, aboutme, hobbies, dateofbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_image);

        Widgets();
        username= getIntent().getStringExtra("username");
        name = getIntent().getStringExtra("name");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");
        aboutme = getIntent().getStringExtra("aboutme");
        hobbies = getIntent().getStringExtra("hobbies");
        dateofbirth = getIntent().getStringExtra("dateofbirth");
        profileimagestoragelocation = FirebaseStorage.getInstance("gs://peoplebook-65c7c.appspot.com").getReference().child(username).child(username + ".jpg");
        usernamedatabasereference = FirebaseDatabase.getInstance("https://peoplebook-65c7c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Username");

        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageUri!=null){
                    UploadImageToFirebaseStorage();
                }
                else {
                    Toast.makeText(UploadProfileImageActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UploadImageToFirebaseStorage() {
        UploadTask uploadTask = profileimagestoragelocation.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UploadProfileImageActivity.this, "An error occured", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileimagestoragelocation.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String profileImageUrl=task.getResult().toString();
                        usernamedatabasereference.child(username).child("profileimage").setValue(profileImageUrl);
                        usernamedatabasereference.child(username).child("username").setValue(username);
                        usernamedatabasereference.child(username).child("name").setValue(name);
                        usernamedatabasereference.child(username).child("password").setValue(password);
                        usernamedatabasereference.child(username).child("phone").setValue(parseLong(phone));
                        usernamedatabasereference.child(username).child("aboutme").setValue(aboutme);
                        usernamedatabasereference.child(username).child("hobbies").setValue(hobbies);
                        usernamedatabasereference.child(username).child("dateofbirth").setValue(dateofbirth);
                    }
                });
                Toast.makeText(UploadProfileImageActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                openHomePageActivity();
            }
        });
    }

    public void openHomePageActivity(){
        finish();
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
            profileimage.setImageURI(ImageUri);
        }
    }

    private void Widgets() {
        profileimage = (CircleImageView) findViewById(R.id.activity_upload_profile_image_imageview);
        uploadimage = (TextView) findViewById(R.id.activity_upload_profile_image_uploadimage_textview);
        createaccount = (TextView) findViewById(R.id.activity_upload_profile_image_createaccount_textview);
    }
}