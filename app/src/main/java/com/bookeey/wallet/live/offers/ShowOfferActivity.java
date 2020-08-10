package com.bookeey.wallet.live.offers;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
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

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.LocaleHelper;
import coreframework.utils.URLUTF8Encoder;
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

/**
 * Created by 30099 on 4/25/2016.
 */
public class ShowOfferActivity extends GenericActivity {
    OfferPreviewResponse offerPreviewResponse = null;
    String mimeType = "text/html";
    String encoding = "utf-8";
    private ProgressDialog progress;
    ImageView show_offer_image;
    private DisplayImageOptions options;
    WebView show_offers_image;
    LinearLayout show_merchant_saved_new_offer_linear, show_merchant_saved_my_active_offer_linear;
    Button show_merchant_saved_offer_new_btn, show_merchant_saved_offer_new_my_active;
    TextView show_merchant_saved_offer_new_count_txt, show_merchant_saved_offer_my_active_count_txt, show_offer_name_txt, show_merchant_name_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_offers);
        offerPreviewResponse = ((CoreApplication) getApplication()).getOfferPreviewResponse();

        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(ShowOfferActivity.this, selectedLanguage);
        }

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("OFFERS");*/

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


        show_offer_name_txt = (TextView) findViewById(R.id.show_offer_name_txt);
        show_merchant_name_txt = (TextView) findViewById(R.id.show_merchant_name_txt);

        show_merchant_saved_offer_new_btn = (Button) findViewById(R.id.show_merchant_saved_offer_new_btn);
        show_merchant_saved_offer_new_my_active = (Button) findViewById(R.id.show_merchant_saved_offer_new_my_active);


        show_merchant_saved_offer_new_count_txt = (TextView) findViewById(R.id.show_merchant_saved_offer_new_count_txt);
        show_merchant_saved_offer_my_active_count_txt = (TextView) findViewById(R.id.show_merchant_saved_offer_my_active_count_txt);

        show_merchant_saved_new_offer_linear = (LinearLayout) findViewById(R.id.show_merchant_saved_new_offer_linear);
        show_merchant_saved_my_active_offer_linear = (LinearLayout) findViewById(R.id.show_merchant_saved_my_active_offer_linear);

        if (!NewOffersActivity.isnewoffer_individual) {
            show_merchant_saved_offer_my_active_count_txt.setVisibility(View.GONE);
            show_merchant_saved_offer_new_my_active.setTextColor(getResources().getColor(R.color.blue));
            show_merchant_saved_my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            show_merchant_saved_offer_new_count_txt.setVisibility(View.VISIBLE);
            show_merchant_saved_new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            show_merchant_saved_offer_new_count_txt.setTextColor(getResources().getColor(R.color.blue));
            show_merchant_saved_offer_new_btn.setTextColor(getResources().getColor(R.color.white));
            show_merchant_saved_offer_new_my_active.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            show_merchant_saved_offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
            show_merchant_saved_new_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            show_merchant_saved_offer_new_count_txt.setText(((CoreApplication) getApplication()).getNewOfferCount());
        } else {
            show_merchant_saved_offer_my_active_count_txt.setVisibility(View.VISIBLE);
            show_merchant_saved_offer_my_active_count_txt.setTextColor(getResources().getColor(R.color.blue));
            show_merchant_saved_offer_my_active_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
            show_merchant_saved_offer_new_my_active.setTextColor(getResources().getColor(R.color.white));
            show_merchant_saved_offer_new_count_txt.setVisibility(View.GONE);
            show_merchant_saved_my_active_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            show_merchant_saved_new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            show_merchant_saved_offer_new_btn.setTextColor(getResources().getColor(R.color.blue));
            show_merchant_saved_offer_new_my_active.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            show_merchant_saved_offer_my_active_count_txt.setText("" + ((CoreApplication) getApplication()).getActiveOfferCount());
        }

        show_merchant_name_txt.setText(((CoreApplication) getApplication()).getOffer_merchantName());
        show_offer_name_txt.setText(((CoreApplication) getApplication()).getOfferName());


        show_offer_image = (ImageView) findViewById(R.id.show_offer_image);
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
                .displayImage(((CoreApplication) getApplication()).getMerchantimage(), show_offer_image, options, new SimpleImageLoadingListener() {
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
        progress = new ProgressDialog(ShowOfferActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        show_offers_image = (WebView) findViewById(R.id.show_offers_image);
        show_offers_image.getSettings().setJavaScriptEnabled(true);
        show_offers_image.getSettings().setSupportZoom(false);
        show_merchant_saved_offer_new_btn = (Button) findViewById(R.id.show_merchant_saved_offer_new_btn);
        show_merchant_saved_offer_new_my_active = (Button) findViewById(R.id.show_merchant_saved_offer_new_my_active);

        show_merchant_saved_offer_my_active_count_txt = (TextView) findViewById(R.id.show_merchant_saved_offer_my_active_count_txt);
        show_merchant_saved_offer_new_count_txt = (TextView) findViewById(R.id.show_merchant_saved_offer_new_count_txt);
        show_merchant_saved_offer_new_count_txt.setText("" + ((CoreApplication) getApplication()).getNewOfferCount());
        show_merchant_saved_offer_my_active_count_txt.setText("" + ((CoreApplication) getApplication()).getActiveOfferCount());

        show_merchant_saved_offer_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOffersActivity.isnewoffer_individual = false;
                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
                startActivity(intent);
            }
        });
        show_merchant_saved_offer_new_my_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOffersActivity.isnewoffer_individual = true;
                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
                startActivity(intent);
            }
        });


        show_offers_image.getSettings().setBuiltInZoomControls(false);
        show_offers_image.loadDataWithBaseURL("", offerPreviewResponse.getLogo(), mimeType, encoding, "");


        ((Button) findViewById(R.id.show_offers_reject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                final OfferPreviewResponse offerPreviewResponse = ((CoreApplication) getApplication()).getOfferPreviewResponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setOfferId(offerPreviewResponse.getOfferId());
                offerPreviewRequest.setUseroffertype(2);
                offerPreviewRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                offerPreviewRequest.setG_transType(TransType.OFFER_SAVE_REQUEST.name());
                String json = new Gson().toJson(offerPreviewRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.OFFER_SAVE_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_SAVE_RESPONSE.name())) {
                                Toast.makeText(getBaseContext(), "This offer won't show in future", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
                                startActivity(intent);
                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(ShowOfferActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(ShowOfferActivity.this, LoginActivity.class);
                                startActivity(intent);
                                ShowOfferActivity.this.finish();
                                return;
                            } else {
                                Toast.makeText(getBaseContext(),getResources().getString(R.string.No_offers_available), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfNotVisible("");
            }
        });
        ((Button) findViewById(R.id.show_offers_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                final OfferPreviewResponse offerPreviewResponse = ((CoreApplication) getApplication()).getOfferPreviewResponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setOfferId(offerPreviewResponse.getOfferId());
                offerPreviewRequest.setUseroffertype(1);
                offerPreviewRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                offerPreviewRequest.setG_transType(TransType.OFFER_SAVE_REQUEST.name());
                String json = new Gson().toJson(offerPreviewRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.OFFER_SAVE_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_SAVE_RESPONSE.name())) {
                                Toast.makeText(getBaseContext(), "Offer saved successfully to My Active", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
                                startActivity(intent);
                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(ShowOfferActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(ShowOfferActivity.this, LoginActivity.class);
                                startActivity(intent);
                                ShowOfferActivity.this.finish();
                                return;
                            } else {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.No_offers_available), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(),getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfNotVisible("");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Display the details of a specific offer");
    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    private void showIfNotVisible(String title) {
        if (!progress.isShowing()) {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        } else {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), IndividualSavedMerchantOffersActivity.class);
        startActivity(intent);
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

}