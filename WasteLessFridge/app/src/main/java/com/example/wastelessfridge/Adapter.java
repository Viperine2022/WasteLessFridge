package com.example.wastelessfridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    Activity activity;
    LinkedList<Integer> images;
    LinkedList<String> dates, names, row_id;
    LinkedList<Integer> pens, bins;
    bddSQL myDB;
    public static final int LAUNCH_MODIFY_PRODUCT = 10;
    public static final String CURRENT_NAME = "com.example.wastelessfridge.CurrentName";
    public static final String CURRENT_DATE = "com.example.wastelessfridge.CurrentDate";
    public static final String POSITION = "com.example.wastelessfridge.Position";
    public static final String ID = "com.example.wastelessfridge.Id";
    public Adapter(Activity activity, Context ct, LinkedList<Integer> images, LinkedList<String> dates,
                   LinkedList<String> names, LinkedList<String> id, LinkedList<Integer> pens, LinkedList<Integer> bins, bddSQL myDB) {
        this.activity = activity;
        this.images = images;
        this.context = ct;
        this.dates = dates;
        this.names = names;
        this.pens = pens;
        this.bins = bins;
        this.row_id = id;
        this.myDB = myDB;

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
        viewHolder.pencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move2modify = new Intent(context, ModifyProduct.class);
                String nameToDisplay = names.get(i);
                String dateToDisplay = dates.get(i);
                move2modify.putExtra(CURRENT_NAME, nameToDisplay);
                move2modify.putExtra(CURRENT_DATE, dateToDisplay);
                move2modify.putExtra(POSITION, String.valueOf(i));
                move2modify.putExtra(ID, String.valueOf(row_id.get(i)));
                activity.startActivityForResult(move2modify, LAUNCH_MODIFY_PRODUCT);
                activity.setResult(Activity.RESULT_OK, move2modify);
            }
        });

        viewHolder.bin.setImageResource(bins.get(i));
        viewHolder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                System.out.println(row_id.get(i));
                myDB.deleteOneRow(String.valueOf(row_id.get(i)));
                images.size();
                for (int j = 1;j<images.size();j++){
                    myDB.updateId(Integer.toString(Integer.parseInt(row_id.get(i))+j));

                }

                images.remove(i);
                dates.remove(i);
                names.remove(i);
                pens.remove(i);
                bins.remove(i);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dates.size());
            }
        });
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

    /** Modifier un élement du recyclerView */
    public void modifyElement(String name, String date, int position) {
        names.set(position,name);
        dates.set(position, date);
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
