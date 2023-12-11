package com.example.avion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.avion.Avion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListeReservationsFragment extends Fragment {

    private DatabaseReference databaseReference, db;
    private ListView listView;
    private List<Avion> flightList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(LoginActivity.UserUID).child("reservedFlights");
        flightList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liste_reservations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components using the root view of the fragment
        listView = view.findViewById(R.id.listeReservationClient);

        // Create a custom adapter with the custom layout for each item
        CustomAdapterReservations adapter = new CustomAdapterReservations(requireContext(), R.layout.adapter_item, flightList,getParentFragmentManager());
        listView.setAdapter(adapter);

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flightList.clear(); // Clear existing data
                for (DataSnapshot flightUidSnapshot : snapshot.getChildren()) {
                    String flightUid = flightUidSnapshot.getValue(String.class);
                    if (flightUid != null) {
                        // Retrieve flight data from "flights" node using the UID
                        DatabaseReference flightsRef = FirebaseDatabase.getInstance().getReference().child("flights").child(flightUid);
                        flightsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot flightSnapshot) {
                                if (flightSnapshot.exists()) {
                                    Avion avion = flightSnapshot.getValue(Avion.class);
                                    avion.setUid(flightSnapshot.getKey());
                                    if (avion != null) {
                                        flightList.add(avion);
                                        adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(requireContext(), "Database Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

