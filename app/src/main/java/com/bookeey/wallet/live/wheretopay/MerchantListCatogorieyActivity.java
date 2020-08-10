package com.bookeey.wallet.live.wheretopay;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.Merchant_Map_Activity;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.forceface.TransTypeInterface;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.List;

import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericListActivity;
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
import ycash.wallet.json.pojo.wheretopay.MerchantCategoryDetailsListPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;
import ycash.wallet.json.pojo.wheretopay.MerchnatListRequest;

/**
 * Created by 30099 on 4/7/2016.
 */
public class MerchantListCatogorieyActivity extends GenericListActivity {
    ListView merchantdetails_list_name;
    MerchantCategoryDetailsListPojo merchantCategoryDetailsListPojo;
    MerchantListResponse merchantListResponse;
    private CustomListAdapter adapter = null;
    ViewHolder holder;
    private DisplayImageOptions options;
    ImageView mapbutton, merchant_category_screen_wallet_logo_back, home_up_back;
    String categoryId, categoryName;
    ProgressDialog progress = null;

    private FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantdetails_layout);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);



        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);

        categoryId = ((CoreApplication) getApplication()).getCategoryId();
        categoryName = ((CoreApplication) getApplication()).getCategoryname();
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(categoryName);
        progress = new ProgressDialog(this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
        adapter = new CustomListAdapter(MerchantListCatogorieyActivity.this, merchantListResponse.getMerchantDetails());
        setListAdapter(adapter);
        ((Button) findViewById(R.id.merchant_category_list_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MerchantListCatogorieyActivity.this.finish();
            }
        });
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
        mapbutton = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_image_logo);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
                merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MAPVIEW_REQUEST.name());
                merchnatListRequest.setCategoryID(categoryId);
                String json = new Gson().toJson(merchnatListRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideifVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            try {
                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.WHERE_TO_PAY_MAPVIEW_RESPONSE.name())) {
                                    merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                                    ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
                                    ((CoreApplication) getApplication()).setImagePath(merchantCategoryDetailsListPojo.getPath());
                                    if (merchantListResponse.getMerchantDetails().size() == 0) {
                                        Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(getBaseContext(), MerchantMapsActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Log.e("Error", "" + e);
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
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page listing merchants under a specific category");

        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 20);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page listing merchants under a specific category");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 20 logged");
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
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
        merchnatListRequest.setCategoryID(categoryId);
        merchnatListRequest.setMerchantName(merchantListResponse.getMerchantDetails().get(position).getMerchantName());
        merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.name());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
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
                            merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
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
    }

    class CustomListAdapter extends ArrayAdapter<MerchantCategoryDetailsListPojo> {
        public CustomListAdapter(Activity context, List<MerchantCategoryDetailsListPojo> genericResponses) {
            super(context, R.layout.merchnat_list_row, genericResponses);
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.new_logo_grey)
                    .showImageForEmptyUri(R.drawable.new_logo_grey)
                    .showImageOnFail(R.drawable.new_logo_grey)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            initImageLoader(getBaseContext());
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            merchantCategoryDetailsListPojo = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchnat_list_row, parent, false);
                holder = new ViewHolder();
                holder.text_name = (TextView) convertView.findViewById(R.id.merchant_title_name);
                // holder.merchant_contact_img = (ImageView) convertView.findViewById(R.id.merchant_contact_img);
                holder.icon = (ImageView) convertView.findViewById(R.id.picture_icon);
               /* holder.merchant_contact_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.DIAL");
                        intent.setData(Uri.parse("tel:" + merchantListResponse.getMerchantDetails().get(position).getMobileNumber()));
                        startActivity(intent);
                    }
                });*/
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance()
                    .displayImage(merchantCategoryDetailsListPojo.getPath(), holder.icon, options, new SimpleImageLoadingListener() {
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
            holder.text_name.setText(merchantCategoryDetailsListPojo.getMerchantName());
            return convertView;
        }
    }

    public class ViewHolder {
        TextView text_name;
        TextView address;
        ImageView icon, merchant_contact_img;
        TransTypeInterface transTypeInterface;

        public TransTypeInterface getTransTypeInterface() {
            return transTypeInterface;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}