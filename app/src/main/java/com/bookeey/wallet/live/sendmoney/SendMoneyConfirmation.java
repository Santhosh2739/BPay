package com.bookeey.wallet.live.sendmoney;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.google.gson.Gson;

import java.util.Date;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.SendmoneyConformationProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.sendmoney.PeertoPeerCommitRequest;
import ycash.wallet.json.pojo.sendmoney.PeertoPeerConformationResponse;

/**
 * Created by 30099 on 1/8/2016.
 */
public class SendMoneyConfirmation extends GenericActivity implements YPCHeadlessCallback {

    private String response = null;
    private PeertoPeerConformationResponse response_obj = null;
    CustomerLoginRequestReponse customerLoginRequestReponse = null;
    LinearLayout ooredoo_sendmoney_confirmpayment_exchangerate_linear;
    TextView ooredoo_sendmoney_confirmpayment_mobileno_left0_tv,
            ooredoo_sendmoney_confirmpayment_mobileno_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_status_left0_tv,
            ooredoo_sendmoney_confirmpayment_status_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_transferamount_left0_tv,
            ooredoo_sendmoney_confirmpayment_transferamount_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_exchangerate_left0_tv,
            ooredoo_sendmoney_confirmpayment_exchangerate_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_wallentbalance_left0_tv,
            ooredoo_sendmoney_confirmpayment_wallentbalance_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_processingfee_left0_tv,
            ooredoo_sendmoney_confirmpayment_processingfee_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_totalamount_left0_tv,
            ooredoo_sendmoney_confirmpayment_totalamount_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_customerid_left0_tv,
            ooredoo_sendmoney_confirmpayment_customerid_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_txntime_left0_tv,
            ooredoo_sendmoney_confirmpayment_txnid_left0_tv,
            ooredoo_sendmoney_confirmpayment_txnid_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_txntime_value_right0_tv,
            ooredoo_sendmoney_confirmpayment_confirmpayment_txt;
    Button ooredoo_sendmoney_confirmpayment_confirm_button;

    View ooredoo_sendmoney_confirmpayment_mobileno_vertical0_view,
            ooredoo_sendmoney_confirmpayment_mobileno_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_status_vertcal0_view,
            ooredoo_sendmoney_confirmpayment_txnid_vertical0_view,
            ooredoo_sendmoney_confirmpayment_txnid_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_status_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_transferamount_vertical0_view,
            ooredoo_sendmoney_confirmpayment_transferamount_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_exchangerate_vertical0_view,
            ooredoo_sendmoney_confirmpayment_exchangerate_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_wallentbalance_vertical0_view,
            ooredoo_sendmoney_confirmpayment_wallentbalance_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_processingfee_vertical0_view,
            ooredoo_sendmoney_confirmpayment_processingfee_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_totalamount_vertical0_view,
            ooredoo_sendmoney_confirmpayment_totalamount_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_customerid_vertical0_view,
            ooredoo_sendmoney_confirmpayment_customerid_value_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_txntime_vertical0_view,
            ooredoo_sendmoney_confirmpayment_txntime_value_horizantal0_view;

    ImageView send_money_final_tickmark_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_money_confirm_payment);

        /*View mCustomView = getActionBar().getCustomView();
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
        mTitleTextView.setText(getResources().getString(R.string.send_money_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ImageView sendmoney_conformation_image = (ImageView) findViewById(R.id.sendmoney_conformation_image);

        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            sendmoney_conformation_image.setImageBitmap(stringToBitmap(image));
        }
        customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        send_money_final_tickmark_img = (ImageView) findViewById(R.id.send_money_final_tickmark_img);
        updateProfile(R.id.sendmoney_confirm_nameTextooredo, R.id.sendmoney_confirm_wallet_id, R.id.sendmoney_confirm_balance_id);
        send_money_final_tickmark_img.setVisibility(View.GONE);
        response = getIntent().getExtras().getString("RESPONSE");
        response_obj = new Gson().fromJson(response, PeertoPeerConformationResponse.class);
        ooredoo_sendmoney_confirmpayment_exchangerate_linear = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_exchangerate_linear);
        ooredoo_sendmoney_confirmpayment_mobileno_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_mobileno_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_status_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_status_left0_tv);
        ooredoo_sendmoney_confirmpayment_confirmpayment_txt = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_confirmpayment_txt);
        ooredoo_sendmoney_confirmpayment_status_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_status_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_transferamount_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_transferamount_left0_tv);
        ooredoo_sendmoney_confirmpayment_transferamount_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_transferamount_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_exchangerate_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_exchangerate_left0_tv);
        ooredoo_sendmoney_confirmpayment_exchangerate_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_exchangerate_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_wallentbalance_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_wallentbalance_left0_tv);
        ooredoo_sendmoney_confirmpayment_wallentbalance_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_wallentbalance_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_processingfee_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_processingfee_left0_tv);
        ooredoo_sendmoney_confirmpayment_processingfee_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_processingfee_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_totalamount_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_totalamount_left0_tv);
        ooredoo_sendmoney_confirmpayment_totalamount_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_totalamount_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_customerid_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_customerid_left0_tv);
        ooredoo_sendmoney_confirmpayment_customerid_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_customerid_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_txntime_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txntime_left0_tv);
        ooredoo_sendmoney_confirmpayment_txntime_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txntime_value_right0_tv);
        ooredoo_sendmoney_confirmpayment_txnid_left0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txnid_left0_tv);
        ooredoo_sendmoney_confirmpayment_txnid_value_right0_tv = (TextView) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txnid_value_right0_tv);

        ooredoo_sendmoney_confirmpayment_mobileno_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_mobileno_vertical0_view);
        ooredoo_sendmoney_confirmpayment_mobileno_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_mobileno_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_status_vertcal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_status_vertcal0_view);
        ooredoo_sendmoney_confirmpayment_status_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_status_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_transferamount_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_transferamount_vertical0_view);
        ooredoo_sendmoney_confirmpayment_transferamount_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_transferamount_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_exchangerate_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_exchangerate_vertical0_view);
        ooredoo_sendmoney_confirmpayment_exchangerate_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_exchangerate_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_wallentbalance_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_wallentbalance_vertical0_view);
        ooredoo_sendmoney_confirmpayment_wallentbalance_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_wallentbalance_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_processingfee_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_processingfee_vertical0_view);
        ooredoo_sendmoney_confirmpayment_processingfee_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_processingfee_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_totalamount_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_totalamount_vertical0_view);
        ooredoo_sendmoney_confirmpayment_totalamount_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_totalamount_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_customerid_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_customerid_vertical0_view);
        ooredoo_sendmoney_confirmpayment_customerid_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_customerid_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_txntime_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txntime_vertical0_view);
        ooredoo_sendmoney_confirmpayment_txntime_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txntime_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_txnid_value_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txnid_value_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_txnid_vertical0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_txnid_vertical0_view);

        ooredoo_sendmoney_confirmpayment_txntime_value_right0_tv.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(response_obj.getServerTime())));
        ooredoo_sendmoney_confirmpayment_confirm_button = (Button) findViewById(R.id.ooredoo_sendmoney_confirmpayment_confirm_button);

        ooredoo_sendmoney_confirmpayment_mobileno_value_right0_tv.setText(response_obj.getMobileNumber());
//            ooredoo_sendmoney_confirmpayment_mobileno_left0_tv.setText(response_obj.getMobileNumber());
        ooredoo_sendmoney_confirmpayment_status_value_right0_tv.setText("Pending");
        ooredoo_sendmoney_confirmpayment_transferamount_value_right0_tv.setText(PriceFormatter.format(Double.parseDouble(response_obj.getTxnamt()), 3, 3));
        ooredoo_sendmoney_confirmpayment_exchangerate_linear.setVisibility(View.GONE);
        ooredoo_sendmoney_confirmpayment_exchangerate_value_right0_tv.setVisibility(View.GONE);
        ooredoo_sendmoney_confirmpayment_exchangerate_left0_tv.setVisibility(View.GONE);
        ooredoo_sendmoney_confirmpayment_exchangerate_horizantal0_view.setVisibility(View.GONE);
        ooredoo_sendmoney_confirmpayment_exchangerate_vertical0_view.setVisibility(View.GONE);
        ooredoo_sendmoney_confirmpayment_wallentbalance_value_right0_tv.setText("-NA-");
        ooredoo_sendmoney_confirmpayment_processingfee_value_right0_tv.setText(PriceFormatter.format(Double.parseDouble(response_obj.getProcessingFee()), 3, 3));
        ooredoo_sendmoney_confirmpayment_totalamount_value_right0_tv.setText(PriceFormatter.format(Double.parseDouble(response_obj.getAmountToDebit()), 3, 3));
        ooredoo_sendmoney_confirmpayment_customerid_value_right0_tv.setText(response_obj.getCustomerID());
        ooredoo_sendmoney_confirmpayment_txnid_value_right0_tv.setText(response_obj.getTxnId());

        Log.e("Pojo", new Gson().toJson(response_obj));
        Log.e("EEEEEEEEE", "" + response_obj.getServerTime());
        //end

        ooredoo_sendmoney_confirmpayment_confirmpayment_txt.setTypeface(Typeface.DEFAULT_BOLD);


        ooredoo_sendmoney_confirmpayment_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeertoPeerCommitRequest peertoPeerCommitRequest = new PeertoPeerCommitRequest();
                peertoPeerCommitRequest.setSenderNumber(ooredoo_sendmoney_confirmpayment_mobileno_value_right0_tv.getText().toString());
                peertoPeerCommitRequest.setTxnId(ooredoo_sendmoney_confirmpayment_txnid_value_right0_tv.getText().toString());
                peertoPeerCommitRequest.setAmountToDebit(response_obj.getAmountToDebit());
                peertoPeerCommitRequest.setTxnamt(response_obj.getTxnamt());
                peertoPeerCommitRequest.setProcessingFee(ooredoo_sendmoney_confirmpayment_processingfee_value_right0_tv.getText().toString());
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new SendmoneyConformationProcessing(peertoPeerCommitRequest, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(false);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("response", response);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (null != savedInstanceState) {
            response = savedInstanceState.getString("response");
            response_obj = new Gson().fromJson(response, PeertoPeerConformationResponse.class);
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
    public void handleProfileUpdate() {
        super.handleProfileUpdate();
        updateProfile(R.id.sendmoney_confirm_nameTextooredo, R.id.sendmoney_confirm_wallet_id, R.id.sendmoney_confirm_balance_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
