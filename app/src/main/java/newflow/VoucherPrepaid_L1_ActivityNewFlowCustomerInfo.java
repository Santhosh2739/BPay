package newflow;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
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
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import newflow_processing.VocherL1ProcessingNewFlow;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.virtualprepaidcards.VoucherLRequest;

public class VoucherPrepaid_L1_ActivityNewFlowCustomerInfo extends GenericActivity implements YPCHeadlessCallback {

    EditText mobile_operatorname_edit, mobile_mobilenumber_edit, mobile_billapy_amount_edit, mobile_tpin_edit, mobile_name_edit, mobile_email_edit, mobile_sender_mobilenumber_edit;
    //    Spinner mobile_topup_amount_spinner;
    LinearLayout mobile_topup_amount_layout, mobile_billpay_amount_layout, mobile_tpin_layout;
    //    List<String> categories;
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
    //    CustomerLoginRequestReponse response = null;
//    TransactionLimitResponse limits = null;
//    ArrayAdapter<String> aa = null;
    String amount_without_kd = null;

//    String response_str = null;

    //NewFlow
    public static final String KEY_VOUCHER_PREPAID_POJO = "KEY_VOUCHER_PREPAID_POJO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voucher_prepaid_leg1_newflow_cust_info);
        mobile_operatorname_edit = (EditText) findViewById(R.id.mobile_operatorname_edit);
        mobile_mobilenumber_edit = (EditText) findViewById(R.id.mobile_mobilenumber_edit);


        //NewFlow
        mobile_name_edit = (EditText) findViewById(R.id.mobile_name_edit);
        mobile_email_edit = (EditText) findViewById(R.id.mobile_email_edit);
        mobile_sender_mobilenumber_edit = (EditText) findViewById(R.id.mobile_sender_mobilenumber_edit);

//        mobile_name_edit.setText("Rahman");
//        mobile_email_edit.setText("tech2@bookeey.com");
//        mobile_sender_mobilenumber_edit.setText("60064534");


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
//        mobile_topup_amount_spinner = (Spinner) findViewById(R.id.mobile_topup_amount_spinner);

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
        mTitleTextView.setText(getResources().getString(R.string.virtual_prepaid_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
//        limits = response.getFilteredLimits().get("INTL_RECHARGE");

//        response_str = getIntent().getStringExtra("DENOMINATIONS_RESPONSE");

//        mobile_billapy_amount_edit.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});

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

//                        if (tpin_double > limits.getTpinLimit()) {
//                            mobile_tpin_layout.setVisibility(View.VISIBLE);
//                           /* Toast toast = Toast.makeText(getBaseContext(), "Please enter TPIN", Toast.LENGTH_SHORT);
//                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                            toast.show();*/
//                        } else {
//                            mobile_tpin_layout.setVisibility(View.GONE);
//                            mobile_tpin_edit.setText("");
//
//                        }
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
//        categories = new ArrayList<>();
//        categories.add("--Select Amount--");

//        DenominationResponse denominationResponse = new Gson().fromJson(response_str, DenominationResponse.class);
//        for (int i = 0; i < denominationResponse.getDenom().size(); i++) {
//            categories.add("" + denominationResponse.getDenom().get(i)+" "+"KD");
//        }

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
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.no_image)
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

//        aa = new ArrayAdapter<String>(this, R.layout.spinner_item_center, categories) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//                if (position == getCount()) {
//
//                    //((TextView) v.findViewById(android.R.id.text1)).setText("");
//                    //((TextView) v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
//                    //((TextView) v).setTextColor(getResources().getColorStateList(R.color.black));//spinner value text color
//                    // ((TextView) v.findViewById(android.R.id.text1)).setGravity(Gravity.CENTER);
//                    ((TextView) v).setGravity(Gravity.CENTER);
//
//                }
//                return v;
//            }
//
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View v = super.getDropDownView(position, convertView,
//                        parent);
//                ((TextView) v).setTextColor(getResources().getColorStateList(
//                        R.color.blue));
//                ((TextView) v).setGravity(Gravity.CENTER);
//
//                return v;
//            }
//
//            @Override
//            public int getCount() {
//                return super.getCount(); // you dont display last item. It is used as hint.
//            }
//
//        };
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mobile_topup_amount_spinner.setAdapter(aa);

//        mobile_topup_amount_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // amount_tpoup_str = "";
//                amount_billpay_str = mobile_topup_amount_spinner.getSelectedItem().toString();
//                if (amount_billpay_str != null) {
//                    String[] amount = amount_billpay_str.split(" ");
//                    amount_without_kd = amount[0];
//
//                }
//
//                try {
//                    if (amount_without_kd != "") {
//                        tpin_double = Double.parseDouble(amount_without_kd);
//
////                        if (tpin_double > limits.getTpinLimit()) {
////                            mobile_tpin_layout.setVisibility(View.VISIBLE);
////                        } else {
////                            mobile_tpin_layout.setVisibility(View.GONE);
////                            mobile_tpin_edit.setText("");
////                        }
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                ((TextView) view).setTextColor(getResources().getColor(R.color.blue));
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        mobile_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//
//                    case R.id.mobile_totup_radio_btn:
//                        mobile_totup_radio_btn.setChecked(true);
//                        mobile_billpay_radio_btn.setChecked(false);
//                        mobile_billpayment_type_str = ((RadioButton) findViewById(R.id.mobile_totup_radio_btn)).getText().toString();
//                        mobile_billpayment_type_str = "topup";
//                        mobile_topup_amount_layout.setVisibility(View.VISIBLE);
//                        mobile_billpay_amount_layout.setVisibility(View.GONE);
//                        mobile_mobilenumber_edit.setText("");
//                        mobile_topup_amount_spinner.setAdapter(aa);
//                        mobile_tpin_layout.setVisibility(View.GONE);
//                        mobile_tpin_edit.setText("");
//
//                        break;
//
//                    case R.id.mobile_billpay_radio_btn:
//                        mobile_totup_radio_btn.setChecked(false);
//                        mobile_billpay_radio_btn.setChecked(true);
//                        mobile_topup_amount_layout.setVisibility(View.GONE);
//                        mobile_billpay_amount_layout.setVisibility(View.VISIBLE);
//                        mobile_tpin_layout.setVisibility(View.GONE);
//                        mobile_billpayment_type_str = ((RadioButton) findViewById(R.id.mobile_billpay_radio_btn)).getText().toString();
//                        mobile_billpayment_type_str = "billpay";
//                        mobile_mobilenumber_edit.setText("");
//                        mobile_billapy_amount_edit.setText("");
//                        mobile_tpin_edit.setText("");
//
//
//                        break;
//
//                    default:
//
//                        break;
//                }
//            }
//        });

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
//                if ((mobile_mobilenumber_edit.getText().toString().length() == 0)) {
//                    Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_mobile_number), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
//                    return;
//                }
//                if ((mobile_mobilenumber_edit.getText().toString().length() != 8)) {
//                    Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_valid_mobile__number), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
//                    return;
//                }



                //Newflow validations
                if (mobile_name_edit.getVisibility() == View.VISIBLE) {
                    if (mobile_name_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_newflow_name), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }

                if (mobile_sender_mobilenumber_edit.getVisibility() == View.VISIBLE) {
                    if (mobile_sender_mobilenumber_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mobile_bill_L1_toast_mobile_number), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }

                if (mobile_email_edit.getVisibility() == View.VISIBLE) {
                    if (mobile_email_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(),getResources().getString(R.string.mail_id_valid_msg), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }




                if (mobile_billpay_amount_layout.getVisibility() == View.VISIBLE) {
                    if (mobile_billapy_amount_edit.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_amount), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }
                if (mobile_topup_amount_layout.getVisibility() == View.VISIBLE) {
                    if (amount_billpay_str.equals("--Select Amount--")) {
                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_select_amount), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }

//                if (mobile_billpay_amount_layout.getVisibility() == View.VISIBLE) {
//
//                    Double _amount = Double.parseDouble(amount_without_kd);
//                    if (_amount < limits.getMinValuePerTransaction() || _amount > limits.getMaxValuePerTransaction()) {
//                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_between) + limits.getMinValuePerTransaction() + getResources().getString(R.string.mobile_bill_L1_toast_to) + limits.getMaxValuePerTransaction(), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                        toast.show();
//                        return;
//                    }
//
//                }
//                if (mobile_topup_amount_layout.getVisibility() == View.VISIBLE) {
//
//                    Double _amount = Double.parseDouble(amount_without_kd);
//                    if (_amount < limits.getMinValuePerTransaction() || _amount > limits.getMaxValuePerTransaction()) {
//                        Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.mobile_bill_L1_toast_between)+ limits.getMinValuePerTransaction() +getResources().getString(R.string.mobile_bill_L1_toast_to) + limits.getMaxValuePerTransaction(), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                        toast.show();
//                        return;
//                    }
//
//                }


                if (mobile_tpin_layout.getVisibility() == View.VISIBLE) {
                    if (mobile_tpin_edit.getText().toString().trim().length() == 0) {
                        Toast toast = Toast.makeText(VoucherPrepaid_L1_ActivityNewFlowCustomerInfo.this, getResources().getString(R.string.mobile_bill_L1_toast_password), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        return;
                    }
                }

                DomesticL1Request();
            }
        });
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

    private void DomesticL1Request() {


        //NewFlow
        String name = mobile_name_edit.getText().toString().trim();
        String emailID = mobile_email_edit.getText().toString().trim();
        String sender_mobile_no = mobile_sender_mobilenumber_edit.getText().toString().trim();



        String voucherPrepaidL1RequestPojoValue = getIntent().getStringExtra(KEY_VOUCHER_PREPAID_POJO);


        VoucherLRequest voucherLRequest = new Gson().fromJson(voucherPrepaidL1RequestPojoValue, VoucherLRequest.class);

        voucherLRequest.setSenderMobileNo(sender_mobile_no);
        voucherLRequest.setFirstName(name);
        voucherLRequest.setEmailId(emailID);

        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new VocherL1ProcessingNewFlow(voucherLRequest, application, true));
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
