package com.bookeey.wallet.live.mobilebill;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.invoice.InvoiceL1Activity;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentInvoice;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentMobileBill;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.mobile.DenominationsL1Processing;
import coreframework.taskframework.DecimalDigitsInputFilter;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import newflow.MobileBill_L1_ActivityNewFlow;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DenominationResponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL1RequestPojo;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2Response;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;

/**
 * Created by 10037 on 6/14/2017.
 */

public class MobileBill_L1_Activity extends GenericActivity implements YPCHeadlessCallback {

    EditText mobile_operatorname_edit, mobile_mobilenumber_edit, mobile_billapy_amount_edit, mobile_tpin_edit;
    Spinner mobile_topup_amount_spinner;
    LinearLayout mobile_topup_amount_layout, mobile_billpay_amount_layout, mobile_tpin_layout;
    List<String> categories;
    RadioGroup mobile_radiogroup;
    RadioButton mobile_totup_radio_btn,
            mobile_billpay_radio_btn;
    private String mobile_billpayment_type_str;
    Button submit_btn;
    String amount_billpay_str = "", operatorName_str;
    private DisplayImageOptions options;
    ImageView opearator_image;
    Double tpin_double = 0.000;
    private String tpin_str = null;
    CustomerLoginRequestReponse response = null;
    TransactionLimitResponse limits = null;
    ArrayAdapter<String> aa = null;
    String amount_without_kd = null;

    String response_str = null;
    Dialog promptsViewPassword;
    boolean IsBio = false;
    String tpin;
    boolean biometricVerified = false;
    private FirebaseAnalytics firebaseAnalytics;
    private KeyStore mKeyStore;
    private Cipher mCipher;
    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    FingerprintAuthenticationDialogFragmentMobileBill mFragment;
    @Inject
    SharedPreferences mSharedPreferences;
    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME = "my_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilebill_leg1_activity);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        mobile_operatorname_edit = (EditText) findViewById(R.id.mobile_operatorname_edit);
        mobile_mobilenumber_edit = (EditText) findViewById(R.id.mobile_mobilenumber_edit);
        mobile_billapy_amount_edit = (EditText) findViewById(R.id.mobile_billapy_amount_edit);
        mobile_tpin_edit = (EditText) findViewById(R.id.mobile_tpin_edit);
        opearator_image = (ImageView) findViewById(R.id.mobile_operator_img);
        //tpin_str = mobile_tpin_edit.getText().toString().trim();
        amount_billpay_str = mobile_billapy_amount_edit.getText().toString().trim();
        mobile_billpay_amount_layout = (LinearLayout) findViewById(R.id.mobile_billpay_amount_layout);
        mobile_topup_amount_layout = (LinearLayout) findViewById(R.id.mobile_topup_amount_layout);
        mobile_tpin_layout = (LinearLayout) findViewById(R.id.mobile_tpin_layout);
        mobile_radiogroup = (RadioGroup) findViewById(R.id.mobile_radiogroup);
        mobile_totup_radio_btn = (RadioButton) findViewById(R.id.mobile_totup_radio_btn);
        mobile_billpay_radio_btn = (RadioButton) findViewById(R.id.mobile_billpay_radio_btn);
        mobile_billpayment_type_str = mobile_totup_radio_btn.getText().toString();
        mobile_billpayment_type_str = "topup";
        submit_btn = (Button) findViewById(R.id.submit_btn);
        mobile_topup_amount_spinner = (Spinner) findViewById(R.id.mobile_topup_amount_spinner);

        mobile_topup_amount_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(MobileBill_L1_Activity.this);
                return false;
            }
        });

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
        ((CoreApplication) getApplication()).inject(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                FINGERPRINT_PERMISSION_REQUEST_CODE);

        response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        limits = response.getFilteredLimits().get("LOCAL_RECHARGE");

        response_str = getIntent().getStringExtra("DENOMINATIONS_RESPONSE");

        mobile_billapy_amount_edit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});

        mobile_billapy_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                amount_without_kd = mobile_billapy_amount_edit.getText().toString().trim();
                int indexofDesc = amount_without_kd.indexOf(".");
                if (indexofDesc > 4) {
                    mobile_billapy_amount_edit.setInputType(InputType.TYPE_NULL);
                }
                try {
                    if (amount_without_kd != "") {
                        tpin_double = Double.parseDouble(amount_without_kd);
                        boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(),  CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
                        if (tpin_double > limits.getTpinLimit() && !guest) {
                            IsBio = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }

        updateProfile(R.id.sendmoney_nameTextooredo, R.id.sendmoney_wallet_id, R.id.sendmoney_balance_id);
        //mobile_operatorname_edit.setText(((CoreApplication) getApplication()).getOperatorName());
        operatorName_str = (((CoreApplication) getApplication()).getOperatorName());
        // Spinner Drop down elements
        categories = new ArrayList<>();

//        categories.add("--Select Amount--");
        categories.add(getString(R.string.select_amount));

        DenominationResponse denominationResponse = new Gson().fromJson(response_str, DenominationResponse.class);
        for (int i = 0; i < denominationResponse.getDenom().size(); i++) {
            categories.add("" + denominationResponse.getDenom().get(i)+" "+"KD");
        }

        /*String denomiAmount = ((CoreApplication) getApplication()).getDenominationsAmount();
        if (denomiAmount != null) {
            String[] amount = denomiAmount.split(",");
            for (int i = 0; i < amount.length; i++) {
                categories.add("" + amount[i] + " " + "KD");
            }
        }*/

       /* if (amount_billpay_str != null) {
            String[] amount = amount_billpay_str.split(" ");
            amount_without_kd = amount[0];

        }*/
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.new_logo_virtual_grey)
                .showImageForEmptyUri(R.drawable.new_logo_virtual_grey)
                .showImageOnFail(R.drawable.new_logo_virtual_grey)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        initImageLoader(getBaseContext());
        ImageLoader.getInstance()
                .displayImage(((CoreApplication) getApplication()).getOperatorImage(), opearator_image, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    }
                });

        aa = new ArrayAdapter<String>(this, R.layout.spinner_item_center, categories) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {

                    //((TextView) v.findViewById(android.R.id.text1)).setText("");
                    //((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                    //((TextView) v).setTextColor(getResources().getColorStateList(R.color.black));//spinner value text color
                    // ((TextView) v.findViewById(android.R.id.text1)).setGravity(Gravity.CENTER);
                    ((TextView) v).setGravity(Gravity.CENTER);

                }
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,
                        parent);
                ((TextView) v).setTextColor(getResources().getColorStateList(
                        R.color.blue));
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount(); // you dont display last item. It is used as hint.
            }

        };
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mobile_topup_amount_spinner.setAdapter(aa);

        mobile_topup_amount_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // amount_tpoup_str = "";
                amount_billpay_str = mobile_topup_amount_spinner.getSelectedItem().toString();
                if (amount_billpay_str != null) {
                    String[] amount = amount_billpay_str.split(" ");
                    amount_without_kd = amount[0];

                }

                try {
                    if (amount_without_kd != "") {
                        tpin_double = Double.parseDouble(amount_without_kd);
                        boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(),  CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
                        if (tpin_double > limits.getTpinLimit() && !guest) {
                            IsBio = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                ((TextView) view).setTextColor(getResources().getColor(R.color.blue));
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mobile_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.mobile_totup_radio_btn:
                        mobile_totup_radio_btn.setChecked(true);
                        mobile_billpay_radio_btn.setChecked(false);
                        mobile_billpayment_type_str = ((RadioButton) findViewById(R.id.mobile_totup_radio_btn)).getText().toString();
                        mobile_billpayment_type_str = "topup";
                        mobile_topup_amount_layout.setVisibility(View.VISIBLE);
                        mobile_billpay_amount_layout.setVisibility(View.GONE);
                        mobile_mobilenumber_edit.setText("");
                        mobile_topup_amount_spinner.setAdapter(aa);
                        mobile_tpin_layout.setVisibility(View.GONE);
                        mobile_tpin_edit.setText("");

                        break;

                    case R.id.mobile_billpay_radio_btn:
                        mobile_totup_radio_btn.setChecked(false);
                        mobile_billpay_radio_btn.setChecked(true);
                        mobile_topup_amount_layout.setVisibility(View.GONE);
                        mobile_billpay_amount_layout.setVisibility(View.VISIBLE);
                        mobile_tpin_layout.setVisibility(View.GONE);
                        mobile_billpayment_type_str = ((RadioButton) findViewById(R.id.mobile_billpay_radio_btn)).getText().toString();
                        mobile_billpayment_type_str = "billpay";
                        mobile_mobilenumber_edit.setText("");
                        mobile_billapy_amount_edit.setText("");
                        mobile_tpin_edit.setText("");


                        break;

                    default:

                        break;
                }
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (mobile_billpayment_type_str == null) {
                    Toast toast = Toast.makeText(MobileBill_L1_Activity.this, "Please select Bill type ", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/

                /*if (mobile_operatorname_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(MobileBill_L1_Activity.this, "Please enter operator name", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                if ((mobile_mobilenumber_edit.getText().toString().length() == 0)) {
                    Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_mobile_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if ((mobile_mobilenumber_edit.getText().toString().length() != 8)) {
                    Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_valid_mobile__number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (mobile_billpay_amount_layout.getVisibility() == View.VISIBLE) {
                    if (mobile_billapy_amount_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_amount), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }
                if (mobile_topup_amount_layout.getVisibility() == View.VISIBLE) {

//                    if (amount_billpay_str.equals("--Select Amount--")) {

                    if (amount_billpay_str.equals(getString(R.string.select_amount))) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_select_amount), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }

                if (mobile_billpay_amount_layout.getVisibility() == View.VISIBLE) {

                    Double _amount = Double.parseDouble(amount_without_kd);
                    if (_amount < limits.getMinValuePerTransaction() || _amount > limits.getMaxValuePerTransaction()) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_between) + limits.getMinValuePerTransaction() + getResources().getString(R.string.mobile_bill_L1_toast_to) + limits.getMaxValuePerTransaction(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }

                }
                if (mobile_topup_amount_layout.getVisibility() == View.VISIBLE) {

                    Double _amount = Double.parseDouble(amount_without_kd);
                    if (_amount < limits.getMinValuePerTransaction() || _amount > limits.getMaxValuePerTransaction()) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_between)+ limits.getMinValuePerTransaction() +getResources().getString(R.string.mobile_bill_L1_toast_to) + limits.getMaxValuePerTransaction(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }

                }
                if (IsBio) {
                    ShowEnterPassword();
                } else
                    DomesticL1Request();
            }
        });
    }

    private void verifyBiometric() {
        try {
            boolean isFingerprintAvailable = false;
            if (!mFragment.isAdded()) {
                boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                        MobileBill_L1_Activity.this, Manifest.permission.USE_FINGERPRINT)
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
                            FingerprintAuthenticationDialogFragmentMobileBill.Stage.PASSWORD);
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
                                FingerprintAuthenticationDialogFragmentMobileBill.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragmentMobileBill.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragmentMobileBill.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        } catch (Exception e) {
            Toast.makeText(MobileBill_L1_Activity.this, " Fingerprint Sensor Exc: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void ShowEnterPassword() {
        promptsViewPassword = new Dialog(this);
        promptsViewPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsViewPassword.setContentView(R.layout.enter_password);
        final EditText pin = promptsViewPassword.findViewById(R.id.pay_via_qrcode_pin_edt);
        final Button pay_qrcode_cancel_btn_new = promptsViewPassword.findViewById(R.id.pay_qrcode_cancel_btn_new);
        final Button verify_password_btn_new = promptsViewPassword.findViewById(R.id.verify_password_btn_new);
        pin.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (pin.getRight() - pin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    verifyBiometric();
                }
            }
            return false;
        });
        pay_qrcode_cancel_btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptsViewPassword.dismiss();
            }
        });
        verify_password_btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pin.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_password_validate), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                tpin = pin.getText().toString().trim();
                DomesticL1Request();
                promptsViewPassword.dismiss();
            }
        });
        promptsViewPassword.show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onResume() {
        super.onResume();



        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Mobile Bill - enter product info - page");



        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 12);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Mobile Bill - enter product info - page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 12 logged");
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

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }

    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_whatsapp, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MobileBill_L1_Activity.this);
        alertDialog.setView(promptsView);
        alertDialog.show();
    }

    public void onPurchased(boolean withFingerprint, String password) {
        boolean bio = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC);
        if(!bio)
            alertDialog();
        else {
            biometricVerified = true;
            DomesticL1Request();
        }
    }

    private void DomesticL1Request() {

        DomesticL1RequestPojo domesticL1RequestPojo = new DomesticL1RequestPojo();

        // String operatorName = mobile_operatorname_edit.getText().toString(jj);
        String operatorName = operatorName_str.trim();
        String mobileNumber = mobile_mobilenumber_edit.getText().toString();
        String operatorType = ((CoreApplication) getApplication()).getOperatorType().toUpperCase();

        domesticL1RequestPojo.setOperatorName(operatorName);
        domesticL1RequestPojo.setMobileNumber(mobileNumber);
        domesticL1RequestPojo.setAmount(amount_without_kd);
        domesticL1RequestPojo.setOperatorType(operatorType);
        domesticL1RequestPojo.setBillPaymentType(mobile_billpayment_type_str.toUpperCase());
        if (biometricVerified) {
            String pin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            domesticL1RequestPojo.setTpin(pin);
        } else
            domesticL1RequestPojo.setTpin(tpin);

        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new DenominationsL1Processing(domesticL1RequestPojo, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }*/

}