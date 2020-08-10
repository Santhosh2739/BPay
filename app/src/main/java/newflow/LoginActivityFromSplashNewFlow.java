package newflow;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.changes.MobChangeMobileNumber;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash;
import com.bookeey.wallet.live.login.ForgotPassword;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.showpushnotificationmessage.ShowPushNotificationMessageDialogActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.GetPushNotificationMessageProcessing;
import coreframework.processing.Login_processing.CustomerAutoLoginProcessingFromNFC;
import coreframework.processing.Login_processing.CustomerLoginProcessing;
import coreframework.processing.Login_processing.FingerPrintLoginProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.HandleUncaughtException;
import coreframework.utils.LocaleHelper;
import ycash.wallet.json.pojo.activationresponse.ActivationResponsePojo;
import ycash.wallet.json.pojo.getpushnotificationmessage.GetPushNotificationMessageRequest;
import ycash.wallet.json.pojo.getpushnotificationmessage.PushNotificationDetailsPojo;
import ycash.wallet.json.pojo.login.CustomerLoginRequest;

public class LoginActivityFromSplashNewFlow extends FragmentActivity implements YPCHeadlessCallback {
    TextView forgotpassword_text, forgotpassword_help_text,change_mobile_no_tv;
    ImageView imageView;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private GoogleCloudMessaging gcm = null;
    private final static String TAG = "DEVICE_ID";
    private static String SENDER_ID = "367248879471"; //google api project number
    //private static String SENDER_ID = "905970718814"; //NEW google api project number
    //private  static  String SENDER_ID="861276600463";
    //    ScheduledExecutorService scheduleTaskExecutor = null;
    //MyReceiver myReceiver;
    String selectedLanguage = null;
    LinearLayout language_layout, login_finger_print_layout;
    ImageView coutry_flag_img;
    TextView language_text, fingerprint_text, login_ok_text, goto_tour_text;
    //Finger print authentication
    private static final String TAG2 = LoginActivityFromSplashNewFlow.class.getSimpleName();

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    /**
     * Alias for our key in the Android Key Store
     */
    private static final String KEY_NAME = "my_key";

    public static final String KEY_SHOW_PUSH_NOTIFICATION_MESSAGE = "KEY_SHOW_PUSH_NOTIFICATION_MESSAGE";

    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;

    private KeyStore mKeyStore;
    private Cipher mCipher;

    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash mFragment;
    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_newflow_from_splash);
//        showMenu(false);
        language_layout = (LinearLayout) findViewById(R.id.language_layout);
        coutry_flag_img = (ImageView) findViewById(R.id.coutry_flag_img);
        language_text = (TextView) findViewById(R.id.language_text);
        fingerprint_text = (TextView) findViewById(R.id.fingerprint_text);
        login_ok_text = (TextView) findViewById(R.id.login_ok_text);
        login_finger_print_layout = (LinearLayout) findViewById(R.id.login_finger_print_layout);
        goto_tour_text = (TextView) findViewById(R.id.goto_tour_text);
        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(LoginActivityFromSplashNewFlow.this, selectedLanguage);
        }

        //to visible the finger print icon for higher versions like android M and above

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            login_ok_text.setVisibility(View.INVISIBLE);
            login_finger_print_layout.setVisibility(View.INVISIBLE);
        }
        goto_tour_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });

       /* String regid = getRegistrationId(LoginActivity.this);
        Log.i(this.toString(), "registration id from shared pref : " + regid);
        if (regid.isEmpty()) {
            // if blank, then app is not yet registered
            registerInBackground();
        }*/
        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("LOGIN");
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));*/


        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_with_notifications_bell, null);
        mActionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.login_title));


//        //Showing Notification count
        TextView count_text_top = (TextView) mCustomView.findViewById(R.id.count_text_top);
        String notification_count  = CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.NOTIFICATION_MSG_COUNT);
        count_text_top.setText(notification_count);

        if(notification_count.length()>0) {
            count_text_top.setText(""+notification_count);
        }else{
            count_text_top.setText("0");
        }

//        blinkNotificationCountView();





//        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
//        home_up_back.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.login_version_name)).setText("Version is : " + BuildConfig.VERSION_NAME);
        forgotpassword_text = (TextView) findViewById(R.id.forgotpassword_text);
        forgotpassword_help_text = (TextView) findViewById(R.id.forgotpassword_help_text);
        change_mobile_no_tv  = (TextView) findViewById(R.id.change_mobile_no_tv);

        final TextView login_user_id = (TextView) findViewById(R.id.login_user_id);
        final EditText login_password = (EditText) findViewById(R.id.login_password);

//        login_password.setText("1234");


        imageView = (ImageView) findViewById(R.id.ooredoo_header_logo);

        //fingerprint
        ((CoreApplication) getApplication()).inject(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                FINGERPRINT_PERMISSION_REQUEST_CODE);

        //language selection
        if (language_text.getText().toString().equals("English")) {
            language_text.setText(getResources().getString(R.string.login_arabic));
            coutry_flag_img.setImageResource(R.drawable.kuwait);
        } else {
            language_text.setText(getResources().getString(R.string.login_english));
            coutry_flag_img.setImageResource(R.drawable.usa);
        }
        language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (language_text.getText().toString().equals("English")) {
                    language_text.setText(getResources().getString(R.string.login_arabic));
                    selectedLanguage = "en";
                    coutry_flag_img.setImageResource(R.drawable.kuwait);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(LoginActivityFromSplashNewFlow.this, "en");
                    refresh(LoginActivityFromSplashNewFlow.this, "en");

                } else {
                    selectedLanguage = "ar";
                    language_text.setText(getResources().getString(R.string.login_english));
                    coutry_flag_img.setImageResource(R.drawable.usa);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(LoginActivityFromSplashNewFlow.this, "ar");
                    refresh(LoginActivityFromSplashNewFlow.this, "ar");
                }
            }
        });
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String session_string = b.getString("session_expired");
            if (session_string != null && !session_string.isEmpty()) {
                if (session_string.equalsIgnoreCase("Session expired"))
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), session_string, Toast.LENGTH_SHORT).show();
            }
        }



      /* String regid = getRegistrationId(LoginActivity.this);
       Log.i(this.toString(), "registration id from shared pref : " + regid);
       if (regid.isEmpty()) // if blank, then app is not yet registered
       {
           registerInBackground();
       }
       else{
           CustomSharedPreferences.saveGCMRegId(LoginActivity.this, "" + regid, CustomSharedPreferences.SP_KEY.GCM_REG_ID);
       }*/
        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        String name = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.NAME);
        login_user_id.setText("Hi " + name + "!");
        login_user_id.setEnabled(false);
        /*login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = login_password.getText().toString().trim().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(login_password.getWindowToken(), 0);
                }
            }
        });*/
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String userId = login_user_id.getText().toString();
                String password = login_password.getText().toString();
                if (password.length() == 0) {
                    Toast toast = Toast.makeText(LoginActivityFromSplashNewFlow.this, getResources().getString(R.string.login_password_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                /*if (password.length() != 7) {
                    Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_valid_password_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                /*String regid = getRegistrationId(LoginActivity.this);
                Log.i(this.toString(), "registration id from shared pref : " + regid);
                if (regid.isEmpty()) // if blank, then app is not yet registered
                {
                    registerInBackground();
                }
                else {*/
//                    CustomSharedPreferences.saveGCMRegId(LoginActivity.this, "" + regid, CustomSharedPreferences.SP_KEY.GCM_REG_ID);
                CustomerLoginRequest clr = new CustomerLoginRequest();
                clr.setLogin_pin(password);
                CustomSharedPreferences.saveStringData(getApplicationContext(), password, CustomSharedPreferences.SP_KEY.PIN);
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();

                Log.e("deviceID", "->" + deviceID);
                Log.e("deviceID", "->" + deviceID);


                String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
                clr.setMobileNumber(mobile_number);
                clr.setDeviceIdNumber(deviceID);
                if (selectedLanguage.equals("en")) {
                    clr.setLanguage("english");
                } else if (selectedLanguage.equals("ar")) {
                    clr.setLanguage("arabic");
                } else {
                    clr.setLanguage("english");
                }
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new CustomerLoginProcessing(clr, true, application));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");
//                }
            }
        });
        if (getIntent().hasExtra("temp_response")) {
            ActivationResponsePojo activationResponsePojo = new Gson().fromJson(getIntent().getStringExtra("temp_response"), ActivationResponsePojo.class);
            login_user_id.setText(activationResponsePojo.getMobileNumber());
            login_password.setText(activationResponsePojo.getNewPin());
            findViewById(R.id.login_btn).performClick();
        }
        forgotpassword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForgotPassword.class);
                startActivity(intent);
                LoginActivityFromSplashNewFlow.this.finish();
            }
        });

        forgotpassword_help_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Help.class);
                startActivity(intent);
            }
        });


        //Sep 18
        change_mobile_no_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), MobChangeMobileNumber.class);
                startActivity(intent);

            }
        });


        //Touch ID Exception Jan 20
        Thread.setDefaultUncaughtExceptionHandler(new HandleUncaughtException(this));



        //Fetch and Show push notification message from server
//        if(getIntent().getBooleanExtra(KEY_SHOW_PUSH_NOTIFICATION_MESSAGE,false)){
//
//
//
//
//
//        }


        FrameLayout notification_count_frame_layout= (FrameLayout)mCustomView. findViewById(R.id.notification_count_frame_layout);


//        BadgeView badge = new BadgeView(this, push_notification_message_bell);
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setBackgroundResource(R.drawable.bookeey_small);
//        badge.setText("1");
//        badge.show();


        notification_count_frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                //Fetch pushnotification message from server
                GetPushNotificationMessageRequest pushNotificationMessageRequest = new GetPushNotificationMessageRequest();

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new GetPushNotificationMessageProcessing(pushNotificationMessageRequest, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");


/*

                //For Test

                try {
//                    PushNotificationDetailsPojo responsePojo = new Gson().fromJson(response_json, PushNotificationDetailsPojo.class);


                    ArrayList<String> push_messages_al =  new ArrayList<String>();

                    //English
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");



                    //Arabic
//                    push_messages_al.add("مرحبًا بك في Bookeey ، لديك عرض لطيف للغاية في Lulu hypermarget ، تعجل احصل على هذا العرض..شكراً .. :-)");
//                    push_messages_al.add("مرحبًا بك في Bookeey ، لديك عرض لطيف للغاية في Lulu hypermarget ، تعجل احصل على هذا العرض..شكراً .. :-)");
//                    push_messages_al.add("مرحبًا بك في Bookeey ، لديك عرض لطيف للغاية في Lulu hypermarget ، تعجل احصل على هذا العرض..شكراً .. :-)");
//                    push_messages_al.add("مرحبًا بك في Bookeey ، لديك عرض لطيف للغاية في Lulu hypermarget ، تعجل احصل على هذا العرض..شكراً .. :-)");


//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
//                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");

                    JSONObject finalJsonObject = new JSONObject();

                    JSONArray pushMessageObjectJsonArray =  new JSONArray();

                    for (int i=0;i< push_messages_al.size();i++) {



                        JSONObject pushMessageJsonObject =  new JSONObject();

                        Charset charset = Charset.forName("ISO-8859-6");
                        CharsetDecoder decoder = charset.newDecoder();
                        ByteBuffer buf = ByteBuffer.wrap(push_messages_al.get(i).getBytes());
                        CharBuffer cbuf = decoder.decode(buf);
                        CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

                        pushMessageJsonObject.put("message", pushNotificationMessage);


                        pushMessageJsonObject.put("time", "30m ago");

                        pushMessageObjectJsonArray.put(pushMessageJsonObject);



                    }

                    finalJsonObject.put("list",pushMessageObjectJsonArray);

//                    Charset charset = Charset.forName("ISO-8859-6");
//                    CharsetDecoder decoder = charset.newDecoder();
//                    ByteBuffer buf = ByteBuffer.wrap(finalJsonObject.toString().getBytes());
//                    CharBuffer cbuf = decoder.decode(buf);
//                    CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

//                    Toast toast = Toast.makeText(LoginActivityFromSplashNewFlow.this, finalJsonObject.toString(), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();


                    Intent intent = new Intent(LoginActivityFromSplashNewFlow.this, ShowPushNotificationMessageDialogActivity.class);
                    intent.putExtra(ShowPushNotificationMessageDialogActivity.KEY_PUSH_NOTIFICATION_MESSAGES,finalJsonObject.toString());
                    startActivity(intent);



                } catch (Exception e) {

                    Log.e("Push Notifica Msg Ex:", "" + e.getMessage());

                    Toast toast = Toast.makeText(LoginActivityFromSplashNewFlow.this, "System Error! "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();



                }

*/


            }
        });
    }

    private void blinkNotificationCountView(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try{Thread.sleep(timeToBlink);}catch (Exception e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LayoutInflater mInflater = LayoutInflater.from(LoginActivityFromSplashNewFlow.this);
                        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_with_notifications_bell, null);
                         FrameLayout notification_count_frame_layout= (FrameLayout)mCustomView. findViewById(R.id.notification_count_frame_layout);

                        //Showing Notification count
                        TextView count_text_top = (TextView) mCustomView.findViewById(R.id.count_text_top);
                        String notification_count  = CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.NOTIFICATION_MSG_COUNT);
                        count_text_top.setText(notification_count);

                        if(notification_count_frame_layout.getVisibility() == View.VISIBLE){
                            notification_count_frame_layout.setVisibility(View.INVISIBLE);
                        }else{
                            notification_count_frame_layout.setVisibility(View.VISIBLE);
                        }
                        blinkNotificationCountView();
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("LoginInvoked");

        Log.e("Login Invoked", "Login Invoked");


//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
//                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }





//        //NFC
//        // Check to see that the Activity started due to an Android Beam
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
//
//            Toast.makeText(LoginActivityFromSplashNewFlow.this,"NFC Found",Toast.LENGTH_LONG).show();
//
//            processNFCData(getIntent());
//        }



    }

//    private void processNFCData( Intent inputIntent ) {
//
//        Log.i("LoginSplashNewFlow", "processNFCData");
//        Parcelable[] rawMessages =
//                inputIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//        if (rawMessages != null && rawMessages.length > 0) {
//
//            NdefMessage[] messages = new NdefMessage[rawMessages.length];
//
//            for (int i = 0; i < rawMessages.length; i++) {
//
//                messages[i] = (NdefMessage) rawMessages[i];
//
//            }
//
//            Log.i("MainMenu", "message size = " + messages.length);
//
//
//            // only one message sent during the beam
//            NdefMessage msg = (NdefMessage) rawMessages[0];
//            // record 0 contains the MIME type, record 1 is the AAR, if present
//            String base = new String(msg.getRecords()[0].getPayload());
////                String str = String.format(Locale.getDefault(), "Message entries=%d. Base message is %s", rawMessages.length, base);
//
//
//
//
//            String[] str_array = base.split("-");
//            String mobile_number = str_array[0];
//            String password = str_array[1];
//
//
////                        Toast.makeText(MainActivity.this,"NFC Message:  "+mobile_number+" "+password,Toast.LENGTH_LONG).show();
//
//            CustomerLoginRequest clr = new CustomerLoginRequest();
//            clr.setLogin_pin(password);
//            CustomSharedPreferences.saveStringData(getApplicationContext(), password, CustomSharedPreferences.SP_KEY.PIN);
//            String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
//
//            Log.e("deviceID","->"+deviceID);
//            Log.e("deviceID","->"+deviceID);
//
//
////            String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
//
//            String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
//
//            clr.setMobileNumber(mobile_number);
//            clr.setDeviceIdNumber(deviceID);
//            if (selectedLanguage.equals("en")) {
//                clr.setLanguage("english");
//            } else if (selectedLanguage.equals("ar")) {
//                clr.setLanguage("arabic");
//            } else {
//                clr.setLanguage("english");
//            }
//            CoreApplication application = (CoreApplication) getApplication();
//            String uiProcessorReference = application.addUserInterfaceProcessor(new CustomerAutoLoginProcessingFromNFC(clr, true, application,true));
//            ProgressDialogFrag progress = new ProgressDialogFrag();
//            Bundle bundle = new Bundle();
//            bundle.putString("uuid", uiProcessorReference);
//            progress.setCancelable(true);
//            progress.setArguments(bundle);
//            progress.show(getSupportFragmentManager(), "progress_dialog");
//
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        System.exit(0);
    }

    //finger print
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] state) {
        boolean isFingerprintPermissionGranted = state[0] == PackageManager.PERMISSION_GRANTED;

        final ImageView fingerprint_img = (ImageView) findViewById(R.id.fingerprint_img);

        LinearLayout login_finger_print_layout = (LinearLayout)findViewById(R.id.login_finger_print_layout);

         TextView     login_ok_text = (TextView)findViewById(R.id.login_ok_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (isFingerprintPermissionGranted) {
                try {
                    if (mFingerprintManager != null) {
                        if (!mFingerprintManager.isHardwareDetected()) {

//                            Toast.makeText(this, "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_LONG).show();

//                            Jan 20 Touch ID Crash
                            login_finger_print_layout.setVisibility(View.INVISIBLE);
                            login_ok_text.setVisibility(View.INVISIBLE);


                        } else if (!mFingerprintManager.hasEnrolledFingerprints()) {
                            // This happens when no fingerprints are registered.

                            //Sara
//                            Toast.makeText(this,
//                                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
//                                    Toast.LENGTH_LONG).show();

                        } else {
                            // Everything is ready for fingerprint authentication - create our key
                            createKey();
                        }
                    } else {
//                        Toast.makeText(this, "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_LONG).show();

//                        Jan 20 Touch ID Crash
                        login_finger_print_layout.setVisibility(View.INVISIBLE);
                        login_ok_text.setVisibility(View.INVISIBLE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        fingerprint_img.setEnabled(true);
        fingerprint_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerprint_img.performClick();
            }
        });
        fingerprint_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean isFingerprintAvailable = false;
                    if (!mFragment.isAdded()) {

                        //findViewById(R.id.confirmation_message).setVisibility(View.GONE);
                        //findViewById(R.id.encrypted_message).setVisibility(View.GONE);

                        boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                                LoginActivityFromSplashNewFlow.this, Manifest.permission.USE_FINGERPRINT)
                                == PackageManager.PERMISSION_GRANTED;
                        if (mFingerprintManager != null) {
                            isFingerprintAvailable = mFingerprintManager.isHardwareDetected()
                                    && mFingerprintManager.hasEnrolledFingerprints();
                        }

                        if (!isFingerprintPermissionGranted || !isFingerprintAvailable) {
                            // The user either rejected permission to read their fingerprint, we're on
                            // a device that doesn't support it, or the user doesn't have any
                            // fingerprints enrolled.
                            // Let them authenticate with a password
                            mFragment.setStage(
                                    FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash.Stage.PASSWORD);
                            mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);

                        } else if (initCipher()) {
                            // Set up the crypto object for later. The object will be authenticated by use
                            // of the fingerprint.

                            // Show the fingerprint dialog. The user has the option to use the fingerprint with
                            // crypto, or you can fall back to using a server-side verified password.
                            mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                            boolean useFingerprintPreference = mSharedPreferences
                                    .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                            true);
                            if (useFingerprintPreference) {
                                mFragment.setStage(
                                        FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash.Stage.FINGERPRINT);
                            } else {
                                mFragment.setStage(
                                        FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash.Stage.PASSWORD);
                            }
                            mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);

                        } else {
                            // This happens if the lock screen has been disabled or or a fingerprint got
                            // enrolled. Thus show the dialog to authenticate with their password first
                            // and ask the user if they want to authenticate with fingerprints in the
                            // future
                            mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                            mFragment.setStage(
                                    FingerprintAuthenticationDialogFragmentNewFlowLoginFromSplash.Stage.NEW_FINGERPRINT_ENROLLED);
                            mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                        }
                    }
                } catch (Exception e) {


                    Toast.makeText(LoginActivityFromSplashNewFlow.this, " Fingerprint Sensor Exc: "+e.getMessage(), Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher() {
        try {
            if (mKeyStore == null) {
                createKey();
            }
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);

            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (Exception e) {
            if (e instanceof KeyPermanentlyInvalidatedException)
                return false;
            else if (e instanceof KeyStoreException | e instanceof CertificateException | e instanceof UnrecoverableKeyException | e instanceof IOException | e instanceof NoSuchAlgorithmException | e instanceof InvalidKeyException)
                throw new RuntimeException("Failed to init Cipher", e);

        }
        /*catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }*/
        return false;

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);

            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refresh(LoginActivityFromSplashNewFlow loginActivity, String selectedLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = loginActivity.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, LoginActivityFromSplashNewFlow.class);
            startActivity(refresh);
            finish();
        } else {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = loginActivity.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, LoginActivityFromSplashNewFlow.class);
            startActivity(refresh);
            finish();
        }
    }


   /* @Override
    protected void onResume() {
        //  UserInfoResponse userInfoResponse = ((CoreApplication) getApplication()).getUserInfoResponse();
        CoreApplication application = (CoreApplication) getApplicationContext();

        //String error = userInfoResponse.getG_status_description();
        if (application.isUserLoggedIn()) {
            Toast toast = Toast.makeText(LoginActivity.this, "ssssssssssssssssssss", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();
            return;
        }

        super.onResume();
    }*/


    /*@Override
    protected void onStart() {
        // TODO Auto-generated method stub

        //Register BroadcastReceiver
        //to receive event from our service



        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SyncService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        //Start our own service
        Intent intent = new Intent(LoginActivity.this,
                com.bookeey.wallet.live.application.SyncService.class);

        startService(intent);

        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        unregisterReceiver(myReceiver);
        super.onStop();
    }*/

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    /*public void sendVersionNumber(){
        String regid = getRegistrationId(LoginActivity.this);
        Log.i(this.toString(), "registration id from shared pref : " + regid);
        if (regid.isEmpty()) // if blank, then app is not yet registered
        {
            registerInBackground();
        } else {
            CustomSharedPreferences.saveGCMRegId(LoginActivity.this, "" + regid, CustomSharedPreferences.SP_KEY.GCM_REG_ID);

//            String merchant_ref_id  = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.USERNAME);
            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            String imeinumber=  telephonyManager.getDeviceId();
            //Send Version Numer
            CoreApplication application = (CoreApplication) getApplication();
            String uiProcessorReference = application.addUserInterfaceProcessor(new SendingVersionNumberProcessing(imeinumber,regid,merchant_ref_id,true, application));
            ProgressDialogFrag progress = new ProgressDialogFrag();
            Bundle bundle = new Bundle();
            bundle.putString("uuid", uiProcessorReference);
            progress.setCancelable(true);
            progress.setArguments(bundle);
            progress.show(getFragmentManager(), "progress_dialog");
        }
    }*/
    private void registerInBackground() {
        new AsyncTask<String, Void, String>() {
            private String regId = "";

            @Override
            protected String doInBackground(String... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivityFromSplashNewFlow.this);
                    }
                    regId = gcm.register(SENDER_ID);
                    Log.d(TAG, "########################################");
                    Log.d(TAG, "Current Device's Registration ID is: " + msg);
                    Log.i(this.toString(), "regId = " + regId);
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // sendRegistrationIdToBackend();
                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.
                    // Persist the regID - no need to register again.
                    storeRegistrationId(LoginActivityFromSplashNewFlow.this, regId);
                    msg = regId;
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                // setting registration id in edit text.
                CustomSharedPreferences.saveGCMRegId(LoginActivityFromSplashNewFlow.this, msg, CustomSharedPreferences.SP_KEY.GCM_REG_ID);

                /*CustomSharedPreferences.saveGCMRegId(LoginActivity.this, msg, CustomSharedPreferences.SP_KEY.GCM_REG_ID);


                // String regid_reg_id = getRegistrationId(MainActivity.this);

//                String merchant_ref_id  = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.USERNAME);
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String imeinumber=  telephonyManager.getDeviceId();
                //Send Version Numer
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new SendingVersionNumberProcessing(imeinumber,msg,merchant_ref_id,true, application));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getFragmentManager(), "progress_dialog");*/
            }

        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(this.toString(), "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(this.toString(), "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(LoginActivityFromSplashNewFlow.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(this.toString(), "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(this.toString(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;


    }

    public void onPurchased(boolean withFingerprint, String password) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            //  tryEncrypt();
            CustomerLoginRequest clr = new CustomerLoginRequest();
            clr.setLogin_pin("");
            String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
            String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
            clr.setMobileNumber(mobile_number);
            clr.setDeviceIdNumber(deviceID);
            if (selectedLanguage.equals("en")) {
                clr.setLanguage("english");
            } else if (selectedLanguage.equals("ar")) {
                clr.setLanguage("arabic");
            } else {
                clr.setLanguage("english");
            }
            CoreApplication application = (CoreApplication) getApplication();
            String uiProcessorReference = application.addUserInterfaceProcessor(new FingerPrintLoginProcessing(clr, true, application));
            ProgressDialogFrag progress = new ProgressDialogFrag();
            Bundle bundle = new Bundle();
            bundle.putString("uuid", uiProcessorReference);
            progress.setCancelable(true);
            progress.setArguments(bundle);
            progress.show(getSupportFragmentManager(), "progress_dialog");
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            //showConfirmation(null);
            CustomerLoginRequest clr = new CustomerLoginRequest();
            clr.setLogin_pin(password);
            String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
            String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
            clr.setMobileNumber(mobile_number);
            clr.setDeviceIdNumber(deviceID);
            if (selectedLanguage.equals("en")) {
                clr.setLanguage("english");
            } else if (selectedLanguage.equals("ar")) {
                clr.setLanguage("arabic");
            } else {
                clr.setLanguage("english");
            }
            CoreApplication application = (CoreApplication) getApplication();
            String uiProcessorReference = application.addUserInterfaceProcessor(new CustomerLoginProcessing(clr, true, application));
            ProgressDialogFrag progress = new ProgressDialogFrag();
            Bundle bundle = new Bundle();
            bundle.putString("uuid", uiProcessorReference);
            progress.setCancelable(true);
            progress.setArguments(bundle);
            progress.show(getSupportFragmentManager(), "progress_dialog");
        }
    }


   /* private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            // int datapassed = arg1.getIntExtra("DATAPASSED", 0);

            Toast.makeText(LoginActivity.this,
                    "Triggered by Service!\n"
                            + "Data passed:",
                    Toast.LENGTH_LONG).show();

        }

    }*/
}