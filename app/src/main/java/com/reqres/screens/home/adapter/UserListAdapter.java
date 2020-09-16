package com.reqres.screens.home.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reqres.R;
import com.reqres.interfaces.OnUiClickListener;
import com.reqres.screens.home.model.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListHolder> {

    Activity mActivity;
    ArrayList<DataItem> userList;
    OnUiClickListener onUiClickListener;

    public UserListAdapter(Activity mActivity, ArrayList<DataItem> dataItems, OnUiClickListener onUiClickListener) {
        this.mActivity = mActivity;
        this.userList = dataItems;
        this.onUiClickListener = onUiClickListener;
    }

    @NonNull
    @Override
    public UserListAdapter.UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_user, parent, false);

        return new UserListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserListHolder holder, int position) {
        if (!userList.get(position).getAvatar().equals("")) {
            Picasso.get()
                    .load(userList.get(position).getAvatar())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .fit()
                    .centerCrop()
                    .into(holder.iv_user_image);
        }
        holder.tv_name.setText(userList.get(position).getFirstName() + " " + userList.get(position).getLastName());
        holder.tv_email_id.setText(userList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    class UserListHolder extends RecyclerView.ViewHolder {

        ImageView iv_user_image;
        TextView tv_name, tv_email_id;
        RelativeLayout rl_content;
        public UserListHolder(@NonNull View itemView) {
            super(itemView);
            iv_user_image = itemView.findViewById(R.id.iv_user_image);
            tv_email_id = itemView.findViewById(R.id.tv_email_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            rl_content = itemView.findViewById(R.id.rl_content);

            rl_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUiClickListener.onUiClick(getAdapterPosition());
                }
            });
        }
    }
}
