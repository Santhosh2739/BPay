package newflow;

import static coreframework.database.CustomSharedPreferences.SP_KEY.MOBILE_NUMBER;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.application.SyncServiceNewFlow;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.ExpandableHeightGridView;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.mainmenu.MerchantSelectionSliderList;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import coreframework.barcodeclient.BarCodeTimeParser;
import coreframework.barcodeclient.BcodeHeaderEncoder;
import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.GetPushNotificationMessageProcessing;
import coreframework.processing.LoadMoneyProcessing;
import coreframework.processing.PayToMerchantValidationProcessing;
import coreframework.processing.invoice_Processing.InvoiceProcessing;
import coreframework.processing.invoice_Processing.InvoiceProcessingForSessionOut;
import coreframework.securityutils.SecurityUtils;
import coreframework.taskframework.DecimalDigitsInputFilter;
import coreframework.taskframework.GenericNewFlowActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.Hex;
import coreframework.utils.LocaleHelper;
import coreframework.utils.PriceFormatter;
import coreframework.utils.SMSUtils;
import coreframework.utils.URLUTF8Encoder;
import newflow_processing.DeviceIDSplashCheckProcessingNewFlow;
import newflow_processing.MobileBillProcessingNewFlow;
import newflow_processing.PrepaidCardProcessingNewFlow;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.getpushnotificationmessage.GetPushNotificationMessageRequest;
import ycash.wallet.json.pojo.loadmoney.PaymentForm;
import ycash.wallet.json.pojo.loadmoney.WalletLimits;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.MobileBillOperatorsRequest;
import ycash.wallet.json.pojo.paytomerchant.P2MBarcodeGenInitValidationRequest;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;
import ycash.wallet.json.pojo.userinfo.UserInfoResponse;

public class MainActivityNewFlow extends GenericNewFlowActivity implements YPCHeadlessCallback, TextToSpeech.OnInitListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //int i = 0;
    static public final int _SLIDE_ECOM = 9;
    public static final int LOCATION_REQUEST = 101;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    TextToSpeech tts;
    Intent intent;
    Bitmap load_wallet, invoice, pay, recharge_paybill, sendmoney, help, where_to_pay, my_offers, mobilebill, prepaid_cards, txn_history, store, testimage;
    ExpandableHeightGridView mainmenu_gridview;
    ScrollView scroll;
    MyAdapter adapter1;
    ImageView imageview;
    WalletLimits walletLimits;
    String amount, tpin;
    ImageView image_person;
    ProgressDialog progressDialog;
    CoreApplication application = null;
    CustomerLoginRequestReponse customerLoginRequestReponse = null;
    boolean isSound = false;
    int i = 0;
    int invoice_count = 0;
    Animation animBlink, animRight, animLeft;
    Button bookeey_mainmenu_loadwallet_btn, bookeey_mainmenu_pay_btn;
    String bannerString = "";
    List<String> bannerList = new ArrayList<>();
    int right_count = 0;
    int left_count = 0;
    Handler handler2 = null;
    boolean isScrolled = true;
    LinearLayout language_layout;
    ImageView coutry_flag_img;
    TextView language_text;
    String selectedLanguage = null;
    String moduleName = "";
    String versionname;
    private UserInfoUpdateHandler userInfoUpdateHandler;
    private String amount_str = null;
    private DrawerLayout mDrawer = null;
    private GoogleApiClient googleApiClient;
    private AppUpdateManager mAppUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (state.installStatus() == InstallStatus.INSTALLED) {
                if (mAppUpdateManager != null) {
                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                }
            } else {
                Log.i("AppUpdate", "InstallStateUpdatedListener: state: " + state.installStatus());
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getApplicationContext(), this);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
        animRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_right);
        setContentView(R.layout.activity_main_newflow);
        moduleName = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MODULE);

        TextView tv_login_newflow = (TextView) findViewById(R.id.tv_login_newflow);
        TextView tv_signup_newflow = (TextView) findViewById(R.id.tv_signup_newflow);
        tv_login_newflow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, false, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });

        tv_signup_newflow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, true, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });

        FrameLayout login_frame_layout = (FrameLayout) findViewById(R.id.login_frame_layout);

        login_frame_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, false, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });

        FrameLayout signup_frame_layout = (FrameLayout) findViewById(R.id.signup_frame_layout);
        signup_frame_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, true, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");
            }
        });

        application = (CoreApplication) getApplication();
        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        if (!(application.getBannerDetails() == null)) {
            if (!application.getBannerDetails().isEmpty() && !application.getBannerDetails().contains("")) {
                bannerList = application.getBannerDetails();
                for (String s : application.getBannerDetails()) {
                    bannerString += s + "\t";
                }
            } else {
                bannerString = "No offers available";
            }
        }

        ActionBar mActionBar = getActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayUseLogoEnabled(false);
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
        }
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_guest, null);
        //Showing Notification count
        TextView count_text_top = (TextView) mCustomView.findViewById(R.id.count_text_top);
        FrameLayout push_notifications_frame_layout = (FrameLayout) mCustomView.findViewById(R.id.push_notifications_frame_layout);
        String notification_count = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.NOTIFICATION_MSG_COUNT);

        if (notification_count.length() > 0) {
            count_text_top.setText("" + notification_count);
        } else {
            count_text_top.setText("0");
        }

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        mActionBar.setCustomView(mCustomView, params);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.marquee_text);
        final ImageView leftArrow = (ImageView) mCustomView.findViewById(R.id.left);
        final ImageView rightArrow = (ImageView) mCustomView.findViewById(R.id.right);

        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setSingleLine(true);
        //NewFlow
        mTitleTextView.setText("" + getString(R.string.please_register_to_gain_all_feature));

        if (right_count < bannerList.size() || left_count < bannerList.size()) {
            if (!bannerString.equalsIgnoreCase("No offers available")) {
                changeText(mTitleTextView);
            } else {
                rightArrow.setEnabled(false);
                leftArrow.setEnabled(false);
            }
        }

        handler2 = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                int sleepTime = 5000;    //in milissegunds
                if (right_count > bannerList.size()) {
                    right_count = 0;
                }
                if (left_count > bannerList.size()) {
                    left_count = 0;
                }
                handler2.postDelayed(this, sleepTime);
            }
        };
        handler2.postDelayed(r, 5000);

        Handler handler01 = new Handler();
        Runnable r1 = new Runnable() {
            public void run() {
                int sleepTime = 15000;    //in milissegunds
                isScrolled = true;
                if (isScrolled) {
                    changeText(mTitleTextView);
                }
                handler2.postDelayed(this, sleepTime);
            }
        };
        handler01.postDelayed(r1, 15000);

        rightArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isScrolled = false;
                String test = null;
                if (right_count < bannerList.size()) {

                    if (right_count > bannerList.size()) {
                        bannerString = bannerList.get(right_count - 1);//get the element by passing the index of the element
                        animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
                        animLeft.setDuration(900);
                        mTitleTextView.startAnimation(animLeft);
                        mTitleTextView.setText(bannerString);

                    } else {
                        bannerString = bannerList.get(right_count);//get the element by passing the index of the element
                        animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
                        animLeft.setDuration(900);
                        mTitleTextView.startAnimation(animLeft);
                        mTitleTextView.setText(bannerString);
                    }
                } else {
                }
                right_count++;

            }
        });
        leftArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isScrolled = false;

                if (left_count < bannerList.size()) {

                    if (left_count > bannerList.size()) {
                        bannerString = bannerList.get(left_count - 1);//get the element by passing the index of the element
                        animRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_right);
                        animRight.setDuration(900);
                        mTitleTextView.startAnimation(animRight);
                        mTitleTextView.setText(bannerString);

                    } else {
                        bannerString = bannerList.get(left_count);//get the element by passing the index of the element
                        mTitleTextView.setText(bannerString);
                        animRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_right);
                        animRight.setDuration(900);
                        mTitleTextView.startAnimation(animRight);
                    }
                } else {
                }
                left_count++;
            }
        });

        //Set the property from S_PREF
        //requestHeaderInformationUpdate();

        progressDialog = new ProgressDialog(MainActivityNewFlow.this, R.style.MyTheme2);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        //Wallet service
        load_wallet = BitmapFactory.decodeResource(this.getResources(), R.drawable.load_wallet_grey);
        invoice = BitmapFactory.decodeResource(this.getResources(), R.drawable.invoice_grey);
        pay = BitmapFactory.decodeResource(this.getResources(), R.drawable.qr_code_grey);
        sendmoney = BitmapFactory.decodeResource(this.getResources(), R.drawable.send_money_grey);
        txn_history = BitmapFactory.decodeResource(this.getResources(), R.drawable.others);


        where_to_pay = BitmapFactory.decodeResource(this.getResources(), R.drawable.where_to_pay_icon);

        recharge_paybill = BitmapFactory.decodeResource(this.getResources(), R.drawable.topupchanged);
        mobilebill = BitmapFactory.decodeResource(this.getResources(), R.drawable.mobilebillnew);

        if (moduleName.equalsIgnoreCase("utility bills")) {
            prepaid_cards = BitmapFactory.decodeResource(this.getResources(), R.drawable.pre_paidcard);
        } else {
            prepaid_cards = BitmapFactory.decodeResource(this.getResources(), R.drawable.pre_paidcard);
        }
        my_offers = BitmapFactory.decodeResource(this.getResources(), R.drawable.offer);

        help = BitmapFactory.decodeResource(this.getResources(), R.drawable.help);

        bookeey_mainmenu_loadwallet_btn = (Button) findViewById(R.id.bookeey_mainmenu_loadwallet_btn);
        bookeey_mainmenu_pay_btn = (Button) findViewById(R.id.bookeey_mainmenu_pay_btn);
        //more_text = (TextView) findViewById(R.id.more_text);
        scroll = (ScrollView) findViewById(R.id.scroll);
        // more_img1 = (ImageView) findViewById(R.id.more_img1);


        //Commented for Creditcard view START Feb12

        image_person = (ImageView) findViewById(R.id.image_person);
        image_person.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });


        //Commented for Creditcard view END Feb12


        refresh();

        /*if (application.getInvoices_count() > 0) {
            isSound = customerLoginRequestReponse.isSpeakstatus();
        }*/
        if (getIntent().getExtras() != null) {
            isSound = getIntent().getExtras().getBoolean("voice");
            /*if (isSound) {
                String text = "You have got new invoices, please pay";
                tts.speak(text, TextToSpeech.QUEUE_ADD, null);
            }*/
            invoice_count = application.getInvoices_count();
        }

        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }

        updateProfile(R.id.nameTextooredo, R.id.wallet_id, R.id.balance_id);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mainmenu_gridview = (ExpandableHeightGridView) findViewById(R.id.grid_view);
        mainmenu_gridview.setExpanded(true);
        adapter1 = new MyAdapter(this);
        mainmenu_gridview.setAdapter(adapter1);
        /*LinearLayout ll = (LinearLayout) findViewById(R.id.more_box);
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scroll.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                //more_img1.setBackgroundResource(R.drawable.down_arrow);
                if (!scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT)) {
                    goleft();
                }
            }

            private void goleft() {
                scroll.fullScroll(HorizontalScrollView.FOCUS_DOWN);
                //more_img1.setBackgroundResource(R.drawable.up_arrow_new);
            }
        });*/
        /*mainmenu_gridview.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent me) {

                System.out.println("Position is " + me.getRawY());

                // int action = me.getActionMasked();  // MotionEvent types such as ACTION_UP, ACTION_DOWN
                float currentXPosition = me.getX();
                float currentYPosition = me.getY();
                StateListDrawable states = null;

                int position = mainmenu_gridview.pointToPosition((int) currentXPosition, (int) currentYPosition);
                switch (position) {

                    case 0:
                        FrameLayout view = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView = (ImageView) view.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.recharge_wallet_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.recharge_wallet));
                        imageView.setImageDrawable(states);
                        break;
                    case 1:
                        FrameLayout view12 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView12 = (ImageView) view12.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.invoice_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.invoice));
                        imageView12.setImageDrawable(states);
                        break;
                    case 2:
                        FrameLayout view1 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView1 = (ImageView) view1.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.qrcode_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.qrcode));
                        imageView1.setImageDrawable(states);
                        break;
                    case 3:

                        FrameLayout view2 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView2 = (ImageView) view2.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.send_money_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.send_money));
                        imageView2.setImageDrawable(states);
                        break;
                    case 4:

                        FrameLayout view3 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView3 = (ImageView) view3.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.where_to_pay_icon_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.where_to_pay_icon));
                        imageView3.setImageDrawable(states);
                        break;

                    case 5:

                        FrameLayout view4 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView4 = (ImageView) view4.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.topupchanged_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.topupchanged));
                        imageView4.setImageDrawable(states);
                        break;
                    case 6:

                        FrameLayout view5 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView5 = (ImageView) view5.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.mobilebillnew_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.mobilebillnew));
                        imageView5.setImageDrawable(states);
                        break;
                    case 7:

                        FrameLayout view6 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView6 = (ImageView) view6.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.pre_paidcard_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.pre_paidcard));
                        imageView6.setImageDrawable(states);
                        break;


                    case 8:

                        FrameLayout view7 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView7 = (ImageView) view7.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.offer_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.offer));
                        imageView7.setImageDrawable(states);
                        break;


                    *//*case 9:

                        FrameLayout view9 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView9 = (ImageView) view9.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.store_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.store));
                        imageView9.setImageDrawable(states);
                        break;*//*
                    case 9:
                        FrameLayout view8 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView8 = (ImageView) view8.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.help_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.help));
                        imageView8.setImageDrawable(states);
                        break;
                    case 10:
                        FrameLayout view11 = (FrameLayout) mainmenu_gridview.getChildAt(position);
                        ImageView imageView11 = (ImageView) view11.findViewById(R.id.picture);
                        //for handling to images in onclick android
                        states = new StateListDrawable();
                        states.addState(new int[]{android.R.attr.state_pressed},
                                getResources().getDrawable(R.drawable.others_secondary));

                        states.addState(new int[]{},
                                getResources().getDrawable(R.drawable.others));
                        imageView11.setImageDrawable(states);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });*/


        //Commented for Creditcardview Feb12 START


        bookeey_mainmenu_loadwallet_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


//                loadMoneyCheck();

//                showNewFlowAlertDialogue();

                showNewFlowAlertDialogueCreditCardView();

            }
        });
        bookeey_mainmenu_pay_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


//                showAmountEntryPayDialogue();

//                showNewFlowAlertDialogue();

                showNewFlowAlertDialogueCreditCardView();

            }
        });

        //Commented for Creditcardview Feb12 END


        mainmenu_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> view, View v, int position, long id) {
                switch (position) {
                    case 0:

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

//                        intent = new Intent(MainActivityNewFlow.this, SendMoneyLeg1RequestActivity.class);
//                        startActivity(intent);


                        //Prior to creditcard view
//                        showNewFlowAlertDialogue();

                        //Implemented on Feb 13
                        showNewFlowAlertDialogueCreditCardView();


                        //Commented on Feb 13 START to show new alert dialog

//                        CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
//                        String deviceID = ((CoreApplication) getApplicationContext()).getThisDeviceUniqueAndroidId();
//                        clr.setDeviceId(deviceID);
//
//                        CoreApplication application = (CoreApplication) getApplicationContext();
//                        String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application,true,false,true));
//                        ProgressDialogFrag progress = new ProgressDialogFrag();
//                        Bundle bundle_req = new Bundle();
//                        bundle_req.putString("uuid", uiProcessorReference);
//                        progress.setCancelable(true);
//                        progress.setArguments(bundle_req);
//                        progress.show(getSupportFragmentManager(), "progress_dialog");

                        //Feb 13 END

                        break;
                    case 1:
//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

                        utilityCardList();
                        break;
                    case 2:
                        mobileBill();
                        break;
                    case 3:

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

                        intent = new Intent(getBaseContext(), TopUpInitialActivityNewFlow.class);
                        intent.putExtra("BACK", "");
                        startActivity(intent);
                        break;
                    case 4:

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

//                        onItemClicked = true;
//                        invoice();


                        //Old prior to creditcardview
//                        showNewFlowAlertDialogue();


                        //Implemented on Feb 13
                        showNewFlowAlertDialogueCreditCardView();


                        //Commented on Feb 13 START to show new alert dialog

//                        CustomerMobileNumberRequest _clr = new CustomerMobileNumberRequest();
//                        String _deviceID = ((CoreApplication) getApplicationContext()).getThisDeviceUniqueAndroidId();
//                        _clr.setDeviceId(_deviceID);
//
//                        CoreApplication _application = (CoreApplication) getApplicationContext();
//                        String _uiProcessorReference = _application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(_clr, true, _application,true,false,true));
//                        ProgressDialogFrag _progress = new ProgressDialogFrag();
//                        Bundle _bundle_req = new Bundle();
//                        _bundle_req.putString("uuid", _uiProcessorReference);
//                        _progress.setCancelable(true);
//                        _progress.setArguments(_bundle_req);
//                        _progress.show(getSupportFragmentManager(), "progress_dialog");

                        //Feb 13 END

                        break;
                    case 5:

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            boolean isEnabled = checkLocationPermission();
//                            if (isEnabled) {
//                                updateSlidingPanel(_SLIDE_ECOM);
//                            }
//                        } else {
//                            updateSlidingPanel(_SLIDE_ECOM);
//                        }


//                        Newflow
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            boolean isEnabled = checkLocationPermission();
//                            if (isEnabled) {
//                                updateSlidingPanelNewFlow(_SLIDE_ECOM);
//                            }
//                        } else {
//                            updateSlidingPanelNewFlow(_SLIDE_ECOM);
//                        }


//                        Intent intent = new Intent(MainActivityNewFlow.this, MerchantListCatogorieyActivityNewFlowNewUI.class);
//                        startActivity(intent);


                        Intent intent = new Intent(MainActivityNewFlow.this, MerchantListCatogorieyActivityNewFlowNewUIActivity.class);
                        startActivity(intent);


                        break;
                    case 6:

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

                        intent = new Intent(getBaseContext(), NewOffersActivityNewFlow.class);
                        startActivity(intent);
                        break;
                    case 7:

//                        showNewFlowAlertDialogue();

//                        Toast.makeText(MainActivityNewFlow.this, "Need to implement", Toast.LENGTH_SHORT).show();

                        Intent serviceIntent = new Intent(getBaseContext(), SyncServiceNewFlow.class);
                        serviceIntent.putExtra("type", SyncServiceNewFlow.TYPE_USER_LOGGED_IN_STATUS);
                        startService(serviceIntent);
                        ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                        Intent i = new Intent(getBaseContext(), TransactionHistoryActivityNewFlow.class);
                        startActivity(i);


                        break;
                    case 8:
                        intent = new Intent(getBaseContext(), Help.class);
                        startActivity(intent);
                        break;
                    /*case 9:
                        intent = new Intent(getBaseContext(), Help.class);
                        startActivity(intent);
                        break;
                    case 10:
                        Intent serviceIntent = new Intent(getBaseContext(), SyncService.class);
                        serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
                        startService(serviceIntent);
                        ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                        Intent i = new Intent(getBaseContext(), TransactionHistoryActivity.class);
                        startActivity(i);
                        break;*/
                    default:
                        break;
                }
            }


        });
        userInfoUpdateHandler = new UserInfoUpdateHandler(this);
//        updateUserInfo(userInfoUpdateHandler);


        //For location
        //Building a instance of Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addOnConnectionFailedListener(this).addConnectionCallbacks(this).build();


        //NewFlow START
        //Commented for creditcard view START Feb 12

        Button btn_newflow_login = (Button) findViewById(R.id.btn_newflow_login);
        btn_newflow_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(MainActivityNewFlow.this, OoredooValidation.class);
//                startActivity(intent);


//                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
//                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
//                clr.setDeviceId(       deviceID);
//
//                CoreApplication application = (CoreApplication) getApplication();
//                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDLoginCheckProcessingNewFlow(clr, true, application));
//                ProgressDialogFrag progress = new ProgressDialogFrag();
//                Bundle bundle = new Bundle();
//                bundle.putString("uuid", uiProcessorReference);
//                progress.setCancelable(true);
//                progress.setArguments(bundle);
//                progress.show(getFragmentManager(), "progress_dialog");
            }
        });


        Button btn_newflow_register = (Button) findViewById(R.id.btn_newflow_register);
        btn_newflow_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivityNewFlow.this, OoredooRegistrationNewFlow.class);
                startActivity(intent);
            }
        });


        //NewFlow END

        //language selection


        language_layout = (LinearLayout) findViewById(R.id.language_layout);
        coutry_flag_img = (ImageView) findViewById(R.id.coutry_flag_img);
        language_text = (TextView) findViewById(R.id.language_text);

        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(MainActivityNewFlow.this, selectedLanguage);
        }


        if (language_text.getText().toString().equals("English")) {
            language_text.setText(getResources().getString(R.string.login_arabic));
            coutry_flag_img.setImageResource(R.drawable.kuwait);
        } else {
            language_text.setText(getResources().getString(R.string.login_english));
            coutry_flag_img.setImageResource(R.drawable.usa);
        }


        language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (language_text.getText().toString().equals("English")) {
                    language_text.setText(getResources().getString(R.string.login_arabic));
                    selectedLanguage = "en";
                    coutry_flag_img.setImageResource(R.drawable.kuwait);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(MainActivityNewFlow.this, "en");
                    refresh(MainActivityNewFlow.this, "en");

                } else {
                    selectedLanguage = "ar";
                    language_text.setText(getResources().getString(R.string.login_english));
                    coutry_flag_img.setImageResource(R.drawable.usa);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(MainActivityNewFlow.this, "ar");
                    refresh(MainActivityNewFlow.this, "ar");
                }
            }
        });


        LinearLayout demo_layout = (LinearLayout) findViewById(R.id.demo_layout);
        TextView demo_text = (TextView) findViewById(R.id.demo_text);

        demo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

            }
        });

        demo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

            }
        });


        //Commented for creditcard view END Feb 12


        //Rahman //Check for updates

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception e) {
            Log.e("TAG1", "" + e);
        }
        int versionCode = BuildConfig.VERSION_CODE;
        versionname = BuildConfig.VERSION_NAME;

        VersionChecker versionChecker = new VersionChecker();
        versionChecker.execute();


        // FrameLayout push_notifications_frame_layout =  (FrameLayout)findViewById(R.id.push_notifications_frame_layout);


//        BadgeView badge = new BadgeView(this, push_notification_message_bell);
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setBackgroundResource(R.drawable.bookeey_small);
//        badge.setText("1");
//        badge.show();


        push_notifications_frame_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Fetch pushnotification message from server
                GetPushNotificationMessageRequest pushNotificationMessageRequest = new GetPushNotificationMessageRequest();

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new GetPushNotificationMessageProcessing(pushNotificationMessageRequest, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");


                //For Test
/*
                try {
//                    PushNotificationDetailsPojo responsePojo = new Gson().fromJson(response_json, PushNotificationDetailsPojo.class);


                    ArrayList<String> push_messages_al =  new ArrayList<String>();
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Knife Chicken");
                    push_messages_al.add("Welocome to Bookeey, you have very nice offer at Lulu Hypermarket");

                    JSONObject finalJsonObject = new JSONObject();

                    JSONArray pushMessageObjectJsonArray =  new JSONArray();

                    for (int i=0;i< push_messages_al.size();i++) {



                        JSONObject pushMessageJsonObject =  new JSONObject();

                        Charset charset = Charset.forName("ISO-8859-6");
                        CharsetDecoder decoder = charset.newDecoder();
                        ByteBuffer buf = ByteBuffer.wrap(push_messages_al.get(i).getBytes());
                        CharBuffer cbuf = decoder.decode(buf);
                        CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

                        pushMessageJsonObject.put("message", pushNotificationMessage);


                        pushMessageJsonObject.put("time", "30m ago");

                        pushMessageObjectJsonArray.put(pushMessageJsonObject);



                    }

                    finalJsonObject.put("list",pushMessageObjectJsonArray);

//                    Charset charset = Charset.forName("ISO-8859-6");
//                    CharsetDecoder decoder = charset.newDecoder();
//                    ByteBuffer buf = ByteBuffer.wrap(finalJsonObject.toString().getBytes());
//                    CharBuffer cbuf = decoder.decode(buf);
//                    CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

//                    Toast toast = Toast.makeText(LoginActivityFromSplashNewFlow.this, finalJsonObject.toString(), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();


                    Intent intent = new Intent(MainActivityNewFlow.this, ShowPushNotificationMessageDialogActivity.class);
                    intent.putExtra(ShowPushNotificationMessageDialogActivity.KEY_PUSH_NOTIFICATION_MESSAGES,finalJsonObject.toString());
                    startActivity(intent);



                } catch (Exception e) {

                    Log.e("Push Notifica Msg Ex:", "" + e.getMessage());

                    Toast toast = Toast.makeText(MainActivityNewFlow.this, "System Error! "+e.getMessage(), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();



                }
*/


            }
        });


        //Creditcard view pager view
//        ViewPager viewPager = findViewById(R.id.view_pager);
//
//        FragmentManager fm = getSupportFragmentManager();
//
//        viewPager.setAdapter(new ViewPagerAdapterNewFlow(fm));


    }

    public void refresh(Activity loginActivity, String selectedLanguage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = loginActivity.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, MainActivityNewFlow.class);
            startActivity(refresh);
            finish();
        } else {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Resources resources = loginActivity.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            Intent refresh = new Intent(this, MainActivityNewFlow.class);
            startActivity(refresh);
            finish();
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivityNewFlow.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void changeText(final TextView mTitleTextView) {
        mTitleTextView.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                if (isScrolled) {
                    if (bannerList.size() > 0) mTitleTextView.setText(bannerList.get(i));
                    i++;
                    if (i == bannerList.size()) i = 0;
                    animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
                    animLeft.setDuration(900);
                    mTitleTextView.startAnimation(animLeft);
                    mTitleTextView.postDelayed(this, 5000);
                }
            }
        });
    }

    private void refresh() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 2 * 60 * 1000;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter1.notifyDataSetChanged();
                        isSound = application.isSpeakstatus();
                        refresh();
                    }
                });
            }
        }).start();

        /*handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                *//*if (application.getInvoices_count() > invoice_count) {
                    adapter1.notifyDataSetChanged();
                    isSound = application.isSpeakstatus();
                }*//*
                adapter1.notifyDataSetChanged();
                isSound = application.isSpeakstatus();
            }
        }, 2 * 60 * 1000);*/
    }

    private void utilityCardList() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        MobileBillOperatorsRequest mobileBillOperatorsRequest = new MobileBillOperatorsRequest();
        String uiProcessorReference = application.addUserInterfaceProcessor(new PrepaidCardProcessingNewFlow(mobileBillOperatorsRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    private void mobileBill() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        MobileBillOperatorsRequest mobileBillOperatorsRequest = new MobileBillOperatorsRequest();
        mobileBillOperatorsRequest.setVersionNumber(customerLoginRequestReponse.getDomesticRechargeVersion());
        String uiProcessorReference = application.addUserInterfaceProcessor(new MobileBillProcessingNewFlow(mobileBillOperatorsRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    private void invoice() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        //CustomSharedPreferences.saveStringData(getApplicationContext(), mobile_number, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        String mobno = CustomSharedPreferences.getStringData(getApplicationContext(), MOBILE_NUMBER);
        //InvoiceDetailsPojo invoiceRequest = new InvoiceDetailsPojo();
        // invoiceRequest.setVersionNumber(customerLoginRequestReponse.getDomesticRechargeVersion());
        //invoiceRequest.setMobileNumber(mobno);
        GenericRequest invoiceRequest = new GenericRequest();
        invoiceRequest.setG_userId(mobno);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoiceProcessing(invoiceRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    private void invoiceForSessionOut() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        //CustomSharedPreferences.saveStringData(getApplicationContext(), mobile_number, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        String mobno = CustomSharedPreferences.getStringData(getApplicationContext(), MOBILE_NUMBER);
        //InvoiceDetailsPojo invoiceRequest = new InvoiceDetailsPojo();
        // invoiceRequest.setVersionNumber(customerLoginRequestReponse.getDomesticRechargeVersion());
        //invoiceRequest.setMobileNumber(mobno);
        GenericRequest invoiceRequest = new GenericRequest();
        invoiceRequest.setG_userId(mobno);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoiceProcessingForSessionOut(invoiceRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    @Override
    public void onPause() {
        super.onPause();

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


//        Toast.makeText(MainActivityNewFlow.this,"Guest page",Toast.LENGTH_LONG).show();


        Log.e("onResume Kill1", "MainActivity " + application.getCustomerLoginRequestReponse());
        CustomerLoginRequestReponse response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();

        Log.e("onResume Kill2", "Limits: " + application.getCustomerLoginRequestReponse().getFilteredLimits());


        //OldFlow "Need to be enabled"
//        if (response.getFilteredLimits() == null) {
//
//            Intent i = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(i);
//            finishAffinity();
//        }


//To identify session out July 02 started
        //OldFlow "Need to be enabled"
//        invoiceForSessionOut();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Don't forget to shutdown tts!
        if (tts != null) {
            if (i == 1) {
                tts.stop();
                tts.shutdown();
            } else {
                tts.shutdown();
            }
        }
    }

    /*private void speakOut() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (application.getInvoices_count() != 0) {
                            for (i = 0; i < 1; i++) {
                                int timeToBlink = 1000;    //in milisseconnds
                                try {
                                    Thread.sleep(timeToBlink);
                                } catch (Exception e) {
                                }
                                letsspeek();
                            }
                        }
                    }
                });
            }
        }).start();
    }*/

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //speakOut();
                letsspeek();

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void letsspeek() {
        if (isSound) {
            String text = "You have got new invoices, please pay";
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }

    }

    public void onStart() {
        super.onStart();
        // Initiating the GoogleApiClient Connection when the activity is visible
        googleApiClient.connect();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.registerListener(installStateUpdatedListener);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                LayoutInflater li = LayoutInflater.from(MainActivityNewFlow.this);
                View promptsView = li.inflate(R.layout.custom_update_playstore_dialog, null);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityNewFlow.this);
                alertDialog.setView(promptsView);
                alertDialog.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
            }
        });
    }

    public void onStop() {
        super.onStop();
        //Disconnecting the GoogleApiClient when the activity goes invisible
        googleApiClient.disconnect();
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    //This callback is invoked when the user grants or rejects the location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT);
                break;
        }
    }

    private void getCurrentLocation() {
        //Checking if the location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        //Fetching location using FusedLOcationProviderAPI
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();
            }
        });
        //In some rare cases Location obtained can be null
        Log.e("Location: ", "" + wayLatitude + " - " + wayLongitude);
        CustomSharedPreferences.saveStringData(getApplicationContext(), String.valueOf(wayLatitude), CustomSharedPreferences.SP_KEY.CURRENT_LATITUTE);
        CustomSharedPreferences.saveStringData(getApplicationContext(), String.valueOf(wayLongitude), CustomSharedPreferences.SP_KEY.CURRENT_LONGITUDE);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        getCurrentLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    void showLoadMoneyNeutralDailog() {
        final Dialog dialog_loadmoney = new Dialog(this);
        dialog_loadmoney.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loadmoney.setContentView(R.layout.loadmoney);
        final EditText amount_enter = (EditText) dialog_loadmoney.findViewById(R.id.ypc_p2m_amount_edit);
        Button proceed_btn = (Button) dialog_loadmoney.findViewById(R.id.pay_load_pay_btn_new);
        Button cancel_btn = (Button) dialog_loadmoney.findViewById(R.id.pay_load_cancel_btn_new);
        TextView your_wallet_balance_Tv = (TextView) dialog_loadmoney.findViewById(R.id.your_wallet_balance_Tv);
        TextView you_can_load_upto_Tv = (TextView) dialog_loadmoney.findViewById(R.id.you_can_load_upto_Tv);
        TextView you_can_load_minimum_Tv = (TextView) dialog_loadmoney.findViewById(R.id.you_can_load_minimum_Tv);
        final CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("PGRECHARGE");
        amount_enter.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});
        you_can_load_upto_Tv.setText(PriceFormatter.format(walletLimits.getLoadmoneyMaxPerTxn(), 3, 3));
        your_wallet_balance_Tv.setText(PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
        you_can_load_minimum_Tv.setText(PriceFormatter.format(walletLimits.getLoadmoneyMinPerTxn(), 3, 3));
        proceed_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amount_enter.getText().toString().trim();
                if (amount.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.invoice_enter_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                Double amount_db = Double.parseDouble(amount);
                if (amount_db == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.invoice_enter_valid_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                loadMoneyRequest(amount_db);
                //newly added
                if (dialog_loadmoney.isShowing()) {
                    dialog_loadmoney.dismiss();
                }

                /*PaymentForm paymentForm = new PaymentForm();
                paymentForm.setMobileNumber(CustomSharedPreferences.getStringData(getBaseContext(), MOBILE_NUMBER));
                paymentForm.setPrice(amount_db);
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new LoadMoneyProcessing(paymentForm, application, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getFragmentManager(), "progress_dialog");*/
            }
        });
        cancel_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_loadmoney.dismiss();
            }
        });
        dialog_loadmoney.show();
    }

    private void loadMoneyRequest(Double amount_db) {
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setMobileNumber(CustomSharedPreferences.getStringData(getBaseContext(), MOBILE_NUMBER));
        paymentForm.setPrice(amount_db);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new LoadMoneyProcessing(paymentForm, application, false));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    void showAmountEntryPayDialogue() {
        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.ypc_get_amount_new);
        final EditText amountField = (EditText) promptsView.findViewById(R.id.ypc_getAmoint_getAmount);
        final CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("P2M");

        amountField.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});


        final EditText pay_via_qrcode_pin_edt = (EditText) promptsView.findViewById(R.id.pay_via_qrcode_pin_edt);
        final Button pay_qrcode_cancel_btn_new = (Button) promptsView.findViewById(R.id.pay_qrcode_cancel_btn_new);
        final Button pay_qrcode_pay_btn_new = (Button) promptsView.findViewById(R.id.pay_qrcode_pay_btn_new);
        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                amount = amountField.getText().toString();
                Double amount_double = 0.000;
                try {
                    amount_double = Double.parseDouble(amount);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_please_enter_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                }
                if (amount_double > limits.getTpinLimit()) {
                    pay_via_qrcode_pin_edt.setVisibility(View.VISIBLE);
                } else {
                    pay_via_qrcode_pin_edt.setVisibility(View.GONE);
                }
            }
        });
        pay_qrcode_cancel_btn_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                promptsView.dismiss();
            }
        });
        pay_qrcode_pay_btn_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_str = amountField.getText().toString().trim();
                tpin = pay_via_qrcode_pin_edt.getText().toString().trim();
                if (amount_str.length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_please_enter_amount), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                Double amount = Double.parseDouble(amount_str);
                if (amount == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_please_enter_amount), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (amount > customerLoginRequestReponse.getWalletBalance()) {
                    Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.p2m_sufficient_balance), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                payViaQrCodeProcess();
                promptsView.dismiss();
            }
        });
        promptsView.show();
    }


    public void showNewFlowAlertDialogueCreditCardView() {
        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.mainmenu_newflow_alert_creditcard_view);

        final ImageView btn_close = (ImageView) promptsView.findViewById(R.id.btn_close);
        final Button mainmenu_newflow_creditcard_view_proceed = (Button) promptsView.findViewById(R.id.mainmenu_newflow_creditcard_view_proceed);

        mainmenu_newflow_creditcard_view_proceed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

                //Jan 22 After call  START

                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, false, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");

                //Jan 22 After call  END

            }
        });


        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

            }
        });

        promptsView.show();
    }


    public void showNewFlowAlertDialogue() {
        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.mainmenu_newflow_alert_new);

        final Button mainmenu_newflow_cancel = (Button) promptsView.findViewById(R.id.mainmenu_newflow_cancel);
        final Button mainmenu_newflow_register = (Button) promptsView.findViewById(R.id.mainmenu_newflow_register);
        final TextView mainmenu_newflow_login = (TextView) promptsView.findViewById(R.id.mainmenu_newflow_login);

        mainmenu_newflow_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

//                Intent intent = new Intent(MainActivityNewFlow.this, OoredooValidation.class);
//                startActivity(intent);

                //Old
//                Intent in = new Intent(getBaseContext(), LoginActivityNewFlow.class);
//                startActivity(in);


                //Jan 22 After call  START

                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, false, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");

                //Jan 22 After call  END

            }
        });


        mainmenu_newflow_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();


                //Old
//                Intent intent = new Intent(MainActivityNewFlow.this, OoredooRegistrationNewFlow.class);
//                startActivity(intent);

                //Jan 22 After call  START

                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application, true, true, false));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getSupportFragmentManager(), "progress_dialog");

                //Jan 22 After call  END


            }
        });

        mainmenu_newflow_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();


            }
        });

        promptsView.show();
    }


    public void updateSlidingPanelNewFlow(int slideType) {
        View sliding_frame_view = (View) findViewById(R.id.sliding_list_frame);
        sliding_frame_view.setBackgroundColor(Color.WHITE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag = null;
        Fragment oldFrag = null;
        switch (slideType) {
            case _SLIDE_ECOM:
                frag = fragmentManager.findFragmentByTag(MerchantSelectionSliderList.tag);
                if (frag == null) {
                    frag = new MerchantSelectionSliderListNewFlow();
                }
                mDrawer.closeDrawer(sliding_frame_view);
                break;
            default:
                return;
        }
        if (frag != null) {
            oldFrag = fragmentManager.findFragmentById(R.id.sliding_list_frame);
            if (oldFrag != null) {
                if (oldFrag.getClass() == frag.getClass()) {
                    if (mDrawer.isDrawerOpen(sliding_frame_view)) {
                        mDrawer.closeDrawer(sliding_frame_view);
                    } else {
                        mDrawer.openDrawer(sliding_frame_view);
                    }
                    return;
                }
            }
            if (oldFrag != null) {
                mDrawer.closeDrawer(sliding_frame_view);
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.sliding_list_frame, frag);
            transaction.commit();
            mDrawer.openDrawer(sliding_frame_view);
        }
    }

    public void updateSlidingPanel(int slideType) {
        View sliding_frame_view = (View) findViewById(R.id.sliding_list_frame);
        sliding_frame_view.setBackgroundColor(Color.WHITE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag = null;
        Fragment oldFrag = null;
        switch (slideType) {
            case _SLIDE_ECOM:
                frag = fragmentManager.findFragmentByTag(MerchantSelectionSliderList.tag);
                if (frag == null) {
                    frag = new MerchantSelectionSliderList();
                }
                mDrawer.closeDrawer(sliding_frame_view);
                break;
            default:
                return;
        }
        if (frag != null) {
            oldFrag = fragmentManager.findFragmentById(R.id.sliding_list_frame);
            if (oldFrag != null) {
                if (oldFrag.getClass() == frag.getClass()) {
                    if (mDrawer.isDrawerOpen(sliding_frame_view)) {
                        mDrawer.closeDrawer(sliding_frame_view);
                    } else {
                        mDrawer.openDrawer(sliding_frame_view);
                    }
                    return;
                }
            }
            if (oldFrag != null) {
                mDrawer.closeDrawer(sliding_frame_view);
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.sliding_list_frame, frag);
            transaction.commit();
            mDrawer.openDrawer(sliding_frame_view);
        }
    }

    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_image, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityNewFlow.this);
        alertDialog.setView(promptsView);
        alertDialog.setPositiveButton(getResources().getString(R.string.registration_camera), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /*if (!checkPermissionForCamera()) {
                    requestPermissionForCamera(1);
                } else {
                    final Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    File mImageCaptureUri = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageCaptureUri));
                    camera.putExtra("android.intent.extras.CAMERA_FACING", 1);
                    startActivityForResult(camera, 1);
                }*/

                if (!checkPermissionForCamera()) {
                    Log.i("IF", "if");
                    requestPermissionForCamera(CAMERA_REQUEST_CODE);
                } else {
                    Log.i("ELSE", "else");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                    } else {
                        Toast.makeText(getBaseContext(), "Your camera can't able to take the data of picture ", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.registration_gallery), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY);
                }

               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, which);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 256);
                    intent.putExtra("outputY", 256);
                    try {
                        intent.putExtra("return-data", true);
                        startActivityForResult(Intent.createChooser(intent,
                                "Complete action using"), PICK_FROM_GALLERY);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getResources(), "" + e, Toast.LENGTH_LONG).show();
                    }
                }*/
            }

        });
        alertDialog.show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void hideProgressIndicatorInAction(final ImageView imageView) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.INVISIBLE);
                onCompletionOfSyncProcess();
            }

        });
    }

    public void onCompletionOfSyncProcess() {
        imageview.setVisibility(View.VISIBLE);
    }

    public void payViaQrCodeProcess() {
        P2MBarcodeGenInitValidationRequest request = new P2MBarcodeGenInitValidationRequest();
        request.setTpin(tpin);
        String timezone = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        long currentLocalTime = cal.getTimeInMillis();
        request.setBarcodeGeneratedDate(currentLocalTime);
        request.setTxnAmount((Double.parseDouble(amount)));
        String barcodeData = generateSecureBarCodeText((byte) 2, PriceFormatter.format(Double.parseDouble(amount_str), 3, 3));
        request.setBarcodeData(Hex.toHex(new BigInteger(barcodeData).toByteArray()));
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new PayToMerchantValidationProcessing(request, application, barcodeData, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    private void loadMoneyCheck() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        String jsondata = new Gson().toJson(paymentForm);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOADMONEY_CHECK_REQUEST.getURL());
        buffer.append("?data=" + URLUTF8Encoder.encode(jsondata));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    walletLimits = new Gson().fromJson((String) msg.obj, WalletLimits.class);
                    if (walletLimits != null && walletLimits.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && walletLimits.getG_status() == 1) {
                        walletLimits = new Gson().fromJson((String) msg.obj, WalletLimits.class);
                        showLoadMoneyNeutralDailog();
                    } else if (walletLimits.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(MainActivityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(MainActivityNewFlow.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivityNewFlow.this.finish();
                    } else if (walletLimits != null && walletLimits.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && walletLimits.getG_status() != 1) {
                        Toast.makeText(getBaseContext(), walletLimits.getG_errorDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();


        //Commented for
//        channel-is-unrecoverably-broken-and-will-be-disposed Exception June 24/2019

//        showIfNotVisible("");


    }

    public String generateSecureBarCodeText(byte spIndex, String amount) {
        try {
            CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
            String android_id = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
            byte[] counter = {0x00, 0x00};
            byte[] staticId = Hex.toByteArr(customerLoginRequestReponse.getUniqueCustomerId());
            amount_str = amount;
            byte[] amount_bytes = Hex.floatToByteArray(Float.parseFloat(amount));
            byte[] random = SecurityUtils.generateApplicationAESKey(4);
            byte[] time_bytes = new BarCodeTimeParser().getEncoded();
            byte[] dataToBeMaced = SMSUtils.mountLVParams(null, new Object[]{counter, random, amount_bytes, time_bytes}, new byte[]{SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed});
            byte[] mac = SecurityUtils.generateMac(Hex.toByteArr(customerLoginRequestReponse.getMac_key()), Hex.toByteArr(android_id), dataToBeMaced, 0, dataToBeMaced.length, 32);
            byte[] dataToBeEnced = SMSUtils.mountLVParams(null, new Object[]{counter, random, time_bytes, mac}, new byte[]{SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed});
            byte[] enc = SecurityUtils.encipherData(Hex.toByteArr(customerLoginRequestReponse.getEnc_key()), Hex.toByteArr(android_id), dataToBeEnced, 0, dataToBeEnced.length);
            BcodeHeaderEncoder headerEncoder = new BcodeHeaderEncoder(staticId, Float.parseFloat(amount), (byte) spIndex);
            StringBuffer url = new StringBuffer();
            url.append(Hex.toHex(headerEncoder.getEncoded()));//6-7
            url.append(Hex.toHex(enc));
            url.append(Hex.toHex(enc.length));
            return new BigInteger(Hex.toByteArr(url.toString())).toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.empty_refresh).setVisible(true);


        //Hiding 'Refresh'
//        menu.findItem(R.id.empty_refresh).setVisible(false);


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            File mImageCaptureUri = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
            try {
                cropCapturedImage(Uri.fromFile(mImageCaptureUri));
            } catch (ActivityNotFoundException aNFE) {
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            if (resultCode != RESULT_CANCELED) {
                if (requestCode == PICK_FROM_GALLERY) {
                    //imageView.setImageBitmap(photo);
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
                        Paint paint = new Paint();
                        paint.setShader(shader);
                        Canvas c = new Canvas(circleBitmap);
                        c.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2, circleBitmap.getWidth() / 2, paint);
                        image_person.setImageBitmap(circleBitmap);
                        bitmapToString(circleBitmap);
                    } catch (IOException ieo) {
                        Toast.makeText(getResources(), "can't able to select the image", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
            *//*Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException ieo) {
                Toast.makeText(getApplicationContext(), "can't able to select the image", Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
            Paint paint = new Paint();
            paint.setShader(shader);
            Canvas c = new Canvas(circleBitmap);
            c.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2, circleBitmap.getWidth() / 2, paint);
            image_person.setImageBitmap(circleBitmap);
            bitmapToString(circleBitmap);*//*
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Bitmap circleBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                BitmapShader shader = new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Paint paint = new Paint();
                paint.setShader(shader);
                paint.setAntiAlias(true);
                Canvas c = new Canvas(circleBitmap);
                c.drawCircle(imageBitmap.getWidth() / 2, imageBitmap.getHeight() / 2, imageBitmap.getWidth() / 2, paint);
                image_person.setImageBitmap(circleBitmap);
                bitmapToString(circleBitmap);
            } else
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
        }
    }*/

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    Bitmap circleBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    BitmapShader shader = new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    Paint paint = new Paint();
                    paint.setShader(shader);
                    paint.setAntiAlias(true);
                    Canvas c = new Canvas(circleBitmap);
                    c.drawCircle(imageBitmap.getWidth() / 2, imageBitmap.getHeight() / 2, imageBitmap.getWidth() / 2, paint);
                    image_person.setImageBitmap(circleBitmap);
                    bitmapToString(circleBitmap);
                }
                if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
                    try {

                        final Uri imageUri = data.getData();
                        try {
                            cropCapturedImage(imageUri);
                        } catch (ActivityNotFoundException aNFE) {
                            String errorMessage = "Sorry - your device doesn't support the crop action!";
                            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        image_person.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                if (requestCode == 0 && resultCode == RESULT_OK) {
                    Bundle extras2 = data.getExtras();
                    if (extras2 != null) {
                        Bitmap imageBitmap = (Bitmap) extras2.get("data");
                        Bitmap circleBitmap = Bitmap.createBitmap(imageBitmap.getWidth(), imageBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        BitmapShader shader = new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                        Paint paint = new Paint();
                        paint.setShader(shader);
                        paint.setAntiAlias(true);
                        Canvas c = new Canvas(circleBitmap);
                        c.drawCircle(imageBitmap.getWidth() / 2, imageBitmap.getHeight() / 2, imageBitmap.getWidth() / 2, paint);
                        image_person.setImageBitmap(circleBitmap);
                        bitmapToString(circleBitmap);
                    } else
                        Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void cropCapturedImage(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 0);
    }

    private void showIfNotVisible(String title) {
        if (!progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.show();
            progressDialog.isShowing();
        } else {
            progressDialog.setTitle(title);
            progressDialog.show();
            progressDialog.isShowing();
        }
    }

    private void hideIfVisible() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        CustomSharedPreferences.saveStringData(getBaseContext(), temp, CustomSharedPreferences.SP_KEY.IMAGE);
        return temp;
    }

    public final int convertDimensionPixelsToPixels(float dimensionPixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dimensionPixels * (scale) + 0.5f);
    }

    private void blink(final TextView count_text, final ImageView bell_image) {

        //if (mBlinking) {
        final Handler handler2 = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 1000;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        if (count_text.getVisibility() == View.VISIBLE && bell_image.getVisibility() == View.VISIBLE) {
                            count_text.setVisibility(View.INVISIBLE);
                            bell_image.setVisibility(View.INVISIBLE);
                        } else {
                            count_text.setVisibility(View.VISIBLE);
                            bell_image.setVisibility(View.VISIBLE);
                        }
                       /* if (onItemClicked) {
                            if (count_text.getVisibility() == View.VISIBLE && bell_image.getVisibility() == View.VISIBLE) {
                                count_text.setVisibility(View.GONE);
                                bell_image.setVisibility(View.GONE);
                            }
                        } else {
                            blink(count_text, bell_image);
                        }*/
                        blink(count_text, bell_image);
                    }
                });
            }
        }).start();
        // }

    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    @Override
    public void handleProfileUpdate() {
        super.handleProfileUpdate();
        updateProfile(R.id.nameTextooredo, R.id.wallet_id, R.id.balance_id);
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

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(MainActivityNewFlow.this, android.Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissionForCamera(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivityNewFlow.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivityNewFlow.this, new String[]{android.Manifest.permission.CAMERA}, requestCode);
        }
    }

    public static class UserInfoUpdateHandler extends Handler {
        WeakReference<MainActivityNewFlow> reference = null;

        public UserInfoUpdateHandler(MainActivityNewFlow mainActivityOld) {
            reference = new WeakReference<MainActivityNewFlow>(mainActivityOld);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                MainActivityNewFlow mainActivityOld = reference.get();
                if (null != mainActivityOld) {
                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_response_trans_type().equalsIgnoreCase(TransType.USER_INFO_RESPONSE.name()) && response.getG_status() == 1) {
                        UserInfoResponse userInfoResponse = new Gson().fromJson((String) msg.obj, UserInfoResponse.class);
                        ((CoreApplication) mainActivityOld.getApplication()).setUserInfoResponse(userInfoResponse);
                        ((CoreApplication) mainActivityOld.getApplication()).getCustomerLoginRequestReponse().setWalletBalance(userInfoResponse.getBalance());
                        mainActivityOld.updateProfile(R.id.nameTextooredo, R.id.wallet_id, R.id.balance_id);
                    }
                }
            }
        }
    }

    private class MyAdapter extends BaseAdapter {
        private final List<Item> items = new ArrayList<Item>();

        private final LayoutInflater inflater;

//        @Override
//        public boolean isEnabled(int position)
//        {
//            if(position == 0 || position==4)
//                return false;
//            else
//                return true;
//        }

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);

            recharge_paybill = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.topupchanged);
            prepaid_cards = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pre_paidcard);
            mobilebill = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.mobilebillnew);
            help = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.help);


            //Wallet service old Sep 17
//            load_wallet = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.recharge_wallet);
//            invoice = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.invoice);
//            pay = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.qrcode);
//            sendmoney = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.send_money);


            //Wallet service new Sep 17
            load_wallet = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.load_wallet_grey);
            invoice = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.invoice_grey);
            pay = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.qr_code_grey);
            sendmoney = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.send_money_grey);

            //Enabled on Nov19
            txn_history = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.others);


            //store = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.store);

            // items.add(new Item(load_wallet, "Load  wallet"));
            //items.add(new Item(pay, "Pay"));

//            sendmoney = adjustedContrast(sendmoney,1200);
//            invoice = adjustedContrast(invoice,1200);

//            sendmoney = bright(sendmoney);
//            invoice = bright(invoice);


            items.add(new Item(sendmoney, getResources().getString(R.string.mainmenu_send_money)));
            if (moduleName.equalsIgnoreCase("utility bills")) {
                items.add(new Item(prepaid_cards, getResources().getString(R.string.mainmenu_utility_bills)));
            } else {
                items.add(new Item(prepaid_cards, getResources().getString(R.string.mainmenu_prepaid_cards)));
            }
            items.add(new Item(mobilebill, getResources().getString(R.string.mainmenu_mobile_billl)));
            items.add(new Item(recharge_paybill, getResources().getString(R.string.mainmenu_intl_top_up)));
            items.add(new Item(invoice, getResources().getString(R.string.mainmenu_invoice)));
            items.add(new Item(where_to_pay, getResources().getString(R.string.mainmenu_where_to_pay)));
            items.add(new Item(my_offers, getResources().getString(R.string.mainmenu_offers)));
            //items.add(new Item(store, "STORE"));
            items.add(new Item(txn_history, getResources().getString(R.string.mainmenu_txn_history)));
            items.add(new Item(help, getResources().getString(R.string.mainmenu_help)));


        }

        public Bitmap bright(Bitmap bmp) {
            Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int p = bmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);

                    r = 100 + r;
                    g = 100 + g;
                    b = 100 + b;
                    alpha = 100 + alpha;
                    operation.setPixel(i, j, Color.argb(alpha, r, g, b));
                }
            }
            return operation;
        }

        public Bitmap gama(Bitmap bmp) {
            Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int p = bmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);

                    r = r + 150;
                    g = 0;
                    b = 0;
                    alpha = 0;
                    operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                }
            }
            return operation;
        }


        public Bitmap gray(Bitmap bmp) {
            Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            double red = 0.33;
            double green = 0.59;
            double blue = 0.11;

            for (int i = 0; i < bmp.getWidth(); i++) {
                for (int j = 0; j < bmp.getHeight(); j++) {
                    int p = bmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);

                    r = (int) red * r;
                    g = (int) green * g;
                    b = (int) blue * b;
                    operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
                }
            }
            return operation;
        }

        private Bitmap adjustedContrast(Bitmap src, double value) {
            // image size
            int width = src.getWidth();
            int height = src.getHeight();
            // create output bitmap

            // create a mutable empty bitmap
            Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

            // create a canvas so that we can draw the bmOut Bitmap from source bitmap
            Canvas c = new Canvas();
            c.setBitmap(bmOut);

            // draw bitmap to bmOut from src bitmap so we can modify it
            c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


            // color information
            int A, R, G, B;
            int pixel;
            // get contrast value
            double contrast = Math.pow((100 + value) / 100, 2);

            // scan through all pixels
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    // get pixel color
                    pixel = src.getPixel(x, y);
                    A = Color.alpha(pixel);
                    // apply filter contrast for every channel R, G, B
                    R = Color.red(pixel);
                    R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (R < 0) {
                        R = 0;
                    } else if (R > 255) {
                        R = 255;
                    }

                    G = Color.green(pixel);
                    G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (G < 0) {
                        G = 0;
                    } else if (G > 255) {
                        G = 255;
                    }

                    B = Color.blue(pixel);
                    B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                    if (B < 0) {
                        B = 0;
                    } else if (B > 255) {
                        B = 255;
                    }

                    // set new pixel color to output bitmap
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }
            return bmOut;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ImageView bell_icon_image;
            TextView name, count_text;
            ImageView picture;
            if (v == null) {
                v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.item_text, v.findViewById(R.id.item_text));
                v.setTag(R.id.count_text, v.findViewById(R.id.count_text));
                v.setTag(R.id.bell_icon_image, v.findViewById(R.id.bell_icon_image));

            }
            picture = (ImageView) v.getTag(R.id.picture);
            name = (TextView) v.getTag(R.id.item_text);
            count_text = (TextView) v.getTag(R.id.count_text);
            count_text.setBackground(null);
            bell_icon_image = (ImageView) v.getTag(R.id.bell_icon_image);

            Item item = (Item) getItem(i);
            /*if (items.get(i).name.equalsIgnoreCase("invoice")) {
                if (application.getInvoices_count() != 0) {
                    count_text.setVisibility(View.VISIBLE);
                    bell_icon_image.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(count_text.getText().toString()) < application.getInvoices_count()) {
                        isSound = true;
                        letsspeek();
                    }
                    count_text.setText("" + application.getInvoices_count());
                    blink(count_text, bell_icon_image);
                }
            } else {
                count_text.setVisibility(View.GONE);
                bell_icon_image.setVisibility(View.GONE);
            }*/

            picture.setImageBitmap(item.drawableId);
            name.setText(item.name);
            // if (name.getText().toString().equalsIgnoreCase("invoice")) {
            //newly addeded
            if (i == 4) {

                if (application.getInvoices_count() != 0) {
                    count_text.setVisibility(View.VISIBLE);
                    bell_icon_image.setVisibility(View.VISIBLE);
                    if (isSound) {
                        isSound = true;
                        letsspeek();
                    }
                    /*} else if (application.getInvoices_count() > invoice_count) {
                        isSound = true;
                        letsspeek();
                    }*/
                    /*} else if (isSound) {
                        isSound = true;
                        letsspeek();
                    }*/
                    else {
                        isSound = false;
                    }
                    count_text.setText("" + application.getInvoices_count());
                    /*if (notifyDataSetChangedCalled) {
                        mBlinking = true;
                        blink(count_text, bell_icon_image);
                    } else {
                        blink(count_text, bell_icon_image);
                    }*/
                    //old time using blink
                    //blink(count_text, bell_icon_image);

                    //new blink using animation
                    count_text.setVisibility(View.VISIBLE);
                    bell_icon_image.setVisibility(View.VISIBLE);
                    count_text.setBackground(getResources().getDrawable(R.drawable.invoice_circle));
                    count_text.startAnimation(animBlink);
                    bell_icon_image.startAnimation(animBlink);
                }
            } else {
                /*count_text.setVisibility(View.GONE);
                bell_icon_image.setVisibility(View.GONE);*/
                count_text.setVisibility(View.GONE);
                bell_icon_image.setVisibility(View.GONE);
                count_text.clearAnimation();
                bell_icon_image.clearAnimation();
            }
            return v;
        }

        private class Item {
            String name;
            Bitmap drawableId;

            Item(Bitmap drawableId, String title) {
                this.name = title;
                this.drawableId = drawableId;
            }

            public Bitmap getImage() {
                return drawableId;
            }

            public void setImage(Bitmap image) {
                this.drawableId = image;
            }
        }
    }

    class Item {
        Bitmap image;
        String title;

        public Item(Bitmap image, String title) {
            super();
            this.image = image;
            this.title = title;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    public class VersionChecker extends AsyncTask<String, String, String> {
        private String newVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        showIfNotVisible("");

        }

        @Override
        protected void onPostExecute(String latestVersion) {
//        hideIfVisible();

            Log.e("Version", "" + latestVersion);


            if (latestVersion != null && !latestVersion.isEmpty()) {
                double live_version = Double.parseDouble(latestVersion);
                double local_version = Double.parseDouble(versionname);
                Log.e("local_version", "" + local_version);
                if (local_version < live_version) {
                    LayoutInflater li = LayoutInflater.from(MainActivityNewFlow.this);
                    View promptsView = li.inflate(R.layout.custom_update_playstore_dialog, null);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityNewFlow.this);
                    alertDialog.setView(promptsView);
                    alertDialog.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });

                    alertDialog.setNegativeButton(getResources().getString(R.string.no_newflow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });


                    alertDialog.setCancelable(false);
                    alertDialog.show();

                } else {
//                check_for_updates_up_to_date.setText(getResources().getString(R.string.check_up_to_date));
//                check_for_updates_version_no.setText(getResources().getString(R.string.current_version) + versionname);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //new way to get version number
               /* newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "trai.gov.in.dnd" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
                        .get(0)
                        .ownText();*/

                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en").timeout(30000).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("http://www.google.com").get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }


    }


}
