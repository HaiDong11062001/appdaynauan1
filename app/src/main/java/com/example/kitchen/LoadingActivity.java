package com.example.kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kitchen.Controllers.UserController;
import com.example.kitchen.Helper.BitmapHelper;
import com.example.kitchen.a_user.MemberMainActivity;
import com.example.kitchen.admin.AdminMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends AppCompatActivity {

    TextView lblHeader;
    TextView lblDesc;
    ImageView imgRedMoon;
//hàm khởi tạo được gọi khi tạo Activity. Trong hàm này, truy cập các `TextView` và `ImageView` từ bố cục và bắt đầu chạy Animation.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        lblHeader = findViewById(R.id.loadingLblHeader);
        lblDesc = findViewById(R.id.loadingLblDesc);
        imgRedMoon = findViewById(R.id.loadingImgRedMoon);
//hàm này áp dụng `AccelerateDecelerateInterpolator` vào `ImageView` và chạy một animation để di chuyển từ trái qua phải và xoay vòng quanh `y`.
        LoadAnimation();

        SharedPreferences sharedPref = getSharedPreferences("AppLocalData", Context.MODE_PRIVATE);//SharedPreferences sharedPref = getSharedPreferences("AppLocalData", Context.MODE_PRIVATE);`: khởi tạo một đối tượng SharedPreferences tương ứng với `AppLocalData` để truy cập và chỉnh sửa các giá trị được lưu trữ cục bộ.
//Timer()` và `TimerTask()`: sử dụng để thực hiện một hành động sau khi đếm giờ. Hàm này được sử dụng để xác định xem đây có phải là lần sử dụng đầu tiên hay không.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(sharedPref.getString("IsFirstUse", "true").equals("true")) {
                    // First Install or First Use
                    try {
                        //`JSONArray jsonArrayUsers = new JSONArray();`: khởi tạo một `JSONArray` rỗng để lưu trữ người dùng.
                       //`put()`: phương thức để thêm các thuộc tính vào đối tượng JSON.
                        //`putString()`: phương thức để thêm chuỗi JSON dưới dạng một phần tử vào SharedPreferences.


                        JSONArray jsonArrayUsers = new JSONArray();
                        jsonArrayUsers
                        .put(new JSONObject()
                                .put("ID", 1)
                                .put("Image", "")
                                .put("Name","Admin")
                                .put("Email", "admin@gmail.com")
                                .put("Password", "admin123")
                                .put("Role", "Admin")
                        ).put(new JSONObject()
                                .put("ID", 2)
                                .put("Image", "")
                                .put("Name","Tâm")
                                .put("Email", "tam@gmail.com")
                                .put("Password", "tam123")
                                .put("Role", "Member")
                        );

                        sharedPref.edit()
                                .putString("Users", jsonArrayUsers.toString())
                                .apply();

                        Bitmap[] menuBitmap = {
                                BitmapFactory.decodeResource(getResources(), R.drawable.sushi2),
                                BitmapFactory.decodeResource(getResources(), R.drawable.okonomiyaki),
                                BitmapFactory.decodeResource(getResources(), R.drawable.onigiri),
                                BitmapFactory.decodeResource(getResources(), R.drawable.ramen)
                        };
//`Bitmap[] menuBitmap = {...`: khởi tạo một mảng bitmap và khởi tạo một bitmap ứng với mỗi item trong danh sách các món ăn.
                        BitmapHelper bitmapHelper = new BitmapHelper();
                        //`BitmapHelper bitmapHelper = new BitmapHelper();`: khởi tạo một `BitmapHelper` để chuyển đổi khung hình thành chuỗi cơ sở 64 để lưu trữ trong danh sách `jsonArrayMenu`.



                        JSONArray jsonArrayMenu = new JSONArray();
                        //`JSONArray jsonArrayMenu = new JSONArray();`: khởi tạo một `JSONArray` rỗng để lưu trữ các món ăn.

//`convertToBase64String()`: phương thức để chuyển đổi một `Bitmap` thành chuỗi cơ sở 64 để lưu trữ trong danh sách `jsonArrayMenu`.
//`put("Ingredients", new JSONArray()...`: phần tử dòng đầu tiên với "Ingredients" là một `JSONArray` chứa ID và tên của từng thành phần của một món ăn.
                        //`put("Details", new JSONArray()...`: phần tử dòng thứ hai là một `JSONArray` chứa lời giải thích và chi tiết của món ăn


                        jsonArrayMenu.put(
                                new JSONObject().put("ID", 1).put("Image", bitmapHelper.convertToBase64String(menuBitmap[0])).put("Name", "Sushi").put("Price", 20000).put("Description", "Nấu cơm. Vo sạch gạo Nhật rồi sau đó châm nước vào ngâm 4 tiếng và nấu như bình thường. ...\n" +
                                                "Làm trứng chiên. Đập 2 quả trứng vào bát rồi nêm 1 muỗng cà phê muối, 2 muỗng cà phê đường, ½ muỗng cà phê bột ngọt, 1 muỗng cà phê hạt nêm, sau đó đánh tan. ...\n" +
                                                "Cuộn rong biển. ...\n" +
                                                "Pha nước chấm. ...\n" +
                                                "Thành phẩm.\n")
                                        .put("Ingredients",
                                                new JSONArray()
                                                        .put(new JSONObject().put("ID", 1).put("Name", "Rice"))
                                                        .put(new JSONObject().put("ID", 2).put("Name", "Shrimp"))
                                                        .put(new JSONObject().put("ID", 3).put("Name", "Tuna"))
                                                        .put(new JSONObject().put("ID", 4).put("Name", "Salmon"))
                                                        .put(new JSONObject().put("ID", 5).put("Name", "Egg"))
                                                        .put(new JSONObject().put("ID", 6).put("Name", "Squid"))
                                                        .put(new JSONObject().put("ID", 7).put("Name", "Nori"))
                                        ).put("Details",
                                        new JSONArray()
                                                .put("1 pc of Ika nigiri")
                                                .put("1 pc of Sake nigiri")
                                                .put("1 pc of Tamagoyaki")
                                                .put("1 pc of Unagi")
                                                .put("1 pc of Ebi nigiri")
                                                .put("1 pc of Tobiko nigiri")
                                                .put("1 pc of Uni")
                                                .put("1 pc of Amaebi")
                                                .put("2 pcs of Tekkamaki")
                                                .put("2 pcs of Kappa maki")
                                )
                        );
                        jsonArrayMenu.put(
                                new JSONObject().put("ID", 2).put("Image", bitmapHelper.convertToBase64String(menuBitmap[1])).put("Name", "Okonomiyaki").put("Price", 32000).put("Description", "Okonomiyaki will be the best choice if it's raining. Because the sauce will make you awake. You will be amazed by the taste.")
                                        .put("Ingredients",
                                                new JSONArray()
                                                        .put(new JSONObject().put("ID", 8).put("Name", "Tomato Sauce"))
                                                        .put(new JSONObject().put("ID", 9).put("Name", "Cabbage"))
                                                        .put(new JSONObject().put("ID", 10).put("Name", "Flour"))
                                                        .put(new JSONObject().put("ID", 5).put("Name", "Egg"))
                                                        .put(new JSONObject().put("ID", 11).put("Name", "Mayonaise"))
                                                        .put(new JSONObject().put("ID", 12).put("Name", "Katsuobushi"))
                                        ).put("Details",
                                        new JSONArray()
                                                .put("350 gr of Cabbage")
                                                .put("100 gr of Flour")
                                                .put("120 gr of Katsuobushi")
                                                .put("2 pcs of Egg")
                                )
                        );

                             //`new JSONObject()`: tạo một đối tượng JSON rỗng.
                        jsonArrayMenu.put(
                                new JSONObject().put("ID", 3).put("Image", bitmapHelper.convertToBase64String(menuBitmap[2])).put("Name", "Onigiri").put("Price", 15000).put("Description", "It's made for you who don't want to eat too much. With the vegetables in it, it will helps you on a diet. The crispy nori will make you smile all day long")
                                        .put("Ingredients",
                                                new JSONArray()
                                                        .put(new JSONObject().put("ID", 1).put("Name", "Rice"))
                                                        .put(new JSONObject().put("ID", 7).put("Name", "Nori"))
                                                        .put(new JSONObject().put("ID", 13).put("Name", "Salt"))
                                                        .put(new JSONObject().put("ID", 3).put("Name", "Tuna"))
                                                        .put(new JSONObject().put("ID", 14).put("Name", "Sesame"))
                                        ).put("Details",
                                        new JSONArray()
                                                .put("2 pcs of Onigiri")
                                                .put("150 gr of Rice each")
                                                .put("1 pc of Nori each")
                                )
                        );

                        //`jsonArray.put()`: thêm một đối tượng JSONObject vào mảng JSONArray.
                        jsonArrayMenu.put(
                                new JSONObject().put("ID", 4).put("Image", bitmapHelper.convertToBase64String(menuBitmap[3])).put("Name", "Ramen").put("Price", 27000).put("Description", "The best ramen is here. With our special hand-made noodle and cured egg. The broth used in this ramen is from a 1 years old chicken")
                                        .put("Ingredients",
                                                new JSONArray()
                                                        .put(new JSONObject().put("ID", 15).put("Name", "Noodle"))
                                                        .put(new JSONObject().put("ID", 16).put("Name", "Chicken"))
                                                        .put(new JSONObject().put("ID", 5).put("Name", "Egg"))
                                                        .put(new JSONObject().put("ID", 17).put("Name", "Soy Sauce"))
                                                        .put(new JSONObject().put("ID", 18).put("Name", "Garlic"))
                                                        .put(new JSONObject().put("ID", 19).put("Name", "Spring Onion"))
                                        ).put("Details",
                                        new JSONArray()
                                                .put("400 gr of Noodle")
                                                .put("100 gr of Chicken")
                                                .put("1 pc of Cured Egg")
                                )
                        );
// lưu trữ dữ liệu vào shared preferences
// edit()` để bắt đầu chỉnh sửa dữ liệu trong shared preferences.
//`putString(key, value)` để lưu trữ các dữ liệu được lấy từ `jsonArrayMenu` trong đối tượng `SharedPreferences` với key là "Menus".
//`apply()` được gọi để lưu các thay đổi vào shared preferences.
//`JSONArray jsonArrayIngredients = new JSONArray(); tạo ra một đối tượng JSONArray mới
                        sharedPref.edit()
                                .putString("Menus", jsonArrayMenu.toString())
                                .apply();

                        JSONArray jsonArrayIngredients = new JSONArray();
                        jsonArrayIngredients
                                .put(new JSONObject().put("ID", 1).put("Name", "Rice"))
                                .put(new JSONObject().put("ID", 2).put("Name", "Shrimp"))
                                .put(new JSONObject().put("ID", 3).put("Name", "Tuna"))
                                .put(new JSONObject().put("ID", 4).put("Name", "Salmon"))
                                .put(new JSONObject().put("ID", 5).put("Name", "Egg"))
                                .put(new JSONObject().put("ID", 6).put("Name", "Squid"))
                                .put(new JSONObject().put("ID", 7).put("Name", "Nori"))
                                .put(new JSONObject().put("ID", 8).put("Name", "Tomato Sauce"))
                                .put(new JSONObject().put("ID", 9).put("Name", "Cabbage"))
                                .put(new JSONObject().put("ID", 10).put("Name", "Flour"))
                                .put(new JSONObject().put("ID", 11).put("Name", "Mayonaise"))
                                .put(new JSONObject().put("ID", 12).put("Name", "Katsuobushi"))
                                .put(new JSONObject().put("ID", 13).put("Name", "Salt"))
                                .put(new JSONObject().put("ID", 14).put("Name", "Sesame"))
                                .put(new JSONObject().put("ID", 15).put("Name", "Noodle"))
                                .put(new JSONObject().put("ID", 16).put("Name", "Chicken"))
                                .put(new JSONObject().put("ID", 17).put("Name", "Soy Sauce"))
                                .put(new JSONObject().put("ID", 18).put("Name", "Garlic"))
                                .put(new JSONObject().put("ID", 19).put("Name", "Spring Onion"));
                        //để lưu trữ một chuỗi JSON chứa thông tin về các thành phần nguyên liệu vào `SharedPreferences`. Quá trình này tương tự như đoạn code trước đó, chỉ khác là key được đặt là "Ingredients", và giá trị chuỗi JSON được lấy từ đối tượng `jsonArrayIngredients`.
                       //`try-catch`. Nếu quá trình chuyển đổi JSON sang String bị lỗi, nó sẽ in ra thông tin lỗi bằng phương thức `printStackTrace()`.
                        //tạo ra một Intent và chuyển hướng người dùng đến SplashScreenActivity.

                        sharedPref.edit()
                                .putString("Ingredients", jsonArrayIngredients.toString())
                                .apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(LoadingActivity.this, SplashScreenActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Already used at least once

                    String loggedInUserEmail = sharedPref.getString("LoggedInUserEmail", "defaultValue");
                    if(!loggedInUserEmail.equals("defaultValue")) {
                        // Already logged in before

                        JSONObject objectUser = new UserController(LoadingActivity.this).getUserObjectByEmail(loggedInUserEmail);
                        try {
                            String userRole = objectUser.getString("Role");
                            if(userRole.equals("Admin")) {
                                Intent intent = new Intent(LoadingActivity.this, AdminMainActivity.class);
                                intent.putExtra("Email", loggedInUserEmail);
                                startActivity(intent);
                            }  else {
                                Intent intent = new Intent(LoadingActivity.this, MemberMainActivity.class);
                                intent.putExtra("Email", loggedInUserEmail);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Never logged in before
                        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }

                    finish();
                }
            }
        }, 1800);
    }
//`LoadAnimation()` được viết ra để tạo ra các animation cho các thành phần trên màn hình loading, bao gồm `labelHeader`, `labelDesc` và `imgRedMoon`.
    private void LoadAnimation() {
        lblHeader.animate().translationX(0).alpha(1).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());
        lblDesc.animate().translationX(0).alpha(1).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());
        imgRedMoon.animate().translationY(0).setDuration(600).setStartDelay(500).setInterpolator(new AccelerateDecelerateInterpolator());
    }
}