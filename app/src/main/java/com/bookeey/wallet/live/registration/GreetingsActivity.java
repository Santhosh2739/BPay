package com.bookeey.wallet.live.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.OoredooValidation;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.offers.NewOffersActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
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
import ycash.wallet.json.pojo.greetings.GreetingResponse;

/**
 * Created by 30099 on 6/9/2016.
 */
public class GreetingsActivity extends Activity {
    ImageView imageView_close;
    TextView greetings_layout_txt;
    ImageView greetings_image;
    private DisplayImageOptions options;
    private ProgressDialog progress;
    GreetingResponse greetingResponse;
    FrameLayout mainframe_greetings;
    public static Context context;
    boolean authentication_status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getApplicationContext();
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.greetings_layout);
        progress = new ProgressDialog(GreetingsActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mainframe_greetings = (FrameLayout) findViewById(R.id.mainframe_greetings);
        // if(authentication_status)
        getGreetings();
        greetings_layout_txt = (TextView) findViewById(R.id.greetings_layout_txt);
        imageView_close = (ImageView) findViewById(R.id.imageView_close);
        greetings_image = (ImageView) findViewById(R.id.greetings_image);
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int app_status = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.APP_STATUS);
                if (app_status == 4 || app_status == 3) {
                    CoreApplication application = (CoreApplication) getApplication();
                    if (application.isUserLoggedIn()) {
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Intent in = new Intent(getBaseContext(), OoredooValidation.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                }
            }
        });
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.new_logo_virtual_grey)
                .showImageForEmptyUri(R.drawable.new_logo_virtual_grey)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        initImageLoader(getBaseContext());
        this.setFinishOnTouchOutside(false);
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

    private void getGreetings() {
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_transType(TransType.APP_GREETING_REQUEST.name());
        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.APP_GREETING_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    String response_str = (String) msg.obj;

                    if (response_str.contains("java.security.cert.CertificateException")) {
                        Toast toast = Toast.makeText(GreetingsActivity.this, "Server certification authentication failed.!!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        GreetingsActivity.this.finish();
                    } else {
                        GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);

                        if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.APP_GREETING_RESPONSE.name())) {
                            mainframe_greetings.setVisibility(View.VISIBLE);
                            greetingResponse = new Gson().fromJson((String) msg.obj, GreetingResponse.class);
                            ImageLoader.getInstance()
                                    .displayImage(greetingResponse.getImagePath(), greetings_image, options, new SimpleImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {
                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            if (imageUri.contains(".gif")) {
                                                Glide.with(context)
                                                        .load(imageUri)
                                                        //.asGif()
                                                       // .crossFade()
                                                        .into(greetings_image);
                                            }
                                        }
                                    }, new ImageLoadingProgressListener() {
                                        @Override
                                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                        }
                                    });
                            greetings_layout_txt.setText(greetingResponse.getText());

                        } else {
                            int app_status = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.APP_STATUS);
                            if (app_status == 4 || app_status == 3) {
                                CoreApplication application = (CoreApplication) getApplication();
                                if (application.isUserLoggedIn()) {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {

                                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                Intent in = new Intent(getBaseContext(), OoredooValidation.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                finish();
                            }
                        }
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    int app_status = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.APP_STATUS);
                    if (app_status == 4 || app_status == 3) {
                        CoreApplication application = (CoreApplication) getApplication();
                        if (application.isUserLoggedIn()) {
                            startActivity(new Intent(GreetingsActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(GreetingsActivity.this, LoginActivity.class));
                        }
                        finish();
                    } else {
                        Intent in = new Intent(getBaseContext(), OoredooValidation.class);
                        startActivity(in);
                        finish();
                    }

                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    int app_status = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.APP_STATUS);
                    if (app_status == 4 || app_status == 3) {
                        CoreApplication application = (CoreApplication) getApplication();
                        if (application.isUserLoggedIn()) {
                            startActivity(new Intent(GreetingsActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(GreetingsActivity.this, LoginActivity.class));
                        }
                        finish();
                    } else {
                        Intent in = new Intent(getBaseContext(), OoredooValidation.class);
                        startActivity(in);
                        finish();
                    }
                }


                return;
            }

        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");

    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
            progress.dismiss();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        hideIfVisible();
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

}
