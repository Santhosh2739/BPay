package com.bookeey.wallet.live.invoice;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.processing.invoice_Processing.InvoicePayNowProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.invoicePojo.InvoicePaymentRequest;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by 10037 on 06-Dec-17.
 */

public class InvoiceReasonDialogue extends FragmentActivity implements YPCHeadlessCallback {
    EditText invoice_remarks_edit;
    Button invoice_remarks_submit_btn;
    String remarks_str, name, no, date, amount;
    long offer_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reasons_dialogue);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Bundle b = getIntent().getExtras();
        if (b != null) {
            name = b.getString("MerchantName");
            no = b.getString("InvoiceNo");
            date = b.getString("Date");
            amount = b.getString("Amount");
            offer_id = b.getLong("OfferID");

        }

        invoice_remarks_edit = (EditText) findViewById(R.id.invoice_remarks_edit);
        invoice_remarks_submit_btn = (Button) findViewById(R.id.invoice_remarks_submit_btn);

        invoice_remarks_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarks = invoice_remarks_edit.getText().toString();
                remarks_str = remarks.replace('\n', ' ');//replaces all occurrences of '\n' to 'free space'
                /*if (remarks_str.toString().length() == 0) {
                    Toast toast = Toast.makeText(getBaseContext(), "Please mention your reason..", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/
                invoiceRejectRequest(name, no, date, amount, remarks_str, offer_id, 1);
            }
        });

    }

    private void invoiceRejectRequest(String name, String no, String date, String amount, String reason, long offerID, int status) {

        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        InvoicePaymentRequest invoicePaymentRequest = new InvoicePaymentRequest();
        invoicePaymentRequest.setInvNo(no);
        invoicePaymentRequest.setAmount(Double.parseDouble(amount));
        invoicePaymentRequest.setOfferId(offerID);
        invoicePaymentRequest.setStatus(status);
        invoicePaymentRequest.setReason(reason);
        String uiProcessorReference = application.addUserInterfaceProcessor(new InvoicePayNowProcessing(invoicePaymentRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(false);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");

    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onProgressComplete() {

    }
}
