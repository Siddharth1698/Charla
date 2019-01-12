package ml.siddharthm.charla;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private tabsAccesorAdapter myTabsAccesorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Charla");

        myViewPager = (ViewPager)findViewById(R.id.main_tabs_pager);
        myTabsAccesorAdapter = new tabsAccesorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccesorAdapter);

        myTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }
}
