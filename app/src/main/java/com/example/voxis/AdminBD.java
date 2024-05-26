package com.example.voxis;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class AdminBD extends SQLiteOpenHelper {

    public static final String NOMBRE_BD = "bdVoxis";
    public static final int VERSION_BD = 3;

    // Tabla registro
    public static final String NOMBRE_TABLA = "registro";
    public static final String CAMPO1 = "id";
    public static final String CAMPO2 = "nombre";
    public static final String CAMPO3 = "apellido";
    public static final String CAMPO4 = "usuario";
    public static final String CAMPO5 = "correo";
    public static final String CAMPO6 = "contraseña";

    // Tabla categorias
    public static final String NOMBRE_TABLA_CATEGORIAS = "categorias";
    public static final String CATEGORIAS_CAMPO1 = "id";
    public static final String CATEGORIAS_CAMPO2 = "nombre";

    // Tabla contactos
    public static final String NOMBRE_TABLA_CONTACTOS = "contactos";
    public static final String CONTACTOS_CAMPO1 = "id";
    public static final String CONTACTOS_CAMPO2 = "nombre";
    public static final String CONTACTOS_CAMPO3 = "telefono";
    public static final String CONTACTOS_CAMPO4 = "correo";
    public static final String CONTACTOS_CAMPO5 = "categoria_id";

    public AdminBD(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear tabla registro
        String queryCrearTablaRegistro = "CREATE TABLE " + NOMBRE_TABLA + " (" +
                CAMPO1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CAMPO2 + " TEXT, " +
                CAMPO3 + " TEXT, " +
                CAMPO4 + " TEXT UNIQUE, " +
                CAMPO5 + " TEXT, " +
                CAMPO6 + " TEXT)";
        sqLiteDatabase.execSQL(queryCrearTablaRegistro);

        // Crear tabla categorias
        String queryCrearTablaCategorias = "CREATE TABLE " + NOMBRE_TABLA_CATEGORIAS + " (" +
                CATEGORIAS_CAMPO1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORIAS_CAMPO2 + " TEXT)";
        sqLiteDatabase.execSQL(queryCrearTablaCategorias);

        // Insertar categorías iniciales
        String queryInsertarCategorias = "INSERT INTO " + NOMBRE_TABLA_CATEGORIAS + " (" + CATEGORIAS_CAMPO2 + ") VALUES " +
                "('Ninguno')," +
                "('Amigos')," +
                "('Familia')," +
                "('Trabajo')";
        sqLiteDatabase.execSQL(queryInsertarCategorias);

        // Crear tabla contactos
        String queryCrearTablaContactos = "CREATE TABLE " + NOMBRE_TABLA_CONTACTOS + " (" +
                CONTACTOS_CAMPO1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CONTACTOS_CAMPO2 + " TEXT, " +
                CONTACTOS_CAMPO3 + " TEXT, " +
                CONTACTOS_CAMPO4 + " TEXT, " +
                CONTACTOS_CAMPO5 + " INTEGER, " +
                "FOREIGN KEY(" + CONTACTOS_CAMPO5 + ") REFERENCES " + NOMBRE_TABLA_CATEGORIAS + "(" + CATEGORIAS_CAMPO1 + "))";
        sqLiteDatabase.execSQL(queryCrearTablaContactos);

        Log.d("AdminBD", "Base de datos y tablas creadas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String queryBorrarTablaRegistro = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        sqLiteDatabase.execSQL(queryBorrarTablaRegistro);

        String queryBorrarTablaCategorias = "DROP TABLE IF EXISTS " + NOMBRE_TABLA_CATEGORIAS;
        sqLiteDatabase.execSQL(queryBorrarTablaCategorias);

        String queryBorrarTablaContactos = "DROP TABLE IF EXISTS " + NOMBRE_TABLA_CONTACTOS;
        sqLiteDatabase.execSQL(queryBorrarTablaContactos);

        onCreate(sqLiteDatabase);
    }
}
