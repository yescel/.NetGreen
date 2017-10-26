package com.tallerandroid.netgreen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by yesce on 01/10/2017.
 */

public class AdaptadorListaPatrocinadores  extends ArrayAdapter {
    Activity activity;
    ArrayList<Patrocinadores> aPatrocinadores;

    public AdaptadorListaPatrocinadores(Activity context, ArrayList<Patrocinadores> aPatrocinadores) {
        super(context, R.layout.item_listview_patrocinadores, aPatrocinadores);///agregar item lista
        this.activity = context;
        this.aPatrocinadores = aPatrocinadores;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AdaptadorListaPatrocinadores.ViewHolder vh = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_patrocinadores, null);

            vh = new AdaptadorListaPatrocinadores.ViewHolder();
            vh.mOrganismo = (TextView) convertView.findViewById(R.id.tvOrganismoPat);
            vh.mContacto = (TextView) convertView.findViewById(R.id.tvContactoPat);
            vh.mTelefono = (TextView) convertView.findViewById(R.id.tvTelefonoPat);

            convertView.setTag(vh);

        } else {
            vh = (AdaptadorListaPatrocinadores.ViewHolder) convertView.getTag();
        }
        vh.mOrganismo.setText(aPatrocinadores.get(position).getOrganismo());
        vh.mContacto.setText(aPatrocinadores.get(position).getContacto());
        vh.mTelefono.setText(aPatrocinadores.get(position).getTelefono());

        return convertView;
    }

    static class ViewHolder {
        TextView mOrganismo;
        TextView mContacto;
        TextView mTelefono;

    }
}