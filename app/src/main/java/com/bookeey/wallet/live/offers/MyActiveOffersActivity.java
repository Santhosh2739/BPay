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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
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
 * Created by 30099 on 4/25/2016.
 */
public class MyActiveOffersActivity extends GenericActivity{
    ListView my_offers_list_view;
    CustomOffersAdapter adapter;
    ViewHolder holder;
    Offers offers= null;
    OfferResponse offerResponse= null;
    private ProgressDialog progress;
    private DisplayImageOptions options;
    private String offer_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myoffers_layout);
        my_offers_list_view= (ListView) findViewById(R.id.my_offers_list_view);
        progress= new ProgressDialog(MyActiveOffersActivity.this, R.style.MyTheme2);
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
        mTitleTextView.setText("MY ACTIVE OFFERS");
        CustomerLoginRequestReponse customerLoginRequestReponse= ((CoreApplication)getApplication()).getCustomerLoginRequestReponse();
        GenericRequest genericRequest= new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.OFFER_MYACTIVE_REQUEST.name());
        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.OFFER_MYACTIVE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if(msg.arg1== ServerConnection.OPERATION_SUCCESS){
                    GenericResponse response = new Gson().fromJson((String)msg.obj,GenericResponse.class);
                    if(null!=response && response.getG_status()==1&&response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_MYACTIVE_RESPONSE.name())){
                        offerResponse= new Gson().fromJson((String) msg.obj, OfferResponse.class);
                        ((CoreApplication)getApplication()).setOfferResponse(offerResponse);
                        adapter= new CustomOffersAdapter(MyActiveOffersActivity.this, offerResponse.getOffers());
                        my_offers_list_view.setAdapter(adapter);
                    }else{
                        Toast.makeText(getBaseContext(), "No offers available ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if(msg.arg1== ServerConnection.OPERATION_FAILURE_GENERAL_SERVER){
                    Toast.makeText(getBaseContext(), "Failure general server",Toast.LENGTH_SHORT).show();
                }else if(msg.arg1== ServerConnection.OPERATION_FAILURE_NETWORK){
                    Toast.makeText(getBaseContext(), "Failure network connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");
        my_offers_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                OfferResponse offerResponse1= ((CoreApplication)getApplication()).getOfferResponse();
                CustomerLoginRequestReponse customerLoginRequestReponse= ((CoreApplication)getApplication()).getCustomerLoginRequestReponse();
                OfferPreviewRequest offerPreviewRequest= new OfferPreviewRequest();
                offerPreviewRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                offerPreviewRequest.setG_transType(TransType.OFFER_PREVIEW_REQUEST.name());
                offerPreviewRequest.setOfferId(offerResponse1.getOffers().get(position).getOfferId());
                String json = new Gson().toJson(offerPreviewRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.OFFER_PREVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if(msg.arg1== ServerConnection.OPERATION_SUCCESS){
                            GenericResponse response = new Gson().fromJson((String)msg.obj,GenericResponse.class);
                            if(null!=response && response.getG_status()==1&&response.getG_response_trans_type().equalsIgnoreCase(TransType.OFFER_PREVIEW_RESPONSE.name())){
                                OfferPreviewResponse offerPreviewResponse= new Gson().fromJson((String)msg.obj, OfferPreviewResponse.class);
                                ((CoreApplication)getApplication()).setMyoOfferPreviewResponse(offerPreviewResponse);
                                offer_type= offerResponse.getOffers().get(position).getOfferType();
                                if(offer_type==null){
                                    Toast.makeText(getBaseContext(),"No offers are there to show", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ((CoreApplication)getApplication()).setOffer_type(offer_type);
                                Intent intent= new Intent(getBaseContext(), RedeemOffersActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getBaseContext(), "No offers available ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }else if(msg.arg1== ServerConnection.OPERATION_FAILURE_GENERAL_SERVER){
                            Toast.makeText(getBaseContext(), "Failure general server",Toast.LENGTH_SHORT).show();
                        }else if(msg.arg1== ServerConnection.OPERATION_FAILURE_NETWORK){
                            Toast.makeText(getBaseContext(), "Failure network connection",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfNotVisible("");
            }
        });
    }
    private void hideIfVisible(){
        if (progress.isShowing()) {
            progress.hide();
        }
    }
    private void showIfNotVisible(String title){
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
    private class CustomOffersAdapter extends ArrayAdapter<Offers>{
        private CustomOffersAdapter(Activity context, List<Offers> genericResponses){
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
            offers= getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.offers_list_row, parent, false);
                holder= new ViewHolder();
                holder.offer_logo       = (ImageView)convertView.findViewById(R.id.offers_merchant_logo);
                holder.merchantname     = (TextView) convertView.findViewById(R.id.offers_merchant_name);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            holder.title.setText(""+offers.getOfferName());
            holder.merchantname.setText(offers.getMerchantName());
            ((CoreApplication)getApplication()).setOffer_type(offer_type);
            ImageLoader.getInstance()
                    .displayImage(offers.getMerchantLogo(), holder.offer_logo, options, new SimpleImageLoadingListener() {
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
            holder.offerValidity.setText("Offer Valid\n"+"From :"+offers.getFromDate()+" "+offers.getStartTime()+"\nTo:     "+offers.getToDate()+" "+offers.getEndIime());
            return convertView;
        }
    }
    public  void initImageLoader(Context context) {
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
    class ViewHolder {
        TextView title;
        TextView offerValidity;
        TextView merchantname;
        ImageView offer_logo;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(getBaseContext(), OffersActivity.class);
        startActivity(intent);
    }
}