package com.bookeey.wallet.live.webotp;

import android.app.ActionBar;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.invoice.Invoice_List_Activity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.invoice_Processing.InvoiceDetailsProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.ecomotp.BWebEComOTP;
import ycash.wallet.json.pojo.ecomotp.BWebEComOTPDetailsResponsePojo;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsPojo;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsResponsePojo;
import ycash.wallet.json.pojo.invoicePojo.InvoicePaymentRequest;

public class BWeb_ECom_OTP_List_Activity extends GenericActivity implements YPCHeadlessCallback {

    ArrayList<BWebEComOTP> arrayList;
    String response_str = null;
    ListView list;
    private static CustomAdapter adapter;


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list_activity);


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

        response_str = getIntent().getStringExtra("BWEB_ECOM_OTP_RESPONSE");
        arrayList = new ArrayList<>();
        BWebEComOTPDetailsResponsePojo response = new Gson().fromJson(response_str, BWebEComOTPDetailsResponsePojo.class);


        if(response.getOtpList()!=null) {

            for (int i = 0; i < response.getOtpList().size(); i++) {

                BWebEComOTP bw = new BWebEComOTP();
                bw.setTxnRefNo(response.getOtpList().get(i).getTxnRefNo());
                bw.setOtp(response.getOtpList().get(i).getOtp());


                arrayList.add(bw);
            }

        }else{
            Toast.makeText(BWeb_ECom_OTP_List_Activity.this,"No OTPs Found",Toast.LENGTH_LONG).show();
        }


        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list = (ListView) findViewById(R.id.list);
        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        final ListView list = (ListView) findViewById(R.id.list);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.mobilebill_nameTextooredo, R.id.mobilebill_wallet_id, R.id.mobilebill_balance_id);

        adapter = new CustomAdapter(arrayList, getApplicationContext());
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                BWebEComOTP dataModel = arrayList.get(position);
                /*Gson gson = new Gson();
                String json = gson.toJson(dataModel);
                Intent intent = new Intent(getBaseContext(), InvoiceL1Activity.class);
                intent.putExtra("INVOICE_RESPONSE_DETAILS", json);
                startActivity(intent);*/
//                deatialsRequest(dataModel.getInvNo(), dataModel.getMerchantName(), dataModel.getAmount());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Invoice page - listing all invoices");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 16);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Invoice page - listing all invoices");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 16 logged");
    }

    private void deatialsRequest(String invNumber, String merchantName, double invAmount) {
        final CoreApplication application = (CoreApplication) getApplication();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest();
        invoicePaymentRequest.setInvNo(invNumber);
        invoicePaymentRequest.setMerchantName(merchantName);
        invoicePaymentRequest.setAmount(invAmount);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoiceDetailsProcessing(invoicePaymentRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
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

    private class CustomAdapter extends ArrayAdapter<BWebEComOTP> {
        private ArrayList<BWebEComOTP> dataSet;
        Context mContext;

        public CustomAdapter(ArrayList<BWebEComOTP> data, Context context) {
            super(context, R.layout.bweb_ecom_otp_list_row, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            BWebEComOTP dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            BWebEComOTPViewHolder bWebEComOTPViewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                bWebEComOTPViewHolder = new BWebEComOTPViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.bweb_ecom_otp_list_row, parent, false);
                bWebEComOTPViewHolder.tv_otp = (TextView) convertView.findViewById(R.id.tv_otp);
                bWebEComOTPViewHolder.tv_txn_ref_no = (TextView) convertView.findViewById(R.id.tv_txn_ref_no);
                convertView.setTag(bWebEComOTPViewHolder);
            } else {
                bWebEComOTPViewHolder = (BWebEComOTPViewHolder) convertView.getTag();
            }
            bWebEComOTPViewHolder.tv_otp.setText("OTP"+ " : " + dataModel.getOtp());
            bWebEComOTPViewHolder.tv_txn_ref_no.setText("Txn Ref No" + " : " + dataModel.getTxnRefNo());


            return convertView;
        }

        private class BWebEComOTPViewHolder {
            TextView tv_otp,
                    tv_txn_ref_no;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}

