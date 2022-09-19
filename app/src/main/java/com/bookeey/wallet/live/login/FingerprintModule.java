/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.bookeey.wallet.live.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.inputmethod.InputMethodManager;

import com.bookeey.wallet.live.invoice.InvoiceL1Activity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.mobilebill.MobileBill_L1_Activity;
import com.bookeey.wallet.live.prepaidcard.VirtualPrepaidMainActivityNew;
import com.bookeey.wallet.live.recharge.TopUpInitialActivity;
import com.bookeey.wallet.live.sendmoney.SendMoneyLeg1RequestActivity;

import dagger.Module;
import dagger.Provides;
import newflow.LoginActivityFromGuestMainMenu;
import newflow.LoginActivityFromSplashNewFlow;
import newflow.LoginActivityNewFlow;

/**
 * Dagger module for Fingerprint APIs.
 */
@Module(
        library = true,
        injects = {MainActivity.class, InvoiceL1Activity.class, MobileBill_L1_Activity.class, VirtualPrepaidMainActivityNew.class, SendMoneyLeg1RequestActivity.class, TopUpInitialActivity.class, LoginActivity.class, LoginActivityFromSplashNewFlow.class, LoginActivityNewFlow.class, LoginActivityFromGuestMainMenu.class}
)
public class FingerprintModule {

    private final Context mContext;

    public FingerprintModule(Context context) {
        mContext = context;
    }

    @Provides
    public Context providesContext() {
        return mContext;
    }

    @Provides
    public FingerprintManagerCompat providesFingerprintManager(Context context) {
        return FingerprintManagerCompat.from(context);
    }

    @Provides
    public InputMethodManager providesInputMethodManager(Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Provides
    public SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
