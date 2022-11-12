package push_notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import androidx.core.app.NotificationCompat;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.Splash;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;
import java.util.Random;

import coreframework.processing.GetPushNotificationMessageProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import newflow.LoginActivityFromGuestMainMenu;
import newflow.LoginActivityFromSplashNewFlow;
import ycash.wallet.json.pojo.getpushnotificationmessage.GetPushNotificationMessageRequest;

/**
 * Created by 10037 on 31-May-18.
 */

public class MyFirebaseMsgService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*//Get string data from firebase console
        //  Log.d(TAG, "From: " + remoteMessage.getFrom());
        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        //create notification
           if (!remoteMessage.getNotification().getBody().isEmpty())
        createNotification(remoteMessage.getNotification().getBody());*/

        //Get bundle data from java server
        String messageBody = null;
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            String key = entry.getKey();
            messageBody = entry.getValue();
            Log.d(TAG, "key, " + key + " value " + messageBody);
        }
        //sending notification

        //For older device below oreo API level push notification
        //createNotification(messageBody);

        //for oreo onwards new flow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                showNotification(messageBody);
            else
                createNotification(messageBody);

    }

    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }


    private void showNotification(String messageBody) {
        final NotificationManager mNotific =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence name = "BookeeyWallet";
        String desc = "Bookeey Application";
        int imp = NotificationManager.IMPORTANCE_HIGH;
        final String ChannelID = "myChannel";
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(ChannelID, name, imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            mChannel.setLightColor(getResources().getColor(R.color.app_color));
            mChannel.enableLights(true);
            mNotific.createNotificationChannel(mChannel);
        }

        final int ncode = 101;

        // String Body = "This is testing notific";


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent notificationIntent;
   /*     if(Splash.isAppRunning){
            notificationIntent = new Intent(this, LoginActivity.class);
        }else{
            notificationIntent = new Intent(this, Splash.class);
        }*/

   //Old
//        notificationIntent = new Intent(this, Splash.class);

        notificationIntent = new Intent(this, Splash.class);

        notificationIntent.putExtra(Splash.KEY_SHOW_PUSH_NOTIFICATION_MESSAGE,true);

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

/*        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), notificationIntent, 0);
        //notificationIntent.putExtra("msg_from_browser", "com.bookeey.wallet.live.launchfrombrowser");


        Notification n = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            n = new Notification.Builder(this, ChannelID)
                    .setContentTitle("Bookeey Application")
                    .setContentText(messageBody)
                    //.setBadgeIconType(R.drawable.icon)
                    //.setNumber(5)
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSound(notificationSoundURI)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .build();
        }

        mNotific.notify(ncode, n);

        //createNotification(message);


    }


    private void createNotification(String messageBody) {
//        Intent intent = new Intent(getApplicationContext(), Splash.class); // Here pass your activity where you want to redirect.


        //Jan 30 Push notification fetching from server
        Intent intent = new Intent(getApplicationContext(), Splash.class);
        intent.putExtra(Splash.KEY_SHOW_PUSH_NOTIFICATION_MESSAGE,true);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);
        //intent.putExtra("msg_from_browser", "com.bookeey.wallet.live.launchfrombrowser");

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                // setSmallIcon(R.drawable.icon)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setPriority(NotificationCompat.PRIORITY_HIGH) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setContentTitle("Bookeey Application")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent)
                .setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.bookeey_notification);
            mBuilder.setColor(getResources().getColor(R.color.app_color));
        } else {
            mBuilder.setSmallIcon(R.drawable.icon);
        }
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());
    }
}


