package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends AppCompatActivity {

    RecyclerView recView;
    TextView btnNextFinish;
    LinearLayout linearLayoutDot;
    int selectedPosition = 0;
    JSONArray splashScreenJSONArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        recView = findViewById(R.id.splashScreenRecView);
        btnNextFinish = findViewById(R.id.splashScreenBtnNextFinish);
        linearLayoutDot = findViewById(R.id.splashScreenLinearLayoutDot);

        LinearLayoutManager layoutManager = new LinearLayoutManager(SplashScreenActivity.this, RecyclerView.HORIZONTAL, false);
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject().put("Image", R.drawable.ic_japan).put("Title", "What is App ?").put("Desc", "Here you can find all kinds of recipes"));
            jsonArray.put(new JSONObject().put("Image", R.drawable.ic_chef).put("Title", "Food").put("Desc", "You can absolutely become a professional chef at home"));
            jsonArray.put(new JSONObject().put("Image", R.drawable.ic_confirmed).put("Title", "Fast").put("Desc", "You can use the app anywhere, anytime"));

            splashScreenJSONArray = jsonArray;

            recView.setAdapter(new SplashScreenRecViewAdapter(SplashScreenActivity.this, jsonArray));
            recView.setLayoutManager(layoutManager);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recView);

            for(int i = 0; i < splashScreenJSONArray.length(); i++) {
                View viewDot = LayoutInflater.from(SplashScreenActivity.this).inflate(R.layout.splash_screen_dot_layout, null, false);

                ImageView imgSliderDot = (ImageView)viewDot.findViewById(R.id.splashScreenDotLayoutImg);

                if(i == selectedPosition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imgSliderDot.setImageDrawable(getDrawable(R.drawable.dot_selected));
                    } else {
                        imgSliderDot.setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
                    }
                }

                imgSliderDot.animate().setDuration(600).setStartDelay(i * 120).alpha(1).translationY(0);

                linearLayoutDot.addView(viewDot);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    selectedPosition = layoutManager.findLastVisibleItemPosition();
                    LoadButtonNextFinish();
                }
            });
        } else {
            recView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    selectedPosition = layoutManager.findLastVisibleItemPosition();
                    LoadButtonNextFinish();
                }
            });
        }

        btnNextFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedPosition < 2) {
                    selectedPosition++;
                    recView.smoothScrollToPosition(selectedPosition);
                    LoadButtonNextFinish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                    SharedPreferences sharedPref = getSharedPreferences("AppLocalData", Context.MODE_PRIVATE);
                    sharedPref.edit()
                            .putString("IsFirstUse", "false")
                            .apply();
                }
            }
        });
    }

    private void LoadButtonNextFinish() {
        if(selectedPosition == 2) {
            btnNextFinish.setText("Finish");
        } else {
            btnNextFinish.setText("Next >");
        }

        LoadSelectedSliderDot();
    }

    private void LoadSelectedSliderDot() {
        for(int i = 0; i < splashScreenJSONArray.length(); i++) {
            View viewDot = linearLayoutDot.getChildAt(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ((ImageView)viewDot.findViewById(R.id.splashScreenDotLayoutImg)).setImageDrawable(getDrawable(R.drawable.dot_unselected));
            } else {
                ((ImageView)viewDot.findViewById(R.id.splashScreenDotLayoutImg)).setImageDrawable(getResources().getDrawable(R.drawable.dot_unselected));
            }
        }

        View viewDotSelected = linearLayoutDot.getChildAt(selectedPosition);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((ImageView)viewDotSelected.findViewById(R.id.splashScreenDotLayoutImg)).setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
        }
    }
}