package com.reqres.screens.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;
import com.reqres.R;

public class HomeView {

    HomeActivity homeActivity;
    ImageView iv_loc, iv_profile, users;
    SupportMapFragment mapFragment;
    LinearLayout ll_map;
     RelativeLayout ll_profile, ll_users;
    public HomeView(HomeActivity homeActivity) {

        this.homeActivity = homeActivity;
        init();
    }

    private void init() {
        iv_loc = homeActivity.findViewById(R.id.iv_location);
        iv_profile = homeActivity.findViewById(R.id.iv_profile);
        users = homeActivity.findViewById(R.id.users);
        mapFragment = (SupportMapFragment) homeActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        ll_map = homeActivity.findViewById(R.id.ll_map);
        ll_profile = homeActivity.findViewById(R.id.ll_profile);
        ll_users = homeActivity.findViewById(R.id.ll_users);
    }
}
