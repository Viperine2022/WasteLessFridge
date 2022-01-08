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

public class AddProduct extends AppCompatActivity {

    EditText nameProduct2Add;
    EditText dateProduct2Add;
    Button addProduct;
    bddSQL myDB;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
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
                if (s.length() == 2 || s.length() == 4) {
                    s = s + "/";
                }

                Log.d("***** s:", s.toString());
                Log.d("***** start : ", Integer.toString(start));
                Log.d("***** before : ", Integer.toString(count));
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    s.append("/");
                }
                if (s.length() == 5) {
                    s.append("/2022");
                }
            }
        });

        addProduct = findViewById(R.id.add_product);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nameProductWritten = nameProduct2Add.getText().toString();
                String dateProductWritten = dateProduct2Add.getText().toString();
                myDB.addProduct2(nameProduct2Add.getText().toString().trim(),
                        dateProduct2Add.getText().toString().trim());
                Intent backToMain = new Intent(AddProduct.this, MainActivity.class);
                backToMain.putExtra("name", nameProductWritten);
                backToMain.putExtra("date", dateProductWritten);

                setResult(Activity.RESULT_OK, backToMain);
                finish();
            }
        });
    }
}
