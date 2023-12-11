package com.example.avion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoAvionClientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoReservationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Avion selectedFlight;
    public InfoReservationFragment() {
        // Required empty public constructor
    }
    public InfoReservationFragment(Avion flight){
        this.selectedFlight = flight;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoAvionClientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoReservationFragment newInstance(String param1, String param2) {
        InfoReservationFragment fragment = new InfoReservationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoReservationFragment newInstance(Avion flight) {
        InfoReservationFragment fragment = new InfoReservationFragment();
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
        return inflater.inflate(R.layout.fragment_info_reservation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Display the selected flight data in the fragment
        String depart,arrive,date,heureD,heureA;
        TextView infos;

        infos = view.findViewById(R.id.infoReservation1);
        depart= selectedFlight.getDepart();
        arrive = selectedFlight.getArrive();
        date=selectedFlight.getDateD();
        heureD=selectedFlight.getHeureD();
        heureA=selectedFlight.getHeureA();
        Button supprimerButton = view.findViewById(R.id.supprimerReservation);
        Button annulerButton = view.findViewById(R.id.annuler_client2);

        infos.setText("Depart : "+depart+"\nArrive : "+arrive+"\nDate : " +date +"\nHeure Depart : "+heureD+"\nHeure Arrive : "+heureA);

        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for canceling and going back to the HomeFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                ListeReservationsFragment listeFragment = new ListeReservationsFragment();
                transaction.replace(R.id.frame_layout_client, listeFragment);
                transaction.addToBackStack(null);  // Add the transaction to the back stack
                transaction.commit();
            }
        });


        supprimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userUid = LoginActivity.UserUID;
                String flightUid = selectedFlight.getUid();

                // Get the reference to the reservedFlights node for the current user
                DatabaseReference reservedFlightsRef = FirebaseDatabase.getInstance().getReference()
                        .child("users")
                        .child(userUid)
                        .child("reservedFlights");

                // Retrieve all flight UIDs from the reservedFlights node
                reservedFlightsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            List<String> flightUids = new ArrayList<>();

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String uid = snapshot.getValue(String.class);
                                flightUids.add(uid);
                            }

                            // Remove the specific flight UID
                            flightUids.remove(flightUid);

                            // Update the reservedFlights node with the new list of flight UIDs
                            reservedFlightsRef.setValue(flightUids)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Flight UID removed successfully
                                            Toast.makeText(requireContext(), "Flight removed from reservations", Toast.LENGTH_SHORT).show();

                                            // Now you might want to navigate back to the HomeFragment or refresh the UI
                                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                            FragmentTransaction transaction = fragmentManager.beginTransaction();

                                            ListeReservationsFragment homeClientFragment = new ListeReservationsFragment();
                                            transaction.replace(R.id.frame_layout_client, homeClientFragment);
                                            transaction.addToBackStack(null);  // Add the transaction to the back stack
                                            transaction.commit();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle failure to update the reservedFlights node
                                            Toast.makeText(requireContext(), "Failed to update reservations", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle the case where reservedFlights node does not exist
                            Toast.makeText(requireContext(), "No reserved flights found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle potential errors
                        Toast.makeText(requireContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





    }
}