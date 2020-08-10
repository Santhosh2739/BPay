package coreframework.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bookeey.wallet.live.registration.GreetingsActivity;

import javax.net.ssl.HttpsURLConnection;

//import coreframework.securityutils.KeyPinStore;

import coreframework.securityutils.KeyPinStore;


//import static coreframework.network.ServerConnection.publiKeyPinning_test;


public class ServerConnection implements Runnable {
    int action_code;
    Handler handler;
    String url;
    boolean isLongOperation = false;
    public static final int OPERATION_SUCCESS = 1001;
    public static final int OPERATION_FAILURE_NETWORK = 1002;
    public static final int OPERATION_FAILURE_GENERAL_SERVER = 1003;
    private Context context = null;


    public ServerConnection(int _action_code, Handler _handler, String _connectionURL,Context _context) {
        this.action_code = _action_code;
        this.handler = _handler;
        this.url = _connectionURL;
        this.context=_context;
    }

    public ServerConnection(int _action_code, Handler _handler, String _connectionURL, boolean isLongOperation,Context _context) {
        this.action_code = _action_code;
        this.handler = _handler;
        this.url = _connectionURL;
        this.isLongOperation = isLongOperation;
        this.context=_context;
    }

    @Override
    public void run() {
        BufferedReader buffer = null;
        int _statuscode = 0;
//        KeyPinStore keystore = null;
//        boolean validation = true;
//        HttpsURLConnection urlConnection = null;
//        try {
//            keystore = KeyPinStore.getInstance(context);
//            URL public_pin_url = new URL(url);
//            urlConnection = (HttpsURLConnection) public_pin_url.openConnection();
//            urlConnection.setSSLSocketFactory(keystore.getContext().getSocketFactory());
//        } catch (CertificateException e) {
//            validation = false;
//            StringBuffer responseBuffer = new StringBuffer();
//            String s = "";
//            responseBuffer.append(e.toString());
//            Message msg = new Message();
//            msg.what = action_code;
//            msg.arg1 = OPERATION_SUCCESS;
//            msg.arg2 = _statuscode;
//            msg.obj = responseBuffer.toString();
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        } catch (IOException e) {
//            validation = false;
//            StringBuffer responseBuffer = new StringBuffer();
//            String s = "";
//            responseBuffer.append(e.toString());
//            Message msg = new Message();
//            msg.what = action_code;
//            msg.arg1 = OPERATION_SUCCESS;
//            msg.arg2 = _statuscode;
//            msg.obj = responseBuffer.toString();
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            validation = false;
//            StringBuffer responseBuffer = new StringBuffer();
//            String s = "";
//            responseBuffer.append(e.toString());
//            Message msg = new Message();
//            msg.what = action_code;
//            msg.arg1 = OPERATION_SUCCESS;
//            msg.arg2 = _statuscode;
//            msg.obj = responseBuffer.toString();
//            handler.sendMessage(msg);
//        } catch (NoSuchAlgorithmException e) {
//            validation = false;
//            StringBuffer responseBuffer = new StringBuffer();
//            String s = "";
//            responseBuffer.append(e.toString());
//            Message msg = new Message();
//            msg.what = action_code;
//            msg.arg1 = OPERATION_SUCCESS;
//            msg.arg2 = _statuscode;
//            msg.obj = responseBuffer.toString();
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            validation = false;
//            StringBuffer responseBuffer = new StringBuffer();
//            String s = "";
//            responseBuffer.append(e.toString());
//            Message msg = new Message();
//            msg.what = action_code;
//            msg.arg1 = OPERATION_SUCCESS;
//            msg.arg2 = _statuscode;
//            msg.obj = responseBuffer.toString();
//            handler.sendMessage(msg);
//            e.printStackTrace();
//        }
//        if (validation)
            try {
                HttpResponse response;

                HttpGet httpGet = new HttpGet(url);

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = 15000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SfO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 40000;
                if (isLongOperation) {
                    timeoutSocket = 40000;
                }
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
                DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
                httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
                response = httpClient.execute(httpGet);


                _statuscode = response.getStatusLine().getStatusCode();
                //Log.d(ServerConnection.class.getCanonicalName(), "statusCode::"+_statuscode);
                if (_statuscode != HttpStatus.SC_OK) {
                    throw new IllegalStateException();
                }
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                StringBuffer responseBuffer = new StringBuffer();
                buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    responseBuffer.append(s);
                }
                buffer.close();
                Message msg = new Message();
                msg.what = action_code;
                msg.arg1 = OPERATION_SUCCESS;
                msg.arg2 = _statuscode;
                msg.obj = responseBuffer.toString();
                handler.sendMessage(msg);
            } catch (IllegalStateException illegalEx) {
                Message msg = new Message();
                msg.what = action_code;
                msg.arg1 = OPERATION_FAILURE_GENERAL_SERVER;
                msg.arg2 = _statuscode;
                handler.sendMessage(msg);
                //interfaceobject.onerrorresponse(int code, int errorcode)
            } catch (Exception eGeneral) {
                //Log.d(getClass().getSimpleName(), "GeneralStateHTTP Connection Error:--"+eGeneral);
                Message msg = new Message();
                msg.what = action_code;
                msg.arg1 = OPERATION_FAILURE_NETWORK;
                msg.arg2 = _statuscode;
                handler.sendMessage(msg);
                //interfaceobject.onerrorresponse(int code, int errorcode)
            } finally {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException io) {
                        //Log.e(getClass().getSimpleName(), "error shutting down connection "+io);
                    }
                }
            }
    }

    private static List<NameValuePair> getQueryParams(String url) throws UnsupportedEncodingException {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length < 2) {
            return nameValuePair;
        }
        String query = urlParts[1];
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if (pair.length > 1) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }
            // skip ?& and &&
            if ("".equals(key) && pair.length == 1) {
                continue;
            }
            nameValuePair.add(new BasicNameValuePair(key, value));
        }
        return nameValuePair;
    }

    private static Message directNonHandlerHTTPPostClient(final String getURL, int action_code, DefaultHttpClient httpClient) {
        BufferedReader buffer = null;
        int _statuscode = 0;
        try {
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 15000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 50000;

            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
//			httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            String baseURL = getURL.substring(0, getURL.indexOf("?"));
            List<NameValuePair> nameValuePair = getQueryParams(getURL);
            httpClient = new DefaultHttpClient(httpParameters);
            httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpPost httpPost = new HttpPost(baseURL);
            //Encoding POST data
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpClient.execute(httpPost);

            //HttpResponse response = null;
           /* InputStream in;
            URI newURI = URI.create(baseURL);
            HttpPost postMethod = new HttpPost(newURI);
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePair));
            HttpResponse response = httpClient1.execute(postMethod);*/


            _statuscode = response.getStatusLine().getStatusCode();
            //Log.d(ServerConnection.class.getCanonicalName(), "statusCode::"+_statuscode);
            if (_statuscode != HttpStatus.SC_OK) {
                throw new IllegalStateException();
            }
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            StringBuffer responseBuffer = new StringBuffer();
            buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                responseBuffer.append(s);
            }
            buffer.close();
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_SUCCESS;
            msg.arg2 = _statuscode;
            msg.obj = responseBuffer.toString();
            return msg;
        } catch (IllegalStateException illegalEx) {
            //Log.d(getClass().getSimpleName(), "IllegalStateHTTP Connection Error:--"+illegalEx);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_GENERAL_SERVER;
            msg.arg2 = _statuscode;
            return msg;
        } catch (Exception eGeneral) {
            //Log.d(getClass().getSimpleName(), "GeneralStateHTTP Connection Error:--"+eGeneral);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_NETWORK;
            msg.arg2 = _statuscode;
            return msg;
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException io) {
                    //Log.e(getClass().getSimpleName(), "error shutting down connection "+io);
                }
            }
        }

    }

    public static Message executeHTTPRequest(final String url, int action_code, boolean isPost, DefaultHttpClient httpClient) {
        if (isPost) {
            return directNonHandlerHTTPPostClient(url, action_code, httpClient);
        } else {
            return directNonHandlerHTTPClient(url, action_code, httpClient);
        }
    }

    private static Message directNonHandlerHTTPClient(final String url, int action_code, DefaultHttpClient httpClient) {
        BufferedReader buffer = null;
        int _statuscode = 0;
        try {

            HttpGet httpGet = new HttpGet(url);
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
            httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpResponse response = httpClient.execute(httpGet);

            _statuscode = response.getStatusLine().getStatusCode();
            //Log.d(ServerConnection.class.getCanonicalName(), "statusCode::"+_statuscode);
            if (_statuscode != HttpStatus.SC_OK) {
                throw new IllegalStateException();
            }
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            StringBuffer responseBuffer = new StringBuffer();
            buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                responseBuffer.append(s);
            }
            buffer.close();
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_SUCCESS;
            msg.arg2 = _statuscode;
            msg.obj = responseBuffer.toString();
            return msg;
        } catch (IllegalStateException illegalEx) {
            //Log.d(getClass().getSimpleName(), "IllegalStateHTTP Connection Error:--"+illegalEx);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_GENERAL_SERVER;
            msg.arg2 = _statuscode;
            return msg;
        } catch (Exception eGeneral) {
            //Log.d(getClass().getSimpleName(), "GeneralStateHTTP Connection Error:--"+eGeneral);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_NETWORK;
            msg.arg2 = _statuscode;
            return msg;
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException io) {
                    //Log.e(getClass().getSimpleName(), "error shutting down connection "+io);
                }
            }
        }
    }

    //TODO: Remove Later and mapp to new HTTP OPEN CONNECTION APIS IN ANDROID
    public synchronized static Message directNonHandlerHTTPClient(final String url, int action_code) {
        BufferedReader buffer = null;
        int _statuscode = 0;
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpParams httpParameters = new BasicHttpParams();
            // Set the timeout in milliseconds until a connection is established.
            // The default value is zero, that means the timeout is not used.
            int timeoutConnection = 15000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 40000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
            HttpResponse response = httpClient.execute(httpGet);
            _statuscode = response.getStatusLine().getStatusCode();
            //Log.d(ServerConnection.class.getCanonicalName(), "statusCode::"+_statuscode);
            if (_statuscode != HttpStatus.SC_OK) {
                throw new IllegalStateException();
            }
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            StringBuffer responseBuffer = new StringBuffer();
            buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                responseBuffer.append(s);
            }
            buffer.close();
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_SUCCESS;
            msg.arg2 = _statuscode;
            msg.obj = responseBuffer.toString();
            return msg;
        } catch (IllegalStateException illegalEx) {
            //Log.d(getClass().getSimpleName(), "IllegalStateHTTP Connection Error:--"+illegalEx);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_GENERAL_SERVER;
            msg.arg2 = _statuscode;
            return msg;
        } catch (Exception eGeneral) {
            //Log.d(getClass().getSimpleName(), "GeneralStateHTTP Connection Error:--"+eGeneral);
            Message msg = new Message();
            msg.what = action_code;
            msg.arg1 = OPERATION_FAILURE_NETWORK;
            msg.arg2 = _statuscode;
            return msg;
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException io) {
                    //Log.e(getClass().getSimpleName(), "error shutting down connection "+io);
                }
            }
        }
    }
}