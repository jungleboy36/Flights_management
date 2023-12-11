package com.example.avion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AddAvionFragment extends Fragment {

    private DatabaseReference databaseReference;

    public AddAvionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("flights");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_avion, container, false);

        // Your UI initialization code here
        EditText departEditText = view.findViewById(R.id.depart);
        EditText arriveEditText = view.findViewById(R.id.arrive);
        EditText dateDepartEditText = view.findViewById(R.id.datedepart);
        EditText heureDepartEditText = view.findViewById(R.id.heuredepart);
        EditText heureArriveEditText = view.findViewById(R.id.heurearrive);

        Button addButton = view.findViewById(R.id.ajouter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get flight information from UI (departure, arrival, etc.)
                String departure = departEditText.getText().toString();
                String arrival = arriveEditText.getText().toString();
                String dateDeparture = dateDepartEditText.getText().toString();
                String timeDeparture = heureDepartEditText.getText().toString();
                String timeArrival = heureArriveEditText.getText().toString();
                String flightId = databaseReference.push().getKey();
                // Create a Flight object
                Avion avion = new Avion(departure, arrival, dateDeparture, timeDeparture, timeArrival);

                // Push the Flight object to the database
                databaseReference.push().setValue(avion);

                // You can also add a success message or navigate to another activity
                Toast.makeText(requireContext(), "Flight added successfully!", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.frame_layout, homeFragment);
                transaction.addToBackStack(null);  // Add the transaction to the back stack
                transaction.commit();
            }

        });

        return view;
    }

}
