package com.example.agenda;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //Este constructor permite mandar a la base de datos el contexto (esta cosa es como la interfaz o algo asi)
    Context context;
    //Aqui se crea la base de datos
    @Override
    public void onCreate(SQLiteDatabase BaseDeDatos) {
        BaseDeDatos.execSQL("create table NotasT(ID integer primary key autoincrement, titulo text not null, texto text not null, tipo text not null, fecha text,hora text,estado text);");
        BaseDeDatos.execSQL("create table MultimediaT(ID integer primary key autoincrement,ruta text not null, tipo text not null,idNota integer not null);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
