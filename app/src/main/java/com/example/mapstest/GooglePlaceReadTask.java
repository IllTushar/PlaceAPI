package com.example.mapstest;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mapstest.Model.Http;
import com.google.android.gms.maps.GoogleMap;

public class GooglePlaceReadTask extends AsyncTask<Object, Integer, String>
{
    String googlePlacesData = null;
    GoogleMap googleMap;

    @Override
    protected String doInBackground(Object... objects) {
        try {
            googleMap = (GoogleMap) objects[0];
            String googlePlacesUrl = (String) objects[1];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {
            Log.d("Google Place Read Task", e.toString());
        }
        return googlePlacesData;
    }
    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = result;
        placesDisplayTask.execute(toPass);
    }
}
