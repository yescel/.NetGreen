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
 * Created by yesce on 16/05/2017.
 */

public class AdaptadorListaRanking extends ArrayAdapter {
    Activity activity;
    ArrayList<Ranking> aRanking;

    public AdaptadorListaRanking(Activity context, ArrayList<Ranking> aRanking){
        super(context, R.layout.item_listview_inicio, aRanking);
        this.activity = context;
        this.aRanking = aRanking;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        AdaptadorListaRanking.ViewHolder vh = null;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_listview_ranking, null);

            vh = new AdaptadorListaRanking.ViewHolder();
            vh.mImageViewUsuario = (ImageView)convertView.findViewById(R.id.ivUsuario_Ranking);
            vh.mUsuario = (TextView)convertView.findViewById(R.id.tvUsuario_Ranking);
            vh.mPuntos = (TextView)convertView.findViewById(R.id.tvPuntos_Ranking);
            vh.mInsignia = (ImageView) convertView.findViewById(R.id.ivInsignia_Ranking);


            convertView.setTag(vh);

        } else {
            vh = (AdaptadorListaRanking.ViewHolder) convertView.getTag();
        }
        vh.mImageViewUsuario.setImageBitmap(aRanking.get(position).getImagen());
        vh.mUsuario.setText(aRanking.get(position).getNomUsuario());
        vh.mPuntos.setText(aRanking.get(position).getPuntos());
        vh.mInsignia.setImageDrawable(aRanking.get(position).getIvInsignida());

        return convertView;
    }

    static class ViewHolder{
        ImageView mImageViewUsuario;
        TextView mUsuario;
        TextView mPuntos;
        ImageView mInsignia;

    }
}