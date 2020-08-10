package com.bookeey.wallet.live.mainmenu;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.application.SyncService;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.offers.NewOffersActivityAfterLogin;
import com.bookeey.wallet.live.recharge.TopUpInitialActivity;
import com.bookeey.wallet.live.txnhistory.TransactionHistoryActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.com.google.zxing.client.android.CaptureActivity;
import br.com.google.zxing.client.android.Intents;
import coreframework.barcodeclient.BarCodeTimeParser;
import coreframework.barcodeclient.BcodeHeaderEncoder;
import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.GetPushNotificationMessageProcessing;
import coreframework.processing.LoadMoneyProcessing;
import coreframework.processing.Login_processing.CustomerAutoLoginProcessingFromNFC;
import coreframework.processing.PayToMerchantValidationProcessing;
import coreframework.processing.bweb_processing.BWebProcessing;
import coreframework.processing.invoice_Processing.InvoiceProcessing;
import coreframework.processing.invoice_Processing.InvoiceProcessingForSessionOut;
import coreframework.processing.mobile.MobileBillProcessing;
import coreframework.processing.prepaidcard.PrepaidCardProcessing;
import coreframework.securityutils.SecurityUtils;
import coreframework.taskframework.DecimalDigitsInputFilter;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.Hex;
import coreframework.utils.LocaleHelper;
import coreframework.utils.PriceFormatter;
import coreframework.utils.SMSUtils;

import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.sendmoney.SendMoneyLeg1RequestActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import coreframework.utils.URLUTF8Encoder;
import merchant.DecodedQrPojo;
import merchant.DisplayAmountToMerchantActivityDummy;
import merchant.PayToMerchantStaticQRCodeInitProcessing;
import wheretopaynew.MerchantListCatogorieyActivityNewUIActivity;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.getpushnotificationmessage.GetPushNotificationMessageRequest;
import ycash.wallet.json.pojo.loadmoney.PaymentForm;
import ycash.wallet.json.pojo.loadmoney.WalletLimits;
import ycash.wallet.json.pojo.login.CustomerLoginRequest;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.MobileBillOperatorsRequest;
import ycash.wallet.json.pojo.paytomerchant.P2MBarcodeGenInitValidationRequest;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequest;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;
import ycash.wallet.json.pojo.userinfo.UserInfoResponse;

import static coreframework.database.CustomSharedPreferences.SP_KEY.MOBILE_NUMBER;


public class MainActivity extends GenericActivity implements YPCHeadlessCallback, TextToSpeech.OnInitListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextToSpeech tts;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Intent intent;
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    Bitmap load_wallet, invoice, pay, recharge_paybill, sendmoney, help, where_to_pay, my_offers, mobilebill, prepaid_cards, txn_history, store, testimage;
    TextView more_text;
    ExpandableHeightGridView mainmenu_gridview;
    //for 3 columns use below grdiview and setExpanded(false)
    //GridView  mainmenu_gridview;
    ScrollView scroll;
    //int i = 0;
    static public final int _SLIDE_ECOM = 9;
    //ImageView more_img1;
    private UserInfoUpdateHandler userInfoUpdateHandler;
    private String amount_str = null;
    MyAdapter adapter1;
    ImageView imageview;
    WalletLimits walletLimits;
    String amount, tpin;
    private DrawerLayout mDrawer = null;
    private Uri mImageCaptureUri;
    // private static final int PICK_FROM_GALLERY = 2;
    ImageView image_person;
    ProgressDialog progressDialog;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PICK_FROM_GALLERY = 2;

    CoreApplication application = null;
    CustomerLoginRequestReponse customerLoginRequestReponse = null;
    boolean onItemClicked = false, isSound = false;
    int i = 0;
    ObjectAnimator textColorAnim, ImageColorAnim;
    boolean mBlinking = true;
    boolean notifyDataSetChangedCalled = false;
    int invoice_count = 0;
    Animation animBlink, animRight, animLeft;
    Button bookeey_mainmenu_loadwallet_btn, bookeey_mainmenu_pay_btn;
    String bannerString = "";
    List<String> bannerList = new ArrayList<>();
    int right_count = 0;
    int left_count = 0;
    Handler handler2 = null;

    boolean isScrolled = true;

    //Initializing the GoogleApiClient object
    private GoogleApiClient googleApiClient;

    public static final int LOCATION_REQUEST = 101;

    public static Context context;

    String versionname;

    private FirebaseAnalytics firebaseAnalytics;


    //Voiece

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts_for_voice;
    private SpeechRecognizer speechRecog;


    public static final String KEY_FROM_LOGIN = "KEY_FROM_LOGIN";

    public static final String KEY_IS_AUTO_LOGIN_FROM_NFC = "KEY_IS_AUTO_LOGIN_FROM_NFC";

    public static final int STATIC_QR_CODE_REQUEST = 2442;

    public boolean should_call_session_time_out_from_onResume = true;


    //For NFC logic
    private boolean session_expired = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getApplicationContext(), this);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        animLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_left);
        animRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_in_right);
        /*if (getIntent().getExtras() != null) {
            isSound = getIntent().getExtras().getBoolean("voice");
        }*/
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main_new_framelayout);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.activity_main_actionbar, null);
        getActionBar().setCustomView(cView, params);*/
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
        // mActionBar.setDisplayHomeAsUpEnabled(false);
        //getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        //getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_mainmenu, null);
        //mActionBar.setDisplayShowCustomEnabled(true);


        //Showing Notification count
        TextView count_text_top = (TextView) mCustomView.findViewById(R.id.count_text_top);
        String notification_count  = CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.NOTIFICATION_MSG_COUNT);
        count_text_top.setText(notification_count);

        if(notification_count.length()>0) {
            count_text_top.setText(""+notification_count);
        }else{
            count_text_top.setText("0");
        }



        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        mActionBar.setCustomView(mCustomView, params);
        final TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.marquee_text);
        final ImageView leftArrow = (ImageView) mCustomView.findViewById(R.id.left);
        final ImageView rightArrow = (ImageView) mCustomView.findViewById(R.id.right);



//        ImageView  main_refresh  = (ImageView)findViewById(R.id.main_refresh) ;
//
//        main_refresh.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                refreshUserInfo(true);
//
//            }
//        });

        //ImageView imageView=(ImageView)mCustomView.findViewById(R.id.home_up_back);
        //imageView.setVisibility(View.GONE);
        //mTitleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //mTitleTextView.setText(bannerString);
        mTitleTextView.setGravity(Gravity.CENTER);
        //mTitleTextView.setSelected(true);
        mTitleTextView.setSingleLine(true);

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

        progressDialog = new ProgressDialog(MainActivity.this, R.style.MyTheme2);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        load_wallet = BitmapFactory.decodeResource(this.getResources(), R.drawable.recharge_wallet);
        invoice = BitmapFactory.decodeResource(this.getResources(), R.drawable.invoice);

        pay = BitmapFactory.decodeResource(this.getResources(), R.drawable.qrcode);

        sendmoney = BitmapFactory.decodeResource(this.getResources(), R.drawable.send_money);
        where_to_pay = BitmapFactory.decodeResource(this.getResources(), R.drawable.where_to_pay_icon);

        recharge_paybill = BitmapFactory.decodeResource(this.getResources(), R.drawable.topupchanged);
        mobilebill = BitmapFactory.decodeResource(this.getResources(), R.drawable.mobilebillnew);

        prepaid_cards = BitmapFactory.decodeResource(this.getResources(), R.drawable.pre_paidcard);
        my_offers = BitmapFactory.decodeResource(this.getResources(), R.drawable.offer);

        help = BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
        txn_history = BitmapFactory.decodeResource(this.getResources(), R.drawable.others);
        bookeey_mainmenu_loadwallet_btn = (Button) findViewById(R.id.bookeey_mainmenu_loadwallet_btn);
        bookeey_mainmenu_pay_btn = (Button) findViewById(R.id.bookeey_mainmenu_pay_btn);
        more_text = (TextView) findViewById(R.id.more_text);
        scroll = (ScrollView) findViewById(R.id.scroll);
        // more_img1 = (ImageView) findViewById(R.id.more_img1);


        image_person = (ImageView) findViewById(R.id.image_person);

//        Commented for CreditCard view Feb 11

//        image_person.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog();
//            }
//        });

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
        //more_img1.setBackgroundResource(R.drawable.down_arrow);
        /*gridArray.add(new Item(load_wallet, "LOAD WALLET"));
        gridArray.add(new Item(invoice, "INVOICE"));
        gridArray.add(new Item(pay, "PAY"));
        gridArray.add(new Item(sendmoney, "SEND MONEY"));
        gridArray.add(new Item(where_to_pay, "WHERE TO PAY"));
        gridArray.add(new Item(recharge_paybill, "INT'L TOP UP"));
        gridArray.add(new Item(mobilebill, "MOBILE BILL"));
        gridArray.add(new Item(prepaid_cards, "VIRTUAL PREPAID CARDS"));
        gridArray.add(new Item(my_offers, "MERCHANT OFFERS"));
        gridArray.add(new Item(help, "HELP"));
        gridArray.add(new Item(txn_history, "TRANSACTION HISTORY"));*/

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.

//                pressMic();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });


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


        //Commented for creditcard view START


        bookeey_mainmenu_loadwallet_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoneyCheck();
            }
        });


        bookeey_mainmenu_pay_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


//                showAmountEntryPayDialogue();

//                CX <->  MX


//                    Original code
//                Intent intent;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                    boolean isEnabled = checkCameraPermissionLatest();
//                    if (isEnabled) {
//                        intent = new Intent(getBaseContext(), CaptureActivity.class);
//                        intent.setAction("br.com.google.zxing.client.android.SCAN");
//                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                        intent.putExtra("CHARACTER_SET", "ISO-8859-1");
//                        startActivityForResult(intent, 1111);
//                    }
//                } else {
//                    intent = new Intent(getBaseContext(), CaptureActivity.class);
//                    intent.setAction("br.com.google.zxing.client.android.SCAN");
//                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                    intent.putExtra("CHARACTER_SET", "ISO-8859-1");
//                    startActivityForResult(intent, 1111);
//                }


//                showScanTypeAlertDialogue();


                //Mar 15
                showAmountEntryPayDialogueWithTwoPayOptions();

            }
        });
         //Commented for creditcard view END



        mainmenu_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> view, View v, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, SendMoneyLeg1RequestActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        prepaidCardList();
                        break;
                    case 2:
                        mobileBill();
                        break;
                    case 3:
                        intent = new Intent(getBaseContext(), TopUpInitialActivity.class);
                        intent.putExtra("BACK", "");
                        startActivity(intent);
                        break;
                    case 4:
                        onItemClicked = true;
                        invoice();

//                      bWeb();

                        break;
                    case 5:


//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            boolean isEnabled = checkLocationPermission();
//                            if (isEnabled) {
//                                updateSlidingPanel(_SLIDE_ECOM);
//                            }
//                        } else {
//                            updateSlidingPanel(_SLIDE_ECOM);
//                        }


                        //New where to pay list

                        Intent intent = new Intent(MainActivity.this, MerchantListCatogorieyActivityNewUIActivity.class);
                        startActivity(intent);

                        break;
                    case 6:
//                        intent = new Intent(getBaseContext(), NewOffersActivity.class);
//                        startActivity(intent);

                        intent = new Intent(getBaseContext(), NewOffersActivityAfterLogin.class);
                        startActivity(intent);

                        break;
                    case 7:
                        Intent serviceIntent = new Intent(getBaseContext(), SyncService.class);
                        serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
                        startService(serviceIntent);
                        ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                        Intent i = new Intent(getBaseContext(), TransactionHistoryActivity.class);
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
        updateUserInfo(userInfoUpdateHandler);


        //For location
        //Building a instance of Google Api Client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();


//        enableVoice(true);




        //Rahman //Check for updates

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception e) {
            Log.e("TAG1", "" + e.toString());
        }
        int versionCode = BuildConfig.VERSION_CODE;
        versionname = BuildConfig.VERSION_NAME;

        VersionChecker versionChecker = new VersionChecker();
        versionChecker.execute();




        //Invoke push notification messages
        FrameLayout frame_layout =  (FrameLayout)findViewById(R.id.push_notifications_frame_layout);


//        BadgeView badge = new BadgeView(this, push_notification_message_bell);
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setBackgroundResource(R.drawable.bookeey_small);
//        badge.setText("1");
//        badge.show();


        frame_layout.setOnClickListener(new View.OnClickListener() {
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


                    Intent intent = new Intent(MainActivity.this, ShowPushNotificationMessageDialogActivity.class);
                    intent.putExtra(ShowPushNotificationMessageDialogActivity.KEY_PUSH_NOTIFICATION_MESSAGES,finalJsonObject.toString());
                    startActivity(intent);



                } catch (Exception e) {

                    Log.e("Push Notifica Msg Ex:", "" + e.getMessage());

                    Toast toast = Toast.makeText(MainActivity.this, "System Error! "+e.getMessage(), Toast.LENGTH_SHORT);
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
//        viewPager.setAdapter(new ViewPagerAdapter(fm));


    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {

            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);


            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

//                    Toast.makeText(MainActivity.this,result_arr.get(0),Toast.LENGTH_LONG).show();

                    processResult(result_arr.get(0));


                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();

        Log.e("processResult", "" + result_message);

        Toast.makeText(MainActivity.this, result_message, Toast.LENGTH_LONG).show();
        Toast.makeText(MainActivity.this, result_message, Toast.LENGTH_LONG).show();


        /*

//       if (result_message.indexOf("pay") != -1||result_message.indexOf("p") != -1||result_message.matches(".*\\d.*")){
        if (result_message.matches(".*\\d.*")) {
//            speak("Opening Pay screen, please enter amount.");


            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(result_message);
            while (m.find()) {

//               Toast.makeText(MainActivity.this,"->"+m.group(),Toast.LENGTH_LONG).show();


                if(result_message.contains("pills")||result_message.contains("film")||result_message.contains("fails")||result_message.contains("fields")) {

                    Log.e("processResult m.group()", "" + m.group());
                    double amount = Double.parseDouble(m.group());
                    Log.e("processResult int", "" + amount);
                    double fills_amout = amount/1000;
                    payViaQrCodeProcess(Double.toString(fills_amout));

                    Log.e("processResult fills", "" + Double.toString(fills_amout));

                }else{

                    Log.e("processResult else", "" + m.group());

                    int amount = Integer.parseInt(m.group());

                    if (amount <= 15) {
                        payViaQrCodeProcess(m.group());
                    } else {
                        speak("Please say amount less than 16");

                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {

                        }

                        pressMic();
                    }

                }

            }

        }else {
            speak("Please say again");
            try {
                Thread.sleep(2000);
            }catch(Exception e){

            }

            pressMic();
        }
*/


        if (result_message.indexOf("load") != -1 || result_message.indexOf("wallet") != -1) {
            speak("Opening load money screen");
            loadMoneyCheck();

        } else if (result_message.indexOf("where to pay") != -1 || result_message.indexOf("where to be") != -1 || result_message.indexOf("where to buy") != -1 || result_message.indexOf("way2way") != -1 || result_message.indexOf("where the play") != -1 || result_message.indexOf("white topi") != -1 || result_message.indexOf("y2k") != -1 || result_message.indexOf("wwe 2k") != -1) {
            speak("Opening merchant categories");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean isEnabled = checkLocationPermission();
                if (isEnabled) {
                    updateSlidingPanel(_SLIDE_ECOM);
                }
            } else {
                updateSlidingPanel(_SLIDE_ECOM);
            }

        } else if (result_message.indexOf("pay") != -1 || result_message.indexOf("de") != -1 || result_message.indexOf("bae") != -1 || result_message.indexOf("baby") != -1 || result_message.indexOf("hay") != -1 || result_message.indexOf("fairy") != -1) {
            speak("Opening Pay screen");
            showAmountEntryPayDialogue();

        } else if (result_message.indexOf("send") != -1 || result_message.indexOf("money") != -1) {
            speak("Opening Send Money");

            killSomeTime();

            intent = new Intent(MainActivity.this, SendMoneyLeg1RequestActivity.class);
            startActivity(intent);

        } else if (result_message.indexOf("prepaid") != -1 || result_message.indexOf("virtual") != -1 || result_message.indexOf("cards") != -1 || result_message.indexOf("chords") != -1) {
            speak("Opening Prepaid virtual cards");
            killSomeTime();
            prepaidCardList();

        } else if (result_message.indexOf("mobile") != -1 || result_message.indexOf("bill") != -1 || result_message.indexOf("bil") != -1) {
            speak("Opening Mobile Bill");
            killSomeTime();
            mobileBill();

        } else if (result_message.indexOf("international") != -1 || result_message.indexOf("top") != -1 || result_message.indexOf("top up") != -1 || result_message.indexOf("toupee") != -1 || result_message.indexOf("national") != -1) {
            speak("Opening International topup");

            killSomeTime();

            intent = new Intent(getBaseContext(), TopUpInitialActivity.class);
            intent.putExtra("BACK", "");
            startActivity(intent);

        } else if (result_message.indexOf("invoice") != -1 || result_message.indexOf("in force") != -1 || result_message.indexOf("voice") != -1) {
            speak("Opening Invoices");
            killSomeTime();
            onItemClicked = true;
            invoice();

        } else if (result_message.indexOf("offers") != -1 || result_message.indexOf("loafers") != -1 || result_message.indexOf("grofers") != -1 || result_message.indexOf("office") != -1 || result_message.indexOf("opus") != -1) {
            speak("Opening Offers");

            killSomeTime();

//            intent = new Intent(getBaseContext(), NewOffersActivity.class);
//            startActivity(intent);

            intent = new Intent(getBaseContext(), NewOffersActivityAfterLogin.class);
            startActivity(intent);

        } else if (result_message.indexOf("transaction") != -1 || result_message.indexOf("history") != -1) {
            speak("Opening transaction history");
            killSomeTime();
            Intent serviceIntent = new Intent(getBaseContext(), SyncService.class);
            serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
            startService(serviceIntent);
            ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
            Intent i = new Intent(getBaseContext(), TransactionHistoryActivity.class);
            startActivity(i);

        } else if (result_message.indexOf("help") != -1 || result_message.indexOf("hug") != -1) {
            speak("Opening Help");
            killSomeTime();
            intent = new Intent(getBaseContext(), Help.class);
            startActivity(intent);

        } else {

            speak("Please say again");

            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }

            pressMic();
        }


    }

    public void killSomeTime() {


        try {
            Thread.sleep(1500);
        } catch (Exception e) {

        }

    }


    public void showVoiceIntroLayout() {


//        try {
//            Thread.sleep(2000);
//        }catch(Exception e){
//
//        }


//        Intent intent = new Intent(getBaseContext(), VoiceCommndsDialogActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.alert_dialog_anim_from_bottom,R.anim.alert_dialog_anim_from_top);

        final LinearLayout main_menu_whole_layout = (LinearLayout) findViewById(R.id.main_menu_whole_layout);
        final LinearLayout voice_command_layout = (LinearLayout) findViewById(R.id.voice_command_layout);

        ImageView close_button = (ImageView) findViewById(R.id.close_button);

        close_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                CustomSharedPreferences.saveBooleanData(MainActivity.this, false, CustomSharedPreferences.SP_KEY.COMING_FROM_LOGIN);

                // slide-down animation
                Animation slide_down = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alert_dialog_anim_from_top);

                if (voice_command_layout.getVisibility() == View.VISIBLE) {
                    voice_command_layout.setVisibility(View.GONE);
                    voice_command_layout.startAnimation(slide_down);
                    main_menu_whole_layout.setEnabled(true);

                    pressMic();
                }
            }
        });

        // slide-up animation
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.alert_dialog_anim_from_bottom);

        if (voice_command_layout.getVisibility() == View.GONE || voice_command_layout.getVisibility() == View.INVISIBLE) {
            voice_command_layout.setVisibility(View.VISIBLE);
            voice_command_layout.startAnimation(slideUp);


            main_menu_whole_layout.setEnabled(false);
        }


    }

    public void enableVoice(boolean fromOncreate) {

        /*Commented for SARA video

//        Toast.makeText(MainActivity.this,"enableVoice",Toast.LENGTH_LONG).show();

        //Voice
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            initializeTextToSpeech();
            initializeSpeechRecognizer();
        }else {
            if (fromOncreate){

//                Toast.makeText(MainActivity.this, "Speech recognition not available!", Toast.LENGTH_LONG).show();
        }

        }


         */


    }


    public void showVoiceIntroDialog() {
        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.mainmenu_voice_intro_alert);

        final ImageView close_button = (ImageView) promptsView.findViewById(R.id.close_button);


        close_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();


            }
        });

        Window window = promptsView.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);
        promptsView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        promptsView.getWindow().getAttributes().windowAnimations = R.style.voice_alert_dialog_animation_style;

        promptsView.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                pressMic();
            }
        });


        promptsView.show();
    }

    private void initializeTextToSpeech() {
        tts_for_voice = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts_for_voice.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.tts_no_engines), Toast.LENGTH_LONG).show();
//                    finish();
                } else {
                    tts_for_voice.setLanguage(Locale.US);
//                    speak("Hello there, I am ready to start our conversation");

                    //Commented for Rawan & Bita testing
                    boolean coming_from_login = CustomSharedPreferences.getBooleanData(MainActivity.this, CustomSharedPreferences.SP_KEY.COMING_FROM_LOGIN);

                    if (coming_from_login) {

                    } else {
                        pressMic();
                    }
                }
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            tts_for_voice.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts_for_voice.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void pressMic() {


        if (SpeechRecognizer.isRecognitionAvailable(this)) {

        /*
        //Commented to install in POS device on Oct 16
        */

            try {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.RECORD_AUDIO)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    // Permission has already been granted
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    speechRecog.startListening(intent);
                }

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Recognize Speech Error!\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
                    if (bannerList.size() > 0)
                        mTitleTextView.setText(bannerList.get(i));
                    i++;
                    if (i == bannerList.size())
                        i = 0;
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

    private void prepaidCardList() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        MobileBillOperatorsRequest mobileBillOperatorsRequest = new MobileBillOperatorsRequest();
        String uiProcessorReference = application.addUserInterfaceProcessor(new PrepaidCardProcessing(mobileBillOperatorsRequest, application, true));
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
        String uiProcessorReference = application.addUserInterfaceProcessor(new MobileBillProcessing(mobileBillOperatorsRequest, application, true));
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


    private void bWeb() {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        //CustomSharedPreferences.saveStringData(getApplicationContext(), mobile_number, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        String mobno = CustomSharedPreferences.getStringData(getApplicationContext(), MOBILE_NUMBER);
        //InvoiceDetailsPojo invoiceRequest = new InvoiceDetailsPojo();
        // invoiceRequest.setVersionNumber(customerLoginRequestReponse.getDomesticRechargeVersion());
        //invoiceRequest.setMobileNumber(mobno);
        GenericRequest invoiceRequest = new GenericRequest();
        invoiceRequest.setG_userId(mobno);
        String uiProcessorReference = application.addUserInterfaceProcessor(new BWebProcessing(invoiceRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    private void invoiceForSessionOut() {

//        Toast.makeText(MainActivity.this,"InvoiceForSessionOut invoked!",Toast.LENGTH_LONG).show();

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

        if (tts_for_voice != null) {
            tts_for_voice.shutdown();
        }

    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        //Commented on Jan 28 after suffling bug fixed
//        deleteCache(getApplicationContext());


        //To identify session out July 02 started
//        invoiceForSessionOut();


//        Toast.makeText(MainActivity.this,"onResume",Toast.LENGTH_LONG).show();


        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Main menu");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 7);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Main menu");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 7 logged");


        context = getApplicationContext();


        Log.e("onResume Kill1", "MainActivity " + application.getCustomerLoginRequestReponse());
        CustomerLoginRequestReponse response = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();

        Log.e("onResume Kill2", "Limits: " + application.getCustomerLoginRequestReponse().getFilteredLimits());

        if (response.getFilteredLimits() == null) {

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finishAffinity();
        }


        //Show voice commands Intro
//        showVoiceIntroDialog();

        boolean coming_from_login = CustomSharedPreferences.getBooleanData(MainActivity.this, CustomSharedPreferences.SP_KEY.COMING_FROM_LOGIN);

        if (coming_from_login) {
//            Toast.makeText(MainActivity.this,"From login",Toast.LENGTH_LONG).show();

            //Show voice commands Layout
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                //Sara
//                showVoiceIntroLayout();
            }
        }


        //NFC
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {

//                boolean session_expired = CustomSharedPreferences.getBooleanData(MainActivity.this, CustomSharedPreferences.SP_KEY.SESSION_EXPIRED);
//
//                if (!session_expired) {
//
//                    showAmountEntryPayDialogue();
//
//                } else {
//                    processNFCData(getIntent());
//                }


            processNFCData(getIntent());
        } else {


            //To identify session out July 02 started
            if (should_call_session_time_out_from_onResume) {
//                invoiceForSessionOut();
                new InvoiceSessionTimeOut().execute();
            }


            //        Reinitialize the recognizer and tts engines upon resuming from background such as after openning the browser

            //Sara
//                enableVoice(false);
        }


        //Came from auto login va NFC
        boolean is_auto_login_from_nfc = CustomSharedPreferences.getBooleanData(MainActivity.this, CustomSharedPreferences.SP_KEY.KEY_IS_AUTO_LOGIN_FROM_NFC);
        if (is_auto_login_from_nfc) {

            CustomSharedPreferences.saveBooleanData(MainActivity.this, false, CustomSharedPreferences.SP_KEY.KEY_IS_AUTO_LOGIN_FROM_NFC);
            showAmountEntryPayDialogue();

        }




    }


    private void processNFCData(Intent inputIntent) {

        Log.i("MainMenu", "processNFCData");
        Parcelable[] rawMessages =
                inputIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (rawMessages != null && rawMessages.length > 0) {

            NdefMessage[] messages = new NdefMessage[rawMessages.length];

            for (int i = 0; i < rawMessages.length; i++) {

                messages[i] = (NdefMessage) rawMessages[i];

            }

            Log.i("MainMenu", "message size = " + messages.length);


            // only one message sent during the beam
            NdefMessage msg = (NdefMessage) rawMessages[0];
            // record 0 contains the MIME type, record 1 is the AAR, if present
            String base = new String(msg.getRecords()[0].getPayload());
//                String str = String.format(Locale.getDefault(), "Message entries=%d. Base message is %s", rawMessages.length, base);


            if (base.contains("-")) {

                String[] str_array = base.split("-");
                String mobile_number = str_array[0];
                String password = str_array[1];


//                        Toast.makeText(MainActivity.this,"NFC Message:  "+mobile_number+" "+password,Toast.LENGTH_LONG).show();

                CustomerLoginRequest clr = new CustomerLoginRequest();
                clr.setLogin_pin(password);
                CustomSharedPreferences.saveStringData(getApplicationContext(), password, CustomSharedPreferences.SP_KEY.PIN);
                String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();

                Log.e("deviceID", "->" + deviceID);
                Log.e("deviceID", "->" + deviceID);


//            String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);

                String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);

                clr.setMobileNumber(mobile_number);
                clr.setDeviceIdNumber(deviceID);
                if (selectedLanguage.equals("en")) {
                    clr.setLanguage("english");
                } else if (selectedLanguage.equals("ar")) {
                    clr.setLanguage("arabic");
                } else {
                    clr.setLanguage("english");
                }
                CoreApplication application = (CoreApplication) getApplication();
                String uiProcessorReference = application.addUserInterfaceProcessor(new CustomerAutoLoginProcessingFromNFC(clr, true, application, true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle = new Bundle();
                bundle.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle);
                progress.show(getSupportFragmentManager(), "progress_dialog");

            } else {

                Toast.makeText(MainActivity.this, "System Error!", Toast.LENGTH_LONG).show();
            }

        }
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

        Log.e("MainActivity", "onDestroy");
        CustomSharedPreferences.saveBooleanData(MainActivity.this, true, CustomSharedPreferences.SP_KEY.SESSION_EXPIRED);
    }


    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                speakOut();

                //Comment this once you enable voice commands

                //Displed based on request July 2020
//                letsspeek();

            }

        } else {
            Log.e("TTS", "Initilization Failed!");
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
    }

    public void onStop() {
        super.onStop();
        //Disconnecting the GoogleApiClient when the activity goes invisible
        googleApiClient.disconnect();
    }

    //This callback is invoked when the user grants or rejects the location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT);
                break;
        }
    }

    private void getCurrentLocation() {
        //Checking if the location permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_REQUEST);
            return;
        }
        //Fetching location using FusedLOcationProviderAPI
        FusedLocationProviderApi fusedLocationApi = LocationServices.FusedLocationApi;
        Location location = fusedLocationApi.getLastLocation(googleApiClient);
        //In some rare cases Location obtained can be null
        if (location == null)
            Log.e("Location: ", "Not able to fetch location");
        else {
            Log.e("Location: ", "" + location.getLatitude() + " - " + location.getLongitude());

            CustomSharedPreferences.saveStringData(getApplicationContext(), String.valueOf(location.getLatitude()), CustomSharedPreferences.SP_KEY.CURRENT_LATITUTE);
            CustomSharedPreferences.saveStringData(getApplicationContext(), String.valueOf(location.getLongitude()), CustomSharedPreferences.SP_KEY.CURRENT_LONGITUDE);
        }


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

    public static class UserInfoUpdateHandler extends Handler {
        WeakReference<MainActivity> reference = null;

        public UserInfoUpdateHandler(MainActivity mainActivityOld) {
            reference = new WeakReference<MainActivity>(mainActivityOld);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                MainActivity mainActivityOld = reference.get();
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

    void showLoadMoneyNeutralDailog() {


        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Load wallet - enter load amount page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 4);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Load wallet - enter load amount page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 4 logged");


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
//                pressMic();
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


    void showScanTypeAlertDialogue() {


        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.scan_type_alert);

        final Button btn_static_qr_code = (Button) promptsView.findViewById(R.id.btn_static_qr_code);
        final Button btn_merchant_qr_code = (Button) promptsView.findViewById(R.id.btn_merchant_qr_code);

        final TextView btn_scan_type_close = (TextView) promptsView.findViewById(R.id.btn_scan_type_close);


        btn_static_qr_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();
                showAmountEntryPayDialogueForStaticQRCode();


            }
        });
        btn_merchant_qr_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

//                Toast.makeText(MainActivity.this, "Need to do", Toast.LENGTH_LONG).show();

                showAmountEntryPayDialogue();

            }
        });


        btn_scan_type_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();


            }
        });


        promptsView.show();
    }

    void showAmountEntryPayDialogueForStaticQRCode() {


        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.static_qr_code_enter_amount);
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
//                pressMic();
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

                promptsView.dismiss();


                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    boolean isEnabled = checkCameraPermissionLatest();
                    if (isEnabled) {
                        intent = new Intent(getBaseContext(), CaptureActivity.class);
                        intent.setAction("br.com.google.zxing.client.android.SCAN");
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        intent.putExtra("CHARACTER_SET", "ISO-8859-1");
                        startActivityForResult(intent, STATIC_QR_CODE_REQUEST);
                    }
                } else {
                    intent = new Intent(getBaseContext(), CaptureActivity.class);
                    intent.setAction("br.com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    intent.putExtra("CHARACTER_SET", "ISO-8859-1");
                    startActivityForResult(intent, STATIC_QR_CODE_REQUEST);
                }


            }
        });
        promptsView.show();
    }

    public void showAmountEntryPayDialogue() {


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
//                pressMic();
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



    public void showHelpAlertForScanMerchantQR(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
//        dialog.setTitle("Dialog on Android");
        dialog.setMessage(getString(R.string.scan_qr_help_content));
        dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public void showHelpAlertForGenerateQR(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setCancelable(false);
//        dialog.setTitle("Dialog on Android");
        dialog.setMessage(getString(R.string.generate_qr_help_content));
        dialog.setPositiveButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }


    public void showAmountEntryPayDialogueWithTwoPayOptions() {


        final Dialog promptsView = new Dialog(this);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        promptsView.setContentView(R.layout.amount_entry_dialog_with_two_pay_options);
        promptsView.setContentView(R.layout.amount_entry_dialog_with_two_pay_options_with_help_buttons);
        final EditText amountField = (EditText) promptsView.findViewById(R.id.ypc_getAmoint_getAmount);
        final CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final TransactionLimitResponse limits = customerLoginRequestReponse.getFilteredLimits().get("P2M");

        amountField.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 3, limits.getMaxValuePerTransaction().floatValue())});


        final EditText pay_via_qrcode_pin_edt = (EditText) promptsView.findViewById(R.id.pay_via_qrcode_pin_edt);
        final Button pay_qrcode_generatel_btn_new = (Button) promptsView.findViewById(R.id.pay_qrcode_generatel_btn_new);
        final Button pay_qrcode_pay_btn_new = (Button) promptsView.findViewById(R.id.pay_qrcode_pay_btn_new);
        final Button pay_qrcode_cancel_btn_new = (Button) promptsView.findViewById(R.id.pay_qrcode_cancel_btn_new);

        final ImageView scanQR_help = (ImageView) promptsView.findViewById(R.id.scanQR_help);
        final ImageView generateQR_help = (ImageView) promptsView.findViewById(R.id.generateQR_help);

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
//                pressMic();
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


        //Static QRCode process
        pay_qrcode_generatel_btn_new.setOnClickListener(new OnClickListener() {
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

                promptsView.dismiss();


                int height = getScreenWidth();
                int width = getScreenHeight();

                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    boolean isEnabled = checkCameraPermissionLatest();
                    if (isEnabled) {
                        intent = new Intent(getBaseContext(), CaptureActivity.class);
                        intent.setAction("br.com.google.zxing.client.android.SCAN");
                        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        intent.putExtra("CHARACTER_SET", "ISO-8859-1");
                        intent.putExtra(Intents.Scan.WIDTH,width);
                        intent.putExtra(Intents.Scan.HEIGHT,height);

                        startActivityForResult(intent, STATIC_QR_CODE_REQUEST);
                    }
                } else {
                    intent = new Intent(getBaseContext(), CaptureActivity.class);
                    intent.setAction("br.com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    intent.putExtra("CHARACTER_SET", "ISO-8859-1");
                    intent.putExtra(Intents.Scan.WIDTH,width);
                    intent.putExtra(Intents.Scan.HEIGHT,height);
                    startActivityForResult(intent, STATIC_QR_CODE_REQUEST);
                }


            }
        });


        scanQR_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showHelpAlertForScanMerchantQR();

            }
        });

        generateQR_help.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showHelpAlertForGenerateQR();

            }
        });






        promptsView.show();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateSlidingPanel(int slideType) {

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Merchant categories list page");

        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 19);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Merchant categories list page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 19 logged");


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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    public void payViaQrCodeProcess(String amount_str) {
        P2MBarcodeGenInitValidationRequest request = new P2MBarcodeGenInitValidationRequest();
        request.setTpin(tpin);
        String timezone = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
        long currentLocalTime = cal.getTimeInMillis();
        request.setBarcodeGeneratedDate(currentLocalTime);
        request.setTxnAmount((Double.parseDouble(amount_str)));
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

    public void loadMoneyCheck() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        PaymentForm paymentForm = new PaymentForm();
        paymentForm.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        String jsondata = new Gson().toJson(paymentForm);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOADMONEY_CHECK_REQUEST.getURL());
        buffer.append("?data=" + URLUTF8Encoder.encode(jsondata));

        Log.e("Load Money URL: ",""+buffer.toString());

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
                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
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


            Log.e("MacKey", "" + customerLoginRequestReponse.getMac_key());
            Log.e("EncKey", "" + customerLoginRequestReponse.getEnc_key());


            String android_id = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
            byte[] counter = {0x00, 0x00};
            byte[] staticId = Hex.toByteArr(customerLoginRequestReponse.getUniqueCustomerId());
            amount_str = amount;
            byte[] amount_bytes = Hex.floatToByteArray(Float.parseFloat(amount));
            byte[] random = SecurityUtils.generateApplicationAESKey(4);
            byte[] time_bytes = new BarCodeTimeParser().getEncoded();
            byte[] dataToBeMaced = SMSUtils.mountLVParams(null, new Object[]{
                    counter, random, amount_bytes, time_bytes}, new byte[]{
                    SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed,
                    SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed});
            byte[] mac = SecurityUtils.generateMac(Hex.toByteArr(customerLoginRequestReponse.getMac_key()), Hex.toByteArr(android_id), dataToBeMaced, 0, dataToBeMaced.length, 32);
            byte[] dataToBeEnced = SMSUtils.mountLVParams(null, new Object[]{
                    counter, random, time_bytes, mac}, new byte[]{
                    SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed,
                    SMSUtils.cw_pre_formed, SMSUtils.cw_pre_formed});
            byte[] enc = SecurityUtils.encipherData(Hex.toByteArr(customerLoginRequestReponse.getEnc_key()), Hex.toByteArr(android_id), dataToBeEnced, 0, dataToBeEnced.length);
            BcodeHeaderEncoder headerEncoder = new BcodeHeaderEncoder(staticId, Float.parseFloat(amount), (byte) spIndex);
            StringBuffer data = new StringBuffer();
            data.append(Hex.toHex(headerEncoder.getEncoded()));//6-7
            data.append(Hex.toHex(enc));
            data.append(Hex.toHex(enc.length));
            return new BigInteger(Hex.toByteArr(data.toString())).toString();
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


    String selectedLanguage = null;

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


            /*
//            Merchant to Customer payment implementd by me

                if (resultCode == RESULT_OK && requestCode == 1111) {

                    //Setting language
                    selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
                    if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
                        LocaleHelper.setLocale(MainActivity.this, selectedLanguage);
                    }
                    //@end
                    try {
                        String qr_code_data = data.getStringExtra("SCAN_RESULT");

//                                CoreApplication application = (CoreApplication) getApplication();
//                                String uiProcessorReference = application.addUserInterfaceProcessor(new DecodeIncomingQrPaymentCode(url));
//                                ProgressDialogFrag progress = new ProgressDialogFrag();
//                                Bundle bundle = new Bundle();
//                                bundle.putString("uuid", uiProcessorReference);
//                                progress.setCancelable(true);
//                                progress.setArguments(bundle);
//                                progress.show(getSupportFragmentManager(), "progress_dialog");


//                                    BigInteger bg = new BigInteger(qr_code_data.trim());
//                                    byte[] encoded = bg.toByteArray();

//                                    byte[] len_bytes_enc_length = new byte[4];
//                                    System.arraycopy(encoded,encoded.length-4,len_bytes_enc_length,0,4);
//                                    int length_of_enc_data = Hex.byteArrayToInt(len_bytes_enc_length);
//                                    int length_of_header_data = encoded.length - length_of_enc_data -4;
//
//                                    byte[] header = new byte[length_of_header_data];
//                                    System.arraycopy(encoded,0,header,0,length_of_header_data);
//
//                                    byte[] enciphered = new byte[length_of_enc_data];
//                                    System.arraycopy(encoded,length_of_header_data,enciphered,0,length_of_enc_data);

//                                    BcodeHeaderEncoderPlain bcodeHeaderEncoder = new BcodeHeaderEncoderPlain(encoded);

//                                    Log.e("mx_auth_token","mx_auth_token"+bcodeHeaderEncoder.mx_auth_token);

//                                    DecodedQrPojo  decodedQrPojo = new DecodedQrPojo(encoded,header,enciphered,bcodeHeaderEncoder);


                        //Rahman

                        String scanned_data = qr_code_data.trim();
                        String scanned_data_spilted[] = scanned_data.split("-");
                        String auth_token = scanned_data_spilted[0];
                        String amount_hex_string = scanned_data_spilted[1];

                        byte[] amount_byte_array = Hex.toByteArr(amount_hex_string);

                        String amount_str = new String(amount_byte_array);

                        Intent proceedIntent = new Intent(this, DisplayAmountToMerchantActivityDummy.class);
                        proceedIntent.putExtra(DisplayAmountToMerchantActivityDummy.KEY_AMOUNT_SCANNED, amount_str);
                        proceedIntent.putExtra(DisplayAmountToMerchantActivityDummy.KEY_MX_AUTH_TOKEN, auth_token);
                        startActivity(proceedIntent);

                    } catch (Exception e) {
                        Log.e("Barcode data decode Ex", " " + e.getMessage());

                    }

                } else {


                    selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
                    if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
                        LocaleHelper.setLocale(MainActivity.this, selectedLanguage);
                    }

                }


             */


            if (resultCode == RESULT_OK && requestCode == STATIC_QR_CODE_REQUEST) {

//                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                should_call_session_time_out_from_onResume = false;

                //Setting language
                    selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
                    if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
                        LocaleHelper.setLocale(MainActivity.this, selectedLanguage);
                    }


                //@end
                try {
                    String qr_code_data = data.getStringExtra("SCAN_RESULT");


                    PayToMerchantRequest payToMerchantRequest = new PayToMerchantRequest();
                    payToMerchantRequest.setAmount(Double.parseDouble(amount_str));
                    payToMerchantRequest.setMerchantId(qr_code_data);

                    String timezone = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime();
                    Calendar cal = null;
                    if (timezone != null) {
                        cal = Calendar.getInstance(TimeZone.getTimeZone(timezone));
                    } else {
                        cal = Calendar.getInstance(TimeZone.getDefault());
                    }
                    long currentLocalTime = cal.getTimeInMillis();
                    payToMerchantRequest.setClientDate(currentLocalTime);

                    //Code to start server thread and display the progress fragment dialogue (retained)
                    CoreApplication application = (CoreApplication) getApplication();
                    String uiProcessorReference = application.addUserInterfaceProcessor(new PayToMerchantStaticQRCodeInitProcessing(payToMerchantRequest, application, true));
                    ProgressDialogFrag progress = new ProgressDialogFrag();
                    Bundle bundle = new Bundle();
                    bundle.putString("uuid", uiProcessorReference);
                    progress.setCancelable(true);
                    progress.setArguments(bundle);
                    progress.show(getSupportFragmentManager(), "progress_dialog");

                } catch (Exception e) {
                    Log.e("Barcode data decode Ex", " " + e.getMessage());

                }

            } else {

                should_call_session_time_out_from_onResume = false;

                    selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
                    if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
                        LocaleHelper.setLocale(MainActivity.this, selectedLanguage);
                    }

            }


        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(MainActivity.this, "OnActivity Result: " + e.getMessage(), Toast.LENGTH_LONG).show();

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

    private class MyAdapter extends BaseAdapter {
        private List<Item> items = new ArrayList<Item>();

        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            load_wallet = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.recharge_wallet);
            invoice = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.invoice);
            pay = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.qrcode);
            recharge_paybill = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.topupchanged);
            sendmoney = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.send_money);
            prepaid_cards = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pre_paidcard);
            mobilebill = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.mobilebillnew);
            help = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.help);
            txn_history = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.others);
            //store = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.store);

            // items.add(new Item(load_wallet, "Load  wallet"));
            //items.add(new Item(pay, "Pay"));


            items.add(new Item(sendmoney, getResources().getString(R.string.mainmenu_send_money)));
            items.add(new Item(prepaid_cards, getResources().getString(R.string.mainmenu_prepaid_cards)));
            items.add(new Item(mobilebill, getResources().getString(R.string.mainmenu_mobile_billl)));
            items.add(new Item(recharge_paybill, getResources().getString(R.string.mainmenu_intl_top_up)));
            items.add(new Item(invoice, getResources().getString(R.string.mainmenu_invoice)));
            items.add(new Item(where_to_pay, getResources().getString(R.string.mainmenu_where_to_pay)));
            items.add(new Item(my_offers, getResources().getString(R.string.mainmenu_offers)));
            //items.add(new Item(store, "STORE"));
            items.add(new Item(txn_history, getResources().getString(R.string.mainmenu_txn_history)));
            items.add(new Item(help, getResources().getString(R.string.mainmenu_help)));


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
//                        letsspeek();
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
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissionForCamera(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, requestCode);
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

            Log.e("Version",""+latestVersion);

            if (latestVersion != null && !latestVersion.isEmpty()) {
                double live_version = Double.parseDouble(latestVersion);
                double local_version = Double.parseDouble(versionname);

                if (local_version < live_version) {
                    LayoutInflater li = LayoutInflater.from(MainActivity.this);
                    View promptsView = li.inflate(R.layout.custom_update_playstore_dialog, null);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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

                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
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


    //Integrating Collect payment in Customer
    private boolean checkCameraPermissionLatest() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    public void onBarcodeDecoded(DecodedQrPojo decodedQrPojo) {
        if (null != decodedQrPojo) {

            Intent proceedIntent = new Intent(this, DisplayAmountToMerchantActivityDummy.class);
            proceedIntent.putExtra("data", decodedQrPojo);
            startActivity(proceedIntent);


        } else {

            Toast.makeText(MainActivity.this, "Data null", Toast.LENGTH_LONG).show();
        }

    }


    private GenericRequest request;

    public class InvoiceSessionTimeOut extends AsyncTask<String, Void, String> {


        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {

            String result;
            String inputLine;
            try {

                CoreApplication application = (CoreApplication) getApplication();
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();

                GenericRequest invoiceRequest = new GenericRequest();

                invoiceRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                invoiceRequest.setG_transType(TransType.INVOICE_LIST_REQUEST.name());

                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.INVOICE_LIST_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(invoiceRequest)));
                String invoiceSessionTimeOutURL = buffer.toString();



                //Create a URL object holding our url
                URL myUrl = new URL(invoiceSessionTimeOutURL);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

                String network_response = result;

                if(network_response!=null) {

                    if (!network_response.isEmpty()) {
                        GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);

                        String error_text_header = response.getG_errorDescription();


                        if (error_text_header.equalsIgnoreCase("Session expired")) {

                            Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {


//                       Toast toast = Toast.makeText(MainActivity.this, " "+network_response, Toast.LENGTH_LONG);
//                       toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                       toast.show();
                        }
                    }


                }else{

                    Toast toast = Toast.makeText(MainActivity.this, " "+getString(R.string.failure_network_error), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();

                }



        }
    }
}


//class Item {
//    Bitmap image;
//    String title;
//
//    public Item(Bitmap image, String title) {
//        super();
//        this.image = image;
//        this.title = title;
//    }
//
//    public Bitmap getImage() {
//        return image;
//    }
//
//    public void setImage(Bitmap image) {
//        this.image = image;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    Button btn_recharge_wallet;
//}

