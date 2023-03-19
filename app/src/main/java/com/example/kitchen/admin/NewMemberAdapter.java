package com.example.kitchen.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitchen.Helper.BitmapHelper;
import com.example.kitchen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

class NewMemberAdapter extends RecyclerView.Adapter<NewMemberAdapter.ViewHolder> {
    AdminMainActivity adminMainActivity;
    AdminMainHomeFragment adminMainHomeFragment;
    JSONArray jsonArray;
    View viewInflate;

    public NewMemberAdapter(AdminMainActivity adminMainActivityParam, JSONArray jsonArrayParam, AdminMainHomeFragment adminMainHomeFragmentParam) {
        this.adminMainActivity = adminMainActivityParam;
        this.jsonArray = jsonArrayParam;
        this.adminMainHomeFragment = adminMainHomeFragmentParam;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewInflate = LayoutInflater.from(adminMainActivity).inflate(R.layout.new_member_layout, parent, false);

        return new ViewHolder(viewInflate);
    }

    @Override
    public void onBindViewHolder(NewMemberAdapter.ViewHolder holder, int position) {
        try {
            JSONObject object = jsonArray.getJSONObject(position);
            if(!object.getString("Image").equals("")) {
                ((ImageView)viewInflate.findViewById(R.id.newMemberLayoutImg)).setImageBitmap(new BitmapHelper().convertToBitmap(object.getString("Image")));
            }

            String memberName = object.getString("Name");
            String userID = object.getString("ID");
            ((TextView)viewInflate.findViewById(R.id.newMemberLayoutLblName)).setText(memberName);
            ((TextView)viewInflate.findViewById(R.id.newMemberLayoutLblRole)).setText(object.getString("Role"));

            JSONArray jsonArrayRole = new JSONArray();
            jsonArrayRole.put(new JSONObject().put("Name", "Chef"));
            jsonArrayRole.put(new JSONObject().put("Name", "Cashier"));

            ConstraintLayout constraintLayout = (ConstraintLayout)viewInflate.findViewById(R.id.newMemberConstraintLayout);
            constraintLayout.animate().setDuration(600).alpha(1).translationY(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
