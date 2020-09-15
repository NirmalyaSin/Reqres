package com.reqres.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.reqres.R;
import com.reqres.screens.home.HomeActivity;
import com.reqres.screens.login.LoginActivity;
import com.reqres.screens.utils.Prefs;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        switchscreen();
    }

    public void switchscreen(){
        Intent mainIntent;
        if(!Prefs.getInstance().getString(Prefs.PREFS_KEY_ACCESS_TOKEN).equals("")){
            mainIntent = new Intent(this, HomeActivity.class);
        }else {
            mainIntent = new Intent(this, LoginActivity.class);
        }
        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finishAffinity();
            SplashActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }, SPLASH_DISPLAY_LENGTH);
    }
}
