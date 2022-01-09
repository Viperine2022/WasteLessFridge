package com.example.wastelessfridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModifyProduct extends AppCompatActivity {

    EditText nameProduct2Modify;
    EditText dateProduct2Modify;
    //Le texte de name à afficher directement sur l'EditText
    String nameToDisplay;
    // Le texte de date à afficher directement sur l'EditText
    String dateToDisplay;
    // La position du produit à modifier
    int position, id;
    Button modifyProduct;
    bddSQL myDB;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        setContentView(R.layout.modify_product);
        myDB = new bddSQL(ModifyProduct.this);

        // Extraction du name et de la date puis affichage sur les EditText correspondants
        nameProduct2Modify = findViewById(R.id.name_product2modify);
        nameToDisplay = getIntent().getStringExtra(Adapter.CURRENT_NAME);
        nameProduct2Modify.setText(nameToDisplay);

        dateProduct2Modify = findViewById(R.id.date_product2modify);
        dateToDisplay = getIntent().getStringExtra(Adapter.CURRENT_DATE);
        dateProduct2Modify.setText(dateToDisplay);

        // Extraction de la position (pour la donner plus tard à MainActivity)
        position = Integer.parseInt(getIntent().getStringExtra(Adapter.POSITION));
        id = Integer.parseInt(getIntent().getStringExtra(Adapter.ID));

        System.out.println("*******" + id);
        dateProduct2Modify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    s.append("/");
                }
                if (s.length() == 5) {
                    s.append("/2022");
                }
                if (s.length() > 10) {
                    CharSequence date_format = s.subSequence(0, 10);
                    s.clear();
                    s.append(date_format);
                }
            }
        });
        modifyProduct = findViewById(R.id.modify_product);
        modifyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameProductWritten = nameProduct2Modify.getText().toString();
                String dateProductWritten = dateProduct2Modify.getText().toString();
                //myDB.addProduct2(nameProduct2Modify.getText().toString().trim(), dateProduct2Modify.getText().toString().trim());
                Intent backToMain = new Intent(ModifyProduct.this, MainActivity.class);
                backToMain.putExtra("name", nameProductWritten);
                backToMain.putExtra("date", dateProductWritten);
                backToMain.putExtra("position", String.valueOf(position));
                backToMain.putExtra("id", String.valueOf(id));
                setResult(Activity.RESULT_OK, backToMain);
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}