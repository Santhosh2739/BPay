package com.bookeey.wallet.live.changes;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.ChangeTPinProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.registration.CustomerActivationRequest;

public class ChangeTPinActivity extends GenericActivity implements YPCHeadlessCallback {
    CheckBox changepin_show_password_checkbox;
    EditText changepin_enter_tpin_edit, changepin_t_createpwd_edit, changepin_confirm_tnew_edit;
    Button changepin_submit_btn;
    ImageView merchant_category_screen_wallet_logo_back, home_up_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_tpin_layout);
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
        View mCustomView = mInflater.inflate(R.layout.map_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);


        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText("CHANGE TPIN");

        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changepin_enter_tpin_edit = (EditText) findViewById(R.id.changepin_enter_tpin_edit);
        changepin_t_createpwd_edit = (EditText) findViewById(R.id.changepin_t_createpwd_edit);
        changepin_confirm_tnew_edit = (EditText) findViewById(R.id.changepin_confirm_tnew_edit);
        changepin_enter_tpin_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = changepin_enter_tpin_edit.getText().toString().length();
                if (value == 4) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(changepin_enter_tpin_edit.getWindowToken(), 0);
                }
            }
        });

        changepin_t_createpwd_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = changepin_t_createpwd_edit.getText().toString().length();
                if (value == 4) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(changepin_t_createpwd_edit.getWindowToken(), 0);
                }
            }
        });
        changepin_confirm_tnew_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = changepin_confirm_tnew_edit.getText().toString().length();
                if (value == 4) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(changepin_confirm_tnew_edit.getWindowToken(), 0);
                }
            }
        });
        changepin_show_password_checkbox = (CheckBox) findViewById(R.id.changepin_show_password_checkbox);
        changepin_show_password_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!changepin_show_password_checkbox.isChecked()) {
                    changepin_enter_tpin_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    changepin_t_createpwd_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    changepin_confirm_tnew_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    changepin_enter_tpin_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    changepin_t_createpwd_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    changepin_confirm_tnew_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        changepin_submit_btn = (Button) findViewById(R.id.changepin_submit_btn);

        changepin_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activation_pin = changepin_enter_tpin_edit.getText().toString().trim();
                String new_pin = changepin_t_createpwd_edit.getText().toString().trim();
                String new_pin_confirm = changepin_confirm_tnew_edit.getText().toString().trim();
                if (activation_pin.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter current TPIN", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                //TODO: Add multiple case
                if (new_pin.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter new TPIN", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                /*if (new_pin.length() != 4) {
                    Toast toast = Toast.makeText(getBaseContext(), "Please enter 4 digit Tpin", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                if (new_pin_confirm.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), "please enter confirm new TPIN", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (!new_pin.equals(new_pin_confirm)) {
                    Toast toast = Toast.makeText(getBaseContext(), "New TPIN and confirm TPIN are not same", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                } else {
                    changepin(activation_pin, new_pin_confirm);
                }
            }
        });
    }

    private void changepin(String activation_pin, String new_pin_confirm) {
        CustomerActivationRequest customerActivationRequest = new CustomerActivationRequest();
        String mobile_number = CustomSharedPreferences.getStringData(ChangeTPinActivity.this, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        customerActivationRequest.setMobileNumber(mobile_number);
        customerActivationRequest.setOldPin(activation_pin);
        customerActivationRequest.setNewPin(new_pin_confirm);
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        customerActivationRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        String uiProcessorReference = application.addUserInterfaceProcessor(new ChangeTPinProcessing(customerActivationRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
