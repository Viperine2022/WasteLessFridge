package com.example.wastelessfridge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;


class bddSQL extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Fridgedb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "fridge";
    private static final String COLUMN_NAME = "product_name";
    private static final String COLUMN_DATE = "limit_date";
    private static final String COLUMN_ID = "ID";

    public bddSQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    //Création de la base de donées
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DATE + " TEXT );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Permet l'ajout de nouveaux éléments à la base de données
    public void addProduct2(String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Permet de créer le cursor afin de parcourir la base de données
    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // Supprimer un élément avec son ID
    void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "ID=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Suppression effectuée avec succés!", Toast.LENGTH_SHORT).show();
        }
    }

    // Modifier une ligne
    void updateData(String row_id, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_DATE, date);
        long result = db.update(TABLE_NAME, cv, "ID=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Modification effectuée avec succés!", Toast.LENGTH_SHORT).show();
        }

    }
    void updateId(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String newID = Integer.toString(Integer.parseInt(row_id)-1);
        cv.put(COLUMN_ID, newID);

        long result = db.update(TABLE_NAME, cv, "ID=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Modification effectuée avec succés!", Toast.LENGTH_SHORT).show();
        }

    }

    // Permet d'effacer le contenu de la table (non utilisé explicitement dans ce projet mais utile en phase de développement)
    void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}