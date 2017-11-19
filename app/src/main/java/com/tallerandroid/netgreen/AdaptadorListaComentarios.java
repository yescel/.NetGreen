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
 * Created by yesce on 05/10/2017.
 */

public class AdaptadorListaComentarios  extends ArrayAdapter {
    Activity activity;
    ArrayList<Comentario> aComentarios;

    public AdaptadorListaComentarios(Activity context, ArrayList<Comentario> aComentarios){
        super(context, R.layout.item_listview_comentarios, aComentarios);///agregar item lista
        this.activity = context;
        this.aComentarios = aComentarios;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AdaptadorListaComentarios.ViewHolder vh = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_comentarios, null);

            vh = new AdaptadorListaComentarios.ViewHolder();
            vh.mComentario = (TextView)convertView.findViewById(R.id.tvComentario_DetalleItemInicio);
            vh.mFecha = (TextView) convertView.findViewById(R.id.tvFechaComentario);
            vh.mImagenUsuario = (ImageView) convertView.findViewById(R.id.ivUsuario_Comentario);


            convertView.setTag(vh);

        } else {
            vh = (AdaptadorListaComentarios.ViewHolder) convertView.getTag();
        }
        vh.mComentario.setText(aComentarios.get(position).getComentario());
        vh.mFecha.setText(aComentarios.get(position).getFecha());
        vh.mImagenUsuario.setImageBitmap(aComentarios.get(position).getImagen());

        return convertView;
    }

    static class ViewHolder{
        TextView mComentario;
        TextView mFecha;
        ImageView mImagenUsuario;

    }
}