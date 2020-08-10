package coreframework.securityutils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.registration.GreetingsActivity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import nostra13.universalimageloader.utils.IoUtils;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ricardo Iramar dos Santos on 14/08/2015.
 */
public class KeyPinStore {

    private static KeyPinStore instance = null;
    private SSLContext sslContext;
    private static Context context;

    public static synchronized KeyPinStore getInstance(Context _context) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        context = _context;

        if (instance == null) {
            instance = new KeyPinStore();
        }
        return instance;
    }

    private KeyPinStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // https://developer.android.com/training/articles/security-ssl.html
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // randomCA.crt should be in the Assets directory (tip from here http://littlesvr.ca/grumble/2014/07/21/android-programming-connect-to-an-https-server-with-self-signed-certificate/)

        InputStream caInput = null;
        try {

            if(context!=null) {

                caInput = new BufferedInputStream(context.getAssets().open("bookey.cer"));

            }


        } catch (Exception e) {
            e.printStackTrace();

            Log.e("KeyPinStore Ex: "," "+e.getMessage());
        }
        //InputStream caInput = new BufferedInputStream(GreetingsActivity.context.getAssets().open("bookey.cer"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            if(caInput!=null) {
                caInput.close();
            }
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        //UnifiedTrustManager trustManager = new UnifiedTrustManager(keyStoreType);


        // Create an SSLContext that uses our TrustManager
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

       /* URL url = new URL("https://www.bookeey.com/mno/");
        HttpsURLConnection  urlConnection = (HttpsURLConnection) url.openConnection();
       // urlConnection.setHostnameVerifier(hostnameVerifier);

        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
        //copyInputStreamToOutputStream(in, System.out);*/

        X509TrustManager xtm = (X509TrustManager) tmf.getTrustManagers()[0];
        for (X509Certificate cert : xtm.getAcceptedIssuers()) {
            String certStr = "S:" + cert.getSubjectDN().getName() + "\nIIIIIII:"
                    + cert.getIssuerDN().getName();
            Log.d("TAG", certStr);
        }
       // sslContext.init(null,  new TrustManager[]{(TrustManager) trustManager}, null);



        //HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

    }






    public SSLContext getContext() {
        return sslContext;
    }
}
