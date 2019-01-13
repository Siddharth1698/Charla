package ml.siddharthm.charla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private Button LoginButton;
    private ImageView PhoneLoginButton;
    private EditText UserEmail,UserPassword;
    private TextView NeedNewAccountLink,ForgotPassowrdLink;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        InitializeFeilds();
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegisterActivity();
            }
        });
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowUserToLogin();
            }
        });
    }

    private void allowUserToLogin() {
        String email;
        String password;
        email = UserEmail.getText().toString();
        password = UserPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Feild Cannot be empty",Toast.LENGTH_SHORT);
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Feild Cannot be empty",Toast.LENGTH_SHORT);
        }
        else{
            loadingBar.setTitle("Loggin into your Account");
            loadingBar.setMessage("Just a minute..Logging in Soon...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
              mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if (task.isSuccessful()){
                      sendUserToMainActivity();
                      Toast.makeText(LoginActivity.this,"Logged In Succesfully",Toast.LENGTH_SHORT);
                      loadingBar.dismiss();
                  }else {
                          String error = task.getException().toString();
                          Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT);
                          loadingBar.dismiss();
                      }}
              });}}






    private void InitializeFeilds() {
        LoginButton = (Button)findViewById(R.id.login_button);
        PhoneLoginButton = (ImageView) findViewById(R.id.phone_login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText)findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView)findViewById(R.id.need_new_account);
        ForgotPassowrdLink = (TextView)findViewById(R.id.forget_password);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null){
            sendUserToMainActivity();
        }
    }

    private void sendUserToMainActivity() {
        Intent sendIntent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(sendIntent);
        }

    private void sendUserToRegisterActivity() {
        Intent regIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(regIntent);
    }
}
