package com.winshelosl.instaapp;

import static com.parse.ParseUser.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SingUp extends AppCompatActivity {

    private String TAG = SingUp.class.getSimpleName();

    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private Button btSignUp;
    private Button btLogin;

    String username;
    String password;
    String email;


    ParseUser user = new ParseUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        btSignUp = findViewById(R.id.btSingup);
        btLogin = findViewById(R.id.btLogin);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                email = etEmail.getText().toString();

                singUp(username, password, email);
            }
        });


    }



    public void singUp(String username, String password, String email){


        if( username.isEmpty()){
            Toast.makeText(SingUp.this, "username is required", Toast.LENGTH_SHORT).show();
            return;
        } else if(password.isEmpty()){
            Toast.makeText(SingUp.this, "password is required", Toast.LENGTH_SHORT).show();

        }else {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with SignUp", e);
                        Toast.makeText(SingUp.this, "Issue with SignUp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    goLoginActivity();
                }
            });
        }


    }

    private void goLoginActivity() {

        Intent i = new Intent(this, login.class);
        startActivity(i);
    }
}