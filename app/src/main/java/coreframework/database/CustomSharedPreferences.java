package coreframework.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.google.gson.Gson;

import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;


public class CustomSharedPreferences {

    public final static String SIMPLE_NULL = "";

    public enum SP_KEY {
        BARCODE_SIZE,
        SUCCESS,
        GCM_REG_ID,
        MOBILE_NUMBER,
        DEVICE_ID,
        PIN,
        APP_STATUS,
        NAME,
        AUTH_TOKEN,
        LAST_LOGGED_IN_STATUS,
        IMAGE,
        NOTIFICATION_MSG_COUNT,
        COMING_FROM_LOGIN,
        SESSION_EXPIRED,
        KEY_IS_AUTO_LOGIN_FROM_NFC,
        FNAME, LNAME, CIVILDID, EMIALID,LANGUAGE,LANGUAGE_TO_BE_DISPLAYED,TEST_PIN,PASSWORD,ENC_PASSWORD,MOBILEBILL_RESPONSE,PREPAID_RESPONSE,BPOINTS,CURRENT_LATITUTE,CURRENT_LONGITUDE,PREVIOUS_CAT_TAG
    }

    public static final String share_db_preference_db = "share_db_preference_db";
    public static final int APP_STATUS_VIRGIN = -1;
    public static final int APP_STATUS_REQ_SENT = 2;
    public static final int APP_STATUS_ACTIVATED = 3;
    public static final int APP_STATUS_LOGGEDIN = 4;
    public static final String share_db_preferenced_db = "share_db_preferenced_db";

    static public void saveStringData(Context context, String data, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(spKey.name(), data);
        editor.commit();
    }



    static public void saveIntData(Context context, int data, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(spKey.name(), data);
        editor.commit();
    }

    static public void saveGCMRegId(Context context, String data, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preference_db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(spKey.name(), data);
        editor.commit();
    }

    static public String getGCMRegId(Context context, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preference_db, Context.MODE_PRIVATE);
        String sourceId = sharedPreferences.getString(spKey.name(), SIMPLE_NULL);
        return sourceId;
    }

    static public void saveLongData(Context context, long data, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(spKey.name(), data);
        editor.commit();
    }

    static public String getStringData(Context context, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        String sourceId = sharedPreferences.getString(spKey.name(), SIMPLE_NULL);
        return sourceId;
    }

    static public int getIntData(Context context, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(spKey.name(), -1);
        return value;
    }

    static public long getLongData(Context context, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        long value = sharedPreferences.getLong(spKey.name(), -1);
        return value;
    }

    //To check that its coming from login
    static public void saveBooleanData(Context context, boolean data, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(spKey.name(), data);
        editor.commit();
    }

    static public boolean getBooleanData(Context context, SP_KEY spKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_db_preferenced_db, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(spKey.name(),false);
        return value;
    }



}