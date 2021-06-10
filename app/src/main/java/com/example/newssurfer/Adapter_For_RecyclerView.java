package com.example.newssurfer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter_For_RecyclerView extends RecyclerView.Adapter<ViewHolder_For_RecyclerView> {

    // treat like final
    private ArrayList<News> ITEMS;
    private item_clicked_callback iLISTENER;

    public Adapter_For_RecyclerView(ArrayList<News> items, item_clicked_callback listener){
        ITEMS = items;
        iLISTENER = listener;
    }

    @NonNull
    @Override
    public ViewHolder_For_RecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_itemview, parent, false);
        ViewHolder_For_RecyclerView view_holder = new ViewHolder_For_RecyclerView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iLISTENER.on_item_click(ITEMS.get(view_holder.getAdapterPosition()));

            }
        });
        return view_holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_For_RecyclerView holder, int position) {

        holder.title.setText(ITEMS.get(position).title);
        holder.author.setText(ITEMS.get(position).author);
        Glide.with(holder.itemView).load(ITEMS.get(position).image_url).into(holder.news_image);
    }

    @Override
    public int getItemCount() {
        return ITEMS.size();
    }

    public void update(ArrayList<News> updated_news){

        ITEMS.clear();
        ITEMS.addAll(updated_news);
        notifyDataSetChanged();
    }

}

class ViewHolder_For_RecyclerView extends RecyclerView.ViewHolder{

    public ViewHolder_For_RecyclerView(@NonNull View itemView) {
        super(itemView);
    }

    TextView title = itemView.findViewById(R.id.title);
    ImageView news_image = itemView.findViewById(R.id.news_image);
    TextView author = itemView.findViewById(R.id.author);

}

interface item_clicked_callback{
    void on_item_click(News i);
}