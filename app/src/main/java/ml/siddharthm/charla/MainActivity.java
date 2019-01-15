package ml.siddharthm.charla;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private tabsAccesorAdapter myTabsAccesorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mAuth = FirebaseAuth.getInstance();
       currentUser = mAuth.getCurrentUser();
       RootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Charla");

        myViewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        myTabsAccesorAdapter = new tabsAccesorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccesorAdapter);

        myTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (currentUser == null){
            sendUserToLoginAActivity();
        }else {
            verifyUserExistance();

        }
    }

    private void verifyUserExistance() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this,"Welcome",Toast.LENGTH_SHORT);
                }else {
                    sendUserToSettingsAActivity();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void sendUserToLoginAActivity() {
        Intent sendIntent = new Intent(MainActivity.this,LoginActivity.class);
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(sendIntent);
        finish();
    }

    private void sendUserToSettingsAActivity() {
        Intent setIntent = new Intent(MainActivity.this,SettingsActivity.class);
        setIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.main_logout_option){
            mAuth.signOut();
            sendUserToLoginAActivity();
        }
        if (item.getItemId() == R.id.main_settings_option){
            sendUserToSettingsAActivity();

        }
        if (item.getItemId() == R.id.main_find_friends_option){

        }
        return true;
    }
}
