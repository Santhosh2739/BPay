package com.bookeey.wallet.live;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.PriceFormatter;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.generic.ViewProfileResponse;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.registration.CustomerRegistrationRequest;

/**
 * Created by 30099 on 1/21/2016.
 */
public class ViewProfileActivity extends GenericActivity {
    EditText view_profile_email_text;
    ViewProfileResponse viewProfileResponse = null;
    String edit_email;
    ProgressDialog progress = null;
    ImageView image_person;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_layout);
        showMenu(false);



        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


       /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("MANAGE PROFILE");*/

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
        mTitleTextView.setText(getResources().getString(R.string.manage_profile_title));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        viewProfileResponse = new Gson().fromJson(getIntent().getStringExtra("view_profile"), ViewProfileResponse.class);
        ((TextView) findViewById(R.id.view_profile_mobilenumber_text)).setText(viewProfileResponse.getMobileNumber());
        ((TextView) findViewById(R.id.view_profile_first_name_text)).setText(viewProfileResponse.getFirstName());
        ((TextView) findViewById(R.id.view_profile_last_name_text)).setText(viewProfileResponse.getLastName());
        //((TextView)findViewById(R.id.view_profile_civil_id_text)).setText(viewProfileResponse.getCivilId());
        ((TextView) findViewById(R.id.view_profile_email_text)).setText(viewProfileResponse.getEmailId());
        ((TextView) findViewById(R.id.view_profile_customer_id_text)).setText(viewProfileResponse.getCustomerId());
        String amount = viewProfileResponse.getWalletBalance();
        ((TextView) findViewById(R.id.view_profile_mwallet_balance_text)).setText(PriceFormatter.format(Double.parseDouble(amount), 3, 3));


        image_person = (ImageView) findViewById(R.id.image_person);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));

        }
        updateProfile(R.id.nameTextooredo, R.id.wallet_id, R.id.balance_id);

        view_profile_email_text = ((EditText) findViewById(R.id.view_profile_email_text));

        progress = new ProgressDialog(ViewProfileActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        ((Button) findViewById(R.id.view_profile_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewProfileActivity.this.finish();
            }
        });
        ((Button) findViewById(R.id.view_profile_save_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_email = view_profile_email_text.getText().toString().trim();
                if (edit_email.length() == 0) {
                    Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.Please_enter_mail_id), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                } else {
                    editProfile();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Manage profile page");



        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 28);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Manage profile page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 28 logged");
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

    private void editProfile() {

        CustomerRegistrationRequest crr = new CustomerRegistrationRequest();
        crr.setMobileNumber(viewProfileResponse.getMobileNumber());
        crr.setEmailID(edit_email);
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        crr.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        crr.setG_transType(TransType.PROFILE_UPDATE_REQUEST.name());

        String json = new Gson().toJson(crr);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PROFILE_UPDATE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.PROFILE_UPDATE_RESPONSE.name())) {
                        Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.Your_Profile_has_been_updated), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        ViewProfileActivity.this.finish();
                    } else if (null != response && response.getG_status() == -1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.PROFILE_UPDATE_RESPONSE.name())) {
                        Toast toast = Toast.makeText(ViewProfileActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();

                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(ViewProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        ViewProfileActivity.this.finish();
                    } else {
                        Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast toast = Toast.makeText(ViewProfileActivity.this, getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");


    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
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