package com.tallerandroid.netgreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by yesce on 13/09/2017.
 */

public class MapaActivity extends AppCompatActivity implements OnMarkerClickListener, OnMapReadyCallback {
        private LatLngBounds NuevoLaredo = new LatLngBounds(
            new LatLng(27.396155  , -99.626083), new LatLng(27.612972 , -99.406357));

        private static final LatLng PLASTICOS1 = new LatLng(27.5164188, -99.58597839999999);
        private static final LatLng METALES2 = new LatLng(27.627573780299002, -99.63844299316406);
        private static final LatLng METALES3 = new LatLng(27.4822551, -99.56243669999998);
        private static final LatLng METALES4 = new LatLng(27.4551582, -99.53118159999997);
        private static final LatLng PLASTICOS5 = new LatLng(27.4850837, -99.51233730000001);
        private static final LatLng PAPELES6 = new LatLng(27.4780606, -99.53701460000002);
        private static final LatLng PAPELES7 = new LatLng(27.4969784, -99.49964949999998);
        private static final LatLng METALES8 = new LatLng(27.4783606, -99.53980869999998);


        private Marker mPlastico1;
        private Marker mMetal2;
        private Marker mMetal3;
        private Marker mMetal4;
        private Marker mPlastico5;
        private Marker mPapel6;
        private Marker mPapel7;
        private Marker mMetal8;

        private GoogleMap mMap;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_fragment_mapa);

            Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
            setSupportActionBar(toolbar);

            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);



        }

        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NuevoLaredo.getCenter(), 10));

            mPlastico1 = mMap.addMarker(new MarkerOptions()
                    .position(PLASTICOS1)
                    .title("Reciclaje plastico"));
            mPlastico1.setTag(0);
            mMetal2 = mMap.addMarker(new MarkerOptions()
                    .position(METALES2)
                    .title("Reciclaje metales"));
            mMetal2.setTag(0);
            mMetal3 = mMap.addMarker(new MarkerOptions()
                    .position(METALES3)
                    .title("Reciclaje metales"));
            mMetal3.setTag(0);
            mMetal4 = mMap.addMarker(new MarkerOptions()
                    .position(METALES4)
                    .title("Reciclaje metales"));
            mMetal4.setTag(0);
            mPlastico5 = mMap.addMarker(new MarkerOptions()
                    .position(PLASTICOS5)
                    .title("Reciclaje plasticos"));
            mPlastico5.setTag(0);
            mPapel6 = mMap.addMarker(new MarkerOptions()
                    .position(PAPELES6)
                    .title("Reciclaje papeles"));
            mPapel6.setTag(0);
            mPapel7 = mMap.addMarker(new MarkerOptions()
                    .position(PAPELES7)
                    .title("Reciclaje papeles"));
            mPapel7.setTag(0);
            mMetal8 = mMap.addMarker(new MarkerOptions()
                    .position(METALES8)
                    .title("Reciclaje metales"));
            mMetal8.setTag(0);

            mMap.setOnMarkerClickListener(this);
        }

    public boolean onMarkerClick(final Marker marker) {

        Integer clickCount = (Integer) marker.getTag();

        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}
