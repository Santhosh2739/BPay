package com.bookeey.wallet.live.changes;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.MobileNumberChangeProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;

import com.bookeey.wallet.live.OoredooValidation;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.ForgotPassword;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;

import coreframework.utils.LocaleHelper;
import ycash.wallet.json.pojo.changes.UpdateCustMobilenumberRequest;

/**
 * Created by T31049 on 1/7/2016.
 */
public class MobChangeMobileNumber extends GenericActivity implements YPCHeadlessCallback {
    EditText mobchange_old_mobile_number_et, mobchange_new_mobile_number_et, mobchange_old_pin_et,
            mobchange_civil_id_et;
    Button mobchange_submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobchange_mobile_number);
        showMenu(false);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("CHANGE MOBILE NUMBER");*/

        //Setting language
       String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(MobChangeMobileNumber.this, selectedLanguage);
        }

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
        mTitleTextView.setText(getString(R.string.mobchange_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getBaseContext(), OoredooValidation.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);

                finish();
            }
        });

        mobchange_old_mobile_number_et = (EditText) findViewById(R.id.mobchange_old_mobile_number_et);
        mobchange_new_mobile_number_et = (EditText) findViewById(R.id.mobchange_new_mobile_number_et);
        mobchange_old_pin_et = (EditText) findViewById(R.id.mobchange_old_pin_et);
        mobchange_civil_id_et = (EditText) findViewById(R.id.mobchange_civil_id_et);
        mobchange_submit_btn = (Button) findViewById(R.id.mobchange_submit_btn);
        mobchange_old_mobile_number_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mobchange_old_mobile_number_et.hasFocus()) {
                    mobchange_old_mobile_number_et.setBackgroundResource(R.drawable.rounded_edittext);
                } else if (!mobchange_old_mobile_number_et.hasFocus()) {
                    mobchange_old_mobile_number_et.setBackgroundResource(R.drawable.rounded_edittext2);
                }
            }
        });
        mobchange_new_mobile_number_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mobchange_new_mobile_number_et.hasFocus()) {
                    mobchange_new_mobile_number_et.setBackgroundResource(R.drawable.rounded_edittext);
                } else if (!mobchange_new_mobile_number_et.hasFocus()) {
                    mobchange_new_mobile_number_et.setBackgroundResource(R.drawable.rounded_edittext2);
                }
            }
        });
        mobchange_old_pin_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mobchange_old_pin_et.hasFocus()) {
                    mobchange_old_pin_et.setBackgroundResource(R.drawable.rounded_edittext);
                } else if (!mobchange_old_pin_et.hasFocus()) {
                    mobchange_old_pin_et.setBackgroundResource(R.drawable.rounded_edittext2);
                }
            }
        });
        mobchange_civil_id_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mobchange_civil_id_et.hasFocus()) {
                    mobchange_civil_id_et.setBackgroundResource(R.drawable.rounded_edittext);
                } else if (!mobchange_civil_id_et.hasFocus()) {
                    mobchange_civil_id_et.setBackgroundResource(R.drawable.rounded_edittext2);
                }
            }
        });
        mobchange_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldmobilenumber = mobchange_old_mobile_number_et.getText().toString().trim();

                String newmobilenumber = mobchange_new_mobile_number_et.getText().toString().trim();

                if (oldmobilenumber.length() == 0) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_old_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (newmobilenumber.length() == 0) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_valid_old_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                String oldpin = mobchange_old_pin_et.getText().toString().trim();
                if (oldpin.length() == 0) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_enter_password), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
              /*  if (oldpin.length() != 7) {
                    Toast toast = Toast.makeText(getBaseContext(), "Enter vaild wallet pin", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                String civilid = mobchange_civil_id_et.getText().toString().trim();
                if (civilid.length() == 0) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_civilID), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (civilid.length() != 12) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_valid_civilID), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                String new_mobile_number = mobchange_new_mobile_number_et.getText().toString().trim();
                if (new_mobile_number.length() == 0) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_new_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (new_mobile_number.length() != 8) {
                    Toast toast = Toast.makeText(MobChangeMobileNumber.this, getString(R.string.mobchange_valid_new_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                UpdateCustMobilenumberRequest mobilenumberchangeRequest = new UpdateCustMobilenumberRequest();
                mobilenumberchangeRequest.setCivilid(civilid);
                mobilenumberchangeRequest.setNewmobilenumber(new_mobile_number);
                mobilenumberchangeRequest.setWalletPin(oldpin);
                String deviceId = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                mobilenumberchangeRequest.setDeviceId(deviceId);
                mobilenumberchangeRequest.setOldmobilenumber(oldmobilenumber);
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new MobileNumberChangeProcessing(mobilenumberchangeRequest, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });
        mobchange_civil_id_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = mobchange_civil_id_et.getText().toString().length();
                if (value == 12) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobchange_civil_id_et.getWindowToken(), 0);
                }
            }
        });
        mobchange_new_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = mobchange_new_mobile_number_et.getText().toString().length();
                if (value == 8) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobchange_new_mobile_number_et.getWindowToken(), 0);
                }
            }
        });
        /*mobchange_old_pin_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = mobchange_old_pin_et.getText().toString().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobchange_old_pin_et.getWindowToken(), 0);
                }
            }
        });*/
        mobchange_old_mobile_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = mobchange_old_mobile_number_et.getText().toString().length();
                if (value == 8) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mobchange_old_mobile_number_et.getWindowToken(), 0);
                }
            }
        });


        TextView forgotpassword_text = (TextView) findViewById(R.id.forgotpassword_text);
        forgotpassword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( MobChangeMobileNumber.this, ForgotPassword.class);
                startActivity(intent);
                MobChangeMobileNumber.this.finish();

            }
        });
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }
}