package com.example.mapstest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


//import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.mapstest.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    double currentLat, currentLog ;
    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

       binding.Resturant.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String resturant  =binding.resSearch.getText().toString();
             String url=getUrl(currentLat,currentLog,resturant);
             Object tranForDate[] =new Object[2];
             PlacesDisplayTask featchData = new PlacesDisplayTask();
             tranForDate[0] = mMap;
             tranForDate[1] = url;
             featchData.execute(tranForDate);
               Toast.makeText(MapsActivity.this, "Searching for nearby Resturenat", Toast.LENGTH_SHORT).show();

               Toast.makeText(MapsActivity.this, "Showing for nearby Resturenat", Toast.LENGTH_SHORT).show();
             mMap.clear();






           }
       });
         binding.btnSearch.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 switch (v.getId()){
                     case R.id.btn_search:
                         EditText addressField = findViewById(R.id.search);
                         String Address  = addressField.getText().toString();
                         if (Address.isEmpty()){
                             Toast.makeText(MapsActivity.this, "Enter Address!!", Toast.LENGTH_SHORT).show();
                         }
                         else if(!TextUtils.isEmpty(Address)){
                             ArrayList<Address>list = null;
                             Geocoder geocoder = new Geocoder(MapsActivity.this);
                             try {
                                 list = (ArrayList<Address>) geocoder.getFromLocationName(Address,5);
                                 if (list!=null){
                                     for (int i =0;i<list.size();i++)
                                     {
                                         Address userAddress = list.get(i);
                                         LatLng latLng = new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                                         MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                                 .title(Address);
                                         mMap.addMarker(markerOptions);
                                         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
                                     }
                                 }else{
                                     Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                                 }
                             }catch (Exception e){
                                 e.printStackTrace();
                             }
                         }
                         break;
                 }
             }
         });









        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Initialize fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Check Permission..
        if (ActivityCompat.checkSelfPermission(MapsActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // When Permission granted
            getCurrentLocation();

        } else {
            //when permission denied..
            //Request Permission
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }

    private String getUrl(double currentLat, double currentLog, String nearbyPlace) {
     StringBuilder googleUrl= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
  //   googleUrl.append("?keyword"+"restaurant");
     googleUrl.append("location="+currentLat+","+currentLog);
     googleUrl.append("&radius=50000");
        googleUrl.append("&type="+nearbyPlace);
         googleUrl.append("&sensor=true");
        googleUrl.append("&key="+"AIzaSyBJrXVMcCLrv8grU1IXvikp2luyxDfVnQY");
        Log.d("GoogleMapsActivity","url="+googleUrl.toString());

        return googleUrl.toString();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permission Granted
                getCurrentLocation();
            }
        }
    }
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location>task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
             if (location!=null){
                 currentLat = location.getLatitude();
                 currentLog = location.getLongitude();
                 mapFragment.getMapAsync(new OnMapReadyCallback() {
                     @Override
                     public void onMapReady(GoogleMap googleMap) {
                         mMap = googleMap;
                         LatLng latLng = new LatLng(currentLat,currentLog);
                         MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                 .title("Current Location");
                         mMap.addMarker(markerOptions);
                         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
                     }
                 });
             }

            }
        });
    }


}