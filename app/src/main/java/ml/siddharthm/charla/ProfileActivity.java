package ml.siddharthm.charla;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    private String receiverUserId;
    private TextView tests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tests = findViewById(R.id.tests);
        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        Toast.makeText(this,"User id : "+ receiverUserId,Toast.LENGTH_LONG);
        tests.setText(receiverUserId);
    }
}
