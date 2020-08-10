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
import com.bookeey.wallet.live.mainmenu.MainActivity;
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
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.offers.MerchantDetailsAndOfferCount;
import ycash.wallet.json.pojo.offers.OfferDetails;
import ycash.wallet.json.pojo.offers.OfferFinalResponse;
import ycash.wallet.json.pojo.offers.OfferPreviewRequest;
import ycash.wallet.json.pojo.offers.OfferResponse;

/**
 * Created by 30099 on 6/6/2016.
 */
public class NewOffersActivity extends GenericActivity {
    ListView offers_list;
    Button offer_new_my_active, offer_new_btn;
    private ProgressDialog progress;
    OfferResponse offerResponse = null;
    MerchantDetailsAndOfferCount merchantDetailsAndOfferCount = null;
    LinearLayout my_active_offer_linear, new_offer_linear;
    private CustomListAdapter adapter;
    private DisplayImageOptions options;
    TextView offer_new_count_txt, offer_my_active_count_txt, offers_new_txt, offers_details_txt;
    private OfferFinalResponse offerFinalResponse;
    ViewHolder holder;
    View text_line;
    public static boolean isnewoffer_individual;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

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


        my_active_offer_linear = (LinearLayout) findViewById(R.id.my_active_offer_linear);
        new_offer_linear = (LinearLayout) findViewById(R.id.new_offer_linear);
        progress = new ProgressDialog(NewOffersActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        text_line = (View) findViewById(R.id.text_line);
        text_line.setVisibility(View.GONE);
        offers_new_txt = (TextView) findViewById(R.id.offers_new_txt);
        offers_details_txt = (TextView) findViewById(R.id.offers_details_txt);
//        getOffersCount();

        my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
        offer_new_my_active = (Button) findViewById(R.id.offer_new_my_active);
        offer_new_btn = (Button) findViewById(R.id.offer_new_btn);


        offer_new_count_txt = (TextView) findViewById(R.id.offer_new_count_txt);
        offer_my_active_count_txt = (TextView) findViewById(R.id.offer_my_active_count_txt);

        offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));

        offer_my_active_count_txt.setVisibility(View.GONE);
        offer_new_my_active.setTextColor(getResources().getColor(R.color.blue));
        my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_light_gray));
        if (!isnewoffer_individual) {
            getOffersCount();
        } else {
            getMyActiveOffersCount();
        }
        offer_new_my_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isnewoffer_individual = true;
                offer_my_active_count_txt.setVisibility(View.VISIBLE);
                offer_my_active_count_txt.setTextColor(getResources().getColor(R.color.blue));
                offer_my_active_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
                offer_new_my_active.setTextColor(getResources().getColor(R.color.white));
                offer_new_count_txt.setVisibility(View.GONE);
                my_active_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
                new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_btn.setTextColor(getResources().getColor(R.color.blue));
                offer_new_my_active.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
                getMyActiveOffersCount();
            }
        });
        offer_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isnewoffer_individual = false;
                offer_my_active_count_txt.setVisibility(View.GONE);
                offer_new_my_active.setTextColor(getResources().getColor(R.color.blue));
                my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setVisibility(View.VISIBLE);
                new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setTextColor(getResources().getColor(R.color.blue));
                offer_new_btn.setTextColor(getResources().getColor(R.color.white));
                offer_new_my_active.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
                new_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));

                getOffersCount();

            }
        });
        offers_list = (ListView) findViewById(R.id.offers_list);
        offers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
                StringBuffer buffer = new StringBuffer();
                if (!isnewoffer_individual) {
                    if (offerFinalResponse != null)
                        if (offerFinalResponse.getOfferCount().getNewOffers() != null) {
                            offerPreviewRequest.setMerchantId(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantId());
                            ((CoreApplication) getApplication()).setOffer_merchantName(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantName());
                            ((CoreApplication) getApplication()).setMerchantimage(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantLogo());
                            offerPreviewRequest.setG_transType(TransType.OFFER_NEWDETAILS_REQUEST.name());
                            buffer.append(TransType.OFFER_NEWDETAILS_REQUEST.getURL());
                        }
                } else {
                    if (offerFinalResponse != null)
                        if (offerFinalResponse.getOfferCount().getMyactiveOffers() != null) {
                            offerPreviewRequest.setMerchantId(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantId());
                            offerPreviewRequest.setMerchantName(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantName());
                            ((CoreApplication) getApplication()).setOffer_merchantName(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantName());
                            offerPreviewRequest.setG_transType(TransType.OFFER_ACTIVEDETAILS_REQUEST.name());
                            ((CoreApplication) getApplication()).setMerchantimage(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantLogo());
                            buffer.append(TransType.OFFER_ACTIVEDETAILS_REQUEST.getURL());
                        }
                }
                if (offerPreviewRequest.getG_transType() != null && offerPreviewRequest.getMerchantId() != null) {
                    String json = new Gson().toJson(offerPreviewRequest);
                    buffer.append("?d=" + URLUTF8Encoder.encode(json));
                    android.os.Handler messageHandler = new android.os.Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            hideIfVisible();
                            if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWDETAILS_RESPONSE.name()) || response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_ACTIVEDETAILS_RESPONSE.name())) {
                                    offerFinalResponse = new Gson().fromJson((String) msg.obj, OfferFinalResponse.class);
                                    OfferResponse offerResponse = new Gson().fromJson((String) msg.obj, OfferResponse.class);
                                    ((CoreApplication) getApplication()).setOfferResponse(offerResponse);

                                    ((CoreApplication) getApplication()).setOfferFinalResponse(offerFinalResponse);

                                    Intent intent = new Intent(getBaseContext(), IndividualSavedMerchantOffersActivity.class);
                                    startActivity(intent);

                                } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                    Toast toast = Toast.makeText(NewOffersActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                    toast.show();
                                    Intent intent = new Intent(NewOffersActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    NewOffersActivity.this.finish();
                                    return;
                                } else {
                                    Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                                Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    };
                    new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                    showIfNotVisible("");
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("List of all merchant's offers");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 23);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "List of all merchant's offers");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 23 logged");
    }

    private void getOffersCount() {
        if (adapter != null) {
            adapter.clear();
        }
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.OFFER_NEWCOUNT_REQUEST.name());
        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.OFFER_NEWCOUNT_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    if (!network_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name()) && response.getG_status() == 1) {
                            offerFinalResponse = new Gson().fromJson((String) msg.obj, OfferFinalResponse.class);
                            ((CoreApplication) getApplication()).setOfferFinalResponse(offerFinalResponse);
                            offer_new_count_txt.setText("" + offerFinalResponse.getOfferCount().getTotalNewOffers());
                            if (offerFinalResponse.getOfferCount().getTotalNewOffers().equalsIgnoreCase("0")) {
                                offers_list.setAdapter(null);
                                Toast.makeText(getBaseContext(), "There is no new offers for you", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            offers_new_txt.setVisibility(View.VISIBLE);
                            offers_details_txt.setVisibility(View.VISIBLE);
                            text_line.setVisibility(View.VISIBLE);
                            adapter = new CustomListAdapter(NewOffersActivity.this, offerFinalResponse.getOfferCount().getNewOffers());
                            ((CoreApplication) getApplication()).setNewOfferCount(offerFinalResponse.getOfferCount().getTotalNewOffers());
                            offers_list.setVisibility(View.VISIBLE);
                            offers_list.setAdapter(adapter);
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(NewOffersActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(NewOffersActivity.this, LoginActivity.class);
                            startActivity(intent);
                            NewOffersActivity.this.finish();
                            return;
                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name()) && response.getG_status() != 1) {
                            if (response.getG_errorDescription().equalsIgnoreCase("No offers available")) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.No_offers_available), Toast.LENGTH_SHORT).show();
                                offers_list.setAdapter(null);
                                offer_my_active_count_txt.setText("0");
                                offers_list.setVisibility(View.VISIBLE);
                                offers_new_txt.setVisibility(View.GONE);
                                offer_new_count_txt.setVisibility(View.VISIBLE);
                                offer_new_count_txt.setText("0");
                                text_line.setVisibility(View.GONE);
                                offers_details_txt.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                                offers_list.setAdapter(null);
                                offer_my_active_count_txt.setText("0");
                                offers_list.setVisibility(View.VISIBLE);
                                offers_new_txt.setVisibility(View.GONE);
                                offer_new_count_txt.setVisibility(View.VISIBLE);
                                offer_new_count_txt.setText("0");
                                text_line.setVisibility(View.GONE);
                                offers_details_txt.setVisibility(View.GONE);
                            }
                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            offers_list.setAdapter(null);
                            offer_my_active_count_txt.setText("0");
                            offers_list.setVisibility(View.VISIBLE);
                            offers_new_txt.setVisibility(View.GONE);
                            text_line.setVisibility(View.GONE);
                            offers_details_txt.setVisibility(View.GONE);
                            return;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    offers_list.setAdapter(null);
                    offer_new_count_txt.setText("0");
                    offers_list.setVisibility(View.VISIBLE);
                    offers_new_txt.setVisibility(View.GONE);
                    text_line.setVisibility(View.GONE);
                    offers_details_txt.setVisibility(View.GONE);
                    return;
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    offers_list.setAdapter(null);
                    offer_new_count_txt.setText("0");
                    offers_list.setVisibility(View.VISIBLE);
                    text_line.setVisibility(View.GONE);
                    offer_my_active_count_txt.setVisibility(View.GONE);
                    offers_details_txt.setVisibility(View.GONE);
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");


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
        if (progress != null) {
            if (progress.isShowing()) {
                progress.hide();
                //newly added
                progress.dismiss();
            }

        }

    }

    private void getMyActiveOffersCount() {
        if (adapter != null) {
            adapter.clear();
        }
        offer_my_active_count_txt.setVisibility(View.VISIBLE);
        offer_my_active_count_txt.setTextColor(getResources().getColor(R.color.blue));
        offer_my_active_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
        offer_new_my_active.setTextColor(getResources().getColor(R.color.white));

        offer_new_count_txt.setVisibility(View.GONE);
        offer_new_count_txt.setVisibility(View.GONE);
        my_active_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
        new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
        offer_new_btn.setTextColor(getResources().getColor(R.color.blue));
        offer_new_my_active.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.OFFER_ACTIVECOUNT_REQUEST.name());
        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.OFFER_ACTIVECOUNT_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String my_active_response = ((String) msg.obj).trim();
                    if (!my_active_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(my_active_response, GenericResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_ACTIVECOUNT_RESPONSE.name()) && response.getG_status() == 1) {
                            offerFinalResponse = new Gson().fromJson((String) msg.obj, OfferFinalResponse.class);
                            ((CoreApplication) getApplication()).setOfferFinalResponse(offerFinalResponse);
                            offer_my_active_count_txt.setText("" + offerFinalResponse.getOfferCount().getTotalActiveoffers());
                            ((CoreApplication) getApplication()).setActiveOfferCount(offerFinalResponse.getOfferCount().getTotalActiveoffers());
                            if (offerFinalResponse.getOfferCount().getTotalActiveoffers().equalsIgnoreCase("0")) {
                                offers_list.setAdapter(null);
                                Toast.makeText(getBaseContext(), "There is no saved offers for you", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            offers_new_txt.setVisibility(View.VISIBLE);
                            offers_details_txt.setVisibility(View.VISIBLE);
                            adapter = new CustomListAdapter(NewOffersActivity.this, offerFinalResponse.getOfferCount().getMyactiveOffers());
                            offers_list.setAdapter(adapter);
                            text_line.setVisibility(View.VISIBLE);
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(NewOffersActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(NewOffersActivity.this, LoginActivity.class);
                            startActivity(intent);
                            NewOffersActivity.this.finish();
                            return;
                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_ACTIVECOUNT_RESPONSE.name()) && response.getG_status() != 1) {
                            if (response.getG_errorDescription().equalsIgnoreCase("No offers available")) {
                                Toast.makeText(getBaseContext(), getResources().getString(R.string.No_offers_available), Toast.LENGTH_SHORT).show();
                                offers_list.setAdapter(null);
                                offer_my_active_count_txt.setText("0");
                                offers_list.setVisibility(View.VISIBLE);
                                offers_new_txt.setVisibility(View.GONE);
                                offer_new_count_txt.setVisibility(View.VISIBLE);
                                offer_new_count_txt.setText("0");
                                text_line.setVisibility(View.GONE);
                                offers_details_txt.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                                offers_list.setAdapter(null);
                                offer_my_active_count_txt.setText("0");
                                offers_list.setVisibility(View.VISIBLE);
                                offers_new_txt.setVisibility(View.GONE);
                                offers_details_txt.setVisibility(View.GONE);
                                text_line.setVisibility(View.GONE);
                            }
                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
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

    private class CustomListAdapter extends ArrayAdapter<MerchantDetailsAndOfferCount> {
        private CustomListAdapter(Activity context, List<MerchantDetailsAndOfferCount> genericResponses) {
            super(context, R.layout.offers_list_row, genericResponses);
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
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            merchantDetailsAndOfferCount = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_list_row, parent, false);
                holder = new ViewHolder();
                holder.offers_merchant_name = (TextView) convertView.findViewById(R.id.offers_merchant_name);
                holder.offer_validity = (TextView) convertView.findViewById(R.id.offers_merchant_size);
                holder.offers_merchant_logo = (ImageView) convertView.findViewById(R.id.offers_merchant_logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance()
                    .displayImage(merchantDetailsAndOfferCount.getMerchantLogo(), holder.offers_merchant_logo, options, new SimpleImageLoadingListener() {
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
            holder.offers_merchant_name.setText(merchantDetailsAndOfferCount.getMerchantName());
            if (offerFinalResponse.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name())) {
                holder.offer_validity.setText("New Offers  " + merchantDetailsAndOfferCount.getOfferCount());
            } else {
                holder.offer_validity.setText("Active Offers " + merchantDetailsAndOfferCount.getOfferCount());
            }
            return convertView;
        }
    }

    public class ViewHolder {
        TextView offer_validity;
        ImageView offers_merchant_logo;
        TextView offers_merchant_name;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
