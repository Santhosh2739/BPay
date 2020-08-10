package com.bookeey.wallet.live.recharge;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeInitiationResponsePojo;

/**
 * Created by 30099 on 4/29/2016.
 */
public class TopupFinalScreenActivity extends GenericActivity {
    InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo = null;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.international_topup_final_receipt_layout);


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
        mTitleTextView.setText("Int'l Top Up");*/

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
        mTitleTextView.setText(getResources().getString(R.string.intl_top_up_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        updateProfile(R.id.topup_final_nameTextooredo, R.id.topup_final_wallet_id, R.id.topup_final_balance_id);
        ImageView imageView = (ImageView) findViewById(R.id.topup_final_image_person);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }


        internationalRechargeInitiationResponsePojo = ((CoreApplication) getApplication()).getInternationalRechargeInitiationResponsePojo();
        if (internationalRechargeInitiationResponsePojo.getG_status() == 1) {
            ((ImageView) findViewById(R.id.topup_final_tickmark_img)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.topup_final_tickmark_img)).setImageDrawable(getResources().getDrawable(R.drawable.tickk));

        } else {
            ((ImageView) findViewById(R.id.topup_final_tickmark_img)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.topup_final_tickmark_img)).setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
        }
        if (internationalRechargeInitiationResponsePojo.getWalletbalanceafter() != null) {
            ((TextView) findViewById(R.id.topup_final_balance_id)).setText(PriceFormatter.format(internationalRechargeInitiationResponsePojo.getWalletbalanceafter(), 3, 3));
        }
        ((TextView) findViewById(R.id.topup_final_value_right0_tv)).setText(internationalRechargeInitiationResponsePojo.getRecipientMobile());
        ((TextView) findViewById(R.id.topup_final_value_status_tv)).setText(internationalRechargeInitiationResponsePojo.getStatus());
        if (internationalRechargeInitiationResponsePojo.getDenominationAmt() != null) {
            ((TextView) findViewById(R.id.topup_final_value_right1_tv)).setText(internationalRechargeInitiationResponsePojo.getCurrencyName() + " " + PriceFormatter.format(internationalRechargeInitiationResponsePojo.getDenominationAmt(), 2, 2));
        }
        if (internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD() != null) {
            ((TextView) findViewById(R.id.topup_final_value_right2_tv)).setText(PriceFormatter.format(internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD(), 3, 3));

//            double bpoints =  internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD()*5;

            BigDecimal bpoints =  BigDecimal.valueOf(internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD() ).multiply(new BigDecimal("5"));

            bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//            DecimalFormat df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
//            ((TextView) findViewById(R.id.topup_final_bpoints_right3_tv)).setText(""+df.format(bpoints));

            ((TextView) findViewById(R.id.topup_final_bpoints_right3_tv)).setText(""+String.valueOf(bpoints));


if(internationalRechargeInitiationResponsePojo.getTotalRedemptionPoints()!=null){
    ((TextView) findViewById(R.id.topup_final_bpoints_balance_value_tv)).setText(""+internationalRechargeInitiationResponsePojo.getTotalRedemptionPoints());

}else{
    ((TextView) findViewById(R.id.topup_final_bpoints_balance_value_tv)).setText("NA");

}

            CustomSharedPreferences.saveStringData(this.getApplicationContext(), String.valueOf(internationalRechargeInitiationResponsePojo.getTotalRedemptionPoints()), CustomSharedPreferences.SP_KEY.BPOINTS);



        }
        ((TextView) findViewById(R.id.topup_final_value_right3_tv)).setText(internationalRechargeInitiationResponsePojo.getCustomerID());
        ((TextView) findViewById(R.id.topup_final_value_right4_tv)).setText(internationalRechargeInitiationResponsePojo.getTransactionID());
        if (internationalRechargeInitiationResponsePojo.getWalletbalanceafter() != null) {
            ((TextView) findViewById(R.id.topup_final_value_balance_tv)).setText(PriceFormatter.format(internationalRechargeInitiationResponsePojo.getWalletbalanceafter(), 3, 3));
        } else {
            ((TextView) findViewById(R.id.topup_final_value_balance_tv)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.topup_final_balance_tv)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_final_balance_view)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_final__balance_horizontal_view)).setVisibility(View.GONE);
        }
        if (internationalRechargeInitiationResponsePojo.getServerTime() != 0l) {
            ((TextView) findViewById(R.id.topup_final_value_right5_tv)).setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(internationalRechargeInitiationResponsePojo.getServerTime())));
        }
        ((Button) findViewById(R.id.topup_final_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("International top up - confirm payment - page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 15);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"International top up - confirm payment - page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 15 logged");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
}
