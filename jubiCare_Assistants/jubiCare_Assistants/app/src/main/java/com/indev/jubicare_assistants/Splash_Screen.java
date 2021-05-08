package com.indev.jubicare_assistants;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.indev.jubicare_assistants.sqllite_db.SqliteHelper;

public class Splash_Screen extends AppCompatActivity {
SharedPrefHelper sharedPrefHelper;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();
        sqliteHelper = new SqliteHelper(this);
        sqliteHelper.openDataBase();
        sharedPrefHelper = new SharedPrefHelper(this);
        String Login=sharedPrefHelper.getString("is_login","");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    if (Login.equals("")) {
                        DataDownload dataDownload = new DataDownload();
                        dataDownload.getMasterTables(Splash_Screen.this);
                        Intent intent = new Intent(Splash_Screen.this, LoginAcivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(Splash_Screen.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }


            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
