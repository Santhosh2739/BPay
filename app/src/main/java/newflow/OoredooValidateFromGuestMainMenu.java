package newflow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.Registration_processing.CustomerMobileValidationProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.LocaleHelper;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;

public class OoredooValidateFromGuestMainMenu extends FragmentActivity implements YPCHeadlessCallback {
    String selectedLanguage = null;
    String mobileNumber = null;
    private Button mobile_number_validation_btn;

    //for language
    LinearLayout language_layout;
    ImageView coutry_flag_img;
    TextView language_text, goto_tour_text;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mobilenumber_validation);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        final EditText mobile_number_validation_edit = (EditText) findViewById(R.id.mobile_number_validation_edit);
        mobile_number_validation_btn = (Button) findViewById(R.id.mobile_number_validation_btn);
        language_layout = (LinearLayout) findViewById(R.id.language_layout);
        coutry_flag_img = (ImageView) findViewById(R.id.coutry_flag_img);
        language_text = (TextView) findViewById(R.id.language_text);
        goto_tour_text = (TextView) findViewById(R.id.goto_tour_text);
        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(OoredooValidateFromGuestMainMenu.this, selectedLanguage);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobileNumber = extras.getString("MobileNumber");
            mobile_number_validation_edit.setText(mobileNumber);
            // and get whatever type user account id is
        }

        mobile_number_validation_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = mobile_number_validation_edit.getText().toString().length();
                if (value == 8) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobile_number_validation_edit.getWindowToken(), 0);
                }
            }
        });

        mobile_number_validation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile_number_validation_edit.getText().toString().trim().length() == 0) {
                    Toast toast = Toast.makeText(OoredooValidateFromGuestMainMenu.this, getResources().getString(R.string.mob_validation_enter_mobile_no), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (mobile_number_validation_edit.getText().toString().trim().length() != 8) {
                    Toast toast = Toast.makeText(OoredooValidateFromGuestMainMenu.this, getResources().getString(R.string.mob_validation_enter_valid_mobile_no), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }

                String newMobi = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
                String mob = mobile_number_validation_edit.getText().toString().trim();
                if(!mob.equals(newMobi)) {
                    CustomSharedPreferences.saveBooleanData(getApplicationContext(), false, CustomSharedPreferences.SP_KEY.BIOMETRIC);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), null, CustomSharedPreferences.SP_KEY.PIN);
                }
                mobileNumberValidation(mobile_number_validation_edit.getText().toString().trim());
            }
        });
        goto_tour_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });
        //language selection
        if (language_text.getText().toString().equals("English")) {
            language_text.setText(getApplicationContext().getString(R.string.login_arabic));
            coutry_flag_img.setImageResource(R.drawable.kuwait);
        } else {
            language_text.setText(getApplicationContext().getString(R.string.login_english));
            coutry_flag_img.setImageResource(R.drawable.usa);
        }
        language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (language_text.getText().toString().equals("English")) {
                    mobileNumber = mobile_number_validation_edit.getText().toString();
                    language_text.setText(getApplicationContext().getString(R.string.login_arabic));
                    selectedLanguage = "en";
                    coutry_flag_img.setImageResource(R.drawable.kuwait);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(OoredooValidateFromGuestMainMenu.this, "en");
                    refresh(OoredooValidateFromGuestMainMenu.this, "en", mobileNumber);

                } else {
                    mobileNumber = mobile_number_validation_edit.getText().toString();
                    selectedLanguage = "ar";
                    language_text.setText(getApplicationContext().getString(R.string.login_english));
                    coutry_flag_img.setImageResource(R.drawable.usa);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(OoredooValidateFromGuestMainMenu.this, "ar");
                    refresh(OoredooValidateFromGuestMainMenu.this, "ar", mobileNumber);
                }
            }
        });

        findViewById(R.id.mobile_number_validation_help_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Help.class);
                startActivity(intent);
            }
        });
        /*if (mobile_number_validation_edit.getText().toString().length() != 0) {
            mobile_number_validation_edit.setError(null);
            return;
        }*/

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent in = new Intent(this, MainActivityNewFlow.class);
//        startActivity(in);
//        finish();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        //Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Mobile no entry page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 2);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Mobile no entry page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 2 logged");

    }

    private void refresh(OoredooValidateFromGuestMainMenu ooredooValidation, String selectedLanguage, String mobileNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = ooredooValidation.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, OoredooValidateFromGuestMainMenu.class);
            refresh.putExtra("MobileNumber", mobileNumber);
            startActivity(refresh);
            finish();
        } else {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = ooredooValidation.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, OoredooValidateFromGuestMainMenu.class);
            refresh.putExtra("MobileNumber", mobileNumber);
            startActivity(refresh);
            finish();
        }
    }

    void mobileNumberValidation(String mobile_number) {
        CustomerMobileNumberRequest cmnr = new CustomerMobileNumberRequest();
        cmnr.setCustomerMobileNumber(mobile_number);
        String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
        cmnr.setDeviceId(deviceID);
        cmnr.setDeviceType("Android-" + android.os.Build.VERSION.SDK_INT);

        Log.e("device id dddddddddddd", deviceID);
        CustomSharedPreferences.saveStringData(getApplicationContext(), mobile_number, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new CustomerMobileValidationProcessing(cmnr, application, false));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public void onProgressComplete() {
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }
}