package coreframework.taskframework;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.bookeey.wallet.live.Splash;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.processing.logout.LogoutProcessing;

public class GenericFragmentActivity extends FragmentActivity {


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


//    METHOD 2

//    public static final long DISCONNECT_TIMEOUT = 5000;
//    public static final long DISCONNECT_TIMEOUT = 5*60*1000;



    private Handler disconnectHandler = null;

    private HandlerThread mHandlerThread = null;

    public void startHandlerThread(){
        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        disconnectHandler = new Handler(mHandlerThread.getLooper());
    }


    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
//
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

            Log.e("Generic Kill2","Called");

            try {


                CoreApplication application = (CoreApplication) getApplication();
                application.setIsUserLoggedIn(false);




                finishAffinity();

                // Wipe your valuable data here
//            System.exit(0);






            }catch(Exception e){

                Log.e("GenericFrag Kill1Ex ", "Called "+e.getMessage());

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

        startHandlerThread();

        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, Splash.DISCONNECT_TIMEOUT);

    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("GenericFr ResumeKill","Called");

        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e("GenericFr Stop","Called");

        //Timer not running after going to background...don't stop it...let it continue
//        stopDisconnectTimer();
    }


}
