package com.bookeey.wallet.live.appsessionout;

import android.app.Application;

public class AppLockManager {
    private static AppLockManager instance;
    private DefaultApplock currentAppLocker;

    public static AppLockManager getInstance() {
        if (instance == null) {
            instance = new AppLockManager();
        }
        return instance;
    }

    public void enableDefaultAppLockIfAvailable(Application currentApp) {

        currentAppLocker = new DefaultApplock(currentApp);

    }

    public void updateTouch(){

        currentAppLocker.updateTouch();
    }
}