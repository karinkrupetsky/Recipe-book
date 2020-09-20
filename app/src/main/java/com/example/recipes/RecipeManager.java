package com.example.recipes;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class RecipeManager {
    public static RecipeManager instance;
    private Context context;
    private ArrayList<Recipes> recipes = new ArrayList<>();
    static final String FILE_NAME = "recipes.dat";


    public RecipeManager(Context context) {
        this.context = context;

        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            recipes = (ArrayList<Recipes>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static RecipeManager getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeManager(context);
        }
        return instance;
    }

    private void saveRecipes() {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(recipes);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Recipes getRecipes(int position) {
        if(position < recipes.size())
            return recipes.get(position);
        return null;
    }
    public void addRecipe(Recipes recipe) {
        recipes.add(recipe);
        saveRecipes();
    }

    public void removeRecipe(int position){
        if (position < recipes.size())
        {
            recipes.remove(position);
        }
        saveRecipes();
    }
    public ArrayList<Recipes> getRecipe() {
        return recipes;
    }

}
