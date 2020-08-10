package coreframework.taskframework;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
public class ProgressDialogFrag extends DialogFragment implements YPCHeadlessCallback {
	private HeadlessFragment mHeadless;
	private YPCHeadlessCallback mCallback;
	private UserInterfaceBackgroundProcessing uiProcessor;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}

	//Solution -  Bug - Can not perform this action after onSaveInstanceState
	//https://stackoverflow.com/questions/7469082/getting-exception-illegalstateexception-can-not-perform-this-action-after-onsa/10261438#10261438
	@Override
	public void onSaveInstanceState( Bundle outState ) {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//Old code till Feb 02 2020
		FragmentManager fm = getFragmentManager();

//Illegal state exception Feb02 Bita WhatsApp
//		FragmentManager fm = getFragmentManager();


		mHeadless = (HeadlessFragment) fm.findFragmentByTag("headless");
		if (mHeadless == null) {
			mHeadless = new HeadlessFragment();
			mHeadless.setArguments(getArguments());


			//Old
//			fm.beginTransaction().add(mHeadless, "headless").commit();

			//New -  Bug - Can not perform this action after onSaveInstanceState
			fm.beginTransaction().add(mHeadless, "headless").commitAllowingStateLoss();
		}
		mHeadless.setTargetFragment(this, 0);
		uiProcessor = ((CoreApplication)getActivity().getApplication()).getUserInterfaceProcessor(getArguments().getString("uuid"));
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof YPCHeadlessCallback){
			mCallback = (YPCHeadlessCallback)activity;
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.MyTheme2);
		dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage(getString(R.string.loading));
//        dialog.setTitle(getString(R.string.loading));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		return dialog;
	}
   /* @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        ProgressDialog dialog = new ProgressDialog(
                getActivity(),
                android.R.attr.spinnerStyle);

        dialog.setIndeterminate(true);


        dialog.setIndeterminateDrawable(getActivity().getResources().getDrawable(R.drawable.my_progress_indeterminate));

        return dialog;
    }*/
	@Override
	public void onProgressUpdate(int progress) {
		ProgressDialog dialog = (ProgressDialog) getDialog();
		if (dialog != null) {
			dialog.setProgress(progress);
			if(mCallback!=null){
				mCallback.onProgressUpdate(progress);
			}
		}
	}
	@Override
	public void onProgressComplete() {
//		dismiss();
		if(mCallback!=null && uiProcessor!=null){
			mCallback.onProgressComplete();
			uiProcessor.performUserInterfaceAndDismiss(getActivity(),this);
		}
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		mHeadless.cancelTask();
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}
	public UserInterfaceBackgroundProcessing getProcessing() {
		return uiProcessor;
	}
	public void setProcessing(UserInterfaceBackgroundProcessing processing) {
		this.uiProcessor = processing;
	}
}