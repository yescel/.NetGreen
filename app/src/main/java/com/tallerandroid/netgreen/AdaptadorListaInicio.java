package com.tallerandroid.netgreen;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yesce on 14/05/2017.
 */

public class AdaptadorListaInicio extends ArrayAdapter {
    Activity activity;
    ArrayList<Inicio> publicacionesArrayLista;
    List<Inicio> publicacionesLista = null;


    public AdaptadorListaInicio(Activity context, List<Inicio> publicacionesLista){
        super(context, R.layout.item_listview_inicio, publicacionesLista);
        this.activity = context;
        this.publicacionesLista = publicacionesLista;
        //this.publicacionesArrayLista = new ArrayList<Inicio>();
        //this.publicacionesArrayLista.addAll(publicacionesLista);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;
        this.publicacionesArrayLista = new ArrayList<Inicio>();
        this.publicacionesArrayLista.addAll(publicacionesLista);

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_inicio, null);

            vh = new ViewHolder();
            vh.mImageViewUsuario = (ImageView)convertView.findViewById(R.id.ivUsuario);
            vh.mNomUsuario = (TextView)convertView.findViewById(R.id.tvUsuario_Inicio);
            vh.mFechaPublicacion = (TextView)convertView.findViewById(R.id.tvFechaPublicacion_Inicio);
            vh.mNomPublicacion = (TextView)convertView.findViewById(R.id.tvPublicacion_Inicio);
            vh.mDescripcion = (TextView)convertView.findViewById(R.id.tvDescripcion_Inicio);
            vh.mIdPublicacion = (TextView) convertView.findViewById(R.id.tvIdPublicacion);
            vh.mTipoPublicacion = (TextView) convertView.findViewById(R.id.tvTipoPubblicacion);
            vh.mEstatus = (TextView) convertView.findViewById(R.id.tvEstatus);
            vh.mVerMas = (TextView) convertView.findViewById(R.id.tvVerMas_Inicio);

            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mImageViewUsuario.setImageBitmap(publicacionesArrayLista.get(position).getImagen());
        vh.mNomUsuario.setText(publicacionesArrayLista.get(position).getNomUsuario());
        vh.mFechaPublicacion.setText(publicacionesArrayLista.get(position).getFechaPublicacion());
        vh.mNomPublicacion.setText(publicacionesArrayLista.get(position).getNomPublicacion());
        vh.mDescripcion.setText(publicacionesArrayLista.get(position).getDescripcion());
        vh.mIdPublicacion.setText(publicacionesArrayLista.get(position).getIdPublicacion());
        vh.mTipoPublicacion.setText(publicacionesArrayLista.get(position).getTipo());
        vh.mVerMas.setText(publicacionesArrayLista.get(position).getVerMas());
        vh.mEstatus.setText(publicacionesArrayLista.get(position).getEstatus());

        if(publicacionesArrayLista.get(position).getEstatus()!= null)
        {
            if(publicacionesArrayLista.get(position).getEstatus().equals("en curso")) {
                vh.mEstatus.setVisibility(View.VISIBLE);
                vh.mVerMas.setVisibility(View.INVISIBLE);
                vh.mEstatus.setTextColor(Color.rgb(255, 128, 0));
            }
            if(publicacionesArrayLista.get(position).getEstatus().equals("cancelada")) {
                vh.mEstatus.setVisibility(View.VISIBLE);
                vh.mVerMas.setVisibility(View.INVISIBLE);
                vh.mEstatus.setTextColor(Color.RED);
            }
            if(publicacionesArrayLista.get(position).getEstatus().equals("completada")) {
                vh.mEstatus.setVisibility(View.VISIBLE);
                vh.mVerMas.setVisibility(View.INVISIBLE);
                vh.mEstatus.setTextColor(Color.GREEN);
            }
        }
        else
        {
            vh.mVerMas.setVisibility(View.VISIBLE);
            vh.mEstatus.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView mImageViewUsuario;
        TextView mNomUsuario;
        TextView mFechaPublicacion;
        TextView mNomPublicacion;
        TextView mDescripcion;
        TextView mIdPublicacion;
        TextView mTipoPublicacion;
        TextView mEstatus;
        TextView mVerMas;

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        publicacionesLista.clear();
        if (charText.length() == 0) {
            publicacionesLista.addAll(publicacionesArrayLista);
        } else {
            for (Inicio wp : publicacionesArrayLista) {
                if (wp.getNomPublicacion().toLowerCase(Locale.getDefault()).contains(charText)) {
                    publicacionesLista.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}