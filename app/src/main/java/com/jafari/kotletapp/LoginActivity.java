package com.jafari.kotletapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        Button login = (Button) findViewById(R.id.loginBtn);
        Button register = (Button) findViewById(R.id.registerBtn);

        login.setOnClickListener(v -> {
            boolean valid_user = databaseHelper.validUser(email.getText().toString(), password.getText().toString());
            if (valid_user) {
                String fullName = databaseHelper.getFullName(email.getText().toString());
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("fullname" , fullName);
                i.putExtra("email" , email.getText().toString());
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }
}