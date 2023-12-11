package com.example.avion;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
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
public class InfoAvionClientFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Avion selectedFlight;
    public InfoAvionClientFragment() {
        // Required empty public constructor
    }
    public InfoAvionClientFragment(Avion flight){
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
    public static InfoAvionClientFragment newInstance(String param1, String param2) {
        InfoAvionClientFragment fragment = new InfoAvionClientFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoAvionClientFragment newInstance(Avion flight) {
        InfoAvionClientFragment fragment = new InfoAvionClientFragment();
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
        return inflater.inflate(R.layout.fragment_info_avion_client, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Display the selected flight data in the fragment
        String depart,arrive,date,heureD,heureA;
        TextView infos;

        infos = view.findViewById(R.id.infoAvion);
        depart= selectedFlight.getDepart();
        arrive = selectedFlight.getArrive();
        date=selectedFlight.getDateD();
        heureD=selectedFlight.getHeureD();
        heureA=selectedFlight.getHeureA();
        Button reserverButton = view.findViewById(R.id.reserver);
        Button annulerButton = view.findViewById(R.id.annuler_client);

        infos.setText("Depart : "+depart+"\nArrive : "+arrive+"\nDate : " +date +"\nHeure Depart : "+heureD+"\nHeure Arrive : "+heureA);

        annulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement the logic for canceling and going back to the HomeFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                HomeClientFragment homeClientFragment = new HomeClientFragment();
                transaction.replace(R.id.frame_layout_client, homeClientFragment);
                transaction.addToBackStack(null);  // Add the transaction to the back stack
                transaction.commit();
            }
        });


        reserverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the UID of the user (you need to implement your own logic to get the user UID)
                String userUid = LoginActivity.UserUID; // Replace with your actual user UID retrieval logic

                // Get the list of reserved flights for the user from the database
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userUid);
                userRef.child("reservedFlights").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> reservedFlights = new ArrayList<>();

                        // Check if the user has any existing reserved flights
                        if (dataSnapshot.exists()) {
                            // If yes, add them to the list
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String reservedFlightUid = snapshot.getValue(String.class);
                                reservedFlights.add(reservedFlightUid);
                            }
                        }

                        // Add the UID of the selected flight to the list
                        reservedFlights.add(selectedFlight.getUid());

                        // Update the user's reserved flights in the Firebase Realtime Database
                        userRef.child("reservedFlights").setValue(reservedFlights)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Reservation successful
                                        Toast.makeText(requireContext(), "Reservation successful", Toast.LENGTH_SHORT).show();
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                                        HomeClientFragment homeFragment = new HomeClientFragment();
                                        transaction.replace(R.id.frame_layout_client, homeFragment);
                                        transaction.addToBackStack(null);  // Add the transaction to the back stack
                                        transaction.commit();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Reservation failed
                                        Toast.makeText(requireContext(), "Reservation failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });
            }
        });



    }
}