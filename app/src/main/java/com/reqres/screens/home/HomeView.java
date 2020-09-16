package com.reqres.screens.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.SupportMapFragment;
import com.reqres.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeView {

    HomeActivity homeActivity;
    ImageView iv_loc, iv_profile, users;
    SupportMapFragment mapFragment;
    LinearLayout ll_map;
     RelativeLayout ll_profile, ll_users;
     TextView tv_select_profile_pic;
     CircleImageView iv_profile_image;
     RecyclerView rv_users;

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
        tv_select_profile_pic = homeActivity.findViewById(R.id.tv_select_profile_pic);
        iv_profile_image = homeActivity.findViewById(R.id.iv_profile_image);
        rv_users = homeActivity.findViewById(R.id.rv_users);
    }
}
