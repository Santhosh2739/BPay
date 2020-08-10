package coreframework.taskframework;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import coreframework.network.ServerConnection;
import com.bookeey.wallet.live.application.CoreApplication;

public class HeadlessFragment extends Fragment {
	private YPCHeadlessCallback mCallback;
	private HeadlessAsyncTask mTask;
	private DefaultHttpClient httpClient; 
	private UserInterfaceBackgroundProcessing uiProcessor;
	private CoreApplication cApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		HttpParams httpParameters = new BasicHttpParams(); 
		// Set the timeout in milliseconds until a connection is established. 
		// The default value is zero, that means the timeout is not used.  
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection); 
		// Set the default socket timeout (SO_TIMEOUT)  
		// in milliseconds which is the timeout for waiting for data. 
		int timeoutSocket = 40000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpClient = new DefaultHttpClient(httpParameters);
	}
	void cancelTask(){
		mTask.cancel(true);
		httpClient.getConnectionManager().shutdown();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cApp = (CoreApplication)getActivity().getApplication();
		uiProcessor = cApp.getUserInterfaceProcessor(getArguments().getString("uuid"));
		if (getTargetFragment() instanceof YPCHeadlessCallback) {
			mCallback = (YPCHeadlessCallback) getTargetFragment();
			if (mTask == null
					|| mTask.getStatus() == AsyncTask.Status.FINISHED) {
				mTask = new HeadlessAsyncTask();
				mTask.execute(httpClient);
			}
		}
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mCallback = null;
	}
	public class HeadlessAsyncTask extends AsyncTask<DefaultHttpClient, Integer, Message> {
		@Override
		protected Message doInBackground(DefaultHttpClient... params) {
			Message msg = null;
			if(uiProcessor!=null) {

				String url = uiProcessor.captureURL();
				publishProgress(1);
				msg = ServerConnection.executeHTTPRequest(url, 0, uiProcessor.isPost(), params[0]);
				publishProgress(2);
//			    SystemClock.sleep(5000);
				uiProcessor.processResponse(msg);
				publishProgress(3);

			}
			return msg;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			if (mCallback != null) {
				mCallback.onProgressUpdate(values[0]);
			}
		}
		@Override
		protected void onPostExecute(Message msg) {
			if (mCallback != null) {
				mCallback.onProgressComplete();
			}
			cApp.removeUserInterfaceProcessor(getArguments().getString("uuid"));
			if(getActivity()!=null)
//			getActivity().getSupportFragmentManager().beginTransaction().remove(HeadlessFragment.this).commit();


				//Can't perform this after onSaveInstance
			getActivity().getSupportFragmentManager().beginTransaction().remove(HeadlessFragment.this).	commitAllowingStateLoss();

		}
		@Override
		protected void onCancelled(Message result) {
           // Toast.makeText(getActivity(),"async task cancelled.....", Toast.LENGTH_SHORT).show();
            cApp.removeUserInterfaceProcessor(getArguments().getString("uuid"));
            if(result!=null) {
				if(getActivity()!=null)
//            	getActivity().getSupportFragmentManager().beginTransaction().remove(HeadlessFragment.this).commit();

				//Can't perform this after onSaveInstance
				getActivity().getSupportFragmentManager().beginTransaction().remove(HeadlessFragment.this).	commitAllowingStateLoss();
            }
		}
	}
	public UserInterfaceBackgroundProcessing getUiProcessor() {
		return uiProcessor;
	}
	public void setUiProcessor(UserInterfaceBackgroundProcessing uiProcessor) {
		this.uiProcessor = uiProcessor;
	}
}