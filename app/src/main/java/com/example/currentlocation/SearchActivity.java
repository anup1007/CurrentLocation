package com.example.currentlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import model.model;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AutoCompleteTextView etcity;
    private Button btnSearch;
    private List<model> modelList;
    Marker markerName;
    CameraUpdate center, zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        etcity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etcity.getText().toString())){
                    etcity.setError("please enter a place name");
                    return;
                }
                //get the currrent location of the place
                int position = SearchArrayList(etcity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this ,"location not found"
                    + etcity.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//load kathmandu city when application launches
      mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172453,85.3239605));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void fillArrayListAndSetAdapter(){
        modelList = new ArrayList<>();
        modelList.add(new model(27.6617812,85.2841827,"Whoopee Land"));

        String[] data = new String[modelList.size()];

        for ( int i = 0 ; i < data.length; i++){
            data[i] = modelList.get(i).getMarker();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
          SearchActivity.this,
                android.R.layout.simple_list_item_1,
                data
        );
        etcity.setAdapter(adapter);
        etcity.setThreshold(1);
    }

    public int SearchArrayList(String name){
        for (int i = 0; i < modelList.size();i++){
            if (modelList.get(i).getMarker().contains(name)){
                return i;
            }
        }
        return -1;
    }

    public void loadMap(int position){
        //Remove old marker from map
        if (markerName!=null){
            markerName.remove();
        }
        double latitude = modelList.get(position).getLat();
        double longitude = modelList.get(position).getLon();
        String marker = modelList.get(position).getMarker();
        center = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position(new LatLng
                (latitude,longitude)).title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
