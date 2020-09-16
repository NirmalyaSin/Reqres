package com.reqres.screens.userDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.reqres.R;
import com.reqres.screens.home.model.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsActivity extends AppCompatActivity {

    TextView user_email, user_name;
    CircleImageView user_image;

   private ArrayList<DataItem> userDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        user_email = findViewById(R.id.tv_email);
        user_name = findViewById(R.id.tv_name);
        user_image = findViewById(R.id.iv_user_image);

        userDetails = (ArrayList<DataItem>) getIntent().getSerializableExtra("userDetail");
        if(getIntent().hasExtra("position")){
            user_name.setText(userDetails.get(getIntent().getIntExtra("position",0)).getFirstName()+
                    " "+ userDetails.get(getIntent().getIntExtra("position",0)).getLastName());
            user_email.setText(userDetails.get(getIntent().getIntExtra("position",0)).getEmail());
            Picasso.get()
                    .load(userDetails.get(getIntent().getIntExtra("position",0)).getAvatar())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .centerCrop()
                    .into(user_image);
        }
    }
}