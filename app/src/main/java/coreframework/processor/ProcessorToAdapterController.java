package coreframework.processor;

import android.os.Handler;
import android.widget.BaseAdapter;

import com.bookeey.wallet.live.mainmenu.EfficientAdapter;
import com.bookeey.wallet.live.mainmenu.Processor;


/**
 * Created by 30099 on 4/12/2016.
 */
public abstract class ProcessorToAdapterController implements Processor {
    private BaseAdapter _adapter = null;
    private Handler _handler = null;

    private String tag = ProcessorToAdapterController.class.getCanonicalName();

    public final void setBaseAdapterAndHandler(EfficientAdapter adapter, Handler handler) {
        _adapter = (BaseAdapter) adapter;
        _handler = handler;
    }
    public final void notifyDataSetChangedToAdapter() {
        if (_adapter != null) {
//			//Log.d(tag, "notifyDataChangedToAdapter Triggered!");
            try {
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        _adapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                //Log.e(tag, "notifyDataChangedToAdapter Error!" + e);
            }
        }
    }
    final void setTag(String _tag) {
        tag = _tag;
    }
    public abstract void shutdownAndCleanInstance();

}
