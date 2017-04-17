package com.example.dell.map1;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    GoogleMap mgooglemap;
    GoogleApiClient mgoolgeapiclient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleSevicesAviliable()) {
            Toast.makeText(this, "connection successful  ", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
            initMap();
        }
        //  Button bt = (Button) findViewById(R.id.button2);


    }


    public void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleSevicesAviliable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isavaliable = api.isGooglePlayServicesAvailable(this);
        if (isavaliable == ConnectionResult.SUCCESS) {

            return true;
        } else if (api.isUserResolvableError(isavaliable)) {
            Dialog dialog = api.getErrorDialog(this, isavaliable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "cannt connect to paly services ", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap = googleMap;
//       if(mgooglemap!=null){
//            mgooglemap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
//                @Override
//                public void onMapLongClick(LatLng latLng) {
//                    MainActivity.this.setMaker("local",latLng.longitude,latLng.longitude);
//                }
//            });
//        }
        if (mgooglemap != null) {
            mgooglemap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Geocoder gc = new Geocoder(MainActivity.this);
                    LatLng ll = marker.getPosition();
                    double lat = ll.latitude;
                    double lng = ll.longitude;
                    List<Address> list = null;
                    try {
                        list = gc.getFromLocation(lat, lng, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();

                }
            });
        }
        if (mgooglemap != null) {
            mgooglemap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView tvlocality = (TextView) v.findViewById(R.id.textView);
                    TextView tvlat = (TextView) v.findViewById(R.id.textView2);
                    TextView tvlng = (TextView) v.findViewById(R.id.textView3);
                    TextView tvsnappet = (TextView) v.findViewById(R.id.textView4);

                    LatLng ll = marker.getPosition();
                    tvlocality.setText(marker.getTitle());
                    tvlat.setText("latitute :" + ll.latitude);
                    tvlng.setText("longitude :" + ll.longitude);
                    tvsnappet.setText(marker.getSnippet());

                    return v;
                }
            });

        }

        //  goToLocationzoom(39.008224,-76.8984527,15);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
// TO CREAT AUTO DETECTION USER LOCATION ON GOOGLE MAP
        mgooglemap.setMyLocationEnabled(true);
//        mgoolgeapiclient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        mgoolgeapiclient.connect();

    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mgooglemap.moveCamera(update);

    }

    private void goToLocationzoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mgooglemap.moveCamera(update);

    }

    Marker marker1;
    Marker marker2;
    Polyline line;

    public void geoLocation(View view) throws IOException {
        EditText etc = (EditText) findViewById(R.id.editText);
        String location = etc.getText().toString();
        Geocoder gc = new Geocoder(MainActivity.this);
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationzoom(lat, lng, 2);
        // To Add A Marker on Map
        setMaker(locality, lat, lng);
    }

    Circle circle;

    private void setMaker(String locality, double lat, double lng) {
        //  if(marker!=null){
        //  marker.remove();
        // removeverything();
        //  }
        MarkerOptions options = new MarkerOptions()
                .draggable(true)
                .title(locality)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .position(new LatLng(lat, lng))
                .snippet("i am here");
        // marker = mgooglemap.addMarker(options);

        if (marker1 == null) {
            marker1 = mgooglemap.addMarker(options);
        } else if (marker2 == null) {
            marker2 = mgooglemap.addMarker(options);
            drawline();
        } else {
            removeverything();
            marker1 = mgooglemap.addMarker(options);
        }
        // TO ADD CIRCLE ON LOCATED MAP
        circle = drawCircle(new LatLng(lat, lng));
    }

    // TO DREW A LINE BETWEEN TWO LOCATION ON MAP
    private void drawline() {
        PolylineOptions option = new PolylineOptions()
                .add(marker1.getPosition())
                .add(marker2.getPosition())
                .color(Color.BLUE)
                .width(3);
        line = mgooglemap.addPolyline(option);
    }

    private Circle drawCircle(LatLng latLng) {
        CircleOptions circleoption = new CircleOptions()
                .center(latLng)
                .radius(100)
                .fillColor(0x33FF0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);
        return mgooglemap.addCircle(circleoption);
    }

    public void removeverything() {
        marker1.remove();
        marker1 = null;
        marker2.remove();
        marker2 = null;
        circle.remove();
        circle = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeTerrain:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeSettelite:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeHybrid:
                mgooglemap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


       LocationRequest mlocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = LocationRequest.create();
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setInterval(1000);
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
       LocationServices.FusedLocationApi.requestLocationUpdates(mgoolgeapiclient, mlocationRequest, (com.google.android.gms.location.LocationListener) this);
       }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    //@Override
    public void onLocationChanged(Location location) {
        if(location==null){
            Toast.makeText(this, "cann't find location ", Toast.LENGTH_SHORT).show();

        }else{
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            CameraUpdate update =CameraUpdateFactory.newLatLngZoom(ll,15);
            mgooglemap.animateCamera(update);
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}


