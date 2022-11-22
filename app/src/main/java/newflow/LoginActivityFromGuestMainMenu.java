package newflow;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.changes.MobChangeMobileNumber;
import com.bookeey.wallet.live.login.ForgotPassword;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import java.util.Locale;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.Login_processing.CustomerLoginProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.LocaleHelper;
import ycash.wallet.json.pojo.activationresponse.ActivationResponsePojo;
import ycash.wallet.json.pojo.login.CustomerLoginRequest;

public class LoginActivityFromGuestMainMenu extends FragmentActivity implements YPCHeadlessCallback {
    TextView forgotpassword_text, forgotpassword_help_text, change_mobile_no_tv;
    ImageView imageView;
    String selectedLanguage = null;
    LinearLayout language_layout, login_finger_print_layout;
    ImageView coutry_flag_img;
    TextView language_text, fingerprint_text, login_ok_text, goto_tour_text;

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
            LocaleHelper.setLocale(LoginActivityFromGuestMainMenu.this, selectedLanguage);
        }

        login_ok_text.setVisibility(View.INVISIBLE);
        login_finger_print_layout.setVisibility(View.INVISIBLE);
        goto_tour_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.map_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.login_title));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.login_version_name)).setText("Version is : " + BuildConfig.VERSION_NAME);
        forgotpassword_text = (TextView) findViewById(R.id.forgotpassword_text);
        forgotpassword_help_text = (TextView) findViewById(R.id.forgotpassword_help_text);
        change_mobile_no_tv = (TextView) findViewById(R.id.change_mobile_no_tv);

        final TextView login_user_id = (TextView) findViewById(R.id.login_user_id);
        final EditText login_password = (EditText) findViewById(R.id.login_password);

//        login_password.setText("1234");


        imageView = (ImageView) findViewById(R.id.ooredoo_header_logo);

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
                    LocaleHelper.setLocale(LoginActivityFromGuestMainMenu.this, "en");
                    refresh(LoginActivityFromGuestMainMenu.this, "en");

                } else {
                    selectedLanguage = "ar";
                    language_text.setText(getResources().getString(R.string.login_english));
                    coutry_flag_img.setImageResource(R.drawable.usa);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(LoginActivityFromGuestMainMenu.this, "ar");
                    refresh(LoginActivityFromGuestMainMenu.this, "ar");
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

        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        String name = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.NAME);
        login_user_id.setText("Hi " + name + "!");
        login_user_id.setEnabled(false);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String userId = login_user_id.getText().toString();
                String password = login_password.getText().toString();
                if (password.length() == 0) {
                    Toast toast = Toast.makeText(LoginActivityFromGuestMainMenu.this, getResources().getString(R.string.login_password_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                CustomerLoginRequest clr = new CustomerLoginRequest();
                clr.setLogin_pin(password);
                //CustomSharedPreferences.saveStringData(getApplicationContext(), password, CustomSharedPreferences.SP_KEY.PIN);
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();

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
                LoginActivityFromGuestMainMenu.this.finish();
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
                Intent intent = new Intent(getBaseContext(), MobChangeMobileNumber.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("LoginInvoked");

        Log.e("Login Invoked", "Login Invoked");
    }

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

    private void refresh(LoginActivityFromGuestMainMenu loginActivity, String selectedLanguage) {
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
}