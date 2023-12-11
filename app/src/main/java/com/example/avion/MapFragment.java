package com.example.avion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap myMap;
    private List<Airport> airports = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateAirports();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.frame_layout, mapFragment).commit();
        }
        mapFragment.getMapAsync(MapFragment.this);

        Button zoomInButton = view.findViewById(R.id.zoomInButton);
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });

        // Add zoom out button
        Button zoomOutButton = view.findViewById(R.id.zoomOutButton);
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });

        return view;
    }

    private void zoomIn() {
        if (myMap != null) {
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    private void zoomOut() {
        if (myMap != null) {
            myMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng sydney = new LatLng(36.4368023, 10.6722175);
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (!airports.isEmpty()) {
            for (Airport airport : airports) {
                myMap.addMarker(new MarkerOptions().position(airport.getGeoPoint()).title(airport.getName()));
            }
        }
    }

    private void populateAirports() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("airports");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot airportSnapshot : snapshot.getChildren()) {
                    // Assuming your Airport class has a constructor that takes DataSnapshot
                    Airport airport = airportSnapshot.getValue(Airport.class);
                    airports.add(airport);
                }

                addMarkersToMap();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void addMarkersToMap() {

        if (myMap != null && !airports.isEmpty()) {
            for (Airport airport : airports) {
                myMap.addMarker(new MarkerOptions().position(airport.getGeoPoint()).title(airport.getName()));
                System.out.println("AirportData"+ "Airport Name: " + airport.getName()+" Coordinates :"+airport.getGeoPoint());

            }
        }
    }

}
