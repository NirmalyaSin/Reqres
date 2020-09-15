package com.reqres.screens.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.reqres.R;
import com.reqres.customviews.CustomProgressDialog;
import com.reqres.gps.GpsFetchActivity;

public class HomeActivity extends GpsFetchActivity {
    CustomProgressDialog customProgressDialog;
    HomeController homeController;
    HomeView homeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        customProgressDialog = new CustomProgressDialog(this);
        homeView = new HomeView(this);
        homeController = new HomeController(this, homeView);
        homeView.iv_loc.performClick();
    }
}