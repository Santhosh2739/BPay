package com.bookeey.wallet.live.application;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.login.FingerprintModule;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import dagger.ObjectGraph;
import newflow.NewFlowOfferNew;
import newflow.OffersNewFlow;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeFinalResponsePojo;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeInitiationResponsePojo;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.offers.OfferDetails;
import ycash.wallet.json.pojo.offers.OfferFinalResponse;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;
import ycash.wallet.json.pojo.offers.OfferResponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;
import ycash.wallet.json.pojo.txnhistory.TransactionHistoryResponse;
import ycash.wallet.json.pojo.userinfo.UserInfoResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.CardDetails;
import ycash.wallet.json.pojo.virtualprepaidcards.PrepaidCardsListResponse;
import ycash.wallet.json.pojo.virtualprepaidcards.RequestCardResponse;
import ycash.wallet.json.pojo.wheretopay.BranchDetailsPojo;
import ycash.wallet.json.pojo.wheretopay.CategoryDetailsListPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;
/**
 * Created by mohit on 01-06-2015.
 */
public class CoreApplication extends Application{


    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private ObjectGraph mObjectGraph;
    private static final String TAG = CoreApplication.class.getSimpleName();



    private CustomerLoginRequestReponse customerLoginRequestReponse = new CustomerLoginRequestReponse();
    private UserInfoResponse userInfoResponse = new UserInfoResponse();
    private MerchantListResponse merchantListResponse= new MerchantListResponse();
    private MerchantListResponse merchantListResponse_1= new MerchantListResponse();
    private OfferResponse offerResponse= new OfferResponse();
    private OfferPreviewResponse offerPreviewResponse= new OfferPreviewResponse();

    public NewFlowOfferNew getNewflow_offer_details() {
        return newflow_offer_details;
    }

    public void setNewflow_offer_details(NewFlowOfferNew newflow_offer_details) {
        this.newflow_offer_details = newflow_offer_details;
    }

    private NewFlowOfferNew newflow_offer_details = new NewFlowOfferNew();
    private MerchantListResponse merchantListResponse_category= new MerchantListResponse();
    private OfferPreviewResponse myoOfferPreviewResponse= new OfferPreviewResponse();
    private String categoryname;
    private String categoryId;
    private String imagePath;
    private String merchantName;
    private String offerName;
    private String newOfferCount;
    private String activeOfferCount;
    private String merchantimage;
    private PrepaidCardsListResponse prepaidCardsListResponse;
    private String offerId;
    private String cardName;
    private String cardImage;
    private Integer cardId;
    private String cardPrice;
    private RequestCardResponse requestCardResponse;
    private List<CardDetails> cardDetailsList;

    private String operatorImage;
    private String operatorName;
    private String operatorType;
    private String denominationsAmount;

    private int invoices_count;
    private boolean speakstatus;

    public BigDecimal getBpoints() {
        return bpoints;
    }

    public void setBpoints(BigDecimal bpoints) {
        this.bpoints = bpoints;
    }

    private BigDecimal bpoints;


    private OfferFinalResponse offerFinalResponse;

    public OffersNewFlow getOfferNewFlowFinalResponse() {
        return offerNewFlowFinalResponse;
    }

    public void setOfferNewFlowFinalResponse(OffersNewFlow offerNewFlowFinalResponse) {
        this.offerNewFlowFinalResponse = offerNewFlowFinalResponse;
    }
    private OffersNewFlow offerNewFlowFinalResponse;

    private ArrayList<String> bannerDetails;

    public ArrayList<String> getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(ArrayList<String> bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public boolean isSpeakstatus() {
        return speakstatus;
    }

    public void setSpeakstatus(boolean speakstatus) {
        this.speakstatus = speakstatus;
    }

    public int getInvoices_count() {
        return invoices_count;
    }

    public void setInvoices_count(int invoices_count) {
        this.invoices_count = invoices_count;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getDenominationsAmount() {
        return denominationsAmount;
    }

    public void setDenominationsAmount(String denominationsAmount) {
        this.denominationsAmount = denominationsAmount;
    }


    public String getOperatorImage() {
        return operatorImage;
    }

    public void setOperatorImage(String operatorImage) {
        this.operatorImage = operatorImage;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }



    public List<CardDetails> getCardDetailsList() {
        return cardDetailsList;
    }

    public void setCardDetailsList(List<CardDetails> cardDetailsList) {
        this.cardDetailsList = cardDetailsList;
    }

    public RequestCardResponse getRequestCardResponse() {
        return requestCardResponse;
    }

    public void setRequestCardResponse(RequestCardResponse requestCardResponse) {
        this.requestCardResponse = requestCardResponse;
    }

    public String getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(String cardPrice) {
        this.cardPrice = cardPrice;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public PrepaidCardsListResponse getPrepaidCardsListResponse() {
        return prepaidCardsListResponse;
    }

    public void setPrepaidCardsListResponse(PrepaidCardsListResponse prepaidCardsListResponse) {
        this.prepaidCardsListResponse = prepaidCardsListResponse;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
    private List<OfferDetails> offerDetails;

    public List<OfferDetails> getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(List<OfferDetails> offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getMerchantimage() {
        return merchantimage;
    }

    public void setMerchantimage(String merchantimage) {
        this.merchantimage = merchantimage;
    }

    public String getNewOfferCount() {
        return newOfferCount;
    }

    public void setNewOfferCount(String newOfferCount) {
        this.newOfferCount = newOfferCount;
    }

    public String getActiveOfferCount() {
        return activeOfferCount;
    }

    public void setActiveOfferCount(String activeOfferCount) {
        this.activeOfferCount = activeOfferCount;
    }

    private BranchDetailsPojo branchDetailsPojo= new BranchDetailsPojo();
    private String branchName;
    private String branchLocation;
    private String country_flag;
    private String country_code;
    private String country_name;
    private String topup_mobile_number;
    private String offer_type;
    private int position_offer;

    public int getPosition_offer() {
        return position_offer;
    }

    public void setPosition_offer(int position_offer) {
        this.position_offer = position_offer;
    }

    public OfferFinalResponse getOfferFinalResponse() {
        return offerFinalResponse;
    }

    public void setOfferFinalResponse(OfferFinalResponse offerFinalResponse) {
        this.offerFinalResponse = offerFinalResponse;
    }

    public String getOffer_type() {
        return offer_type;
    }
    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }
    public String getTopup_mobile_number() {
        return topup_mobile_number;
    }
    public void setTopup_mobile_number(String topup_mobile_number) {
        this.topup_mobile_number = topup_mobile_number;
    }
    private InternationalRechargeFinalResponsePojo internationalRechargeFinalResponsePojo= new InternationalRechargeFinalResponsePojo();
    public InternationalRechargeFinalResponsePojo getInternationalRechargeFinalResponsePojo() {
        return internationalRechargeFinalResponsePojo;
    }
    public void setInternationalRechargeFinalResponsePojo(InternationalRechargeFinalResponsePojo internationalRechargeFinalResponsePojo) {
        this.internationalRechargeFinalResponsePojo = internationalRechargeFinalResponsePojo;
    }
    public String getCountry_name() {
        return country_name;
    }
    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
    public String getCountry_code() {
        return country_code;
    }
    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }
    public String getCountry_flag() {
        return country_flag;
    }
    public void setCountry_flag(String country_flag) {
        this.country_flag = country_flag;
    }
    public OfferPreviewResponse getMyoOfferPreviewResponse() {
        return myoOfferPreviewResponse;
    }
    public void setMyoOfferPreviewResponse(OfferPreviewResponse myoOfferPreviewResponse) {
        this.myoOfferPreviewResponse = myoOfferPreviewResponse;
    }
    public MerchantListResponse getMerchantListResponse_1() {
        return merchantListResponse_1;
    }
    public void setMerchantListResponse_1(MerchantListResponse merchantListResponse_1) {
        this.merchantListResponse_1 = merchantListResponse_1;
    }
    public MerchantListResponse getMerchantListResponse_category() {
        return merchantListResponse_category;
    }
    public void setMerchantListResponse_category(MerchantListResponse merchantListResponse_category) {
        this.merchantListResponse_category = merchantListResponse_category;
    }
    public BranchDetailsPojo getBranchDetailsPojo() {
        return branchDetailsPojo;
    }
    public void setBranchDetailsPojo(BranchDetailsPojo branchDetailsPojo) {
        this.branchDetailsPojo = branchDetailsPojo;
    }
    public String getMerchantName() {
        return merchantName;
    }
    public String offer_merchantName;

    public String getOffer_merchantName() {
        return offer_merchantName;
    }

    public void setOffer_merchantName(String offer_merchantName) {
        this.offer_merchantName = offer_merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public String getBranchName() {
        return branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public String getBranchLocation() {
        return branchLocation;
    }
    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }
    public OfferResponse getOfferResponse() {
        return offerResponse;
    }
    public void setOfferResponse(OfferResponse offerResponse) {
        this.offerResponse = offerResponse;
    }
    private boolean isUserLoggedIn = false;
    private HashMap<String, UserInterfaceBackgroundProcessing> processingHolder = new HashMap<String, UserInterfaceBackgroundProcessing>();

    private TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse();
    private TransactionLimitResponse transactionLimitResponse= new TransactionLimitResponse();
    private CategoryDetailsListPojo categoryDetailsListPojo= new CategoryDetailsListPojo();
    private InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo= new InternationalRechargeInitiationResponsePojo();
    public InternationalRechargeInitiationResponsePojo getInternationalRechargeInitiationResponsePojo() {
        return internationalRechargeInitiationResponsePojo;
    }
    public void setInternationalRechargeInitiationResponsePojo(InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo) {
        this.internationalRechargeInitiationResponsePojo = internationalRechargeInitiationResponsePojo;
    }
    public CategoryDetailsListPojo getCategoryDetailsListPojo() {
        return categoryDetailsListPojo;
    }
    List<CategoryDetailsListPojo> categoryDetails;
    public List<CategoryDetailsListPojo> getCategoryDetails() {
        return categoryDetails;
    }
    public void setCategoryDetails(List<CategoryDetailsListPojo> categoryDetails) {
        this.categoryDetails = categoryDetails;
    }
    public void setCategoryDetailsListPojo(CategoryDetailsListPojo categoryDetailsListPojo) {
        this.categoryDetailsListPojo = categoryDetailsListPojo;
    }
    public OfferPreviewResponse getOfferPreviewResponse() {
        return offerPreviewResponse;
    }
    public void setOfferPreviewResponse(OfferPreviewResponse offerPreviewResponse) {
        this.offerPreviewResponse = offerPreviewResponse;
    }
    public MerchantListResponse getMerchantListResponse() {
        return merchantListResponse;
    }
    public void setMerchantListResponse(MerchantListResponse merchantListResponse) {
        this.merchantListResponse = merchantListResponse;
    }
    public String getCategoryname() {
        return categoryname;
    }
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public TransactionLimitResponse getTransactionLimitResponse() {
        return transactionLimitResponse;
    }
    public void setTransactionLimitResponse(TransactionLimitResponse transactionLimitResponse) {
        this.transactionLimitResponse = transactionLimitResponse;
    }
    public String addUserInterfaceProcessor(UserInterfaceBackgroundProcessing uiProcessor){
        String random = UUID.randomUUID().toString();
        processingHolder.put(random, uiProcessor);
        return random;
    }

    public UserInterfaceBackgroundProcessing getUserInterfaceProcessor(String processid){
        return processingHolder.get(processid);
    }
    public UserInterfaceBackgroundProcessing removeUserInterfaceProcessor(String processid){
        return processingHolder.remove(processid);
    }
    public int getSizeOfUserInterfaceProcessors(){
        return processingHolder.size();
    }

    public CustomerLoginRequestReponse getCustomerLoginRequestReponse() {

        Log.e("BookeeyKill","Response: "+customerLoginRequestReponse);

        return customerLoginRequestReponse;
    }
    public void setCustomerLoginRequestReponse(CustomerLoginRequestReponse customerLoginRequestReponse) {
        this.customerLoginRequestReponse = customerLoginRequestReponse;
    }
    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }
    public void setIsUserLoggedIn(boolean isUserLoggedIn) {

        Log.e("CoreLoggedInKill",""+isUserLoggedIn);

        this.isUserLoggedIn = isUserLoggedIn;
        System.out.println("Logged in");
        if(isUserLoggedIn){
            initializeUserLoggedInStatusAlarmManager();
        }
    }
    public TransactionHistoryResponse getTransactionHistoryResponse() {
        return transactionHistoryResponse;
    }
    public void setTransactionHistoryResponse(TransactionHistoryResponse transactionHistoryResponse) {
        this.transactionHistoryResponse = transactionHistoryResponse;
    }
    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }
    public void setUserInfoResponse(UserInfoResponse userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }
    public void initializeUserLoggedInStatusAlarmManager(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, SyncService.class);
        alarmIntent.putExtra("type",SyncService.TYPE_USER_LOGGED_IN_STATUS);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 2*60*1000, pending);
    }
    public void cancelUserLoggedInStatusAlarmManager(){
        Intent alarmIntent = new Intent(this, SyncService.class);
        boolean alarmUp = (PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp){
            AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
            alarmMgr.cancel(pending);
        }
    }
    public String getThisDeviceUniqueAndroidId(){


        /******************* Don't use it given for POS jar and static QR code **************************/

        //For Test 1
        //98037944 ,Civil ID: 555555555555, Salam
//        android_id = "875cafcab9098646";
        /******************************************/



        String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_id=android_id.length()%2==1?android_id+"FA0A1":android_id;
//        android_id="0000000000022919";

        Log.e("Android ID: ",""+android_id);

        Log.e("deviceID Core: ",""+android_id);


//Without sign
//        17175451b6c5e59c
//        With signed
//        17175451b6c5e59c

//        97891913

        //On July 30 for screenshot purpose
//        android_id="00000229199";

        //Rawan
//        android_id = "565be6fb34f38dbd";

        //My Device from laptop installation
//        android_id = "f947fef8942eb5cf";

        //To test NewFlow  98037947,1234,Civil ID: 787878787878, Srinu garu
//        android_id = "875cafcab9098647";

//      android_id = "f947fef8942eb5cb";


        //For some random device Ahmad    for got the Password
//        android_id = "875cafcab9098649";


        //For some random device Ahmad and it is real device Nokia 2.1 mine
//        android_id = "f947fef8942eb5cf";



        //Demo  - Rawan 66141629 , Don't use it given to Merchant
//        android_id = "70e87117db081ca9";

        //Irfan production guest device ID
//        android_id = "f947fef8942eb5cg";


        //Guest Rahman demo device ID
//        android_id = "f947fef8942eb5k";

        //Guest Irfan demo rejected device ID
//        android_id = "1a5eec8dab4c1176";


        //Guest Irfan Production rejected device ID
//        android_id = "70e87117db081ca9";



        //For Test 2
        //98037946 ,Civil ID: 777777777777, Suresh
//        android_id = "a063942d9f7c871f";


        //Ranjith iPhone
//        android_id = "D009CB72A3574E8391F6EEDB9F992303";


        //Irfan Registered and not Approved
//        android_id ="F9468882A6B14E26A90568147505810B";




        /*******************************************/
//        66662222 - For device change Srinu
//        android_id = "70e87117db081ca8";
        /*******************************************/

        //PRODUCTION


        //Production reject case
//        android_id = "70e87117db081ca5";


        //Production Pending case
//        android_id = "70e87117db081ca8";



        //For random device demo
//        android_id = "70e87117db081ca5";

        //For random PRD
//        android_id = "70e87117db081cb5";


        //For demo Password changed from portal
//        android_id = "70e87117db081ab5";

        //For demo Password changed from portal
//        android_id = "70e87217db081ab5";


        //For Asif mobile for BWeb otps
//        android_id = "de0808eb181e6984";


        //For Random for PRD
//        android_id = "ae0808eb181e6984";

        //For Random for PRD
//        android_id = "ae0808eb181e6989";

        //Jan 06 for Rejected case
//        android_id =  "70e87117db081ca9";

        //Jan 07 new user test
//        android_id =  "71e87117db081ca9";

        //Jan 07 Account locked
//        android_id =  "70e87117db081ca9";

        //Jan 16 Account Rejected, 363255555
//        android_id =  "80e87117db081ca9";

        //Jan 22 Account Rejected, Reason(Other), 66141629
//        android_id =  "70e87117db081ca9";


        //Jan 22 Failure general error, 55555555
//        android_id =  "70e87117db081ca9";

        //Error in PRD Jan 25 abdul vahid shethwala
//        android_id =  "b37f360f153a30b7";


        //New device To check rejected case in PRD
//        android_id =  "a37f360f153a30b7";

        //New device To check offers case in Demo
//        android_id =  "a37f360f153a30c7";


        //New device To check loadmoney error in PRD
//        android_id =  "c37f361f153a30c9";

        //New device To in PRD
//        android_id =  "c37f362f153a30c4";

        //New device To in PRD for Nairy
//        android_id =  "70e87127db081ca9";

        //New device To in Demo - 69853454
//        android_id =  "70e87137db081ca9";


        //New device To in Demo for JMeter
//        android_id =  "70e87137db081cb9";


//        Apr 30
        //New device To in PRD for JMeter
//        android_id =  "70c87137ab081cb9";


        //Irfans PRD Signed Goolge play build
//        android_id =  "0d254fb96188c0e4";


        //New device To in Demo Irfan
//        android_id =  "70e87137db081ca9";


        //New device To in Demo to test Knet card details
//        android_id =  "70e97147db082ca9";

        //New device To in PRD to test by Rawan (Referred by, Invite friends)
//        android_id =  "70e97157db282ca9";


        //New device To in Demo to test by Rawan  (Referred by, Invite friends)
//        android_id =  "70e87159db282ca9";

        //New device To in PRD to test by Rawan  (Referred by, Invite friends)
//        android_id =  "70e97159db482ca7";

        //New device To in DEMO to take Registartion screenshot )
//        android_id =  "60e97149db492ca7";


        //New device To in Munee Gaming app
//        android_id =

        //New device To Gaming register SDK
//        android_id =  "75f02158dd396cb7";


//Save it locally
        CustomSharedPreferences.saveStringData(getApplicationContext(), android_id, CustomSharedPreferences.SP_KEY.DEVICE_ID);

        return android_id;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        initObjectGraph(new FingerprintModule(this));
        sAnalytics = GoogleAnalytics.getInstance(this);
    }
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }*/


    public void initObjectGraph(Object module) {
        mObjectGraph = module != null ? ObjectGraph.create(module) : null;
    }
    public void inject(Object object) {
        if (mObjectGraph == null) {
            // This usually happens during tests.
            Log.i(TAG, "Object graph is not initialized.");
            return;
        }
        mObjectGraph.inject(object);
    }

    //For session Expiry
//    public void touch() {
//
//        Log.e(TAG, "Session touch in CoreApp");
//        AppLockManager.getInstance().enableDefaultAppLockIfAvailable(CoreApplication.this);
//        AppLockManager.getInstance().updateTouch();
//    }
}