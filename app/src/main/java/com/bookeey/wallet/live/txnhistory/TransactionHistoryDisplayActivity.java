package com.bookeey.wallet.live.txnhistory;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import coreframework.database.CustomSharedPreferences;
import ycash.wallet.json.pojo.Internationaltopup.TransactionalHistoryForInternationalRecharge;

import com.bookeey.wallet.live.application.CoreApplication;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.forceface.TransTypeInterface;

import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.incomingmoney.SendMoneyIncomingResponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticRechargeTranHistoryResponsePojo;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantCommitRequestResponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyCommitRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyRequestResponse;

import static com.bookeey.wallet.live.R.id.receiptername_linear;
import static com.bookeey.wallet.live.R.id.status_linearlayout;
import static com.bookeey.wallet.live.R.id.txntype_left;

/**
 * Created by mohit on 7/1/16.
 */
public class TransactionHistoryDisplayActivity extends GenericActivity implements YPCHeadlessCallback {
    TextView ooredoo_transaction_screen_time_txt, warning_text;
    TextView right0;
    TextView right1;
    TextView right2;
    TextView right3;
    TextView right4;
    TextView right5;
    TextView right6, right7, right8, wallet_balance_right5, right9, txntype_right;
    LinearLayout txntype_linear, processingfee_linear, customer_id_linear, total_amount_linear, number_linear, status_linear, paymentamount_linear, receiptentname_linear, walletbalance_linear, balanceafter_linear, transactionid_linear, transactiontime_linear, loadtype_linear, extra_linear,
            branch_linear;
    TextView left0, left1, left2, left3, left4, left5, left6, left7, left8, wallet_balance_left5, left9, txntype_left, left11, right11, branch_left, branch_right;
    View vertical0, vertical1, vertical2, vertical3, wallet_balance_vertical5, vertical4, vertical5, vertical6, vertical7, vertical8, vertical9,
            horizontal0, horizontal1, horizontal2, horizontal3, wallet_balance_horizontal5, horizontal4, horizontal5, horizontal6, horizontal7, horizontal8, horizontal9,
            txntype_hor_view, horizontal11, vertical11, branch_hor_view;
    Button close_button;
    ImageView ooredoo_bill_payment_img;

    ImageView merchant_category_screen_wallet_logo_back, home_up_back;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_display);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //TODO: SET XML VIEW HERE
        //TODO: CONFIGURE BUTTON TO CLOSE THE ACTIVITY

       /* View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("RECEIPT");*/

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
        mTitleTextView.setText(getResources().getString(R.string.txn_history_title));

        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String json = getIntent().getStringExtra("transaction");
        String typeOfResponse = getIntent().getStringExtra("type");
        TransType type = TransType.valueOf(typeOfResponse);
        LinearLayout language_table = (LinearLayout) findViewById(R.id.language_table);
        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage.equals("ar")) {
            language_table.setTextDirection(4);
        } else {
            language_table.setTextDirection(3);
        }

        ooredoo_transaction_screen_time_txt = (TextView) findViewById(R.id.ooredoo_transaction_screen_time_txt);
        right0 = (TextView) findViewById(R.id.right0);
        right1 = (TextView) findViewById(R.id.right1);
        right2 = (TextView) findViewById(R.id.right2);
        right3 = (TextView) findViewById(R.id.right3);
        right4 = (TextView) findViewById(R.id.right4);
        right5 = (TextView) findViewById(R.id.right5);
        right6 = (TextView) findViewById(R.id.right6);
        right7 = (TextView) findViewById(R.id.right7);
        right8 = (TextView) findViewById(R.id.right8);
        wallet_balance_right5 = (TextView) findViewById(R.id.wallet_balance_right5);
        right9 = (TextView) findViewById(R.id.right9);
        processingfee_linear = (LinearLayout) findViewById(R.id.processingfee_linear);
        customer_id_linear = (LinearLayout) findViewById(R.id.customer_id_linear);
        total_amount_linear = (LinearLayout) findViewById(R.id.total_amount_linear);
        number_linear = (LinearLayout) findViewById(R.id.number_linear);
        status_linear = (LinearLayout) findViewById(status_linearlayout);
        paymentamount_linear = (LinearLayout) findViewById(R.id.paymentamount_linear);
        balanceafter_linear = (LinearLayout) findViewById(R.id.walletbalance_linear);
        transactionid_linear = (LinearLayout) findViewById(R.id.transactionid_linear);
        transactiontime_linear = (LinearLayout) findViewById(R.id.transactiontime_linear);
        loadtype_linear = (LinearLayout) findViewById(R.id.loadtype_linear);
        receiptentname_linear = (LinearLayout) findViewById(receiptername_linear);
        walletbalance_linear = (LinearLayout) findViewById(R.id.walletbalance_linear);
        txntype_linear = (LinearLayout) findViewById(R.id.txntype_linear);
        extra_linear = (LinearLayout) findViewById(R.id.extra_linear);
        txntype_left = (TextView) findViewById(R.id.txntype_left);
        left0 = (TextView) findViewById(R.id.left0);
        left1 = (TextView) findViewById(R.id.left1);
        left2 = (TextView) findViewById(R.id.left2);
        left3 = (TextView) findViewById(R.id.left3);
        left4 = (TextView) findViewById(R.id.left4);
        left5 = (TextView) findViewById(R.id.left5);
        left6 = (TextView) findViewById(R.id.left6);
        left7 = (TextView) findViewById(R.id.left7);
        left8 = (TextView) findViewById(R.id.left8);
        left11 = (TextView) findViewById(R.id.left11);
        right11 = (TextView) findViewById(R.id.right11);
        txntype_right = (TextView) findViewById(R.id.txntype_right);
        wallet_balance_left5 = (TextView) findViewById(R.id.wallet_balance_left5);
        left9 = (TextView) findViewById(R.id.left9);
        vertical0 = (View) findViewById(R.id.vertical0);
        vertical1 = (View) findViewById(R.id.vertical1);
        vertical2 = (View) findViewById(R.id.vertical2);
        vertical3 = (View) findViewById(R.id.vertical3);
        vertical4 = (View) findViewById(R.id.vertical4);
        vertical5 = (View) findViewById(R.id.vertical5);
        vertical6 = (View) findViewById(R.id.vertical6);
        vertical7 = (View) findViewById(R.id.vertical7);
        vertical8 = (View) findViewById(R.id.vertical8);
        wallet_balance_vertical5 = (View) findViewById(R.id.wallet_balance_vertical5);
        vertical9 = (View) findViewById(R.id.vertical9);
        horizontal0 = (View) findViewById(R.id.horizontal0);
        horizontal1 = (View) findViewById(R.id.horizontal1);
        horizontal2 = (View) findViewById(R.id.horizontal2);
        horizontal3 = (View) findViewById(R.id.horizontal3);
        horizontal4 = (View) findViewById(R.id.horizontal4);
        horizontal5 = (View) findViewById(R.id.horizontal5);
        horizontal6 = (View) findViewById(R.id.horizontal6);
        horizontal7 = (View) findViewById(R.id.horizontal7);
        horizontal8 = (View) findViewById(R.id.horizontal8);
        txntype_hor_view = (View) findViewById(R.id.txntype_hor_view);
        horizontal11 = (View) findViewById(R.id.horizontal11);
        vertical11 = (View) findViewById(R.id.vertical11);
        wallet_balance_horizontal5 = (View) findViewById(R.id.wallet_balance_horizontal5);
        horizontal9 = (View) findViewById(R.id.horizontal9);
        ooredoo_bill_payment_img = (ImageView) findViewById(R.id.ooredoo_bill_payment_img);
        close_button = (Button) findViewById(R.id.close_button);
        warning_text = (TextView) findViewById(R.id.warning_text);

        //for branch newly added
        branch_linear = (LinearLayout) findViewById(R.id.branch_linear);
        branch_left = (TextView) findViewById(R.id.branch_left);
        branch_right = (TextView) findViewById(R.id.branch_right);
        branch_hor_view = (View) findViewById(R.id.branch_hor_view);


        if (null != type) {
            switch (type) {
                case SEND_MONEY_REQUEST_RESPONSE:
                    loadDetails(new Gson().fromJson(json, SendMoneyRequestResponse.class));
                    break;
                case SEND_MONEY_COMMIT_REQUEST_RESPONSE:
                    loadDetails(new Gson().fromJson(json, SendMoneyCommitRequestResponse.class));
                    break;
                case DOMESTIC_RECHARGE_TRAN_RESPONSE:
                    loadDetails(new Gson().fromJson(json, DomesticRechargeTranHistoryResponsePojo.class));
                    break;
                case ECOMMERCE_TRAN_RESPONSE:
                    loadDetails(new Gson().fromJson(json, DomesticRechargeTranHistoryResponsePojo.class));
                    break;
                case VOCHER_RECHARGE_TRAN_RESPONSE:
                    loadDetails(new Gson().fromJson(json, DomesticRechargeTranHistoryResponsePojo.class));
                    break;
                case PAY_TO_MERCHANT_RESPONSE:
                    loadDetails(new Gson().fromJson(json, PayToMerchantRequestResponse.class));
                    break;
                case PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE:
                    loadDetails(new Gson().fromJson(json, PayToMerchantCommitRequestResponse.class));
                    break;
                case P2P_RECEIVED:
                    loadDetails(new Gson().fromJson(json, SendMoneyIncomingResponse.class));
                    break;
                case LOAD_MONEY:
                    loadDetails(new Gson().fromJson(json, SendMoneyIncomingResponse.class));
                    break;
                case INTERNATIONAL_RECHARGE_L2_RESPONSE:
                    loadDetails(new Gson().fromJson(json, TransactionalHistoryForInternationalRecharge.class));
                    break;
                case PREPAID_REQUESTCARD_RESPONSE:
                    loadDetails(new Gson().fromJson(json, TransactionalHistoryForInternationalRecharge.class));
                    break;
                case INVOICE_TRAN_RESPONSE:
                    loadDetails(new Gson().fromJson(json, DomesticRechargeTranHistoryResponsePojo.class));
                   break;
                case CASHBACK:
                    loadDetails(new Gson().fromJson(json, DomesticRechargeTranHistoryResponsePojo.class));
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page for details of a specific transaction history");



        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 26);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page for details of a specific transaction history");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 26 logged");
    }

    private void loadDetails(TransTypeInterface transTypeInterface) {
        //TODO: Initialize Global Views If Any
        TransType type = TransType.valueOf(transTypeInterface.getG_response_trans_type());
        switch (type) {
            case SEND_MONEY_REQUEST_RESPONSE:
                SendMoneyRequestResponse sendMoneyRequestResponse = (SendMoneyRequestResponse) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                right1.setText(getResources().getString(R.string.txn_failure));
                right0.setText(sendMoneyRequestResponse.getRecipientMobileNumber());
                right2.setText(sendMoneyRequestResponse.getTransactionId());
                right3.setText(PriceFormatter.format(sendMoneyRequestResponse.getTxnAmount(), 3, 3));
                right4.setText(PriceFormatter.format(sendMoneyRequestResponse.getProcessingfee(), 3, 3));
                right5.setText(PriceFormatter.format(sendMoneyRequestResponse.getTotal(), 3, 3));
                right6.setText(sendMoneyRequestResponse.getRecipientName());
                right7.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                customer_id_linear.setVisibility(View.GONE);
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyRequestResponse.getServerTime())));
                wallet_balance_right5.setText(PriceFormatter.format(sendMoneyRequestResponse.getBalance(), 3, 3));
                break;
            case DOMESTIC_RECHARGE_TRAN_RESPONSE:
                DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo = (DomesticRechargeTranHistoryResponsePojo) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                if (domesticRechargeTranHistoryResponsePojo.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right1.setText(domesticRechargeTranHistoryResponsePojo.getG_status_description());
                } else if (domesticRechargeTranHistoryResponsePojo.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right1.setText(getResources().getString(R.string.txn_failure));
                } else if (domesticRechargeTranHistoryResponsePojo.getTranType().equals("ssss")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    right1.setText(getResources().getString(R.string.txn_txn_under_process));
                }
                left4.setVisibility(View.GONE);
                right4.setVisibility(View.GONE);
                vertical4.setVisibility(View.GONE);
                horizontal4.setVisibility(View.GONE);
                left0.setText(getResources().getString(R.string.txn_operator_name));
                right0.setText(domesticRechargeTranHistoryResponsePojo.getOperatorName());
                txntype_hor_view.setVisibility(View.VISIBLE);
                txntype_linear.setVisibility(View.VISIBLE);
                txntype_right.setText(domesticRechargeTranHistoryResponsePojo.getBillType());

                left6.setText(getResources().getString(R.string.txn_topup_amount_kwd));
                right6.setText(PriceFormatter.format(domesticRechargeTranHistoryResponsePojo.getDenominationinKWD(), 3, 3));

                customer_id_linear.setVisibility(View.GONE);
                right2.setText(domesticRechargeTranHistoryResponsePojo.getTransactionId());

                left3.setText(getResources().getString(R.string.txn_history_mobilenumber_txt));
                right3.setText(domesticRechargeTranHistoryResponsePojo.getRecipientMobileNumber());

                left5.setText(getResources().getString(R.string.txn_amount));
                right5.setText(PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo.getRechargeAmt()), 3, 3));

                right7.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);

                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo.getServerTime())));

                wallet_balance_left5.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                wallet_balance_right5.setText(PriceFormatter.format(domesticRechargeTranHistoryResponsePojo.getSenderbalanceAfeter(), 3, 3));
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);

                //BPoints START
                View bpoints_horizontal5 = (View) findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.VISIBLE);
                LinearLayout bpoints_linear = (LinearLayout) findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.VISIBLE);

                TextView bpoints_right = (TextView) findViewById(R.id.bpoints_right);

//                double bpoints = domesticRechargeTranHistoryResponsePojo.getDenominationinKWD() * 5;

                BigDecimal bpoints =   BigDecimal.valueOf(domesticRechargeTranHistoryResponsePojo.getDenominationinKWD()).multiply(new BigDecimal("5"));

                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//                DecimalFormat df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                bpoints_right.setText("" + String.valueOf(bpoints));

                //BPoints END


                //Bpoint balance START
                horizontal7.setVisibility(View.GONE);


                if (domesticRechargeTranHistoryResponsePojo.getTotalRedemptionPoints() != null) {
                    horizontal7.setVisibility(View.VISIBLE);
                    View prepaid_cards_bpoints_balance_view = (View) findViewById(R.id.prepaid_cards_bpoints_balance_view);
                    prepaid_cards_bpoints_balance_view.setVisibility(View.GONE);
                    LinearLayout prepaid_cards_bpoints_balance_linear = (LinearLayout) findViewById(R.id.prepaid_cards_bpoints_balance_linear);
                    prepaid_cards_bpoints_balance_linear.setVisibility(View.VISIBLE);

                    TextView prepaid_cards_bpoints_balance_right = (TextView) findViewById(R.id.prepaid_cards_bpoints_balance_right);
                    prepaid_cards_bpoints_balance_right.setText("" + domesticRechargeTranHistoryResponsePojo.getTotalRedemptionPoints());
                }

                //Bpoint balance END

                branch_hor_view.setVisibility(View.GONE);

                break;

            case VOCHER_RECHARGE_TRAN_RESPONSE:
                DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo1 = (DomesticRechargeTranHistoryResponsePojo) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                if (domesticRechargeTranHistoryResponsePojo1.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    left2.setText(getResources().getString(R.string.txn_history_status_txt));
                    right2.setText(domesticRechargeTranHistoryResponsePojo1.getG_status_description());
                } else if (domesticRechargeTranHistoryResponsePojo1.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    left2.setText(getResources().getString(R.string.txn_failure));
                    right2.setText(domesticRechargeTranHistoryResponsePojo1.getG_status_description());
                } else if (domesticRechargeTranHistoryResponsePojo1.getTranType().equals("ssss")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    left2.setText(getResources().getString(R.string.txn_txn_under_process));
                    right2.setText(domesticRechargeTranHistoryResponsePojo1.getG_status_description());
                }
                left0.setVisibility(View.GONE);
                vertical0.setVisibility(View.GONE);
                right0.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                walletbalance_linear.setVisibility(View.GONE);
                wallet_balance_vertical5.setVisibility(View.GONE);
                wallet_balance_horizontal5.setVisibility(View.VISIBLE);
                left1.setText(getResources().getString(R.string.txn_operator_name));
                right1.setText(domesticRechargeTranHistoryResponsePojo1.getOperatorName());


                if(domesticRechargeTranHistoryResponsePojo1.getSerialNo()!=null)
                {

                    left3.setText(getResources().getString(R.string.txn_serial_number));
                    right3.setText(domesticRechargeTranHistoryResponsePojo1.getSerialNo());

                }else{
                    paymentamount_linear.setVisibility(View.GONE);
                    horizontal3.setVisibility(View.GONE);
                }

                left4.setText(getResources().getString(R.string.txn_recharge_code));
                String recharge_code = "";

                if(domesticRechargeTranHistoryResponsePojo1.getRechargeCode()!=null) {

                     recharge_code = domesticRechargeTranHistoryResponsePojo1.getRechargeCode().replaceAll(System.getProperty("line.separator"), "");
                    right4.setText(recharge_code);
                }else{
                    processingfee_linear.setVisibility(View.GONE);
                    horizontal1.setVisibility(View.GONE);
                }


                right4.setTextIsSelectable(true);
                left5.setText(getResources().getString(R.string.txn_amount));
                right5.setText(PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo1.getRechargeAmt()), 3, 3));

                left6.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                right6.setText(PriceFormatter.format(domesticRechargeTranHistoryResponsePojo1.getSenderbalanceAfeter(), 3, 3));
                left7.setText(getResources().getString(R.string.txn_ref_no));
                right7.setText(domesticRechargeTranHistoryResponsePojo1.getTransactionId());
                left8.setText(getResources().getString(R.string.txn_history_time_txt));
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo1.getServerTime())));
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);
                String opeator_Name = domesticRechargeTranHistoryResponsePojo1.getOperatorName();
                if (opeator_Name != null && !opeator_Name.isEmpty()) {
                    if (opeator_Name.equalsIgnoreCase("Zain")) {
                        warning_text.setVisibility(View.VISIBLE);
                        warning_text.setText(getResources().getString(R.string.txn_recharge_141));
                    } else if (opeator_Name.equalsIgnoreCase("Ooredoo")) {
                        warning_text.setVisibility(View.VISIBLE);
                        warning_text.setText(getResources().getString(R.string.txn_recharge_111));
                    } else if (opeator_Name.equalsIgnoreCase("Viva")) {
                        warning_text.setVisibility(View.VISIBLE);
                        warning_text.setText(getResources().getString(R.string.txn_recharge_500));
                    } else {
                        // warning_text.setVisibility(View.GONE);
                    }
                }


                //BPoints START
                bpoints_horizontal5 = (View) findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.GONE);
                bpoints_linear = (LinearLayout) findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.VISIBLE);

                bpoints_right = (TextView) findViewById(R.id.bpoints_right);

                TextView bpoints_left = (TextView) findViewById(R.id.bpoints_left);

//                bpoints = Double.parseDouble(domesticRechargeTranHistoryResponsePojo1.getRechargeAmt()) * 5;

                bpoints =  new BigDecimal(domesticRechargeTranHistoryResponsePojo1.getRechargeAmt()).multiply(new BigDecimal("5"));

                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                bpoints_right.setText(" " + String.valueOf(bpoints));

                //BPoints END


                left7.setText(getResources().getString(R.string.bpoitns_earned));
                right7.setText("" + String.valueOf(bpoints));


                bpoints_left.setText(getResources().getString(R.string.txn_ref_no));
                bpoints_right.setText(domesticRechargeTranHistoryResponsePojo1.getTransactionId());


                //Bpoint balance START

                if (domesticRechargeTranHistoryResponsePojo1.getTotalRedemptionPoints() != null) {
                    View bpoints_balance_horizontal5 = (View) findViewById(R.id.bpoints_balance_horizontal5);
                    bpoints_balance_horizontal5.setVisibility(View.VISIBLE);
                    LinearLayout bpoints_balance_linear_dom_recharge_linear = (LinearLayout) findViewById(R.id.bpoints_balance_linear_dom_recharge_linear);
                    bpoints_balance_linear_dom_recharge_linear.setVisibility(View.VISIBLE);

                    TextView domestic_recharge_bpoints_balance_right = (TextView) findViewById(R.id.domestic_recharge_bpoints_balance_right);
                    domestic_recharge_bpoints_balance_right.setText("" + domesticRechargeTranHistoryResponsePojo1.getTotalRedemptionPoints());
                }

                //Bpoint balance END


                branch_hor_view.setVisibility(View.GONE);

                break;

            case INVOICE_TRAN_RESPONSE:
                DomesticRechargeTranHistoryResponsePojo invoice = (DomesticRechargeTranHistoryResponsePojo) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);

                extra_linear.setVisibility(View.VISIBLE);
                horizontal11.setVisibility(View.VISIBLE);

                if (invoice.getPaymentStatus() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.hold_icon));
                    right11.setText(getResources().getString(R.string.txn_hold));
                } else if (invoice.getPaymentStatus() == 2) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right11.setText(getResources().getString(R.string.txn_success));
                } else if (invoice.getPaymentStatus() == 3) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right11.setText(getResources().getString(R.string.txn_rejected));
                } else if (invoice.getPaymentStatus() == 4) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right11.setText(getResources().getString(R.string.txn_failed));
                }
                if (!invoice.getOfferDescription().isEmpty()) {
                    processingfee_linear.setVisibility(View.VISIBLE);
                    left4.setVisibility(View.VISIBLE);
                    right4.setVisibility(View.VISIBLE);
                    vertical4.setVisibility(View.VISIBLE);
                    horizontal4.setVisibility(View.VISIBLE);
                    left4.setText(getResources().getString(R.string.txn_offer_id));
                    right4.setText("" + invoice.getOfferDescription());
                } else {
                    processingfee_linear.setVisibility(View.GONE);
                    left4.setVisibility(View.GONE);
                    right4.setVisibility(View.GONE);
                    vertical4.setVisibility(View.GONE);
                    horizontal4.setVisibility(View.GONE);
                }
                if (invoice.getDiscountPrice() != 0) {
                    walletbalance_linear.setVisibility(View.VISIBLE);
                    wallet_balance_left5.setVisibility(View.VISIBLE);
                    wallet_balance_vertical5.setVisibility(View.VISIBLE);
                    wallet_balance_right5.setVisibility(View.VISIBLE);
                    wallet_balance_horizontal5.setVisibility(View.VISIBLE);
                    wallet_balance_left5.setText(getResources().getString(R.string.txn_discount_amt));
                    wallet_balance_right5.setText(PriceFormatter.format(invoice.getDiscountPrice(), 3, 3) + " KWD");
                } else {
                    walletbalance_linear.setVisibility(View.GONE);
                    wallet_balance_left5.setVisibility(View.GONE);
                    wallet_balance_vertical5.setVisibility(View.GONE);
                    wallet_balance_right5.setVisibility(View.GONE);
                    wallet_balance_horizontal5.setVisibility(View.GONE);
                }
                left11.setText(getResources().getString(R.string.txn_history_status_txt));
                status_linear.setVisibility(View.GONE);
                number_linear.setVisibility(View.GONE);
                receiptentname_linear.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                txntype_hor_view.setVisibility(View.VISIBLE);
                txntype_linear.setVisibility(View.VISIBLE);
                txntype_left.setText(getResources().getString(R.string.txn_mercahnt_name));
                txntype_right.setText(invoice.getOperatorName());
                left2.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                right2.setText(PriceFormatter.format(invoice.getSenderbalanceAfeter(), 3, 3) + " KWD");

                horizontal1.setVisibility(View.GONE);
                left3.setText(getResources().getString(R.string.txn_history_mobilenumber_txt));
                right3.setText(invoice.getRecipientMobileNumber());

                left5.setText(getResources().getString(R.string.txn_inv_amount));
                right5.setText(PriceFormatter.format((invoice.getTotalPrice()), 3, 3) + " KWD");

                //offers module
                if (invoice.getRechargeAmt() != "" && invoice.getRechargeAmt() != null) {
                    customer_id_linear.setVisibility(View.VISIBLE);
                    right7.setVisibility(View.VISIBLE);
                    left7.setVisibility(View.VISIBLE);
                    vertical7.setVisibility(View.VISIBLE);
                    horizontal7.setVisibility(View.VISIBLE);
                    if (invoice.getPaymentStatus() == 2) {
                        left7.setText(getResources().getString(R.string.txn_amount_paid));
                    } else {
                        left7.setText(getResources().getString(R.string.txn_amount_pay));
                    }
                    right7.setText(PriceFormatter.format(Double.parseDouble(invoice.getRechargeAmt()), 3, 3) + " KWD");
                } else {
                    customer_id_linear.setVisibility(View.GONE);
                    right7.setVisibility(View.GONE);
                    left7.setVisibility(View.GONE);
                    vertical7.setVisibility(View.GONE);
                    horizontal7.setVisibility(View.GONE);
                }

                //for branch
                if (invoice.getBranch() != "" && invoice.getBranch() != null) {
                    branch_linear.setVisibility(View.VISIBLE);
                    branch_hor_view.setVisibility(View.VISIBLE);
                    branch_right.setText(invoice.getBranch());
                } else {
                    branch_linear.setVisibility(View.GONE);
                    branch_hor_view.setVisibility(View.GONE);
                }


                left8.setText(getResources().getString(R.string.txn_history_txn_id_txt));
                right8.setText(invoice.getTransactionId());

                left9.setText(getResources().getString(R.string.txn_history_time_txt));
                right9.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(invoice.getServerTime())));


                //BPoints START
                bpoints_horizontal5 = (View) findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.GONE);
                bpoints_linear = (LinearLayout) findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.GONE);

                bpoints_right = (TextView) findViewById(R.id.bpoints_right);

//                bpoints = Double.parseDouble(invoice.getRechargeAmt()) * 5;

                 bpoints =   new BigDecimal(invoice.getRechargeAmt()).multiply(new BigDecimal("5"));

                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                bpoints_right.setText("" + String.valueOf(bpoints));


                View horizontal_invoice_bpoints_view = (View) findViewById(R.id.horizontal_invoice_bpoints_view);
                horizontal_invoice_bpoints_view.setVisibility(View.VISIBLE);
                LinearLayout bpoints_linear_invoice = (LinearLayout) findViewById(R.id.bpoints_linear_invoice);
                bpoints_linear_invoice.setVisibility(View.VISIBLE);

                TextView invoice_bpoints_right2 = (TextView) findViewById(R.id.invoice_bpoints_right2);

//                bpoints = Double.parseDouble(invoice.getRechargeAmt()) * 5;

                 bpoints =  new BigDecimal(invoice.getRechargeAmt()).multiply(new BigDecimal("5"));
                 bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);



//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                invoice_bpoints_right2.setText("" + String.valueOf(bpoints));

                //BPoints END


                //BPoints balance START

                if (invoice.getTotalRedemptionPoints() != null) {
                    View horizontal_invoice_bpoints_balance_view = (View) findViewById(R.id.horizontal_invoice_bpoints_balance_view);
                    horizontal_invoice_bpoints_balance_view.setVisibility(View.VISIBLE);
                    LinearLayout bpoints_balance_linear_invoice = (LinearLayout) findViewById(R.id.bpoints_balance_linear_invoice);
                    bpoints_balance_linear_invoice.setVisibility(View.VISIBLE);

                    TextView invoice_bpoints_balance_right2 = (TextView) findViewById(R.id.invoice_bpoints_balance_right2);
                    invoice_bpoints_balance_right2.setText("" + invoice.getTotalRedemptionPoints());
                }

                //BPoints balance END


                break;

            case SEND_MONEY_COMMIT_REQUEST_RESPONSE:
                SendMoneyCommitRequestResponse sendMoneyCommitRequestResponse = (SendMoneyCommitRequestResponse) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                if (sendMoneyCommitRequestResponse.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right1.setText(sendMoneyCommitRequestResponse.getG_status_description());
                } else if (sendMoneyCommitRequestResponse.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right1.setText(getResources().getString(R.string.txn_failure));
                }
                right0.setText(sendMoneyCommitRequestResponse.getRecipientMobileNumber());
                right6.setText(sendMoneyCommitRequestResponse.getRecipientName());
                customer_id_linear.setVisibility(View.GONE);
                right2.setText(sendMoneyCommitRequestResponse.getTransactionId());
                right3.setText(PriceFormatter.format(sendMoneyCommitRequestResponse.getTxnAmount(), 3, 3));
                right4.setText(PriceFormatter.format(sendMoneyCommitRequestResponse.getProcessingfee(), 3, 3));
                right5.setText(PriceFormatter.format(sendMoneyCommitRequestResponse.getTotal(), 3, 3));
                right7.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                right6.setText(sendMoneyCommitRequestResponse.getRecipientName());
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyCommitRequestResponse.getServerTime())));
                wallet_balance_right5.setText(PriceFormatter.format(sendMoneyCommitRequestResponse.getBalance(), 3, 3));
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);




                horizontal0.setVisibility(View.GONE);


                break;

            case PAY_TO_MERCHANT_RESPONSE:
                PayToMerchantRequestResponse payToMerchantRequestResponse = (PayToMerchantRequestResponse) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                right0.setVisibility(View.GONE);
                left0.setVisibility(View.GONE);
                vertical0.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                right1.setVisibility(View.GONE);
                left1.setVisibility(View.GONE);
                vertical1.setVisibility(View.GONE);
                horizontal1.setVisibility(View.GONE);
                right2.setText(payToMerchantRequestResponse.getTransactionId());
                right3.setText("" + PriceFormatter.format(payToMerchantRequestResponse.getTxnAmount(), 3, 3));
                right4.setText(payToMerchantRequestResponse.getCustomerId());
                right5.setVisibility(View.GONE);
                left5.setVisibility(View.GONE);
                vertical5.setVisibility(View.GONE);
                horizontal5.setVisibility(View.GONE);
                processingfee_linear.setVisibility(View.GONE);
                total_amount_linear.setVisibility(View.GONE);
                right6.setText(payToMerchantRequestResponse.getRecipientName());
                wallet_balance_right5.setText(PriceFormatter.format(payToMerchantRequestResponse.getBalance(), 3, 3));
                right7.setText(payToMerchantRequestResponse.getCustomerId());
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(payToMerchantRequestResponse.getServerTime())));
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);
                break;
            case PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE:
                PayToMerchantCommitRequestResponse payToMerchantCommitRequestResponse = (PayToMerchantCommitRequestResponse) transTypeInterface;
                if (payToMerchantCommitRequestResponse.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                } else if (payToMerchantCommitRequestResponse.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                }
                left6.setText(getResources().getString(R.string.txn_mercahnt_name));
                right6.setText(payToMerchantCommitRequestResponse.getRecipientName());
                left0.setText(getResources().getString(R.string.txn_history_txn_id_txt));
                right0.setText(payToMerchantCommitRequestResponse.getTransactionId());

                if (payToMerchantCommitRequestResponse.getOfferId() != 0) {
                    status_linear.setVisibility(View.VISIBLE);
                    left1.setText(getResources().getString(R.string.txn_history_offer_ID));
                    right1.setText("" + payToMerchantCommitRequestResponse.getOfferId());
                } else {
                    status_linear.setVisibility(View.GONE);
                    horizontal1.setVisibility(View.GONE);

                }
                if (payToMerchantCommitRequestResponse.getTotalPrice() != 0) {
                    paymentamount_linear.setVisibility(View.VISIBLE);
                    left3.setText(getResources().getString(R.string.txn_history_amount_due));
                    right3.setText("" + PriceFormatter.format(payToMerchantCommitRequestResponse.getTotalPrice(), 3, 3) + " KWD");
                } else {
                    paymentamount_linear.setVisibility(View.GONE);
                    horizontal3.setVisibility(View.GONE);
                }

                if (payToMerchantCommitRequestResponse.getDiscountPrice() != 0) {
                    left4.setText(getResources().getString(R.string.txn_history_discount));
                    right4.setText("" + PriceFormatter.format(payToMerchantCommitRequestResponse.getDiscountPrice(), 3, 3) + " KWD");
                } else {
                    processingfee_linear.setVisibility(View.GONE);
                    horizontal4.setVisibility(View.GONE);
                }

                if (payToMerchantCommitRequestResponse.getOfferId() != 0 && payToMerchantCommitRequestResponse.getDiscountPrice() != 0) {
                    left5.setText(getResources().getString(R.string.txn_history_amount_paid));
                } else {
                    left5.setText(getResources().getString(R.string.txn_history_payment_amount_txt));
                }
                String payment_amount = PriceFormatter.format(payToMerchantCommitRequestResponse.getTxnAmount(), 3, 3);
                right5.setText(payment_amount + " KWD");
                wallet_balance_right5.setText(PriceFormatter.format(payToMerchantCommitRequestResponse.getBalance(), 3, 3));
                if (payToMerchantCommitRequestResponse.getG_status_description().equalsIgnoreCase("Cancelled by user")) {
                    left7.setText(getString(R.string.status));
                    right7.setText(getResources().getString(R.string.txn_cancelled_merchant));
                    horizontal2.setVisibility(View.GONE);
                } else {
                    left7.setText(getString(R.string.status));
                    right7.setText(payToMerchantCommitRequestResponse.getG_status_description());
                    horizontal2.setVisibility(View.GONE);
                }

                //BPoints START
                bpoints_horizontal5 = (View) findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.VISIBLE);
                bpoints_linear = (LinearLayout) findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.VISIBLE);

                bpoints_right = (TextView) findViewById(R.id.bpoints_right);

//                bpoints = payToMerchantCommitRequestResponse.getTotalPrice() * 5;

//                bpoints =   BigDecimal.valueOf(new Double(payToMerchantCommitRequestResponse.getTotalPrice())).multiply(new BigDecimal("5"));

//                 bpoints =  new BigDecimal(Double.toString(payToMerchantCommitRequestResponse.getTotalPrice())).multiply(new BigDecimal("5"));

                 bpoints =   BigDecimal.valueOf(payToMerchantCommitRequestResponse.getTotalPrice()).multiply(new BigDecimal("5"));


//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
//                bpoints_right.setText("" + df.format(bpoints));


                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);
                bpoints_right.setText("" + String.valueOf(bpoints));

                //BPoints END


                //BPoints balace START

                if (payToMerchantCommitRequestResponse.getTotalRedemptionPoints() != null) {
                    View bpoints_horizontal6 = (View) findViewById(R.id.bpoints_horizontal6);
                    bpoints_horizontal6.setVisibility(View.VISIBLE);
                    LinearLayout bpoints_linear6 = (LinearLayout) findViewById(R.id.bpoints_linear6);
                    bpoints_linear6.setVisibility(View.VISIBLE);

                    TextView bpoints_total_right = (TextView) findViewById(R.id.bpoints_total_right);

                    bpoints_total_right.setText("" + payToMerchantCommitRequestResponse.getTotalRedemptionPoints());
                }

                //BPoints balace END


                transactionid_linear.setVisibility(View.GONE);
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(payToMerchantCommitRequestResponse.getServerTime())));
                right9.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);

                //newly added
                if (payToMerchantCommitRequestResponse.getBranch() != "" && payToMerchantCommitRequestResponse.getBranch() != null) {
                    branch_linear.setVisibility(View.VISIBLE);
                    branch_hor_view.setVisibility(View.VISIBLE);
                    branch_right.setText(payToMerchantCommitRequestResponse.getBranch());
                } else {
                    branch_linear.setVisibility(View.GONE);
                    branch_hor_view.setVisibility(View.GONE);
                }
                break;
            case P2P_RECEIVED:
                SendMoneyIncomingResponse sendMoneyIncomingResponse = (SendMoneyIncomingResponse) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                left0.setText(getResources().getString(R.string.txn_sender_number));
                right0.setText(sendMoneyIncomingResponse.getSenderMobileNumber());
                if (sendMoneyIncomingResponse.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                } else if (sendMoneyIncomingResponse.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                }
                horizontal1.setVisibility(View.GONE);
                status_linear.setVisibility(View.GONE);
                right2.setText(sendMoneyIncomingResponse.getTransactionId());
                horizontal4.setVisibility(View.GONE);
                processingfee_linear.setVisibility(View.GONE);
                horizontal5.setVisibility(View.GONE);
                balanceafter_linear.setVisibility(View.GONE);
                wallet_balance_left5.setVisibility(View.GONE);
                wallet_balance_horizontal5.setVisibility(View.GONE);
                wallet_balance_right5.setVisibility(View.GONE);
                wallet_balance_vertical5.setVisibility(View.GONE);
                left6.setText(getResources().getString(R.string.txn_sender_name));
                right6.setText(sendMoneyIncomingResponse.getSenderName());
                total_amount_linear.setVisibility(View.GONE);
                right7.setText(PriceFormatter.format(sendMoneyIncomingResponse.getBalanceAfter(), 3, 3));
                left7.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                customer_id_linear.setVisibility(View.VISIBLE);
                right3.setText(PriceFormatter.format(sendMoneyIncomingResponse.getTxnAmount(), 3, 3));
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyIncomingResponse.getServerTime())));
                loadtype_linear.setVisibility(View.GONE);
                horizontal8.setVisibility(View.GONE);
                branch_hor_view.setVisibility(View.GONE);






                break;
            case LOAD_MONEY:
                sendMoneyIncomingResponse = (SendMoneyIncomingResponse) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                right9.setText(sendMoneyIncomingResponse.getG_response_trans_type());
                right0.setVisibility(View.GONE);
                left0.setVisibility(View.GONE);
                vertical0.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                number_linear.setVisibility(View.GONE);
                if (sendMoneyIncomingResponse.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                } else if (sendMoneyIncomingResponse.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                }
                right1.setVisibility(View.GONE);
                left1.setVisibility(View.GONE);
                vertical1.setVisibility(View.GONE);
                horizontal1.setVisibility(View.GONE);
                status_linear.setVisibility(View.GONE);
                right2.setText(sendMoneyIncomingResponse.getTransactionId());
                total_amount_linear.setVisibility(View.GONE);
                right3.setText(PriceFormatter.format(sendMoneyIncomingResponse.getTxnAmount(), 3, 3));
                right4.setVisibility(View.GONE);
                left4.setVisibility(View.GONE);
                vertical4.setVisibility(View.GONE);
                horizontal4.setVisibility(View.GONE);
                processingfee_linear.setVisibility(View.GONE);
                right5.setVisibility(View.GONE);
                left5.setVisibility(View.GONE);
                vertical5.setVisibility(View.GONE);
                horizontal5.setVisibility(View.GONE);
                wallet_balance_right5.setText(PriceFormatter.format(sendMoneyIncomingResponse.getBalanceAfter(), 3, 3));
                right6.setVisibility(View.GONE);
                left6.setVisibility(View.GONE);
                vertical6.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);
                right0.setVisibility(View.GONE);
                receiptentname_linear.setVisibility(View.GONE);
                right7.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                right0.setVisibility(View.GONE);
                customer_id_linear.setVisibility(View.GONE);
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyIncomingResponse.getServerTime())));


                horizontal1.setVisibility(View.GONE);
                branch_hor_view.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);

                break;
            case ECOMMERCE_TRAN_RESPONSE:
                DomesticRechargeTranHistoryResponsePojo ecommerce = (DomesticRechargeTranHistoryResponsePojo) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                if (ecommerce.getG_status() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right1.setText(ecommerce.getG_status_description());
                } else if (ecommerce.getG_status() == -1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right1.setText(getResources().getString(R.string.txn_failure));
                } else if (ecommerce.getTranType().equals("ssss")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    right1.setText(getResources().getString(R.string.txn_txn_under_process));
                }

                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                right0.setVisibility(View.GONE);
                left0.setVisibility(View.GONE);
                vertical0.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                number_linear.setVisibility(View.GONE);
                loadtype_linear.setVisibility(View.GONE);
                right1.setVisibility(View.VISIBLE);
                left1.setVisibility(View.VISIBLE);
                vertical1.setVisibility(View.VISIBLE);
                horizontal1.setVisibility(View.VISIBLE);
                status_linear.setVisibility(View.VISIBLE);
                right1.setText(ecommerce.getG_status_description());
                left2.setText(getResources().getString(R.string.txn_ref_no));
                right2.setText(ecommerce.getTransactionId());
                total_amount_linear.setVisibility(View.GONE);
                left3.setText(getResources().getString(R.string.txn_amount_kwd));
                right3.setText(PriceFormatter.format(ecommerce.getDenominationinKWD(), 3, 3));
                right4.setVisibility(View.VISIBLE);
                left4.setVisibility(View.VISIBLE);
                left4.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                right4.setText(PriceFormatter.format(ecommerce.getSenderbalanceAfeter(), 3, 3));
                vertical4.setVisibility(View.VISIBLE);
                horizontal4.setVisibility(View.GONE);
                processingfee_linear.setVisibility(View.VISIBLE);
                right3.setText(PriceFormatter.format(ecommerce.getDenominationinKWD(), 3, 3));
                right5.setVisibility(View.GONE);
                left5.setVisibility(View.GONE);
                vertical5.setVisibility(View.GONE);
                horizontal5.setVisibility(View.GONE);
                walletbalance_linear.setVisibility(View.GONE);
                right6.setVisibility(View.GONE);
                left6.setVisibility(View.GONE);
                vertical6.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);
                right0.setVisibility(View.GONE);
                receiptentname_linear.setVisibility(View.GONE);
                right7.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                right0.setVisibility(View.GONE);
                customer_id_linear.setVisibility(View.GONE);
                horizontal8.setVisibility(View.GONE);
                right8.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(ecommerce.getServerTime())));




                //Merchant name display
                View  ecom_mer_name_hor_view = (View) findViewById(R.id.ecom_mer_name_hor_view);
                ecom_mer_name_hor_view.setVisibility(View.GONE);
                LinearLayout ecom_mer_name_linearlayout = (LinearLayout) findViewById(R.id.ecom_mer_name_linearlayout);
                ecom_mer_name_linearlayout.setVisibility(View.VISIBLE);
                TextView ecom_mer_name_right = (TextView) findViewById(R.id.ecom_mer_name_right);
                ecom_mer_name_right.setText(ecommerce.getOperatorName());

//                View branch_hor_view =  (View)findViewById(R.id.branch_hor_view);
                branch_hor_view.setVisibility(View.VISIBLE);

                //BPoints display
                View  ecom_bpoints_view = (View) findViewById(R.id.ecom_bpoints_view);
                ecom_bpoints_view.setVisibility(View.VISIBLE);
                LinearLayout ecom_bpoints_linear = (LinearLayout) findViewById(R.id.ecom_bpoints_linear);
                ecom_bpoints_linear.setVisibility(View.VISIBLE);
                TextView ecom_bpoints_right = (TextView) findViewById(R.id.ecom_bpoints_right);



                bpoints =   BigDecimal.valueOf(ecommerce.getDenominationinKWD()).multiply(new BigDecimal("5"));
                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);
                ecom_bpoints_right.setText("" + String.valueOf(bpoints));



                //BPoints balance display
                View  ecom_bpoints_balance_view = (View) findViewById(R.id.ecom_bpoints_balance_view);
                ecom_bpoints_balance_view.setVisibility(View.VISIBLE);
                LinearLayout ecom_bpoints_balance_linear = (LinearLayout) findViewById(R.id.ecom_bpoints_balance_linear);
                ecom_bpoints_balance_linear.setVisibility(View.VISIBLE);
                TextView ecom_bpoints_balance_right = (TextView) findViewById(R.id.ecom_bpoints_balance_right);


                ecom_bpoints_balance_right.setText(""+String.valueOf(ecommerce.getTotalRedemptionPoints()));

                break;
            case PREPAID_REQUESTCARD_RESPONSE:
                TransactionalHistoryForInternationalRecharge transactionalHistoryForInternationalRecharge = (TransactionalHistoryForInternationalRecharge) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                left0.setText(getResources().getString(R.string.txn_history_mobilenumber_txt));
                right0.setText(transactionalHistoryForInternationalRecharge.getRecipientMobileNumber());
                left1.setText(getResources().getString(R.string.txn_history_status_txt));
                if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("ssss")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                }
                if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                    right1.setText(getResources().getString(R.string.txn_success));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                    right1.setText(getResources().getString(R.string.txn_failure));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                    right1.setText(getResources().getString(R.string.txn_initiated));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("ssss")) {
                    right1.setText(getResources().getString(R.string.txn_tup));
                }
                left6.setText(getResources().getString(R.string.txn_product));
                right6.setText(transactionalHistoryForInternationalRecharge.getProviderName());
                left4.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                right4.setText(PriceFormatter.format(transactionalHistoryForInternationalRecharge.getSenderbalanceAfeter(), 3, 3));
                left3.setText(getResources().getString(R.string.txn_topup_amount_kwd));
                right3.setText(PriceFormatter.format(transactionalHistoryForInternationalRecharge.getDenominationinKWD(), 3, 3));
                left5.setText(getResources().getString(R.string.txn_history_txn_id_txt));
                right5.setText(transactionalHistoryForInternationalRecharge.getTransactionId());
                left2.setText(getResources().getString(R.string.txn_history_time_txt));
                right2.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(transactionalHistoryForInternationalRecharge.getServerTime())));
                wallet_balance_left5.setVisibility(View.GONE);
                wallet_balance_vertical5.setVisibility(View.GONE);
                wallet_balance_right5.setVisibility(View.GONE);
                wallet_balance_horizontal5.setVisibility(View.GONE);
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                right7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                left8.setVisibility(View.GONE);
                vertical8.setVisibility(View.GONE);
                right8.setVisibility(View.GONE);
                horizontal8.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                right9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);


                //BPoints START
                bpoints_horizontal5 = (View) findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.VISIBLE);
                bpoints_linear = (LinearLayout) findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.VISIBLE);
                bpoints_right = (TextView) findViewById(R.id.bpoints_right);


//                bpoints = transactionalHistoryForInternationalRecharge.getDenominationinKWD() * 5;

                bpoints =   BigDecimal.valueOf(transactionalHistoryForInternationalRecharge.getDenominationinKWD()).multiply(new BigDecimal("5"));

                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                bpoints_right.setText("" + String.valueOf(bpoints));
                //BPoints END


                //Bpoint balance START
                if (transactionalHistoryForInternationalRecharge.getTotalRedemptionPoints() != null){
                    View bpoints_balance_horizontal6 = (View) findViewById(R.id.bpoints_balance_horizontal6);
                bpoints_balance_horizontal6.setVisibility(View.VISIBLE);
                LinearLayout bpoints_balance_linear = (LinearLayout) findViewById(R.id.bpoints_balance_linear);
                bpoints_balance_linear.setVisibility(View.VISIBLE);

                TextView bpoints_balance_right = (TextView) findViewById(R.id.bpoints_balance_right);
                bpoints_balance_right.setText("" + transactionalHistoryForInternationalRecharge.getTotalRedemptionPoints());
        }

                //Bpoint balance END

                break;
            case INTERNATIONAL_RECHARGE_L2_RESPONSE:
                transactionalHistoryForInternationalRecharge = (TransactionalHistoryForInternationalRecharge) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);
                left0.setText(getResources().getString(R.string.txn_history_mobilenumber_txt));
                right0.setText(transactionalHistoryForInternationalRecharge.getRecipientMobileNumber());
                left1.setText(getResources().getString(R.string.txn_history_status_txt));
                if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("ssss")) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                }
                if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                    right1.setText(getResources().getString(R.string.txn_success));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                    right1.setText(getResources().getString(R.string.txn_failure));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                    right1.setText(getResources().getString(R.string.txn_initiated));
                } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("ssss")) {
                    right1.setText(getResources().getString(R.string.txn_tup));
                }
                left6.setText(getResources().getString(R.string.txn_product));
                right6.setText(transactionalHistoryForInternationalRecharge.getProviderName());
                left3.setText(getResources().getString(R.string.txn_topup_local_currency));

                right3.setText(transactionalHistoryForInternationalRecharge.getLocalCurrency() + " " + PriceFormatter.format(Double.parseDouble(transactionalHistoryForInternationalRecharge.getDenominationAmt()), 2, 2));
                left4.setText(getResources().getString(R.string.txn_topup_amount_kwd));
                right4.setText(PriceFormatter.format(transactionalHistoryForInternationalRecharge.getDenominationinKWD(), 3, 3));
                left5.setText(getResources().getString(R.string.txn_history_txn_id_txt));
                right5.setText(transactionalHistoryForInternationalRecharge.getTransactionId());
                left2.setText(getResources().getString(R.string.txn_history_time_txt));
                right2.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(transactionalHistoryForInternationalRecharge.getServerTime())));
                wallet_balance_left5.setVisibility(View.VISIBLE);
                wallet_balance_left5.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                wallet_balance_right5.setVisibility(View.VISIBLE);
                wallet_balance_right5.setText(PriceFormatter.format(transactionalHistoryForInternationalRecharge.getSenderbalanceAfeter(), 3, 3));
                left7.setVisibility(View.GONE);
                vertical7.setVisibility(View.GONE);
                right7.setVisibility(View.GONE);
                horizontal7.setVisibility(View.GONE);
                left8.setVisibility(View.GONE);
                vertical8.setVisibility(View.GONE);
                right8.setVisibility(View.GONE);
                horizontal8.setVisibility(View.GONE);
                left9.setVisibility(View.GONE);
                vertical9.setVisibility(View.GONE);
                right9.setVisibility(View.GONE);
                horizontal9.setVisibility(View.GONE);

                //BPoints START
                bpoints_horizontal5 = (View)findViewById(R.id.bpoints_horizontal5);
                bpoints_horizontal5.setVisibility(View.VISIBLE);
                bpoints_linear = (LinearLayout)findViewById(R.id.bpoints_linear);
                bpoints_linear.setVisibility(View.VISIBLE);
                bpoints_right = (TextView)findViewById(R.id.bpoints_right) ;


//                bpoints =  transactionalHistoryForInternationalRecharge.getDenominationinKWD()*5;

                bpoints =   BigDecimal.valueOf(transactionalHistoryForInternationalRecharge.getDenominationinKWD()).multiply(new BigDecimal("5"));

                bpoints = bpoints.setScale(3, BigDecimal.ROUND_DOWN);

//                df = new DecimalFormat("#.###");
//                df.setRoundingMode(RoundingMode.CEILING);
                bpoints_right.setText(""+String.valueOf(bpoints));
                //BPoints END



                //Bpoint balance START

                if(transactionalHistoryForInternationalRecharge.getTotalRedemptionPoints()!=null) {
                    View bpoints_balance_horizontal6 = (View) findViewById(R.id.bpoints_balance_horizontal6);
                    bpoints_balance_horizontal6.setVisibility(View.VISIBLE);
                    LinearLayout bpoints_balance_linear = (LinearLayout) findViewById(R.id.bpoints_balance_linear);
                    bpoints_balance_linear.setVisibility(View.VISIBLE);

                    TextView bpoints_balance_right = (TextView) findViewById(R.id.bpoints_balance_right);
                    bpoints_balance_right.setText("" + transactionalHistoryForInternationalRecharge.getTotalRedemptionPoints());
                }
                //Bpoint balance END

                break;


            case CASHBACK:
                invoice = (DomesticRechargeTranHistoryResponsePojo) transTypeInterface;
                ooredoo_transaction_screen_time_txt.setVisibility(View.INVISIBLE);

                extra_linear.setVisibility(View.VISIBLE);

                horizontal11.setVisibility(View.VISIBLE);

                if (invoice.getPaymentStatus() == 1) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.hold_icon));
                    right11.setText(getResources().getString(R.string.txn_hold));
                } else if (invoice.getPaymentStatus() == 2) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right11.setText(getResources().getString(R.string.txn_success));
                } else if (invoice.getPaymentStatus() == 3) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right11.setText(getResources().getString(R.string.txn_rejected));
                } else if (invoice.getPaymentStatus() == 4) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    right11.setText(getResources().getString(R.string.txn_failed));
                }else if (invoice.getPaymentStatus() == 0) {
                    ooredoo_bill_payment_img.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    right11.setText(getResources().getString(R.string.txn_success));
                }


                if (!invoice.getOfferDescription().isEmpty()) {
                    processingfee_linear.setVisibility(View.VISIBLE);
                    left4.setVisibility(View.VISIBLE);
                    right4.setVisibility(View.VISIBLE);
                    vertical4.setVisibility(View.VISIBLE);
                    horizontal4.setVisibility(View.VISIBLE);
                    left4.setText(getResources().getString(R.string.txn_offer_id));
                    right4.setText("" + invoice.getOfferDescription());
                } else {
                    processingfee_linear.setVisibility(View.GONE);
                    left4.setVisibility(View.GONE);
                    right4.setVisibility(View.GONE);
                    vertical4.setVisibility(View.GONE);
                    horizontal4.setVisibility(View.GONE);
                }
                if (invoice.getDiscountPrice() != 0) {
                    walletbalance_linear.setVisibility(View.VISIBLE);
                    wallet_balance_left5.setVisibility(View.VISIBLE);
                    wallet_balance_vertical5.setVisibility(View.VISIBLE);
                    wallet_balance_right5.setVisibility(View.VISIBLE);
                    wallet_balance_horizontal5.setVisibility(View.VISIBLE);
                    wallet_balance_left5.setText(getResources().getString(R.string.txn_discount_amt));
                    wallet_balance_right5.setText(PriceFormatter.format(invoice.getDiscountPrice(), 3, 3) + " KWD");
                } else {
                    walletbalance_linear.setVisibility(View.GONE);
                    wallet_balance_left5.setVisibility(View.GONE);
                    wallet_balance_vertical5.setVisibility(View.GONE);
                    wallet_balance_right5.setVisibility(View.GONE);
                    wallet_balance_horizontal5.setVisibility(View.VISIBLE);
                }


                left11.setText(getString(R.string.txn_history_tranasaction_type_txt));
                right11.setText("BpointsCashback");


                status_linear.setVisibility(View.GONE);
                number_linear.setVisibility(View.GONE);
                receiptentname_linear.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                txntype_hor_view.setVisibility(View.VISIBLE);
                txntype_linear.setVisibility(View.GONE);
                txntype_left.setText(getResources().getString(R.string.txn_mercahnt_name));
                txntype_right.setText(invoice.getOperatorName());
                left2.setText(getResources().getString(R.string.txn_history_after_balance_txt));
                right2.setText(PriceFormatter.format(invoice.getSenderbalanceAfeter(), 3, 3) + " KWD");

                horizontal1.setVisibility(View.GONE);
                left3.setText(getResources().getString(R.string.mobilenumber));
                right3.setText(invoice.getRecipientMobileNumber());

//                left5.setText(getResources().getString(R.string.txn_amount_kwd));

                left5.setText(getResources().getString(R.string.cashback_amount));

                right5.setText(PriceFormatter.format((invoice.getTotalPrice()), 3, 3) + " KWD");

                //offers module
                if (invoice.getRechargeAmt() != "" && invoice.getRechargeAmt() != null) {
                    customer_id_linear.setVisibility(View.VISIBLE);
                    right7.setVisibility(View.VISIBLE);
                    left7.setVisibility(View.VISIBLE);
                    vertical7.setVisibility(View.VISIBLE);
                    horizontal7.setVisibility(View.VISIBLE);
                    if (invoice.getPaymentStatus() == 2) {
                        left7.setText(getResources().getString(R.string.txn_amount_paid));
                    } else {
                        left7.setText(getResources().getString(R.string.txn_amount_pay));


                        //For BPoints
                        left7.setText(getString(R.string.before_balance));
                    }
                    right7.setText(PriceFormatter.format(Double.parseDouble(invoice.getRechargeAmt()), 3, 3) + " KWD");
                } else {
                    customer_id_linear.setVisibility(View.GONE);
                    right7.setVisibility(View.GONE);
                    left7.setVisibility(View.GONE);
                    vertical7.setVisibility(View.GONE);
                    horizontal7.setVisibility(View.GONE);
                }

                //for branch
                if (invoice.getBranch() != "" && invoice.getBranch() != null) {
                    branch_linear.setVisibility(View.VISIBLE);
                    branch_hor_view.setVisibility(View.VISIBLE);
                    branch_right.setText(invoice.getBranch());
                } else {
                    branch_linear.setVisibility(View.GONE);
                    branch_hor_view.setVisibility(View.GONE);
                }


                left8.setText(getResources().getString(R.string.txn_history_txn_id_txt));
                right8.setText(invoice.getTransactionId());

                left9.setText(getResources().getString(R.string.txn_history_time_txt));
                right9.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(invoice.getServerTime())));





                //BPoints before redemption START
                View tv_after_cashback_points_before_view = (View)findViewById(R.id.tv_after_cashback_points_before_view);
                tv_after_cashback_points_before_view.setVisibility(View.GONE);
                LinearLayout tv_after_cashback_points_before_linear = (LinearLayout)findViewById(R.id.tv_after_cashback_points_before_linear);
                tv_after_cashback_points_before_linear.setVisibility(View.VISIBLE);
                TextView tv_cashback_points_before_redemption = (TextView)findViewById(R.id.tv_cashback_points_before_redemption) ;
                tv_cashback_points_before_redemption.setText(""+invoice.getBeforeCashBackPoints());

                //BPoints before redemption END


                //BPoints after redemption START
                View tv_after_cashback_points_after_view = (View)findViewById(R.id.tv_after_cashback_points_after_view);
                tv_after_cashback_points_after_view.setVisibility(View.VISIBLE);
                LinearLayout tv_after_cashback_points_after_linear = (LinearLayout)findViewById(R.id.tv_after_cashback_points_after_linear);
                tv_after_cashback_points_after_linear.setVisibility(View.VISIBLE);
                TextView tv_after_cashback_points_redemption = (TextView)findViewById(R.id.tv_after_cashback_points_redemption) ;
                tv_after_cashback_points_redemption.setText(""+invoice.getAfterCashBackPoints());

                //BPoints after redemption END


                branch_hor_view.setVisibility(View.GONE);
                horizontal1.setVisibility(View.GONE);
                horizontal0.setVisibility(View.GONE);
                horizontal6.setVisibility(View.GONE);
                horizontal3.setVisibility(View.VISIBLE);
                txntype_hor_view.setVisibility(View.GONE);

                break;
            default:
                break;
        }
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionHistoryDisplayActivity.this.finish();
            }
        });
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}