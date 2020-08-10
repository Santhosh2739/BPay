package com.bookeey.wallet.live.appsessionout;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bookeey.wallet.live.Splash;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;

import coreframework.taskframework.GenericActivity;


public class Waiter extends Thread
{
    private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop = false;
    private Context mContext;

    public Waiter(Context context,long period) {
        this.period=period;
        stop=false;
        mContext = context;
    }

    public void run() {
        long idle=0;
        this.touch();
        Log.e("SessionValue of stop",String.valueOf(stop));
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
//            Log.d(TAG, "Application is idle for "+idle +" ms");
            try
            {
                Thread.sleep(2000); //check every 5 seconds
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "SessionWaiter interrupted!");
            }
            if(idle > period)
            {
                idle=0;

                Log.e(TAG, "Session Perform Your desired Function like Logout or expire the session for the app.!");
                //do something here - e.g. call popup or so

                // Perform Your desired Function like Logout or expire the session for the app.



                startActivityFromMainThread();

                stopThread();
            }
        }
        while(!stop);
        Log.e(TAG, "SessionFinishing Waiter thread");
    }


    public void startActivityFromMainThread(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {


//

                            CoreApplication application = (CoreApplication) mContext.getApplicationContext();
                            application.setIsUserLoggedIn(false);
                            Intent intent = new Intent(mContext,Splash.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);


//                            Toast.makeText(mContext,"Log out2",Toast.LENGTH_LONG).show();

                            System.exit(0);


            }
        });
    }
    public synchronized void touch() {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void forceInterrupt() {
        this.interrupt();
    }

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

    public synchronized void stopThread() {
        stop = true;
    }

    public synchronized void startThread() {
        stop = false;
    }

    public synchronized void closeThread() {
        // Perform Your desired Function like Logout or expire the session for the app.
        stopThread();
    }



}
