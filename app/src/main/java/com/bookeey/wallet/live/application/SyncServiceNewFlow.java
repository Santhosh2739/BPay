package com.bookeey.wallet.live.application;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.bookeey.wallet.live.forceface.TransTypeInterface;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import coreframework.network.ServerConnection;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.Internationaltopup.TransactionalHistoryForInternationalRecharge;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.incomingmoney.SendMoneyIncomingResponse;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticRechargeTranHistoryResponsePojo;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantCommitRequestResponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyCommitRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyRequestResponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryRequest;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;
import ycash.wallet.json.pojo.userinfo.UserInfoResponse;
import ycash.wallet.json.pojo.wheretopay.ImageLogoRequest;
import ycash.wallet.json.pojo.wheretopay.MerLogoResponse;

public class SyncServiceNewFlow extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static final String TAG = "SyncServiceNewFlow";
    public static final int TYPE_TRANS_HIST = 1;
    public static final int TYPE_MERCHANT_LIST = 3;
    public static final int TYPE_MERCHANT_LOGO = 4;
    public static final int TYPE_USER_LOGGED_IN_STATUS = 2;
    public static final int TYPE_USER_BARCODE_CLOSE = 5;
    public static final String MY_ACTION = "MY_ACTION";

    public static final int STATUS_OUT = 6;

    public SyncServiceNewFlow() {
        super(SyncServiceNewFlow.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Service Started!");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        int type = intent.getIntExtra("type", -1);
        String tranType = intent.getStringExtra("tranType");

        Bundle bundle = new Bundle();
        if (type == -1) {
            //SEND ERROR HERE
            bundle.putString(Intent.EXTRA_TEXT, "INVALID REQUEST TYPE");
            receiver.send(STATUS_ERROR, bundle);
            return;
        }
        if (type == TYPE_TRANS_HIST) {
            refreshTransactionHistory(intent, type, tranType);
        } else if (type == TYPE_USER_LOGGED_IN_STATUS) {
//            checkUserLoggedInStatus(intent);
        } else if (type == TYPE_MERCHANT_LOGO) {
            refreshLogo(intent, type);
        } else {
            bundle.putString(Intent.EXTRA_TEXT, "REQUEST TYPE NOT SUPPORTED");
            receiver.send(STATUS_ERROR, bundle);
        }
    }


    private void refreshLogo(Intent intent, int type) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        String ref_number = null;
        if (intent.hasExtra("mlia_reference_number")) {
            ref_number = intent.getStringExtra("mlia_reference_number");
        }
        int position = intent.getIntExtra("mlia_position", -1);
        if (ref_number == null || position == -1) {
            bundle.putString(Intent.EXTRA_TEXT, "ref number & position required");
            receiver.send(STATUS_ERROR, bundle);
            return;
        }
        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        ImageLogoRequest imageLogoRequest = new ImageLogoRequest();
        imageLogoRequest.setMerchantRefNumber(ref_number);
        imageLogoRequest.setG_transType(TransType.WHERE_TO_PAY_LOGO_REQUEST.name());
        imageLogoRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        String json = new Gson().toJson(imageLogoRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_LOGO_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        Message msg = ServerConnection.directNonHandlerHTTPClient(buffer.toString(), -1);
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            try {
                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                if (null != response && response.getG_status() == 1) {
                    MerLogoResponse logoResponse = new Gson().fromJson((String) msg.obj, MerLogoResponse.class);
                    bundle.putInt("status", msg.arg1);
                    bundle.putParcelable("image", logoResponse);
                    bundle.putInt("mlia_position", position);
                    bundle.putString("mlia_reference_number", ref_number);
                    receiver.send(STATUS_FINISHED, bundle);
                } else {
                    bundle.putInt("mlia_position", position);
                    bundle.putString("mlia_reference_number", ref_number);
                    bundle.putInt("status", ServerConnection.OPERATION_FAILURE_GENERAL_SERVER);
                    bundle.putString(Intent.EXTRA_TEXT, response.getG_errorDescription());
                    receiver.send(STATUS_ERROR, bundle);
                }
            } catch (Exception e) {
                bundle.putInt("status", ServerConnection.OPERATION_FAILURE_GENERAL_SERVER);
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            bundle.putInt("status", msg.arg1);
            bundle.putString(Intent.EXTRA_TEXT, "SERVER ERROR");
            receiver.send(STATUS_ERROR, bundle);
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            bundle.putInt("status", msg.arg1);
            bundle.putString(Intent.EXTRA_TEXT, "NETWORK ERROR");
            receiver.send(STATUS_ERROR, bundle);
        }
    }


    private void refreshTransactionHistory(Intent intent, int typeOfService, String tranType) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle bundle = new Bundle();
        bundle.putInt("type", typeOfService);
        long from = intent.getLongExtra("from", -1L);
        if (from == -1L) {
            bundle.putString(Intent.EXTRA_TEXT, "FROM REQUIRED");
            receiver.send(STATUS_ERROR, bundle);
            return;
        }
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest();
        transactionHistoryRequest.setG_transType(TransType.GUEST_TRAN_HISTORY_REQUEST.name());
//        transactionHistoryRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        transactionHistoryRequest.setFrom((int) from);
        transactionHistoryRequest.setTranType(tranType);

        String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();

        //Just for test
//        String deviceID = "875cafcab9098647";
        transactionHistoryRequest.setDeviceIdNumber(deviceID);


        String json = new Gson().toJson(transactionHistoryRequest);

        Log.e("NewFlow RData","History:"+json);

        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.GUEST_TRAN_HISTORY_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));

        Log.e("NewFlow URL","History:"+buffer.toString());


        Message msg = ServerConnection.directNonHandlerHTTPClient(buffer.toString(), -1);
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            try {
                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                if (null != response) {
                    TransactionHistoryResponse res = new TransactionHistoryResponse();
                    JSONObject jsonObject = new JSONObject((String) msg.obj);

                    Log.e("NewFlow Res","History: "+jsonObject);

                    JSONArray jsonArray = jsonObject.getJSONArray("records");
                    if (jsonArray.length() > 0) {
                        ArrayList<TransTypeInterface> all = new ArrayList<TransTypeInterface>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            TransType type = TransType.valueOf(jsonArray.getJSONObject(i).getString("g_response_trans_type"));
                            String string = jsonArray.getJSONObject(i).toString();

                            Log.e("Txn Response: ", " " + string);


                            switch (type) {//P2P received
                                case PAY_TO_MERCHANT_RESPONSE:
                                    all.add(new Gson().fromJson(string, PayToMerchantRequestResponse.class));
                                    break;
                                case PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE:

                                    Log.e("P2M->: ", " " + string);

                                    all.add(new Gson().fromJson(string, PayToMerchantCommitRequestResponse.class));
                                    break;
                                case DOMESTIC_RECHARGE_TRAN_RESPONSE:
                                    all.add(new Gson().fromJson(string, DomesticRechargeTranHistoryResponsePojo.class));
                                    break;
                                case VOCHER_RECHARGE_TRAN_RESPONSE:
                                    all.add(new Gson().fromJson(string, DomesticRechargeTranHistoryResponsePojo.class));
                                    break;
                                case SEND_MONEY_REQUEST_RESPONSE:
                                    all.add(new Gson().fromJson(string, SendMoneyRequestResponse.class));
                                    break;
                                case SEND_MONEY_COMMIT_REQUEST_RESPONSE:
                                    all.add(new Gson().fromJson(string, SendMoneyCommitRequestResponse.class));
                                    break;
                                case P2P_RECEIVED:
                                    all.add(new Gson().fromJson(string, SendMoneyIncomingResponse.class));
                                    break;
                                case LOAD_MONEY:
                                    all.add(new Gson().fromJson(string, SendMoneyIncomingResponse.class));
                                    break;
                                case INTERNATIONAL_RECHARGE_L2_RESPONSE:
                                    all.add(new Gson().fromJson(string, TransactionalHistoryForInternationalRecharge.class));
                                    break;
                                case PREPAID_REQUESTCARD_RESPONSE:
                                    all.add(new Gson().fromJson(string, TransactionalHistoryForInternationalRecharge.class));
                                    break;
                                case ECOMMERCE_TRAN_RESPONSE:
                                    all.add(new Gson().fromJson(string, DomesticRechargeTranHistoryResponsePojo.class));
                                    break;
                                case INVOICE_TRAN_RESPONSE:
                                    all.add(new Gson().fromJson(string, DomesticRechargeTranHistoryResponsePojo.class));
                                    break;
                                case CASHBACK:
                                    all.add(new Gson().fromJson(string, DomesticRechargeTranHistoryResponsePojo.class));
                                    break;
                                default:
                                    all.add(new Gson().fromJson(string, GenericResponse.class));
                                    break;
                            }
                        }
                        res.setRecords(all);
                        res.setTotalNoOfTransactions(jsonObject.getLong("totalNoOfTransactions"));
                        res.setTo(jsonObject.getLong("to"));
                        res.setFrom(jsonObject.getLong("from"));
                        TransactionHistoryResponse coreResponse = ((CoreApplication) getApplicationContext()).getTransactionHistoryResponse();
                        if (coreResponse.getRecords().size() == 0) {
                            //BLANKET OVERWRITE
                            ((CoreApplication) getApplicationContext()).setTransactionHistoryResponse(res);
                        } else {
                            //CONTENT ADDITION
                            coreResponse.getRecords().addAll(all);
                            coreResponse.setFrom(res.getFrom());
                            coreResponse.setTo(res.getTo());
                            coreResponse.setTotalNoOfTransactions(res.getTotalNoOfTransactions());
                        }
                        bundle.putInt("status", msg.arg1);
                        receiver.send(STATUS_FINISHED, bundle);
                    }
                }
            } catch (Exception e) {
                bundle.putInt("status", ServerConnection.OPERATION_FAILURE_GENERAL_SERVER);
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            bundle.putInt("status", msg.arg1);
            bundle.putString(Intent.EXTRA_TEXT, "SERVER ERROR");
            receiver.send(STATUS_ERROR, bundle);
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            bundle.putInt("status", msg.arg1);
            bundle.putString(Intent.EXTRA_TEXT, "NETWORK ERROR");
            receiver.send(STATUS_ERROR, bundle);
        }
    }

    void checkUserLoggedInStatus(Intent intent) {
        final DownloadResultReceiver receiver1;
        receiver1 = new DownloadResultReceiver(new Handler());
        Bundle bundle1 = new Bundle();
        CoreApplication application = (CoreApplication) getApplicationContext();
        CustomerLoginRequestReponse onlineLoginResponse = application.getCustomerLoginRequestReponse();
        if (application.isUserLoggedIn()) {
            GenericRequest genericRequest = new GenericRequest();
            genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
            genericRequest.setG_transType(TransType.USER_INFO_REQUEST.name());
            genericRequest.setG_security_counter(100);
            StringBuffer buffer = new StringBuffer();
            buffer.append(TransType.USER_INFO_REQUEST.getURL());
            buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(genericRequest)));
            Message msg = ServerConnection.directNonHandlerHTTPClient(buffer.toString(), -1);
            if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                boolean isSessionInvalid = false;
                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                if (response != null && (response.getG_errorDescription().trim().equalsIgnoreCase("E116") || response.getG_errorDescription().trim().equalsIgnoreCase("E117"))) {
                    isSessionInvalid = true;
                } else if (null != response && response.getG_response_trans_type().equalsIgnoreCase(TransType.USER_INFO_RESPONSE.name()) && response.getG_status() == 1) {
                    UserInfoResponse userInfoResponse = new Gson().fromJson((String) msg.obj, UserInfoResponse.class);
                    application.setUserInfoResponse(userInfoResponse);
                    application.getCustomerLoginRequestReponse().setWalletBalance(userInfoResponse.getBalance());
                    //application.setCustomerLoginRequestReponse(new Gson().fromJson((String) msg.obj, CustomerLoginRequestReponse.class));
                    application.setInvoices_count(userInfoResponse.getDueInvoiceCount());
                    application.setBannerDetails(userInfoResponse.getBannerDetails());
                    application.setSpeakstatus(userInfoResponse.isSpeakstatus());
                    application.setBpoints(userInfoResponse.getBpoints());
                    Intent intent_local_bc = new Intent("custom-event-profile-update");
                    intent_local_bc.putExtra("message", "This is my message!");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent_local_bc);
                } else {
                    isSessionInvalid = true;
                }
                final String errorDesc = response.getG_errorDescription();

                if (isSessionInvalid) {

                    Log.e("SyncKill", "Called");

                    application.setIsUserLoggedIn(false);
                    application.setCustomerLoginRequestReponse(new CustomerLoginRequestReponse());

                    Intent i = new Intent(SyncServiceNewFlow.this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("exit", Boolean.TRUE);
                    i.putExtra("session_expired", errorDesc);
                    startActivity(i);

/*
                    Intent i = new Intent();
                    intent.setAction(MY_ACTION);

                    intent.putExtra("DATAPASSED", response.getG_errorDescription());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(i);*/

                    //sendBroadcast(i);


                   /* Intent i = new Intent(this, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("exit", Boolean.TRUE);
                    startActivity(i);*/
//                    bundle1.putString("session expired", response.getG_errorDescription());
//                    i.putExtra("session expired", response.getG_errorDescription());
////                    receiver1.send(STATUS_OUT, bundle1);


//                    sendBroadcast(i);
                    // stopSelf();

//                    startActivity(i);
                }
            }
        } else {
            ((CoreApplication) getApplicationContext()).cancelUserLoggedInStatusAlarmManager();
        }
    }
}

