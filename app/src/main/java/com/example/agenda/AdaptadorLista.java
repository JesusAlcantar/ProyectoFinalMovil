package com.example.agenda;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.ui.DaoNotas;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorLista extends RecyclerView.Adapter<AdaptadorLista.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private List<Nota> lista;
    private List<Multimedia> listaM;
    private LayoutInflater inflador;
    private Context context;
    private View.OnClickListener clic;
    private View.OnLongClickListener clickListener;
    DaoMultimedia daoMultimedia;
    Multimedia multimedia;
    ArrayList<Nota> listaNotas;
    ArrayList<String> listaInformacion;

    public AdaptadorLista(List<Nota> itemList,List<Multimedia> listaM,Context context){
        this.inflador = LayoutInflater.from(context);
        this.context = context;
        this.lista=itemList;
        this.listaM = listaM;
    }
    @Override
    public int getItemCount(){
        return lista.size();
    }
    @Override
    public AdaptadorLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflador.inflate(R.layout.list_element,null);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdaptadorLista.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final AdaptadorLista.ViewHolder holder, final int position) {
        try {
            holder.bindData(lista.get(position));
        }catch (Exception e){

        }
    }
    public void setItems(List<Nota> items, List<Multimedia> lst){

        lista=items;
        listaM = lst;
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.clic = listener;
    }
    public void setOnLongClickListener(View.OnLongClickListener listener){
        this.clickListener = listener;
    }

    @Override
    public void onClick(View view) {
        if(clic!=null){
            clic.onClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if(clickListener!=null) {
            this.clickListener.onLongClick(view);
        }
        return false;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titulo, texto, estado,estadoNotificacion;

        public ViewHolder(View itemview) {
            super(itemview);
            imageView = itemview.findViewById(R.id.ImgLista);
            titulo = itemview.findViewById(R.id.titulo);
            texto = itemview.findViewById(R.id.texto);
            estado = itemview.findViewById(R.id.Estado);
            estadoNotificacion=itemview.findViewById(R.id.EstadoNotificacion);

        }

        void bindData(final Nota item) {
            try {
                for(int i=0;i<listaM.size();i++){
                    if (item.getID()==listaM.get(i).getIdNota()) {
                        imageView.setImageURI(Uri.parse(listaM.get(i).getRuta()));
                        titulo.setText(item.getTitulo());
                        texto.setText(item.getTexto());
                        estado.setText(item.getTipo());
                        estadoNotificacion.setText(item.getEstado());
                        break;
                    } else {
                        titulo.setText(item.getTitulo());
                        texto.setText(item.getTexto());
                        estado.setText(item.getTipo());
                        estadoNotificacion.setText(item.getEstado());
                    }
                }

            } catch (Exception e) {
                titulo.setText(item.getTitulo());
                texto.setText(item.getTexto());
                estado.setText(item.getTipo());
                estadoNotificacion.setText(item.getEstado());
            }
        }
    }
}
