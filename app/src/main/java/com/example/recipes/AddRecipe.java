package com.example.recipes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddRecipe extends AppCompatActivity {

    EditText recipe_name,time_preperation , ingridients;
    Bitmap bitmap;
    static List<Recipes> recipe = new ArrayList<>();

    private final int CAMERA_REQUEST = 1;
    private String currentImagePath = null;
    //    private boolean isReached = false;
    private final int  WRITE_PERMISSION_REQUEST = 2;
    int angle = 90;
    private ImageView ivPhoto;
    private boolean photoTaken = false;
    private boolean saveData = false;
    private RatingBar rating_bar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        recipe_name = findViewById(R.id.food_name);
        time_preperation = findViewById(R.id.time);
        ingridients = findViewById(R.id.ingridients);

        //rating_bar= findViewById(R.id.ratingBar);



        ingridients.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (ingridients.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });


        Button add_recipe = findViewById(R.id.add_btn);
        //btnBackSong = findViewById(R.id.btnBackSong);
        Button btnCamera = findViewById(R.id.take_pic_btn);
        ivPhoto = findViewById(R.id.resultIv);


        //loadData();

        Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddRecipe.this,MainActivity.class);
                startActivity(intent);
            }
        });


        add_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recipe_name.getText().toString().length() == 0 || time_preperation.getText().toString().length() == 0|| ingridients.getText().toString().length() == 0|| !photoTaken) {
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        Toast.makeText(AddRecipe.this, "Incomplete details. Fill all the fields and try again ", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddRecipe.this, "אנא מלא את כל הפרטים ונסה שנית ", Toast.LENGTH_LONG).show();
                    }
                } else
                if (Locale.getDefault().getLanguage().equals("en")) {
                    if (photoTaken) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipe.this,R.style.AlertDialogCustom); //alert for confirm to delete
                        builder.setTitle("Please Confirm");
                        builder.setMessage("Save the recipe to your list ?");    //set message
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { //when click on DELETE
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addRecipe();


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AddRecipe.this, "Recipe not saved", Toast.LENGTH_LONG).show();
                            }
                        }).show();  //show alert dialog
                    } else
                        addRecipe();
                }
                else
                {
                    if (photoTaken) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipe.this,R.style.AlertDialogCustom); //alert for confirm to delete

                        builder.setTitle("הוסף מתכון");
                        builder.setMessage("האם תרצה להוסיף את המתכון?");    //set message
                        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() { //when click on DELETE
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addRecipe();

                            }
                        }).setNegativeButton("לא", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AddRecipe.this, "מתכון לא הוכנס לרשימה", Toast.LENGTH_LONG).show();
                            }
                        }).show();  //show alert dialog
                    } else
                        addRecipe();
                }
            }
        });



        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
                    } else
                       captureImage();
                } else
                   captureImage();
            }
        });
    }

    private void addRecipe() {
        String recipename = recipe_name.getText().toString();
        String time= time_preperation.getText().toString();
        String ingridient = ingridients.getText().toString();

        Recipes recipes = new Recipes(recipename, time,ingridient,bitmap, currentImagePath);
        RecipeManager manager = RecipeManager.getInstance(AddRecipe.this);
        manager.addRecipe(recipes);
        //recipe.add(recipes);
        //saveData();
        Intent intent = new Intent(AddRecipe.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //saveData = true;
        //finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (Locale.getDefault().getLanguage().equals("en")) {
                    Toast.makeText(this, "cannot take picture", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "אין אפשרות לצלם תמונה", Toast.LENGTH_SHORT).show();
                }
            } else

               captureImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK) {
            bitmap = getBitmap(currentImagePath);
            bitmap = BitmapFactory.decodeFile(currentImagePath);
            ivPhoto.setImageBitmap(bitmap);
            ivPhoto.setVisibility(View.VISIBLE);
            photoTaken = true;

        }
    }


    public Bitmap getBitmap(String path) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File getImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(timeStamp,".jpg",storageDir);
        currentImagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    public void captureImage() {
        if(photoTaken) {
            if (Locale.getDefault().getLanguage().equals("en")) {
                Toast.makeText(AddRecipe.this, "Previous taken photo deleted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(AddRecipe.this, "×”×ª×ž×•× ×” ×”×§×•×“×ž×ª ×©×¦×•×œ×ž×” × ×ž×—×§×”", Toast.LENGTH_SHORT).show();
            }
            ivPhoto.setImageBitmap(null);
            ivPhoto.setVisibility(View.GONE);
            photoTaken = false;
        }
        deleteFile();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try{
                imageFile = getImageFile();
            }catch(IOException e){
                e.printStackTrace();
            }

            if (imageFile != null){
                Uri imageUri = FileProvider.getUriForFile(this,"com.example.recipes.provider",imageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        }
    }

//    // save data
//    private void saveData() {
//        SharedPreferences prefs = getSharedPreferences("com.example.recipes.provider", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(recipe);
//        editor.putString("recipe", json);
//        editor.apply();
//    }
//
//    // load data
//    private void loadData() {
//        SharedPreferences prefs = getSharedPreferences("com.example.recipes.provider", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString("recipe", null);
//        Type type = new TypeToken<ArrayList<Recipes>>() {
//        }.getType();
//        recipe = gson.fromJson(json, type);
//
//        if (recipe == null) {
//            recipe = new ArrayList<>();
//        }
//    }
//
//
////    // check if all cells filled
////    private void checkCells() {
////        if (etNameOfSinger.getText().toString().length() == 0 || etNameOfSong.getText().toString().length() == 0
////                || etAlbum.getText().toString().length() == 0 || etLyrics.getText().toString().length() == 0 || !photoTaken)
////            btnAddSong.setEnabled(false);
////        else
////            btnAddSong.setEnabled(true);
////
////    }
//
//    // reason for override is if user clicked outside of dialog activity to remove the taken photo
//    @Override
//    public void finish() {
//        if(!saveData) {
//            if(photoTaken){
//                deleteFile();
//                if (Locale.getDefault().getLanguage().equals("en")) {
//                    Toast.makeText(AddRecipe.this, "Taken photo deleted", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(AddRecipe.this, "תמונה נימחקה", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//        super.finish();
//    }
//
//    @Override
//    public void onBackPressed() {
////        // if photo already taken remove it from memory
////        if(ivPhoto.getDrawable()!=null)
////            deleteFile();
//        finish();
//    }
//
    private void deleteFile(){
        // delete saved photo from device memory
        if (currentImagePath!=null) {
            File file = new File(currentImagePath);
            file.delete();
            if (file.exists()) {
                try {
                    file.getCanonicalFile().delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file.exists()) {
                    getApplicationContext().deleteFile(file.getName());
                }
            }
        }
    }


}
