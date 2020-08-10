package com.bookeey.wallet.live.offers;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
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
import ycash.wallet.json.pojo.offers.OfferPreviewRequest;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;
import ycash.wallet.json.pojo.offers.OfferResponse;
import ycash.wallet.json.pojo.offers.Offers;

/**
 * Created by 30099 on 4/23/2016.
 */
public class OffersActivity extends GenericActivity {
    ListView offers_list;
    Button offer_new_my_active, offer_new_btn;
    private CustomListAdapter adapter;
    private ProgressDialog progress;
    ViewHolder holder;
    OfferResponse offerResponse = null;
    Offers offers = null;
    LinearLayout my_active_offer_linear, new_offer_linear;
    private DisplayImageOptions options;
    TextView offer_new_count_txt, offer_my_active_count_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_layout);
        View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText(getResources().getString(R.string.offers_title));
        my_active_offer_linear = (LinearLayout) findViewById(R.id.my_active_offer_linear);
        new_offer_linear = (LinearLayout) findViewById(R.id.new_offer_linear);

        my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
        offer_new_my_active = (Button) findViewById(R.id.offer_new_my_active);
        offer_new_btn = (Button) findViewById(R.id.offer_new_btn);


        offer_new_count_txt = (TextView) findViewById(R.id.offer_new_count_txt);
        offer_my_active_count_txt = (TextView) findViewById(R.id.offer_my_active_count_txt);

        offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));

        offer_my_active_count_txt.setVisibility(View.GONE);
        offer_new_my_active.setTextColor(getResources().getColor(R.color.light_blue));
        my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_light_gray));


        offer_new_my_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offer_my_active_count_txt.setVisibility(View.VISIBLE);
                offer_my_active_count_txt.setTextColor(getResources().getColor(R.color.light_blue));
                offer_my_active_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
                offer_my_active_count_txt.setText("12");
                offer_new_my_active.setTextColor(getResources().getColor(R.color.white));
                offer_new_count_txt.setVisibility(View.GONE);
                my_active_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
                new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_btn.setTextColor(getResources().getColor(R.color.light_blue));
                offer_new_my_active.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));
            }
        });
        offer_new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offer_my_active_count_txt.setVisibility(View.GONE);
                offer_new_my_active.setTextColor(getResources().getColor(R.color.light_blue));
                my_active_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setVisibility(View.VISIBLE);
                new_offer_linear.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setTextColor(getResources().getColor(R.color.light_blue));
                offer_new_btn.setTextColor(getResources().getColor(R.color.white));
                offer_new_my_active.setBackgroundColor(getResources().getColor(R.color.very_lightgray));
                offer_new_count_txt.setText("12");
                offer_new_count_txt.setBackgroundDrawable(getResources().getDrawable(R.drawable.offer_count_background));
                new_offer_linear.setBackgroundDrawable(getResources().getDrawable(R.drawable.offers_background));

            }
        });
        offers_list = (ListView) findViewById(R.id.offers_list);
        progress = new ProgressDialog(OffersActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        offers_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                OfferPreviewRequest offerPreviewRequest = new OfferPreviewRequest();
                offerPreviewRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
                offerPreviewRequest.setG_transType(TransType.OFFER_PREVIEW_REQUEST.name());
                offerPreviewRequest.setOfferId(((CoreApplication) getApplication()).getOfferResponse().getOffers().get(position).getOfferId());
                offerPreviewRequest.setOfferName(((CoreApplication) getApplication()).getOfferResponse().getOffers().get(position).getOfferName());
                String json = new Gson().toJson(offerPreviewRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.OFFER_REQUEST.getURL());
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
                                ((CoreApplication) getApplication()).setOfferPreviewResponse(offerPreviewResponse);
                                Intent intent = new Intent(getBaseContext(), ShowOfferActivity.class);
                                startActivity(intent);
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

    private class CustomListAdapter extends ArrayAdapter<Offers> {
        private CustomListAdapter(Activity context, List<Offers> genericResponses) {
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
            offers = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_list_row, parent, false);
                holder = new ViewHolder();
//                holder.offers_offer_name = (TextView) convertView.findViewById(R.id.offers_offer_name);
                holder.offers_merchant_logo = (ImageView) convertView.findViewById(R.id.offers_merchant_logo);
                holder.offers_merchant_name = (TextView) convertView.findViewById(R.id.offers_merchant_name);
//                holder.offer_validity= (TextView) convertView.findViewById(R.id.offers_list_validity);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance()
                    .displayImage(offers.getMerchantLogo(), holder.offers_merchant_logo, options, new SimpleImageLoadingListener() {
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
            holder.offers_offer_name.setText(offers.getOfferName());
            holder.offers_merchant_name.setText(offers.getMerchantName());
            holder.offer_validity.setText("Offer Valid\n" + "From :" + offers.getFromDate() + " " + offers.getStartTime() + "\nTo:     " + offers.getToDate() + " " + offers.getEndIime());
            return convertView;
        }
    }

    public class ViewHolder {
        TextView offers_offer_name;
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