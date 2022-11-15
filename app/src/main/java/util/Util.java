package util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.biometric.BiometricPrompt;

import com.bookeey.wallet.live.R;

public class Util {

    public static void EnableBiometricAlert(Context context) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_alert_whatsapp, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setView(promptsView);
        alertDialog.show();
    }

    public static BiometricPrompt.PromptInfo GetLoginBiometricDialog() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Bookeey Pay")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Login with password")
                .build();
    }

    public static BiometricPrompt.PromptInfo GetBiometricDialog() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verify Biometric")
                .setSubtitle("Verify your biometric credential.")
                .setNegativeButtonText("Verify with password")
                .build();
    }

    public static BiometricPrompt.PromptInfo EnableBiometricDialog() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Enable Biometric")
                .setSubtitle("Verify your biometric credential.")
                .setNegativeButtonText("Cancel")
                .build();
    }
}
