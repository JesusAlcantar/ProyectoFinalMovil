package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.agenda.ui.DaoNotas;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView notificationsTime;
    private int alarmID = 1;
    SharedPreferences settings;
    String horaDia;
    TextView txthora;
    ImageView imgG;
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    VideoView videoView;
    ArrayList<String>listaInformacion;
    RecyclerView recyclerView;
    ArrayList<Multimedia> listaMultimedia;
    private MediaRecorder grabacion;
    private String archivoSalida=null;
    private BottomNavigationView btnMenu;
    private EditText txtTitulo,txtTexto;
    private CheckBox chkHomeWork, chkNotes;
    Multimedia multimedia;
    //private CalendarView dtpCalendar;
    Nota nota;
    private Button btnCal;
    private TextView txtFecha;
    Calendar cal;
    int anio;
    int mes ;
    int dia;
    int ultimo;
    int hr;
    int mn;
    String FechaSeleccionada;
    private ImageView img;
    String RutaAbsoluta="";
    private final static  String CHANNEL_ID = "NOTIFICACION";
    private final static  int NOTIFICACION_ID = 0;
    private static final int REQUEST_PERMISSION_CAMERA =100;
    private static final int REQUEST_IMAGE_CAMERA =101;
    private AppBarConfiguration mAppBarConfiguration;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    Context context;
    String datoI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img = (ImageView) findViewById(R.id.image_view);
        videoView=(VideoView)findViewById(R.id.videoView);
        txtTitulo = (EditText) findViewById(R.id.txtTituloNota);
        txtTexto = (EditText) findViewById(R.id.txtContenido);
        chkHomeWork = (CheckBox) findViewById(R.id.chkTareas);
        chkNotes = (CheckBox) findViewById(R.id.chkNotas);
        txtFecha = (TextView) findViewById(R.id.txtFecha);
        btnCal = (Button) findViewById(R.id.btnCalendario);
        datoI=getIntent().getStringExtra("IdNota");
        btnMenu = (BottomNavigationView) findViewById(R.id.btnNavegacion);
        imgG =(ImageView)findViewById(R.id.imageViewGrande);
        txthora=(TextView)findViewById(R.id.texHora);
        btnMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.btnCamaraMenu) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            //este arreglo guarda las opciones que tendra este menu emergente
                            String [] op = {"Tomar Foto", "Grabar Video"};
                            //el menu emergente es un Alert dialog, que como contexto le estamos mando la interfaz de la clase listviewNotas
                            AlertDialog.Builder alop = new AlertDialog.Builder(MainActivity.this);
                            //se establce el titulo que tendra este alertDialog
                            alop.setTitle("Seleccione una opción");
                            //Aqui se le asignan los elementos que contendra el alert dialog, ademas de la accion de darle clic a los elementos
                            alop.setItems(op, new DialogInterface.OnClickListener() {
                                //se sobre escribe el metodo onclick
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //di la opcion elegida es una editar o eliminar se haran diversas funciones
                                    if(op[i].equals("Tomar Foto")){
                                        TomarFoto();
                                    }else if(op[i].equals("Grabar Video")){
                                        //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this

                                        TomarVideo();
                                    }
                                }
                            }).create().show();//se crea y se muestra el alertDialog

                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                        }
                    } else {
                        TomarFoto();

                    }
                } else if (item.getItemId() == R.id.btnAudioMenu) {
                    //este arreglo guarda las opciones que tendra este menu emergente
                    final String[] op = {"Grabar", "Reproducir"};
                    //el menu emergente es un Alert dialog, que como contexto le estamos mando la interfaz de la clase listviewNotas
                    AlertDialog.Builder alop = new AlertDialog.Builder(MainActivity.this);
                    //se establce el titulo que tendra este alertDialog
                    alop.setTitle("Seleccione una opción");
                    //Aqui se le asignan los elementos que contendra el alert dialog, ademas de la accion de darle clic a los elementos
                    alop.setItems(op, new DialogInterface.OnClickListener() {
                        //se sobre escribe el metodo onclick
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //di la opcion elegida es una editar o eliminar se haran diversas funciones
                            if (op[i].equals("Grabar")) {
                                Recorder();
                            } else if (op[i].equals("Reproducir")) {

                            }
                        }
                    }).create().show();//se crea y se muestra el alertDialog
                    return false;

                }
                return true;
            }
        });
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }
        if(datoI!=null){
            consultarUnaNotas(datoI);
        }
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.setVisibility(View.GONE);
            }
        });
        imgG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgG.setVisibility(View.GONE);
            }
        });
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
    }
    public void consultarUnaNotas(String id){
        DaoNotas daoNotas = new DaoNotas(getApplicationContext());
        Nota notas = null;
        listaNotas = new ArrayList<Nota>();
        Cursor cursor = daoNotas.BuscarUno(id);
        String tipo;
        if(cursor.moveToFirst()){
            txtTitulo.setText(cursor.getString(1));
            txtTexto.setText(cursor.getString(2));
            txtFecha.setText(cursor.getString(4));
            tipo=cursor.getString(3);
            if(tipo.equals("Nota")){
                chkNotes.setChecked(true);
            }else{
                chkHomeWork.setChecked(true);
            }
            consultarMultimediaIdNotas(id);
            iniciar();
        }
        Button button = (Button)findViewById(R.id.btnAgregar);
        button.setText("Actualizar");
    }
    public void consultarMultimedia(){
        DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
        Multimedia multimedia = null;
        listaMultimedia = new ArrayList<Multimedia>();
        Cursor cursor = daoMultimedia.ConsultarTodas();
        while(cursor.moveToNext()){
            multimedia= new Multimedia(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            listaMultimedia.add(multimedia);
        }
    }
    public void consultarMultimediaIdNotas(){
        DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
        Multimedia multimedia = null;
        listaMultimedia = new ArrayList<Multimedia>();
        Cursor cursor = daoMultimedia.BuscarUno(ultimo+1+"");
        while(cursor.moveToNext()){
            multimedia= new Multimedia(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            listaMultimedia.add(multimedia);
        }
    }
    public void consultarMultimediaIdNotas(String id){
        DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
        Multimedia multimedia = null;
        listaMultimedia = new ArrayList<Multimedia>();
        Cursor cursor = daoMultimedia.BuscarUno(id);
        while(cursor.moveToNext()){
            multimedia= new Multimedia(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            listaMultimedia.add(multimedia);
        }
    }
    public void init(){
        //
        AdaptadorListaMultimedia adaptadorLista = new AdaptadorListaMultimedia(listaMultimedia,ultimo,this);
        recyclerView = findViewById(R.id.rvMultimedia);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorLista);
        recyclerView.invalidate();
    }
    String tipo;
    public void iniciar(){
        //

        AdaptadorListaMultimedia adaptadorLista = new AdaptadorListaMultimedia(listaMultimedia,this);
        recyclerView = findViewById(R.id.rvMultimedia);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptadorLista);
        recyclerView.invalidate();
        adaptadorLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipo=listaMultimedia.get(recyclerView.getChildAdapterPosition(view)).getTipo()+"";

                if(tipo.equals("Audio")){
                    reproducir(listaMultimedia.get(recyclerView.getChildAdapterPosition(view)).getRuta());
                }
                if(tipo.equals("Video")){
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(Uri.parse(listaMultimedia.get(recyclerView.getChildAdapterPosition(view)).getRuta()));
                    videoView.start();

                }
                if(tipo.equals("Imagen")){
                    imgG.setVisibility(View.VISIBLE);
                    imgG.setImageURI(Uri.parse(listaMultimedia.get(recyclerView.getChildAdapterPosition(view)).getRuta()));
                }

                //Toast.makeText(getApplicationContext(),"Seleccion: "+listaNotas.get(recyclerView.getChildAdapterPosition(view)).getTexto(),Toast.LENGTH_SHORT).show();
            }
        });
        adaptadorLista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String [] op = {"Eliminar"};
                AlertDialog.Builder alop = new AlertDialog.Builder(MainActivity.this);
                //se establce el titulo que tendra este alertDialog
                alop.setTitle("Desea Eliminar?");
                //Aqui se le asignan los elementos que contendra el alert dialog, ademas de la accion de darle clic a los elementos
                alop.setItems(op, new DialogInterface.OnClickListener() {
                    //se sobre escribe el metodo onclick
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //di la opcion elegida es una editar o eliminar se haran diversas funciones
                        if(op[i].equals("Eliminar")){
                            //se crea el objeto daonotas que como parametro le mandamos el contexto listviewNotas.this
                            DaoMultimedia daoMultimedia = new DaoMultimedia(MainActivity.this);
                            //para eliminar en este caso se optiene el ID guarado en la listaNotas para asi eliminar el elemento correcto
                            daoMultimedia.EliminarUnoMultimedia(listaMultimedia.get(recyclerView.getChildAdapterPosition(view)).getId()+"");
                            iniciar();
                        }
                    }
                }).create().show();//se crea y se muestra el alertDialog

                return false;
            }
        });
    }
    public void Recorder(){
        String Ruta="";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        try{
            DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
            consultarNotas();

            if(grabacion==null) {
                ultimo=listaNotas.get(listaNotas.size()-1).getID();
                archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+timeStamp+".mp3";
                grabacion = new MediaRecorder();
                grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
                grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                grabacion.setOutputFile(archivoSalida);
                try{
                    grabacion.prepare();
                    grabacion.start();
                }catch (IOException e){

                }
                Toast.makeText(this,"Grabando....",Toast.LENGTH_SHORT).show();
            }else if(grabacion!=null){
                grabacion.stop();
                grabacion.release();
                grabacion=null;
                Toast.makeText(this,"Grabacion finalizada",Toast.LENGTH_SHORT).show();
                multimedia = new Multimedia(archivoSalida,"Audio",ultimo+1);
                daoMultimedia.Registrar(multimedia);
                consultarMultimediaIdNotas();
                iniciar();
            }

        }catch (Exception e) {
            DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
            consultarNotas();
            if (grabacion == null) {
                if (listaNotas.isEmpty()) {
                    ultimo = 1;
                }
                archivoSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + timeStamp + ".mp3";
                grabacion = new MediaRecorder();
                grabacion.setAudioSource(MediaRecorder.AudioSource.MIC);
                grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                grabacion.setOutputFile(archivoSalida);
                try{
                    grabacion.prepare();
                    grabacion.start();
                }catch (IOException ex){

                }
                Toast.makeText(this,"Grabando....",Toast.LENGTH_SHORT).show();
            }else if(grabacion!=null){
                grabacion.stop();
                grabacion.release();
                grabacion=null;
                Toast.makeText(this,"Grabacion finalizada",Toast.LENGTH_SHORT).show();
                multimedia = new Multimedia(archivoSalida,"Audio",ultimo);
                daoMultimedia.Registrar(multimedia);
                consultarMultimediaIdNotas();
                iniciar();
            }

        }


    }
    public void reproducir(String ur){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(ur);
            mediaPlayer.prepare();

        }catch (IOException e){

        }
        mediaPlayer.start();
        Toast.makeText(this,"Reproduciendo...",Toast.LENGTH_SHORT).show();
    }
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private void TomarVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    //Sirve para generar el nombre de la imagen

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "Nota_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        RutaAbsoluta = image.getAbsolutePath();
        return image;
    }

    //Tomar foto y crear el archivo
    //final int REQUEST_TAKE_PHOTO = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_IMAGE_CAMERA){
            if(permissions.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TomarFoto();
            }else{
                Toast.makeText(this,"No tienes permisos",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void TomarFoto() {
        Intent camaraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(camaraIntent.resolveActivity(getPackageManager())!=null){
            //startActivityForResult(camaraIntent,REQUEST_IMAGE_CAMERA);
            File foto = null;
            try {
                foto = createImageFile();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(foto!=null){
                Uri uri = FileProvider.getUriForFile(this,"com.example.agenda",foto);
                camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(camaraIntent,REQUEST_IMAGE_CAMERA);

            }
        }


    }
    //Aqui esta la alarma
    public void Alarma(){
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        alarmIntent = PendingIntent.getBroadcast(context,0,intent,0);
        DaoNotas notas = new DaoNotas(getApplicationContext());;
        ArrayList<String> fechas = notas.ObtenerFecha();
        String dia, mes, año = ""; String[] fecha;
        String hora, min;
        String[] horaseparada;
        for(String lista : fechas){
            fecha = lista.split("/");
            horaseparada=horaDia.split("/");
            hora =horaseparada[0];
            min=horaseparada[1];
            dia = fecha[0];
            mes = fecha[1];
            año = fecha[2];
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(29));
            //calendar.set(Calendar.MONTH, Integer.valueOf(11));
            //calendar.set(Calendar.YEAR,Integer.valueOf(2020));
            calendar.set(Calendar.HOUR_OF_DAY,hr);
            calendar.set(Calendar.MINUTE,mn);
            calendar.set(Calendar.SECOND,0);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),1000*60*20,alarmIntent);
        }
        if(alarmMgr!=null){
            alarmMgr.cancel(alarmIntent);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAMERA) {
            if(resultCode== Activity.RESULT_OK){
                //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //img.setImageBitmap(bitmap);
                //Log.i("TAG","Result=>"+bitmap);
                //img.setImageURI(Uri.parse(RutaAbsoluta));
                try{
                    DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
                    consultarNotas();
                    ultimo=listaNotas.get(listaNotas.size()-1).getID();
                    multimedia = new Multimedia(RutaAbsoluta,"Imagen",ultimo+1);
                    daoMultimedia.Registrar(multimedia);
                    consultarMultimediaIdNotas();
                    iniciar();
                }catch (Exception e){
                    DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
                    consultarNotas();
                    if(listaNotas.isEmpty()){
                        ultimo=1;
                    }
                    multimedia = new Multimedia(RutaAbsoluta,"Imagen",ultimo);
                    daoMultimedia.Registrar(multimedia);
                    consultarMultimediaIdNotas();
                    iniciar();
                }
            }

        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();
            try{
                DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
                consultarNotas();
                ultimo=listaNotas.get(listaNotas.size()-1).getID();
                multimedia = new Multimedia(videoUri.toString(),"Video",ultimo+1);
                daoMultimedia.Registrar(multimedia);
                consultarMultimediaIdNotas();
                iniciar();
            }catch (Exception e){
                DaoMultimedia daoMultimedia = new DaoMultimedia(getApplicationContext());
                consultarNotas();
                if(listaNotas.isEmpty()){
                    ultimo=1;
                }
                multimedia = new Multimedia(videoUri.toString(),"Video",ultimo);
                daoMultimedia.Registrar(multimedia);
                consultarMultimediaIdNotas();
                iniciar();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    ArrayList<Nota> listaNotas;

    //Metodo para dar de alta las notas
    public void Registrar(View view){
        //Se crea el objeto DaoNotas
        if(datoI!=null){
            DaoNotas daoNotas = new DaoNotas(getApplicationContext());

            //Se llenan las variables con el contenido de los textview
            Boolean tarea = false;
            String titulo = txtTitulo.getText().toString();
            String texto = txtTexto.getText().toString();
            //Si ambos textview no estan llenos no se permite registrar la nota, en caso contrario se porcede a registrar en la base de datos
            if(!titulo.isEmpty() && !texto.isEmpty()){
                if(chkNotes.isChecked()){
                    nota = new Nota(titulo,texto,"Nota");
                    daoNotas.ModificarTodo(nota);
                    txtTitulo.setText("");
                    txtTexto.setText("");

                    tarea = false;
                }
                else if(chkHomeWork.isChecked()) {
                    nota = new Nota(titulo,texto,"Tarea",FechaSeleccionada,horaDia,"Activo");
                    daoNotas.ModificarTodo(nota);
                    txtTitulo.setText("");
                    txtTexto.setText("");
                    tarea = true;
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).
                        setContentText("Se ha guardado la tarea "+titulo)
                        .setContentTitle("Tareas")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Se ha guardado la tarea "+titulo))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setSmallIcon(R.drawable.ic_menu_send);
                builder.setColor(Color.BLUE);
                builder.setVibrate(new long[]{1000,1000,1000,1000});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(NOTIFICACION_ID,builder.build());
                createNotificationChannel(titulo);
                Toast.makeText(this,"Registro exitoso",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
            NuevaAlarma(Integer.parseInt(datoI));
        }else {
            DaoNotas daoNotas = new DaoNotas(getApplicationContext());

            //Se llenan las variables con el contenido de los textview
            Boolean tarea = false;
            String titulo = txtTitulo.getText().toString();
            String texto = txtTexto.getText().toString();
            //Si ambos textview no estan llenos no se permite registrar la nota, en caso contrario se porcede a registrar en la base de datos
            if (!titulo.isEmpty() && !texto.isEmpty()) {
                if (chkNotes.isChecked()) {
                    nota = new Nota(titulo, texto, "Nota");
                    daoNotas.Registrar(nota);
                    txtTitulo.setText("");
                    txtTexto.setText("");

                    tarea = false;
                } else if (chkHomeWork.isChecked()) {
                    nota = new Nota(titulo, texto, "Tarea", FechaSeleccionada, horaDia, "Activo");
                    daoNotas.RegistrarTarea(nota);
                    txtTitulo.setText("");
                    txtTexto.setText("");
                    tarea = true;
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).
                        setContentText("Se ha guardado la tarea " + titulo)
                        .setContentTitle("Tareas")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Se ha guardado la tarea " + titulo))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setSmallIcon(R.drawable.ic_menu_send);
                builder.setColor(Color.BLUE);
                builder.setVibrate(new long[]{1000, 1000, 1000, 1000});

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(NOTIFICACION_ID, builder.build());
                createNotificationChannel(titulo);
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
            NuevaAlarma();
        }

    }
    //Metodo para consultar una nota
    //public void Notificacion(String titulo){

    //}
    private void createNotificationChannel(String titulo) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            String description = titulo;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void Buscar(View view){
        DaoNotas daoNotas = new DaoNotas(getApplicationContext());
        String titulo = txtTitulo.getText().toString();
        if(!titulo.isEmpty()){
            //el cursor es como un arraylist en la que nos permite posicionar el corsur al principio de la colecicon y a partir de ahi tomar lo elementos o datos
            Cursor fila =daoNotas.BuscarUno(titulo);
            if(fila.moveToFirst()){
                txtTitulo.setText(fila.getString(0));
                txtTexto.setText(fila.getString(1));
            }else{
                Toast.makeText(this,"No existe la nota",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"Debes ingresar algun titulo",Toast.LENGTH_SHORT).show();
        }
    }
    public void Eliminar(View view){
        DaoNotas daoNotas = new DaoNotas(getApplicationContext());
        String titulo = txtTexto.getText().toString();
        if(!titulo.isEmpty()){
            daoNotas.EliminarPorTitulo(titulo);
            Toast.makeText(this,"Nota eliminada de forma exitosa",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Debes ingresar algun titulo",Toast.LENGTH_SHORT).show();
        }
    }
    public void Modificar(View view){
        DaoNotas daoNotas = new DaoNotas(getApplicationContext());
        String titulo = txtTitulo.getText().toString();
        String texto = txtTexto.getText().toString();
        if(!titulo.isEmpty() && !texto.isEmpty()){
            Nota nota = new Nota(titulo,texto);
            daoNotas.Modificar(nota);
            Toast.makeText(this,"Nota Modificada de foma exitosa",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Debes llenar todos los campos",Toast.LENGTH_SHORT).show();
        }
    }
    public void Seleccion(View view){

        boolean verificado = ((CheckBox) view).isChecked();
        if(!chkHomeWork.isChecked()){
            chkNotes.setEnabled(true);
            txtTexto.setVisibility(View.INVISIBLE);
            txtFecha.setVisibility(View.INVISIBLE);
            txthora.setVisibility(View.INVISIBLE);
            btnCal.setVisibility(View.INVISIBLE);
            //dtpCalendar.setVisibility(View.INVISIBLE);
        }
        else if(!chkNotes.isChecked()) {
            chkHomeWork.setEnabled(true);
            txtTexto.setVisibility(View.INVISIBLE);
            txtFecha.setVisibility(View.INVISIBLE);
            txthora.setVisibility(View.INVISIBLE);
            btnCal.setVisibility(View.INVISIBLE);
        }
        switch (view.getId()){
            case R.id.chkTareas:
                if(verificado) {
                    chkNotes.setEnabled(false);
                    txtTexto.setVisibility(View.VISIBLE);
                    //dtpCalendar.setVisibility(View.VISIBLE);
                    txtFecha.setVisibility(View.VISIBLE);
                    txthora.setVisibility(View.VISIBLE);
                    btnCal.setVisibility(View.VISIBLE);
                }else{
                    chkNotes.setEnabled(true);
                }
                break;
            case R.id.chkNotas:
                if(verificado) {
                    chkHomeWork.setEnabled(false);
                    txtTexto.setVisibility(View.VISIBLE);
                }
                else{
                    chkHomeWork.setEnabled(true);
                }
                break;
        }

    }
    public void Calendario(View view){
        cal = Calendar.getInstance();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anio, int mes, int dia) {
                FechaSeleccionada="";
                FechaSeleccionada = dia+"/"+mes+"/"+anio;
                txtFecha.setText("");
                txtFecha.setText(txtFecha.getText()+FechaSeleccionada);
            }
        },anio,mes,dia);
        dpd.show();
        hora();
    }
    public void hora(){
        Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog tmd = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hora, int min) {
                txthora.setText("");
                horaDia="";
                txthora.setText("Hora: "+hora+":"+min);
                horaDia=hora+":"+min;
                hr=hora;
                mn=min;

            }
        },hora,min,false);
        tmd.show();
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
    public void NuevaAlarma(){

                Calendar mcurrentTime = Calendar.getInstance();

                        String finalHour, finalMinute;
                        alarmID=ultimo+1;
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

                        Toast.makeText(MainActivity.this, ""+ hr + ":" + mn,Toast.LENGTH_SHORT).show();

                        Utils.setAlarm(alarmID, calendar.getTimeInMillis(), MainActivity.this);
    }
    public void NuevaAlarma(int id){

        Calendar mcurrentTime = Calendar.getInstance();

        String finalHour, finalMinute;
        alarmID=id;
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

        Toast.makeText(MainActivity.this, ""+ hr + ":" + mn,Toast.LENGTH_SHORT).show();

        Utils.setAlarm(alarmID, calendar.getTimeInMillis(), MainActivity.this);
    }
    public void DesactivarAlarma(int id){
        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        Calendar mcurrentTime = Calendar.getInstance();

        String finalHour, finalMinute;
        String hour = settings.getString("hour","");
        String minute = settings.getString("minute","");


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,2000);
        calendar.set(Calendar.MONTH,mes);
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, mn);
        calendar.set(Calendar.SECOND, 0);

        SharedPreferences.Editor edit = settings.edit();
        edit.putString("hour", hr+"");
        edit.putString("minute", mn+"");

        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
        edit.putInt("alarmID", id);
        edit.putLong("alarmTime", calendar.getTimeInMillis());

        edit.commit();

        Toast.makeText(MainActivity.this, ""+ hr + ":" + mn,Toast.LENGTH_SHORT).show();

        Utils.setAlarm(alarmID, calendar.getTimeInMillis(), MainActivity.this);

    }
}