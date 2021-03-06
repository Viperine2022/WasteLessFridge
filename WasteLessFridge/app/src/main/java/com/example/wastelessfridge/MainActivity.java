package com.example.wastelessfridge;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    bddSQL myDB;
    // La date (jour actuel) à afficher sur la vue principale
    String currentDate;
    // Le EditText sur lequel afficher le jour actuel
    TextView tvCurrentDate;
    // Le bouton '+'
    ImageButton add_toolbar;
    // Le bouton pour ouvrir le menu"
    ImageButton open_menu;
    // Code de retour de l'intent
    public static final int LAUNCH_ACTIVITY_ADD = 2;

    ///////////////////
    // Recycler View //
    ///////////////////

    /* Objet RecycleView */
    RecyclerView recyclerView;

    /* Adapter pour le RecyclerView */
    Adapter adapter;

    /* Listes nécessaires pour le RecyclerView */
    private LinkedList<String> dates, names, row_id;
    private LinkedList<Integer> images, pens, bins;

    /* Données du CardView (ligne de chaque produit) cf row_product */
    ImageView img_product;
    TextView date_product;
    TextView name_product;
    ImageButton pencil;
    ImageButton bin;
    PopupMenu popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new bddSQL(MainActivity.this);

        // On affiche la date du jour
        currentDate = java.text.DateFormat.getDateInstance().format(new Date());
        tvCurrentDate = findViewById(R.id.current_date);
        tvCurrentDate.setText(currentDate);

        // On définit le listener du bouton Add
        add_toolbar = findViewById(R.id.toolbar_add);
        add_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addProductIntent = new Intent(MainActivity.this, AddProduct.class);
                startActivityForResult(addProductIntent, LAUNCH_ACTIVITY_ADD);
            }
        });

        // On définit le listener du bouton openMenu
        open_menu = findViewById(R.id.dots_open_menu);
        open_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup = new PopupMenu(MainActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());
                popup.show();
            }
        });

        dates = new LinkedList<String>();
        names = new LinkedList<String>();
        row_id = new LinkedList<String>();
        images = new LinkedList<Integer>();
        pens = new LinkedList<Integer>();
        bins = new LinkedList<Integer>();
        storeDataInArrays();

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new Adapter(MainActivity.this, this, images, dates, names, row_id, pens, bins, myDB);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_ACTIVITY_ADD && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String date = data.getStringExtra("date");

            myDB.addProduct2(name.toString().trim(),
                    date.toString().trim());
            storeDataInArrays();

            recyclerView.setAdapter(adapter);
        }
        if (requestCode == Adapter.LAUNCH_MODIFY_PRODUCT && resultCode == RESULT_OK) {
            String nameModified = data.getStringExtra("name");
            String dateModified = data.getStringExtra("date");
            int id = Integer.parseInt(data.getStringExtra("id"));
            int position = Integer.parseInt(data.getStringExtra("position"));

            myDB.updateData(String.valueOf(id), nameModified, dateModified);

            adapter.modifyElement(nameModified, dateModified, position);
            recyclerView.setAdapter(adapter);
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        pens.clear();
        bins.clear();
        images.clear();
        row_id.clear();
        names.clear();
        dates.clear();
        while (cursor.moveToNext()) {
            pens.add(R.drawable.pencil);
            bins.add(R.drawable.bin);
            images.add(R.drawable.food);
            row_id.add(cursor.getString(0));
            names.add(cursor.getString(1));
            dates.add(cursor.getString(2));
        }
    }
    public void historiqueTemp(MenuItem item) {
        startActivity(new Intent(this, HistoriqueTemperature.class));
    }

    public void historiqueLum(MenuItem item) {
        startActivity(new Intent(this, HistoriqueLuminosite.class));
    }
}