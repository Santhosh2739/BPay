package com.bookeey.wallet.live.mobilebill;

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
import android.widget.TableRow;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2Response;

/**
 * Created by 10037 on 6/17/2017.
 */

public class MobileBill_Final_Screen extends GenericActivity implements YPCHeadlessCallback {
    TextView mobilebill_mobilenumber_value_txt,
            mobilebill_status_value_txt,
            mobilebill_transferamount_value_txt,
            mobilebill_balanceafter_value_txt,
            mobilebill_customerid_value_txt,
            mobilebill_txnid_value_txt,
            mobilebill_txndate_value_txt,
            mobilebill_mobilebill_paymentreft_value_txt,mobilebill_bpoints_earned_value_txt,mobilebill_bpoints_balance_value_txt;
    Button mobilebill_close_btn;
    DomesticL2Response domesticL2Response = null;
    ImageView mobilebill_tickmark_img;
    TableRow mobile_leg2_payment_ref_tablerow;
    View mobile_payment_ref_leg2_view;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilebill_final_screen);

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
        mTitleTextView.setText("MOBILE BILL");*/

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
        mTitleTextView.setText(getResources().getString(R.string.mobile_bill_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        mobilebill_mobilenumber_value_txt = (TextView) findViewById(R.id.mobilebill_mobilenumber_value_txt);
        mobilebill_status_value_txt = (TextView) findViewById(R.id.mobilebill_status_value_txt);
        mobilebill_transferamount_value_txt = (TextView) findViewById(R.id.mobilebill_transferamount_value_txt);
        mobilebill_balanceafter_value_txt = (TextView) findViewById(R.id.mobilebill_balanceafter_value_txt);
        mobilebill_customerid_value_txt = (TextView) findViewById(R.id.mobilebill_customerid_value_txt);
        mobilebill_txnid_value_txt = (TextView) findViewById(R.id.mobilebill_txnid_value_txt);
        mobilebill_txndate_value_txt = (TextView) findViewById(R.id.mobilebill_txndate_value_txt);
        mobilebill_mobilebill_paymentreft_value_txt = (TextView) findViewById(R.id.mobilebill_mobilebill_paymentreft_value_txt);
        mobile_leg2_payment_ref_tablerow = (TableRow) findViewById(R.id.mobile_leg2_payment_ref_tablerow);
        mobile_payment_ref_leg2_view = (View) findViewById(R.id.mobile_payment_ref_leg2_view);
        mobile_payment_ref_leg2_view.setVisibility(View.GONE);
        mobile_leg2_payment_ref_tablerow.setVisibility(View.GONE);
        mobilebill_close_btn = (Button) findViewById(R.id.mobilebill_close_btn);
        mobilebill_tickmark_img = (ImageView) findViewById(R.id.mobilebill_tickmark_img);

        mobilebill_bpoints_earned_value_txt = (TextView)findViewById(R.id.mobilebill_bpoints_earned_value_txt);
        mobilebill_bpoints_balance_value_txt = (TextView)findViewById(R.id.mobilebill_bpoints_balance_value_txt);


        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.mobilebill_l2_nameTextooredo, R.id.mobilebill_l2_wallet_id, R.id.mobilebill_l2_balance_id);

        domesticL2Response = new Gson().fromJson(getIntent().getStringExtra("DOMESTIC_L2_RESPONSE"), DomesticL2Response.class);
        mobilebill_mobilenumber_value_txt.setText(domesticL2Response.getRecieverMobileNumber());
        mobilebill_status_value_txt.setText("--SUCCESS--");
        mobilebill_transferamount_value_txt.setText(domesticL2Response.getTransferAmount());
        mobilebill_balanceafter_value_txt.setText(domesticL2Response.getBalanceAfter());
        mobilebill_customerid_value_txt.setText(domesticL2Response.getCustomerId());
        mobilebill_txnid_value_txt.setText(domesticL2Response.getTransactionId());
        mobilebill_txndate_value_txt.setText(domesticL2Response.getTxnDate());
        //mobilebill_mobilebill_paymentreft_value_txt.setText(domesticL2Response.getPaymentRef());



//        double bpoints =  Double.parseDouble(domesticL2Response.getTransferAmount())*5;
//        DecimalFormat df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);

        BigDecimal bpoints =  new BigDecimal(domesticL2Response.getTransferAmount()).multiply(new BigDecimal("5"));

        bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

        mobilebill_bpoints_earned_value_txt.setText(""+String.valueOf(bpoints));


        mobilebill_bpoints_balance_value_txt.setText(""+domesticL2Response.getTotalRedemptionPoints());

        if (domesticL2Response.getBalanceAfter() != null) {
            double amount_after = Double.parseDouble(domesticL2Response.getBalanceAfter());
            ((TextView) findViewById(R.id.mobilebill_l2_balance_id)).setText("KWD " + PriceFormatter.format(amount_after, 3, 3));
        }
        mobilebill_close_btn.setOnClickListener(new View.OnClickListener() {
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
        logger.logEvent("Mobile Bill - confirm payment - page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 13);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Mobile Bill - confirm payment - page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 13 logged");

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
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }
}
