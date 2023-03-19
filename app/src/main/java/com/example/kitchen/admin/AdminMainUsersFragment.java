package com.example.kitchen.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kitchen.Controllers.UserController;
import com.example.kitchen.Helper.BitmapHelper;
import com.example.kitchen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class AdminMainUsersFragment extends Fragment {
    View viewInflate;
    AdminMainActivity adminMainActivity;

    TextView lblHeader;
    TextView lblDesc;
    EditText txtSearch;
    ImageView btnSearch;
    LinearLayout linearLayoutUsers;

    public AdminMainUsersFragment(AdminMainActivity adminMainActivityParam) {
        this.adminMainActivity = adminMainActivityParam;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.admin_main_users_fragment_layout, container, false);

        lblHeader = viewInflate.findViewById(R.id.adminMainUsersLblHeader);
        lblDesc = viewInflate.findViewById(R.id.adminMainUsersLblDesc);
        txtSearch = viewInflate.findViewById(R.id.adminMainUsersTxtSearch);
        btnSearch = viewInflate.findViewById(R.id.adminMainUsersBtnSearch);
        linearLayoutUsers = viewInflate.findViewById(R.id.adminMainUsersLinearLayoutUsers);

        LoadData();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadData();
            }
        });

        return viewInflate;
    }

    public void LoadData() {
        UserController userController = new UserController(adminMainActivity);
        linearLayoutUsers.removeAllViews();

        JSONArray jsonArrayUsers = new JSONArray();
        if(!txtSearch.getText().toString().trim().equals("")) {
            jsonArrayUsers = userController.getSearchUser(txtSearch.getText().toString().trim());
        } else {
            jsonArrayUsers = userController.getAllUsers();
        }

        for(int i = 0; i < jsonArrayUsers.length(); i++) {
            try {
                JSONObject objectUser = jsonArrayUsers.getJSONObject(i);
                View viewUsers = LayoutInflater.from(adminMainActivity).inflate(R.layout.user_item_large_layout, null, false);

                if(!objectUser.getString("Image").equals("")) {
                    ((ImageView)viewUsers.findViewById(R.id.userItemLargeLayoutImg)).setImageBitmap(new BitmapHelper().convertToBitmap(objectUser.getString("Image")));
                }

                String userID = objectUser.getString("ID");
                String userName = objectUser.getString("Name");
                ((TextView)viewUsers.findViewById(R.id.userItemLargeLayoutLblName)).setText(userName);
                ((TextView)viewUsers.findViewById(R.id.userItemLargeLayoutLblEmail)).setText(objectUser.getString("Email"));
                ((TextView)viewUsers.findViewById(R.id.userItemLargeLayoutLblRole)).setText(objectUser.getString("Role"));

                JSONArray jsonArrayRole = new JSONArray();
                jsonArrayRole.put(new JSONObject().put("Name", "Chef"));
                jsonArrayRole.put(new JSONObject().put("Name", "Cashier"));

                CardView cardView = (CardView)viewUsers.findViewById(R.id.userItemLargeLayoutCardView);
                cardView.animate().setDuration(600).setStartDelay(120 * i).alpha(1).translationY(0);
                linearLayoutUsers.addView(viewUsers);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
