package com.reqres.screens.home;

import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reqres.R;
import com.reqres.gps.GpsFetchCallback;

public class HomeController implements View.OnClickListener/*, OnMapReadyCallback*/ {

    HomeActivity homeActivity;
    HomeView homeView;
    LatLng position;

    public HomeController(HomeActivity homeActivity, HomeView homeView) {
        this.homeActivity = homeActivity;
        this.homeView = homeView;
        init();
    }

    private void init() {

        homeView.iv_loc.setOnClickListener(this);
        homeView.iv_profile.setOnClickListener(this);
        homeView.users.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_location:
                homeView.ll_profile.setVisibility(View.GONE);
                homeView.ll_map.setVisibility(View.VISIBLE);
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

                break;
        }

    }
}
