package com.example.avion;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListeVolsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private ListView listView;
    private List<Avion> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listevols);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("flights");

        // Initialize UI components
        listView = findViewById(R.id.liste);
        flightList = new ArrayList<>();

        // Set up the adapter for the ListView
        ArrayAdapter<Avion> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flightList);
        listView.setAdapter(adapter);

        // Retrieve data from Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                flightList.clear();
                for (DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Avion flight = flightSnapshot.getValue(Avion.class);
                    flightList.add(flight);
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListeVolsActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
