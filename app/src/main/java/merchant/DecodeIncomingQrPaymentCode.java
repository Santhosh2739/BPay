package merchant;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import com.bookeey.wallet.live.mainmenu.MainActivity;

import java.math.BigInteger;

import coreframework.barcodeclient.BcodeHeaderEncoder;
import coreframework.taskframework.BackgroundProcessingAbstractFilter;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.Hex;


/**
 * Created by mohit on 02-07-2015.
 */
public class DecodeIncomingQrPaymentCode implements UserInterfaceBackgroundProcessing {
    private String qr_code_data = null;
    private DecodedQrPojo decodedQrPojo;

    public DecodeIncomingQrPaymentCode(String qr_code_data){
        this.qr_code_data = qr_code_data;
    }
    @Override
    public void performTask() {

        Log.e("Decode Barcode data","Decode Barcode data");

        try{
            BigInteger bg = new BigInteger(this.qr_code_data.trim());
            byte[] encoded = bg.toByteArray();
            byte[] len_bytes_enc_length = new byte[4];
            System.arraycopy(encoded,encoded.length-4,len_bytes_enc_length,0,4);
            int length_of_enc_data = Hex.byteArrayToInt(len_bytes_enc_length);
            int length_of_header_data = encoded.length - length_of_enc_data -4;

            byte[] header = new byte[length_of_header_data];
            System.arraycopy(encoded,0,header,0,length_of_header_data);

            byte[] enciphered = new byte[length_of_enc_data];
            System.arraycopy(encoded,length_of_header_data,enciphered,0,length_of_enc_data);

            BcodeHeaderEncoder bcodeHeaderEncoder = new BcodeHeaderEncoder(header);

            decodedQrPojo = new DecodedQrPojo(encoded,header,enciphered,bcodeHeaderEncoder);

        }catch (Exception e){
            Log.e("Barcode data decode Ex"," "+e.getMessage());
            decodedQrPojo = null;
        }
    }

    @Override
    public void handleSessionInvalid(Activity activity, ProgressDialogFrag dialogueFragment) {

    }

    @Override
    public String captureURL() {
        return null;
    }
    @Override
    public void processResponse(Message msg) {

    }
    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        dialogueFragment.dismiss();
        ((MainActivity)activity).onBarcodeDecoded(decodedQrPojo);
    }
    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    public void preProcessResponse(Message msg) {

    }

    @Override
    public void prePerformUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {

    }

    @Override
    public boolean isLocalProcess() {
        return true;
    }
}
