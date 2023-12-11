package com.example.avion;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateAvionFragment extends Fragment {

    private Avion selectedFlight;

    public UpdateAvionFragment() {
        // Required empty public constructor
    }
    public UpdateAvionFragment(Avion flight){
        this.selectedFlight = flight;
    }
    public static UpdateAvionFragment newInstance(Avion flight) {
        UpdateAvionFragment fragment = new UpdateAvionFragment();
        Bundle args = new Bundle();
        args.putParcelable("selectedFlight", (Parcelable) flight);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedFlight = getArguments().getParcelable("selectedFlight");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_avion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Display the selected flight data in the fragment
        EditText depart,arrive,date,heureD,heureA;
        depart = view.findViewById(R.id.depart2);
        arrive = view.findViewById(R.id.arrive2);
        date = view.findViewById(R.id.datedepart2);
        heureD = view.findViewById(R.id.heuredepart2);
        heureA = view.findViewById(R.id.heurearrive2);

        depart.setText(selectedFlight.getDepart());
        arrive.setText(selectedFlight.getArrive());
        date.setText(selectedFlight.getDateD());
        heureD.setText(selectedFlight.getHeureD());
        heureA.setText(selectedFlight.getHeureA());
        Button modifierButton = view.findViewById(R.id.modifier);
        Button supprimerButton = view.findViewById(R.id.supprimer);
        Button annulerButton = view.findViewById(R.id.annuler);
        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for canceling and going back to the HomeFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.frame_layout, homeFragment);
                transaction.addToBackStack(null);  // Add the transaction to the back stack
                transaction.commit();
            }
        });

        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for deleting the flight
                DatabaseReference flightsRef = FirebaseDatabase.getInstance().getReference().child("flights");

                // Retrieve the UID of the selected flight
                String flightUid = selectedFlight.getUid();

                // Check if the UID is not null or empty before attempting to delete
                if (flightUid != null && !flightUid.isEmpty()) {
                    // Construct the reference to the specific flight using the UID
                    DatabaseReference flightToDeleteRef = flightsRef.child(flightUid);

                    // Remove the flight
                    flightToDeleteRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Flight deleted successfully
                                    Toast.makeText(requireContext(), "Flight deleted successfully", Toast.LENGTH_SHORT).show();

                                    // Now you might want to navigate back to the HomeFragment
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    HomeFragment homeFragment = new HomeFragment();
                                    transaction.replace(R.id.frame_layout, homeFragment);
                                    transaction.addToBackStack(null);  // Add the transaction to the back stack
                                    transaction.commit();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure to delete the flight
                                    Toast.makeText(requireContext(), "Failed to delete flight", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Handle the case where the UID is null or empty
                    Toast.makeText(requireContext(), "Flight UID is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });


        modifierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the UID of the selected flight
                String flightUid = selectedFlight.getUid();

                // Check if the UID is not null or empty before attempting to update
                if (flightUid != null && !flightUid.isEmpty()) {
                    // Construct the reference to the specific flight using the UID
                    DatabaseReference flightToUpdateRef = FirebaseDatabase.getInstance().getReference().child("flights").child(flightUid);

                    // Retrieve updated data from the UI components
                    String updatedDepart = depart.getText().toString();
                    String updatedArrive = arrive.getText().toString();
                    String updatedDate = date.getText().toString();
                    String updatedHeureD = heureD.getText().toString();
                    String updatedHeureA = heureA.getText().toString();
                    if(updatedDepart.isEmpty() || updatedArrive.isEmpty() || updatedDate.isEmpty() || updatedHeureA.isEmpty() || updatedHeureD.isEmpty())
                        Toast.makeText(getContext(), "Please fill all the fields !", Toast.LENGTH_SHORT).show();
                    else{
                        // Create a new Avion object with the updated data
                    Avion updatedFlight = new Avion(updatedDepart, updatedArrive, updatedDate, updatedHeureD, updatedHeureA);

                    // Update the flight in the database
                    flightToUpdateRef.setValue(updatedFlight)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Flight updated successfully
                                    Toast.makeText(requireContext(), "Flight updated successfully", Toast.LENGTH_SHORT).show();

                                    // Now you might want to navigate back to the HomeFragment
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction = fragmentManager.beginTransaction();

                                    HomeFragment homeFragment = new HomeFragment();
                                    transaction.replace(R.id.frame_layout, homeFragment);
                                    transaction.addToBackStack(null);  // Add the transaction to the back stack
                                    transaction.commit();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure to update the flight
                                    Toast.makeText(requireContext(), "Failed to update flight", Toast.LENGTH_SHORT).show();
                                }
                            });}
                } else {
                    // Handle the case where the UID is null or empty
                    Toast.makeText(requireContext(), "Flight UID is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
