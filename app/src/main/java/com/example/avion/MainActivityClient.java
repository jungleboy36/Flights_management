package com.example.avion;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.avion.databinding.ActivityMainClientBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityClient extends AppCompatActivity {

    ActivityMainClientBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeClientFragment());

        binding.bottomNavigationViewClient.setOnItemSelectedListener(item ->{
            if (item.getItemId() == binding.bottomNavigationViewClient.getSelectedItemId()) {
                return true;  // Do nothing if reselecting the same item
            }
            if (item.getItemId() == R.id.home_client) {
                replaceFragment(new HomeClientFragment());
            }
            else if (item.getItemId() == R.id.reservations) {
                replaceFragment(new ListeReservationsFragment());

            }
            else if (item.getItemId() == R.id.map_client) {
                replaceFragment(new MapFragment());

            }
            else if (item.getItemId() == R.id.logout_client){showLogoutConfirmationDialog();}

            return true;
        });
    }

    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_client,fragment);
        fragmentTransaction.commit();

    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, perform logout
                // Add your logout logic here, such as clearing session, Firebase logout, etc.
                LoginActivity.UserUID = null;
                // After logout, start the intent to the landing page
                Intent intent = new Intent(MainActivityClient.this, LandingPageActivity.class);
                startActivity(intent);
                // You might want to clear the back stack if needed
                // Intent intent = new Intent(requireContext(), LandingPageActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Cancel, do nothing
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}