package coreframework.taskframework;

import android.app.Activity;
import android.os.Message;

public interface UserInterfaceBackgroundProcessing {
	public String captureURL();
	public void processResponse(Message msg);
	public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment);
    public boolean isPost();


	public void preProcessResponse(Message msg);
	public void prePerformUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment);
	public boolean isLocalProcess();
	public void performTask();
	public void handleSessionInvalid(Activity activity, ProgressDialogFrag dialogueFragment);
}
