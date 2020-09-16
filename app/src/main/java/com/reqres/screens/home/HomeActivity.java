package com.reqres.screens.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.reqres.R;
import com.reqres.customviews.CustomProgressDialog;
import com.reqres.gps.GpsFetchActivity;
import com.reqres.screens.home.adapter.UserListAdapter;
import com.reqres.screens.home.model.DataItem;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

import static com.reqres.screens.home.HomeController.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class HomeActivity extends GpsFetchActivity {
    CustomProgressDialog customProgressDialog;
    HomeController homeController;
    HomeView homeView;
    public LinearLayoutManager linearLayoutManager;
    public UserListAdapter userListAdapter;
    public ArrayList<DataItem> user_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        customProgressDialog = new CustomProgressDialog(this);
        homeView = new HomeView(this);
        homeController = new HomeController(this, homeView);
        homeView.iv_loc.performClick();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            } else {
                CropImage.activity()
                        .start(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                homeView.iv_profile_image.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}