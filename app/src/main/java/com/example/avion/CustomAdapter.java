package com.example.avion;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Avion> {

    private Context context;
    private int resource;
    private FragmentManager fragmentManager; // Add this field

    // Modify the constructor to accept FragmentManager
    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Avion> objects, FragmentManager fragmentManager) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.fragmentManager = fragmentManager; // Initialize the field
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_item, null);
        }

        Avion flight = getItem(position);
        Log.d("3710", flight.toString());
        if (flight != null) {
            // Populate the TextView with flight information
            TextView textFlightInfo = view.findViewById(R.id.textFlightInfo);
            textFlightInfo.setText(flight.toString());

            // Set up the icon click listener
            ImageView iconNavigate = view.findViewById(R.id.iconNavigate);
            iconNavigate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle icon click (navigate to another fragment)
                    Avion flight = getItem(position);

                    UpdateAvionFragment updateFlightFragment = new UpdateAvionFragment(flight);
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.frame_layout, updateFlightFragment);
                    transaction.replace(R.id.frame_layout, updateFlightFragment);
                    transaction.commit();
                    // Add your code to navigate to another fragment here
                }
            });
        } else {
            TextView textFlightInfo = view.findViewById(R.id.textFlightInfo);
            textFlightInfo.setText("null");
        }

        return view;
    }
}
