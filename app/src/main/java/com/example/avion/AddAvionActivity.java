package com.example.avion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class AddAvionActivity extends AppCompatActivity {

   /* private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vol);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("flights");

        // Your UI initialization code here
        EditText departEditText = findViewById(R.id.depart);
        EditText arriveEditText = findViewById(R.id.arrive);
        EditText dateDepartEditText = findViewById(R.id.datedepart);
        EditText heureDepartEditText = findViewById(R.id.heuredepart);
        EditText heureArriveEditText = findViewById(R.id.heurearrive);

        Button addButton = findViewById(R.id.ajouter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get flight information from UI (departure, arrival, etc.)
                String departure = departEditText.getText().toString();
                String arrival = arriveEditText.getText().toString();
                String dateDeparture = dateDepartEditText.getText().toString();
                String timeDeparture = heureDepartEditText.getText().toString();
                String timeArrival = heureArriveEditText.getText().toString();

                // Create a Flight object
                Avion avion = new Avion(departure, arrival, dateDeparture, timeDeparture, timeArrival);

                // Push the Flight object to the database
                databaseReference.push().setValue(avion);

                // You can also add a success message or navigate to another activity
                Toast.makeText(AddAvionActivity.this, "Flight added successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
