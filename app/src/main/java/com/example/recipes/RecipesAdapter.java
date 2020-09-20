package com.example.recipes;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {



    private List<Recipes> recipes;
    private MyRecipesListener listener;


    interface MyRecipesListener {
        void onRecipeClicked(int position, View view);
    }
    public void setListener(MyRecipesListener listener){
        this.listener=listener;
    }



    public RecipesAdapter(List<Recipes> recipes) {
        this.recipes = recipes;
    }



    public class RecipesViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv ;
        TextView prepTime;
        ImageView recipe_pic;
        ImageView recipe_pic_drawble;
        RatingBar ratingBar;



        public RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.recipe_name);
            prepTime = itemView.findViewById(R.id.prep_time);
            recipe_pic = itemView.findViewById(R.id.recipe_pic);
            recipe_pic_drawble = itemView.findViewById(R.id.recipe_pic);
           // ratingBar = itemView.findViewById(R.id.rating_bar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onRecipeClicked(getAdapterPosition(),v);

                }
            });

        }
    }
    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_layout,parent,false);
        RecipesViewHolder recipesViewHolder = new RecipesViewHolder(view);
        return recipesViewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        Recipes recipe = recipes.get(position);

        holder.nameTv.setText(" " +recipe.getRecipe_name());
        holder.prepTime.setText(" " +recipe.getPreparation_time());
        Bitmap bitmap = getBitmap(recipe.getCurrent_path());
        holder.recipe_pic.setImageBitmap(recipe.getBitmap());

        if(bitmap!=null) {
            Glide.with(holder.recipe_pic.getContext())
                    .load(new File(recipe.getCurrent_path()))
                    .into(holder.recipe_pic);
        }

        /*
        if(recipe.isDynamic()) {
            Bitmap bitmap = getBitmap(recipe.getCurrent_path());
            holder.recipe_pic.setImageBitmap(bitmap);
        }
        else {
            holder.recipe_pic_drawble.setImageResource(recipe.getRecipeResId());
        }

         */

        //holder.recipe_pic.setImageResource(recipe.getRecipeResId());
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public Bitmap getBitmap(String path) {
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(path,options);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

