package com.example.kitchen;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
//class cha và Adapter cung cấp dữ liệu cho RecyclerView.
class AddEditMenuIngredientsAdapter extends RecyclerView.Adapter<AddEditMenuIngredientsAdapter.ViewHolder> {

    // khởi tạo biến để sử dụng ở các hàm khác.
    AddEditMenuActivity addEditMenuActivity;
    JSONArray jsonArray;
    View viewInflate;

    //contructor của class `AddEditMenuIngredientsAdapter`, lấy `addEditMenuActivity` và `jsonArray` là các tham số đầu vào.
    public AddEditMenuIngredientsAdapter(AddEditMenuActivity addEditMenuActivityParam, JSONArray jsonArrayParam) {
        this.addEditMenuActivity = addEditMenuActivityParam;
        this.jsonArray = jsonArrayParam;
    }
//tạo ViewHolder cho phần tử trong RecyclerView
    @Override
    public AddEditMenuIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewInflate = LayoutInflater.from(addEditMenuActivity).inflate(R.layout.add_edit_menu_ingredients_layout, parent, false);

        return new ViewHolder(viewInflate);
    }

    //khởi tạo và gán giá trị cho các thành phần con của item. Hàm này cũng bao gồm logic xóa phần tử khi người dùng nhấn vào nút xóa.
    @Override
    public void onBindViewHolder(AddEditMenuIngredientsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            JSONObject object = jsonArray.getJSONObject(position);

            ((ImageView)viewInflate.findViewById(R.id.addEditMenuIngredientsLayoutImg)).setImageResource(getImage(object.getString("Name")));
            ((TextView)viewInflate.findViewById(R.id.addEditMenuIngredientsLayoutLblName)).setText(object.getString("Name"));
            ((CardView)viewInflate.findViewById(R.id.addEditMenuIngredientsLayoutCardView)).animate().setDuration(600).setStartDelay(200).alpha(1).translationY(0);
            FloatingActionButton btnDelete = (FloatingActionButton)viewInflate.findViewById(R.id.addEditMenuIngredientsBtnDelete);
            btnDelete.animate().setDuration(600).setStartDelay(200).alpha(1).translationY(0);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addEditMenuActivity.jsonArrayMenuIngredients.remove(position);
                    addEditMenuActivity.LoadDataIngredients();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //: phương thức trả về hình ảnh ứng với mỗi thành phần trong một món ăn.
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
//trả về số lượng các thành phần trong danh sách `jsonArray`.
    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    //view holder cho RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
