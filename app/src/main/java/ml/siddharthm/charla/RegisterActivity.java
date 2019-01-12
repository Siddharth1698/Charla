package ml.siddharthm.charla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText UserEmail,UserPassword;
    private TextView AlreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeFeilds();
        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginActivity();
            }
        });
    }

    private void InitializeFeilds() {
        createAccountButton = (Button)findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText)findViewById(R.id.register_password);
        AlreadyHaveAccount = (TextView)findViewById(R.id.already_have_an_account);
    }

    private void sendUserToLoginActivity() {
        Intent regIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(regIntent);
    }
}
