package com.example.harjoitustyo2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Context context;
    TextView password;
    TextView username;
    Button register, login;
    DatabaseHelper databaseHelper;
    Hashing hashing;
    User user;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = Login.this;
        username = (TextView) findViewById(R.id.address);
        password = (TextView) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login  = (Button) findViewById(R.id.login);
        databaseHelper = new DatabaseHelper(this);
        hashing = new Hashing();

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final String usernameCheck = username.getText().toString();
                final String passwordCheck = password.getText().toString();

                String finalCheckPassword = null;
                
                if (usernameCheck.isEmpty() || passwordCheck.isEmpty()){
                    Toast.makeText(Login.this, "Enter username and password!", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.checkUsername(usernameCheck)) {
                        user = new User(context, usernameCheck);
                        byte[] byteConverter = user.getSaltId();

                        finalCheckPassword = hashing.getSecurePassword(passwordCheck, byteConverter);

                        if (databaseHelper.isLoginValid(usernameCheck, finalCheckPassword)) {
                            Intent intent = new Intent(Login.this, MainPage.class);
                            intent.putExtra("Username", username.getText().toString());
                            startActivity(intent);

                            Toast.makeText(Login.this, "Login is succesful!", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(Login.this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "   User does not exist! \nDo you want to register?", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }
    

}