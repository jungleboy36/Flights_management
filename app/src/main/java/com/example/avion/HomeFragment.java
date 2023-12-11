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

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private ListView listView;
    private List<Avion> flightList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("flights");
        flightList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components using the root view of the fragment
        listView = view.findViewById(R.id.listeAvion);

        // Create a custom adapter with the custom layout for each item
        CustomAdapter adapter = new CustomAdapter(requireContext(), R.layout.adapter_item, flightList,getParentFragmentManager());
        listView.setAdapter(adapter);

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flightList.clear(); // Move this line outside the loop if you want to append data
                for (DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Avion flight = flightSnapshot.getValue(Avion.class);
                    flight.setUid(flightSnapshot.getKey());
                    flightList.add(flight);
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

