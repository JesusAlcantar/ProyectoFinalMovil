package com.example.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DaoMultimedia {
    Context context;
    AdminSQLiteOpenHelper admin;
    SQLiteDatabase BaseDeDatos;
    public DaoMultimedia(Context ctx){
        this.context=ctx;
        admin= new AdminSQLiteOpenHelper(ctx,"MultimediaT",null,1);
        BaseDeDatos= admin.getWritableDatabase();

    }
    public void Registrar(Multimedia multimedia){

        ContentValues registro = new ContentValues();
        registro.put("ruta",multimedia.getRuta());
        registro.put("tipo",multimedia.getTipo());
        registro.put("idNota",multimedia.getIdNota());
        BaseDeDatos.insert("MultimediaT",null,registro);
        BaseDeDatos.close();

    }
    //Metodo para consultar una Multimedia
    public Cursor BuscarUno(String  id){
        return  BaseDeDatos.rawQuery("select * from MultimediaT WHERE idNota"+"=?",new String[]{id});
    }
    public Cursor ConsultarTodas(){
        return  BaseDeDatos.rawQuery("select * from MultimediaT",null);
    }
    public void EliminarUno(String  ID){
        int cantidad = BaseDeDatos.delete("MultimediaT","idNota="+"=?",new String[]{ID});
        BaseDeDatos.close();
    }
    public void EliminarUnoMultimedia(String  ID){
        int cantidad = BaseDeDatos.delete("MultimediaT","ID="+"=?",new String[]{ID});
        BaseDeDatos.close();
    }
}
