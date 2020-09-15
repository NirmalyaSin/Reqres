package com.reqres.screens.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.reqres.R;
import com.reqres.customviews.CustomProgressDialog;

public class LoginActivity extends AppCompatActivity {

    LoginView loginView;
    LoginController loginController;
    CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        customProgressDialog = new CustomProgressDialog(this);
        loginView = new LoginView(this);
        loginController = new LoginController(this,loginView);
    }
}