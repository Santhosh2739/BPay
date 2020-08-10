package coreframework.utils;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.bookeey.wallet.live.BuildConfig;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.database.CustomSharedPreferences;

public class HandleUncaughtException implements Thread.UncaughtExceptionHandler {
    private static Activity myContext = null;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public HandleUncaughtException(Activity context) {
        myContext = context;
    }

    //*********************************************************
    public void reportLogs(final String errorLogs) {
        Log.e("custom error", errorLogs.toString());

        //1.Open Send log activity
        Intent i = new Intent(myContext, SendLogReportsActivity.class);
        i.putExtra("logs", errorLogs);
        myContext.startActivity(i);
        myContext.finish();

        //2.store the logs in internal storage(enable if requires)
        //writeToFile(errorLogs);

    }

    private void sendMail_toDeveloper(String logs) {
        if (logs == null)
            return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bitak@xenon4pay.com","tech2@bookeey.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error reported from BookeeyWallet");
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached." + logs); // do this so some email clients don't complain about empty body.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.exit(0);
        myContext.startActivity(intent);
        myContext.finish();
    }

    private void writeToFile(String currentStacktrace) {
        try {
            //Gets the Android external storage directory & Create new folder Crash_Reports
            String PATH = Environment.getExternalStorageDirectory() + "/Bookeey_Crash_Reports/";
            File dir = new File(PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String filename = dateFormat.format(date) + "_STACKTRACE" + ".txt";

            // Write the file into the folder
            File reportFile = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(reportFile);
            fileWriter.append(currentStacktrace);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            Log.e("ExceptionHandler", e.getMessage());
        }

    }

    public void prepareLogs(Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************" + LINE_SEPARATOR);
        errorReport.append(stackTrace.toString());
        errorReport.append("************ Timestamp ************"+ LINE_SEPARATOR);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy & hh:mm a");
            Date date = new Date();
            String filename = dateFormat.format(date);
            errorReport.append("Date and Time: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        errorReport.append(LINE_SEPARATOR + "************ DEVICE INFORMATION ***********" + LINE_SEPARATOR);
        errorReport.append("Brand: " + Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: " + Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: " + Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: " + Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: " + Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(LINE_SEPARATOR + "************ BUILD INFO ************" + LINE_SEPARATOR);
        errorReport.append("SDK: " + Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: " + Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: " + Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        errorReport.append(LINE_SEPARATOR + "************ APP INFO ************" + LINE_SEPARATOR);

        int versionCode = BuildConfig.VERSION_CODE;
        String version = BuildConfig.VERSION_NAME;

        errorReport.append("VersionCode: " + versionCode);
        errorReport.append(LINE_SEPARATOR);

        errorReport.append("App Version: " + version);
        errorReport.append(LINE_SEPARATOR);



       String deviceID = CustomSharedPreferences.getStringData(myContext, CustomSharedPreferences.SP_KEY.DEVICE_ID);
       errorReport.append("Device ID: " + deviceID);
       errorReport.append(LINE_SEPARATOR);


        reportLogs(errorReport.toString());


    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        prepareLogs(throwable);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}
