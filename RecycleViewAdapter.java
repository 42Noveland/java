package com.example.text;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public  class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.articleViewHolder> {
    List<Article> myarticles;

    public RecycleViewAdapter(List<Article> articles) {
        this.myarticles = articles;
    }
    @NonNull
    @NotNull
    @Override
    public articleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,parent,false);
        articleViewHolder holder = new articleViewHolder(view);
        return new articleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull articleViewHolder holder, int position) {
        Article article = myarticles.get(position);
        holder.title.setText(article.title);
        holder.time.setText(article.date);
        holder.zuozhe.setText(article.username);
        String link = article.link;
        holder.articleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),link,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(),MainActivity2.class);
                intent.putExtra("link", link);
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myarticles.size();
    }

    class articleViewHolder extends RecyclerView.ViewHolder{
        TextView title,time,zuozhe;
        View articleView;
        public articleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            articleView = itemView;
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.time);
            zuozhe = itemView.findViewById(R.id.zuozhe);
        }
    }

}
