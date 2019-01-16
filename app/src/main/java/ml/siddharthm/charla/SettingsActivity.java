package ml.siddharthm.charla;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private EditText userName,userStatus;
    private Button updateAccountSettings;
    private CircleImageView userProfileImage;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        InitializeFeilds();
        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateSettings();
            }
        });
        RetriveUserInfo();
    }

    private void RetriveUserInfo() {
        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image"))){
                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();
                  //  String retriveProfileImage = dataSnapshot.child("image").getValue().toString();

                    userName.setText(retriveUserName);
                    userStatus.setText(retriveStatus);

                }else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("name"))){

                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveStatus = dataSnapshot.child("status").getValue().toString();
                   // String retriveProfileImage = dataSnapshot.child("image").getValue().toString();

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
    }
    private void sendUserToMainActivity(){
        Intent mainIntent = new Intent(SettingsActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
