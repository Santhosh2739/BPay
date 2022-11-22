package com.bookeey.wallet.live.prepaidcard;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.mobile.DenominationsL1Processing;
import coreframework.processing.prepaidcard.VocherL1Processing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.utils.URLUTF8Encoder;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import util.Util;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL1RequestPojo;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.CardDetails;
import ycash.wallet.json.pojo.virtualprepaidcards.DenominationRequest;
import ycash.wallet.json.pojo.virtualprepaidcards.PrepaidCardsListResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.VoucherLRequest;

/**
 * Created by 10037 on 7/11/2017.
 */

public class VirtualPrepaidMainActivityNew extends GenericActivity {
    static Spinner virtual_prepaid_cardtype_spinner, virtual_prepaid_store_spinner,
            virtual_prepaid_cardvalue_spinner;
    static EditText virtual_prepaid_tpin_edit, virtual_prepaid_store_edit;
    static TextView virtual_prepaid_price_value_text;
    List<String> namelist = new ArrayList<String>();
    Map<String, Integer> cardIdMap = new HashMap<String, Integer>();
    List<String> imagelist = new ArrayList<String>();
    List<String> operator_typelist = new ArrayList<String>();
    List<String> denomlist = new ArrayList<String>();
    List<String> denomKDlist = new ArrayList<String>();
    List<String> storelist = new ArrayList<String>();
    String cardtype_str = "", operatorType_str = "", quantity_str = "1", card_value_str = "", store_value_str = "", card_value_KD_str = "";
    String operator_2_res_name = "", operator_2_res_type = "", store_name_2_res = "";
    int value = 0;
    PrepaidCardsListResponse prepaidCardsListResponse = null;
    PrepaidCardsListResponse prepaidCardsListResponse2 = null;
    String[] names = null;
    String response_str = null;
    ProgressDialog progress;
    Button virtual_prepaid_proceed_button;
    StringBuffer sBuffer = null;
    LinearLayout petrol_cards_layout, virtual_cards_layout = null;
    int cardId = 0;
    EditText petrol_card_number, petrol_card_amount_to_pay = null;
    boolean IsBio = false;
    Dialog promptsViewPassword;
    boolean biometricVerified = false;
    String tpin;
    String moduleName;
    private DisplayImageOptions options;
    private FirebaseAnalytics firebaseAnalytics;
    private Executor executor;
    private BiometricPrompt biometricPrompt;

    public static void clearAllFields() {
        virtual_prepaid_cardtype_spinner.setSelection(0);
        virtual_prepaid_store_spinner.setSelection(0);
        virtual_prepaid_store_edit.setText("");
        virtual_prepaid_cardvalue_spinner.setSelection(0);
        virtual_prepaid_tpin_edit.setText("");
        virtual_prepaid_price_value_text.setText("0.00 KD");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.virtual_prepaid_activity_main);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("VIRTUAL PREPAID CARDS");*/


        //For petrol cards
        petrol_cards_layout = (LinearLayout) findViewById(R.id.petrol_cards_layout);
        virtual_cards_layout = (LinearLayout) findViewById(R.id.virtual_cards_layout);

        petrol_card_number = (EditText) findViewById(R.id.petrol_card_number);
        petrol_card_amount_to_pay = (EditText) findViewById(R.id.petrol_card_amount_to_pay);

        //Alfa Test
//        petrol_card_number.setText("7005430001000676933");
//        petrol_card_amount_to_pay.setText("1");


        //Oula Test
//        petrol_card_number.setText("7000740013311998323");
//        petrol_card_amount_to_pay.setText("1");

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
        moduleName = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MODULE);
        if (moduleName.equalsIgnoreCase("utility bills")) {
            mTitleTextView.setText(getResources().getString(R.string.mainmenu_utility_bills));
        } else {
            mTitleTextView.setText(getResources().getString(R.string.virtual_prepaid_title));
        }
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progress = new ProgressDialog(VirtualPrepaidMainActivityNew.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        response_str = getIntent().getStringExtra("CARD_RESPONSE");
        CustomSharedPreferences.saveStringData(VirtualPrepaidMainActivityNew.this, response_str, CustomSharedPreferences.SP_KEY.PREPAID_RESPONSE);


        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.virtuap_prepaid_nameTextooredo, R.id.virtuap_prepaid_wallet_id, R.id.virtuap_prepaid_balance_id);
        prepaidCardsListResponse = new Gson().fromJson(response_str, PrepaidCardsListResponse.class);

//        namelist.add("Select Card Type");
        namelist.add("" + getString(R.string.select_card_type));

        operator_typelist.add("");
        imagelist.add("");
        for (int i = 0; i < prepaidCardsListResponse.getPriceList().size(); i++) {
            String cardname = prepaidCardsListResponse.getPriceList().get(i).getCardName().toString();
            String operatorType = prepaidCardsListResponse.getPriceList().get(i).getOperatorType().toString();
            String images = prepaidCardsListResponse.getPriceList().get(i).getImages().toString();


            CardDetails cd = prepaidCardsListResponse.getPriceList().get(i);

            if (cd != null) {
                int cardId = prepaidCardsListResponse.getPriceList().get(i).getCardId();
                cardIdMap.put(cardname, cardId);
            }


            namelist.add(cardname);
            imagelist.add(images);

            operator_typelist.add(operatorType);
        }

        petrol_card_amount_to_pay.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (s.length() > 0) {
                    totalKDvalue(s.toString());
                }
            }
        });


        virtual_prepaid_cardtype_spinner = (Spinner) findViewById(R.id.virtual_prepaid_cardtype_spinner);
        virtual_prepaid_store_edit = (EditText) findViewById(R.id.virtual_prepaid_store_edit);
        virtual_prepaid_cardvalue_spinner = (Spinner) findViewById(R.id.virtual_prepaid_cardvalue_spinner);
        virtual_prepaid_store_spinner = (Spinner) findViewById(R.id.virtual_prepaid_store_spinner);
        virtual_prepaid_price_value_text = (TextView) findViewById(R.id.virtual_prepaid_price_value_text);
        virtual_prepaid_proceed_button = (Button) findViewById(R.id.virtual_prepaid_proceed_button);


        virtual_prepaid_tpin_edit = (EditText) findViewById(R.id.virtual_prepaid_tpin_edit);
        //newly added for navigate from L2 page to previous page
        String bundle_data = getIntent().getStringExtra("BACK");
        if (bundle_data != null && !bundle_data.isEmpty()) {
            virtual_prepaid_cardtype_spinner.setSelection(0);
            virtual_prepaid_store_spinner.setSelection(0);
            virtual_prepaid_store_edit.setText("");
            virtual_prepaid_cardvalue_spinner.setSelection(0);
            virtual_prepaid_tpin_edit.setText("");
            virtual_prepaid_price_value_text.setText("0.00 KD");
        }

        names = new String[namelist.size()];
        names = namelist.toArray(names);

        String[] images = new String[imagelist.size()];
        images = imagelist.toArray(images);

//        denomlist.add("Select Card Value");
//        denomKDlist.add("Select Card Value");

        denomlist.add(getString(R.string.select_card_value));
        denomKDlist.add(getString(R.string.select_card_value));


        final DenomAmount denominations = new DenomAmount(getApplicationContext(), denomlist);
        virtual_prepaid_cardvalue_spinner.setAdapter(denominations);

//        storelist.add("Select Store Type");
        storelist.add(getString(R.string.select_store_type));

        final StoreType store = new StoreType(getApplicationContext(), storelist);
        virtual_prepaid_store_spinner.setAdapter(store);

        //creating custom adapter to load text and image
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), names, images);
        virtual_prepaid_cardtype_spinner.setAdapter(customAdapter);
        virtual_prepaid_cardtype_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store.notifyDataSetChanged();
                denominations.notifyDataSetChanged();

                if (position > 0) {

                    cardtype_str = names[position];
                    cardId = cardIdMap.get(cardtype_str);

//                    Toast.makeText(VirtualPrepaidMainActivityNew.this,cardtype_str+" Card ID: "+cardId,Toast.LENGTH_LONG).show();


                }


                if (cardId == 1) {
                    petrol_cards_layout.setVisibility(View.GONE);
                    virtual_cards_layout.setVisibility(View.VISIBLE);
                }
                if (cardId == 6) {
                    petrol_cards_layout.setVisibility(View.VISIBLE);
                    virtual_cards_layout.setVisibility(View.GONE);

                    petrol_card_number.setHint("" + cardtype_str + " Card number");
                }


                virtual_prepaid_tpin_edit.setText("");
                virtual_prepaid_tpin_edit.setVisibility(View.GONE);
                virtual_prepaid_store_spinner.setSelection(0);
                virtual_prepaid_cardvalue_spinner.setSelection(0);

                denomlist.clear();
                denomKDlist.clear();
                storelist.clear();
//                denomlist.add("Select Card Value");
//                denomKDlist.add("Select Card Value");
//                storelist.add("Select Store Type");

                denomlist.add(getString(R.string.select_card_value));
                denomKDlist.add(getString(R.string.select_card_value));
                storelist.add(getString(R.string.select_store_type));

                virtual_prepaid_store_edit.setText("");
                virtual_prepaid_store_edit.setVisibility(view.GONE);
                virtual_prepaid_store_spinner.setVisibility(view.VISIBLE);


//                if (cardtype_str.equalsIgnoreCase("Select Card Type")) {
//                    cardtype_str = "Select Card Type";
                if (cardtype_str.equalsIgnoreCase(getString(R.string.select_card_type))) {
                    cardtype_str = getString(R.string.select_card_type);
                    operatorType_str = "";
                } else {
                    operatorType_str = operator_typelist.get(position);

                    if (cardId == 1) {
                        storeRequest(cardtype_str);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        virtual_prepaid_store_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store_value_str = storelist.get(position);
                virtual_prepaid_tpin_edit.setText("");
                virtual_prepaid_tpin_edit.setVisibility(View.GONE);
                store.notifyDataSetChanged();
                denominations.notifyDataSetChanged();
                virtual_prepaid_cardvalue_spinner.setSelection(0);
                denomlist.clear();
                denomKDlist.clear();

//                denomlist.add("Select Card Value");
//                denomKDlist.add("Select Card Value");

                denomlist.add(getString(R.string.select_card_value));
                denomKDlist.add(getString(R.string.select_card_value));

//                if (store_value_str.equalsIgnoreCase("Select Store Type")) {
//                    store_value_str = "Select Store Type";

                if (store_value_str.equalsIgnoreCase(getString(R.string.select_store_type))) {
                    store_value_str = getString(R.string.select_store_type);
                    operatorType_str = "";
                } else {
                    denominationsRequest(cardtype_str, store_value_str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        virtual_prepaid_cardvalue_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                virtual_prepaid_tpin_edit.setText("");
                store.notifyDataSetChanged();
                denominations.notifyDataSetChanged();
                card_value_str = denomlist.get(position).toString();
                card_value_KD_str = denomKDlist.get(position).toString();
//                if (card_value_str == "Select Card Value") {

                if (card_value_str == getString(R.string.select_card_value)) {
                    totalKDvalue(card_value_str);
                    virtual_prepaid_tpin_edit.setText("");
                    virtual_prepaid_tpin_edit.setVisibility(View.GONE);
                } else {
                    card_value_str = denomlist.get(position).toString();
                    card_value_KD_str = denomKDlist.get(position).toString();
                    totalKDvalue(card_value_KD_str);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        virtual_prepaid_proceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cardId == 1) {

//                if (cardtype_str.equals("Select Card Type")) {
                    if (cardtype_str.equals(getString(R.string.select_card_type))) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.virtual_prepaid_card_type), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                    if (virtual_prepaid_store_spinner.getVisibility() == View.VISIBLE) {
//                    if (store_value_str.equals("Select Store Type")) {

                        if (store_value_str.equals(getString(R.string.select_store_type))) {
                            Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.virtual_prepaid_store_type), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            return;
                        }
                    }
//                if (card_value_str.equals("Select Card Value")) {

                    if (card_value_str.equals(getString(R.string.select_card_value))) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.virtual_prepaid_card_value), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                    if (virtual_prepaid_store_edit.getVisibility() == View.VISIBLE) {
                        if (virtual_prepaid_store_edit.getText().toString().trim().equalsIgnoreCase("Store")) {
                            Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, getResources().getString(R.string.virtual_prepaid_store), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            return;
                        }
                    }

                /*if (virtual_prepaid_tpin_edit.getVisibility() == View.VISIBLE) {
                    if (virtual_prepaid_tpin_edit.getText().toString().trim().length() == 0) {
                        Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, getResources().getString(R.string.virtual_prepaid_password), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }*/

                    boolean biometric_device = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
                    boolean biometric_enabled = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
                    if (IsBio) {
                        value = 1;
                        if (biometric_device && biometric_enabled)
                            biometricPrompt.authenticate(Util.GetBiometricDialog());
                        else
                            ShowEnterPassword();
                    } else
                        proceedL1Request();
                    //proceedL1Request();

                } else if (cardId == 6) {

                    if (petrol_card_number.getVisibility() == View.VISIBLE) {
                        if (petrol_card_number.getText().toString().trim().length() == 0) {
                            Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, "Please enter card number", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            return;
                        }
                    }

                    if (petrol_card_amount_to_pay.getVisibility() == View.VISIBLE) {
                        if (petrol_card_number.getText().toString().trim().length() == 0) {
                            Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, "Please enter amount", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            return;
                        }
                    }
                    boolean biometric_device = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
                    boolean biometric_enabled = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
                    if (IsBio) {
                        value = 2;
                        if (biometric_device && biometric_enabled)
                            biometricPrompt.authenticate(Util.GetBiometricDialog());
                        else
                            ShowEnterPassword();
                    } else
                        domesticL1Request();

                }

            }
        });

        boolean biometric_enabled = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "" + errString, Toast.LENGTH_LONG).show();
                ShowEnterPassword();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // Toast.makeText(getApplicationContext(),"Authentication succeeded!", Toast.LENGTH_SHORT).show();
                if (biometric_enabled) {
                    biometricVerified = true;
                    if (value == 1)
                        proceedL1Request();
                    else if (value == 2)
                        domesticL1Request();
                } else
                    Util.EnableBiometricAlert(VirtualPrepaidMainActivityNew.this);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void ShowEnterPassword() {
        promptsViewPassword = new Dialog(this);
        promptsViewPassword.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsViewPassword.setContentView(R.layout.enter_password);
        final TextView enter_pwd_title = promptsViewPassword.findViewById(R.id.enter_pwd_title);
        final Button pay_qrcode_cancel_btn_new = promptsViewPassword.findViewById(R.id.pay_qrcode_cancel_btn_new);
        final Button verify_password_btn_new = promptsViewPassword.findViewById(R.id.verify_password_btn_new);
        final EditText pin;
        boolean biometric_device = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_DEVICE);
        boolean biometric_enabled = CustomSharedPreferences.getBooleanData(getBaseContext(), CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
        if (biometric_device && biometric_enabled) {
            pin = promptsViewPassword.findViewById(R.id.enter_pwd_edt_new);
            pin.setOnTouchListener((view, motionEvent) -> {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (pin.getRight() - pin.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        biometricPrompt.authenticate(Util.GetBiometricDialog());
                        promptsViewPassword.dismiss();
                    }
                }
                return false;
            });
        } else {
            pin = promptsViewPassword.findViewById(R.id.enter_pwd_edt_old);
            enter_pwd_title.setText(R.string.Verify);
        }
        pin.setVisibility(View.VISIBLE);
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
                if (value == 1)
                    proceedL1Request();
                else if (value == 2)
                    domesticL1Request();

                promptsViewPassword.dismiss();
            }
        });
        promptsViewPassword.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Prepaid card - product selected page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 10);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Prepaid card - product selected page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 10 logged");


    }

    private void totalKDvalue(String card_value_kd_str) {

//        if (card_value_kd_str.equalsIgnoreCase("Select Card Value")) {

        if (card_value_kd_str.equalsIgnoreCase(getString(R.string.select_card_value))) {
            card_value_kd_str = "0.00";
            virtual_prepaid_price_value_text.setText(" " + card_value_kd_str + " " + "KD");

        } else {
            virtual_prepaid_price_value_text.setText(" " + card_value_kd_str + " " + "KD");
            CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
            customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
            final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("PREPAIDCARDS");
            boolean guest = CustomSharedPreferences.getBooleanData(getApplicationContext(), CustomSharedPreferences.SP_KEY.GUEST_LOGIN);
            if (Double.valueOf(card_value_kd_str) > limits.getTpinLimit() && !guest) {
                //virtual_prepaid_tpin_edit.setVisibility(View.VISIBLE);
                IsBio = true;
            }
        }
    }

    private void domesticL1Request() {

        DomesticL1RequestPojo domesticL1RequestPojo = new DomesticL1RequestPojo();

        // String operatorName = mobile_operatorname_edit.getText().toString(jj);
        String operatorName = cardtype_str;
        String mobileNumber = petrol_card_number.getText().toString();
//        String operatorType = ((CoreApplication) getApplication()).getOperatorType().toUpperCase();

        domesticL1RequestPojo.setOperatorName(operatorName);
        domesticL1RequestPojo.setMobileNumber(mobileNumber);
        domesticL1RequestPojo.setAmount(petrol_card_amount_to_pay.getText().toString());
//        domesticL1RequestPojo.setOperatorType(operatorType);
        domesticL1RequestPojo.setBillPaymentType("TOPUP");
        if (biometricVerified) {
            String pin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            domesticL1RequestPojo.setTpin(pin);
        } else
            domesticL1RequestPojo.setTpin(tpin);
        domesticL1RequestPojo.setTpin(virtual_prepaid_tpin_edit.getText().toString().trim());


        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new DenominationsL1Processing(domesticL1RequestPojo, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    private void proceedL1Request() {
        VoucherLRequest voucherLRequest = new VoucherLRequest();
        voucherLRequest.setOperatorName(operator_2_res_name);
        voucherLRequest.setOperatorType(operator_2_res_type);
        voucherLRequest.setStore(store_name_2_res);
        voucherLRequest.setDenominations(card_value_str);
        voucherLRequest.setDenominationsInKD(card_value_KD_str);
        voucherLRequest.setQuantity(Integer.parseInt(quantity_str));
        voucherLRequest.setTotalAmountInKD(Double.parseDouble(card_value_KD_str));
        if (biometricVerified) {
            String pin = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.PIN);
            voucherLRequest.setTpin(pin);
        } else
            voucherLRequest.setTpin(tpin);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new VocherL1Processing(voucherLRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    private void storeRequest(String card_name) {
        DenominationRequest denominationRequest = new DenominationRequest();
        denominationRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        denominationRequest.setCardName(card_name);
        denominationRequest.setG_transType(TransType.VOCHER_STORE_REQUEST.name());
        String jsondata = new Gson().toJson(denominationRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.VOCHER_STORE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));

        android.os.Handler handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    if (!network_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_STORE_RESPONSE.name()) && response.getG_status() == 1) {
                            prepaidCardsListResponse2 = new Gson().fromJson((String) msg.obj, PrepaidCardsListResponse.class);
                            denomlist.clear();
                            denomKDlist.clear();
                            storelist.clear();
//                            denomlist.add("Select Card Value");
//                            denomKDlist.add("Select Card Value");
//                            storelist.add("Select Store Type");

                            denomlist.add(getString(R.string.select_card_value));
                            denomKDlist.add(getString(R.string.select_card_value));
                            storelist.add(getString(R.string.select_store_type));

                            for (int i = 0; i < prepaidCardsListResponse2.getStores().size(); i++) {
                                if (prepaidCardsListResponse2.getStores().size() == 1) {
                                    virtual_prepaid_store_edit.setVisibility(View.VISIBLE);
                                    virtual_prepaid_store_spinner.setVisibility(View.GONE);
                                    virtual_prepaid_store_edit.setText(prepaidCardsListResponse2.getStores().get(i).toString());
                                    denomlist.clear();
                                    denomKDlist.clear();

//                                    denomlist.add("Select Card Value");
//                                    denomKDlist.add("Select Card Value");


                                    denomlist.add(getString(R.string.select_card_value));
                                    denomKDlist.add(getString(R.string.select_card_value));


                                    operator_2_res_name = prepaidCardsListResponse2.getOperatorName();
                                    operator_2_res_type = prepaidCardsListResponse2.getOperatorType();
                                    store_name_2_res = prepaidCardsListResponse2.getStore();

                                    if (prepaidCardsListResponse2.getStores().get(i).toString().equalsIgnoreCase("uk")) {
                                        denomlist.clear();
                                        denomKDlist.clear();

//                                        denomlist.add("Select Card Value");
//                                        denomKDlist.add("Select Card Value");

                                        denomlist.add(getString(R.string.select_card_value));
                                        denomKDlist.add(getString(R.string.select_card_value));


                                        sBuffer = new StringBuffer();
                                        for (int j = 0; j < prepaidCardsListResponse2.getDenominations().size(); j++) {
                                            String dd = prepaidCardsListResponse2.getDenominations().get(j).toString();
                                            sBuffer.append(dd);
                                            sBuffer.append(",");
                                        }
                                        String pound = sBuffer.toString();
                                        String[] amount_pount = pound.split(",");
                                        for (int k = 0; k < amount_pount.length; k++) {
                                            String aa = amount_pount[k];
                                            String poundsymbol = "\u00a3";
                                            String full_str = poundsymbol + aa.substring(1);
                                            denomlist.add(full_str);
                                        }
                                        for (int l = 0; l < prepaidCardsListResponse2.getDenominationsInKD().size(); l++) {
                                            denomKDlist.add(prepaidCardsListResponse2.getDenominationsInKD().get(l).toString());
                                        }

                                    } else {
                                        denomlist.clear();
                                        denomKDlist.clear();

//                                        denomlist.add("Select Card Value");
//                                        denomKDlist.add("Select Card Value");

                                        denomlist.add(getString(R.string.select_card_value));
                                        denomKDlist.add(getString(R.string.select_card_value));

                                        for (int m = 0; m < prepaidCardsListResponse2.getDenominations().size(); m++) {
                                            denomlist.add(prepaidCardsListResponse2.getDenominations().get(m).toString());
                                        }
                                        for (int n = 0; n < prepaidCardsListResponse2.getDenominationsInKD().size(); n++) {
                                            denomKDlist.add(prepaidCardsListResponse2.getDenominationsInKD().get(n).toString());
                                        }
                                    }

                                } else {
                                    virtual_prepaid_store_edit.setVisibility(View.GONE);
                                    virtual_prepaid_store_spinner.setVisibility(View.VISIBLE);
                                    storelist.add(prepaidCardsListResponse2.getStores().get(i).toString());
                                }
                            }
                            /*storeBuffer = new StringBuffer();
                            for (int i = 0; i < prepaidCardsListResponse2.getStores().size(); i++) {
                                String ss = prepaidCardsListResponse2.getStores().get(i).toString();
                                storeBuffer.append(ss);
                                storeBuffer.append(",");
                            }
                            String stores_all = storeBuffer.toString();
                            String[] store_single = stores_all.split(",");
                            for (int i = 0; i < store_single.length; i++) {
                                String stores = store_single[i];
                                storelist.add(stores);
                            }*/
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(VirtualPrepaidMainActivityNew.this, LoginActivity.class);
                            startActivity(intent);
                            VirtualPrepaidMainActivityNew.this.finish();
                            return;
                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_STORE_RESPONSE.name()) && response.getG_status() != 1) {
                            Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                            /*denomlist.clear();
                            denomKDlist.clear();
                            denomlist.add("Select Card Value");
                            denomKDlist.add("Select Card Value");
                            virtual_prepaid_cardvalue_spinner.setSelection(0);
                            virtual_prepaid_store_spinner.setSelection(0);*/
                            clearAllFields();

                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            clearAllFields();

                            return;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
                        clearAllFields();

                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    clearAllFields();

                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    clearAllFields();
                    return;
                }

            }
        };
        new Thread(new ServerConnection(0, handler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");
    }

    private void denominationsRequest(String cardname, String storeName) {
        DenominationRequest denominationRequest = new DenominationRequest();
        denominationRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        denominationRequest.setCardName(cardname);
        denominationRequest.setStore(storeName);
        denominationRequest.setG_transType(TransType.VOCHER_DENOMINATION_REQUEST.name());
        String jsondata = new Gson().toJson(denominationRequest);


        Log.e("Denomination JSON", "" + jsondata);


        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.VOCHER_DENOMINATION_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(jsondata));


        Log.e("Denomination Request", "" + buffer.toString());

        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    if (!network_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                        if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_DENOMINATION_RESPONSE.name()) && response.getG_status() == 1) {
                            prepaidCardsListResponse2 = new Gson().fromJson((String) msg.obj, PrepaidCardsListResponse.class);

                            denomlist.clear();
                            denomKDlist.clear();
//                            denomlist.add("Select Card Value");
//                            denomKDlist.add("Select Card Value");

                            denomlist.add(getString(R.string.select_card_value));
                            denomKDlist.add(getString(R.string.select_card_value));

                            operator_2_res_name = prepaidCardsListResponse2.getOperatorName();
                            operator_2_res_type = prepaidCardsListResponse2.getOperatorType();
                            store_name_2_res = prepaidCardsListResponse2.getStore();

                            if (store_name_2_res.equalsIgnoreCase("uk")) {
                                denomlist.clear();
                                denomKDlist.clear();
//                                denomlist.add("Select Card Value");
//                                denomKDlist.add("Select Card Value");

                                denomlist.add(getString(R.string.select_card_value));
                                denomKDlist.add(getString(R.string.select_card_value));

                                sBuffer = new StringBuffer();
                                for (int i = 0; i < prepaidCardsListResponse2.getDenominations().size(); i++) {
                                    String dd = prepaidCardsListResponse2.getDenominations().get(i).toString();
                                    sBuffer.append(dd);
                                    sBuffer.append(",");
                                }
                                String pound = sBuffer.toString();
                                String[] amount_pount = pound.split(",");
                                for (int i = 0; i < amount_pount.length; i++) {

                                    String aa = amount_pount[i];
                                    String decoded = null;
                                    try {
                                        decoded = new String(aa.getBytes("ISO-8859-1"));
//                                           decoded = new String(aa.getBytes(), "UTF-8");
                                    } catch (Exception e) {

                                        Log.e("Pound decodingEx", "" + e.getMessage());

                                    }
                                    String poundsymbol = "\u00a3";
//                                    String full_str = poundsymbol + aa.substring(1);
                                    String full_str = poundsymbol + decoded.replace("?", "");
                                    denomlist.add(full_str);
                                }
                                for (int i = 0; i < prepaidCardsListResponse2.getDenominationsInKD().size(); i++) {
                                    denomKDlist.add(prepaidCardsListResponse2.getDenominationsInKD().get(i).toString());
                                }
                            } else {
                                denomlist.clear();
                                denomKDlist.clear();
//                                denomlist.add("Select Card Value");
//                                denomKDlist.add("Select Card Value");

                                denomlist.add(getString(R.string.select_card_value));
                                denomKDlist.add(getString(R.string.select_card_value));

                                for (int i = 0; i < prepaidCardsListResponse2.getDenominations().size(); i++) {
                                    denomlist.add(prepaidCardsListResponse2.getDenominations().get(i).toString());
                                }
                                for (int i = 0; i < prepaidCardsListResponse2.getDenominationsInKD().size(); i++) {
                                    denomKDlist.add(prepaidCardsListResponse2.getDenominationsInKD().get(i).toString());
                                }
                            }
                            return;
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(VirtualPrepaidMainActivityNew.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(VirtualPrepaidMainActivityNew.this, LoginActivity.class);
                            startActivity(intent);
                            VirtualPrepaidMainActivityNew.this.finish();
                            return;
                        } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_DENOMINATION_RESPONSE.name()) && response.getG_status() != 1) {
                            Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                            denomlist.clear();
                            denomKDlist.clear();
//                            denomlist.add("Select Card Value");
//                            denomKDlist.add("Select Card Value");
                            denomlist.add(getString(R.string.select_card_value));
                            denomKDlist.add(getString(R.string.select_card_value));

                            virtual_prepaid_cardvalue_spinner.setSelection(0);
                            virtual_prepaid_store_spinner.setSelection(0);
                            clearAllFields();

                            return;
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Denominations are not available for this store please change the Store and try again")) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.Denominations_are_not_available), Toast.LENGTH_SHORT).show();
                            clearAllFields();

                            return;
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            clearAllFields();

                            return;
                        }
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT).show();
                        clearAllFields();

                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                    clearAllFields();

                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    clearAllFields();

                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private class CustomAdapter extends BaseAdapter {
        Context context;
        String names[];
        String[] images;
        LayoutInflater inflter;

        public CustomAdapter(Context applicationContext, String[] names, String[] images) {
            this.context = applicationContext;
            this.names = names;
            this.images = images;
            inflter = (LayoutInflater.from(applicationContext));
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
        }

        @Override
        public int getCount() {
            return names.length;
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

            view = inflter.inflate(R.layout.virtual_card_new_row, null);
            ImageView icon = (ImageView) view.findViewById(R.id.vp_logo_image);
            TextView namess = (TextView) view.findViewById(R.id.vp_name_text);
            TextView vp_name_dummy_text = (TextView) view.findViewById(R.id.vp_name_dummy_text);
            //namess.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER);
            /*namess.setGravity(Gravity.RIGHT|Gravity.CENTER);
            namess.setPadding(0, 0, 90, 0);*/

            icon.setImageURI(Uri.parse(images[position]));
            if (position == 0) {
                namess.setText(names[position]);
                icon.setVisibility(View.GONE);
                vp_name_dummy_text.setVisibility(View.GONE);

            } else {
                namess.setText(names[position]);
                ImageLoader.getInstance()
                        .displayImage(images[position], icon, options, new SimpleImageLoadingListener() {
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
            return view;
        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = inflter.inflate(R.layout.virtual_card_new_row, null);
            ImageView icon = (ImageView) view.findViewById(R.id.vp_logo_image);
            TextView namess = (TextView) view.findViewById(R.id.vp_name_text);
            TextView vp_name_dummy_text = (TextView) view.findViewById(R.id.vp_name_dummy_text);
            vp_name_dummy_text.setVisibility(View.GONE);
            View view1 = super.getDropDownView(position, convertView, parent);
            namess.setGravity(Gravity.LEFT | Gravity.CENTER);
            namess.setPadding(20, 0, 0, 0);

            icon.setImageURI(Uri.parse(images[position]));
            if (position == 0) {
                namess.setText(names[position]);
                icon.setVisibility(View.GONE);
            } else {
                namess.setText(names[position]);
                ImageLoader.getInstance()
                        .displayImage(images[position], icon, options, new SimpleImageLoadingListener() {
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
            return view;

        }

    }

    private class DenomAmount extends BaseAdapter {
        List<String> denomlist;
        Context context;
        LayoutInflater inflter;

        public DenomAmount(Context applicationContext, List<String> denomlist) {
            this.context = applicationContext;
            this.denomlist = denomlist;
            inflter = (LayoutInflater.from(applicationContext));

        }

        @Override
        public int getCount() {
            return denomlist.size();
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
            namess.setText(denomlist.get(position));
            return view;
        }

    }

    private class StoreType extends BaseAdapter {

        List<String> storelist;
        Context context;
        LayoutInflater inflter;

        public StoreType(Context applicationContext, List<String> storelist) {
            this.context = applicationContext;
            this.storelist = storelist;
            inflter = (LayoutInflater.from(applicationContext));

        }

        @Override
        public int getCount() {
            return storelist.size();
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

//            Toast.makeText(context,storelist.get(position),Toast.LENGTH_LONG).show();

            namess.setText(storelist.get(position));
            return view;
        }
    }
}

