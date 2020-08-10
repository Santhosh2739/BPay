package com.bookeey.wallet.live.offers;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import java.io.InputStream;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.LocaleHelper;
import coreframework.utils.URLUTF8Encoder;
import newflow.NewFlowOfferNew;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.offers.OfferPreviewRequest;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;

public class ShowOfferActivityNewFlow extends GenericActivity {
    NewFlowOfferNew newflow_offer_details = null;
    String mimeType = "text/html";
    String encoding = "utf-8";
    private ProgressDialog progress;
    ImageView show_offer_image,merchant_logo;
    private DisplayImageOptions options;
    LinearLayout show_merchant_saved_new_offer_linear, show_merchant_saved_my_active_offer_linear;
    Button show_merchant_saved_offer_new_btn, show_merchant_saved_offer_new_my_active;
    TextView show_merchant_saved_offer_new_count_txt, show_merchant_saved_offer_my_active_count_txt, show_offer_name_txt, show_merchant_name_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_offer_details_newflow_new_layout);
        newflow_offer_details = ((CoreApplication) getApplication()).getNewflow_offer_details();

        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(ShowOfferActivityNewFlow.this, selectedLanguage);
        }


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
        mTitleTextView.setText(getResources().getString(R.string.offers_title));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        show_offer_image = (ImageView) findViewById(R.id.show_offer_image);
        merchant_logo =  (ImageView) findViewById(R.id.merchant_logo);


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


//        ImageLoader.getInstance()
//                .displayImage((newflow_offer_details.getImageFile()), show_offer_image, options, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//
//                    }
//                }, new ImageLoadingProgressListener() {
//                    @Override
//                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//                    }
//                });



        TextView  tv_offer_name = (TextView) findViewById(R.id.tv_offer_name);

        TextView  tv_offer_validity = (TextView) findViewById(R.id.tv_offer_validity);

        TextView  tv_offer_description= (TextView) findViewById(R.id.tv_offer_description);


        tv_offer_name.setText(""+newflow_offer_details.getOfferName());
        tv_offer_validity.setText("Validity: "+newflow_offer_details.getFromDate() +" - "+newflow_offer_details.getToDate());
        tv_offer_description.setText(""+newflow_offer_details.getOfferText());


//        ImageLoader.getInstance()
//                .displayImage((newflow_offer_details.getMerchantLogo()), merchant_logo, options, new SimpleImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    }
//                }, new ImageLoadingProgressListener() {
//                    @Override
//                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//                    }
//                });


        new DownloadImageTask(show_offer_image,newflow_offer_details.getImageFile()).execute();
        new DownloadImageTask(merchant_logo,newflow_offer_details.getMerchantLogo()).execute();



    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Display the details of a specific offer new flow");
    }



    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String imageUrl;

        public DownloadImageTask(ImageView bmImage,String _imageUrl) {
            this.bmImage = bmImage;
            this.imageUrl = _imageUrl;
        }

        protected Bitmap doInBackground(String... urls) {

            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(imageUrl).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);

            if(result!=null)
            bmImage.setBackground(new BitmapDrawable(getResources(), result));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getBaseContext(), IndividualSavedMerchantOffersActivity.class);
//        startActivity(intent);
//    }

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
}

