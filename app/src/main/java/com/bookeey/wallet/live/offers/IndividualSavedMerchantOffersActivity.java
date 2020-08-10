package com.bookeey.wallet.live.offers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.List;

import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
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
import ycash.wallet.json.pojo.offers.OfferDetails;
import ycash.wallet.json.pojo.offers.OfferFinalResponse;
import ycash.wallet.json.pojo.offers.OfferPreviewRequest;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;
import ycash.wallet.json.pojo.offers.OfferResponse;
import ycash.wallet.json.pojo.offers.Offers;

/**
 * Created by 30099 on 5/30/2016.
 */
public class IndividualSavedMerchantOffersActivity extends GenericActivity {
    Button individual_merchant_saved_offer_new_btn, individual_merchant_saved_offer_new_my_active;
    TextView individual_merchant_saved_offer_my_active_count_txt, individual_merchant_saved_offer_new_count_txt, individual_merchant_saved_name;
    OfferDetails offerDetails;
    Offers offers;
    ImageView individual_merchant_saved_image;
    private CustomListAdapter adapter;
    private ListView individual_list_view;
    OfferFinalResponse offerFinalResponse;
    private DisplayImageOptions options;
    private ProgressDialog progress;
    IndivedualViewHolder holder;
    LinearLayout individual_merchant_saved_new_offer_linear, individual_merchant_saved_my_active_offer_linear;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_saved_merchant_offers);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


       /* View mCustomView = getActionBar().getCustomView();
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

        progress = new ProgressDialog(IndividualSavedMerchantOffersActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        individual_merchant_saved_new_offer_linear = (LinearLayout) findViewById(R.id.individual_merchant_saved_new_offer_linear);
        individual_merchant_saved_my_active_offer_linear = (LinearLayout) findViewById(R.id.individual_merchant_saved_my_active_offer_linear);
        offerFinalResponse = ((CoreApplication) getApplication()).getOfferFinalResponse();

        individual_merchant_saved_offer_new_btn = (Button) findViewById(R.id.individual_merchant_saved_offer_new_btn);
        individual_merchant_saved_offer_new_my_active = (Button) findViewById(R.id.individual_merchant_saved_offer_new_my_active);

        individual_merchant_saved_offer_my_active_count_txt = (TextView) findViewById(R.id.individual_merchant_saved_offer_my_active_count_txt);
        individual_merchant_saved_offer_new_count_txt = (TextView) findViewById(R.id.individual_merchant_saved_offer_new_count_txt);
        individual_merchant_saved_name = (TextView) findViewById(R.id.individual_merchant_saved_name);

        individual_merchant_saved_image = (ImageView) findViewById(R.id.individual_merchant_saved_image);
        individual_list_view = (ListView) findViewById(R.id.individual_list_view);


        if (!NewOffersActivity.isnewoffer_individual) {
            individual_merchant_saved_offer_my_active_count_txt.setVisibility(View.GONE);
            individual_merchant_saved_offer_new_my_active.setTextColor(getResources().getColor(R.color.blue));
            individual_merchant_saved_my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            individual_merchant_saved_offer_new_count_txt.setVisibility(View.VISIBLE);
            individual_merchant_saved_new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            individual_merchant_saved_offer_new_count_txt.setTextColor(getResources().getColor(R.color.blue));
            individual_merchant_saved_offer_new_btn.setTextColor(getResources().getColor(R.color.white));
            individual_merchant_saved_offer_new_my_active.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            individual_merchant_saved_offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
            individual_merchant_saved_new_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            individual_merchant_saved_offer_new_count_txt.setText(((CoreApplication) getApplication()).getNewOfferCount());


        } else {
            individual_merchant_saved_offer_my_active_count_txt.setVisibility(View.VISIBLE);
            individual_merchant_saved_offer_my_active_count_txt.setTextColor(getResources().getColor(R.color.blue));
            individual_merchant_saved_offer_my_active_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
            individual_merchant_saved_offer_new_my_active.setTextColor(getResources().getColor(R.color.white));
            individual_merchant_saved_offer_new_count_txt.setVisibility(View.GONE);
            individual_merchant_saved_my_active_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            individual_merchant_saved_new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
            individual_merchant_saved_offer_new_btn.setTextColor(getResources().getColor(R.color.blue));
            individual_merchant_saved_offer_new_my_active.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            individual_merchant_saved_offer_my_active_count_txt.setText("" + ((CoreApplication) getApplication()).getActiveOfferCount());
        }
        if (offerFinalResponse != null) {
            if (offerFinalResponse.getOfferDetails() != null) {
                adapter = new CustomListAdapter(IndividualSavedMerchantOffersActivity.this, offerFinalResponse.getOfferDetails().getOfferDetails());
                individual_list_view.setAdapter(adapter);
            }
        } else {
            Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }


        individual_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setOfferName(offerFinalResponse.getOfferDetails().getOfferDetails().get(position).getOfferName());
                ((CoreApplication) getApplication()).setOfferName(offerFinalResponse.getOfferDetails().getOfferDetails().get(position).getOfferName());
                ((CoreApplication) getApplication()).setOfferId("" + offerFinalResponse.getOfferDetails().getOfferDetails().get(position).getOfferId());

                offerPreviewRequest.setOfferId(offerFinalResponse.getOfferDetails().getOfferDetails().get(position).getOfferId());
                if (NewOffersActivity.isnewoffer_individual) {
                    ((CoreApplication) getApplication()).setOffer_type(offerFinalResponse.getOfferDetails().getOfferDetails().get(position).getTypeofOffer());
                }
                offerPreviewRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                offerPreviewRequest.setG_transType(TransType.OFFER_PREVIEW_REQUEST.name());
                String json = new Gson().toJson(offerPreviewRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.OFFER_PREVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_PREVIEW_RESPONSE.name())) {
                                OfferPreviewResponse offerPreviewResponse = new Gson().fromJson((String) msg.obj, OfferPreviewResponse.class);
                                if (!NewOffersActivity.isnewoffer_individual) {
                                    ((CoreApplication) getApplication()).setOfferPreviewResponse(offerPreviewResponse);
                                    Intent intent = new Intent(getBaseContext(), ShowOfferActivity.class);
                                    startActivity(intent);
                                }
                                if (NewOffersActivity.isnewoffer_individual) {
                                    ((CoreApplication) getApplication()).setOfferPreviewResponse(offerPreviewResponse);
                                    Intent intent = new Intent(getBaseContext(), RedeemOffersActivity.class);
                                    startActivity(intent);
                                }

                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(IndividualSavedMerchantOffersActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(IndividualSavedMerchantOffersActivity.this, LoginActivity.class);
                                startActivity(intent);
                                IndividualSavedMerchantOffersActivity.this.finish();
                                return;
                            } else {
                                Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfNotVisible("");
            }
        });
        individual_merchant_saved_offer_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOffersActivity.isnewoffer_individual = false;
                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
                startActivity(intent);
            }
        });
        individual_merchant_saved_offer_new_my_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewOffersActivity.isnewoffer_individual = true;
                Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
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
        initImageLoader(getBaseContext());

        ImageLoader.getInstance()
                .displayImage(offerFinalResponse.getOfferDetails().getMerchantlogo(), individual_merchant_saved_image, options, new SimpleImageLoadingListener() {
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
        individual_merchant_saved_name.setText(((CoreApplication) getApplication()).getOffer_merchantName());

    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Display the details of a specific offer");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 24);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Display the details of a specific offer");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 24 logged");

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

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    private class CustomNewListAdapter extends ArrayAdapter<Offers> {
        private CustomNewListAdapter(Activity context, List<Offers> offers) {
            super(context, R.layout.offers_individual_list_row, offers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            offers = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_individual_list_row, parent, false);
                holder = new IndivedualViewHolder();
                holder.offers_individual_merchant_count = (TextView) convertView.findViewById(R.id.offers_individual_merchant_count);
                holder.offers_individual_offer_name = (TextView) convertView.findViewById(R.id.offers_individual_offer_name);
                holder.offers_individual_offer_expire_date = (TextView) convertView.findViewById(R.id.offers_individual_offer_expire_date);
                convertView.setTag(holder);
            } else {
                holder = (IndivedualViewHolder) convertView.getTag();
            }
            holder.offers_individual_merchant_count.setText("" + (position + 1));
            holder.offers_individual_offer_name.setText(offers.getOfferName());
            holder.offers_individual_offer_expire_date.setText("Expires " + offers.getEndIime() + " " + offers.getToDate());

            return convertView;
        }
    }

    private class CustomListAdapter extends ArrayAdapter<OfferDetails> {
        private CustomListAdapter(Activity context, List<OfferDetails> offerDetails) {
            super(context, R.layout.offers_individual_list_row, offerDetails);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            offerDetails = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_individual_list_row, parent, false);
                holder = new IndivedualViewHolder();
                holder.offers_individual_merchant_count = (TextView) convertView.findViewById(R.id.offers_individual_merchant_count);
                holder.offers_individual_offer_name = (TextView) convertView.findViewById(R.id.offers_individual_offer_name);
                holder.offers_individual_offer_expire_date = (TextView) convertView.findViewById(R.id.offers_individual_offer_expire_date);
                convertView.setTag(holder);
            } else {
                holder = (IndivedualViewHolder) convertView.getTag();
            }
            holder.offers_individual_merchant_count.setText("" + offerDetails.getsNo());
            holder.offers_individual_offer_name.setText(offerDetails.getOfferName());
            holder.offers_individual_offer_expire_date.setText("Expires " + offerDetails.getEndTime() + " " + offerDetails.getToDate());

            return convertView;
        }


    }

    public class IndivedualViewHolder {

        TextView offers_individual_merchant_count;
        TextView offers_individual_offer_name;
        TextView offers_individual_offer_expire_date;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), NewOffersActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
