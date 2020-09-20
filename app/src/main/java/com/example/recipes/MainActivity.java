package com.example.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton ivAddrecipe;
    static List<Recipes> recipe = new ArrayList<>();
    RecyclerView.ViewHolder viewHolder;
    boolean removed;
    SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean firstTime = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivAddrecipe = findViewById(R.id.ivAddRecipe);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        final RecipeManager manager = RecipeManager.getInstance(this);
        final RecipesAdapter recipesAdapter = new RecipesAdapter(manager.getRecipe());
        recyclerView.setAdapter(recipesAdapter);




    /*
        if(isFirstTime())
        {
            recipe.add(new Recipes(getResources().getString(R.string.pancake),
                    getResources().getString(R.string.pancake),
                    getResources().getString(R.string.pancake),
                    R.drawable.pancake));

            saveData();
        }
        else
        {
            loadData();
        }

     */


        ivAddrecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddRecipe.class));
            }
        });

        //loadData();



        /*

        sp = getSharedPreferences("details", MODE_PRIVATE);
        if (sp.getBoolean("FirstTime", false)) {
            firstTime = true;
        }
        if (!firstTime) {
            editor = sp.edit();
            editor.putBoolean("FirstTime", true);
            editor.apply();
            recipe = new ArrayList<>();
            recipe.add(new Recipes("Pancake", "2", R.drawable.pancake));
            //פה מוסיפים עוד מתכונים שאני רוצה
            recipe.add(new Recipes("Pancake", "2", R.drawable.pancake));
            recipesAdapter.notifyDataSetChanged();
            loadData();
            saveData();
        }
        if (firstTime) {
            loadData();
        }

         */



        recipesAdapter.setListener(new RecipesAdapter.MyRecipesListener() {
        @Override
         public void onRecipeClicked(int position, View view) {
            Intent intent = new Intent(MainActivity.this, CardView.class);
            intent.putExtra("position", position);
            startActivity(intent);

        }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder draggedItem, RecyclerView.ViewHolder target) {
                int positionDragged = draggedItem.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                Collections.swap(recipe, positionDragged, positionTarget);
                recipesAdapter.notifyItemMoved(positionDragged, positionTarget);
               // saveData();
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.START || direction == ItemTouchHelper.END) {    //if swipe to either side
                    if (Locale.getDefault().getLanguage().equals("en")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogCustom); //alert for confirm to delete
                        builder.setMessage("Are you sure to remove the recipe ?");    //set message
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { //when click on DELETE
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = recipe.get(position).getCurrent_path();

                                // if somehow item was saved with no photo or photo was deleted this will prevent app crash!
                                if (str != null)
                                    //deletePhoto(str);

                                recipesAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                                recipe.remove(position);
                                //saveData();

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recipesAdapter.notifyItemRangeChanged(position, recipesAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }
                        }).show();  //show alert dialog
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialogCustom); //alert for confirm to delete
                        builder.setMessage("אתה בטוח שברצונך למחוק את המתכון?");    //set message
                        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() { //when click on DELETE
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RecipeManager recipeManager = new RecipeManager(MainActivity.this) ;
                                recipeManager.removeRecipe(position);
                                // if somehow item was saved with no photo or photo was deleted this will prevent app crash!
                                //if (str != null)
                                    //deletePhoto(str);

                                recipesAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                                recipe.remove(position);
                                //saveData();

                            }
                        }).setNegativeButton("לא", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recipesAdapter.notifyItemRangeChanged(position, recipesAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            }
                        }).show();  //show alert dialog
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview
        recyclerView.setAdapter(recipesAdapter);

    }
//    private void loadData() {
//        SharedPreferences prefs = getSharedPreferences("com.example.recipes.provider", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = prefs.getString("recipe", null);
//        Type type = new TypeToken<ArrayList<Recipes>>() {}.getType();
//        recipe = gson.fromJson(json, type);
//
//        if (recipe == null){
//            recipe = new ArrayList<>();
//        }
//    }
//
//    private void saveData() {
//        SharedPreferences prefs = getSharedPreferences("com.example.recipes.provider", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(recipe);
//        editor.putString("recipe", json);
//        editor.apply();
//    }
//
//    private void deletePhoto(String path){
//        // delete saved photo from device memory
//        File file = new File(path);
//        file.delete();
//        if (file.exists()) {
//            try {
//                file.getCanonicalFile().delete();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (file.exists()) {
//                getApplicationContext().deleteFile(file.getName());
//            }
//        } else
//        if (Locale.getDefault().getLanguage().equals("en")) {
//            Toast.makeText(MainActivity.this, "Recipe deleted from memory !", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(MainActivity.this, "המתכון נמחק מהזכרון!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    private boolean isFirstTime()
//    {
//        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
//        boolean runBefore = preferences.getBoolean("RunBefore", false);
//        if (!runBefore) {
//            // first time
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean("RunBefore", true);
//            editor.apply();
//        }
//        return !runBefore;
//    }
}
