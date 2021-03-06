package ml.siddharthm.charla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText userName,userStatus;
    private Button updateAccountSettings;
    private CircleImageView userProfileImage;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    private static int galleryPic = 1;
    private ProgressDialog loadingbar;
    private StorageReference UserProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        InitializeFeilds();
        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });
        RetriveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,galleryPic);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==galleryPic && resultCode==RESULT_OK && data!=null){
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                loadingbar.setTitle("Set Profile Image");
                loadingbar.setMessage("PLEASE WAIT...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                Uri resultUri = result.getUri();
                StorageReference filepath = UserProfileImageRef.child(currentUserId + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SettingsActivity.this,"Image uploaded succesfully...",Toast.LENGTH_SHORT);
                            final String downloadUrl = task.getResult().getDownloadUrl().toString();
                            RootRef.child("Users").child(currentUserId).child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SettingsActivity.this,"Image saved in db succesfully",Toast.LENGTH_SHORT);
                                        loadingbar.dismiss();
                                    }else {
                                        Toast.makeText(SettingsActivity.this,task.getException().toString(),Toast.LENGTH_SHORT);
                                        loadingbar.dismiss();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(SettingsActivity.this,task.getException().toString(),Toast.LENGTH_SHORT);
                            loadingbar.dismiss();

                        }
                    }
                });
            }
        }

    }

    private void RetriveUserInfo() {
        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image"))){
                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();
                    String retriveProfileImage = dataSnapshot.child("image").getValue().toString();

                    userName.setText(retriveUserName);
                    userStatus.setText(retriveStatus);
                    Picasso.get().load(retriveProfileImage).into(userProfileImage);


                }else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();

                    userName.setText(retriveUserName);
                    userStatus.setText(retriveStatus);


                }else {
                    Toast.makeText(SettingsActivity.this,"Please update your info",Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateSettings() {
          String setUserName = userName.getText().toString();
          String setUserStatus = userStatus.getText().toString();

          if (TextUtils.isEmpty(setUserName)){
              Toast.makeText(this,"Please write your User name",Toast.LENGTH_SHORT);
          }
        if (TextUtils.isEmpty(setUserStatus)){
            Toast.makeText(this,"Please write your Status",Toast.LENGTH_SHORT);
        }
        else{
            HashMap<String,String> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserId);
            profileMap.put("name",setUserName);
            profileMap.put("status",setUserStatus);
            RootRef.child("Users").child(currentUserId).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserToMainActivity();
                        Toast.makeText(SettingsActivity.this,"Profile Updated Succesfully",Toast.LENGTH_SHORT);
                    }else {
                        String error = task.getException().toString();
                        Toast.makeText(SettingsActivity.this,error,Toast.LENGTH_SHORT);
                    }
                }
            });
        }

    }

    private void InitializeFeilds() {
        updateAccountSettings = (Button)findViewById(R.id.set_settings_button);
        userName = (EditText)findViewById(R.id.set_user_name);
        userStatus = (EditText)findViewById(R.id.set_profile_status);
        userProfileImage = (CircleImageView)findViewById(R.id.set_profile_image);
        loadingbar = new ProgressDialog(this);
    }
    private void sendUserToMainActivity(){
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);

    }
}
