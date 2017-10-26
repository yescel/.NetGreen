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
            vh.mImageViewUsuario = (ImageView)convertView.findViewById(R.id.ivUsuario);
            vh.mNomUsuario = (TextView)convertView.findViewById(R.id.tvUsuario_Inicio);
            vh.mFechaPublicacion = (TextView)convertView.findViewById(R.id.tvFechaPublicacion_Inicio);
            vh.mNomPublicacion = (TextView)convertView.findViewById(R.id.tvPublicacion_Inicio);
            vh.mDescripcion = (TextView)convertView.findViewById(R.id.tvDescripcion_Inicio);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mImageViewUsuario.setImageBitmap(publicaciones.get(position).getImagen());
        vh.mNomUsuario.setText(publicaciones.get(position).getNomUsuario());
        vh.mFechaPublicacion.setText(publicaciones.get(position).getFechaPublicacion());
        vh.mNomPublicacion.setText(publicaciones.get(position).getNomPublicacion());
        vh.mDescripcion.setText(publicaciones.get(position).getDescripcion());

        return convertView;
    }

    static class ViewHolder{
        ImageView mImageViewUsuario;
        TextView mNomUsuario;
        TextView mFechaPublicacion;
        TextView mNomPublicacion;
        TextView mDescripcion;

    }
}