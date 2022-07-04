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
import com.quizz.QuestionActivity;
import com.quizz.QuestionAddActivity;
import com.quizz.R;
import com.quizz.model.Category;

import java.util.List;
import java.util.logging.Level;


public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private Context mContext;
    private List<Category> categories;
    private List<Integer> list;
    int i;
    String image;

    public LevelAdapter(Context mContext, List<Integer> list, List<Category> categories){
        this.categories = categories;
        this.list = list;
        this.mContext = mContext;
        this.i = i;
        this.image = image;
    }

    @NonNull
    @Override
    public LevelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.level_item, parent, false);
        return new LevelAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelAdapter.ViewHolder holder, int position) {


            Category category =categories.get(position);

        holder.category_name.setText("Seviye " + list.get(position));

        Glide.with(mContext).load(category.getImage()).into(holder.category_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                intent.putExtra("level", String.valueOf(list.get(position)));
                mContext.startActivity(intent);
                //Toast.makeText(mContext, String.valueOf(list.get(position)), Toast.LENGTH_SHORT).show();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.categoryName);
            category_image = itemView.findViewById(R.id.categoryImage);
        }
    }
}
