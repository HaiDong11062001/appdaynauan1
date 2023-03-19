package com.example.kitchen.a_user.dish;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchen.R;

class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    Context context;
    JSONArray jsonArray;
    View viewInflate;

    public IngredientsAdapter(Context contextParam, JSONArray jsonArrayParam) {
        this.context = contextParam;
        this.jsonArray = jsonArrayParam;
    }

    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewInflate = LayoutInflater.from(context).inflate(R.layout.ingredients_layout, parent, false);

        return new ViewHolder(viewInflate);
    }

    @Override
    public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {
        try {
            JSONObject object = jsonArray.getJSONObject(position);

            ((TextView)viewInflate.findViewById(R.id.ingredientsLayoutLblName)).setText(object.getString("Name"));
            ((ImageView)viewInflate.findViewById(R.id.ingredientsLayoutImg)).setImageResource(getImage(object.getString("Name")));
            ((CardView)viewInflate.findViewById(R.id.ingredientsLayoutCardView)).animate().setDuration(600).setStartDelay(200).alpha(1).translationY(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getImage(String s){
        int anh=R.drawable.icon_food;
        if(s.equals("Rice")){
            anh=R.drawable.rice;
        }else if(s.equals("Shrimp")){
            anh=R.drawable.shrimp;
        }else if(s.equals("Tuna")){
            anh=R.drawable.tuna;
        }else if(s.equals("Salmon")){
            anh=R.drawable.salmon;
        }else if(s.equals("Egg")){
            anh=R.drawable.egg;
        }else if(s.equals("Squid")){
            anh=R.drawable.squid;
        }else if(s.equals("Nori")){
            anh=R.drawable.nori;
        }else if(s.equals("Tomato Sauce")){
            anh=R.drawable.tomatosauce;
        }else if(s.equals("Cabbage")){
            anh=R.drawable.cabbage;
        }else if(s.equals("Flour")){
            anh=R.drawable.flour;
        }else if(s.equals("Mayonaise")){
            anh=R.drawable.mayonaise;
        }else if(s.equals("Katsuobushi")){
            anh=R.drawable.katsuobushi;
        }else if(s.equals("Salt")){
            anh=R.drawable.salt;
        }else if(s.equals("Sesame")){
            anh=R.drawable.sesame;
        }else if(s.equals("Noodle")){
            anh=R.drawable.noodle;
        }else if(s.equals("Chicken")){
            anh=R.drawable.chicken;
        }else if(s.equals("Soy Sauce")){
            anh=R.drawable.soysauce;
        }else if(s.equals("Garlic")){
            anh=R.drawable.garlic;
        }else if(s.equals("Spring Onion")){
            anh=R.drawable.springonion;
        }
        return anh;
    }
    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
