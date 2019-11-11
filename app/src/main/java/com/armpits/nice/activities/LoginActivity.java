package com.armpits.nice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.armpits.nice.R;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtUsername = findViewById(R.id.txt_username);
                EditText txtPassword = findViewById(R.id.txt_password);

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                SharedPreferencesManager.set(Const.SP_USERNAME, username, LoginActivity.this);
                SharedPreferencesManager.set(Const.SP_PASSWORD, password, LoginActivity.this);
                SharedPreferencesManager.set(Const.SP_LOGGED_IN,
                        Const.SP_LOGGED_IN_TRUE, LoginActivity.this);

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
}