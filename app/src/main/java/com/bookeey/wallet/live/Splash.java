package com.bookeey.wallet.live;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.fragment.app.FragmentActivity;

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
        checkBiometric();
        boolean biometric_device = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
        boolean asked_biometric = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.ASKED_BIOMETRIC);
        if(biometric_device && !asked_biometric) {
            CustomSharedPreferences.saveBooleanData(getApplicationContext(), true, CustomSharedPreferences.SP_KEY.SHOW_ENABLE_BIOMETRIC);
            CustomSharedPreferences.saveStringData(getApplicationContext(), null, CustomSharedPreferences.SP_KEY.PIN);
            CustomSharedPreferences.saveBooleanData(getBaseContext(), false, CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
        }

        Log.e("biometric_device", ":"+biometric_device);
        Log.e("asked_biometric", ":"+asked_biometric);
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

        //convert intent to URI android
        Intent test = new Intent("com.bookeey.wallet.live.launchfrombrowser");
        test.addCategory(Intent.CATEGORY_DEFAULT);
        test.addCategory(Intent.CATEGORY_BROWSABLE);
        Bundle bundle = new Bundle();
        bundle.putString("msg_from_browser", "Launched from Browser");
        test.putExtras(bundle);
        Log.d("AndroidSRC", test.toUri(Intent.URI_INTENT_SCHEME));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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
            }
        }, 500);
        // Obtain the shared Tracker instance.
        CoreApplication application = (CoreApplication) getApplication();
        mTracker = application.getDefaultTracker();
    }

    public void checkBiometric(){
        CustomSharedPreferences.saveBooleanData(getBaseContext(), false, CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
        BiometricManager biometricManager = BiometricManager.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    CustomSharedPreferences.saveBooleanData(getBaseContext(), true,CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.e("MY_APP_TAG", "No biometric features available on this device.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    // Prompts the user to create credentials that your app accepts.
                    Log.e("MY_APP_TAG", "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint");
                    Toast.makeText(getApplication(), "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint", Toast.LENGTH_LONG).show();
                    /*final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                    startActivityForResult(enrollIntent, REQUEST_CODE);*/
                    break;
            }
        }
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