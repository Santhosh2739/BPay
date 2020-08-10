package newflow;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.Merchant_Map_Activity;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.offers.RedeemOffersActivity;
import com.bookeey.wallet.live.offers.ShowOfferActivity;
import com.bookeey.wallet.live.offers.ShowOfferActivityNewFlow;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.URLUTF8Encoder;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.ImageScaleType;
import nostra13.universalimageloader.core.assist.ImageSize;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import nostra13.universalimageloader.utils.DiskCacheUtils;
import nostra13.universalimageloader.utils.MemoryCacheUtils;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.offers.OfferPreviewRequest;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;
import ycash.wallet.json.pojo.offers.OfferResponse;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;
import ycash.wallet.json.pojo.wheretopay.MerchnatListRequest;


public class NewOffersActivityNewFlow extends GenericActivity {
    ListView offers_list;
    Button offer_new_my_active, offer_new_btn;
    private ProgressDialog progress;
    OfferResponse offerResponse = null;
    NewFlowOffer merchantDetailsAndOfferCount = null;
    LinearLayout my_active_offer_linear, new_offer_linear;
    private CustomListAdapter adapter;
    private DisplayImageOptions options;
    TextView offer_new_count_txt, offer_my_active_count_txt, offers_new_txt, offers_details_txt;
    private OffersNewFlow offerFinalResponse;
    private NewFlowOfferNew newflow_offer_details;
    ViewHolder holder;
    View text_line;
    public static boolean isnewoffer_individual;


    //Location permission for Merchant Map
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,

    };
    private static final int INITIAL_REQUEST=1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout_newflow);

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
                finish();
            }
        });


        ImageView merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        my_active_offer_linear = (LinearLayout) findViewById(R.id.my_active_offer_linear);
        new_offer_linear = (LinearLayout) findViewById(R.id.new_offer_linear);
        progress = new ProgressDialog(NewOffersActivityNewFlow.this, R.style.MyTheme2);
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


//        if (!isnewoffer_individual) {
////            getOffersCount();
//        } else {
//            getMyActiveOffersCount();
//        }

        //For newflow invoke directly Sep 18


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

//                getOffersCount();

            }
        });


        offers_list = (ListView) findViewById(R.id.offers_list);
        offers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                String merchantCategory = offerFinalResponse.getNewFlowOffers().get(position).getMerchantCategory();
                String merchantName= offerFinalResponse.getNewFlowOffers().get(position).getMerchantName();
                final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
                merchnatListRequest.setCategoryID(merchantCategory);
                merchnatListRequest.setMerchantName(merchantName);
                merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.name());
                String json = new Gson().toJson(merchnatListRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideifVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            try {
                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                                if (null != response && response.getG_status() == 1) {
                                    MerchantListResponse merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                                    if (merchantListResponse.getMerchantDetails().size() == 0) {
                                        Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        ((CoreApplication) getApplication()).setMerchantName(merchantListResponse.getMerchantDetails().get(0).getMerchantName());
                                        ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
                                        ((CoreApplication) getApplication()).setImagePath(merchantListResponse.getMerchantDetails().get(0).getPath());
                                        Intent intent = new Intent(getBaseContext(), Merchant_Map_Activity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
                showIfnotVisible("");






                /*


                String offerId = offerFinalResponse.getNewFlowOffers().get(position).getOfferId();

//                Toast toast = Toast.makeText(NewOffersActivityNewFlow.this, "Offer Clicked: "+offerId, Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                toast.show();

                CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setG_transType(TransType.NEW_OFFER_AllACTIVEDETAILS_REQUEST.name());
                offerPreviewRequest.setOfferId(Long.parseLong(offerId));
                StringBuffer buffer = new StringBuffer();
                String json = new Gson().toJson(offerPreviewRequest);
                Log.e("OfferDetlsNewFlow Data", "Data: " + json);
                buffer.append(TransType.NEW_OFFER_AllACTIVEDETAILS_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));

                Log.e("OfferDetailsNewFlow", "URL:" + buffer.toString());

                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);


                            if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_OFFER_AllACTIVEDETAILS_RESPONSE.name()) || response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_OFFER_AllACTIVEDETAILS_RESPONSE.name())) {
                                try {

                                    JSONObject offers_jo = new JSONObject((String) msg.obj);
                                    Log.e("OfferDetails", "Res:" + response.toString());

                                    newflow_offer_details = new Gson().fromJson(offers_jo.getString("offerDetails"), NewFlowOfferNew.class);
                                } catch (Exception e) {
                                    Toast.makeText(getBaseContext(), "System Error!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                ((CoreApplication) getApplication()).setNewflow_offer_details(newflow_offer_details);

                                Intent intent = new Intent(getBaseContext(), ShowOfferActivityNewFlow.class);
                                startActivity(intent);

//                                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();


//                                    Intent intent = new Intent(getBaseContext(), RedeemOffersActivity.class);
//                                    startActivity(intent);

                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(NewOffersActivityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(NewOffersActivityNewFlow.this, LoginActivity.class);
                                startActivity(intent);
                                NewOffersActivityNewFlow.this.finish();
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
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
                showIfNotVisible("");

                */

            }
        });


//        Old
//        offers_list = (ListView) findViewById(R.id.offers_list);
//        offers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
//                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
//                offerPreviewRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
//                StringBuffer buffer = new StringBuffer();
//                if (!isnewoffer_individual) {
//                    if (offerFinalResponse != null)
//                        if (offerFinalResponse.getOfferCount().getNewOffers() != null) {
//                            offerPreviewRequest.setMerchantId(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantId());
//                            ((CoreApplication) getApplication()).setOffer_merchantName(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantName());
//                            ((CoreApplication) getApplication()).setMerchantimage(offerFinalResponse.getOfferCount().getNewOffers().get(position).getMerchantLogo());
//                            offerPreviewRequest.setG_transType(TransType.OFFER_NEWDETAILS_REQUEST.name());
//                            buffer.append(TransType.OFFER_NEWDETAILS_REQUEST.getURL());
//                        }
//                } else {
//                    if (offerFinalResponse != null)
//                        if (offerFinalResponse.getOfferCount().getMyactiveOffers() != null) {
//                            offerPreviewRequest.setMerchantId(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantId());
//                            offerPreviewRequest.setMerchantName(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantName());
//                            ((CoreApplication) getApplication()).setOffer_merchantName(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantName());
//                            offerPreviewRequest.setG_transType(TransType.OFFER_ACTIVEDETAILS_REQUEST.name());
//                            ((CoreApplication) getApplication()).setMerchantimage(offerFinalResponse.getOfferCount().getMyactiveOffers().get(position).getMerchantLogo());
//                            buffer.append(TransType.OFFER_ACTIVEDETAILS_REQUEST.getURL());
//                        }
//                }
//                if (offerPreviewRequest.getG_transType() != null && offerPreviewRequest.getMerchantId() != null) {
//                    String json = new Gson().toJson(offerPreviewRequest);
//                    buffer.append("?d=" + URLUTF8Encoder.encode(json));
//                    android.os.Handler messageHandler = new android.os.Handler() {
//                        @Override
//                        public void handleMessage(Message msg) {
//                            super.handleMessage(msg);
//                            hideIfVisible();
//                            if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
//                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
//                                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWDETAILS_RESPONSE.name()) || response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_ACTIVEDETAILS_RESPONSE.name())) {
//                                    offerFinalResponse = new Gson().fromJson((String) msg.obj, OfferFinalResponse.class);
//                                    OfferResponse offerResponse = new Gson().fromJson((String) msg.obj, OfferResponse.class);
//                                    ((CoreApplication) getApplication()).setOfferResponse(offerResponse);
//
//                                    ((CoreApplication) getApplication()).setOfferFinalResponse(offerFinalResponse);
//
//                                    Intent intent = new Intent(getBaseContext(), IndividualSavedMerchantOffersActivity.class);
//                                    startActivity(intent);
//
//                                } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
//                                    Toast toast = Toast.makeText(NewOffersActivityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
//                                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                                    toast.show();
//                                    Intent intent = new Intent(NewOffersActivityNewFlow.this, LoginActivity.class);
//                                    startActivity(intent);
//                                    NewOffersActivityNewFlow.this.finish();
//                                    return;
//                                } else {
//                                    Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                            } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
//                                Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
//                            } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
//                                Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                    };
//                    new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
//                    showIfNotVisible("");
//                }
//            }
//        });
//
//    }

//    private void getOffersCount() {
//        if (adapter != null) {
//            adapter.clear();
//        }
//        GenericRequest genericRequest = new GenericRequest();
//        genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
//        genericRequest.setG_transType(TransType.OFFER_NEWCOUNT_REQUEST.name());
//        String json = new Gson().toJson(genericRequest);
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(TransType.OFFER_NEWCOUNT_REQUEST.getURL());
//        buffer.append("?d=" + URLUTF8Encoder.encode(json));
//
//        android.os.Handler messageHandler = new android.os.Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                hideIfVisible();
//                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
//                    String network_response = ((String) msg.obj).trim();
//                    if (!network_response.isEmpty()) {
//                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
//                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name()) && response.getG_status() == 1) {
//                            offerFinalResponse = new Gson().fromJson((String) msg.obj, OfferFinalResponse.class);
//                            ((CoreApplication) getApplication()).setOfferFinalResponse(offerFinalResponse);
//                            offer_new_count_txt.setText("" + offerFinalResponse.getOfferCount().getTotalNewOffers());
//                            if (offerFinalResponse.getOfferCount().getTotalNewOffers().equalsIgnoreCase("0")) {
//                                offers_list.setAdapter(null);
//                                Toast.makeText(getBaseContext(), "There is no new offers for you", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            offers_new_txt.setVisibility(View.VISIBLE);
//                            offers_details_txt.setVisibility(View.VISIBLE);
//                            text_line.setVisibility(View.VISIBLE);
//                            adapter = new CustomListAdapter(NewOffersActivityNewFlow.this, offerFinalResponse.getOfferCount().getNewOffers());
//                            ((CoreApplication) getApplication()).setNewOfferCount(offerFinalResponse.getOfferCount().getTotalNewOffers());
//                            offers_list.setVisibility(View.VISIBLE);
//                            offers_list.setAdapter(adapter);
//                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
//                            Toast toast = Toast.makeText(NewOffersActivityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                            toast.show();
//                            Intent intent = new Intent(NewOffersActivityNewFlow.this, LoginActivity.class);
//                            startActivity(intent);
//                            NewOffersActivityNewFlow.this.finish();
//                            return;
//                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name()) && response.getG_status() != 1) {
//                            if (response.getG_errorDescription().equalsIgnoreCase("No offers available")) {
//                                Toast.makeText(getBaseContext(), getResources().getString(R.string.No_offers_available), Toast.LENGTH_SHORT).show();
//                                offers_list.setAdapter(null);
//                                offer_my_active_count_txt.setText("0");
//                                offers_list.setVisibility(View.VISIBLE);
//                                offers_new_txt.setVisibility(View.GONE);
//                                offer_new_count_txt.setVisibility(View.VISIBLE);
//                                offer_new_count_txt.setText("0");
//                                text_line.setVisibility(View.GONE);
//                                offers_details_txt.setVisibility(View.GONE);
//                            } else {
//                                Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
//                                offers_list.setAdapter(null);
//                                offer_my_active_count_txt.setText("0");
//                                offers_list.setVisibility(View.VISIBLE);
//                                offers_new_txt.setVisibility(View.GONE);
//                                offer_new_count_txt.setVisibility(View.VISIBLE);
//                                offer_new_count_txt.setText("0");
//                                text_line.setVisibility(View.GONE);
//                                offers_details_txt.setVisibility(View.GONE);
//                            }
//                            return;
//                        } else {
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
//                            offers_list.setAdapter(null);
//                            offer_my_active_count_txt.setText("0");
//                            offers_list.setVisibility(View.VISIBLE);
//                            offers_new_txt.setVisibility(View.GONE);
//                            text_line.setVisibility(View.GONE);
//                            offers_details_txt.setVisibility(View.GONE);
//                            return;
//                        }
//                    } else {
//                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
//                    }
//                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
//                    Toast.makeText(getBaseContext(), getApplicationContext().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
//                    offers_list.setAdapter(null);
//                    offer_new_count_txt.setText("0");
//                    offers_list.setVisibility(View.VISIBLE);
//                    offers_new_txt.setVisibility(View.GONE);
//                    text_line.setVisibility(View.GONE);
//                    offers_details_txt.setVisibility(View.GONE);
//                    return;
//                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
//                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
//                    offers_list.setAdapter(null);
//                    offer_new_count_txt.setText("0");
//                    offers_list.setVisibility(View.VISIBLE);
//                    text_line.setVisibility(View.GONE);
//                    offer_my_active_count_txt.setVisibility(View.GONE);
//                    offers_details_txt.setVisibility(View.GONE);
//                    return;
//                }
//            }
//        };
//        new Thread(new ServerConnection(0, messageHandler, buffer.toString())).start();
//        showIfNotVisible("");
//
//


        getMyActiveOffersCount();

        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }
    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {


            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Location permission", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void showIfnotVisible(String title) {
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

    private void hideifVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        getMyActiveOffersCount();
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
//        genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.NEW_OFFER_ACTIVEDETAILS_REQUEST.name());
        String json = new Gson().toJson(genericRequest);

        Log.e("Offers List Data: ", json);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.NEW_OFFER_ACTIVEDETAILS_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));

        Log.e("Offers List", "Url: " + buffer.toString());

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String my_active_response = ((String) msg.obj).trim();

                    Log.e("Offers Res: ", my_active_response);

                    if (!my_active_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(my_active_response, GenericResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_OFFER_ACTIVEDETAILS_RESPONSE.name()) && response.getG_status() == 1) {
                            offerFinalResponse = new Gson().fromJson((String) msg.obj, OffersNewFlow.class);
                            ((CoreApplication) getApplication()).setOfferNewFlowFinalResponse(offerFinalResponse);

                            offers_new_txt.setVisibility(View.VISIBLE);
                            offers_details_txt.setVisibility(View.VISIBLE);
                            adapter = new CustomListAdapter(NewOffersActivityNewFlow.this, offerFinalResponse.getNewFlowOffers());
                            offers_list.setAdapter(adapter);
                            text_line.setVisibility(View.VISIBLE);
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(NewOffersActivityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(NewOffersActivityNewFlow.this, LoginActivity.class);
                            startActivity(intent);
                            NewOffersActivityNewFlow.this.finish();
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
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");

    }

    private class CustomListAdapter extends ArrayAdapter<NewFlowOffer> {


        private List<NewFlowOffer> genericResponses = null;

        private CustomListAdapter(Activity context, List<NewFlowOffer> _genericResponses) {
            super(context, R.layout.offers_list_row, _genericResponses);


            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.offer_bg)
                    .showImageForEmptyUri(R.drawable.offer_bg)
                    .showImageOnFail(R.drawable.offer_bg)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();


//            initImageLoader(getBaseContext());

//            initImageLoader(getApplicationContext());

            initImageLoader(NewOffersActivityNewFlow.this);



            genericResponses = _genericResponses;

        }


        @Override
        public int getViewTypeCount() {
            if(genericResponses.size()==0){
                return 1;
            }
            return genericResponses.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            merchantDetailsAndOfferCount = getItem(position);

            Log.e("Offer Details: ", " "+merchantDetailsAndOfferCount);

            if (convertView == null) {

                //>=8.2
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_list_row_bita2, parent, false);

                //<8.2
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_list_row_new, parent, false);



                holder = new ViewHolder();
                holder.offers_merchant_name = (TextView) convertView.findViewById(R.id.offers_merchant_name);
                holder.offer_expiry = (TextView) convertView.findViewById(R.id.offer_expiry);
                holder.offer_background_image = (ImageView) convertView.findViewById(R.id.offer_background_image);

                holder.merchant_logo = (ImageView) convertView.findViewById(R.id.merchant_logo);

                holder.offer_text = (TextView)convertView.findViewById(R.id.offer_text);
                holder.offer_expiry = (TextView)convertView.findViewById(R.id.offer_expiry);
                holder.offer_discount = (TextView) convertView.findViewById(R.id.offer_discount);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            ImageSize imSize  = new ImageSize(380,240);






            ImageLoader.getInstance()
                    .displayImage(merchantDetailsAndOfferCount.getOfferImage(), holder.offer_background_image, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                            DiskCacheUtils.removeFromCache(merchantDetailsAndOfferCount.getOfferImage(), ImageLoader.getInstance().getDiskCache());
                            MemoryCacheUtils.removeFromCache(merchantDetailsAndOfferCount.getOfferImage(), ImageLoader.getInstance().getMemoryCache());

                            ((ImageView)view).setImageResource(R.drawable.offer_bg);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            ((ImageView)view).setImageResource(R.drawable.offer_bg);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            ((ImageView)view).setImageResource(android.R.color.transparent);

                            view.setBackground(new BitmapDrawable(getResources(), loadedImage));

//                            ((ImageView)view).setImageResource(R.drawable.offer_bg);



                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {


                        }
                    });





            if(holder.merchant_logo!=null) {

                //Loading merchant logo
                ImageLoader.getInstance()
                        .displayImage(merchantDetailsAndOfferCount.getMerchantLogo(), holder.merchant_logo, options, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {


                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {


                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                ((ImageView) view).setImageResource(android.R.color.transparent);


//                                Bitmap paddedImage = pad(loadedImage,40,40);

//                                Bitmap loadedImageRoundedScaled = Bitmap.createScaledBitmap(paddedImage, 120, 120, false);



                                Bitmap  whiteBorderedBitmap =  addWhiteBorder(loadedImage,50);

//                                Bitmap  downloadedBitmap = getResizedBitmap(loadedImage,100);


//                                Bitmap loadedImageRounded = getRoundedCornerBitmap(whiteBorderedBitmap,360);


                                Bitmap loadedImageRoundedNew = getCircularBitmap(whiteBorderedBitmap);


                                view.setBackground(new BitmapDrawable(getResources(), loadedImageRoundedNew));

//                            ((ImageView)view).setImageResource(R.drawable.offer_bg);


                            }
                        }, new ImageLoadingProgressListener() {
                            @Override
                            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            }
                        });

            }


            //Uncomment Only when plain images updated  for PRD release flor 8.2
            //Comment below code for < 8.2


/*

            holder.offers_merchant_name.setText(merchantDetailsAndOfferCount.getMerchantName());

            holder.offer_expiry.setText( "Exp: "+merchantDetailsAndOfferCount.getValidity());


            if(merchantDetailsAndOfferCount.getOfferText()!=null) {
                holder.offer_text.setText(merchantDetailsAndOfferCount.getOfferText());
            }else{
                holder.offer_text.setText("Not available");
            }

            if(merchantDetailsAndOfferCount.getDiscount()!=null) {
                holder.offer_discount.setText("  " + merchantDetailsAndOfferCount.getDiscount()+"% ");
            }else{

//                holder.offer_discount.setText("NA");

                if(merchantDetailsAndOfferCount.getDiscountFlat()!=null) {
                    holder.offer_discount.setText("  " + merchantDetailsAndOfferCount.getDiscountFlat() );
                }else{
                    holder.offer_discount.setText("NA");
                }

            }




//            Picasso.get().load(merchantDetailsAndOfferCount.getOfferImage()).into(holder.offer_background_image);

           // Picasso.get().load(merchantDetailsAndOfferCount.getOfferImage()).resizeDimen(R.drawable.sample_offers_background,R.drawable.sample_offers_background).into(holder.offer_background_image);


//            new DownloadImageTask(holder.offer_background_image,merchantDetailsAndOfferCount.getOfferImage()).execute();



            //FOR ONLY Offer Image
//            holder.offers_merchant_name.setText(""+merchantDetailsAndOfferCount.getMerchantName());
//            holder.offer_expiry.setText("" + merchantDetailsAndOfferCount.getValidity());


//            holder.offer_expiry.setText("" + merchantDetailsAndOfferCount.getOfferImage());


            if (offerFinalResponse.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_NEWCOUNT_RESPONSE.name())) {
//                holder.offer_validity.setText("New Offers  " + merchantDetailsAndOfferCount.getOfferCount());
            } else {
//                holder.offer_validity.setText("Active Offers " + merchantDetailsAndOfferCount.getOfferCount());
            }



*/



            return convertView;
        }
    }

    private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public Bitmap pad(Bitmap Src, int padding_x, int padding_y) {
        Bitmap outputimage = Bitmap.createBitmap(Src.getWidth() + padding_x,Src.getHeight() + padding_y, Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(outputimage);
        can.drawARGB(0,0,0,0); //This represents White color
        can.drawBitmap(Src, padding_x, padding_y, null);
        return outputimage;
    }


    protected Bitmap getCircularBitmap(Bitmap srcBitmap) {
        // Calculate the circular bitmap width with border
        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());
        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap (
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);
        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth-srcBitmap.getWidth())/2;
        float top = (squareBitmapWidth-srcBitmap.getHeight())/2;
        canvas.drawBitmap(srcBitmap, left, top, paint);
        // Free the native object associated with this bitmap.
        srcBitmap.recycle();
        // Return the circular bitmap
        return dstBitmap;
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

            bmImage.setBackground(new BitmapDrawable(getResources(), result));
        }
    }

    public class ViewHolder {

        ImageView offer_background_image;

        ImageView merchant_logo;

        TextView offers_merchant_name;
        TextView offer_text;
        TextView offer_expiry;
        TextView offer_discount;

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

        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(getBaseContext(), MainActivityNewFlow.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//    }


}
