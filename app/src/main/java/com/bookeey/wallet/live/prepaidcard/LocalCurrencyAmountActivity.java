package com.bookeey.wallet.live.prepaidcard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.DenominationRequest;
import ycash.wallet.json.pojo.virtualprepaidcards.RequestCardResponse;

/**
 * Created by 30099 on 6/16/2016.
 */
public class LocalCurrencyAmountActivity extends Activity {
    TextView virtual_card_name, virtual_card_amount;
    RequestCardResponse requestCardResponse;
    Button local_currency_buy_btn, local_currency_cancel_btn;
    EditText localcurrency_pin_edit;
    ProgressDialog progress;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.localcurrency_popup);
        progress = new ProgressDialog(LocalCurrencyAmountActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText(((CoreApplication) getApplication()).getCardName() + "- Card - Confirm");*/
        local_currency_cancel_btn = (Button) findViewById(R.id.local_currency_cancel_btn);
        this.setFinishOnTouchOutside(false);
        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText(((CoreApplication) getApplication()).getCardName() + "- Card");*/
        virtual_card_name = (TextView) findViewById(R.id.virtual_card_name);
        virtual_card_amount = (TextView) findViewById(R.id.virtual_card_amount);
        localcurrency_pin_edit = (EditText) findViewById(R.id.localcurrency_pin_edit);
        requestCardResponse = ((CoreApplication) getApplication()).getRequestCardResponse();
        local_currency_buy_btn = (Button) findViewById(R.id.local_currency_buy_btn);
        local_currency_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), VirtualDenominationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        virtual_card_name.setText("" + ((CoreApplication) getApplication()).getCardPrice().replace("GBP", "\u00A3"));
        virtual_card_amount.setText(requestCardResponse.getPrice());
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("PREPAIDCARDS");
        if (limits.getTpinLimit() < Double.parseDouble(requestCardResponse.getPrice().trim())) {
            localcurrency_pin_edit.setVisibility(View.VISIBLE);
        } else {
            localcurrency_pin_edit.setVisibility(View.INVISIBLE);
        }


        localcurrency_pin_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pin = localcurrency_pin_edit.getText().toString().trim();

                if (pin.length() == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(localcurrency_pin_edit.getWindowToken(), 0);
                } /*else {
                    Toast.makeText(getBaseContext(), "Please enter tpin", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        local_currency_buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = localcurrency_pin_edit.getText().toString().trim();
                if (pin.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), "Please enter tpin", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                } else {
                    buyOperation();
                }
            }
        });
    }

    private void buyOperation() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        DenominationRequest denominationRequest = new DenominationRequest();
        denominationRequest.setPrice(((CoreApplication) getApplication()).getRequestCardResponse().getPrice());
        denominationRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        denominationRequest.setRefId(((CoreApplication) getApplication()).getRequestCardResponse().getRefId());
        denominationRequest.setG_transType(TransType.PREPAID_CONFIRM_REQUEST.name());
        denominationRequest.setTpin(pin);

        String jsondata = new Gson().toJson(denominationRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PREPAID_CONFIRM_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CONFIRM_RESPONSE.name()) && response.getG_status() == 1) {
                        RequestCardResponse requestCardResponse = new Gson().fromJson((String) msg.obj, RequestCardResponse.class);
                        ((CoreApplication) getApplication()).setRequestCardResponse(requestCardResponse);
                        Intent intent = new Intent(getBaseContext(), FinalVirtualCardActvity.class);
                        startActivity(intent);
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(LocalCurrencyAmountActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(LocalCurrencyAmountActivity.this, LoginActivity.class);
                        startActivity(intent);
                        LocalCurrencyAmountActivity.this.finish();
                        return;
                    } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CONFIRM_RESPONSE.name()) && response.getG_status() != 1) {
                        Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), "General server error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), "Failure general server", Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), "Failure network connection", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), true,getApplicationContext())).start();
        showIfNotVisible("");

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

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), VirtualDenominationActivity.class);
        startActivity(intent);
    }
}
