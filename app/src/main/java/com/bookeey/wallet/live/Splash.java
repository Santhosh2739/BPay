package com.bookeey.wallet.live;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import com.bookeey.wallet.live.application.CoreApplication;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.LocaleHelper;
import newflow_processing.DeviceIDSplashCheckProcessingNewFlow;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;
public class Splash extends FragmentActivity implements YPCHeadlessCallback {
    public static final long DISCONNECT_TIMEOUT = 60 * 60 * 1000;
    public static final String KEY_SHOW_PUSH_NOTIFICATION_MESSAGE = "KEY_SHOW_PUSH_NOTIFICATION_MESSAGE";
    private static final String LAUNCH_FROM_URL = "com.bookeey.wallet.live.launchfrombrowser";
    String selectedLanguage = null;
//    public static final long DISCONNECT_TIMEOUT = 5*1000;
    //for FCM push notifications
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAnalytics firebaseAnalytics;
    private Tracker mTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookeey_splash_screen_new);
        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
//        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
//            LocaleHelper.setLocale(Splash.this, selectedLanguage);
//        }
        if (selectedLanguage != null && selectedLanguage.length() == 0) {
            CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE);
            LocaleHelper.setLocale(Splash.this, "en");
        } else {
            //Setting language
            selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
            if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
                LocaleHelper.setLocale(Splash.this, selectedLanguage);
            }
        }
        //CustomSharedPreferences.saveIntData(getApplicationContext(),-1, CustomSharedPreferences.SP_KEY.FIRST_LOGIN);
        int first_login = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.FIRST_LOGIN);
        if(first_login == -1)
            CustomSharedPreferences.saveIntData(getApplicationContext(), 1 ,CustomSharedPreferences.SP_KEY.FIRST_LOGIN);

        //for FCM push notifications
        mAuth = FirebaseAuth.getInstance();
        //  createUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TEST", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TEST", "onAuthStateChanged:signed_out");
                }
            }
        };
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.bookeey.wallet.live",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        getActionBar().hide();
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);*/
//        if(LAUNCH_FROM_URL!=null){
//
//            Intent intent = getIntent();
//            if (intent != null && intent.getAction().equals(LAUNCH_FROM_URL)) {
//                Bundle bundle = intent.getExtras();
//                if (bundle != null) {
//                    String msgFromBrowserUrl = bundle.getString("msg_from_browser");
//                    //Toast.makeText(getApplicationContext(),msgFromBrowserUrl,Toast.LENGTH_LONG).show();
//                }
//            } else {
//                //Toast.makeText(getApplicationContext(),"Normal application launch",Toast.LENGTH_LONG).show();
//            }
//
//        }
        //convert intent to URI android
        Intent test = new Intent("com.bookeey.wallet.live.launchfrombrowser");
        test.addCategory(Intent.CATEGORY_DEFAULT);
        test.addCategory(Intent.CATEGORY_BROWSABLE);
        Bundle bundle = new Bundle();
        bundle.putString("msg_from_browser", "Launched from Browser");
        test.putExtras(bundle);
        Log.d("AndroidSRC", test.toUri(Intent.URI_INTENT_SCHEME));

        /*Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            Intent i1 = new Intent(Intent.ACTION_VIEW);
            i1.setData(data);

            // Always use string resources for UI text. This says something like "Share this photo with"
            String title = "Please choose app";
            // Create and start the chooser
            Toast.makeText(getApplicationContext(),"aaaa",Toast.LENGTH_LONG).show();
            Intent chooser = Intent.createChooser(i1, title);
            startActivity(chooser);
        }*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*int app_status = CustomSharedPreferences.getIntData(getApplicationContext(), CustomSharedPreferences.SP_KEY.APP_STATUS);
                if (app_status == 4 || app_status == 3) {
                    CoreApplication application = (CoreApplication) getApplication();
                    if (application.isUsferLoggedIn()) {
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(Splash.this, LoginActivity.class));
                    }
                    finish();
                } else {
                    Intent in = new Intent(getBaseContext(), OoredooValidation.class);
                    startActivity(in);
                    finish();
                }*/
                //OldFlow
//                Intent intent = new Intent(getBaseContext(), GreetingsActivity.class);
//                startActivity(intent);
//                finish();
                //NewFlowJuly23
//                Intent intent = new Intent(getBaseContext(), MainActivityNewFlow.class);
//                startActivity(intent);
//                finish();
                //For Test Jan 06
//                Intent intent = new Intent(getBaseContext(), CreditCardFormActivity.class);
//                startActivity(intent);
//                finish();
                String gcm_id = CustomSharedPreferences.getGCMRegId(getApplicationContext(), CustomSharedPreferences.SP_KEY.GCM_REG_ID);
                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);
                //CustomSharedPreferences.saveStringData(getApplicationContext(), "", CustomSharedPreferences.SP_KEY.PIN);
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, false, false, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");
//                GetPushNotificationMessageRequest pushNotificationMessageRequest = new GetPushNotificationMessageRequest();
//
//                CoreApplication application = (CoreApplication) getApplication();
//                String uiProcessorReference = application.addUserInterfaceProcessor(new GetPushNotificationMessageProcessing(pushNotificationMessageRequest, application, true));
//                ProgressDialogFrag progress = new ProgressDialogFrag();
//                Bundle bundle = new Bundle();
//                bundle.putString("uuid", uiProcessorReference);
//                progress.setCancelable(true);
//                progress.setArguments(bundle);
//                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        }, 500);
        // Obtain the shared Tracker instance.
        CoreApplication application = (CoreApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("App launch splash page");
        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 1);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "App launch splash page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase", " Event 1 logged");
        //Google Analytics
        Log.i("Splash GA", "App launch splash page");
        mTracker.setScreenName("App launch splash page");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App launch splash page")
                .build());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }
}