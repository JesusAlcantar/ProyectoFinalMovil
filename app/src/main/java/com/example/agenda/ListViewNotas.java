package com.example.agenda;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.agenda.ui.DaoNotas;

import java.util.ArrayList;
import java.util.Calendar;

public class ListViewNotas extends AppCompatActivity {
    private SharedPreferences settings;
    private ListView listViewNotas;
    ArrayList<Nota> listaNotas;
    ArrayList<Multimedia> listaMultimedia;
    ArrayList<String> listaInformacion;
    RecyclerView recyclerView;
    MainActivity m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_notas);



        consultarNotas();
        consultarMultimedia();
        init();
        //Esta cosa de adapter lo que hacer es que no se le puede asignar directamente un arreglo por lo que se debe de hacer una adaptacion en al que
        //posteriormente le podemos mandar ya todo lo que debe contener
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listaInformacion);
        //aqui ya se llena la lista con los parametros
        //listViewNotas.setAdapter(adaptador);
        //Este metodo con sobre escritura es la que permite darle la funcion de que haga una accion al dar un clic


        //listViewNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        // @Override
        //public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
        //  //optiene la informacion del listview, en este caso del elemento seleccionado de cierta posicion
        // String informacion = "id: "+listaNotas.get(pos).getID()+"\n";
        //informacion+="Titulo: "+listaNotas.get(pos).getTitulo()+"\n";
        //informacion+="Texto: "+listaNotas.get(pos).getTexto()+"\n";
        //Aqui se manda un mensaje emergente con duracion corta
        //if(listaNotas.get(pos).getFecha()==""){
        //  Toast.makeText(getApplicationContext(),informacion, Toast.LENGTH_SHORT).show();
        //}
        //else{
        //  informacion+="Fecha: "+listaNotas.get(pos).getFecha()+"\n";
        //Toast.makeText(getApplicationContext(),informacion, Toast.LENGTH_SHORT).show();
        //}

        //}
        //});
        //Este metodo permite que cuando se mantiene presionado sobre un elemento del listview, le saldra un menu
        //listViewNotas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        //  @Override
        //public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicion, long l) {
        //  int id = listaNotas.get(posicion).getID();
        //String  titulo = listaNotas.get(posicion).getTitulo();
        //String  Texto = listaNotas.get(posicion).getTexto();
        //este arreglo guarda las opciones que tendra este menu emergente
        //String [] op = {"Editar", "Eliminar"};
        //el menu emergente es un Alert dialog, que como contexto le estamos mando la interfaz de la clase listviewNotas
        //AlertDialog.Builder alop = new AlertDialog.Builder(ListViewNotas.this);
        //se establce el titulo que tendra este alertDialog
        //alop.setTitle("Seleccione una opción");
        //Aqui se le asignan los elementos que contendra el alert dialog, ademas de la accion de darle clic a los elementos
        //alop.setItems(op, new DialogInterface.OnClickListener() {
        //se sobre escribe el metodo onclick
        //@Override
        //public void onClick(DialogInterface dialogInterface, int i) {
        //di la opcion elegida es una editar o eliminar se haran diversas funciones
        // if(op[i].equals("Editar")){
        //   AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListViewNotas.this);
        // alertDialog.setTitle("Inserta el nuevo texto");
        //EditText txt = new EditText(ListViewNotas.this);
        //alertDialog.setView(txt);
        //alertDialog.setIcon(R.drawable.ic_menu_send);
        //alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        //  @Override
        // public void onClick(DialogInterface dialogInterface, int i) {
        //   String item = txt.getText().toString();
        // DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
        //Nota nota = new Nota(id,titulo,item);
        // daoNotas.Modificar(nota);
        //}
        //}).create().show();

        //}else if(op[i].equals("Eliminar")){
        //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this
        //  DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
        //para eliminar en este caso se optiene el ID guarado en la listaNotas para asi eliminar el elemento correcto
        //daoNotas.EliminarUno(""+listaNotas.get(posicion).getID());

        //}
        //}
        //}).create().show();//se crea y se muestra el alertDialog
        //return false;
        //}
        //});
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    public void NuevaAlarma(int alarmID, int anio,int mes, int dia, int hr, int mn){

        Calendar mcurrentTime = Calendar.getInstance();

        String finalHour, finalMinute;
        String hour = settings.getString("hour","");
        String minute = settings.getString("minute","");


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,anio);
        calendar.set(Calendar.MONTH,mes);
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, mn);
        calendar.set(Calendar.SECOND, 0);

        SharedPreferences.Editor edit = settings.edit();
        edit.putString("hour", hr+"");
        edit.putString("minute", mn+"");

        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
        edit.putInt("alarmID", alarmID);
        edit.putLong("alarmTime", calendar.getTimeInMillis());

        edit.commit();

        Toast.makeText(getApplicationContext(),"Activa",Toast.LENGTH_SHORT).show();

        Utils.setAlarm(alarmID, calendar.getTimeInMillis(), getApplicationContext());
    }
        public void DesactivarAlarma(int alarmID){


            String hour = settings.getString("hour",""+1);
            String minute = settings.getString("minute",""+1);
            String finalHour, finalMinute;

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,2000);
            calendar.set(Calendar.MONTH,1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.set(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);

            SharedPreferences.Editor edit=settings.edit();
            edit.putString("hour", hour+"");
            edit.putString("minute", minute+"");

            //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
            edit.putInt("alarmID", alarmID);
            edit.putLong("alarmTime", calendar.getTimeInMillis());

            edit.commit();

            Toast.makeText(getApplicationContext(),"Desactivada",Toast.LENGTH_SHORT).show();

            Utils.setAlarm(alarmID, calendar.getTimeInMillis(), getApplicationContext());



    }
    public void enviar(String id){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("IdNota",id);
        startActivity(i);
    }
    //Este metodo permite traer todos los datos de la base de datos, se guarda en un arraylist de tipo notas para posteriormente extraerlas
    public void consultarMultimedia(){
        DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
        Multimedia multimedia = null;
        listaMultimedia = new ArrayList<Multimedia>();
        Cursor cursor = daoMultimedia.ConsultarTodas();
        while(cursor.moveToNext()){
            multimedia= new Multimedia(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            listaMultimedia.add(multimedia);
        }
        obtenerLista();
    }
    public void consultarNotas(){
        DaoNotas daoNotas = new DaoNotas(getApplicationContext());
        Nota notas = null;
        listaNotas = new ArrayList<Nota>();
        Cursor cursor = daoNotas.ConsultarTodas();
        while(cursor.moveToNext()){
            notas= new Nota(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
            listaNotas.add(notas);
        }
        obtenerLista();
    }
    //Este metodo permite pasar de un arraylist de tipo notas a uno simple de tipo string
    private void obtenerLista(){
        listaInformacion = new ArrayList<String>();
        for(int i = 0;i<listaNotas.size();i++){
            if(listaNotas.get(i).getFecha().toString()==""){
                listaInformacion.add(listaNotas.get(i).getID()+" - "+listaNotas.get(i).getTitulo()+" - "+listaNotas.get(i).getTipo()+" - ");

            }
            else{
                listaInformacion.add(listaNotas.get(i).getID()+" - "+listaNotas.get(i).getTitulo()+" - "+listaNotas.get(i).getTipo()+" - "+listaNotas.get(i).getFecha());

            }
        }
    }
     String id="";
    public void init(){

        AdaptadorLista adaptadorLista = new AdaptadorLista(listaNotas,listaMultimedia,this);
        recyclerView = findViewById(R.id.listRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id=listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID()+"";
                String informacion = "id: "+id+"\n";
                informacion+="Titulo: "+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTitulo()+"\n";
                informacion+="Texto: "+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTexto()+"\n";
                //Aqui se manda un mensaje emergente con duracion corta
                if(listaNotas.get(recyclerView.getChildAdapterPosition(view)).getFecha()==""){
                  Toast.makeText(getApplicationContext(),informacion, Toast.LENGTH_SHORT).show();
                  enviar(id);
                }
                else{
                  informacion+="Fecha: "+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getFecha()+"\n";
                Toast.makeText(getApplicationContext(),informacion, Toast.LENGTH_SHORT).show();
                    enviar(id);
                }
                //Toast.makeText(getApplicationContext(),"Seleccion: "+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTexto(),Toast.LENGTH_SHORT).show();
            }
        });
        adaptadorLista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int id = listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID();
                String  titulo = listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTitulo();
                String  Texto = listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTexto();
                //este arreglo guarda las opciones que tendra este menu emergente
                String[] op;
                if(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getEstado()!="") {
                    op = new String[]{"Editar", "Eliminar","Cambiar Estado"};
                }else{
                    op = new String[]{"Editar", "Eliminar"};
                }
                //el menu emergente es un Alert dialog, que como contexto le estamos mando la interfaz de la clase listviewNotas
                AlertDialog.Builder alop = new AlertDialog.Builder(ListViewNotas.this);
                //se establce el titulo que tendra este alertDialog
                alop.setTitle("Seleccione una opción");
                //Aqui se le asignan los elementos que contendra el alert dialog, ademas de la accion de darle clic a los elementos
                alop.setItems(op, new DialogInterface.OnClickListener() {
                    //se sobre escribe el metodo onclick
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     if(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getEstado()!=""){
                         //di la opcion elegida es una editar o eliminar se haran diversas funciones
                         if(op[i].equals("Editar")){
                             AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListViewNotas.this);
                             alertDialog.setTitle("Inserta el nuevo texto");
                             EditText txt = new EditText(ListViewNotas.this);
                             alertDialog.setView(txt);
                             alertDialog.setIcon(R.drawable.ic_menu_send);
                             alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     String item = txt.getText().toString();
                                     DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
                                     Nota nota = new Nota(id,titulo,item);
                                     daoNotas.Modificar(nota);
                                 }
                             }).create().show();

                         }else if(op[i].equals("Eliminar")){
                             //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this
                             DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
                             DaoMultimedia daoMultimedia = new DaoMultimedia(ListViewNotas.this);
                             //para eliminar en este caso se optiene el ID guarado en la listaNotas para asi eliminar el elemento correcto
                             daoNotas.EliminarUno(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID());
                             daoMultimedia.EliminarUno(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID());

                         }else if(op[i].equals("Cambiar Estado")){
                             //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this
                             DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
                             if(listaNotas.get(recyclerView.getChildAdapterPosition(view)).getEstado()=="Inactivo"){
                                 Nota nota = new Nota(id,"Activo");
                                 String fecha = listaNotas.get(recyclerView.getChildAdapterPosition(view)).getFecha();
                                 String[] f = fecha.split("/");
                                 for (String fe : f)
                                 {
                                     System.out.println(fe);
                                 }
                                 String hora = listaNotas.get(recyclerView.getChildAdapterPosition(view)).getHora();
                                 String[] h = fecha.split(":");
                                 for (String hr : h)
                                 {
                                     System.out.println(hr);
                                 }
                                 NuevaAlarma(id,Integer.parseInt(f[2]),Integer.parseInt(f[1]),Integer.parseInt(f[0]),Integer.parseInt(h[0]),Integer.parseInt(h[1]));
                                 daoNotas.ModificarEstado(nota);
                             }else{
                                 Nota nota = new Nota(id,"Inactivo");
                                 daoNotas.ModificarEstado(nota);

                                 DesactivarAlarma(id);
                             }


                         }
                        }else{
                         //di la opcion elegida es una editar o eliminar se haran diversas funciones
                         if(op[i].equals("Editar")){
                             AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListViewNotas.this);
                             alertDialog.setTitle("Inserta el nuevo texto");
                             EditText txt = new EditText(ListViewNotas.this);
                             alertDialog.setView(txt);
                             alertDialog.setIcon(R.drawable.ic_menu_send);
                             alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialogInterface, int i) {
                                     String item = txt.getText().toString();
                                     DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
                                     Nota nota = new Nota(id,titulo,item);
                                     daoNotas.Modificar(nota);
                                 }
                             }).create().show();

                         }else if(op[i].equals("Eliminar")){
                             //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this
                             DaoNotas daoNotas = new DaoNotas(ListViewNotas.this);
                             DaoMultimedia daoMultimedia = new DaoMultimedia(ListViewNotas.this);
                             //para eliminar en este caso se optiene el ID guarado en la listaNotas para asi eliminar el elemento correcto
                             daoNotas.EliminarUno(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID());
                             daoMultimedia.EliminarUno(""+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getID());

                         }

                        }
                    }
                }).create().show();//se crea y se muestra el alertDialog
                return false;
            }
        });
        recyclerView.setAdapter(adaptadorLista);
    }
}