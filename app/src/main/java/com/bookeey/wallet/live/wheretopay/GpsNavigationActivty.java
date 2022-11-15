package com.bookeey.wallet.live.wheretopay;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bookeey.wallet.live.GPSTracker;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Locale;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericFragmentActivity;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.wheretopay.BranchDetailsPojo;

/**
 * Created by 30099 on 4/13/2016.
 */
public class GpsNavigationActivty extends GenericFragmentActivity implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, LocationListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
    private GoogleMap googleMap;
    com.google.android.gms.location.LocationListener listener;
    Boolean gps_enabled;
    private GoogleApiClient googleApiClient;
    GPSTracker gps;
    private DisplayImageOptions options;
    Geocoder geocoder;
    HashMap<String, BranchDetailsPojo> hashMap;
    double c_lat, c_long;
    double current_lat = 29.370659;
    double current_lon = 48.005223;
    TextView nearest_map_branchName, nearest_map_branchLocation;
    ImageView nearest_map_image;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    ImageView merchant_category_screen_wallet_logo_back, home_up_back;
    BranchDetailsPojo branchDetailsPojo=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearestmarchant_map);
        googleMap = null;
        hashMap = new HashMap<>();
       /* getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setTitle("            MERCHANT LOCATION");*/

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.map_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);


        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.where_to_pay_merchant_location_title));
//
        String imagepath = ((CoreApplication) getApplication()).getImagePath();
        branchDetailsPojo = ((CoreApplication) getApplication()).getBranchDetailsPojo();
        hashMap = new HashMap<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        gps = new GPSTracker(getBaseContext());


        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        nearest_map_branchName = (TextView) findViewById(R.id.nearest_map_branchName);
        nearest_map_branchLocation = (TextView) findViewById(R.id.nearest_map_branchLocation);
//        Log.e("string", ((CoreApplication) getApplication()).getBranchLocation());
        nearest_map_image = (ImageView) findViewById(R.id.nearest_map_image);
        nearest_map_branchName.setText(((CoreApplication) getApplication()).getMerchantName());
        nearest_map_branchLocation.setText(((CoreApplication) getApplication()).getBranchLocation());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.new_logo_virtual_grey)
                .showImageForEmptyUri(R.drawable.new_logo_virtual_grey)
                .showImageOnFail(R.drawable.new_logo_virtual_grey)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance()
                .displayImage(imagepath, nearest_map_image, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    }
                });
        check_gps();
        if (gps_enabled) {
            current_lat = gps.getLatitude();//current lattitude
            current_lon = gps.getLongitude();//current longitude
            LatLng currentpos = new LatLng(current_lat, current_lon);
            geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        }
        if (googleMap == null) {

            SupportMapFragment  mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nearest_map);
            mapFragment.getMapAsync(this);

            /*googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nearest_map)).getMap();*/

            /*googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(29.3697, 47.9783))
                    .zoom(8)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMarkerDragListener(this);*/
        }
        ((Button) findViewById(R.id.nearestmerchantmap_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                String uri_str = "http://maps.google.com/maps?saddr=" + branchDetailsPojo.getLatitude() + "," + branchDetailsPojo.getLongitude() + "&daddr=" + current_lat + "," + current_lon;
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse(uri_str));
//                startActivity(intent);


                String current_lat_str =  CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.CURRENT_LATITUTE);

                String current_long_str =  CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.CURRENT_LONGITUDE);

                String uri_str = "http://maps.google.com/maps?saddr=" + current_lat_str + "," + current_long_str + "&daddr=" + branchDetailsPojo.getLatitude() + "," + branchDetailsPojo.getLongitude();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(uri_str));
                startActivity(intent);

                finish();
            }
        });
    }

   private void addMarkers(GoogleMap googleMap,double latitude, double longitude) {
        LatLng location_latitude_longitude = new LatLng(latitude, longitude);
        if (googleMap != null) {


            googleMap.addMarker(new MarkerOptions().position(location_latitude_longitude)
                    .draggable(true));

            googleMap.addMarker(new MarkerOptions()
                    .position(location_latitude_longitude)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

//            googleMap.addCircle(new CircleOptions()
//                    .center(location_latitude_longitude)
//                    .radius(500)
//                    .strokeWidth(10)
//                    .strokeColor(Color.GREEN)
//                    .fillColor(Color.argb(128, 255, 0, 0))
//                    .clickable(true));
        }
    }

    private String distance(double lat1, double lon1, double lat2, double lon2, String merChantHashMapKey) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return merChantHashMapKey;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void check_gps() {
        LocationManager lm = null;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled) {
            enableLoc();
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//    }

    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult((Activity) GpsNavigationActivty.this, 3);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (listener != null) {
            listener.onLocationChanged(location);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(GpsNavigationActivty.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
        googleMap.setMyLocationEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(29.3697, 47.9783))
                .zoom(8)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(branchDetailsPojo.getLatitude()), Double.parseDouble(branchDetailsPojo.getLongitude())), 14.0f));
        addMarkers(googleMap,Double.parseDouble(branchDetailsPojo.getLatitude()), Double.parseDouble(branchDetailsPojo.getLongitude()));

    }
}