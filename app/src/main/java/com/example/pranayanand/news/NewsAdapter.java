package com.example.pranayanand.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static com.example.pranayanand.news.R.id.cardView;
import static com.example.pranayanand.news.R.id.linearLayout;

/**
 * Created by Pranay Anand on 05-09-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private ArrayList<String> titles;
    private Context context;
    private ArrayList<String> urls;
    private ArrayList<String> urlsToImage;
    private ArrayList<String> descriptions;
    private int[] androidColors;

    public NewsAdapter(int[] androidColors, ArrayList<String> titles, ArrayList<String> descriptions, ArrayList<String> urls,ArrayList<String> urlsToImage, Context context) {
        this.titles = titles;
        this.descriptions = descriptions;
        this.urls = urls;
        this.context = context;
        this.urlsToImage = urlsToImage;
        this.androidColors = androidColors;
    }

    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, final int position) {
        holder.title.setText(titles.get(position));
        holder.description.setText(descriptions.get(position));

        int randomColor = androidColors[new Random().nextInt(androidColors.length)];
        holder.linearLayout.setBackgroundColor(randomColor);
        Picasso.with(context).load(urlsToImage.get(position)).into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Main2Activity.class).putExtra("url", urls.get(position)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


            }
        });



    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;
        public ImageView imageView;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textView1);
            imageView = (ImageView) itemView.findViewById(R.id.imageview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            description = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
