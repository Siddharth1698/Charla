package ml.siddharthm.charla;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private tabsAccesorAdapter myTabsAccesorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       mAuth = FirebaseAuth.getInstance();
       currentUser = mAuth.getCurrentUser();

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
        }
    }

    private void sendUserToLoginAActivity() {
        Intent sendIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(sendIntent);
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
        if (item.getItemId() == R.id.main_logout_option){

        }
        if (item.getItemId() == R.id.main_logout_option){

        }
        return true;
    }
}
