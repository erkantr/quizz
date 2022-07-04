package com.quizz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.quizz.LevelActivity;
import com.quizz.R;
import com.quizz.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Category> categories;

    public CategoryAdapter(Context mContext,List<Category> categories){
        this.categories = categories;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        Category category =categories.get(position);

        holder.category_name.setText(category.getCategoryName());
        Glide.with(mContext).load(category.getImage()).into(holder.category_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LevelActivity.class);
                intent.putExtra("category", category.getCategoryName());
                intent.putExtra("level", category.getLevel());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView category_name;
        public ImageView category_image;

        public ViewHolder(View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.categoryName);
            category_image = itemView.findViewById(R.id.categoryImage);
        }
    }
}
