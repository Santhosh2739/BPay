package com.bookeey.wallet.live.prepaidcard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import ycash.wallet.json.pojo.virtualprepaidcards.CardDetails;
import ycash.wallet.json.pojo.virtualprepaidcards.DenominationRequest;
import ycash.wallet.json.pojo.virtualprepaidcards.PrepaidCardsListResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.RequestCardResponse;

/**
 * Created by 30099 on 6/16/2016.
 */
public class VirtualDenominationActivity extends GenericActivity {
    ImageView virtual_denomination_image;
    ProgressDialog progress;
    private DisplayImageOptions options;
    RadioGroup radiogroup_countries;
    RadioButton usa_radio0, uk_radio01;
    List<CardDetails> cardDetailsList;
    Map<Integer, CardDetails> map;
    PrepaidCardsListResponse prepaidCardsListResponse;
    ListView denomination_list;
    CustomGridViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virutal_denomination_layout);
        progress = new ProgressDialog(VirtualDenominationActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText(((CoreApplication) getApplication()).getCardName() + "- Card");
        virtual_denomination_image = (ImageView) findViewById(R.id.virtual_denomination_image);

        cardDetailsList = ((CoreApplication) getApplication()).getCardDetailsList();
//        prepaidCardsListResponse    = ((CoreApplication)getApplication()).getPrepaidCardsListResponse();
        denomination_list = (ListView) findViewById(R.id.denomination_list);
        getList();
        /*adapter = new CustomGridViewAdapter(VirtualDenominationActivity.this, prepaidCardsListResponse.getPriceList());
        denomination_list.setAdapter(adapter);
*/

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
                .displayImage(((CoreApplication) getApplication()).getCardImage(), virtual_denomination_image, options, new SimpleImageLoadingListener() {
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
        denomination_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DenominationRequest denominationRequest = new DenominationRequest();
                denominationRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
                denominationRequest.setCountryName(((CoreApplication) getApplication()).getCountry_name());
                denominationRequest.setCardId(prepaidCardsListResponse.getPriceList().get(position).getCardId());
                denominationRequest.setPrice(prepaidCardsListResponse.getPriceList().get(position).getPrice());
                ((CoreApplication) getApplication()).setCardPrice(prepaidCardsListResponse.getPriceList().get(position).getPrice());
                denominationRequest.setG_transType(TransType.PREPAID_REQUESTCARD_REQUEST.name());
                String jsondata = new Gson().toJson(denominationRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.PREPAID_REQUESTCARD_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));

                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_REQUESTCARD_RESPONSE.name()) && response.getG_status() == 1) {
                                RequestCardResponse requestCardResponse = new Gson().fromJson((String) msg.obj, RequestCardResponse.class);
                                ((CoreApplication) getApplication()).setRequestCardResponse(requestCardResponse);
                                Intent intent = new Intent(getBaseContext(), LocalCurrencyAmountActivity.class);
                                startActivity(intent);
                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(VirtualDenominationActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(VirtualDenominationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                VirtualDenominationActivity.this.finish();
                                return;
                            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_REQUESTCARD_RESPONSE.name()) && response.getG_status() != 1) {
                                Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                                denomination_list.setAdapter(null);
                                return;
                            } else {
                                Toast.makeText(getBaseContext(), "General server error", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(), "Failure general server", Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(), "Failure network connection", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfNotVisible("");

            }
        });


    }

    private void getList() {
        DenominationRequest denominationRequest = new DenominationRequest();
        denominationRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        denominationRequest.setCardName(((CoreApplication) getApplication()).getCardName());
        denominationRequest.setG_transType(TransType.PREPAID_DENOMINATION_REQUEST.name());
        String jsondata = new Gson().toJson(denominationRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PREPAID_DENOMINATION_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_DENOMINATION_RESPONSE.name()) && response.getG_status() == 1) {
                        prepaidCardsListResponse = new Gson().fromJson((String) msg.obj, PrepaidCardsListResponse.class);
                        ((CoreApplication) getApplication()).setPrepaidCardsListResponse(prepaidCardsListResponse);
                        adapter = new CustomGridViewAdapter(VirtualDenominationActivity.this, prepaidCardsListResponse.getPriceList());
                        denomination_list.setAdapter(adapter);

                    } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_DENOMINATION_RESPONSE.name()) && response.getG_status() != 1) {
                        Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        denomination_list.setAdapter(null);
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), "General server error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), "Failure general server", Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), "Failure network connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");
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
}
