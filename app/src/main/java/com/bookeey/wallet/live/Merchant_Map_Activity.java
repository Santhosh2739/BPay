package com.bookeey.wallet.live;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.application.DownloadResultReceiver;
import com.bookeey.wallet.live.forceface.TransTypeInterface;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.wheretopay.GpsNavigationActivty;
import com.bookeey.wallet.live.wheretopay.WorkaroundFragment;
import com.facebook.appevents.AppEventsLogger;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.google.zxing.common.StringUtils;
import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericFragmentActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.wheretopay.BranchDetailsPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantCategoryDetailsListPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListDetailsResponsePojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;

// * Created by 30099 on 3/21/2016.
public class Merchant_Map_Activity extends GenericFragmentActivity implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, YPCHeadlessCallback, OnMapReadyCallback , GoogleApiClient.ConnectionCallbacks, LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private GoogleMap googleMap;

    private DownloadResultReceiver mReceiver = null;
    private TextView merchantinformation_name;
    private TextView map_merchant_name;
    ImageView marker_image_logo;

    private DisplayImageOptions options;
    MerchantListResponse merchantListResponse;
    List<BranchDetailsPojo> branchDetailsPojo = null;
    String nearest_merChantHashMapKey;
    Button merchant_list_close_btn;
    ListView merchant_list_items;
    private CustomAdapter adapter = null;
    BranchDetailsPojo branchDetailsPojo_item;
    ViewHolder holder;
    Marker marker;
    ImageView merchant_map_merchant_list_image, merchant_map_merchant_globe_image;
    private SupportMapFragment mMapFragment;
    //  ScrollView mscroll_view;

    ImageView merchant_category_screen_wallet_logo_back, home_up_back;

    GPSTracker gps;
    Geocoder geocoder;
    double current_lat = 29.370659;
    double current_lon = 48.005223;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    Boolean gps_enabled;
    private GoogleApiClient googleApiClient;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantlist_inforamtion_new_june);
       /* getActionBar().setTitle("                   BRANCHES");
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);*/


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.map_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);

        gps = new GPSTracker(getBaseContext());


        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.where_to_pay_branches_title));


        String imagepath = ((CoreApplication) getApplication()).getImagePath();
        String merchant_name = ((CoreApplication) getApplication()).getMerchantName();
        map_merchant_name = (TextView) findViewById(R.id.map_merchant_name);
        map_merchant_name.setText(merchant_name);

        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                    1, this);
        }catch(Exception e){
            Toast.makeText(Merchant_Map_Activity.this, "Location Ex: "+e.getMessage(),Toast.LENGTH_LONG).show();
        }



        marker_image_logo = (ImageView) findViewById(R.id.marker_image_logo);
        merchant_list_items = (ListView) findViewById(R.id.merchant_list_items);
//        mscroll_view = (ScrollView) findViewById(R.id.mscroll_view);
//        mscroll_view.fullScroll(ScrollView.FOCUS_UP);
        merchant_list_items.setFocusable(false);
        merchant_list_items.setVisibility(View.VISIBLE);

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

        if (googleMap == null) {
            /*googleMap = ((WorkaroundFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();*/

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            ((WorkaroundFragment) getSupportFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                }
            });
            /*googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    .zoom(10)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMarkerDragListener(this);*/


            merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse();


            for (int i = 0; i < merchantListResponse.getMerchantDetails().size(); i++) {
                branchDetailsPojo = merchantListResponse.getMerchantDetails().get(i).getBranch();
                adapter = new CustomAdapter(Merchant_Map_Activity.this, merchantListResponse.getMerchantDetails().get(i).getBranch());
                /*for (int j = 0; j < merchantListResponse.getMerchantDetails().get(i).getBranch().size(); j++) {
                    if (branchDetailsPojo.get(j).getLatitude() == null && branchDetailsPojo.get(j).getLongitude() == null) {
                        Toast.makeText(getBaseContext(), "Address didn't provide properly", Toast.LENGTH_LONG).show();
                    } else {
                        addMarkers(branchDetailsPojo.get(j).getLocation(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()));
                    }
                }*/
            }

            merchant_list_items.setAdapter(adapter);


//            merchant_list_items.setOnTouchListener(new View.OnTouchListener() {
//                // Setting on Touch Listener for handling the touch inside ScrollView
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    // Disallow the touch request for parent scroll on touch of child view
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });
//
//
//            setListViewHeightBasedOnChildren(merchant_list_items);


            merchant_list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((CoreApplication) getApplication()).setBranchDetailsPojo(branchDetailsPojo.get(position));
                    ((CoreApplication) getApplication()).setBranchLocation(branchDetailsPojo.get(position).getLocation());
                    Intent intent = new Intent(getBaseContext(), GpsNavigationActivty.class);
                    startActivity(intent);
                }
            });
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
                    .displayImage(imagepath, marker_image_logo, options, new SimpleImageLoadingListener() {
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


        }


//        Button listHighlightBtn = (Button)findViewById(R.id.listHighlightBtn);
//        listHighlightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                highlightListItem(1);
//
//            }
//        });

//For location access
        checkLocationPermission();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page where branch details with map location is displayed");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 22);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page where branch details with map location is displayed");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 22 logged");


        check_gps();
        if (gps_enabled) {
            current_lat = gps.getLatitude();//current lattitude
            current_lon = gps.getLongitude();//current longitude
            LatLng currentpos = new LatLng(current_lat, current_lon);
            geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

            Log.e("current_lat",""+current_lat);

            Log.e("current_lon",""+current_lon);

        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }

    private void check_gps() {


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
        gps_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled) {
            enableLoc();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

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
                                status.startResolutionForResult((Activity) Merchant_Map_Activity.this, 3);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                    }
                }
            });
        }
    }

    private void addMarkers(GoogleMap googleMap, String branchlocation, double lat, double lng, String snippet) {



        Log.e("Merchantmap", "" + branchlocation + " Lat: " + lat + " Long: " + lng);

//        Double lati =  29.370398;
//        Double lngi = 48.005321;


        LatLng location_latitude_longitude = new LatLng(lat, lng);
        if (googleMap != null) {

//            googleMap.addMarker(new MarkerOptions().position(location_latitude_longitude)
//                    .title(branchlocation).draggable(true).snippet(snippet));

            googleMap.addMarker(new MarkerOptions()
                    .title(branchlocation)
                    .snippet(snippet)
                    .position(location_latitude_longitude)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));


//            map.setInfoWindowAdapter(new InfoWindowCustom(this));
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String snippet = marker.getSnippet();

        if (snippet != null && !snippet.isEmpty()) {
            highlightListItem(Integer.parseInt(snippet));
        }

//        Toast.makeText(Merchant_Map_Activity.this, ""+marker.getTitle(),Toast.LENGTH_LONG).show();

        return false;
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
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }


    private void highlightListItem(final int position) {

//        CustomAdapter new_adapter = (CustomAdapter) merchant_list_items.getAdapter();
//        new_adapter.setSelectedItem(position);


        Log.e("\n\nFor the Positon: ",""+position);

        for(int i=0;i<branchDetailsPojo.size();i++) {
            Log.e("Before Item: "+i, "" +branchDetailsPojo.get(i).getBranchName()+" - "+branchDetailsPojo.get(i).getAddress());
        }


        if(position!=0) {

            BranchDetailsPojo item = branchDetailsPojo.get(position);
            branchDetailsPojo.remove(position);
            branchDetailsPojo.add(0, item);
        }


        googleMap.clear();

        for(int j=0;j<branchDetailsPojo.size();j++) {
            Log.e("After Item: " + j, "" + branchDetailsPojo.get(j).getBranchName() + " - " + branchDetailsPojo.get(j).getAddress());


            if (branchDetailsPojo.get(j).getLatitude() == null && branchDetailsPojo.get(j).getLongitude() == null) {
                Toast.makeText(getBaseContext(), "Address didn't provide properly", Toast.LENGTH_LONG).show();
            } else {


                addMarkers(googleMap, branchDetailsPojo.get(j).getLocation(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()), "" + j);
            }
        }


        CustomAdapter adapter_modified = new CustomAdapter(Merchant_Map_Activity.this,branchDetailsPojo);
        adapter_modified.setSelectedItem(0);

        // in some cases, it may be necessary to re-set adapter (as in the line below)
        merchant_list_items.setAdapter(adapter_modified);




//        merchant_list_items.setSelection(0);


        merchant_list_items.post(new Runnable() {
            @Override
            public void run() {

                merchant_list_items.smoothScrollToPosition(0);


            }
        });

    }

    @Override
    public void onMapReady(GoogleMap _googleMap) {

        googleMap = _googleMap;


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                .zoom(10)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse();
        for (int i = 0; i < merchantListResponse.getMerchantDetails().size(); i++) {
            branchDetailsPojo = merchantListResponse.getMerchantDetails().get(i).getBranch();
            adapter = new CustomAdapter(Merchant_Map_Activity.this, merchantListResponse.getMerchantDetails().get(i).getBranch());
            for (int j = 0; j < merchantListResponse.getMerchantDetails().get(i).getBranch().size(); j++) {
                if (branchDetailsPojo.get(j).getLatitude() == null && branchDetailsPojo.get(j).getLongitude() == null) {
                    Toast.makeText(getBaseContext(), "Address didn't provide properly", Toast.LENGTH_LONG).show();
                } else {
                    addMarkers(googleMap, branchDetailsPojo.get(j).getLocation(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()), "" + j);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        current_lat = location.getLatitude();
        current_lon = location.getLongitude();

        CustomSharedPreferences.saveStringData(getApplicationContext(),String.valueOf(current_lat),CustomSharedPreferences.SP_KEY.CURRENT_LATITUTE);
        CustomSharedPreferences.saveStringData(getApplicationContext(),String.valueOf(current_lon),CustomSharedPreferences.SP_KEY.CURRENT_LONGITUDE);

//        Toast.makeText(Merchant_Map_Activity.this,current_lat+" - "+current_lon,Toast.LENGTH_LONG).show();

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

    class CustomAdapter extends ArrayAdapter<BranchDetailsPojo> {

        private int selectedItem = -1;

        public CustomAdapter(Context context, List<BranchDetailsPojo> genericResponses) {
            super(context, R.layout.individual_merchnat_list_row, genericResponses);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            branchDetailsPojo_item = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.individual_merchnat_list_row, parent, false);
                holder = new ViewHolder();
                holder.text_name = (TextView) convertView.findViewById(R.id.individual_merchant_title_name);
                holder.address = (TextView) convertView.findViewById(R.id.individual_merchant_title_address);
                holder.merchant_contact_img = (ImageView) convertView.findViewById(R.id.merchant_contact_img);
                holder.merchant_branch_direction = (ImageView) convertView.findViewById(R.id.merchant_branch_direction);

                if (branchDetailsPojo_item.getMobileNumber() != null && !branchDetailsPojo_item.getMobileNumber().isEmpty()) {
                    holder.merchant_contact_img.setVisibility(View.VISIBLE);
                } else {
                    holder.merchant_contact_img.setVisibility(View.GONE);
                }


                holder.merchant_contact_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.DIAL");
                        intent.setData(Uri.parse("tel:" + branchDetailsPojo_item.getMobileNumber()));
                        startActivity(intent);
                    }
                });

                holder.merchant_branch_direction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


//                        Toast.makeText(Merchant_Map_Activity.this, ""+branchDetailsPojo_item.getLatitude()+" - "+branchDetailsPojo_item.getLongitude()+"\n"+current_lat+" - "+current_lon,Toast.LENGTH_LONG).show();
//
//
//                        String uri_str = "http://maps.google.com/maps?saddr=" + branchDetailsPojo_item.getLatitude() + "," + branchDetailsPojo_item.getLongitude() + "&daddr=" + current_lat + "," + current_lon;
//                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                                Uri.parse(uri_str));
//                        startActivity(intent);


                        String current_lat_str =  CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.CURRENT_LATITUTE);

                        String current_long_str =  CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.CURRENT_LONGITUDE);

                            String uri_str = "http://maps.google.com/maps?saddr=" + current_lat_str + "," + current_long_str + "&daddr=" + branchDetailsPojo_item.getLatitude() + "," + branchDetailsPojo_item.getLongitude();
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(uri_str));
                            startActivity(intent);

                            finish();

                    }
                });


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            highlightItem(position, convertView);

            if (branchDetailsPojo_item.getBranchName() == null) {
                holder.text_name.setText("");
            } else {
                holder.text_name.setText(branchDetailsPojo_item.getBranchName().trim());
            }
            if (branchDetailsPojo_item.getLocation() == null) {
                holder.address.setText("");
            } else {
                holder.address.setText(branchDetailsPojo_item.getLocation().trim());
            }
            ((CoreApplication) getApplication()).setBranchName(branchDetailsPojo_item.getBranchName());
            ((CoreApplication) getApplication()).setBranchLocation(branchDetailsPojo_item.getLocation());
            return convertView;
        }

        /**
         * methods from StringUtils calls:
         * getContext().getResources().getColor(int resourceId)
         * getContext().getResources().getDrawable(int resourceId)
         * You can use them in your own context
         * (e.g. generic application context or you can pass activity context)
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void highlightItem(int position, View result) {
            if (position == selectedItem) {
                // you can define your own color of selected item here

//                result.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.dark_blue));





                //Working fine till June24 and in live
//                result.setBackground(getDrawable(R.drawable.listitem_shadow));

                result.setBackground(getDrawable(R.drawable.branch_list_selector));

//                result.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));

                TextView merchant_title_name = (TextView) result.findViewById(R.id.individual_merchant_title_name);
                TextView merchant_address = (TextView) result.findViewById(R.id.individual_merchant_title_address);

                merchant_title_name.setTextColor(getResources().getColor(R.color.black));
                merchant_address.setTextColor(getResources().getColor(R.color.black));


//                merchant_title_name.setShadowLayer(3, 0, 3, Color.DKGRAY);
//                merchant_address.setShadowLayer(3, 0, 3, Color.DKGRAY);
//
            } else {
                // you can define your own default selector here
                result.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.iphone_dark_blue));

//                result.setBackground(getDrawable(R.drawable.listitem_shadow));


                TextView merchant_title_name = (TextView) result.findViewById(R.id.individual_merchant_title_name);
                TextView merchant_address = (TextView) result.findViewById(R.id.individual_merchant_title_address);

                merchant_title_name.setTextColor(getResources().getColor(R.color.white));
                merchant_address.setTextColor(getResources().getColor(R.color.white));





            }
        }

        public void setSelectedItem(int selectedItem) {
            this.selectedItem = selectedItem;
        }

    }

    public class ViewHolder {
        TextView text_name;
        TextView address;
        ImageView merchant_contact_img;
        ImageView merchant_branch_direction;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                listItem.setPadding(0, 0, 0, 30);
            }

            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        /*ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);*/
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 30;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

//    public class InfoWindowCustom implements GoogleMap.InfoWindowAdapter {
//        Context context;
//        LayoutInflater inflater;
//        public InfoWindowCustom(Context context) {
//            this.context = context;
//        }
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//        @Override
//        public View getInfoWindow(Marker marker) {
//            inflater = (LayoutInflater)
//                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // R.layout.echo_info_window is a layout in my
//            // res/layout folder. You can provide your own
//            View v = inflater.inflate(R.layout.echo_info_window, null);
//
//            TextView title = (TextView) v.findViewById(R.id.info_window_title);
//            TextView subtitle = (TextView) v.findViewById(R.id.info_window_subtitle);
//            title.setText(marker.getTitle());
//            subtitle.setText(marker.getSnippet());
//            return v;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location permisson required")
                        .setMessage("Please grant location access")
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Merchant_Map_Activity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        }).create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //For current lat and long
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}