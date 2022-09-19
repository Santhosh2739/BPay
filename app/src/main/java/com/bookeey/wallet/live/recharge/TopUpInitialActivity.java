package com.bookeey.wallet.live.recharge;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentTopUp;
import com.bookeey.wallet.live.login.LoginActivity;
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
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.inject.Inject;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.URLUTF8Encoder;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.Internationaltopup.DenominationRequestPojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeFinalResponsePojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeInitiationResponsePojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeRequestPojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeResponsePojo;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.sendmoney.P2PReceiverNumberListResponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;

/**
 * Created by 30099 on 4/27/2016.
 */
public class TopUpInitialActivity extends GenericActivity implements AdapterView.OnItemSelectedListener {
    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME = "my_key";
    ImageView image_person;
    ArrayList<String> search_country;
    ProgressDialog progress = null;
    InternationalRechargeResponsePojo internationalRechargeResponsePojo = null;
    InternationalRechargeFinalResponsePojo internationalRechargeFinalResponsePojo = null;
    HashMap<String, String> hm;
    String countryCode = "";
    String imagepath;
    P2PReceiverNumberListResponse p2PReceiverNumberListResponse = null;
    TextView textname, topup_1_country_code;
    boolean itemselected;
    List<HashMap<String, String>> aList = null;
    LinearLayout topup_1_editmobilenumber_linear;
    String final_countryname, final_countrycode;
    List<String> list = null;
    //newly added
    Spinner spinner;
    TextView top_up_currency_local, top_up_currency_kwd_text, top_up_warning_msg_text, topup_1_dash_text;
    EditText top_up_pin_edt;
    LinearLayout top_up_denominations_layout;
    SimpleAdapter adapter;
    String amountinKwd, amount_currency;
    String local_currency_value, denominationvalue;
    int count = 0;
    InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo = new InternationalRechargeInitiationResponsePojo();
    CustomAutoCompleteTextView autoCompleteTextView;
    SimpleAdapter adapter0;
    Dialog promptsViewPassword;
    boolean IsBio = false;
    String tpin = "";
    boolean biometricVerified = false;
    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    FingerprintAuthenticationDialogFragmentTopUp mFragment;
    @Inject
    SharedPreferences mSharedPreferences;
    InternationalRechargeRequestPojo requestPojo = new InternationalRechargeRequestPojo();
    private Button topup_1_next;
    private CustomTextview topup_1_mobilenumber_edit;
    private DisplayImageOptions options;
    private ImageView flag_country;
    private FirebaseAnalytics firebaseAnalytics;
    private KeyStore mKeyStore;
    private Cipher mCipher;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_initial_layout);

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
        mTitleTextView.setText(getResources().getString(R.string.intl_top_up_title));

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
        ImageView imageView = (ImageView) findViewById(R.id.topup_1_image_person);
        topup_1_country_code = (TextView) findViewById(R.id.topup_1_country_code);
        topup_1_editmobilenumber_linear = (LinearLayout) findViewById(R.id.topup_1_editmobilenumber_linear);

        //newly added
        spinner = (Spinner) findViewById(R.id.topup_denominations_spinner);
        top_up_currency_local = (TextView) findViewById(R.id.top_up_currency_local);
        top_up_currency_kwd_text = (TextView) findViewById(R.id.top_up_currency_kwd_text);
        top_up_pin_edt = (EditText) findViewById(R.id.top_up_pin_edt);
        top_up_denominations_layout = (LinearLayout) findViewById(R.id.top_up_denominations_layout);
        top_up_warning_msg_text = (TextView) findViewById(R.id.top_up_warning_msg_text);
        topup_1_dash_text = (TextView) findViewById(R.id.topup_1_dash_text);
        autoCompleteTextView = (CustomAutoCompleteTextView) findViewById(R.id.input_for_country_code);
        flag_country = (ImageView) findViewById(R.id.flag_country);
        topup_1_mobilenumber_edit = (CustomTextview) findViewById(R.id.topup_1_mobilenumber_edit);


        /*if (!autoCompleteTextView.getText().toString().equalsIgnoreCase("Enter Country Name")) {
            flag_country.setVisibility(View.VISIBLE);
        }*/
        //newly added for navigate from L2 page to previous page
        String bundle_data = getIntent().getStringExtra("BACK");
        if (bundle_data != null && !bundle_data.isEmpty()) {
            autoCompleteTextView.setText("");
            flag_country.setVisibility(View.GONE);
            topup_1_mobilenumber_edit.setText("");
            spinner.setSelection(0);
            top_up_currency_local.setText("");
            top_up_currency_kwd_text.setText("");
            top_up_pin_edt.setText("");
        }


        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoCompleteTextView.showDropDown();
                autoCompleteTextView.requestFocus();
                return false;

            }

            /*@Override
            public void onT(View v) {
                if (!itemselected) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                }
            }*/
        });

        topup_1_mobilenumber_edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!itemselected) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            /*@Override
            public void onT(View v) {
                if (!itemselected) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                }
            }*/
        });

        List<HashMap<String, String>> aList = new ArrayList<>();
        aList.add(addData(getString(R.string.select_denomination), ""));
        String[] from0 = {"txt", "name"};

        int[] to0 = {R.id.txt_country_name_spinner, R.id.txt_country_code_spinner};
        adapter0 = new SimpleAdapter(getBaseContext(), aList, R.layout.customspinner_layout, from0, to0);
        spinner.setAdapter(adapter0);

        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String str = topup_1_mobilenumber_edit.getText().toString();
                if (str.length() == 0)
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_enter_mobile_number), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.topup_1_nameTextooredo, R.id.topup_1_wallet_id, R.id.topup_1_balance_id);
        progress = new ProgressDialog(TopUpInitialActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        getCountryList();

        topup_1_mobilenumber_edit.setKeyImeChangeListener(new CustomTextview.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                count++;
                InputMethodManager manager = (InputMethodManager) topup_1_mobilenumber_edit.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(topup_1_mobilenumber_edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


                if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                    // do something
                    //Toast.makeText(TopUpInitialActivity.this, "Hardware Back Pressed", Toast.LENGTH_SHORT).show();
                    String topup_initial_mobileNumber_edit_str = topup_1_mobilenumber_edit.getText().toString().trim();
                    if (!itemselected) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                    } else if (topup_initial_mobileNumber_edit_str.length() == 0) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {
                        if (count == 1)
                            getDenominations(topup_initial_mobileNumber_edit_str);
                        else {
                            //getDenominations(topup_initial_mobileNumber_edit_str);
                        }
                    }
                }
            }
        });
        //newly added
        topup_1_mobilenumber_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    //Toast.makeText(TopUpInitialActivity.this, "Done Back Pressed", Toast.LENGTH_SHORT).show();
                    String topup_initial_mobileNumber_edit_str = topup_1_mobilenumber_edit.getText().toString().trim();
                    if (!itemselected) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                    } else if (topup_initial_mobileNumber_edit_str.length() == 0) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    } else {

                        hideKeyboard(TopUpInitialActivity.this);
                        getDenominations(topup_initial_mobileNumber_edit_str);
                    }
                }
                return false;
            }
        });


        String[] from = {"txt", "name"};
        int[] to = {R.id.txt_country_name_spinner, R.id.txt_country_code_spinner};
        spinner.setOnItemSelectedListener(this);
        top_up_currency_kwd_text.setText("");
        top_up_currency_local.setText("");
        topup_1_next = (Button) findViewById(R.id.topup_1_next);
        image_person = (ImageView) findViewById(R.id.topup_1_image_person);
        topup_1_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topup_initial_mobileNumber_edit_str = topup_1_mobilenumber_edit.getText().toString().trim();
                if (!itemselected) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_select_country), Toast.LENGTH_SHORT).show();
                } else if (topup_initial_mobileNumber_edit_str.length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_top_up_enter_mobile_number), Toast.LENGTH_SHORT).show();
                } else if (top_up_currency_local.getText().toString().trim().length() == 0 || top_up_currency_kwd_text.getText().toString().trim().length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_select_denomination), Toast.LENGTH_SHORT).show();
                } else {
                    String[] seperated = top_up_currency_kwd_text.getText().toString().split(" ");
                    String[] denomination = top_up_currency_local.getText().toString().split(" ");
                    CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                    final TransactionLimitResponse limitResponse = customerLoginRequestReponse.getFilteredLimits().get("INTL_RECHARGE");

                    requestPojo.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
               /* if (customerLoginRequestReponse.getWalletBalance() < Double.parseDouble(seperated[0])) {
                    Toast.makeText(getBaseContext(), "You don't have sufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                    boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(), CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
                    if (Double.parseDouble(seperated[0]) > limitResponse.getTpinLimit() && !guest) {
                        IsBio = true;
                    } else {
                        IsBio = false;
                        requestPojo.setTpin(null);
                    }
                    requestPojo.setDenominationAmt(Double.parseDouble(denomination[0]));
                    String[] separated1 = top_up_currency_kwd_text.getText().toString().split(" ");
                    requestPojo.setDenominationinKWD(Double.parseDouble(separated1[0]));
                    requestPojo.setCurrencyName(denomination[1]);
                    requestPojo.setMobileNumber(((CoreApplication) getApplication()).getTopup_mobile_number());
                    requestPojo.setCountryCode(((CoreApplication) getApplication()).getCountry_code());
                    requestPojo.setCountryName(((CoreApplication) getApplication()).getCountry_name());
                    requestPojo.setG_transType(TransType.INTERNATIONAL_RECHARGE_L1_REQUEST.name());
                    if (IsBio)
                        ShowEnterPassword();
                    else
                        proceedTopup();
                }
            }
        });
    }

    private void verifyBiometric() {
        try {
            boolean isFingerprintAvailable = false;
            if (!mFragment.isAdded()) {
                boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                        TopUpInitialActivity.this, Manifest.permission.USE_FINGERPRINT)
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
                            FingerprintAuthenticationDialogFragmentTopUp.Stage.PASSWORD);
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
                                FingerprintAuthenticationDialogFragmentTopUp.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragmentTopUp.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragmentTopUp.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        } catch (Exception e) {
            Toast.makeText(TopUpInitialActivity.this, " Fingerprint Sensor Exc: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                if (pin.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_password_validate), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                tpin = pin.getText().toString().trim();
                proceedTopup();
                promptsViewPassword.dismiss();
            }
        });
        promptsViewPassword.show();
    }

    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_whatsapp, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TopUpInitialActivity.this);
        alertDialog.setView(promptsView);
        alertDialog.show();
    }

    public void onPurchased(boolean withFingerprint, String password) {
        boolean bio = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC);
        if (!bio)
            alertDialog();
        else {
            biometricVerified = true;
            tpin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            proceedTopup();
        }
    }

    public void proceedTopup() {
        if(IsBio)
            requestPojo.setTpin(tpin);
        String json = new Gson().toJson(requestPojo);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.INTERNATIONAL_RECHARGE_L1_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_L1_RESPONSE.name())) {
                        internationalRechargeInitiationResponsePojo = new Gson().fromJson((String) msg.obj, InternationalRechargeInitiationResponsePojo.class);
                        ((CoreApplication) getApplication()).setInternationalRechargeInitiationResponsePojo(internationalRechargeInitiationResponsePojo);
                        Intent intent = new Intent(getBaseContext(), TopupL1ScreenActvity.class);
                        startActivity(intent);
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(TopUpInitialActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(TopUpInitialActivity.this, LoginActivity.class);
                        startActivity(intent);
                        TopUpInitialActivity.this.finish();
                        return;
                    } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_L1_RESPONSE.name())) {
                        switch (response.getG_errorDescription()) {
                            case "Invalid password":
                                Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Invalid_Password), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Problem occurs while validating. Please try again":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Problem_occurs_while_validating_Please_try_again), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Monthly Recharge Limits Exceeding":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Monthly_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Daily Recharge Limits Exceeding":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Daily_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Recharge Amount Min & Max":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Recharge_Amount_Min_Max), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Daily & Monthly Limits Not Available":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Number is InActive":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Number_is_InActive), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "Number not Registered":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.number_not_registered), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                break;
                            case "Data Entry Error!Please Check":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Data_Entry_Error_Please_Check), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            case "FAILED":
                                toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.FAILED), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                            default:
                                toast = Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                clearAllFileds();
                                break;
                        }
                        return;
                    } else {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        clearAllFileds();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), true, getApplicationContext())).start();
        showIfNotVisible("");
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("International top up - product selected - page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 14);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "International top up - product selected - page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 14 logged");
    }

    private void clearAllFileds() {
        autoCompleteTextView.setText("");
        flag_country.setVisibility(View.GONE);
        topup_1_mobilenumber_edit.setText("");
        spinner.setSelection(0);
        top_up_currency_local.setText("");
        top_up_currency_kwd_text.setText("");
        top_up_pin_edt.setText("");
        IsBio = false;
    }

    private void getDenominations(String topup_initial_mobileNumber_edit_str) {
        ((CoreApplication) getApplication()).setCountry_code(final_countrycode);
        ((CoreApplication) getApplication()).setTopup_mobile_number(topup_initial_mobileNumber_edit_str);
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        DenominationRequestPojo request = new DenominationRequestPojo();
        request.setMobileNumber(topup_initial_mobileNumber_edit_str);
        request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        request.setG_transType(TransType.INTERNATIONAL_RECHARGE_DENOMINATIONLIST_REQUEST.name());
        request.setCountryCode(final_countrycode);
        request.setCountryName(final_countryname);
        String json = new Gson().toJson(request);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.INTERNATIONAL_RECHARGE_DENOMINATIONLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (!network_response.isEmpty()) {
                        if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_DENOMINATIONLIST_RESPONSE.name())) {
                            internationalRechargeFinalResponsePojo = new Gson().fromJson((String) msg.obj, InternationalRechargeFinalResponsePojo.class);
                            ((CoreApplication) getApplication()).setInternationalRechargeFinalResponsePojo(internationalRechargeFinalResponsePojo);

                            spinner.setVisibility(View.VISIBLE);
                            top_up_denominations_layout.setVisibility(View.VISIBLE);
                            topup_1_next.setVisibility(View.VISIBLE);
                            top_up_currency_kwd_text.setText("");
                            top_up_currency_local.setText("");
                            List<HashMap<String, String>> aList = new ArrayList<>();
                            aList.add(addData(getString(R.string.select_denomination), ""));
                            for (int i = 0; i < internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().size(); i++) {
                                hm = new HashMap<String, String>();
                                hm.put("txt", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getRechargeAmount());
                                hm.put("name", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getAmtInKWD());
                                aList.add(addData(internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getRechargeAmount(), internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getAmtInKWD()));
                            }
                            String[] from = {"txt", "name"};

                            int[] to = {R.id.txt_country_name_spinner, R.id.txt_country_code_spinner};
                            adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.customspinner_layout, from, to);
                            spinner.setAdapter(adapter);
                        /*Intent intent = new Intent(getBaseContext(), TopupScreen2Activity.class);
                        startActivity(intent);*/
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(TopUpInitialActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(TopUpInitialActivity.this, LoginActivity.class);
                            startActivity(intent);
                            TopUpInitialActivity.this.finish();
                            return;
                        } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_DENOMINATIONLIST_RESPONSE.name())) {
                            Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.VISIBLE);
                            spinner.setAdapter(adapter0);
                            top_up_denominations_layout.setVisibility(View.VISIBLE);
                            top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                            top_up_currency_kwd_text.setHint("KWD");
                            topup_1_next.setVisibility(View.VISIBLE);
                            top_up_warning_msg_text.setVisibility(View.VISIBLE);
                            top_up_pin_edt.setVisibility(View.INVISIBLE);
                            top_up_pin_edt.setText("");
                            clearAllFileds();
                            return;
                        } else if (response.getG_errorDescription().equalsIgnoreCase("No Denomination available")) {
                            Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Denomination_Not_available), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            spinner.setVisibility(View.VISIBLE);
                            spinner.setAdapter(adapter0);
                            top_up_denominations_layout.setVisibility(View.VISIBLE);
                            top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                            top_up_currency_kwd_text.setHint("KWD");
                            topup_1_next.setVisibility(View.VISIBLE);
                            top_up_pin_edt.setVisibility(View.INVISIBLE);
                            top_up_pin_edt.setText("");
                            clearAllFileds();
                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.VISIBLE);
                            spinner.setAdapter(adapter0);
                            top_up_denominations_layout.setVisibility(View.VISIBLE);
                            top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                            top_up_currency_kwd_text.setHint("KWD");
                            topup_1_next.setVisibility(View.VISIBLE);
                            top_up_pin_edt.setVisibility(View.INVISIBLE);
                            top_up_pin_edt.setText("");
                            clearAllFileds();
                            return;
                        }
                    } else {
                        spinner.setVisibility(View.VISIBLE);
                        spinner.setAdapter(adapter0);
                        top_up_denominations_layout.setVisibility(View.VISIBLE);
                        top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                        top_up_currency_kwd_text.setHint("KWD");
                        topup_1_next.setVisibility(View.VISIBLE);
                        top_up_pin_edt.setVisibility(View.INVISIBLE);

                        top_up_pin_edt.setText("");
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
                        clearAllFileds();

                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setAdapter(adapter0);
                    top_up_denominations_layout.setVisibility(View.VISIBLE);
                    top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                    top_up_currency_kwd_text.setHint("KWD");
                    topup_1_next.setVisibility(View.VISIBLE);
                    top_up_pin_edt.setVisibility(View.INVISIBLE);
                    top_up_pin_edt.setText("");
                    clearAllFileds();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.VISIBLE);
                    spinner.setAdapter(adapter0);
                    top_up_denominations_layout.setVisibility(View.VISIBLE);
                    top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                    top_up_currency_kwd_text.setHint("KWD");
                    topup_1_next.setVisibility(View.VISIBLE);
                    top_up_pin_edt.setVisibility(View.INVISIBLE);
                    top_up_pin_edt.setText("");
                    clearAllFileds();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    private void getCountryList() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.INTERNATIONAL_RECHARGE_COUNTRYLIST_REQUEST.name());
        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.INTERNATIONAL_RECHARGE_COUNTRYLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    // int[]flags=new int[aList.size()];
                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_COUNTRYLIST_RESPONSE.name())) {
                        internationalRechargeFinalResponsePojo = new Gson().fromJson((String) msg.obj, InternationalRechargeFinalResponsePojo.class);
                        aList = new ArrayList<HashMap<String, String>>();
                        for (int i = 0; i < internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getCountryNameAndCodeDetails().size(); i++) {
                            hm = new HashMap<String, String>();
                            hm.put("txt", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getCountryNameAndCodeDetails().get(i).getCountryCode());
                            hm.put("name", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getCountryNameAndCodeDetails().get(i).getCountryName());
                            hm.put("flag", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getCountryNameAndCodeDetails().get(i).getFlag());
                            //hm.put("flag",""+Uri.parse(internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getCountryNameAndCodeDetails().get(i).getFlag()));
                            aList.add(hm);
                        }
                        String[] from = {"txt", "name"};
                        int[] to = {R.id.txt_country_code, R.id.txt_country_name};

                        myAdapter adapter = new myAdapter(getBaseContext(), aList, R.layout.customautocompletetext,
                                from, to);
                        // SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.customautocompletetext, from, to);
                        autoCompleteTextView.setThreshold(1);
                        autoCompleteTextView.setAdapter(adapter);

                        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                flag_country.setVisibility(View.GONE);
                                itemselected = false;
                                spinner.setVisibility(View.VISIBLE);
                                spinner.setAdapter(adapter0);
                                top_up_denominations_layout.setVisibility(View.VISIBLE);
                                top_up_currency_local.setHint(getString(R.string.amount_for_intl_topup));
                                top_up_currency_kwd_text.setHint("KWD");
                                topup_1_next.setVisibility(View.VISIBLE);
                                top_up_pin_edt.setVisibility(View.INVISIBLE);
                                top_up_pin_edt.setText("");

                                //topup_1_editmobilenumber_linear.setVisibility(View.INVISIBLE);
                                //topup_1_mobilenumber_edit.setVisibility(View.INVISIBLE);
                                //topup_1_country_code.setVisibility(View.INVISIBLE);
                                topup_1_mobilenumber_edit.setText("");
                                //spinner.setVisibility(View.INVISIBLE);
                                //top_up_denominations_layout.setVisibility(View.INVISIBLE);
                                //top_up_warning_msg_text.setVisibility(View.INVISIBLE);
                                //top_up_pin_edt.setVisibility(View.INVISIBLE);
                                //topup_1_next.setVisibility(View.INVISIBLE);
                                top_up_pin_edt.setText("");
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                itemselected = false;
                            }
                        });
                        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
                                imagepath = hm.get("flag");
                                final_countryname = hm.get("name");
                                final_countrycode = "+" + hm.get("txt");
                                itemselected = true;
                                flag_country.setVisibility(View.VISIBLE);
                                topup_1_mobilenumber_edit.setVisibility(View.VISIBLE);
                                topup_1_country_code.setVisibility(View.VISIBLE);
                                topup_1_editmobilenumber_linear.setVisibility(View.VISIBLE);
                                getMobileNumberList();
                                topup_1_country_code.setText(final_countrycode);
                                //logo_image.setImageURI(Uri.parse(images[position]));

                                flag_country.setVisibility(View.VISIBLE);

                                countryCode = parent.getItemAtPosition(position).toString();
                                ((CoreApplication) getApplication()).setCountry_flag(imagepath);
                                ((CoreApplication) getApplication()).setCountry_name(final_countryname);
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
                                        .displayImage(imagepath, flag_country, options, new SimpleImageLoadingListener() {
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
                            }
                        };
                        autoCompleteTextView.setOnItemClickListener(itemClickListener);

                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(TopUpInitialActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(TopUpInitialActivity.this, LoginActivity.class);
                        startActivity(intent);
                        TopUpInitialActivity.this.finish();
                        return;
                    } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_COUNTRYLIST_RESPONSE.name())) {
                        Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        clearAllFileds();
                        return;
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Service are currently down. Please try again")) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.Service_are_currently_down_Please_try_again), Toast.LENGTH_SHORT).show();
                        clearAllFileds();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        clearAllFileds();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                    return;
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

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
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

    private void getMobileNumberList() {
        topup_1_mobilenumber_edit.setGravity(Gravity.LEFT | Gravity.CENTER);
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        DenominationRequestPojo request = new DenominationRequestPojo();
        request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        request.setG_transType(TransType.INTERNATIONAL_RECHARGE_RECIPIENTLIST_REQUEST.name());
        request.setCountryCode(final_countrycode);
        request.setCountryName(final_countryname);
        String json = new Gson().toJson(request);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.INTERNATIONAL_RECHARGE_RECIPIENTLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_RECIPIENTLIST_RESPONSE.name())) {
                        p2PReceiverNumberListResponse = new Gson().fromJson((String) msg.obj, P2PReceiverNumberListResponse.class);
                        list = new ArrayList<String>();
                        topup_1_dash_text.setVisibility(View.VISIBLE);
                        topup_1_country_code.setVisibility(View.VISIBLE);
                        for (int i = 0; i < p2PReceiverNumberListResponse.getMobileNumber().size(); i++) {
                            String mobilenumber = p2PReceiverNumberListResponse.getMobileNumber().get(i);

                            list.add(mobilenumber);
                        }

                        topup_1_mobilenumber_edit.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                topup_1_mobilenumber_edit.showDropDown();
                                topup_1_mobilenumber_edit.requestFocus();
                                return false;
                            }
                        });
                        ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.customautocompletetextview, list);
                        topup_1_mobilenumber_edit.setThreshold(1);
                        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String value = parent.getAdapter().getItem(position).toString();
                                topup_1_mobilenumber_edit.setText(value);
                                getDenominations(value);
                                hidekeyBoard(TopUpInitialActivity.this);
                            }
                        };
                        topup_1_mobilenumber_edit.setOnItemClickListener(itemClickListener);
                        topup_1_mobilenumber_edit.setAdapter(adapter);
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(TopUpInitialActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(TopUpInitialActivity.this, LoginActivity.class);
                        startActivity(intent);
                        TopUpInitialActivity.this.finish();
                        return;
                    } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_RECIPIENTLIST_RESPONSE.name())) {
                        //Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Service are currently down. Please try again")) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.Service_are_currently_down_Please_try_again), Toast.LENGTH_SHORT).show();
                        clearAllFileds();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        clearAllFileds();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    clearAllFileds();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");
    }

    private void hidekeyBoard(TopUpInitialActivity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private HashMap<String, String> addData(String local, String kwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("txt", kwd);
        map.put("name", local);
        return map;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
        if (hm.get("name").equalsIgnoreCase("Select Denomination")) {
            top_up_currency_local.setText("");
            top_up_pin_edt.setVisibility(View.INVISIBLE);
            top_up_pin_edt.setText("");
        } else {
            top_up_currency_local.setText(hm.get("name"));
        }
        top_up_warning_msg_text.setVisibility(View.VISIBLE);
        top_up_currency_kwd_text.setText(hm.get("txt"));
        //  top_up_currency_local.setText(hm.get("name"));
        denominationvalue = hm.get("txt");
        //itemselected = true;

        if (top_up_currency_kwd_text.getText().toString().trim().length() != 0) {
            String[] values = top_up_currency_kwd_text.getText().toString().split(" ");
            CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
            final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("INTL_RECHARGE");
            boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(),  CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
            if (Double.parseDouble(values[0]) > limits.getTpinLimit() && !guest) {
                //top_up_pin_edt.setVisibility(View.VISIBLE);
                IsBio = true;
            } else {
                //top_up_pin_edt.setVisibility(View.INVISIBLE);
                //top_up_pin_edt.setText("");
                IsBio = false;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class myAdapter extends SimpleAdapter {
        Context context;

        public myAdapter(Context baseContext, List<HashMap<String, String>> aList, int customautocompletetext, String[] from, int[] to) {
            super(baseContext, aList, customautocompletetext, from, to);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.customautocompletetext,
                        null);
            }
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView nameTextView = (TextView) convertView.findViewById(R.id.txt_country_name);
            ImageView iconImageView = (ImageView) convertView.findViewById(R.id.logo_image);
            String nameString = (String) data.get("name");
            nameTextView.setText(nameString);
            String imagePath = (String) data.get("flag");


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
                    .displayImage(imagePath, iconImageView, options, new SimpleImageLoadingListener() {
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
            return convertView;
        }
    }
}