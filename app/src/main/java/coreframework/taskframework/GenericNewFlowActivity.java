package coreframework.taskframework;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.CheckForUpdatesActivity;
import com.bookeey.wallet.live.Contact_Us_Activity;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.Splash;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.application.DownloadResultReceiver;
import com.bookeey.wallet.live.application.SyncService;
import com.bookeey.wallet.live.application.SyncServiceNewFlow;
import com.bookeey.wallet.live.changes.ChangePinActivity;
import com.bookeey.wallet.live.changes.ChangeTPinActivity;
import com.bookeey.wallet.live.changes.ErrorDialog_MobileNumberChange;
import com.bookeey.wallet.live.txnhistory.About_Us_Activity;
import com.bookeey.wallet.live.txnhistory.TransactionHistoryActivity;
import com.google.gson.Gson;

import java.util.Date;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.ViewProfileProcessing;
import coreframework.processing.logout.LogoutProcessing;
import coreframework.utils.HandleUncaughtException;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import coreframework.utils.URLUTF8Encoder;
import newflow.TransactionHistoryActivityNewFlow;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;

public class GenericNewFlowActivity extends FragmentActivity implements YPCHeadlessCallback {


    public static final String Tag = GenericActivity.class.getSimpleName();


    private boolean isUndoBarEnabled = false;
    private InfoUpdateReceiver infoUpdateReceiver = null;
    private Menu optionsMenu;
    private DownloadResultReceiver mReceiver = null;
    private Menu menu;
    private String inMenuTitle = "Set to In";
    private String outMenuTitle = "Set to Out";
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayUseLogoEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //getActionBar().setIcon(R.drawable.back_update);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        infoUpdateReceiver = new InfoUpdateReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(infoUpdateReceiver, new IntentFilter("custom-event-profile-update"));
        Thread.setDefaultUncaughtExceptionHandler(new HandleUncaughtException(this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(infoUpdateReceiver);
    }

    public boolean isCloseActivityOnAlertDialoguesExecution() {
        return closeActivityOnAlertDialoguesExecution;
    }

    public void setCloseActivityOnAlertDialoguesExecution(boolean closeActivityOnAlertDialoguesExecution) {
        this.closeActivityOnAlertDialoguesExecution = closeActivityOnAlertDialoguesExecution;
    }

    private boolean closeActivityOnAlertDialoguesExecution = false;


    private boolean showMenu = true;

    public void showMenu(boolean show) {
        showMenu = show;


    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (showMenu) {
            this.optionsMenu = menu;
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_new_flow, menu);

            /*menu.add("Add Contacts");
            menu.getItem(0).setIcon(R.drawable.dot2);
            menu.add("Home");
            menu.getItem(0).setIcon(R.drawable.homemenuicon);
*/

            /*for (int i = 0; i < optionsMenu.size(); i++) {


                final MenuItem item = optionsMenu.getItem(i);
                if (item.getItemId() == R.id.empty) {
                    View itemChooser = item.getActionView();
                    if (itemChooser != null) {
                        itemChooser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "ttt" + item.getItemId(), Toast.LENGTH_LONG).show();
                                onOptionsItemSelected(item);

                            }
                        });
                    }
                }
            }*/
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home_profile:
                return true;
            case R.id.action_manage_profile:
                manageProfile();
                return true;
            case R.id.action_change_pin:
                changePassword();
                return true;

            case R.id.action_change_sim:
                Intent intent = new Intent(GenericNewFlowActivity.this, ErrorDialog_MobileNumberChange.class);
                startActivity(intent);
//                finish();
                return true;

            /*case R.id.action_change_tpin:
                changeTPin();
                return true;
            case R.id.action_reset_tpin:
                Intent intent1 = new Intent(GenericActivity.this, ResetTPIN.class);
                startActivity(intent1);
                return true;*/
            case R.id.action_txn_history:

                //Code to trigger service to update user information:
//                Intent serviceIntent = new Intent(this, SyncService.class);
//                serviceIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
//                startService(serviceIntent);
//
//                ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
//                Intent i = new Intent(GenericNewFlowActivity.this, TransactionHistoryActivity.class);
//                startActivity(i);


                Intent serviceIntent = new Intent(getBaseContext(), SyncServiceNewFlow.class);
                serviceIntent.putExtra("type", SyncServiceNewFlow.TYPE_USER_LOGGED_IN_STATUS);
                startService(serviceIntent);
                ((CoreApplication) getApplication()).setTransactionHistoryResponse(new TransactionHistoryResponse());
                Intent i = new Intent(getBaseContext(), TransactionHistoryActivityNewFlow.class);
                startActivity(i);

                return true;
            case R.id.action_about_us:
                Intent intent2 = new Intent(GenericNewFlowActivity.this, About_Us_Activity.class);
                startActivity(intent2);
                return true;
            case R.id.action_contact_us:
                Intent in = new Intent(GenericNewFlowActivity.this, Contact_Us_Activity.class);
                startActivity(in);
                return true;

            case R.id.action_check_for_update:
                checkforupdate();
               // Toast.makeText(getApplicationContext(), "Disabled.", Toast.LENGTH_LONG).show();
                return true;

//            case R.id.action_invite_friends:
//
//                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
//
//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        "Hey check out BookeeyPay at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID+"\n\nReferal Code: "+customerLoginRequestReponse.getUniqueCustomerId());
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
//
//                return true;

            case R.id.action_exit_app:
                performSecureLogOff();
                //this.finish();
                return true;
            case R.id.empty_refresh:
                setRefreshActionButtonState(true);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeTPin() {
        Intent intent1 = new Intent(GenericNewFlowActivity.this, ChangeTPinActivity.class);
        startActivity(intent1);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        updateMenuItems(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    private void updateMenuItems(Menu menu) {
//        MenuItem date = menu.findItem(R.id.last_loggedin_date);
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        long last_login_time = customerLoginRequestReponse.getLastSuccessLoginTime();
        String loggedin_status = TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(last_login_time));

//        if (loggedin_status != null) {
//            date.setTitle(loggedin_status);
//        } else {
//            date.setTitle("");
//        }


        //FOR BPOINTS
//        MenuItem bPoints = menu.findItem(R.id.action_bpoints_profile);
//        String bPointsSaved = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.BPOINTS);
//
//        if (((CoreApplication) getApplication()).getBpoints() != null) {
//
//            bPoints.setTitle(getString(R.string.bpoitns) + " : " + ((CoreApplication) getApplication()).getBpoints());
//
//        } else {
//
//            bPoints.setTitle(getString(R.string.bpoitns) + " : " + "0.00");
//
//        }

    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.empty_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshUserInfo(false);
                            setRefreshActionButtonState(false);
                        }
                    }, 1000);
                } else {
                    refreshItem.setActionView(null);
                }

            }
        }
    }

    private void refreshUserInfo(boolean requestRefresh) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
        startService(intent);
    }

    public final void updateUserInfo(Handler handler) {
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_oauth_2_0_client_token(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        genericRequest.setG_transType(TransType.USER_INFO_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.USER_INFO_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(genericRequest)));
        new Thread(new ServerConnection(-1, handler, buffer.toString(), getApplicationContext())).start();
    }

    private void changePassword() {
        Intent intent1 = new Intent(GenericNewFlowActivity.this, ChangePinActivity.class);
        intent1.putExtra("changepin", "changepin");
//        CustomSharedPreferences.saveIntData(getBaseContext(), CustomSharedPreferences.APP_STATUS_ACTIVATED, CustomSharedPreferences.SP_KEY.APP_STATUS);
        startActivity(intent1);
    }

    private void performSecureLogOff() {
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new LogoutProcessing(application, false));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    public void updateProfile(int customerName, int walletId, int balanceId) {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        if (new Gson().toJson(customerLoginRequestReponse) == null) {
            /*Intent i=new Intent(GenericActivity.this, OoredooValidation.class);
            startActivity(i);*/
            return;
        } else {

            //Implemented for Creditcard view Feb11

            if(  ((TextView) findViewById(customerName))!=null && ((TextView) findViewById(walletId)) !=null &&     ((TextView) findViewById(balanceId))!=null) {

                ((TextView) findViewById(customerName)).setText(customerLoginRequestReponse.getCustFirstName() + " " + customerLoginRequestReponse.getCustLastName());
                ((TextView) findViewById(walletId)).setText(customerLoginRequestReponse.getMobileNumber());
                if (customerLoginRequestReponse.getWalletBalance() != null) {

                    ((TextView) findViewById(balanceId)).setText("KWD " + PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));

//                ((TextView) findViewById(balanceId)).setText( ""+ PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
                }
            }
        }
    }

    public class InfoUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleProfileUpdate();
        }
    }

    public void handleProfileUpdate() {

    }

    void checkforupdate() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (Exception e) {
            Log.e("TAG1", "" + e.toString());
        }
        int versionCode = BuildConfig.VERSION_CODE;
        String version = BuildConfig.VERSION_NAME;

        Intent intent = new Intent(getBaseContext(), CheckForUpdatesActivity.class);
        intent.putExtra("VersionName", version);
        intent.putExtra("VersionCode", versionCode);
        startActivity(intent);
    }

    void manageProfile() {
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new ViewProfileProcessing(application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onProgressComplete() {

    }


//    //Session Expiry code

//    METHOD 1
//    public CoreApplication getApp() {
//        return (CoreApplication) this.getApplication();
//    }
//
//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        getApp().touch();
//        Log.d("TAG", "User interaction to " + this.toString());
//    }


//    METHOD 2

//    public static final long DISCONNECT_TIMEOUT = 5000;

//    public static final long DISCONNECT_TIMEOUT = 5*60*1000;


    private Handler disconnectHandler = null;

    private HandlerThread mHandlerThread = null;

    public void startHandlerThread() {
        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        disconnectHandler = new Handler(mHandlerThread.getLooper());
    }

//    private Handler disconnectHandler = new Handler(Looper.myLooper()) {
//        public void handleMessage(Message msg) {
//        }
//    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {

//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                    GenericActivity.this);
//            alertDialog.setCancelable(false);
//            alertDialog.setTitle("Alert");
//            alertDialog
//                    .setMessage("Session Timeout, Hit ok to go to previous screen.");
//            alertDialog.setNegativeButton("OK",
//                    new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//
////                            Intent intent = new Intent(GenericActivity.this,
////                                    Splash.class);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
////                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivity(intent);
//
////                            performSecureLogOff();
//
//
//                            CoreApplication application = (CoreApplication) getApplication();
//                            application.setIsUserLoggedIn(false);
//                            Intent intent = new Intent(GenericActivity.this,Splash.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//
//
//                            dialog.cancel();
//                        }
//                    });
//
//            alertDialog.show();


            try {

                Log.e("Generic Kill1", "Called");

                CoreApplication application = (CoreApplication) getApplication();
                application.setIsUserLoggedIn(false);


                finishAffinity();

                // Wipe your valuable data here
//            System.exit(0);


            } catch (Exception e) {

                Log.e("GenericPlain Kill1Ex ", "Called " + e.getMessage());

                CoreApplication application = (CoreApplication) getApplication();
                application.setIsUserLoggedIn(false);


//                Intent intent = new Intent(GenericActivity.this, Splash.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);


//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();


//                performSecureLogOff();
//                System.exit(0);


            }


        }
    };

    public void resetDisconnectTimer() {


        try {
            startHandlerThread();

            disconnectHandler.removeCallbacks(disconnectCallback);
            disconnectHandler.postDelayed(disconnectCallback, Splash.DISCONNECT_TIMEOUT);


        } catch (Exception e) {
            Log.e("Generic ExcepresetKill", "Called");
        }


    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        Log.e("Gene onUserInteraction", "Called");
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("Generic resumeKill", "Called");

        resetDisconnectTimer();


        //For static QR balance refesh & Removal of 'Refresh' icon
        refreshUserInfo(true);


    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("Generic Stop", "Called");

        //Timer not running after going to background...don't stop it...let it continue
//        stopDisconnectTimer();
    }
}




