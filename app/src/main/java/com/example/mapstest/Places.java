package com.example.mapstest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Places
{
  private HashMap<String,String> getSingleNearByPlace(JSONObject googlePlaceJSON){
      HashMap<String,String> googlePlaceMap  =new HashMap<>();
      String NameOfPlace="-NA-";
      String vicinity ="-NA-";
      String latitude = "";
      String longitude ="";
      String refernce = "";
      try {
          if (!googlePlaceJSON.isNull("name")){
              NameOfPlace = googlePlaceJSON.getString("name");
          }
          if (!googlePlaceJSON.isNull("vicinity")){
              NameOfPlace = googlePlaceJSON.getString("vicinity");

          }
          latitude = googlePlaceJSON.getJSONObject("geometry")
                  .getJSONObject("location")
                  .getString("lat");
          longitude = googlePlaceJSON.getJSONObject("geometry")
                  .getJSONObject("location")
                  .getString("lng");
          refernce = googlePlaceJSON.getString("reference");
          googlePlaceMap.put("place_name",NameOfPlace);
          googlePlaceMap.put("vicinity",vicinity);
          googlePlaceMap.put("lat",latitude);
          googlePlaceMap.put("lng",longitude);
          googlePlaceMap.put("reference",refernce);

      } catch (JSONException e) {
          e.printStackTrace();
      }
   return googlePlaceMap;
  }
  private List<HashMap<String,String>> getAllNearByPlaces(JSONArray jsonArray){
      int counter = jsonArray.length();
      List<HashMap<String,String>> NearBYPlaceslist = new ArrayList<>();
      HashMap<String,String>NearByPlaceMap = null;
      for (int i =0;i<counter;i++){
          try {
              NearByPlaceMap = getSingleNearByPlace((JSONObject)jsonArray.get(i));
              NearBYPlaceslist.add(NearByPlaceMap);

          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
      return NearBYPlaceslist;
  }
  public List<HashMap<String,String>>parse(String jsonData){
      JSONArray jsonArray =null;
      JSONObject jsonObject ;
      try {
          jsonObject = new JSONObject(jsonData);
          jsonArray = jsonObject.getJSONArray("results");
      } catch (JSONException e) {
          e.printStackTrace();
      }
      return getAllNearByPlaces(jsonArray);
  }
//public List<HashMap<String, String>> parse(JSONObject jsonObject) {
//    JSONArray jsonArray = null;
//    try {
//        jsonArray = jsonObject.getJSONArray("results");
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//    return getPlaces(jsonArray);
//}
//
//    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
//        int placesCount = jsonArray.length();
//        List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
//        HashMap<String, String> placeMap = null;
//
//        for (int i = 0; i < placesCount; i++) {
//            try {
//                placeMap = getPlace((JSONObject) jsonArray.get(i));
//                placesList.add(placeMap);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return placesList;
//    }
//
//    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
//        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
//        String placeName = "-NA-";
//        String vicinity = "-NA-";
//        String latitude = "";
//        String longitude = "";
//        String reference = "";
//
//        try {
//            if (!googlePlaceJson.isNull("name")) {
//                placeName = googlePlaceJson.getString("name");
//            }
//            if (!googlePlaceJson.isNull("vicinity")) {
//                vicinity = googlePlaceJson.getString("vicinity");
//            }
//            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
//            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
//            reference = googlePlaceJson.getString("reference");
//            googlePlaceMap.put("place_name", placeName);
//            googlePlaceMap.put("vicinity", vicinity);
//            googlePlaceMap.put("lat", latitude);
//            googlePlaceMap.put("lng", longitude);
//            googlePlaceMap.put("reference", reference);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return googlePlaceMap;
//    }
}
