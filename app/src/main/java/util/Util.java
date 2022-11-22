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

    public static BiometricPrompt.PromptInfo GetLoginBiometricDialog(Context context) {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getResources().getString(R.string.unlock_bookeey))
                .setSubtitle(context.getResources().getString(R.string.login_biometric))
                .setNegativeButtonText(context.getResources().getString(R.string.login_password))
                .build();
    }

    public static BiometricPrompt.PromptInfo GetBiometricDialog(Context context) {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getResources().getString(R.string.verify_biometric))
                .setSubtitle(context.getResources().getString(R.string.verify_biometric_cred))
                .setNegativeButtonText(context.getResources().getString(R.string.verify_with_password))
                .build();
    }

    public static BiometricPrompt.PromptInfo EnableBiometricDialog(Context context) {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getResources().getString(R.string.enable_biometric_text))
                .setSubtitle(context.getResources().getString(R.string.verify_biometric_cred))
                .setNegativeButtonText(context.getResources().getString(R.string.cancel))
                .build();
    }
}
