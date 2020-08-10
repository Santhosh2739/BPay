package newflow;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.application.DownloadResultReceiver;
import com.bookeey.wallet.live.application.SyncServiceNewFlow;
import com.bookeey.wallet.live.forceface.TransTypeInterface;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coreframework.taskframework.GenericListActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import ycash.wallet.json.pojo.Internationaltopup.TransactionalHistoryForInternationalRecharge;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.incomingmoney.SendMoneyIncomingResponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticRechargeTranHistoryResponsePojo;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantCommitRequestResponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyCommitRequestResponse;
import ycash.wallet.json.pojo.sendmoney.SendMoneyRequestResponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;

public class TransactionHistoryActivityNewFlow  extends GenericListActivity implements DownloadResultReceiver.Receiver, YPCHeadlessCallback {
    private Handler handler = new Handler();
    private CustomList adapter = null;
    private DownloadResultReceiver mReceiver = null;
    private View loadMoreView;
    boolean loadingMore = false;
    ImageView merchant_category_screen_wallet_logo_back, home_up_back;
    Spinner transaction_history_spinner;
    boolean selected = false;
    List<String> tranType_list = new ArrayList<>();
    String tranType_str = "select";
    boolean test = false;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txn_history_list);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        transaction_history_spinner = (Spinner) findViewById(R.id.transaction_history_spinner);
        /*getActionBar().setTitle("      TRANSACTION HISTORY");
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);*/

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

        tranType_list.add(getResources().getString(R.string.txn_history_filter));

//        tranType_list.add(getResources().getString(R.string.txn_history_loadwallet));
//        tranType_list.add(getResources().getString(R.string.txn_history_pay2mercahnt));
//        tranType_list.add(getResources().getString(R.string.txn_history_sendp2p));
//        tranType_list.add(getResources().getString(R.string.txn_history_receivep2p));
        tranType_list.add(getResources().getString(R.string.txn_history_prepaidcards));
        tranType_list.add(getResources().getString(R.string.txn_history_mobilebill));
        tranType_list.add(getResources().getString(R.string.txn_history_recharge));
//        tranType_list.add(getResources().getString(R.string.txn_history_invoice));
//        tranType_list.add(getResources().getString(R.string.txn_history_ecommerce));
//        tranType_list.add(getResources().getString(R.string.bpoitns_cashback));


        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ((Button) findViewById(R.id.transaction_history_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getBaseContext(), MainActivityNewFlow.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

//                (Or)

                finish();
            }
        });

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), tranType_list);
        transaction_history_spinner.setAdapter(customAdapter);
        tranType_str = "all";
        requestLoadingOfList(false, tranType_str);
        if (tranType_str.equalsIgnoreCase("all")) {
            selected = true;
        }

        loadMoreView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loadmore, null, false);
        getListView().addFooterView(loadMoreView);
        //venkatesh

        int initialposition = transaction_history_spinner.getSelectedItemPosition();
        transaction_history_spinner.setSelection(initialposition, false);

        //anju
        transaction_history_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //   tranType_str = tranType_list.get(position);
                switch (position) {
                    case 0:
                        tranType_str = "all";
                        break;
                    case 1:
                        tranType_str = "VOCHER";
                        break;
                    case 2:
                        tranType_str = "DOMESTICRECHARGES";
                        break;
                    case 3:
                        tranType_str = "INTL_RECHARGE";
                        break;


//                    case 4:
//                        tranType_str = "P2P_RECEIVED";
//                        break;
//                    case 5:
//                        tranType_str = "VOCHER";
//                        break;
//                    case 6:
//                        tranType_str = "DOMESTICRECHARGES";
//                        break;
//                    case 7:
//                        tranType_str = "INTL_RECHARGE";
//                        break;
//                    case 8:
//                        tranType_str = "INVOICE";
//                        break;
//                    case 9:
//                        tranType_str = "ECOMMERCE";
//                        break;
//                    case 10:
//                        tranType_str = "CASHBACK";
//                        break;


                    default:
                        break;

                }

                TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplication()).getTransactionHistoryResponse();
                transactionHistoryResponse.getRecords().clear();
                adapter.clear();
                adapter.notifyDataSetChanged();
                requestLoadingOfList(false, tranType_str);
                /*if (tranType_str.equalsIgnoreCase("Filter by category"))
                    selected = false;
                else
                    selected = true;*/

                //today
                if (position == 0)
                    selected = true;
                else
                    selected = true;


//                getListView().getOverscrollFooter();
//                getListView().setScrollX(0);
            /*    adapter.clear();
                TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplicationContext()).getTransactionHistoryResponse();
                transactionHistoryResponse.getRecords().clear();
                requestLoadingOfList(false, tranType_str);*/
//                requestLoadingOfList(false, tranType_str);

                //Toast.makeText(getApplicationContext(), tranType_str, Toast.LENGTH_LONG).show();
//                TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplication()).getTransactionHistoryResponse();
//                if (position != 0) {
                // transactionHistoryResponse.setRecords(null);
//                    requestLoadingOfList(false);
//                    test=true;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplication()).getTransactionHistoryResponse();
        adapter = new CustomList((Activity) TransactionHistoryActivityNewFlow.this, transactionHistoryResponse.getRecords());
        setListAdapter(adapter);
      /*  mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);*/
//        requestLoadingOfList(false);

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {
                    if (selected)
                        requestLoadingOfList(false, tranType_str);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page including list of all transaction histories ");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 25);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page including list of all transaction histories");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 25 logged");

    }

    void requestLoadingOfList(boolean requestRefresh, String tranType_str) {
        long from = -1;
        TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplication()).getTransactionHistoryResponse();
        if (transactionHistoryResponse.getRecords() == null || transactionHistoryResponse.getRecords().size() == 0) {
            from = 1;
        } else if (transactionHistoryResponse.getRecords().size() >= transactionHistoryResponse.getTotalNoOfTransactions()) {
            from = -1;
        } else if (transactionHistoryResponse.getTo() < transactionHistoryResponse.getTotalNoOfTransactions()) {
            from = transactionHistoryResponse.getTo() + 1L;
        }
        if (from != -1L) {
            /* Starting Download Service */
            loadingMore = true;
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncServiceNewFlow.class);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("type", SyncServiceNewFlow.TYPE_TRANS_HIST);
            intent.putExtra("from", from);
            intent.putExtra("tranType", tranType_str);
            startService(intent);
        } else {
            loadingMore = true;
            Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncServiceNewFlow.class);
            intent.putExtra("receiver", mReceiver);
            intent.putExtra("type", SyncServiceNewFlow.TYPE_TRANS_HIST);
            intent.putExtra("from", from);
            intent.putExtra("tranType", tranType_str);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case SyncServiceNewFlow.STATUS_RUNNING:
                setProgressBarIndeterminateVisibility(true);
                break;
            case SyncServiceNewFlow.STATUS_FINISHED:
                setProgressBarIndeterminateVisibility(false);
                String[] results = resultData.getStringArray("result");
                TransactionHistoryResponse transactionHistoryResponse = ((CoreApplication) getApplicationContext()).getTransactionHistoryResponse();
                if (adapter.getCount() < transactionHistoryResponse.getRecords().size()) {
                    int start = adapter.getCount();
                    int count = transactionHistoryResponse.getRecords().size() - adapter.getCount();
                    while (count != 0) {
                        adapter.add(transactionHistoryResponse.getRecords().get(start));
                        start++;
                        count--;
                    }
                    adapter.notifyDataSetChanged();
                    //loadingMore = false;

                }
                loadingMore = false;
                break;
            case SyncServiceNewFlow.STATUS_ERROR:
                //TransactionHistoryResponse transactionHistoryRespons = ((CoreApplication) getApplication()).getTransactionHistoryResponse();

                /*if (transactionHistoryRespons.getRecords() == null || transactionHistoryRespons.getRecords().size() == 0)
                    setListAdapter(null);*/
                setProgressBarIndeterminateVisibility(false);
                String error = resultData.getString(Intent.EXTRA_TEXT);
                getListView().removeFooterView(loadMoreView);
                //Toast.makeText(getBaseContext(), "No transactions are available", Toast.LENGTH_SHORT).show();
                final Toast toast = Toast.makeText(getBaseContext(), "No transactions are available more", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);


                loadingMore = false;
                break;
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Object o = v.getTag();
        if (null == o) {
            return;
        }
        TransTypeInterface transTypeInterface = ((ViewHolder) v.getTag()).getTransTypeInterface();
        TransType type = TransType.valueOf(transTypeInterface.getG_response_trans_type());
        String json = null;
        String transType = null;
        Intent intent = new Intent();
        if (null != type) {
            switch (type) {
                case SEND_MONEY_REQUEST_RESPONSE:
                    json = new Gson().toJson((SendMoneyRequestResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case SEND_MONEY_COMMIT_REQUEST_RESPONSE:
                    json = new Gson().toJson((SendMoneyCommitRequestResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;

                case PAY_TO_MERCHANT_RESPONSE:
                    json = new Gson().toJson((PayToMerchantRequestResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE:
                    json = new Gson().toJson((PayToMerchantCommitRequestResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case DOMESTIC_RECHARGE_TRAN_RESPONSE:
                    json = new Gson().toJson((DomesticRechargeTranHistoryResponsePojo) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case ECOMMERCE_TRAN_RESPONSE:
                    json = new Gson().toJson((DomesticRechargeTranHistoryResponsePojo) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case VOCHER_RECHARGE_TRAN_RESPONSE:
                    json = new Gson().toJson((DomesticRechargeTranHistoryResponsePojo) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case P2P_RECEIVED:
                    json = new Gson().toJson((SendMoneyIncomingResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case LOAD_MONEY:
                    json = new Gson().toJson((SendMoneyIncomingResponse) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case INTERNATIONAL_RECHARGE_L2_RESPONSE:
                    json = new Gson().toJson((TransactionalHistoryForInternationalRecharge) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case PREPAID_REQUESTCARD_RESPONSE:
                    json = new Gson().toJson((TransactionalHistoryForInternationalRecharge) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;
                case INVOICE_TRAN_RESPONSE:
                    json = new Gson().toJson((DomesticRechargeTranHistoryResponsePojo) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;

                case CASHBACK:
                    json = new Gson().toJson((DomesticRechargeTranHistoryResponsePojo) transTypeInterface);
                    transType = type.name();
                    intent.putExtra("transaction", json);
                    intent.putExtra("type", transType);
                    intent.setClass(this, TransactionHistoryDisplayActivityNewFlow.class);
                    break;

                default:
                    Toast.makeText(this, "Not Yet Implemented-" + type, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (null != json) {
            startActivity(intent);
        }
    }

    class CustomList extends ArrayAdapter<TransTypeInterface> {
        public CustomList(Activity context, ArrayList<TransTypeInterface> genericResponses) {
            super(context, R.layout.list_row, genericResponses);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            TransTypeInterface genericResponse = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);
                holder = new ViewHolder();
                holder.transType = (TextView) convertView.findViewById(R.id.transType);
                holder.text_name = (TextView) convertView.findViewById(R.id.textView1);
                holder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.transaction_value = (TextView) convertView.findViewById(R.id.transaction_value);
                holder.textNextLine = (TextView) convertView.findViewById(R.id.textView2);

                //RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(TransactionHistoryActivity.this, R.anim.myanim);
                //ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
                //holder.transType.setAnimation(ranim);
                holder.transType.setRotation(90); // 90 degree rotation

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TransType type = TransType.valueOf(genericResponse.getG_response_trans_type());
            switch (type) {
                case PAY_TO_MERCHANT_RESPONSE:
                    PayToMerchantRequestResponse payToMerchantRequestResponse = (PayToMerchantRequestResponse) genericResponse;
                    holder.text_name.setText(payToMerchantRequestResponse.getRecipientName());
                    holder.transType.setText("P2M");
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(payToMerchantRequestResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(payToMerchantRequestResponse.getTxnAmount(), 3, 3));
                    break;
                case PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE:
                    PayToMerchantCommitRequestResponse payToMerchantCommitRequestResponse = (PayToMerchantCommitRequestResponse) genericResponse;
                    holder.text_name.setText(payToMerchantCommitRequestResponse.getRecipientName());
                    if (payToMerchantCommitRequestResponse.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (payToMerchantCommitRequestResponse.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("P2M");
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(payToMerchantCommitRequestResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(payToMerchantCommitRequestResponse.getTxnAmount(), 3, 3));
                    break;
                case SEND_MONEY_REQUEST_RESPONSE:
                    SendMoneyRequestResponse sendMoneyRequestResponse = (SendMoneyRequestResponse) genericResponse;
                    holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    holder.transType.setText("P2P");
                    holder.text_name.setText(sendMoneyRequestResponse.getRecipientName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyRequestResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(sendMoneyRequestResponse.getTotal(), 3, 3));
                    break;
                case DOMESTIC_RECHARGE_TRAN_RESPONSE:
                    DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo = (DomesticRechargeTranHistoryResponsePojo) genericResponse;
                    if (domesticRechargeTranHistoryResponsePojo.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (domesticRechargeTranHistoryResponsePojo.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("MOBILE\nBILL");
                    holder.text_name.setText(domesticRechargeTranHistoryResponsePojo.getOperatorName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo.getRechargeAmt()), 3, 3));
                    break;
                case ECOMMERCE_TRAN_RESPONSE:
                    DomesticRechargeTranHistoryResponsePojo ecommece = (DomesticRechargeTranHistoryResponsePojo) genericResponse;
                    if (ecommece.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (ecommece.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("ECOMMERCE");
                    holder.text_name.setText(ecommece.getOperatorName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(ecommece.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(Double.parseDouble(ecommece.getRechargeAmt()), 3, 3));
                    break;
                case VOCHER_RECHARGE_TRAN_RESPONSE:
                    DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo1 = (DomesticRechargeTranHistoryResponsePojo) genericResponse;
                    if (domesticRechargeTranHistoryResponsePojo1.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (domesticRechargeTranHistoryResponsePojo1.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("PREPAID\nCARDS");
                    holder.text_name.setText(domesticRechargeTranHistoryResponsePojo1.getOperatorName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo1.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo1.getRechargeAmt()), 3, 3));
                    break;
                case SEND_MONEY_COMMIT_REQUEST_RESPONSE:
                    SendMoneyCommitRequestResponse sendMoneyCommitRequestResponse = (SendMoneyCommitRequestResponse) genericResponse;
                    if (sendMoneyCommitRequestResponse.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (sendMoneyCommitRequestResponse.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("P2P");
                    holder.text_name.setText(sendMoneyCommitRequestResponse.getRecipientName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(sendMoneyCommitRequestResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(sendMoneyCommitRequestResponse.getTotal(), 3, 3));
                    break;
                case P2P_RECEIVED:
                    SendMoneyIncomingResponse moneyIncomingResponse = (SendMoneyIncomingResponse) genericResponse;
                    if (moneyIncomingResponse.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (moneyIncomingResponse.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("P2PR");
                    holder.text_name.setText(moneyIncomingResponse.getSenderName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(moneyIncomingResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(moneyIncomingResponse.getTxnAmount(), 3, 3));
                    break;
                case LOAD_MONEY:
                    moneyIncomingResponse = (SendMoneyIncomingResponse) genericResponse;
                    if (moneyIncomingResponse.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (moneyIncomingResponse.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    }
                    holder.transType.setText("TOPUP");
                    holder.text_name.setText("TOPUP");
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(moneyIncomingResponse.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(moneyIncomingResponse.getTxnAmount(), 3, 3));
                    break;
                case INTERNATIONAL_RECHARGE_L2_RESPONSE:
                    TransactionalHistoryForInternationalRecharge transactionalHistoryForInternationalRecharge = (TransactionalHistoryForInternationalRecharge) genericResponse;
                    if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("4")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    }
                    holder.transType.setText("INT'L\nTOPUP");
                    holder.text_name.setText(transactionalHistoryForInternationalRecharge.getProviderName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(transactionalHistoryForInternationalRecharge.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(transactionalHistoryForInternationalRecharge.getDenominationinKWD(), 3, 3));
                    break;
                case PREPAID_REQUESTCARD_RESPONSE:
                    transactionalHistoryForInternationalRecharge = (TransactionalHistoryForInternationalRecharge) genericResponse;
                    if (transactionalHistoryForInternationalRecharge.getTranType().equals("2")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("3")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("1")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    } else if (transactionalHistoryForInternationalRecharge.getTranType().equals("4")) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.exclaimation_android52));
                    }
                    holder.transType.setText("PREPAID\nCARDS");
                    holder.text_name.setText(transactionalHistoryForInternationalRecharge.getProviderName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(transactionalHistoryForInternationalRecharge.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(transactionalHistoryForInternationalRecharge.getDenominationinKWD(), 3, 3));
                    break;
                case INVOICE_TRAN_RESPONSE:
                    DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo2 = (DomesticRechargeTranHistoryResponsePojo) genericResponse;
                    /*if (domesticRechargeTranHistoryResponsePojo2.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (domesticRechargeTranHistoryResponsePojo2.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.mer_exc));
                    }*/
                    if (domesticRechargeTranHistoryResponsePojo2.getPaymentStatus() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.hold_icon));
                        // right0.setText("HOLD");
                    } else if (domesticRechargeTranHistoryResponsePojo2.getPaymentStatus() == 2) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                        //right0.setText("SUCCESS");
                    } else if (domesticRechargeTranHistoryResponsePojo2.getPaymentStatus() == 3) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                        //right0.setText("REJECTED");
                    } else if (domesticRechargeTranHistoryResponsePojo2.getPaymentStatus() == 4) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                        //right0.setText("FAILED");
                    }
                    holder.transType.setText("INVOICE");
                    holder.text_name.setText(domesticRechargeTranHistoryResponsePojo2.getOperatorName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo2.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo2.getRechargeAmt()), 3, 3));
                    break;

                case CASHBACK:
                    DomesticRechargeTranHistoryResponsePojo domesticRechargeTranHistoryResponsePojo3 = (DomesticRechargeTranHistoryResponsePojo) genericResponse;
                    /*if (domesticRechargeTranHistoryResponsePojo2.getG_status() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.rsz_tickk));
                    } else if (domesticRechargeTranHistoryResponsePojo2.getG_status() == -1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.mer_exc));
                    }*/
                    if (domesticRechargeTranHistoryResponsePojo3.getPaymentStatus() == 1) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.hold_icon));
                        // right0.setText("HOLD");
                    } else if (domesticRechargeTranHistoryResponsePojo3.getPaymentStatus() == 2) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                        //right0.setText("SUCCESS");
                    } else if (domesticRechargeTranHistoryResponsePojo3.getPaymentStatus() == 3) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                        //right0.setText("REJECTED");
                    } else if (domesticRechargeTranHistoryResponsePojo3.getPaymentStatus() == 4) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.crossfinal));
                        //right0.setText("FAILED");
                    } else if (domesticRechargeTranHistoryResponsePojo3.getPaymentStatus() == 0) {
                        holder.icon.setImageDrawable(getResources().getDrawable(R.drawable.tickk));
                        //right0.setText("SUCCESS");
                    }
                    holder.transType.setText("BPOINTS\nCASHBACK");
                    holder.text_name.setText(domesticRechargeTranHistoryResponsePojo3.getOperatorName());
                    holder.textNextLine.setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(domesticRechargeTranHistoryResponsePojo3.getServerTime())));
                    holder.transaction_value.setText("Txn Amount: " + PriceFormatter.format(Double.parseDouble(domesticRechargeTranHistoryResponsePojo3.getRechargeAmt()), 3, 3));
                    break;
                default:
                    holder.text_name.setText("UNKNOWN");
                    GenericResponse genericResponse1 = (GenericResponse) genericResponse;
                    break;
            }
            holder.transTypeInterface = genericResponse;
            return convertView;
        }
    }

    //@@romeo
    public class ViewHolder {
        TextView transType;
        TextView transaction_value;
        TextView text_name;
        TextView textNextLine;
        ImageView icon;
        TransTypeInterface transTypeInterface;

        public TransTypeInterface getTransTypeInterface() {
            return transTypeInterface;
        }
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    private class CustomAdapter extends BaseAdapter {
        Context context;
        List<String> item_list;
        String[] images;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, List<String> tranType_list) {
            this.context = applicationContext;
            this.item_list = tranType_list;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return item_list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view = inflter.inflate(R.layout.virtual_card_new_row_text, null);
            TextView namess = (TextView) view.findViewById(R.id.vp_name_text);
            namess.setText(item_list.get(position));
            return view;

        }
    }
}
