package com.example.wastelessfridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddProduct extends AppCompatActivity {

    Context context;
    EditText nameProduct2Add;
    EditText dateProduct2Add;
    Button addProduct;
    bddSQL myDB;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.add_product);
        myDB = new bddSQL(AddProduct.this);
        nameProduct2Add = findViewById(R.id.name_product2add);

        dateProduct2Add = findViewById(R.id.date_product2add);
        dateProduct2Add.addTextChangedListener(new TextWatcher() {
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

        addProduct = findViewById(R.id.add_product);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameProductWritten = nameProduct2Add.getText().toString();
                String dateProductWritten = dateProduct2Add.getText().toString();

                Intent backToMain = new Intent(AddProduct.this, MainActivity.class);
                backToMain.putExtra("name", nameProductWritten);
                backToMain.putExtra("date", dateProductWritten);

                setResult(Activity.RESULT_OK, backToMain);
                if (nameProductWritten.isEmpty() || dateProductWritten.isEmpty()) {
                    Toast.makeText(context, "Name or Date musn't be null !", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
            }
        });
    }
}
