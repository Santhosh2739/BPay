package com.bookeey.wallet.live.sendmoney;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.FingerprintAuthenticationDialogFragmentSendMoney;
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
import coreframework.network.ServerConnection;
import coreframework.processing.SendMoneyPhase1Processing;
import coreframework.taskframework.DecimalDigitsInputFilter;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.sendmoney.P2PReceiverNumberListResponse;
import ycash.wallet.json.pojo.sendmoney.PeerToPeerTranRequest;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;

/**
 * @author mohit
 */
public class SendMoneyLeg1RequestActivity extends GenericActivity implements YPCHeadlessCallback {
    private static final int FINGERPRINT_PERMISSION_REQUEST_CODE = 0;
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String KEY_NAME = "my_key";
    RadioGroup sendmoney_radioGroup1;
    RadioButton sendmoney_local_radio, sendmoney_international_radio;
    LinearLayout send_money_international;
    LinearLayout send_money_local;
    ImageView send_money_selectcontact_local_image;
    EditText send_money_to_mobile_amount_edit, ooredoo_sendmoneyscreeen_local_pin_edt;
    LinearLayout ooredoo_sendmoneyscreeen_pin_layout;
    View pinlayout_line_view;
    PeerToPeerTranRequest peerToPeerTranRequest;
    Double amount = 0.000;
    int RQS_PICK_CONTACT = 1;
    AutoCompleteTextView send_money_to_mobile_recepient_mobile_no_edit;
    ProgressDialog progress = null;
    List<String> aList = null;
    P2PReceiverNumberListResponse p2PReceiverNumberListResponse = null;
    Dialog promptsViewPassword;
    boolean IsBio = false;
    String tpin = "";
    @Inject
    FingerprintManagerCompat mFingerprintManager;
    @Inject
    FingerprintAuthenticationDialogFragmentSendMoney mFragment;
    @Inject
    SharedPreferences mSharedPreferences;
    private FirebaseAnalytics firebaseAnalytics;
    private KeyStore mKeyStore;
    private Cipher mCipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_money_new);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        send_money_selectcontact_local_image = (ImageView) findViewById(R.id.send_money_selectcontact_local_image);

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
                onBackPressed();
            }
        });
        ((CoreApplication) getApplication()).inject(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT},
                FINGERPRINT_PERMISSION_REQUEST_CODE);
        progress = new ProgressDialog(SendMoneyLeg1RequestActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        getMobileNumberList();
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }

        send_money_to_mobile_recepient_mobile_no_edit = (AutoCompleteTextView) findViewById(R.id.send_money_to_mobile_recepient_mobile_no_complete_edt);
        send_money_to_mobile_amount_edit = (EditText) findViewById(R.id.send_money_to_mobile_amount_edit);
        CustomerLoginRequestReponse response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = response.getFilteredLimits().get("SENDER");
        send_money_to_mobile_amount_edit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});
        ooredoo_sendmoneyscreeen_pin_layout = (LinearLayout) findViewById(R.id.ooredoo_sendmoneyscreeen_pin_layout);
        ooredoo_sendmoneyscreeen_local_pin_edt = (EditText) findViewById(R.id.ooredoo_sendmoneyscreeen_local_pin_edt);
        ooredoo_sendmoneyscreeen_pin_layout.setVisibility(View.INVISIBLE);
        pinlayout_line_view = (View) findViewById(R.id.pinlayout_line_view);
        pinlayout_line_view.setVisibility(View.GONE);

        send_money_to_mobile_amount_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amount_str = send_money_to_mobile_amount_edit.getText().toString().trim();
                int indexofDesc = amount_str.indexOf(".");
                if (indexofDesc > 4) {
                    send_money_to_mobile_amount_edit.setInputType(InputType.TYPE_NULL);
                }


                try {
                    amount = Double.parseDouble(amount_str);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
                }

                boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(), CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
                //ooredoo_sendmoneyscreeen_pin_layout.setVisibility(View.VISIBLE);
                //pinlayout_line_view.setVisibility(View.VISIBLE);
                /* Toast toast = Toast.makeText(getBaseContext(), "Please enter TPIN", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();*/
                //ooredoo_sendmoneyscreeen_pin_layout.setVisibility(View.GONE);
                //pinlayout_line_view.setVisibility(View.GONE);
                IsBio = amount > limits.getTpinLimit() && !guest;
            }
        });
        updateProfile(R.id.sendmoney_nameTextooredo, R.id.sendmoney_wallet_id, R.id.sendmoney_balance_id);

        findViewById(R.id.send_money_local_mobile_ok_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // if (sendmoney_local_radio.isChecked()) {
                String local_recipientmobilenumber = ((EditText) findViewById(R.id.send_money_to_mobile_recepient_mobile_no_complete_edt)).getText().toString().trim();
                String string_amount = ((EditText) findViewById(R.id.send_money_to_mobile_amount_edit)).getText().toString().trim();
                //String string_pin = ((EditText) findViewById(R.id.ooredoo_sendmoneyscreeen_local_pin_edt)).getText().toString().trim();

                if (local_recipientmobilenumber.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.send_money_enter_mobile_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (local_recipientmobilenumber.length() != 8) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.send_money_enter_valid_mobile_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (string_amount.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.send_money_enter_amount), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                Double _amount = Double.parseDouble(string_amount);
                if (_amount < limits.getMinValuePerTransaction() || _amount > limits.getMaxValuePerTransaction()) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.send_money_amount_between) + limits.getMinValuePerTransaction() + getResources().getString(R.string.send_money_amount_between_to) + limits.getMaxValuePerTransaction(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                peerToPeerTranRequest = new PeerToPeerTranRequest();
                peerToPeerTranRequest.setTxnAmount(Double.parseDouble(string_amount));
                peerToPeerTranRequest.setRecipientName(null);
                peerToPeerTranRequest.setTpin("");
                peerToPeerTranRequest.setRecipientMobileNumber(local_recipientmobilenumber);
                if (IsBio)
                    ShowEnterPassword();
                else {
                    sendMoneyProcessing();
                }
            }
        });
        send_money_selectcontact_local_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });
        sendmoney_radioGroup1 = (RadioGroup) findViewById(R.id.sendmoney_radioGroup1);
        send_money_local = (LinearLayout) findViewById(R.id.send_money_local);
        sendmoney_local_radio = (RadioButton) findViewById(R.id.sendmoney_local_radio);
        sendmoney_international_radio = (RadioButton) findViewById(R.id.sendmoney_international_radio);

        sendmoney_radioGroup1
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(
                            RadioGroup ooredoo_sendmoney_radioGroup1,
                            int checked) {
                        // TODO Auto-generated method stub
                        if (sendmoney_local_radio.isChecked()) {

                            send_money_international.setVisibility(View.GONE);
                            send_money_local.setVisibility(View.VISIBLE);

                        } else if (sendmoney_international_radio
                                .isChecked()) {

                            send_money_local.setVisibility(View.GONE);
                            send_money_international.setVisibility(View.VISIBLE);

                        }

                    }
                });
    }

    private void verifyBiometric() {
        try {
            boolean isFingerprintAvailable = false;
            if (!mFragment.isAdded()) {
                boolean isFingerprintPermissionGranted = ActivityCompat.checkSelfPermission(
                        SendMoneyLeg1RequestActivity.this, Manifest.permission.USE_FINGERPRINT)
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
                            FingerprintAuthenticationDialogFragmentSendMoney.Stage.PASSWORD);
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
                                FingerprintAuthenticationDialogFragmentSendMoney.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragmentSendMoney.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got
                    // enrolled. Thus show the dialog to authenticate with their password first
                    // and ask the user if they want to authenticate with fingerprints in the
                    // future
                    mFragment.setCryptoObject(new FingerprintManagerCompat.CryptoObject(mCipher));
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragmentSendMoney.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        } catch (Exception e) {
            Toast.makeText(SendMoneyLeg1RequestActivity.this, " Fingerprint Sensor Exc: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                sendMoneyProcessing();
                promptsViewPassword.dismiss();
            }
        });
        promptsViewPassword.show();
    }

    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_whatsapp, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendMoneyLeg1RequestActivity.this);
        alertDialog.setView(promptsView);
        alertDialog.show();
    }

    public void onPurchased(boolean withFingerprint, String password) {
        boolean bio = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC);
        if (!bio)
            alertDialog();
        else {
            tpin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            sendMoneyProcessing();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Send money - enter mobile no & amount page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 8);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Send money - enter mobile no & amount page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 8 logged");

    }

    private void sendMoneyProcessing() {
        if (IsBio)
            peerToPeerTranRequest.setTpin(tpin);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new SendMoneyPhase1Processing(peerToPeerTranRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();

                String number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String str = number.replaceAll(" ", "").trim();
                str = str.replaceAll("-", "");
                if (str.length() == 8) {
                    send_money_to_mobile_recepient_mobile_no_edit.setText(str);
                } else if (str.startsWith("+965")) {
                    str = str.replaceAll("\\+965", "");
                    send_money_to_mobile_recepient_mobile_no_edit.setText(str);
                } else {
                    int start = str.length() - 8;
                    str = str.substring(start);
                    send_money_to_mobile_recepient_mobile_no_edit.setText(str);
                }
//                //contactName.setText(name);
//                String str = number.replaceAll("\\+965", "");
//                str = str.replaceAll("-", "");
//                str = str.replaceAll(" ", "");
//                send_money_to_mobile_recepient_mobile_no_edit.setText(str.trim());
                //contactEmail.setText(email);
            }
        }
    }

    @Override
    public void handleProfileUpdate() {
        super.handleProfileUpdate();
        updateProfile(R.id.sendmoney_nameTextooredo, R.id.sendmoney_wallet_id, R.id.sendmoney_balance_id);
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
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.PEERTOPEER_RECEIVER_MOBILELIST_REQUEST.name());

        String json = new Gson().toJson(genericRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PEERTOPEER_RECEIVER_MOBILELIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {


                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.PEERTOPEER_RECEIVER_MOBILELIST_RESPONSE.name())) {
                        p2PReceiverNumberListResponse = new Gson().fromJson((String) msg.obj, P2PReceiverNumberListResponse.class);
                        aList = new ArrayList<String>();

                        for (int i = 0; i < p2PReceiverNumberListResponse.getMobileNumber().size(); i++) {
                            String mobilenumber = p2PReceiverNumberListResponse.getMobileNumber().get(i);

                            aList.add(mobilenumber);
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.customautocompletetextview, aList);
                        send_money_to_mobile_recepient_mobile_no_edit.setThreshold(1);
                        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String value = parent.getAdapter().getItem(position).toString();
                                send_money_to_mobile_recepient_mobile_no_edit.setText(value);
                            }
                        };
                        send_money_to_mobile_recepient_mobile_no_edit.setOnItemClickListener(itemClickListener);
                        send_money_to_mobile_recepient_mobile_no_edit.setAdapter(adapter);
                    } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.PEERTOPEER_RECEIVER_MOBILELIST_RESPONSE.name())) {
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), "General server error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), "Failure general server", Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), "Failure network connection", Toast.LENGTH_SHORT).show();
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

    public class ItemData {
        String text;
        Integer imageId;


        public ItemData(String text, Integer imageId) {
            this.text = text;
            this.imageId = imageId;

        }


        public String getText() {
            return text;

        }

        public Integer getImageId() {

            return imageId;
        }

    }
}