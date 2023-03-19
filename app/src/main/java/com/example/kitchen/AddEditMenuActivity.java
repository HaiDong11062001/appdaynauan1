package com.example.kitchen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchen.Controllers.IngredientsController;
import com.example.kitchen.Controllers.MenuController;
import com.example.kitchen.Helper.BitmapHelper;
import com.example.kitchen.admin.AdminMainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddEditMenuActivity extends AppCompatActivity {

    TextView btnBack;
    Button btnSave;
    Button btnCancel;
    FloatingActionButton btnAddImage;
    Spinner comboIngredients;
    TextView btnAddIngredients;
    TextView btnAddDetails;
    FloatingActionButton btnDeleteMenu;

    ImageView imgTop;
    ImageView imgMenu;
    TextView lblName;
    EditText txtName;
    TextView lblPrice;
    EditText txtPrice;
    TextView lblIngredients;
    RecyclerView recViewIngredients;
    TextView lblDescription;
    EditText txtDescription;
    TextView lblDetails;
    TextView lblDetailsDesc;
    EditText txtDetails;
    LinearLayout linearLayoutDetails;

    JSONArray jsonArrayQueryIngredients;

    JSONArray jsonArrayMenuIngredients = new JSONArray();
    JSONArray jsonArrayMenuDetails = new JSONArray();

    Bitmap menuImageBitmap = null;
    MenuController menuController;
    BitmapHelper bitmapHelper = new BitmapHelper();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 501) {
                Uri dataUri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(dataUri, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String path = cursor.getString(columnIndex);

                menuImageBitmap = BitmapFactory.decodeFile(path);
                imgMenu.setImageBitmap(menuImageBitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_menu);

        btnBack = findViewById(R.id.addEditMenuBtnBack);
        btnSave = findViewById(R.id.addEditMenuBtnSave);
        btnCancel = findViewById(R.id.addEditMenuBtnCancel);
        btnAddImage = findViewById(R.id.addEditMenuBtnAddImage);
        comboIngredients = findViewById(R.id.addEditMenuComboIngredients);
        btnAddIngredients = findViewById(R.id.addEditMenuBtnAddIngredients);
        btnAddDetails = findViewById(R.id.addEditMenuBtnAddDetails);
        btnDeleteMenu = findViewById(R.id.addEditMenuBtnDeleteMenu);
        imgMenu = findViewById(R.id.addEditMenuImg);
        imgTop = findViewById(R.id.addEditMenuImgTop);
        lblName = findViewById(R.id.addEditMenuLblName);
        txtName = findViewById(R.id.addEditMenuTxtName);
        lblPrice = findViewById(R.id.addEditMenuLblPrice);
        txtPrice = findViewById(R.id.addEditMenuTxtPrice);
        lblIngredients = findViewById(R.id.addEditMenuLblIngredients);
        recViewIngredients = findViewById(R.id.addEditMenuRecViewIngredients);
        lblDescription = findViewById(R.id.addEditMenuLblDescription);
        txtDescription = findViewById(R.id.addEditMenuTxtDescription);
        lblDetails = findViewById(R.id.addEditMenuLblDetails);
        lblDetailsDesc = findViewById(R.id.addEditMenuLblDetailsDesc);
        txtDetails = findViewById(R.id.addEditMenuTxtDetails);
        linearLayoutDetails = findViewById(R.id.addEditMenuLinearLayoutDetails);

        menuController = new MenuController(AddEditMenuActivity.this);

        LoadAnimation();

        String menuID = getIntent().getStringExtra("MenuID");
        if(menuID != null) {
            LoadData(menuID);
        } else {
            btnDeleteMenu.setVisibility(View.GONE);
        }

        LoadComboIngredients();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditMenuActivity.super.onBackPressed();
            }
        });

        btnDeleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddEditMenuActivity.this);
                View viewDialog = LayoutInflater.from(AddEditMenuActivity.this).inflate(R.layout.delete_confirmation_popup_layout, null, false);

                AlertDialog dialog = builder.create();
                dialog.setView(viewDialog);
                dialog.setCancelable(true);

                ((TextView)viewDialog.findViewById(R.id.deleteConfirmationPopupLayoutLblHeader)).setText("Delete Menu");
                ((TextView)viewDialog.findViewById(R.id.deleteConfirmationPopupLayoutLblDesc)).setText("Delete a menu will permanently remove it from your menu list.");

                Button btnPositive = (Button)viewDialog.findViewById(R.id.deleteConfirmationPopupLayoutBtnPositive);
                btnPositive.setText("Yes, Delete Menu");
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        menuController.deleteMenu(menuID);
                        dialog.dismiss();

                        Intent intent = new Intent(AddEditMenuActivity.this, AdminMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        finish();
                    }
                });

                Button btnNegative = (Button)viewDialog.findViewById(R.id.deleteConfirmationPopupLayoutBtnNegative);
                btnNegative.setText("No, Keep Menu");
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(menuImageBitmap != null) {
                    if(!txtName.getText().toString().trim().equals("")) {
                        if(!txtPrice.getText().toString().trim().equals("")) {
                            if(TextUtils.isDigitsOnly(txtPrice.getText().toString().trim())) {
                                int price = Integer.parseInt(txtPrice.getText().toString().trim());

                                if(price > 0) {
                                    if(jsonArrayMenuIngredients.length() > 0) {
                                        if(!txtDescription.getText().toString().trim().equals("")) {
                                            if(jsonArrayMenuDetails.length() > 0) {
                                                if(menuID != null) {
                                                    // Update
                                                    try {
                                                        JSONObject objectMenuUpdate = new JSONObject();
                                                        objectMenuUpdate.put("ID", menuID).put("Image", bitmapHelper.convertToBase64String(menuImageBitmap)).put("Name", txtName.getText().toString().trim()).put("Price", price).put("Description", txtDescription.getText().toString().trim())
                                                                .put("Ingredients", jsonArrayMenuIngredients)
                                                                .put("Details", jsonArrayMenuDetails);

                                                        menuController.editMenu(menuID, objectMenuUpdate);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    // Insert
                                                    try {
                                                        JSONArray jsonArrayAllMenus = menuController.getMenus();
                                                        int latestID = jsonArrayAllMenus.getJSONObject(jsonArrayAllMenus.length() - 1).getInt("ID");

                                                        int insertID = latestID + 1;
                                                        JSONObject objectMenuInsert = new JSONObject();
                                                        objectMenuInsert.put("ID", insertID).put("Image", bitmapHelper.convertToBase64String(menuImageBitmap)).put("Name", txtName.getText().toString().trim()).put("Price", price).put("Description", txtDescription.getText().toString().trim())
                                                                .put("Ingredients", jsonArrayMenuIngredients)
                                                                .put("Details", jsonArrayMenuDetails);

                                                        menuController.addMenu(objectMenuInsert);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                Intent intent = new Intent(AddEditMenuActivity.this, AdminMainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);

                                                finish();
                                            } else {
                                                Toast.makeText(AddEditMenuActivity.this, "Please add at least one detail to the menu", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            txtDescription.setError("Fill up menu Processing method");
                                        }
                                    } else {
                                        Toast.makeText(AddEditMenuActivity.this, "Please add at least one ingredient to the menu", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    txtPrice.setError("Raw material price must be more than zero");
                                    txtPrice.requestFocus();
                                }
                            } else {
                                txtPrice.setError("Raw material price must be a number without decimal");
                                txtPrice.requestFocus();
                            }
                        } else {
                            txtPrice.setError("Fill up menu Raw material price");
                            txtPrice.requestFocus();
                        }
                    } else {
                        txtName.setError("Fill up menu name");
                        txtName.requestFocus();
                    }
                } else {
                    Toast.makeText(AddEditMenuActivity.this, "Please select a menu image", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditMenuActivity.super.onBackPressed();
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadExternalPermitted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 501);
                }
            }
        });

        btnAddIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject objectIngredientSelected = jsonArrayQueryIngredients.getJSONObject(comboIngredients.getSelectedItemPosition());
                    boolean isThereThatIngredientAlready = false;
                    for(int i = 0; i < jsonArrayMenuIngredients.length(); i++) {
                        JSONObject objectIngredient = jsonArrayMenuIngredients.getJSONObject(i);
                        if(objectIngredient.getInt("ID") == objectIngredientSelected.getInt("ID")) {
                            isThereThatIngredientAlready = true;
                            break;
                        }
                    }

                    if(!isThereThatIngredientAlready) {
                        jsonArrayMenuIngredients.put(objectIngredientSelected);
                        LoadDataIngredients();
                    } else {
                        Toast.makeText(AddEditMenuActivity.this, "This ingredients is already added to the menu", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btnAddDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtDetails.getText().toString().trim().equals("")) {
                    jsonArrayMenuDetails.put(txtDetails.getText().toString().trim());

                    LoadDataDetails();

                    txtDetails.setText("");
                    txtDetails.requestFocus();
                } else {
                    txtDetails.setError("Fill up menu details to add");
                    txtDetails.requestFocus();
                }
            }
        });
    }

    private void LoadComboIngredients() {
        jsonArrayQueryIngredients = new IngredientsController(AddEditMenuActivity.this).getIngredients();
        comboIngredients.setAdapter(new ComboBoxAdapter(AddEditMenuActivity.this, jsonArrayQueryIngredients));
    }

    private void LoadData(String menuID) {
        try {
            JSONObject objectMenu = menuController.getMenusWhere("ID", menuID).getJSONObject(0);
            txtName.setText(objectMenu.getString("Name"));
            txtPrice.setText(objectMenu.getString("Price"));
            txtDescription.setText(objectMenu.getString("Description"));

            menuImageBitmap = bitmapHelper.convertToBitmap(objectMenu.getString("Image"));
            imgMenu.setImageBitmap(menuImageBitmap);

            jsonArrayMenuIngredients = objectMenu.getJSONArray("Ingredients");
            jsonArrayMenuDetails = objectMenu.getJSONArray("Details");

            LoadDataIngredients();
            LoadDataDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isReadExternalPermitted() {
        if(ActivityCompat.checkSelfPermission(AddEditMenuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(AddEditMenuActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, 500);
            return false;
        }
    }

    public void LoadDataIngredients() {
        recViewIngredients.setAdapter(new AddEditMenuIngredientsAdapter(AddEditMenuActivity.this, jsonArrayMenuIngredients));
        recViewIngredients.setLayoutManager(new LinearLayoutManager(AddEditMenuActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    public void LoadDataDetails() {
        linearLayoutDetails.removeAllViews();

        for(int i = 0; i < jsonArrayMenuDetails.length(); i++) {
            try {
                View viewDetails = LayoutInflater.from(AddEditMenuActivity.this).inflate(R.layout.add_edit_menu_details_layout, null, false);

                ((TextView)viewDetails.findViewById(R.id.addEditMenuDetailsLayoutLbl)).setText(jsonArrayMenuDetails.getString(i));

                int index = i;
                ((TextView)viewDetails.findViewById(R.id.addEditMenuDetailsLayoutBtnDelete)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jsonArrayMenuDetails.remove(index);
                        LoadDataDetails();
                    }
                });

                linearLayoutDetails.addView(viewDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void LoadAnimation() {
        imgTop.animate().setDuration(800).alpha(1).translationY(0).setInterpolator(new DecelerateInterpolator());

        btnBack.animate().setDuration(600).setStartDelay(120).alpha(1).translationY(0);
        btnDeleteMenu.animate().setDuration(600).setStartDelay(240).alpha(1).translationY(0);
        imgMenu.animate().setDuration(600).setStartDelay(360).alpha(1).translationY(0);
        btnAddImage.animate().setDuration(600).setStartDelay(480).alpha(1).translationY(0);

        lblName.animate().setDuration(600).setStartDelay(600).alpha(1).translationY(0);
        txtName.animate().setDuration(600).setStartDelay(720).alpha(1).translationX(0);
        lblPrice.animate().setDuration(600).setStartDelay(840).alpha(1).translationY(0);
        txtPrice.animate().setDuration(600).setStartDelay(960).alpha(1).translationX(0);
        lblIngredients.animate().setDuration(600).setStartDelay(1080).alpha(1).translationY(0);
        ((LinearLayout)findViewById(R.id.addEditMenuIngredientsControls)).animate().setDuration(600).setStartDelay(1200).alpha(1).translationX(0);
        lblDescription.animate().setDuration(600).setStartDelay(1320).alpha(1).translationY(0);
        txtDescription.animate().setDuration(600).setStartDelay(1440).alpha(1).translationX(0);
        lblDetails.animate().setDuration(600).setStartDelay(1560).alpha(1).translationY(0);
        lblDetailsDesc.animate().setDuration(600).setStartDelay(1680).alpha(1).translationY(0);
        ((LinearLayout)findViewById(R.id.addEditMenuDetailsControls)).animate().setDuration(600).setStartDelay(1800).alpha(1).translationX(0);
        btnSave.animate().setDuration(600).setStartDelay(1920).alpha(1).translationX(0);
        btnCancel.animate().setDuration(600).setStartDelay(2040).alpha(1).translationX(0);
    }
}