package umn.ac.shovia;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyTheatre extends AsyncTask<Object, String, String> {

    private String googlePlaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlaceData = downloadUrl.ReadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList = dataParser.parse(s);

        DisplayNearbyPlaces(nearbyPlacesList);
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){
        for(int i=0; i<nearbyPlacesList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyPlace = nearbyPlacesList.get(i);
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

            mMap.addMarker(markerOptions); // masukin lokasi sesuai marker
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)); // layarnya otomatis pindah ke lokasi sekarang
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12)); // layarnya ngezoom seberapa

            String reference = googleNearbyPlace.get("reference");
        }
    }
}
