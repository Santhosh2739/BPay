package com.bookeey.wallet.live.invoice;

import static coreframework.database.CustomSharedPreferences.SP_KEY.MOBILE_NUMBER;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentInvoice;
import com.bookeey.wallet.live.login.LoginActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.LoadMoneyProcessing;
import coreframework.processing.invoice_Processing.InvoicePayNowProcessing;
import coreframework.taskframework.DecimalDigitsInputFilter;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import coreframework.utils.URLUTF8Encoder;
import newflow.LoginActivityFromSplashNewFlow;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsPojo;
import ycash.wallet.json.pojo.invoicePojo.InvoicePaymentRequest;
import ycash.wallet.json.pojo.invoicePojo.InvoiceResponse;
import ycash.wallet.json.pojo.loadmoney.PaymentForm;
import ycash.wallet.json.pojo.loadmoney.WalletLimits;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
/**
 * Created by 10037 on 23-Sep-17.
 */
public class InvoiceL1Activity extends GenericActivity implements YPCHeadlessCallback {
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME = "my_key";
    Button invoice_paynow_btn, invoice_hold_btn, invoice_reject_btn;
    EditText invoice_mer_name_edit, invoice_inv_no_edit,
            invoice_inv_date_edit, invoice_inv_amount_edit, invoice_desc_edit, invoice_tpin_edit,
            invoice_cust_name_edit, invoice_cust_email_edit,
    invoice_inv_offerID_edit,
            invoice_inv_discount_per_edit,
            invoice_inv_discount_amt_edit,
            invoice_inv_total_amount_edit;
    View invoice_tpin_horizantal_view, description_view,
            ooredoo_sendmoney_confirmpayment_cust_name_horizantal0_view,
            ooredoo_sendmoney_confirmpayment_cust_email_horizantal0_view,
    ooredoo_sendmoney_confirmpayment_offerID_horizantal0_view2,
            ooredoo_sendmoney_confirmpayment_discount_per_horizantal0_view2,
            ooredoo_sendmoney_confirmpayment_discount_amt_horizantal0_view2,
            ooredoo_sendmoney_confirmpayment_total_amount_horizantal0_view2;
    WalletLimits walletLimits;
    ProgressDialog progress;
    LinearLayout invoice_group_btn_layout, invoice_inv_tpin_linear, description_linear,
            ooredoo_sendmoney_confirmpayment_cust_name_layout,
            ooredoo_sendmoney_confirmpayment_cust_email_layout,
    ooredoo_sendmoney_confirmpayment_offerID_linear,
            ooredoo_sendmoney_confirmpayment_discount_per_linear,
            ooredoo_sendmoney_confirmpayment_discount_amt_linear,
            ooredoo_sendmoney_confirmpayment_total_amount_linear;
    double walletBalance, amount, invoice_amount = 0.0;
    CoreApplication application = null;
    TransactionLimitResponse limits = null;
    boolean biometricVerified = false;
    long offerID = 0;
    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    FingerprintAuthenticationDialogFragmentInvoice mFragment;
    @Inject
    SharedPreferences mSharedPreferences;
    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;
    private String response_det_str = null;
    private boolean isMerchantRequest = false;
    private FirebaseAnalytics firebaseAnalytics;
    private KeyStore mKeyStore;
    private Cipher mCipher;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_details_page1);
        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        invoice_mer_name_edit = (EditText) findViewById(R.id.invoice_mer_name_edit);
        invoice_inv_no_edit = (EditText) findViewById(R.id.invoice_inv_no_edit);
        invoice_inv_date_edit = (EditText) findViewById(R.id.invoice_inv_date_edit);
        invoice_inv_amount_edit = (EditText) findViewById(R.id.invoice_inv_amount_edit);
        invoice_paynow_btn = (Button) findViewById(R.id.invoice_paynow_btn);
        invoice_group_btn_layout = (LinearLayout) findViewById(R.id.invoice_group_btn_layout);
        invoice_reject_btn = (Button) findViewById(R.id.invoice_reject_btn);
        invoice_hold_btn = (Button) findViewById(R.id.invoice_hold_btn);
        invoice_desc_edit = (EditText) findViewById(R.id.invoice_desc_edit);
        //remarks_linear = (LinearLayout) findViewById(R.id.remarks_linear);
        invoice_tpin_edit = (EditText) findViewById(R.id.invoice_tpin_edit);
        invoice_inv_tpin_linear = (LinearLayout) findViewById(R.id.invoice_inv_tpin_linear);
        invoice_tpin_horizantal_view = (View) findViewById(R.id.invoice_tpin_horizantal_view);
        //invoice discount enhancement
        ooredoo_sendmoney_confirmpayment_offerID_linear = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_offerID_linear);
        ooredoo_sendmoney_confirmpayment_discount_per_linear = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_discount_per_linear);
        ooredoo_sendmoney_confirmpayment_discount_amt_linear = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_discount_amt_linear);
        ooredoo_sendmoney_confirmpayment_total_amount_linear = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_total_amount_linear);
        ooredoo_sendmoney_confirmpayment_offerID_horizantal0_view2 = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_offerID_horizantal0_view2);
        ooredoo_sendmoney_confirmpayment_discount_per_horizantal0_view2 = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_discount_per_horizantal0_view2);
        ooredoo_sendmoney_confirmpayment_discount_amt_horizantal0_view2 = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_discount_amt_horizantal0_view2);
        ooredoo_sendmoney_confirmpayment_total_amount_horizantal0_view2 = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_total_amount_horizantal0_view2);
        invoice_inv_discount_amt_edit = (EditText) findViewById(R.id.invoice_inv_discount_amt_edit);
        invoice_inv_total_amount_edit = (EditText) findViewById(R.id.invoice_inv_total_amount_edit);
        ooredoo_sendmoney_confirmpayment_cust_name_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_cust_name_horizantal0_view);
        ooredoo_sendmoney_confirmpayment_cust_email_horizantal0_view = (View) findViewById(R.id.ooredoo_sendmoney_confirmpayment_cust_email_horizantal0_view);
        invoice_cust_name_edit = (EditText) findViewById(R.id.invoice_cust_name_edit);
        invoice_cust_email_edit = (EditText) findViewById(R.id.invoice_cust_email_edit);
        invoice_inv_offerID_edit = (EditText) findViewById(R.id.invoice_inv_offerID_edit);
        invoice_inv_discount_per_edit = (EditText) findViewById(R.id.invoice_inv_discount_per_edit);
        ooredoo_sendmoney_confirmpayment_cust_name_layout = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_cust_name_layout);
        ooredoo_sendmoney_confirmpayment_cust_email_layout = (LinearLayout) findViewById(R.id.ooredoo_sendmoney_confirmpayment_cust_email_layout);
        description_linear = (LinearLayout) findViewById(R.id.description_linear);
        description_view = (View) findViewById(R.id.description_view);
        invoice_paynow_btn.setText(getResources().getString(R.string.invoice_pay_btn));
        invoice_mer_name_edit.setEnabled(false);
        invoice_inv_no_edit.setEnabled(false);
        invoice_inv_date_edit.setEnabled(false);
        invoice_inv_amount_edit.setEnabled(false);
        invoice_desc_edit.setEnabled(false);
        invoice_inv_offerID_edit.setEnabled(false);
        invoice_inv_discount_per_edit.setEnabled(false);
        invoice_inv_discount_amt_edit.setEnabled(false);
        invoice_inv_total_amount_edit.setEnabled(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_mer_name_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_inv_no_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_inv_amount_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_desc_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_tpin_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_cust_email_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_mer_name_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.invoice_mer_name_edit)).getWindowToken(), 0);
        invoice_tpin_edit.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (invoice_tpin_edit.getRight() - invoice_tpin_edit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    verifyBiometric();
                }
            }
            return false;
        });
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
                onBackPressed();
            }
        });
        ((CoreApplication) getApplication()).inject(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                FINGERPRINT_PERMISSION_REQUEST_CODE);
        ImageView image_person = (ImageView) findViewById(R.id.invoice_user_image);
        progress = new ProgressDialog(InvoiceL1Activity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        application = (CoreApplication) getApplication();
        response_det_str = getIntent().getStringExtra("INVOICE_RESPONSE_DETAILS");
        CustomerLoginRequestReponse inv_limits_response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        if (inv_limits_response.getFilteredLimits().get("ECOMMERCE").getTpinLimit() != 0) {
            limits = inv_limits_response.getFilteredLimits().get("ECOMMERCE");
        }
        if (!response_det_str.isEmpty()) {
            InvoiceDetailsPojo response = new Gson().fromJson(response_det_str, InvoiceDetailsPojo.class);
            try {
                JSONObject obj = new JSONObject(response_det_str);
                JSONObject user = obj.getJSONObject("invoiceDetails");
                InvoiceDetailsPojo response2 = new Gson().fromJson(user.toString(), InvoiceDetailsPojo.class);
                invoice_mer_name_edit.setText(response2.getMerchantName());
                invoice_inv_no_edit.setText(response2.getInvNo());
                invoice_inv_date_edit.setText(response2.getInvoiceDate());
                invoice_inv_amount_edit.setText(String.valueOf(response.getTotalAmt()));
                if (response_det_str.contains("customerName")) {
                    if (response2.getCustomerName() != "") {
                        ooredoo_sendmoney_confirmpayment_cust_name_layout.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_cust_name_horizantal0_view.setVisibility(View.VISIBLE);
                        invoice_cust_name_edit.setText(response2.getCustomerName());
                    } else {
                        ooredoo_sendmoney_confirmpayment_cust_name_layout.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_cust_name_horizantal0_view.setVisibility(View.GONE);
                    }
                }
                if (response2.getArabicCustomerName() != null) {
                    try {
                        Charset charset = Charset.forName("ISO-8859-6");
                        CharsetDecoder decoder = charset.newDecoder();
                        ByteBuffer buf = ByteBuffer.wrap(response2.getArabicCustomerName());
                        CharBuffer cbuf = decoder.decode(buf);
                        CharSequence customer_name = java.nio.CharBuffer.wrap(cbuf);
                        invoice_cust_name_edit.setText(customer_name);
                    } catch (Exception e) {
                        Log.e("Invoice CustName Ex:", "" + e.getMessage());
                    }
                }
                if (response_det_str.contains("customerEmailId")) {
                    if (response2.getCustomerEmailId() != "") {
                        ooredoo_sendmoney_confirmpayment_cust_email_layout.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_cust_email_horizantal0_view.setVisibility(View.VISIBLE);
                        invoice_cust_email_edit.setText(response2.getCustomerEmailId());
                    } else {
                        ooredoo_sendmoney_confirmpayment_cust_email_layout.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_cust_email_horizantal0_view.setVisibility(View.GONE);
                    }
                }
                //For discount
                if (response_det_str.contains("offerDescription")) {
                    if (!response.getOfferDescription().isEmpty()) {
                        ooredoo_sendmoney_confirmpayment_offerID_linear.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_offerID_horizantal0_view2.setVisibility(View.VISIBLE);
                        invoice_inv_offerID_edit.setText("" + response.getOfferDescription());
                    } else {
                        ooredoo_sendmoney_confirmpayment_offerID_linear.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_offerID_horizantal0_view2.setVisibility(View.GONE);
                    }
                }
                //For discount
                if (response_det_str.contains("offerId")) {
                    if (response.getOfferId() != 0)
                        offerID = response.getOfferId();
                }

                /*if (response_det_str.contains("discountPrice")) {
                    if (response.getDiscountPrice() != 0) {
                        ooredoo_sendmoney_confirmpayment_discount_per_linear.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_discount_per_horizantal0_view2.setVisibility(View.VISIBLE);
                        invoice_inv_discount_per_edit.setText("" +PriceFormatter.format(response.getDiscountPrice(), 3, 3) + "%");
                    } else {
                        ooredoo_sendmoney_confirmpayment_discount_per_linear.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_discount_per_horizantal0_view2.setVisibility(View.GONE);
                    }
                }*/
                if (response_det_str.contains("discount")) {
                    if (response.getDiscount() != 0) {
                        ooredoo_sendmoney_confirmpayment_discount_amt_linear.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_discount_amt_horizantal0_view2.setVisibility(View.VISIBLE);
                        invoice_inv_discount_amt_edit.setText("" + PriceFormatter.format(response.getDiscount(), 3, 3) + " KWD");
                    } else {
                        ooredoo_sendmoney_confirmpayment_discount_amt_linear.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_discount_amt_horizantal0_view2.setVisibility(View.GONE);
                    }
                }
                if (response_det_str.contains("totalAmt")) {
                    if (response2.getAmount() != 0) {
                        ooredoo_sendmoney_confirmpayment_total_amount_linear.setVisibility(View.VISIBLE);
                        ooredoo_sendmoney_confirmpayment_total_amount_horizantal0_view2.setVisibility(View.VISIBLE);
                        invoice_inv_total_amount_edit.setText("" + PriceFormatter.format(response2.getAmount(), 3, 3) + " KWD");
                    } else {
                        ooredoo_sendmoney_confirmpayment_total_amount_linear.setVisibility(View.GONE);
                        ooredoo_sendmoney_confirmpayment_total_amount_horizantal0_view2.setVisibility(View.GONE);
                    }
                }
                if (response2.getDescription() != null && !response2.getDescription().isEmpty()) {
                    description_linear.setVisibility(View.VISIBLE);
                    description_view.setVisibility(View.VISIBLE);
                    invoice_desc_edit.setText(response2.getDescription());
                } else {
                    description_linear.setVisibility(View.GONE);
                    description_view.setVisibility(View.GONE);
                }
                if (response2.getArabicDescription() != null) {
                    try {
                        Charset charset = Charset.forName("ISO-8859-6");
                        CharsetDecoder decoder = charset.newDecoder();
                        ByteBuffer buf = ByteBuffer.wrap(response2.getArabicDescription());
                        CharBuffer cbuf = decoder.decode(buf);
                        CharSequence description = java.nio.CharBuffer.wrap(cbuf);
                        invoice_desc_edit.setText(description);
                    } catch (Exception e) {
                        Log.e("Invoice Descr Ex:", "" + e.getMessage());
                    }
                }
                this.isMerchantRequest = response2.isMerchantRequest();
                amount = Double.parseDouble(invoice_inv_amount_edit.getText().toString().trim());
                if (amount > limits.getTpinLimit()) {
                    invoice_inv_tpin_linear.setVisibility(View.VISIBLE);
                    invoice_tpin_horizantal_view.setVisibility(View.VISIBLE);
                    invoice_inv_amount_edit.requestFocus();
                } else {
                    invoice_inv_tpin_linear.setVisibility(View.GONE);
                    invoice_tpin_horizantal_view.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        walletBalance = Double.parseDouble(PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
        //amount = Double.parseDouble(invoice_inv_amount_edit.getText().toString().trim());
        invoice_amount = Double.parseDouble(PriceFormatter.format(amount, 3, 3));
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.invoice_l1_nameTextooredo, R.id.invoice_l1_wallet_id, R.id.invoice_l1_balance_id);
        if (isMerchantRequest) {
            invoice_group_btn_layout.setVisibility(View.VISIBLE);
            invoice_paynow_btn.setVisibility(View.VISIBLE);
        } else {
            invoice_paynow_btn.setVisibility(View.VISIBLE);
            invoice_group_btn_layout.setVisibility(View.GONE);
        }
        invoice_reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(InvoiceL1Activity.this, InvoiceReasonDialogue.class);
//                i.putExtra("MerchantName", invoice_mer_name_edit.getText().toString());
//                i.putExtra("InvoiceNo", invoice_inv_no_edit.getText().toString());
//                i.putExtra("Date", invoice_inv_date_edit.getText().toString());
//                i.putExtra("Amount", invoice_inv_amount_edit.getText().toString());
//                i.putExtra("OfferID", offerID);
//                startActivity(i);
                String merchantName = invoice_mer_name_edit.getText().toString();
                String invoiceNo = invoice_inv_no_edit.getText().toString();
                String date = invoice_inv_date_edit.getText().toString();
                String amount = invoice_inv_amount_edit.getText().toString();
//                i.putExtra("OfferID", offerID);
                invoiceRejectRequest(merchantName, invoiceNo, date, amount, "", offerID, 1);
                //  finish();
            }
        });
        invoice_hold_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceHoldRequest(invoice_amount, 2);
            }
        });
        invoice_paynow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (invoice_inv_tpin_linear.getVisibility() == View.VISIBLE) {
                    if(biometricVerified)
                        invoicePayNowRequest(invoice_amount, 0, offerID);
                    else if (invoice_tpin_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.invoice_enter_password), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }
                else
                    invoicePayNowRequest(invoice_amount, 0, offerID);
            }
        });
    }

    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_whatsapp, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InvoiceL1Activity.this);
        alertDialog.setView(promptsView);
        alertDialog.show();
    }

    public void onPurchased(boolean withFingerprint, String password) {
        //if (!withFingerprint) invoice_tpin_edit.setText(password);
       // invoicePayNowRequest(invoice_amount, 0, offerID);
        boolean bio = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC);
        if(!bio)
            alertDialog();
        else {
            biometricVerified = true;
            invoice_tpin_edit.setHint("");
            invoice_tpin_edit.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.biometric_verified, 0);
            Toast.makeText(getBaseContext(), getResources().getString(R.string.fingerprint_success), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyBiometric() {
        try {
            boolean isFingerprintAvailable = false;
            if (!mFragment.isAdded()) {
                boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                        InvoiceL1Activity.this, Manifest.permission.USE_FINGERPRINT)
                        == PackageManager.PERMISSION_GRANTED;
                if (mFingerprintManager != null) {
                    isFingerprintAvailable = mFingerprintManager.isHardwareDetected()
                            && mFingerprintManager.hasEnrolledFingerprints();
                }
                if (!isFingerprintPermissionGranted || !isFingerprintAvailable) {
                    // The user either rejected permission to read their fingerprint, we're on
                    // a device that doesn't support it, or the user doesn't have any
                    // fingerprints enrolled.
                    // Let them authenticate with a password
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragmentInvoice.Stage.PASSWORD);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else if (initCipher()) {
                    // Set up the crypto object for later. The object will be authenticated by use
                    // of the fingerprint.
                    // Show the fingerprint dialog. The user has the option to use the fingerprint with
                    // crypto, or you can fall back to using a server-side verified password.
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                    boolean useFingerprintPreference = mSharedPreferences
                            .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                    true);
                    if (useFingerprintPreference) {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragmentInvoice.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragmentInvoice.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragmentInvoice.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        } catch (Exception e) {
            Toast.makeText(InvoiceL1Activity.this, " Fingerprint Sensor Exc: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher() {
        try {
            if (mKeyStore == null) {
                createKey();
            }
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(KEY_NAME, null);
            mCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (Exception e) {
            if (e instanceof KeyPermanentlyInvalidatedException)
                return false;
            else if (e instanceof KeyStoreException | e instanceof CertificateException | e instanceof UnrecoverableKeyException | e instanceof IOException | e instanceof NoSuchAlgorithmException | e instanceof InvalidKeyException)
                throw new RuntimeException("Failed to init Cipher", e);
        }
        /*catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }*/
        return false;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | KeyStoreException
                | CertificateException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Invoice page - details of a specific invoice");
        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 17);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Invoice page - details of a specific invoice");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 17 logged");
    }

    private void invoicePayNowRequest(double invoice_amount, int i, long offerID) {
        final CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest();
        invoicePaymentRequest.setInvNo(invoice_inv_no_edit.getText().toString().trim());
        invoicePaymentRequest.setAmount(invoice_amount);
        invoicePaymentRequest.setStatus(i);
        invoicePaymentRequest.setOfferId(offerID);
        if (biometricVerified) {
            String pin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            invoicePaymentRequest.setTpin(pin);
        } else
            invoicePaymentRequest.setTpin(invoice_tpin_edit.getText().toString().trim());
        invoicePaymentRequest.setG_transType(TransType.INVOICE_PAYMENT_REQUEST.name());
        invoicePaymentRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        String jsondata = new Gson().toJson(invoicePaymentRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.INVOICE_PAYMENT_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String response_json = null;
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    if (!network_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                        InvoiceResponse invoiceResponse = new Gson().fromJson(network_response, InvoiceResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.INVOICE_PAYMENT_RESPONSE.name()) && response.getG_status() == 1) {
                            response_json = network_response;
                            if (invoiceResponse != null) {
                                application.setInvoices_count(invoiceResponse.getDueInvoiceCount());
                            }
                            Intent i = new Intent(InvoiceL1Activity.this, InvoiceFinalActivity.class);
                            i.putExtra("INVOICE_RESPONSE_L2", response_json);
                            startActivity(i);
                            return;
                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.INVOICE_PAYMENT_RESPONSE.name()) && response.getG_status() != 1) {
                            if (invoiceResponse != null) {
                                application.setInvoices_count(invoiceResponse.getDueInvoiceCount());
                            }
                            if (response.getG_errorDescription().equalsIgnoreCase("Invalid password ")) {
                                Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Invalid_Password), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                            } else if (response.getG_errorDescription().equalsIgnoreCase("Insufficient balance")) {
                                invoice_paynow_btn.setText(getResources().getString(R.string.invoice_load_money_btn));
                                loadMoneyCheck();
                            } else {
                                switch (response.getG_errorDescription()) {
                                    case "Invalid password":
                                        Toast toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Invalid_Password), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Invalid tpin":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Invalid_tpin), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "You have no invoices":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.You_have_no_invoices), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Monthly Recharge Limits Exceeding":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Monthly_Recharge_Limits_Exceeding), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Daily Recharge Limits Exceeding":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Daily_Recharge_Limits_Exceeding), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Invoice Amount Min & Max":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Invoice_Amount_Min_Max), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Daily & Monthly Limits Not Available":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "wrong tpin please enter correct tpin":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.wrong_tpin_please_enter_correct_tpin), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Customer Details are not available":
                                        toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.Customer_Details_are_not_available), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    default:
                                        toast = Toast.makeText(InvoiceL1Activity.this, response.getG_errorDescription(), Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                }
                                return;
                            }
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(InvoiceL1Activity.this, LoginActivity.class);
                            startActivity(intent);
                            InvoiceL1Activity.this.finish();
                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");
    }

    private void invoiceHoldRequest(double invoice_amount, int status) {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest();
        invoicePaymentRequest.setInvNo(invoice_inv_no_edit.getText().toString().trim());
        invoicePaymentRequest.setAmount(invoice_amount);
        invoicePaymentRequest.setStatus(status);
        //invoicePaymentRequest.setReason(remarks_str);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoicePayNowProcessing(invoicePaymentRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(false);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    private void loadMoneyCheck() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        String jsondata = new Gson().toJson(paymentForm);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOADMONEY_CHECK_REQUEST.getURL());
        buffer.append("?data=" + URLUTF8Encoder.encode(jsondata));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    walletLimits = new Gson().fromJson((String) msg.obj, WalletLimits.class);
                    if (walletLimits != null && walletLimits.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && walletLimits.getG_status() == 1) {
                        walletLimits = new Gson().fromJson((String) msg.obj, WalletLimits.class);
                        showLoadMoneyNeutralDailog();
                    } else if (walletLimits.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(InvoiceL1Activity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(InvoiceL1Activity.this, LoginActivity.class);
                        startActivity(intent);
                        InvoiceL1Activity.this.finish();
                    } else if (walletLimits != null && walletLimits.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && walletLimits.getG_status() != 1) {
                        Toast.makeText(getBaseContext(), walletLimits.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");
    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
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

    private void showLoadMoneyNeutralDailog() {
        final Dialog dialog_loadmoney = new Dialog(this);
        dialog_loadmoney.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loadmoney.setContentView(R.layout.loadmoney);
        final EditText amount_enter = (EditText) dialog_loadmoney.findViewById(R.id.ypc_p2m_amount_edit);
        Button proceed_btn = (Button) dialog_loadmoney.findViewById(R.id.pay_load_pay_btn_new);
        Button cancel_btn = (Button) dialog_loadmoney.findViewById(R.id.pay_load_cancel_btn_new);
        TextView your_wallet_balance_Tv = (TextView) dialog_loadmoney.findViewById(R.id.your_wallet_balance_Tv);
        TextView you_can_load_upto_Tv = (TextView) dialog_loadmoney.findViewById(R.id.you_can_load_upto_Tv);
        TextView you_can_load_minimum_Tv = (TextView) dialog_loadmoney.findViewById(R.id.you_can_load_minimum_Tv);
        final CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("PGRECHARGE");
        amount_enter.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});
        you_can_load_upto_Tv.setText(PriceFormatter.format(walletLimits.getLoadmoneyMaxPerTxn(), 3, 3));
        your_wallet_balance_Tv.setText(PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
        you_can_load_minimum_Tv.setText(PriceFormatter.format(walletLimits.getLoadmoneyMinPerTxn(), 3, 3));
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amount_enter.getText().toString().trim();
                if (amount.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.invoice_enter_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                Double amount_db = Double.parseDouble(amount);
                if (amount_db == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.invoice_enter_valid_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                PaymentForm paymentForm = new PaymentForm();
                paymentForm.setMobileNumber(CustomSharedPreferences.getStringData(getBaseContext(), MOBILE_NUMBER));
                paymentForm.setPrice(amount_db);
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new LoadMoneyProcessing(paymentForm, application, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_loadmoney.dismiss();
            }
        });
        dialog_loadmoney.show();
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

    private void invoiceRejectRequest(String name, String no, String date, String amount, String reason, long offerID, int status) {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest();
        invoicePaymentRequest.setInvNo(no);
        invoicePaymentRequest.setAmount(Double.parseDouble(amount));
        invoicePaymentRequest.setOfferId(offerID);
        invoicePaymentRequest.setStatus(status);
        invoicePaymentRequest.setReason(reason);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoicePayNowProcessing(invoicePaymentRequest, application, true));
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
