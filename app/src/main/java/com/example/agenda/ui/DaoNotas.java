package com.example.agenda.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import com.example.agenda.AdminSQLiteOpenHelper;
import com.example.agenda.Nota;

import java.util.ArrayList;

public class DaoNotas {
    Context context;
    AdminSQLiteOpenHelper admin;
    SQLiteDatabase BaseDeDatos;
    public DaoNotas(Context ctx){
        this.context=ctx;
        admin= new AdminSQLiteOpenHelper(ctx,"NotasT",null,1);
        BaseDeDatos= admin.getWritableDatabase();

    }
    public void Registrar(Nota nota){

            ContentValues registro = new ContentValues();
            registro.put("titulo",nota.getTitulo());
            registro.put("texto",nota.getTexto());
            registro.put("tipo",nota.getTipo());
            registro.put("fecha","");
            registro.put("hora","");
            registro.put("estado","");
            BaseDeDatos.insert("NotasT",null,registro);
            BaseDeDatos.close();

    }
    public void RegistrarTarea(Nota nota){

        ContentValues registro = new ContentValues();
        registro.put("titulo",nota.getTitulo());
        registro.put("texto",nota.getTexto());
        registro.put("tipo",nota.getTipo());
        registro.put("fecha",nota.getFecha());
        registro.put("hora",nota.getHora());
        registro.put("estado",nota.getEstado());
        BaseDeDatos.insert("NotasT",null,registro);
        BaseDeDatos.close();

    }
    //Metodo para consultar una nota
    public Cursor BuscarUno(String  id){
        return  BaseDeDatos.rawQuery("select * from NotasT WHERE id"+"=?",new String[]{id});
    }
    public Cursor ConsultarTodas(){
        return  BaseDeDatos.rawQuery("select * from NotasT",null);
    }
    public void EliminarUno(String  ID){
        int cantidad = BaseDeDatos.delete("NotasT","ID="+"=?",new String[]{ID});
        BaseDeDatos.close();
    }
    public void EliminarPorTitulo(String  Titulo){
        int cantidad = BaseDeDatos.delete("NotasT","titulo="+"=?",new String[]{Titulo});
        BaseDeDatos.close();
    }
    public void Modificar(Nota nota){
            ContentValues registro = new ContentValues();
            registro.put("titulo",nota.getTitulo());
            registro.put("texto",nota.getTexto());
            int cantidad = BaseDeDatos.update("NotasT",registro,"ID="+"=?",new String[]{""+nota.getID()});
            BaseDeDatos.close();

    }
    public void ModificarEstado(Nota nota){
        ContentValues registro = new ContentValues();
        registro.put("estado",nota.getEstado());
        int cantidad = BaseDeDatos.update("NotasT",registro,"ID="+nota.getID(),null);
        BaseDeDatos.close();

    }
    public void ModificarTodo(Nota nota){
        ContentValues registro = new ContentValues();
        registro.put("titulo",nota.getTitulo());
        registro.put("texto",nota.getTexto());
        registro.put("tipo",nota.getTipo());
        registro.put("fecha",nota.getFecha());
        registro.put("hora",nota.getHora());
        registro.put("estado",nota.getEstado());
        BaseDeDatos.update("NotasT",registro,"ID="+nota.getID(),null);
        BaseDeDatos.close();

    }
    public ArrayList<String> ObtenerFecha(){
        ArrayList<String> fecha = new ArrayList<>();
        Cursor c = BaseDeDatos.rawQuery("SELECT fecha FROM NotasT", null);
        if(c.moveToFirst()){
            do{
                fecha.add(c.getString(4));
            }while(c.moveToNext());
        }
        return fecha;
    }
}
