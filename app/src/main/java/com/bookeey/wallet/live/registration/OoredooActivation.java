package com.bookeey.wallet.live.registration;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.Activation_processing.ActivationProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.registration.CustomerActivationRequest;

/**
 * Created by 30099 on 10/29/2015.
 */
public class OoredooActivation extends GenericActivity implements YPCHeadlessCallback {
    CheckBox activation_show_password_checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
if(selectedLanguage.equalsIgnoreCase("en")) {
    setContentView(R.layout.activiation);
}else{
    setContentView(R.layout.activation_ar);
}
        //View mCustomView;
        showMenu(false);

        /*mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setText(getString(R.string.activation_str));
        mTitleTextView.setPadding(-80, 0, 0, 0);*/
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.general_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.activation_title));

        //((ImageView) mCustomView.findViewById(R.id.image_logo)).setVisibility(View.GONE);
        final TextView activation_help_text = (TextView) findViewById(R.id.activation_help_text);
        final LinearLayout login_created_text = (LinearLayout) findViewById(R.id.login_created_text);
        final EditText activation_enter_pin_edit = (EditText) findViewById(R.id.activation_enter_pin_edit);
        final EditText activation_create_pwd_edit = (EditText) findViewById(R.id.activation_createpwd_edit);
        final EditText activation_confirm_new_edit = (EditText) findViewById(R.id.activation_confirm_new_edit);
        final Button registration_submit_btn = (Button) findViewById(R.id.registration_submit_btn);
        activation_show_password_checkbox = (CheckBox) findViewById(R.id.activation_show_password_checkbox);
        /*activation_enter_pin_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = activation_enter_pin_edit.getText().toString().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(activation_enter_pin_edit.getWindowToken(), 0);
                }
            }
        });
        activation_create_pwd_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = activation_create_pwd_edit.getText().toString().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(activation_create_pwd_edit.getWindowToken(), 0);
                }
            }
        });
        activation_confirm_new_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = activation_confirm_new_edit.getText().toString().length();
                if (value == 7) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(activation_confirm_new_edit.getWindowToken(), 0);
                }
            }
        });*/
        activation_show_password_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!activation_show_password_checkbox.isChecked()) {
                    activation_enter_pin_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    activation_create_pwd_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    activation_confirm_new_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    activation_enter_pin_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    activation_create_pwd_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    activation_confirm_new_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        registration_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activation_pin = activation_enter_pin_edit.getText().toString().trim();
                String new_pin = activation_create_pwd_edit.getText().toString().trim();
                String new_pin_confirm = activation_confirm_new_edit.getText().toString().trim();
                if (activation_pin.length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.activation_toast_pin_msg), Toast.LENGTH_LONG).show();
                    return;
                }
                //TODO: Add multiple case
                if (new_pin.length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.activation_toast_newpin_msg), Toast.LENGTH_LONG).show();
                    return;
                }
                /*if(new_pin.length()!=7){
                    Toast toast= Toast.makeText(getBaseContext(), getResources().getString(R.string.activation_toast_valid_newpin_msg), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                if (new_pin_confirm.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.activation_toast_confirmpin_msg), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (!new_pin.equals(new_pin_confirm)) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.activation_toast_samepin_msg), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                } else {
                    activation(activation_pin, new_pin_confirm);
                }
            }
        });
        activation_help_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.activation_toast_help_text), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void activation(String activationPin, String newPin) {
        CustomerActivationRequest customerActivationRequest = new CustomerActivationRequest();
        String mobile_number = CustomSharedPreferences.getStringData(OoredooActivation.this, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        customerActivationRequest.setMobileNumber(mobile_number);
        customerActivationRequest.setOldPin(activationPin);
        customerActivationRequest.setNewPin(newPin);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new ActivationProcessing(customerActivationRequest, application, true));
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

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }
}