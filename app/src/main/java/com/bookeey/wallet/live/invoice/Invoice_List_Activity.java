package com.bookeey.wallet.live.invoice;

import android.app.ActionBar;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.invoice_Processing.InvoiceDetailsProcessing;
import coreframework.processing.invoice_Processing.InvoiceProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsPojo;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsResponsePojo;
import ycash.wallet.json.pojo.invoicePojo.InvoicePaymentRequest;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

public class Invoice_List_Activity extends GenericActivity implements YPCHeadlessCallback {

    ArrayList<InvoiceDetailsPojo> arrayList;
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

        response_str = getIntent().getStringExtra("INVOICE_RESPONSE");
        arrayList = new ArrayList<>();
        InvoiceDetailsResponsePojo response = new Gson().fromJson(response_str, InvoiceDetailsResponsePojo.class);
        for (int i = 0; i < response.getInvoiceDetailsPojo().size(); i++) {
            arrayList.add(new InvoiceDetailsPojo(response.getInvoiceDetailsPojo().get(i).getInvNo(), response.getInvoiceDetailsPojo().get(i).getInvoiceDate(),
                    response.getInvoiceDetailsPojo().get(i).getMerchantName(), response.getInvoiceDetailsPojo().get(i).getAmount(),
                    response.getInvoiceDetailsPojo().get(i).isMerchantRequest(), response.getInvoiceDetailsPojo().get(i).getDescription(), response.getInvoiceDetailsPojo().get(i).getCustomerName(), response.getInvoiceDetailsPojo().get(i).getCustomerEmailId()));
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
                InvoiceDetailsPojo dataModel = arrayList.get(position);
                /*Gson gson = new Gson();
                String json = gson.toJson(dataModel);
                Intent intent = new Intent(getBaseContext(), InvoiceL1Activity.class);
                intent.putExtra("INVOICE_RESPONSE_DETAILS", json);
                startActivity(intent);*/
                deatialsRequest(dataModel.getInvNo(), dataModel.getMerchantName(), dataModel.getAmount());
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
        Log.e("Firebase "," Event 16 logged");
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

    private class CustomAdapter extends ArrayAdapter<InvoiceDetailsPojo> {
        private ArrayList<InvoiceDetailsPojo> dataSet;
        Context mContext;

        public CustomAdapter(ArrayList<InvoiceDetailsPojo> data, Context context) {
            super(context, R.layout.invoice_list_row, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            InvoiceDetailsPojo dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            InVoiceHolder inVoiceHolder; // view lookup cache stored in tag
            if (convertView == null) {
                inVoiceHolder = new InVoiceHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.invoice_list_row, parent, false);
                inVoiceHolder.invoice_row_inv_no_text = (TextView) convertView.findViewById(R.id.invoice_row_inv_no_text);
                inVoiceHolder.invoice_row_inv_date_text = (TextView) convertView.findViewById(R.id.invoice_row_inv_date_text);
                convertView.setTag(inVoiceHolder);
            } else {
                inVoiceHolder = (InVoiceHolder) convertView.getTag();
            }
            inVoiceHolder.invoice_row_inv_no_text.setText(getResources().getString(R.string.invoice_number) + " :" + dataModel.getInvNo());
            inVoiceHolder.invoice_row_inv_date_text.setText(getResources().getString(R.string.invoice_date) + " :" + dataModel.getInvoiceDate());
           /* if (dataModel.getCustomerName() != "") {
                inVoiceHolder.invoice_row_inv_cust_name_text.setVisibility(View.VISIBLE);
                inVoiceHolder.invoice_row_inv_cust_name_text.setText("FullName :" + dataModel.getCustomerName());
            }else{
                inVoiceHolder.invoice_row_inv_cust_name_text.setVisibility(View.GONE);
            }

            if (dataModel.getCustomerEmailId() != "") {
                inVoiceHolder.invoice_row_inv_cust_emailID_text.setVisibility(View.VISIBLE);
                inVoiceHolder.invoice_row_inv_cust_emailID_text.setText("EmailID :" + dataModel.getCustomerEmailId());
            }else{
                inVoiceHolder.invoice_row_inv_cust_emailID_text.setVisibility(View.GONE);
            }*/

            return convertView;
        }

        private class InVoiceHolder {
            TextView invoice_row_inv_no_text,
                    invoice_row_inv_date_text;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}