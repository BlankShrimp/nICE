package com.armpits.nice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.armpits.nice.R;
import com.armpits.nice.networking.Parser;
import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // allow internet
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(v -> {
            EditText txtUsername = findViewById(R.id.txt_username);
            EditText txtPassword = findViewById(R.id.txt_password);

            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();

            // check if username and password work
            boolean credentialsAreValid = tryToLogin(username, password);
            if (!credentialsAreValid) return;

            SharedPreferencesManager.set(Const.SP_USERNAME, username, LoginActivity.this);
            SharedPreferencesManager.set(Const.SP_PASSWORD, password, LoginActivity.this);
            SharedPreferencesManager.set(Const.SP_LOGGED_IN,
                    Const.SP_LOGGED_IN_TRUE, LoginActivity.this);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        });
    }

    private boolean tryToLogin(String username, String password) {
        String[] response = Parser.login(username, password);
        String status = response[0];

        if (status.equals("Failed")) {
            Toast.makeText(LoginActivity.this, response[1], Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}