package com.bookeey.wallet.live.wheretopay;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.offers.MyActiveOffersActivity;
import com.facebook.appevents.AppEventsLogger;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import coreframework.taskframework.GenericFragmentActivity;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.wheretopay.BranchDetailsPojo;
import ycash.wallet.json.pojo.wheretopay.CategoryDetailsListPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;

/**
 * Created by 30099 on 4/18/2016.
 */
public class MerchantMapsActivity extends GenericFragmentActivity implements
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, OnMapReadyCallback {
    List<String> locations = new ArrayList<String>();
    LocationManager locationManager = null;
    MerchantListResponse merchantListResponse = null;
    List<BranchDetailsPojo> branchDetailsPojo = null;
    BranchDetailsPojo branchdetails_pojo = null;
    private DisplayImageOptions options;
    private GoogleMap googleMap;
    private TextView all_merchant_branch_name, all_merchant_branch_location;
    String imagePath;
    HashMap<String, String> hashMapForMerchantLogoPath;
    ImageView allmerchantmap_image;
    String imagePath_1;
    ImageView merchant_category_screen_wallet_logo_back, home_up_back;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allmerchantmap);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        String categoryName = ((CoreApplication) getApplication()).getCategoryname();
        //getActionBar().setTitle("               "+categoryName);
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
        // mTitleTextView.setTextSize(16);
        //mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_MM,5);
        mTitleTextView.setTextSize(16);

        mTitleTextView.setText(categoryName);

        //getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        allmerchantmap_image = (ImageView) findViewById(R.id.allmerchantmap_image);
        hashMapForMerchantLogoPath = new HashMap<String, String>();
        all_merchant_branch_name = (TextView) findViewById(R.id.all_merchant_branch_name);
        all_merchant_branch_location = (TextView) findViewById(R.id.all_merchant_branch_location);
        String merchantdetails = getIntent().getStringExtra("mapdetails");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
            /*googleMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.allmap)).getMap();*/

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.allmap);
            mapFragment.getMapAsync(this);
            /*googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(29.3697, 47.9783))
                    .zoom(10)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMarkerDragListener(this);*/

            /*merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse();
            for (int i = 0; i < merchantListResponse.getMerchantDetails().size(); i++) {
                branchDetailsPojo = merchantListResponse.getMerchantDetails().get(i).getBranch();
                ((CoreApplication) getApplication()).setImagePath(imagePath);
                for (int j = 0; j < branchDetailsPojo.size(); j++) {
                    String imagePath_1 = merchantListResponse.getMerchantDetails().get(i).getPath();

                *//*List<BranchDetailsPojo> duplicates = new ArrayList<BranchDetailsPojo>();
                Set<BranchDetailsPojo> bikeSet = new TreeSet<BranchDetailsPojo>(new bikeComparator());
                for (BranchDetailsPojo c : branchDetailsPojo) {
                    if (!bikeSet.add(c)) {
                        duplicates.add(c);
                    }
                }
                if (duplicates.size() != 0) {
                    addMarkers(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()+.00004), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()+.00004), branchDetailsPojo.get(j).getLocation());
                    hashMapForMerchantLogoPath.put(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), merchantListResponse.getMerchantDetails().get(i).getPath());

                } else*//*
                    addMarkers(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()), branchDetailsPojo.get(j).getLocation());
                    hashMapForMerchantLogoPath.put(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), merchantListResponse.getMerchantDetails().get(i).getPath());
                }
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page where branches of a selected merchant is listed");




        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 21);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page where branches of a selected merchant is listed");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 21 logged");


    }

    private void addMarkers(GoogleMap googleMap, String merchant_name, double lat, double lng, String branchlocation) {
        String latt = String.valueOf(lat);
        String longg = String.valueOf(lng);
        String loc = latt + "," + longg;

        if (locations.contains(loc)) {
            LatLng location_latitude_longitude = new LatLng(lat + .00004, lng + .00004);
            if (googleMap != null) {
                googleMap.addMarker(new MarkerOptions().position(location_latitude_longitude)
                        .title(merchant_name).snippet(branchlocation).draggable(true));
                googleMap.addMarker(new MarkerOptions()
                        .position(location_latitude_longitude)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        } else {
            locations.add(loc);
            LatLng location_latitude_longitude = new LatLng(lat, lng);
            if (googleMap != null) {
                googleMap.addMarker(new MarkerOptions().position(location_latitude_longitude)
                        .title(merchant_name).snippet(branchlocation).draggable(true));
                googleMap.addMarker(new MarkerOptions()
                        .position(location_latitude_longitude)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        all_merchant_branch_name.setText(marker.getTitle());
        String imagePath = hashMapForMerchantLogoPath.get(marker.getTitle());
        all_merchant_branch_location.setText(marker.getSnippet());
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.new_logo_virtual_grey)
                .showImageForEmptyUri(R.drawable.new_logo_virtual_grey)
                .showImageOnFail(R.drawable.new_logo_virtual_grey)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        initImageLoader(getBaseContext());
        ImageLoader.getInstance()
                .displayImage(imagePath, allmerchantmap_image, options, new SimpleImageLoadingListener() {
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

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
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
                .zoom(10)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);

        //markers
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse();
        for (int i = 0; i < merchantListResponse.getMerchantDetails().size(); i++) {
            branchDetailsPojo = merchantListResponse.getMerchantDetails().get(i).getBranch();
            ((CoreApplication) getApplication()).setImagePath(imagePath);
            for (int j = 0; j < branchDetailsPojo.size(); j++) {
                String imagePath_1 = merchantListResponse.getMerchantDetails().get(i).getPath();

                /*List<BranchDetailsPojo> duplicates = new ArrayList<BranchDetailsPojo>();
                Set<BranchDetailsPojo> bikeSet = new TreeSet<BranchDetailsPojo>(new bikeComparator());
                for (BranchDetailsPojo c : branchDetailsPojo) {
                    if (!bikeSet.add(c)) {
                        duplicates.add(c);
                    }
                }
                if (duplicates.size() != 0) {
                    addMarkers(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()+.00004), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()+.00004), branchDetailsPojo.get(j).getLocation());
                    hashMapForMerchantLogoPath.put(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), merchantListResponse.getMerchantDetails().get(i).getPath());

                } else*/
                addMarkers(googleMap, merchantListResponse.getMerchantDetails().get(i).getMerchantName(), Double.parseDouble(branchDetailsPojo.get(j).getLatitude()), Double.parseDouble(branchDetailsPojo.get(j).getLongitude()), branchDetailsPojo.get(j).getLocation());
                hashMapForMerchantLogoPath.put(merchantListResponse.getMerchantDetails().get(i).getMerchantName(), merchantListResponse.getMerchantDetails().get(i).getPath());
            }
        }
    }


   /* private class bikeComparator implements Comparator<BranchDetailsPojo> {

        @Override
        public int compare(BranchDetailsPojo p0, BranchDetailsPojo p1) {
            return p0.getLatitude().compareTo(p1.getLongitude());
        }
    }*/
}