/*
package push_notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.Splash;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            */
/*
             * Filter messages based on message type. Since it is likely that GCM will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't recognize.
			 *//*


            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) // If it's a regular GCM message, do some work.
            {
                // This loop represents the service doing some work.
                for (int i = 0; i < 5; i++) {
                    Log.i(this.toString(), "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(this.toString(), "Completed work @ " + SystemClock.elapsedRealtime());

                // Post notification of received message.
                String message = (String) extras.get("message");
                sendNotification(message);

                Log.i(this.toString(), "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(getApplicationContext(), Splash.class);
//        notificationIntent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).
                setSmallIcon(R.drawable.icon)
                .setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setContentTitle("Latest Bookeey Offer")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notification);

       */
/* mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Gcw_Driver_Splash_Screen.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.gcw_app_icon)
                .setContentTitle("Gocarwash")
                .setStyle(new NotificationCompat
                        .BigTextStyle()
                        .bigText(msg))
                .setAutoCancel(true)
                .setContentText(msg);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, notification);
        mBuilder.setContentIntent(contentIntent);
        //mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*//*

    }
}
*/
