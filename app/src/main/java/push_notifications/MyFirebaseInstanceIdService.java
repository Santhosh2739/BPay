package push_notifications;

import android.util.Log;

import com.bookeey.wallet.live.login.LoginActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import coreframework.database.CustomSharedPreferences;

/**
 * Created by 10037 on 31-May-18.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFCMIIDService";

    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        CustomSharedPreferences.saveGCMRegId(getApplicationContext(), refreshedToken, CustomSharedPreferences.SP_KEY.GCM_REG_ID);
    }

    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}
