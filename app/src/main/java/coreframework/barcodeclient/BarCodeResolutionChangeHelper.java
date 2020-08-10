package coreframework.barcodeclient;

import android.content.Context;
import android.view.WindowManager;

import coreframework.database.CustomSharedPreferences;

/**
 * Ported and Modified from Legacy YPC Mobile Application,
 * Data Model changed from Singleton, DB to Static Method and Shared Preferences Model
 * @author mohit
 */
public final class BarCodeResolutionChangeHelper {
	private static int default_size = 200;
	private static void init(Context context){
		int res = -1;
		if((res = CustomSharedPreferences.getIntData(context, CustomSharedPreferences.SP_KEY.BARCODE_SIZE))==-1){
			int estimated = computeEstimateDefaultSize(context);
			default_size = estimated;
		}else{
			default_size = res;
		}
	}
	private static int computeEstimateDefaultSize(Context context){
		int defined = 0;
		int width 	= getScreenWidth(context);
		int height 	= getScreenHeight(context);
		if(width>200 && height>200){
			defined = 200;
		}else{
			defined = 200;
		}
		return defined;
	}
	public static boolean updateResolution(int size,Context context){
		CustomSharedPreferences.saveIntData(context,size, CustomSharedPreferences.SP_KEY.BARCODE_SIZE);
		return true;
	}
	public static int getCurrentResolutionSize(Context context){
		init(context);
		return default_size;
	}
	public void resetToDefault(Context context){
		updateResolution(computeEstimateDefaultSize(context),context);
	}
	final public static int getScreenWidth(Context context){
		WindowManager mWinMgr = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		int displayWidth = mWinMgr.getDefaultDisplay().getWidth();
		return displayWidth;
	}
	final public static int getScreenHeight(Context context){
		WindowManager mWinMgr = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		int displayHeght = mWinMgr.getDefaultDisplay().getHeight();
		return displayHeght;
	}
}