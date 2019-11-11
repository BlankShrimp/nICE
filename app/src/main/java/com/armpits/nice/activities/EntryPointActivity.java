package com.armpits.nice.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.armpits.nice.utils.Const;
import com.armpits.nice.utils.SharedPreferencesManager;

public class EntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startAppIntent;

        // if the user is authenticated, go to the main activity, login otherwise
        if (SharedPreferencesManager.get(Const.SP_LOGGED_IN, this) != null)
            startAppIntent = new Intent(this, MainActivity.class);
        else
            startAppIntent = new Intent(this, LoginActivity.class);

        startActivity(startAppIntent);
        finish();
    }
}
