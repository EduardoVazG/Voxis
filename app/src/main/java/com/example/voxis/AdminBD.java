package com.example.voxis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminBD extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "bdVoxis";
    public static final int VERSION_BD = 1;

    public static final String NOMBRE_TABLA = "registro";
    public static final String CAMPO1 = "id";
    public static final String CAMPO2 = "nombre";
    public static final String CAMPO3 = "apellido";
    public static final String CAMPO4 = "usuario";
    public static final String CAMPO5 = "correo";
    public static final String CAMPO6 = "contraseña";

    public AdminBD(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 1.e.i query para crear la tabla
        String queryCrearTabla = "CREATE TABLE " + NOMBRE_TABLA + " (" +
                CAMPO1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CAMPO2 + " TEXT, " +
                CAMPO3 + " TEXT, " +
                CAMPO4 + " TEXT UNIQUE, " +
                CAMPO5 + " TEXT, " +
                CAMPO6 + " TEXT)";

        // 1.e.ii se aplica el query
        sqLiteDatabase.execSQL(queryCrearTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String queryBorrarTabla = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        sqLiteDatabase.execSQL(queryBorrarTabla);
        onCreate(sqLiteDatabase);
    }
}
