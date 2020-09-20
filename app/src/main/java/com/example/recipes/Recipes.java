package com.example.recipes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RatingBar;

import java.io.IOException;
import java.io.Serializable;

public class Recipes implements Serializable {
    private String recipe_name;
    private String preparation_time;
    private int recipeResId;
    transient private Bitmap bitmap;
    private String current_path;
    private boolean dynamic = false;
    private String ingridients;
    //private float ratingBar;

    public Recipes() {}


    public Recipes(String recipe_name, String preparation_time, String ingridients, int recipeResId) {
        this.recipe_name = recipe_name;
        this.preparation_time = preparation_time;
        //this.bitmap=bitmap;
        this.ingridients = ingridients;
        this.recipeResId = recipeResId;

    }


    public Recipes(String recipe_name, String preparation_time,String ingridients,Bitmap bitmap, String current_path) {
        this.recipe_name = recipe_name;
        this.preparation_time = preparation_time;
        //this.bitmap=bitmap;
        this.current_path = current_path;
        this.ingridients = ingridients;
        this.bitmap= bitmap;
        dynamic = true;


    }

    public String getIngridients() {
        return ingridients;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public int getRecipeResId() {
        return recipeResId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    /*

    public Recipes(String name, long preparation_time, Bitmap bitmap,String path) {
        this.name=name;
        this.preparation_time=preparation_time;
        this.bitmap=bitmap;
        this.path=path;
        dynamic=true;
    }

     */


    public String getRecipe_name() {
        return recipe_name;
    }

    public String getPreparation_time() {
        return preparation_time;
    }

    public String getCurrent_path() {
        return current_path;
    }



    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,out);
        out.defaultWriteObject();
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException,ClassNotFoundException {
       bitmap = BitmapFactory.decodeStream(in);
       in.defaultReadObject();
    }


}