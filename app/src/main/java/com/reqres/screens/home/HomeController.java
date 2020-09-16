package com.reqres.screens.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reqres.R;
import com.reqres.gps.GpsFetchCallback;
import com.reqres.interfaces.OnUiClickListener;
import com.reqres.retrofit.RetrofitService;
import com.reqres.retrofit.ServiceGenerator;
import com.reqres.screens.home.adapter.UserListAdapter;
import com.reqres.screens.home.model.UserListResponse;
import com.reqres.screens.login.model.LoginResponse;
import com.reqres.screens.userDetails.UserDetailsActivity;
import com.reqres.screens.utils.Prefs;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class HomeController implements View.OnClickListener/*, OnMapReadyCallback*/ {

    HomeActivity homeActivity;
    HomeView homeView;
    LatLng position;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public HomeController(HomeActivity homeActivity, HomeView homeView) {
        this.homeActivity = homeActivity;
        this.homeView = homeView;
        init();
    }

    private void init() {

        homeView.iv_loc.setOnClickListener(this);
        homeView.iv_profile.setOnClickListener(this);
        homeView.users.setOnClickListener(this);
        homeView.tv_select_profile_pic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_location:
                homeView.ll_profile.setVisibility(View.GONE);
                homeView.ll_map.setVisibility(View.VISIBLE);
                homeView.ll_users.setVisibility(View.GONE);
                homeActivity.initGps(true, new GpsFetchCallback() {
                    @Override
                    public void onLocationUpdate(double latitude, double longitude) {
                        position = new LatLng(latitude, longitude);
                        homeActivity.stopLocationUpdates(false);
                        homeView.mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                googleMap.addMarker(new
                                        MarkerOptions().position(position).visible(true)).showInfoWindow();
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));

                            }
                        });
                    }
                });
                break;

            case R.id.iv_profile:
                homeView.ll_map.setVisibility(View.GONE);
                homeView.ll_users.setVisibility(View.GONE);
                homeView.ll_profile.setVisibility(View.VISIBLE);

                break;

            case R.id.users:
                homeView.ll_users.setVisibility(View.VISIBLE);
                homeView.ll_map.setVisibility(View.GONE);
                homeView.ll_profile.setVisibility(View.GONE);

                callUserListAPI();
                break;

            case R.id.tv_select_profile_pic:

                if (checkAndRequestPermissions(homeActivity)) {
                    CropImage.activity()
                            .start(homeActivity);
                }
                break;
        }

    }

    private void setAdapter() {
        homeView.rv_users.setHasFixedSize(true);
        homeActivity.linearLayoutManager = new LinearLayoutManager(homeActivity);
        homeView.rv_users.setLayoutManager(homeActivity.linearLayoutManager);
        homeActivity.userListAdapter = new UserListAdapter(homeActivity, homeActivity.user_list, (position) -> {

            homeActivity.startActivity(new Intent(homeActivity, UserDetailsActivity.class)
                    .putExtra("userDetail", homeActivity.user_list)
                    .putExtra("position", position));
            homeActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        });
        homeView.rv_users.setAdapter(homeActivity.userListAdapter);
    }

    private void callUserListAPI() {
        homeActivity.customProgressDialog.show();
        RetrofitService apiService = ServiceGenerator.getAPIClient();
        Call<UserListResponse> call = apiService.getUserList(2);
        call.enqueue(new retrofit2.Callback<UserListResponse>() {

            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                homeActivity.customProgressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().getData() != null) {
                        homeActivity.user_list.addAll(response.body().getData());
                        setAdapter();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                homeActivity.customProgressDialog.dismiss();
            }
        });

    }


    public boolean checkAndRequestPermissions(final Activity context) {
        int ExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
