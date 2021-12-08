package com.example.agenda;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorListaMultimedia extends RecyclerView.Adapter<AdaptadorListaMultimedia.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{
    private List<Multimedia> listaM;
    private LayoutInflater inflador;
    private Context context;
    private View.OnClickListener clic;
    private View.OnLongClickListener clickListener;
    private int ultimo=-1;
    DaoMultimedia daoMultimedia;
    Multimedia multimedia;

    public AdaptadorListaMultimedia(List<Multimedia> listaM,int ultimo,Context context){
        this.inflador = LayoutInflater.from(context);
        this.context = context;
        this.listaM = listaM;
        this.ultimo=ultimo;
    }
    public AdaptadorListaMultimedia(List<Multimedia> listaM,Context context){
        this.inflador = LayoutInflater.from(context);
        this.context = context;
        this.listaM = listaM;
    }
    @Override
    public int getItemCount(){
        return listaM.size();
    }
    @Override
    public AdaptadorListaMultimedia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = inflador.inflate(R.layout.list_multimedia,null);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new AdaptadorListaMultimedia.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final AdaptadorListaMultimedia.ViewHolder holder, final int position) {
        try {
            holder.bindData(listaM.get(position));
        }catch (Exception e){

        }
    }
    public void setItems(List<Multimedia> lst){
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
        VideoView videoView;
        TextView titulo, texto, estado;

        public ViewHolder(View itemview) {
            super(itemview);
            videoView = itemview.findViewById(R.id.VideoLista);
            imageView = itemview.findViewById(R.id.ImgLista);
            titulo = itemview.findViewById(R.id.titulo);
            texto = itemview.findViewById(R.id.texto);
            estado = itemview.findViewById(R.id.Estado);

        }

        void bindData(final Multimedia item) {
            if(ultimo!=-1){
                if(ultimo+1==item.getIdNota()&&item.getRuta()!=""){
                    String tipo= item.getTipo();
                    if(tipo.equals("Imagen")){
                        videoView.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageURI(Uri.parse(item.getRuta()));
                    }
                    else if (tipo.equals("Video")){
                        videoView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        videoView.setBackgroundResource(R.drawable.camara);

                    }
                    else if (tipo.equals("Audio")){
                        videoView.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setBackgroundResource(R.drawable.ic_menu_share);

                    }
                    titulo.setText(item.getTipo());
                    texto.setText(item.getRuta());
                    estado.setText(""+item.getIdNota());

                }
            }else{
                String tipo= item.getTipo();
                if(tipo.equals("Imagen")){
                    videoView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageURI(Uri.parse(item.getRuta()));
                }
                else if (tipo.equals("Video")){
                    videoView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView.setBackgroundResource(R.drawable.camara);

                }
                else if (tipo.equals("Audio")){
                    videoView.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setBackgroundResource(R.drawable.ic_menu_share);

                }
                titulo.setText(item.getTipo());
                texto.setText(item.getRuta());
                estado.setText(""+item.getIdNota());
            }

        }
    }
}
