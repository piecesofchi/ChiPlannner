package com.example.chiplanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        TextView tvLanguage = view.findViewById(R.id.tvLanguage);
        TextView tvLocation = view.findViewById(R.id.tvLocation);

        tvLanguage.setOnClickListener(v -> showBottomSheet("Language"));
        tvLocation.setOnClickListener(v -> showBottomSheet("Location"));
    }

    private void showBottomSheet(String type) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = getLayoutInflater().inflate(R.layout.layout_bottom_sheet, null);

        TextView title = view.findViewById(R.id.sheetTitle);
        RadioGroup group = view.findViewById(R.id.radioGroupOptions);
        RadioButton opt1 = view.findViewById(R.id.opt1);
        RadioButton opt2 = view.findViewById(R.id.opt2);

        if (type.equals("Language")) {
            title.setText("Language");
            opt1.setText("English");
            opt2.setText("Indonesia");
        } else if (type.equals("Location")) {
            title.setText("Location");
            opt1.setText("GPS Location");
            opt2.setText("Indonesia");
        }

        group.setOnCheckedChangeListener((radioGroup, id) -> {
            RadioButton selected = view.findViewById(id);
            if (selected.getText().toString().equals("GPS Location")) {
                getLocation();
            } else {
                Toast.makeText(getContext(), "Selected: " + selected.getText(), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getDeviceLocation();
        }
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            String locationString = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                            Toast.makeText(getContext(), locationString, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Could not get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            } else {
                Toast.makeText(getContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
