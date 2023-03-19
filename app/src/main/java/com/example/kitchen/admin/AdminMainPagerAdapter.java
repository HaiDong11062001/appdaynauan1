package com.example.kitchen.admin;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminMainPagerAdapter extends FragmentStateAdapter {
    JSONArray jsonArray;

    public AdminMainPagerAdapter(FragmentActivity fa, JSONArray jsonArrayParam) {
        super(fa);
        this.jsonArray = jsonArrayParam;
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragReturn = null;

        try {
            fragReturn = (Fragment) jsonArray.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fragReturn;
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}
