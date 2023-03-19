package com.example.kitchen.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitchen.Controllers.UserController;
import com.example.kitchen.ProfileActivity;
import com.example.kitchen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminMainHomeFragment extends Fragment {
    AdminMainActivity adminMainActivity;
    View viewInflate;
    ImageView imgTop;
    TextView lblHello;
    ImageView btnProfile;
    TextView lblName;
    TextView lblRole;
    TextView lblNewMember;
    RecyclerView recViewNewMember;
    UserController userController;

    public AdminMainHomeFragment(AdminMainActivity adminMainActivityParam) {
        this.adminMainActivity = adminMainActivityParam;
        this.userController = new UserController(adminMainActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {
            // Update Profile
            LoadDataAdmin();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.admin_main_home_fragment_layout, container, false);

        imgTop = viewInflate.findViewById(R.id.adminMainHomeImgTop);
        lblHello = viewInflate.findViewById(R.id.adminMainHomeLblHello);
        btnProfile = viewInflate.findViewById(R.id.adminMainHomeBtnProfile);
        lblName = viewInflate.findViewById(R.id.adminMainHomeLblName);
        lblRole = viewInflate.findViewById(R.id.adminMainHomeLblRole);
        lblNewMember = viewInflate.findViewById(R.id.adminMainHomeLblNewMember);
        recViewNewMember = viewInflate.findViewById(R.id.adminMainHomeRecViewNewMembers);

        LoadAnimation();
        LoadDataAdmin();
        LoadDataNewMembers();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminMainActivity, ProfileActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        return viewInflate;
    }

    public void LoadDataNewMembers() {
        JSONArray jsonArrayMember = userController.getUserWhere("Role", "Member");
        recViewNewMember.setAdapter(new NewMemberAdapter(adminMainActivity, jsonArrayMember, AdminMainHomeFragment.this));
        recViewNewMember.setLayoutManager(new LinearLayoutManager(adminMainActivity, LinearLayoutManager.HORIZONTAL, false));
    }

    private void LoadDataAdmin() {
        try {
            JSONObject objectUser = userController.getLoggedInUserObject();
            lblName.setText(objectUser.getString("Name"));
            lblRole.setText(objectUser.getString("Role"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadAnimation() {
        imgTop.animate().setDuration(800).alpha(1).translationY(0).setInterpolator(new DecelerateInterpolator());

        lblHello.animate().setDuration(600).setStartDelay(120).alpha(1).translationY(0);
        btnProfile.animate().setDuration(600).setStartDelay(240).alpha(1).translationY(0);
        lblName.animate().setDuration(600).setStartDelay(360).alpha(1).translationX(0);
        lblRole.animate().setDuration(600).setStartDelay(480).alpha(1).translationX(0);

        lblNewMember.animate().setDuration(600).setStartDelay(600).alpha(1).translationX(0);
    }
}
