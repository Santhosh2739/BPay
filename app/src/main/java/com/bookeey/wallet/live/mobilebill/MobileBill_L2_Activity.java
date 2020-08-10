package com.bookeey.wallet.live.mobilebill;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TableRow;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.mobile.DenominationsL2Processing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2RequestPojo;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2Response;


/**
 * Created by 10037 on 6/17/2017.
 */

public class MobileBill_L2_Activity extends GenericActivity{
    TextView mobilebill_mobilenumber_value_txt,
            mobilebill_status_value_txt,
            mobilebill_transferamount_value_txt,
            mobilebill_balanceafter_value_txt,
            mobilebill_customerid_value_txt,
            mobilebill_txnid_value_txt,
            mobilebill_txndate_value_txt,
            mobilebill_mobilebill_paymentreft_value_txt;
    Button mobilebill_confirm_btn;
    DomesticL2Response domesticL2Response = null;
    TableRow mobile_payment_ref_tablerow;
    View mobile_payment_ref_view;
    ProgressDialogFrag progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilebill_leg2_activity);
        progress = new ProgressDialogFrag();


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
                onBackPressed();
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
        mobile_payment_ref_tablerow = (TableRow) findViewById(R.id.mobile_payment_ref_tablerow);
        mobile_payment_ref_view = (View) findViewById(R.id.mobile_payment_ref_view);
        mobile_payment_ref_tablerow.setVisibility(View.GONE);
        mobile_payment_ref_view.setVisibility(View.GONE);
        mobilebill_confirm_btn = (Button) findViewById(R.id.mobilebill_confirm_btn);

        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.mobilebill_l1_nameTextooredo, R.id.mobilebill_l1_wallet_id, R.id.mobilebill_l1_balance_id);
        domesticL2Response = new Gson().fromJson(getIntent().getStringExtra("DOMESTIC_L1_RESPONSE"), DomesticL2Response.class);
        mobilebill_mobilenumber_value_txt.setText(domesticL2Response.getRecieverMobileNumber());
        mobilebill_status_value_txt.setText("--PENDING--");
        mobilebill_transferamount_value_txt.setText(domesticL2Response.getTransferAmount());
        mobilebill_balanceafter_value_txt.setText(domesticL2Response.getBalanceAfter());
        mobilebill_customerid_value_txt.setText(domesticL2Response.getCustomerId());
        mobilebill_txnid_value_txt.setText(domesticL2Response.getTransactionId());
        mobilebill_txndate_value_txt.setText(domesticL2Response.getTxnDate());
        // mobilebill_mobilebill_paymentreft_value_txt.setText(domesticL2Response.getPaymentRef());

        mobilebill_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DomesticL2Request();
            }
        });
    }

    private void DomesticL2Request() {
        DomesticL2RequestPojo domesticL2RequestPojo = new DomesticL2RequestPojo();
        domesticL2RequestPojo.setTxnAmount(domesticL2Response.getTransferAmount());
        domesticL2RequestPojo.setTxnRefNo(domesticL2Response.getTransactionId());

        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new DenominationsL2Processing(domesticL2RequestPojo, application, true));
        //ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(false);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

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

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(getBaseContext(), MobileBill_L1_Activity.class);
        startActivity(intent);
    }*/
}
