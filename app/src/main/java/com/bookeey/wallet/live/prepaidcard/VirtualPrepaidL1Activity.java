package com.bookeey.wallet.live.prepaidcard;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
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
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.prepaidcard.VocherL2Processing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2RequestPojo;
import ycash.wallet.json.pojo.virtualprepaidcards.TopupValidationResponse;

/**
 * Created by 10037 on 7/12/2017.
 */

public class VirtualPrepaidL1Activity extends GenericActivity {

    TextView virtualprepaid_l2_opname_value_txt,
            virtualprepaid_l2_status_value_txt,
            virtualprepaid_l2_transferamount_value_txt,
            virtualprepaid_l2_balanceafter_value_txt,
            virtualprepaid_l2_customerid_value_txt,
            virtualprepaid_l2_txnid_value_txt,
            virtualprepaid_l2_txndate_value_txt;
    Button virtualprepaid_l2_confirm_btn;
    String response_str = null;
    TopupValidationResponse topupValidationResponse = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtualprepaid_leg1_activity);

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
                onBackPressed();
            }
        });


        response_str = getIntent().getStringExtra("CARD_L2_RESPONSE");
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();

        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.virtualprepaid_l2_nameTextooredo, R.id.virtualprepaid_l2_wallet_id, R.id.virtualprepaid_l2_balance_id);

        virtualprepaid_l2_opname_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_opname_value_txt);
        virtualprepaid_l2_status_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_status_value_txt);
        virtualprepaid_l2_transferamount_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_transferamount_value_txt);
        virtualprepaid_l2_balanceafter_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_balanceafter_value_txt);
        virtualprepaid_l2_customerid_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_customerid_value_txt);
        virtualprepaid_l2_txnid_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_txnid_value_txt);
        virtualprepaid_l2_txndate_value_txt = (TextView) findViewById(R.id.virtualprepaid_l2_txndate_value_txt);


        topupValidationResponse = new Gson().fromJson(response_str, TopupValidationResponse.class);

        if (topupValidationResponse.getOperatorName() != null && !topupValidationResponse.getOperatorName().isEmpty()){
            virtualprepaid_l2_opname_value_txt.setText(topupValidationResponse.getOperatorName());
        }
        virtualprepaid_l2_status_value_txt.setText("--PENDING--");
        virtualprepaid_l2_transferamount_value_txt.setText(topupValidationResponse.getTransferAmount());
        virtualprepaid_l2_balanceafter_value_txt.setText(topupValidationResponse.getBalanceAfter());
        virtualprepaid_l2_customerid_value_txt.setText(topupValidationResponse.getCustomerId());
        virtualprepaid_l2_txnid_value_txt.setText(topupValidationResponse.getTransactionId());
        virtualprepaid_l2_txndate_value_txt.setText(topupValidationResponse.getTxnDate());

        virtualprepaid_l2_confirm_btn = (Button) findViewById(R.id.virtualprepaid_l2_confirm_btn);
        virtualprepaid_l2_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmL2Request();
            }
        });

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

    private void confirmL2Request() {
        DomesticL2RequestPojo domesticL2RequestPojo = new DomesticL2RequestPojo();
        domesticL2RequestPojo.setTxnAmount(virtualprepaid_l2_transferamount_value_txt.getText().toString());
        domesticL2RequestPojo.setTxnRefNo(topupValidationResponse.getTransactionId());
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new VocherL2Processing(domesticL2RequestPojo, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(false);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
