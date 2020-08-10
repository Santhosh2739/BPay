package com.bookeey.wallet.live.prepaidcard;

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
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.prepaidcard.VocherL2Processing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.utils.PriceFormatter;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2RequestPojo;
import ycash.wallet.json.pojo.virtualprepaidcards.RequestCardResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.TopupValidationResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.VocherL2Response;

/**
 * Created by 10037 on 7/12/2017.
 */

public class VirtualPrepaid_final_Activity extends GenericActivity {

    ImageView virtualprepaid_final_tickmark_img;

    TextView virtualprepaid_final_opname_value_txt,
            virtualprepaid_final_status_value_txt,
            virtualprepaid_final_serialno_value_txt,
            virtualprepaid_final_recode_value_txt,
            virtualprepaid_final_txn_amount_value_txt,
            virtualprepaid_final_txn_ref_value_txt,
            virtualprepaid_final_txndate_value_txt, vp_warning_text,virtualprepaid_final_bpoints_earned_value_txt,virtualprepaid_final_bpoints_balance_value_txt;

    Button virtualprepaid_final_confirm_btn;
    String response_str = null;
    VocherL2Response vocherL2Response = null;
    RequestCardResponse requestCardResponse = null;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtualprepaid_final_activity);

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
        mTitleTextView.setText("VIRTUAL PREPAID CARDS");*/

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
        mTitleTextView.setText(getResources().getString(R.string.virtual_prepaid_title));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        response_str = getIntent().getStringExtra("CARD_FINAL_RESPONSE");

        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.virtualprepaid_final_nameTextooredo, R.id.virtualprepaid_final_wallet_id, R.id.virtualprepaid_final_balance_id);

        vp_warning_text = (TextView) findViewById(R.id.vp_warning_text);
        virtualprepaid_final_opname_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_opname_value_txt);
        virtualprepaid_final_status_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_status_value_txt);
        virtualprepaid_final_serialno_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_serialno_value_txt);
        virtualprepaid_final_recode_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_recode_value_txt);
        virtualprepaid_final_txn_amount_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_txn_amount_value_txt);
        virtualprepaid_final_txn_ref_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_txn_ref_value_txt);
        virtualprepaid_final_txndate_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_txndate_value_txt);
        virtualprepaid_final_tickmark_img = (ImageView) findViewById(R.id.virtualprepaid_final_tickmark_img);


        virtualprepaid_final_bpoints_earned_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_bpoints_earned_value_txt);
        virtualprepaid_final_bpoints_balance_value_txt = (TextView) findViewById(R.id.virtualprepaid_final_bpoints_balance_value_txt);

        requestCardResponse = new Gson().fromJson(response_str, RequestCardResponse.class);
        vocherL2Response = requestCardResponse.getVocherList();
        if (vocherL2Response.getOperatorName() != null && !vocherL2Response.getOperatorName().isEmpty())
            virtualprepaid_final_opname_value_txt.setText(vocherL2Response.getOperatorName());

        String opeator_Name = vocherL2Response.getOperatorName();
        if (opeator_Name != null && !opeator_Name.isEmpty())
            if (opeator_Name.equalsIgnoreCase("Zain")) {
                vp_warning_text.setVisibility(View.VISIBLE);
                vp_warning_text.setText("To recharge dial :  *141*RechargeCode#");
            } else if (opeator_Name.equalsIgnoreCase("Ooredoo")) {
                vp_warning_text.setVisibility(View.VISIBLE);
                vp_warning_text.setText("To recharge dial : *111*RechargeCode#");
            } else if (opeator_Name.equalsIgnoreCase("Viva")) {
                vp_warning_text.setVisibility(View.VISIBLE);
                vp_warning_text.setText("To recharge dial : *500*RechargeCode#");
            } else {
                vp_warning_text.setVisibility(View.GONE);
                // warning_text.setVisibility(View.GONE);
            }
        virtualprepaid_final_status_value_txt.setText("--SUCCESS--");
        virtualprepaid_final_serialno_value_txt.setText(vocherL2Response.getSerialNo());



        String recharge_code  = vocherL2Response.getRechargeCode().replaceAll(System.getProperty("line.separator"), "");

        virtualprepaid_final_recode_value_txt.setText(recharge_code);



        virtualprepaid_final_txn_amount_value_txt.setText(requestCardResponse.getPrice());


//        double bpoints =  Double.parseDouble(requestCardResponse.getPrice())*5;
        BigDecimal bpoints =  new BigDecimal(requestCardResponse.getPrice() ).multiply(new BigDecimal("5"));
//        DecimalFormat df = new DecimalFormat("#.###");
//        df = new DecimalFormat("#.###");

        bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

        virtualprepaid_final_bpoints_earned_value_txt.setText(""+String.valueOf(bpoints));



        if(requestCardResponse.getTotalRedemptionPoints()!=null) {

            virtualprepaid_final_bpoints_balance_value_txt.setText("" + requestCardResponse.getTotalRedemptionPoints());
        }else{
            virtualprepaid_final_bpoints_balance_value_txt.setText("NA");
        }


        virtualprepaid_final_txn_ref_value_txt.setText(vocherL2Response.getTxnRefNumber());
        virtualprepaid_final_txndate_value_txt.setText(vocherL2Response.getDateTime());
        if (requestCardResponse.getBalAfter() != null) {
            double amount_after = Double.parseDouble(requestCardResponse.getBalAfter());
            ((TextView) findViewById(R.id.virtualprepaid_final_balance_id)).setText("KWD " + PriceFormatter.format(amount_after, 3, 3));
        }

        virtualprepaid_final_confirm_btn = (Button) findViewById(R.id.virtualprepaid_final_confirm_btn);
        virtualprepaid_final_confirm_btn.setOnClickListener(new View.OnClickListener() {
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
        logger.logEvent("Prepaid card - confirm payment - page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 11);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Prepaid card - confirm payment - page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 11 logged");

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

}
