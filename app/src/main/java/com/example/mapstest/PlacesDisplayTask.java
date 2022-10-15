package com.example.mapstest;

import android.os.AsyncTask;

import com.example.mapstest.Model.Http;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object,String,String>
{
String googleNearByPlaceData;
GoogleMap googleMap;
String url;


   @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>>nearByPlacesList=null;
        Places dataParse = new Places();
        nearByPlacesList= dataParse.parse(s);

        googleMap.clear();
        for (int i = 0; i < nearByPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearByPlacesList.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            googleMap.addMarker(markerOptions);
        }
    }

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        Http downloadUrl = new Http();
        try {

            googleNearByPlaceData = downloadUrl.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleNearByPlaceData;
    }
//    private void DisplayNearbyPlaces(List<HashMap<String,String>>nearByPlacesList){
//        for (int i =0;i< nearByPlacesList.size();i++){
//            MarkerOptions markerOptions = new MarkerOptions();
//            HashMap<String,String> googleNearByPlace = nearByPlacesList.get(i);
//            String nameOfPlace = googleNearByPlace.get("place_name");
//            String vicinity = googleNearByPlace.get("vicinity");
//            double lat = Double.parseDouble(googleNearByPlace.get("lat"));
//            double lng = Double.parseDouble(googleNearByPlace.get("lng"));
//           markerOptions.title(nameOfPlace);
//           LatLng latLng =new LatLng(lat,lng);
//           markerOptions.position(latLng);
//            googleMap.addMarker(markerOptions);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,18f));
//
//
//
//        }
//    }
}
