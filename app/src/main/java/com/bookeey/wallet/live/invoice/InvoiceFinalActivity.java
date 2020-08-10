package com.bookeey.wallet.live.invoice;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsPojo;
import ycash.wallet.json.pojo.invoicePojo.InvoiceResponse;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by 10037 on 23-Sep-17.
 */

public class InvoiceFinalActivity extends GenericActivity implements YPCHeadlessCallback {
    TextView invoice_success_name_text, invoice_success_invno_text, invoice_success_amount_text, invoice_success_status_text, invoice_success_header_text, invoice_l2_balance_id,invoice_success_bpoints_earned,invoice_success_bpoints_balance;
    Button invoice_success_ok_btn;
    private String response_l2_str = null;
    ImageView invoice_l2_user_image, invoice_success_status_img;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_details_page2);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);



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
        mTitleTextView.setText(getResources().getString(R.string.invoice_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        invoice_success_name_text = (TextView) findViewById(R.id.invoice_success_name_text);
        invoice_success_invno_text = (TextView) findViewById(R.id.invoice_success_invno_text);
        invoice_success_amount_text = (TextView) findViewById(R.id.invoice_success_amount_text);
        invoice_success_status_text = (TextView) findViewById(R.id.invoice_success_status_text);
        invoice_success_header_text = (TextView) findViewById(R.id.invoice_success_header_text);
        invoice_success_ok_btn = (Button) findViewById(R.id.invoice_success_ok_btn);
        invoice_l2_user_image = (ImageView) findViewById(R.id.invoice_l2_user_image);
        invoice_success_status_img = (ImageView) findViewById(R.id.invoice_success_status_img);
        invoice_l2_balance_id = (TextView) findViewById(R.id.invoice_l2_balance_id);


        invoice_success_bpoints_earned = (TextView) findViewById(R.id.invoice_success_bpoints_earned);

        invoice_success_bpoints_balance = (TextView) findViewById(R.id.invoice_success_bpoints_balance);


        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            invoice_l2_user_image.setImageBitmap(stringToBitmap(image));
        }


        updateProfile(R.id.invoice_l2_nameTextooredo, R.id.invoice_l2_wallet_id, R.id.invoice_l2_balance_id);

        response_l2_str = getIntent().getStringExtra("INVOICE_RESPONSE_L2");
        if (!response_l2_str.isEmpty()) {
            InvoiceResponse response = new Gson().fromJson(response_l2_str, InvoiceResponse.class);
            invoice_success_name_text.setText(response.getMerchantName());
            invoice_success_invno_text.setText(response.getInvNo());
            invoice_success_amount_text.setText(String.valueOf(response.getAmount()));



            BigDecimal bpoints =  response.getAmount().multiply(new BigDecimal("5"));
//            DecimalFormat df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);


            bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

            invoice_success_bpoints_earned.setText(""+String.valueOf(bpoints));



            if(response.getTotalRedemptionPoints()!=null){
                invoice_success_bpoints_balance.setText(""+response.getTotalRedemptionPoints());

            }else{
                invoice_success_bpoints_balance.setText("NAS");
            }


            invoice_success_status_text.setText(response.getStatus());
            if (response.getStatus().equalsIgnoreCase("Success")) {
                invoice_success_status_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                invoice_success_header_text.setText("Invoice Successfull");
                CustomerLoginRequestReponse rep = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                if (response.getBalAfter() != null) {
                    invoice_l2_balance_id.setText("KWD " + response.getBalAfter());
                }
            } else if (response.getStatus().equalsIgnoreCase("Reject")) {
                invoice_success_status_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                invoice_success_header_text.setText("Invoice Rejected");
            } else if (response.getStatus().equalsIgnoreCase("onhold")) {
                invoice_success_status_img.setImageDrawable(getResources().getDrawable(R.drawable.invoice_hold));
                invoice_success_header_text.setText("Invoice onHold");
            }
        }

        invoice_success_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("voice", false);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Invoice transaction status page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 18);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Invoice transaction status page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 18 logged");

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
