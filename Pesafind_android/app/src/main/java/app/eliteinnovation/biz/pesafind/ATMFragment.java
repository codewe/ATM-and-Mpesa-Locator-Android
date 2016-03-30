package app.eliteinnovation.biz.pesafind;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ATMFragment extends Fragment implements LocationListener{
    private LocationManager locationManager;
    Location location;
    double latitude, longitude;
    String provider;
    public static GoogleMap aMap;
    private boolean isGPSEnabled;
    // Getting reference to radio group
    private LatLng atmCoordinates, currentLocation;
    private static View rootView;
    private SeekBar seekBar;
    private TextView txtSeekbar;
    private ProgressDialog pDialog;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static String KEY_SUCCESS = "status";
    public static final String TAG_ATMS = "atms", TAG_NAME = "atmtype",
            TAG_ID = "atmid", TAG_BUILDING = "building", TAG_LAT = "latitude", TAG_LONG = "longitude",
            TAG_BANK = "bankid";

    JSONArray atms = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> atmDetails;
    //    private static String url = "http://eliteinnovations.biz/mapsap/fetch.php?sid=2";
    private static String url="http://eliteinnovations.biz/pesafind/retrieve.php?sid=3";

    private TextView anotherLabel, level;
    //private static int selectedBankID;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    Circle mapCircle;
    CircleOptions circleOptions;
    public static ATMFragment newInstance() {
        //selectedBankID = bankID;
        return new ATMFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_atm, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        //Getting LocationManager object
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if (provider != null && !provider.equals("")) {
            // Get the location from the given provider
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 20000, 1, this);
            if(location!=null)
                onLocationChanged(location);
            else
                Toast.makeText(getContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
        if(Build.VERSION.SDK_INT == 23) {
            int hasWriteMapPermission = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasWriteMapPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        checkGPSAndGetCurrentLatLong();
        //intialize arraylist to store hospital data
        atmDetails = new ArrayList<HashMap<String,String>>();
        //setup map
        new GetATMs().execute();
        return rootView;
    }
    private void checkGPSAndGetCurrentLatLong() {
        if (!isGPSEnabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

            // Setting Dialog Title
            alertDialog.setTitle("GPS settings");

            // Setting Dialog Message
            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
    }
    private void setupATMMap(ArrayList<HashMap<String, String>> details) {
        Marker currentMarker = null;
        if (aMap == null || aMap != null) {
            aMap = ((MapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.mapa)).getMap();
            // Add a marker in Nairobi and move the camera
            for (int i = 0; i < details.size(); i++) {
                String atmLatitude = details.get(i).get(TAG_LAT),
                        atmLongitude = details.get(i).get(TAG_LONG),
                        name = details.get(i).get(TAG_NAME),
                        bank=details.get(i).get(TAG_BANK),
                        building = details.get(i).get(TAG_BUILDING), address = "";
                atmCoordinates = new LatLng(Double.parseDouble(atmLatitude), Double.parseDouble(atmLongitude));
                MarkerOptions markerOptions = new MarkerOptions().position(atmCoordinates);
                try {
                    address = new Geocoder(getActivity(), Locale.getDefault()).
                            getFromLocation(Double.parseDouble(atmLatitude), Double.parseDouble(atmLongitude), 1).
                            get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException indexEXP){
                    address = "Unknown";
                }

                name = name + "#" + address + "#"+ building;
                markerOptions.title(name);

                switch (Integer.parseInt(bank)){
                    case 1:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.barclaysicon));
                        break;
                    case 2:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.equityicon));
                        break;
                    case 3:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.nic));
                        break;
                    case 4:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.standard));
                        break;
                    case 5:
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.kcb));
                        break;
                }

                currentMarker = aMap.addMarker(markerOptions);
            }

            //last hospital in the list
            currentLocation = new LatLng(latitude, longitude);

            aMap.setMyLocationEnabled(true);
            aMap.getUiSettings().setZoomControlsEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 14));
            circleOptions = new CircleOptions()
                    .center(currentLocation)
                    .radius(1000)
                    .strokeColor(Color.CYAN);
//                .fillColor(Color.GRAY)
            mapCircle = aMap.addCircle(circleOptions);
            aMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());

            aMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                    marker.showInfoWindow();
                    return true;
                }
            });

            aMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                }
            });


            currentMarker.showInfoWindow();
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            seekBar = (SeekBar) rootView.findViewById(R.id.seekBar1);
            txtSeekbar = (TextView) rootView.findViewById(R.id.txtSeek);
            txtSeekbar.setText("Circle Radius: 10 KM");
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                    progress = progresValue;
//                    Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
//                    Toast.makeText(getActivity(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    txtSeekbar.setText("Circle Radius: " + progress + " KM");
//                    Toast.makeText(getActivity(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                    //Implement change listener
                    if(mapCircle!=null){
                        mapCircle.remove();
                    }
                    circleOptions = new CircleOptions()
                            .center(currentLocation)
                            .radius(progress*1000)
                            .strokeColor(Color.CYAN);
                    mapCircle = aMap.addCircle(circleOptions);
                    Toast.makeText(getActivity(), "Stopped tracking seekbar: "+progress*1000, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        isGPSEnabled = true;
        Toast.makeText(getContext(), "Latitude: "+location.getLatitude()+" Longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker)
        {
            View v  = getActivity().getLayoutInflater().inflate(R.layout.mapinfowindow, null);

//            MyMarker myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            anotherLabel = (TextView)v.findViewById(R.id.another_label);
            level = (TextView)v.findViewById(R.id.updatetime);

            try {
                markerIcon.setImageResource(R.mipmap.icon);
                markerLabel.setText(marker.getTitle().split("#")[0]);
            }catch (NullPointerException exp){
                markerIcon.setImageResource(R.mipmap.icon);
                markerLabel.setText("Hospital");
            }
            anotherLabel.setText("Location: "+marker.getTitle().split("#")[1]);
            level.setText(marker.getTitle().split("#")[2]);

            return v;
        }
    }
    private class GetATMs extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String id, name, building, latitude, longitude, bank;
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    final JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node for the object we called student
                    atms = jsonObj.getJSONArray(TAG_ATMS);

                    if(Integer.parseInt(jsonObj.getString(KEY_SUCCESS)) != 0) {
                        // looping through All Contacts
                        for (int i = 0; i < atms.length(); i++) {
                            JSONObject s = atms.getJSONObject(i);

                            id = s.getString(TAG_ID);
                            name = s.getString(TAG_NAME);
                            building=s.getString(TAG_BUILDING);
                            latitude = s.getString(TAG_LAT);
                            longitude = s.getString(TAG_LONG);
                            bank = s.getString(TAG_BANK);

                            // tmp hashmap for single hospital
                            HashMap<String, String> atm = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            atm.put(TAG_ID, id);
                            atm.put(TAG_NAME, name);
                            atm.put(TAG_LAT, latitude);
                            atm.put(TAG_LONG, longitude);
                            atm.put(TAG_BUILDING, building);
                            atm.put(TAG_BANK, bank);

                            // adding hosi to hosi list
                            atmDetails.add(atm);
                        }
                    }else{
                        //no malls found
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                try {
                                    alert.showAlertDialog(
                                            getActivity(),
                                            "Service response",
                                            jsonObj.getString("message"),
                                            false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismiss();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //no malls found
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        alert.showAlertDialog(
                                getActivity(),
                                "Service response",
                                "No records found",
                                false);
                        pDialog.dismiss();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setupATMMap(atmDetails);
        }

    }
}
