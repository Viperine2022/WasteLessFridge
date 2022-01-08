package com.example.wastelessfridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    // La date (jour actuel) à afficher sur la vue principale
    String currentDate;
    // Le EditText sur lequel afficher le jour actuel
    TextView tvCurrentDate;
    // Le bouton '+'
    ImageButton add_toolbar;
    // Code de retour de l'intent
    public final int LAUNCH_ACTIVITY_ADD = 2;

    ///////////////////
    // Recycler View //
    ///////////////////

    /* Objet RecycleView */
    RecyclerView recyclerView;

    /* Adapter pour le RecyclerView */
    Adapter adapter;

    /* Listes nécessaires pour le RecyclerView */
    private LinkedList<String> dates, names;
    private LinkedList<Integer> images, pens, bins;

    /* Données du CardView (ligne de chaque produit) cf row_product */
    ImageView img_product;
    TextView date_product;
    TextView name_product;
    ImageButton pencil;
    ImageButton bin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        dates = new LinkedList<String>();
        names = new LinkedList<String>();

        images = new LinkedList<Integer>();
        pens = new LinkedList<Integer>();
        bins = new LinkedList<Integer>();

        images.add(R.drawable.food);
        images.add(R.drawable.food);

        String[] dates_array = getResources().getStringArray(R.array.product_dates);
        String[] names_array = getResources().getStringArray(R.array.product_names);

        for (int i=0 ; i<dates_array.length ; i++) {
            dates.add(dates_array[i]);
            names.add(names_array[i]);
        }

        pens.add(R.drawable.pencil);
        pens.add(R.drawable.pencil);

        bins.add(R.drawable.bin);
        bins.add(R.drawable.bin);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new Adapter(this, images, dates, names, pens, bins);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LAUNCH_ACTIVITY_ADD && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String date = data.getStringExtra("date");

            adapter.addElement(name, date);
            recyclerView.setAdapter(adapter);
        }
    }
}