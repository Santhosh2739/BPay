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

import coreframework.processing.DeviceIdChangeProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;

import com.bookeey.wallet.live.OoredooValidation;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.ForgotPassword;
import com.bookeey.wallet.live.login.ForgotPasswordDeviceChange;

import ycash.wallet.json.pojo.changes.UpdateCustDeviceIdRequest;

public class        DevChangeDeviceNumber extends GenericActivity implements YPCHeadlessCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devicechange_device_number);
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
        mTitleTextView.setText("CHANGE DEVICE");*/

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
        mTitleTextView.setText(getString(R.string.devchange_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), OoredooValidation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        final EditText devchange_old_device_number_et = (EditText) findViewById(R.id.devchange_old_device_number_et);
        final EditText devchange_old_pin_et = (EditText) findViewById(R.id.devchange_old_pin_et);
        final EditText devchange_civil_id_et = (EditText) findViewById(R.id.devchange_civil_id_et);
        final Button devchange_submit_btn = (Button) findViewById(R.id.devchange_submit_btn);
        findViewById(R.id.devchange_old_device_number_et).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    devchange_old_device_number_et.setBackgroundResource(R.drawable.rounded_edittext2);
                } else
                    devchange_old_device_number_et.setBackgroundResource(R.drawable.rounded_edittext);
            }
        });
        devchange_old_pin_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    devchange_old_pin_et.setBackgroundResource(R.drawable.rounded_edittext2);
                } else
                    devchange_old_pin_et.setBackgroundResource(R.drawable.rounded_edittext);
            }
        });
        devchange_civil_id_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    devchange_civil_id_et.setBackgroundResource(R.drawable.rounded_edittext2);
                } else
                    devchange_civil_id_et.setBackgroundResource(R.drawable.rounded_edittext);
            }
        });
        devchange_old_device_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = devchange_old_device_number_et.getText().toString().length();
                if (value == 8) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(devchange_old_device_number_et.getWindowToken(), 0);
                }
            }
        });
        /*devchange_old_pin_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = devchange_old_pin_et.getText().toString().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(devchange_old_pin_et.getWindowToken(), 0);
                }
            }
        });*/
        devchange_civil_id_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = devchange_civil_id_et.getText().toString().length();
                if (value == 12) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(devchange_civil_id_et.getWindowToken(), 0);
                }
            }
        });
        devchange_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile_number = devchange_old_device_number_et.getText().toString().trim();
                if (mobile_number.length() == 0) {
                    Toast toast = Toast.makeText(DevChangeDeviceNumber.this,getString(R.string.devchange_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (mobile_number.length() != 8) {
                    Toast toast = Toast.makeText(DevChangeDeviceNumber.this,getString(R.string.devchange_valid_mobile_number), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                String civil_id = devchange_civil_id_et.getText().toString().trim();
                if (civil_id.length() == 0) {
                    Toast toast = Toast.makeText(DevChangeDeviceNumber.this,getString(R.string.devchange_civilID), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (civil_id.length() != 12) {
                    Toast toast = Toast.makeText(DevChangeDeviceNumber.this,getString(R.string.devchange_valid_civilID), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                String pin = devchange_old_pin_et.getText().toString().trim();
                if (pin.length() == 0) {
                    Toast toast = Toast.makeText(DevChangeDeviceNumber.this,getString(R.string.devchange_enter_password), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
           /* if(pin.length()!=7){
                Toast toast= Toast.makeText(getBaseContext(),"Enter valid wallet pin",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                return;
            }*/
                UpdateCustDeviceIdRequest deviceidchangeRequest = new UpdateCustDeviceIdRequest();
                deviceidchangeRequest.setWalletPin(pin);
                deviceidchangeRequest.setCivilid(civil_id);
                deviceidchangeRequest.setMobilenumber(mobile_number);
                String deviceId = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                deviceidchangeRequest.setDeviceid(deviceId);
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIdChangeProcessing(deviceidchangeRequest, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });


        TextView forgotpassword_text = (TextView) findViewById(R.id.forgotpassword_text);
        forgotpassword_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( DevChangeDeviceNumber.this, ForgotPasswordDeviceChange.class);
                startActivity(intent);
                DevChangeDeviceNumber.this.finish();

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