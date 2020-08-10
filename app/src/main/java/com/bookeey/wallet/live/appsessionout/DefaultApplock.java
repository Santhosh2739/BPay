package com.bookeey.wallet.live.appsessionout;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bookeey.wallet.live.Splash;
import com.bookeey.wallet.live.application.CoreApplication;

import java.util.Date;

public class DefaultApplock implements Application.ActivityLifecycleCallbacks {

    final String TAG = DefaultApplock.class.getSimpleName();

    private Application mCurrentApp;

    private long WAIT_TIME = 5*60*1000;


//    private long WAIT_TIME = 1*60*1000;

//    private long WAIT_TIME = 10*1000;

    private Waiter waiter;
    private Date mLostFocusDate;

    public DefaultApplock(Application app) {
        super();
        mCurrentApp = app;

        //Registering Activity lifecycle callbacks
        mCurrentApp.unregisterActivityLifecycleCallbacks(this);
        mCurrentApp.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {




        // for UserInactivity
        if(waiter!=null) {
            waiter.stopThread();
        }
        waiter=new Waiter(activity,WAIT_TIME);
        waiter.start();

        // for Screen lock
        if (shouldShowUnlockScreen()) {
            Log.e(TAG, "Sessiontime over");
            //

//            Intent intent = new Intent(activity.getApplicationContext(), Splash.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            Log.e(TAG, "Sessionchanging mLostFocus to null");
//            mLostFocusDate = null;
//            activity.getApplicationContext().startActivity(intent);


//            Toast.makeText(mCurrentApp,"Log out1",Toast.LENGTH_LONG).show();

            CoreApplication application = (CoreApplication) mCurrentApp.getApplicationContext();
            application.setIsUserLoggedIn(false);
            Intent intent = new Intent(mCurrentApp,Splash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCurrentApp.startActivity(intent);

            System.exit(0);

        }


    }

    private boolean shouldShowUnlockScreen() {
        Boolean isvalid = false;
        if (mLostFocusDate == null) {
            isvalid = false;
        } else {
            Log.e(TAG, "SessionTimeout >"+timeSinceLocked());
            if (timeSinceLocked() >= (WAIT_TIME/1000)) {
                isvalid = true;
            } else {
                mLostFocusDate = null;
            }
        }
        Log.d(TAG, isvalid.toString());
        return isvalid;
    }

    private int timeSinceLocked() {
        return Math.abs((int) ((new Date().getTime() - mLostFocusDate.getTime()) / 1000));
    }


    @Override
    public void onActivityPaused(Activity activity) {
        if(waiter!=null) {
            waiter.stopThread();
        }
        mLostFocusDate = new Date();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void updateTouch() {
        if(waiter!=null) {
            waiter.touch();
        }
        mLostFocusDate = new Date();
    }
}
