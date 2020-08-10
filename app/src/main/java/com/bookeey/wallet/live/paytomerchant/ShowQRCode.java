package com.bookeey.wallet.live.paytomerchant;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import coreframework.barcodeclient.QRBitmap_Generator;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.NFCUtils;
import coreframework.utils.PriceFormatter;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.application.DownloadResultReceiver;
import com.bookeey.wallet.live.application.SyncService;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.txnhistory.TransactionHistoryActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequest;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class ShowQRCode extends GenericActivity {
    TextView buy_header_txt, payamount_edit;
    ImageView qr_code_img_id;
    Button show_qr_code_close_btn;
    private DownloadResultReceiver mReceiver = null;
    ScheduledExecutorService scheduleTaskExecutor = null;
    String barcodedata = null;

    private FirebaseAnalytics firebaseAnalytics;


    //For NFC
    private NfcAdapter _nfcAdapter;
    private PendingIntent _pendingIntent;
    private IntentFilter[] _intentFilters;
    private final String _MIME_TYPE = "text/plain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_qr_code);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics1 = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics1.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("PAY");*/

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
        mTitleTextView.setText(getResources().getString(R.string.show_qr_code_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        barcodedata = getIntent().getStringExtra("barcodedata");
        show_qr_code_close_btn = (Button) findViewById(R.id.show_qr_code_close_btn);
        buy_header_txt = (TextView) findViewById(R.id.buy_header_txt);
        payamount_edit = (TextView) findViewById(R.id.payamount_edit);
        String amount = getIntent().getStringExtra("payamount");
        payamount_edit.setText(getApplicationContext().getString(R.string.qr_code_amount_pay) + PriceFormatter.format(Double.parseDouble(amount), 3, 3) + " KWD");
        qr_code_img_id = (ImageView) findViewById(R.id.qr_code_img_id);

        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();

        if (customerLoginRequestReponse != null && customerLoginRequestReponse.getWalletBalance() != null) {
            buy_header_txt.setText(getString(R.string.qr_code_wallet_balance) + "KWD: " + PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3) + "\n" + getString(R.string.qr_code_ask_merchant_scan));
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int width = metrics.widthPixels;
        final int height = metrics.heightPixels;


//        qr_code_img_id.setImageBitmap(QRBitmap_Generator.getQrBitmap(qr_str, getBaseContext()));


        show_qr_code_close_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleTaskExecutor.shutdown();
                Intent serviceIntent = new Intent(getBaseContext(), SyncService.class);
                serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
                startService(serviceIntent);
                ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                Intent intent = new Intent(getBaseContext(), TransactionHistoryActivity.class);
                startActivity(intent);
                ShowQRCode.this.finish();
            }
        });
        shedulardata();


        //For NFC
//        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//
//        if (_nfcAdapter != null){
//            _init();
//          }

    }

    @Override
    public void onResume() {
        super.onResume();
        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("QR code (generated for a specific amount) page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 6);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "QR code (generated for a specific amount) page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 6 logged");


        //QRCode with logo
        String qr_str = getIntent().getExtras().getString("barcode_data");
        initQRCodeWithLogo(qr_str);

    }

    public void initQRCodeWithLogo(String qrCodeData){
        try {

            //For NFC
//            _nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//            if (_nfcAdapter != null){
//                _enableNdefExchangeMode(qrCodeData);
//              }

            //setting size of qr code
            int width = 1000;
            int height = 1000;
            int smallestDimension = width < height ? width : height;

            //setting parameters for qr code
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCodeData, charset, hintMap, smallestDimension, smallestDimension);

        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }


    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {

        Log.e("QR Code started: ",""+System.currentTimeMillis());
        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap

            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {

                    //Quick
                    pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;


                    //Delay
//                    pixels[offset + x] = matrix.get(x, y) ?ResourcesCompat.getColor(getResources(), R.color.black, null) : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view

            Bitmap _overlay = BitmapFactory.decodeResource(getResources(), R.drawable.bookeey_small);

            Bitmap overlay = pad(_overlay,40,40);

            Log.e("Time before merge: ",""+System.currentTimeMillis());

            Bitmap mergedBitmap =  mergeBitmaps(overlay, bitmap);

            Log.e("Time after merge: ",""+System.currentTimeMillis());

            qr_code_img_id.setImageBitmap(mergedBitmap);

        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
        }
    }

    public Bitmap pad(Bitmap Src, int padding_x, int padding_y) {
        Bitmap outputimage = Bitmap.createBitmap(Src.getWidth() + padding_x,Src.getHeight() + padding_y, Bitmap.Config.ARGB_8888);
        Canvas can = new Canvas(outputimage);
        can.drawARGB(0XFFFFFF,0XFFFFFF,0XFFFFFF,0XFFFFFF); //This represents White color
        can.drawBitmap(Src, padding_x-20, padding_y-20, null);
        return outputimage;
    }
    public Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }




    private void shedulardata() {

        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 5 seconds
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                shedulardatafromserver();
            }
        }, 0, 5, TimeUnit.SECONDS);

    }

    GenericResponse response = null;

    private void shedulardatafromserver() {
        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        PayToMerchantRequest payToMerchantRequest = new PayToMerchantRequest();
        payToMerchantRequest.setCustomerId(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getUniqueCustomerId());
        payToMerchantRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        payToMerchantRequest.setBarcodeData(barcodedata);
        payToMerchantRequest.setG_transType(TransType.P2M_BARCODECLOSE_REQUEST.name());
        String json = new Gson().toJson(payToMerchantRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.P2M_BARCODECLOSE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        Message msg = ServerConnection.directNonHandlerHTTPClient(buffer.toString(), -1);
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            try {
                response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.P2M_BARCODECLOSE_RESPONSE.name()) && (response.getG_status_description().equalsIgnoreCase("Success") || (response.getG_status_description().equalsIgnoreCase("Failure")))) {
                    Intent serviceIntent = new Intent(getBaseContext(), SyncService.class);
                    serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
                    startService(serviceIntent);
                    ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                    Intent intent = new Intent(getBaseContext(), TransactionHistoryActivity.class);
                    startActivity(intent);
                    ShowQRCode.this.finish();
                    scheduleTaskExecutor.shutdown();
                } else if (null != response && response.getG_status() == 1 && response.getG_status_description().equalsIgnoreCase("Initiated")) {
                    return;
                } else if (null != response && response.getG_status() == 100) {
                    Log.e("fff", "eeeee");
                    Log.e("ggggg", "hhhhh");
                    try {

                    /**/
                        ShowQRCode.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), response.getG_errorDescription(), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                            }
                        });
                        scheduleTaskExecutor.shutdown();
                        ShowQRCode.this.finish();
                    } catch (Exception e) {
                        Log.e("", "");
                    }
                } else {
                    return;
                }
            } catch (Exception e) {
                return;
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
//            showQRCode.cancelUserLoggedInStatusAlarmManagerForBarcode();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelUserLoggedInStatusAlarmManagerForBarcode() {
        Intent alarmIntent = new Intent(this, SyncService.class);
        boolean alarmUp = (PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp) {
            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
            alarmMgr.cancel(pending);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        scheduleTaskExecutor.shutdown();
    }


    //For NFC
    private void _enableNdefExchangeMode(String qrCodeData)
    {
        Log.e("4._enableNdefExchange","_enableNdefExchangeMode");

//        EditText messageTextField = (EditText) findViewById(R.id.message_text_field);
//        messageTextField.setText("Rahman");
//        String stringMessage = " " + messageTextField.getText().toString();

        NdefMessage message = NFCUtils.getNewMessage(_MIME_TYPE, qrCodeData.getBytes());

        _nfcAdapter.setNdefPushMessage(message, this);
        _nfcAdapter.enableForegroundDispatch(this, _pendingIntent, _intentFilters, null);
    }
    private void _init()
    {
        Log.e("_init","_init");

        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (_nfcAdapter == null)
        {
            Toast.makeText(this, "This device does not support NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (_nfcAdapter.isEnabled())
        {
            _pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            try
            {
                ndefDetected.addDataType(_MIME_TYPE);
            } catch (IntentFilter.MalformedMimeTypeException e)
            {
                Log.e(this.toString(), e.getMessage());
            }

            _intentFilters = new IntentFilter[] { ndefDetected };
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {

        super.onNewIntent(intent);

        Log.e("1.onNewIntent","onNewIntent");

        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
        {
            List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);

//            Toast.makeText(this, "Message received : "+msgs.get(0), Toast.LENGTH_LONG).show();


            Toast.makeText(this, "Wallet app need to confirm NFC data sharing", Toast.LENGTH_LONG).show();
        }
    }
}
