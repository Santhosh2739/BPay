package com.bookeey.wallet.live.prepaidcard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
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
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.virtualprepaidcards.CardDetails;
import ycash.wallet.json.pojo.virtualprepaidcards.DenominationRequest;
import ycash.wallet.json.pojo.virtualprepaidcards.PrepaidCardsListResponse;

/**
 * Created by 30099 on 6/15/2016.
 */
public class VirtualPrepaidcardListActivity extends GenericActivity {
    ListView virtual_card_list;
    VirtualPrepaidAdapter adapter;
    private DisplayImageOptions options;
    PrepaidCardViewHolder holder;
    PrepaidCardsListResponse prepaidCardsListResponse;
    CardDetails cardDetails;
    ProgressDialog progress;
    List<CardDetails> cardDetailsList;
    Map<Integer, CardDetails> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_card_list);

        View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("Virtual Prepaid Cards");

        String response_str = getIntent().getStringExtra("VIRTUAL_CARD_RESPONSE");
        prepaidCardsListResponse = new Gson().fromJson(response_str, PrepaidCardsListResponse.class);
        progress = new ProgressDialog(VirtualPrepaidcardListActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        virtual_card_list = (ListView) findViewById(R.id.virtual_card_list);
        getPrepaidCardList();


        virtual_card_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ((CoreApplication) getApplication()).setCardImage(cardDetailsList.get(position).getImages());
                ((CoreApplication) getApplication()).setCardName(cardDetailsList.get(position).getCardName());
                Intent intent = new Intent(getBaseContext(), VirtualDenominationActivity.class);
                startActivity(intent);

            }
        });
    }


    class VirtualPrepaidAdapter extends ArrayAdapter<CardDetails> {
        private VirtualPrepaidAdapter(Activity context, List<CardDetails> genericResponses) {
            super(context, R.layout.virtual_card_row, genericResponses);
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
            cardDetails = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.virtual_card_row, parent, false);
                holder = new PrepaidCardViewHolder();
                holder.virtual_prepaid_img = (ImageView) convertView.findViewById(R.id.prepaid_virtual_card_image);
                convertView.setTag(holder);
            } else {
                holder = (PrepaidCardViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(cardDetails.getImages(), holder.virtual_prepaid_img, options, new SimpleImageLoadingListener() {
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
            return convertView;
        }
    }

    public class PrepaidCardViewHolder {
        ImageView virtual_prepaid_img;

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

    private void getPrepaidCardList() {
        GenericRequest genericRequest = new GenericRequest();
        CustomerLoginRequestReponse loginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        genericRequest.setG_oauth_2_0_client_token(loginRequestReponse.getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.PREPAID_CATEGORYLIST_REQUEST.name());
        String jsondata = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PREPAID_CATEGORYLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CATEGORYLIST_RESPONSE.name()) && response.getG_status() == 1) {
                        prepaidCardsListResponse = new Gson().fromJson((String) msg.obj, PrepaidCardsListResponse.class);
                        map = new TreeMap<Integer, CardDetails>(prepaidCardsListResponse.getList());
                        cardDetailsList = new ArrayList<CardDetails>(map.values());
                        ((CoreApplication) getApplication()).setPrepaidCardsListResponse(prepaidCardsListResponse);
                        ((CoreApplication) getApplication()).setCardDetailsList(cardDetailsList);
                        adapter = new VirtualPrepaidAdapter(VirtualPrepaidcardListActivity.this, cardDetailsList);
                        virtual_card_list.setAdapter(adapter);
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(VirtualPrepaidcardListActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(VirtualPrepaidcardListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        VirtualPrepaidcardListActivity.this.finish();
                        return;
                    } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CATEGORYLIST_RESPONSE.name()) && response.getG_status() != 1) {
                        Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
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
