package com.tallerandroid.netgreen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by yesce on 01/10/2017.
 */

public class AdaptadorListaNotificaciones extends ArrayAdapter {
    Activity activity;
    ArrayList<Notificacion> aNotificaciones;

    public AdaptadorListaNotificaciones(Activity context, ArrayList<Notificacion> aNotificaciones){
        super(context, R.layout.item_listview_notificaciones, aNotificaciones);///agregar item lista
        this.activity = context;
        this.aNotificaciones = aNotificaciones;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AdaptadorListaNotificaciones.ViewHolder vh = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_notificaciones, null);

            vh = new AdaptadorListaNotificaciones.ViewHolder();
            vh.mUsuarioNotificacion = (TextView)convertView.findViewById(R.id.tvUsuario_Notificacion);
            vh.mAccionNotificacion = (TextView)convertView.findViewById(R.id.tvAccion_Notificacion);
            vh.mActNotNotificacion = (TextView)convertView.findViewById(R.id.tvAct_Not_Notificacion);

            convertView.setTag(vh);

        } else {
            vh = (AdaptadorListaNotificaciones.ViewHolder) convertView.getTag();
        }
        vh.mUsuarioNotificacion.setText(aNotificaciones.get(position).getUsuario());
        vh.mAccionNotificacion.setText(aNotificaciones.get(position).getAccion());
        vh.mActNotNotificacion.setText(aNotificaciones.get(position).getActividad_noticia());

        return convertView;
    }

    static class ViewHolder{
        TextView mUsuarioNotificacion;
        TextView mAccionNotificacion;
        TextView mActNotNotificacion;

    }
}