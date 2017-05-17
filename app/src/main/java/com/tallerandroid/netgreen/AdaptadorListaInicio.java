package com.tallerandroid.netgreen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yesce on 14/05/2017.
 */

public class AdaptadorListaInicio extends ArrayAdapter {
    Activity activity;
    ArrayList<Inicio> publicaciones;

    public AdaptadorListaInicio(Activity context, ArrayList<Inicio> publicaciones){
        super(context, R.layout.item_listview_inicio, publicaciones);
        this.activity = context;
        this.publicaciones = publicaciones;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_inicio, null);

            vh = new ViewHolder();
           // vh.mImageViewUsuario = (ImageView)convertView.findViewById(R.id.ivUsuario);
            vh.mNom_Act_Not = (TextView)convertView.findViewById(R.id.tvNomAct_Not);
            vh.mDescripcion = (TextView)convertView.findViewById(R.id.tvDescripcion);
            vh.mUsuario = (TextView)convertView.findViewById(R.id.tvUsuario);
            vh.mCategoria = (TextView)convertView.findViewById(R.id.tvCategoria);
           // vh.mParticipantes = (TextView)convertView.findViewById(R.id.tvParticipantes);
            //vh.mSeguidores = (TextView)convertView.findViewById(R.id.tvSeguidores);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
       // vh.mImageViewUsuario.setImageDrawable(publicaciones.get(position).getImagenUsuario().getDrawable());
        vh.mNom_Act_Not.setText(publicaciones.get(position).getNomActividad_Noticia());
        vh.mDescripcion.setText(publicaciones.get(position).getDescripcionAct_Not());
        vh.mUsuario.setText(publicaciones.get(position).getNomUsuario());
        vh.mCategoria.setText(publicaciones.get(position).getCategoria());
       // vh.mParticipantes.setText(publicaciones.get(position).getCantParticipantes() +"");
       // vh.mSeguidores.setText(publicaciones.get(position).getCantSeguidores()+"");

        return convertView;
    }

    static class ViewHolder{
        //ImageView mImageViewUsuario;
        TextView mNom_Act_Not;
        TextView mDescripcion;
        TextView mUsuario;
        TextView mCategoria;
       // TextView mParticipantes;
       // TextView mSeguidores;
    }
}