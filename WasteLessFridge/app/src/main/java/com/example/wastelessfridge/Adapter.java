package com.example.wastelessfridge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    LinkedList<Integer> images;
    LinkedList<String> dates, names;
    LinkedList<Integer> pens, bins;

    public Adapter(Context ct, LinkedList<Integer> images, LinkedList<String> dates,
                   LinkedList<String> names, LinkedList<Integer> pens, LinkedList<Integer> bins) {
        this.images = images;
        this.context = ct;
        this.dates = dates;
        this.names = names;
        this.pens = pens;
        this.bins = bins;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_product, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i) {
        viewHolder.img.setImageResource(images.get(i));
        viewHolder.date.setText(dates.get(i));
        viewHolder.name.setText(names.get(i));
        viewHolder.pencil.setImageResource(pens.get(i));
        viewHolder.bin.setImageResource(bins.get(i));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    /** Ajouter un élément au RecyclerView */
    public void addElement (String name, String date) {
        images.add(R.drawable.food);
        names.add(name);
        dates.add(date);
        pens.add(R.drawable.pencil);
        bins.add(R.drawable.bin);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView date;
        TextView name;
        ImageView pencil;
        ImageView bin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_product);
            date = itemView.findViewById(R.id.date_product);
            name = itemView.findViewById(R.id.name_product);
            pencil = itemView.findViewById(R.id.pencil);
            bin = itemView.findViewById(R.id.bin);
        }
    }
}
