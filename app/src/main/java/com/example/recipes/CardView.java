package com.example.recipes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CardView extends AppCompatActivity {

    static List<Recipes> recipe = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view);

        Button backk_btn = findViewById(R.id.backk_btn);
        backk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardView.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView nameRecipes = findViewById(R.id.name_recipes);
        TextView timePreperation = findViewById(R.id.prepe_time);
        TextView ingridients = findViewById(R.id.ingri);
        ImageView ivReciprType = findViewById(R.id.result_image);


        ImageView ivReciprType_drawable = findViewById(R.id.result_image);

        int position = getIntent().getIntExtra("position", 0);
        Recipes recipes = RecipeManager.getInstance(this).getRecipes(position);


        //loadData();

        //Recipes recipes = recipe.get(position);
        String str = recipes.getRecipe_name();
        nameRecipes.setText(str);

        str = recipes.getPreparation_time();
        timePreperation.setText(str + getResources().getString(R.string.min));

        str = recipes.getIngridients();
        ingridients.setText(str);
        //ivReciprType.setImageBitmap(recipes.getBitmap());

        Bitmap bitmap = getBitmap(recipes.getCurrent_path());
        int height = 0;
        if (bitmap != null) {
            height = bitmap.getHeight() / 3;
            int width = bitmap.getWidth() / 3;
            ivReciprType.setImageBitmap(bitmap);
            ivReciprType.getLayoutParams().height = height;
            ivReciprType.getLayoutParams().width = width;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivReciprType.setClipToOutline(true);
            }


//            if (recipes.isDynamic()) {
//
//                Bitmap bitmap = getBitmap(recipes.getCurrent_path());
//                int height = 0;
//                if (bitmap != null) {
//                    height = bitmap.getHeight() / 3;
//                    int width = bitmap.getWidth() / 3;
//                    ivReciprType.setImageBitmap(bitmap);
//                    ivReciprType.getLayoutParams().height = height;
//                    ivReciprType.getLayoutParams().width = width;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        ivReciprType.setClipToOutline(true);
//                    }
//                }
//            } else {
//                int resourceId = recipes.getRecipeResId();
//                ivReciprType_drawable.setImageResource(resourceId);
//            }
        }


//        private void loadData() {
//            SharedPreferences prefs = getSharedPreferences("com.example.recipes.provider", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = prefs.getString("recipe", null);
//            Type type = new TypeToken<ArrayList<Recipes>>() {}.getType();
//            recipe = gson.fromJson(json, type);
//
//            if (recipe == null){
//                recipe = new ArrayList<Recipes>();
//            }
//        }
    }
//
        public Bitmap getBitmap(String path) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
//
//        public void finish(View view) {
//            finish();
//        }
//
}