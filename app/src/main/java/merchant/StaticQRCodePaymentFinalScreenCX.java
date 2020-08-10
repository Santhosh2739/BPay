package merchant;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
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

import com.bookeey.wallet.live.CheckForUpdatesActivity;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Date;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.LocaleHelper;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantCommitRequestResponse;
import ycash.wallet.json.pojo.sendmoney.PeertoPeerConformationResponse;

public class StaticQRCodePaymentFinalScreenCX extends GenericActivity implements YPCHeadlessCallback, View.OnClickListener {


    private FirebaseAnalytics firebaseAnalytics;

    private String response = null;
    private PayToMerchantCommitRequestResponse response_obj = null;

    Button payment_confirm_print_btn;

    TextView static_qr_p2m_merchant_name,static_qr_p2m_txn_id,static_qr_p2m_branch,static_qr_p2m_offer_desc,
            static_qr_p2m_amount_due,static_qr_p2m_discount_amount,static_qr_p2m_amount_paid,static_qr_p2m_after_balance,static_qr_p2m_bpoints_earned,
            static_qr_p2m_bpoints_balance,static_qr_p2m_status,static_qr_p2m_txn_time,static_qr_p2m_amount_paid_header;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestHeaderInformationUpdate();
        setContentView(R.layout.static_qr_p2m_final);


        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(StaticQRCodePaymentFinalScreenCX.this, selectedLanguage);
        }


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

       /* View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("SEND MONEY");*/

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
        mTitleTextView.setText(getString(R.string.paid_to_merchant));


        static_qr_p2m_merchant_name = (TextView) findViewById(R.id.static_qr_p2m_merchant_name);
        static_qr_p2m_txn_id = (TextView) findViewById(R.id.static_qr_p2m_txn_id);

        static_qr_p2m_branch = (TextView) findViewById(R.id.static_qr_p2m_branch);


        static_qr_p2m_offer_desc = (TextView) findViewById(R.id.static_qr_p2m_offer_desc);

        static_qr_p2m_amount_due = (TextView) findViewById(R.id.static_qr_p2m_amount_due);

        static_qr_p2m_discount_amount = (TextView) findViewById(R.id.static_qr_p2m_discount_amount);

        static_qr_p2m_amount_paid = (TextView) findViewById(R.id.static_qr_p2m_amount_paid);
        static_qr_p2m_after_balance = (TextView) findViewById(R.id.static_qr_p2m_after_balance);
        static_qr_p2m_bpoints_earned = (TextView) findViewById(R.id.static_qr_p2m_bpoints_earned);
        static_qr_p2m_bpoints_balance = (TextView) findViewById(R.id.static_qr_p2m_bpoints_balance);

        static_qr_p2m_status = (TextView) findViewById(R.id.static_qr_p2m_status);
        static_qr_p2m_txn_time = (TextView) findViewById(R.id.static_qr_p2m_txn_time);
        static_qr_p2m_amount_paid_header = (TextView) findViewById(R.id.static_qr_p2m_amount_paid_header);


        //print
        payment_confirm_print_btn = (Button) findViewById(R.id.payment_confirm_print_btn);



        response = getIntent().getExtras().getString("response");

//         P2M Response: {"transactionId":"P2M19001144","balance":50.75,"processingfee":0.0,"total":1.0,"serverTime":1575975155585,
//                "serverTimeL2":1575975155585,"customerId":"001369","txnAmount":1.0,"customerMobileNumber":"66662222","merchant_balance":0.0,
//                "totalAmountDebittedFromCust":0.0,"totalAmountCreditedToMerchant":0.0,"discountPrice":0.0,"totalPrice":1.0,"branch":"salmiya",
//                "totalRedemptionPoints":2526.750,"redemptionPoints":5.000,"merchantName":"Mahalgram","g_status":1,"g_status_description":"Transaction success",
//                "g_errorDescription":"","g_response_trans_type":"SCAN_STATIC_QR_CODE_RESPONSE"}



        Log.e("P2M Response: ","P2M Response: "+response);

        response_obj = new Gson().fromJson(response, PayToMerchantCommitRequestResponse.class);


        static_qr_p2m_merchant_name.setText(""+response_obj.getMerchantName());


        if(response_obj.getG_status_description().contains("success")){

            static_qr_p2m_status.setText("" + getString(R.string.transaction_success));



        }else{
            static_qr_p2m_status.setText("" + response_obj.getG_status_description());
        }


        if (response_obj.getTotalPrice() != 0) {

            static_qr_p2m_amount_due.setText("" + PriceFormatter.format(response_obj.getTotalPrice(), 3, 3) + " KWD");
        }


        View horizontal_discount_amnt_line = (View)findViewById(R.id.horizontal_discount_amnt_line);
        LinearLayout     discount_amnt_linear = (LinearLayout)findViewById(R.id.discount_amnt_linear);



//Discount Amount
        if (response_obj.getDiscountPrice() != 0) {

            static_qr_p2m_discount_amount.setText("" + PriceFormatter.format(response_obj.getDiscountPrice(), 3, 3) + " KWD");
            static_qr_p2m_amount_paid_header.setText("Amount Paid.");

        }else{

            horizontal_discount_amnt_line.setVisibility(View.GONE);
            discount_amnt_linear.setVisibility(View.GONE);
            static_qr_p2m_amount_paid_header.setText(getString(R.string.payment_amount));


        }
        static_qr_p2m_amount_paid.setText(""+"" + PriceFormatter.format(response_obj.getTotal(), 3, 3) + " KWD");

        //Offer Description
        View horizontal_offer_des = (View)findViewById(R.id.horizontal_offer_des);
        LinearLayout     offer_desc_linear = (LinearLayout)findViewById(R.id.offer_desc_linear);



        if (response_obj.getOfferDescription() != null) {

            static_qr_p2m_offer_desc.setText("" + response_obj.getOfferDescription());

        }else{

            horizontal_offer_des.setVisibility(View.GONE);
            offer_desc_linear.setVisibility(View.GONE);


        }




        static_qr_p2m_after_balance.setText("" + PriceFormatter.format(response_obj.getBalance(), 3, 3) + " KWD");


        static_qr_p2m_bpoints_earned.setText("" + String.valueOf(response_obj.getRedemptionPoints()));


        static_qr_p2m_bpoints_balance.setText(""+ String.valueOf(response_obj.getTotalRedemptionPoints()));



        static_qr_p2m_txn_id.setText("" + response_obj.getTransactionId());

        static_qr_p2m_branch.setText(""+response_obj.getBranch());


        String time_zone_str = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime();


        static_qr_p2m_txn_time.setText("" + TimeUtils.getDisplayableDateWithSeconds(time_zone_str, new Date(response_obj.getServerTime())));

        ImageView static_qr_status = (ImageView)findViewById(R.id.static_qr_status) ;

        switch (response_obj.getG_status()) {
            case 1:
                static_qr_status.setBackgroundResource(R.drawable.success);
                break;
            case -1:
                static_qr_status.setBackgroundResource(R.drawable.red_cross);
                break;
        }

        Button close_button = (Button)findViewById(R.id.close_button);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });



        ImageView merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });





    }


    @Override
    public void onResume() {
        super.onResume();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("P2M Static QR - confirm transaction page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 9);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "P2M Static QR - confirm transaction page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 9 logged");

    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onProgressComplete() {

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("response", response);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }
}

