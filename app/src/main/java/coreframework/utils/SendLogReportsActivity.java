package coreframework.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import com.bookeey.wallet.live.R;

public class SendLogReportsActivity extends Activity implements View.OnClickListener {
    private AlertDialog alertDialog;
    private String logs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // make a dialog without a titlebar
        setFinishOnTouchOutside(false); // prevent users from dismissing the dialog by tapping outside
        setContentView(R.layout.send_log_reports_activity);
        logs = getIntent().getStringExtra("logs");
        showConfirmation();
    }

    @Override
    public void onClick(View v) {
        // respond to button clicks in your UI
    }

    private void sendLogFile() {
        if (logs == null)
            return;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bitak@xenon4pay.com","tech2@bookeey.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Error reported from BookeeyWallet");
        intent.putExtra(Intent.EXTRA_TEXT, "Log file attached." + logs); // do this so some email clients don't complain about empty body.
        startActivityForResult(intent,1);
    }

    private void showConfirmation() {
        // method as shown above
        alertDialog = new AlertDialog.Builder(SendLogReportsActivity.this).create();
        alertDialog.setTitle(Html.fromHtml("<font color='#000000'>Report Error !</font>"));
        alertDialog.setMessage("The Application Bookeey(process com.bookeey.wallet.live) has stopped unexpectedly.Please try again");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Report", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sendLogFile();
            //    SendLogReportsActivity.this.finish();

            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
                SendLogReportsActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}

