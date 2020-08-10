package com.bookeey.wallet.live.recharge;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

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
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeFinalResponsePojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeInitiationResponsePojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeRequestPojo;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;

/**
 * Created by 30099 on 4/29/2016.
 */
public class TopupScreen2Activity extends GenericActivity implements AdapterView.OnItemSelectedListener {
    TextView topup_2_mobilenumber_edit, top_up_currency_local;
    ImageView topup_country_flag;
    private DisplayImageOptions options;
    HashMap<String, String> hm;
    String amountinKwd, amount_currency;
    Button top_up_second_proceed_btn;
    Spinner spinner;
    EditText top_up_pin_edt;
    TextView top_up_currency_kwd_text, topup_2_country_name;
    ProgressDialog progress = null;
    InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo = null;
    String denominationvalue;
    boolean itemselected = false;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_second_layout);

       /* View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("Int'l Top Up");*/

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

        top_up_pin_edt = (EditText) findViewById(R.id.top_up_pin_edt);
        ImageView imageView = (ImageView) findViewById(R.id.topup_2_image_person);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        topup_2_country_name = (TextView) findViewById(R.id.topup_2_country_name);
        topup_2_country_name.setText(((CoreApplication) getApplication()).getCountry_name());


        topup_2_mobilenumber_edit = (TextView) findViewById(R.id.topup_2_mobilenumber_edit);
        top_up_currency_kwd_text = (TextView) findViewById(R.id.top_up_currency_kwd_text);
        top_up_currency_local = (TextView) findViewById(R.id.top_up_currency_local);
        topup_country_flag = (ImageView) findViewById(R.id.topup_country_flag);
        top_up_second_proceed_btn = (Button) findViewById(R.id.top_up_second_proceed_btn);

        topup_2_mobilenumber_edit.setText(((CoreApplication) getApplication()).getCountry_code() + "-" + ((CoreApplication) getApplication()).getTopup_mobile_number());


        progress = new ProgressDialog(TopupScreen2Activity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        updateProfile(R.id.topup_2_nameTextooredo, R.id.topup_2_wallet_id, R.id.topup_2_balance_id);

        /*top_up_pin_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int value = top_up_pin_edt.getText().toString().trim().length();
                if (value == 4) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(top_up_pin_edt.getWindowToken(), 0);
                }
            }
        });*/

        top_up_second_proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (top_up_currency_local.getText().toString().trim().length() == 0 || top_up_currency_kwd_text.getText().toString().trim().length() == 0) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_select_denomination), Toast.LENGTH_SHORT).show();
                    return;
                }
                String seperated[] = top_up_currency_kwd_text.getText().toString().split(" ");
                String denomination[] = top_up_currency_local.getText().toString().split(" ");
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                final TransactionLimitResponse limitResponse = customerLoginRequestReponse.getFilteredLimits().get("INTL_RECHARGE");
                InternationalRechargeRequestPojo requestPojo = new InternationalRechargeRequestPojo();
                requestPojo.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
               /* if (customerLoginRequestReponse.getWalletBalance() < Double.parseDouble(seperated[0])) {
                    Toast.makeText(getBaseContext(), "You don't have sufficient balance", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (Double.parseDouble(seperated[0]) > limitResponse.getTpinLimit()) {
                    if (top_up_pin_edt.getText().toString().trim().equals("")) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.intl_enter_password), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    requestPojo.setTpin(top_up_pin_edt.getText().toString().trim());
                } else {
                    requestPojo.setTpin(null);
                }
                requestPojo.setDenominationAmt(Double.parseDouble(denomination[0]));
                String separated1[] = top_up_currency_kwd_text.getText().toString().split(" ");
                requestPojo.setDenominationinKWD(Double.parseDouble(separated1[0]));
                requestPojo.setCurrencyName(denomination[1]);
                requestPojo.setMobileNumber(((CoreApplication) getApplication()).getTopup_mobile_number());
                requestPojo.setCountryCode(((CoreApplication) getApplication()).getCountry_code());
                requestPojo.setCountryName(((CoreApplication) getApplication()).getCountry_name());
                requestPojo.setG_transType(TransType.INTERNATIONAL_RECHARGE_L1_REQUEST.name());
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
                                Toast toast = Toast.makeText(TopupScreen2Activity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(TopupScreen2Activity.this, LoginActivity.class);
                                startActivity(intent);
                                TopupScreen2Activity.this.finish();
                                return;
                            } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_L1_RESPONSE.name())) {
                                switch (response.getG_errorDescription()) {
                                    case "Invalid password":
                                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Invalid_Password), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Problem occurs while validating. Please try again":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Problem_occurs_while_validating_Please_try_again), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Monthly Recharge Limits Exceeding":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Monthly_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Daily Recharge Limits Exceeding":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Daily_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Recharge Amount Min & Max":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Recharge_Amount_Min_Max), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Daily & Monthly Limits Not Available":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    case "Number is InActive":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.Number_is_InActive), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
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
                                        break;
                                    case "FAILED":
                                        toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.FAILED), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                    default:
                                        toast = Toast.makeText(getBaseContext(), response.getG_errorDescription(), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        break;
                                }
                                return;
                            } else {
                                Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
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
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(), true,getApplicationContext())).start();
                showIfNotVisible("");

            }
        });


        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.new_logo_virtual_grey)
                .showImageForEmptyUri(R.drawable.new_logo_virtual_grey)
                .showImageOnFail(R.drawable.new_logo_virtual_grey)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        initImageLoader(getBaseContext());
        String flag_image = ((CoreApplication) getApplication()).getCountry_flag();

        ImageLoader.getInstance()
                .displayImage(flag_image, topup_country_flag, options, new SimpleImageLoadingListener() {
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
        InternationalRechargeFinalResponsePojo internationalRechargeFinalResponsePojo = ((CoreApplication) getApplication()).getInternationalRechargeFinalResponsePojo();
        List<HashMap<String, String>> aList = new ArrayList<>();
        aList.add(addData("Select Denomination", ""));
        for (int i = 0; i < internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().size(); i++) {
            hm = new HashMap<String, String>();
            hm.put("txt", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getRechargeAmount());
            hm.put("name", internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getAmtInKWD());
            aList.add(addData(internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getRechargeAmount(), internationalRechargeFinalResponsePojo.getInternationalRechargeDetails().get(0).getDenominationList().get(i).getAmtInKWD()));
        }

        String[] from = {"txt", "name"};

        int[] to = {R.id.txt_country_name_spinner, R.id.txt_country_code_spinner};

//        ArrayAdapter<String> adapter= new ArrayAdapter(getBaseContext(), android.R.layout.simple_spinner_item,aList);
        adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.customspinner_layout, from, to);

        spinner = (Spinner) findViewById(R.id.top_up_currency_custom_auto_complete_text);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
                amountinKwd = hm.get("txt");
                amount_currency = hm.get("name");
                top_up_currency_local.setText(amount_currency);
                top_up_currency_kwd_text.setText(amountinKwd);

            }
        };
        top_up_currency_kwd_text.setText("");

        top_up_currency_local.setText("");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
        HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);
        if (position == 0) {
            if (top_up_currency_kwd_text.getText().toString().trim().length() != 0) {
                String[] values = top_up_currency_kwd_text.getText().toString().split(" ");
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("INTL_RECHARGE");

                if (Double.parseDouble(values[0]) > limits.getTpinLimit()) {
                    top_up_pin_edt.setVisibility(View.VISIBLE);
                } else {
                    top_up_pin_edt.setVisibility(View.INVISIBLE);
                    top_up_pin_edt.setText("");
                }
            }
            return;
        }


        top_up_currency_kwd_text.setText(hm.get("txt"));
        top_up_currency_local.setText(hm.get("name"));
        denominationvalue = hm.get("txt");
        itemselected = true;
        spinner.setSelection(0, true);
        spinner.setOnItemSelectedListener(this);


    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private HashMap<String, String> addData(String local, String kwd) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("txt", kwd);
        map.put("name", local);
        return map;
    }

}
