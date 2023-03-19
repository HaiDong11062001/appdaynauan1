package com.example.kitchen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kitchen.Controllers.UserController;
import com.example.kitchen.Helper.BitmapHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgBackgroundTop;
    CardView cardViewBackground;
    ImageView imgProfile;
    FloatingActionButton btnUploadImage;
    Button btnLogout;
    TextView lblName;
    TextView lblRole;
    TextView lblEmail;
    TextView btnBack;
    Button btnEditProfile;
    ImageView imgBtnEditProfile;

    JSONObject objectUser = new JSONObject();
    UserController userController;

    Bitmap bitmapProfileImage = null;

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

                bitmapProfileImage = BitmapFactory.decodeFile(path);
                imgProfile.setImageBitmap(bitmapProfileImage);

                userController.updateUserImage(bitmapProfileImage);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userController = new UserController(ProfileActivity.this);

        imgBackgroundTop = findViewById(R.id.profileImgTop);
        cardViewBackground = findViewById(R.id.profileCardView);
        btnUploadImage = findViewById(R.id.profileBtnUploadImage);
        imgProfile = findViewById(R.id.profileImgProfile);
        btnLogout = findViewById(R.id.profileBtnLogout);
        lblName = findViewById(R.id.profileLblName);
        lblRole = findViewById(R.id.profileLblRole);
        lblEmail = findViewById(R.id.profileLblEmail);
        btnBack = findViewById(R.id.profileBtnBack);
        btnEditProfile = findViewById(R.id.profileBtnEditProfile);
        imgBtnEditProfile = findViewById(R.id.profileImgEditProfile);

        LoadData();
        LoadAnimation();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileActivity.super.onBackPressed();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReadExternalPermitted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 501);
                }
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                View viewDialog = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.edit_profile_popup_layout, null, false);

                builder.setView(viewDialog);
                builder.setCancelable(true);

                AlertDialog dialog = builder.create();

                ((TextView)viewDialog.findViewById(R.id.editProfilePopupLayoutBtnCancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                EditText txtName = (EditText)viewDialog.findViewById(R.id.editProfilePopupLayoutTxtName);
                EditText txtEmail = (EditText)viewDialog.findViewById(R.id.editProfilePopupLayoutTxtEmail);
                EditText txtPassword = (EditText)viewDialog.findViewById(R.id.editProfilePopupLayoutTxtPassword);

                try {
                    txtName.setText(objectUser.getString("Name"));
                    txtEmail.setText(objectUser.getString("Email"));
                    txtPassword.setText(objectUser.getString("Password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ((Button)viewDialog.findViewById(R.id.editProfilePopupLayoutBtnSave)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!txtName.getText().toString().trim().equals("") && !txtEmail.getText().toString().trim().equals("") && !txtPassword.getText().toString().trim().equals("")) {
                            if(Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString().trim()).matches()) {
                                if(txtPassword.getText().toString().trim().length() >= 8) {
                                    userController.updateUserProfile(txtName.getText().toString().trim(), txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim());

                                    LoadData();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Password must contains at least 8 characters", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ProfileActivity.this, "Email format is not valid", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Please fill up the profile information", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                dialog.show();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userController.logoutUser();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                finish();
            }
        });
    }

    private boolean isReadExternalPermitted() {
        if(ActivityCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 500);
            return false;
        }
    }

    private void LoadAnimation() {
        imgBackgroundTop.animate().setDuration(800).alpha(1).translationY(0).setInterpolator(new DecelerateInterpolator());

        btnBack.animate().setDuration(600).setStartDelay(120).alpha(1).translationX(0);
        cardViewBackground.animate().setDuration(600).setStartDelay(240).alpha(1).translationY(0);
        imgProfile.animate().setDuration(600).setStartDelay(360).alpha(1).translationY(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgProfile.setElevation(8);
        }
        btnUploadImage.animate().setDuration(600).setStartDelay(360).alpha(1).translationY(0);

        lblName.animate().setDuration(600).setStartDelay(480).alpha(1).translationY(0);
        lblRole.animate().setDuration(600).setStartDelay(600).alpha(1).translationX(0);
        lblEmail.animate().setDuration(600).setStartDelay(720).alpha(1).translationX(0);

        btnEditProfile.animate().setDuration(600).setStartDelay(840).alpha(1).translationY(0);
        imgBtnEditProfile.animate().setDuration(600).setStartDelay(840).alpha(1).translationY(0);

        btnLogout.animate().setDuration(600).setStartDelay(1080).alpha(1).translationY(0);
    }

    private void LoadData() {
        objectUser = new UserController(ProfileActivity.this).getLoggedInUserObject();
        try {
            if(objectUser.getString("Image").equals("")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    imgProfile.setImageDrawable(getDrawable(R.drawable.dot_unselected));
                } else {
                    imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.dot_unselected));
                }
            } else {
                imgProfile.setImageBitmap(new BitmapHelper().convertToBitmap(objectUser.getString("Image")));
            }
            lblName.setText(objectUser.getString("Name"));
            lblRole.setText(objectUser.getString("Role"));
            lblEmail.setText(objectUser.getString("Email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}