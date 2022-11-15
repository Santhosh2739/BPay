package com.bookeey.wallet.live.application;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.bookeey.wallet.live.R;
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
public class CoreApplication extends Application {
    private static final String TAG = CoreApplication.class.getSimpleName();
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    public String offer_merchantName;
    List<CategoryDetailsListPojo> categoryDetails;
    private ObjectGraph mObjectGraph;
    private CustomerLoginRequestReponse customerLoginRequestReponse = new CustomerLoginRequestReponse();
    private UserInfoResponse userInfoResponse = new UserInfoResponse();
    private MerchantListResponse merchantListResponse = new MerchantListResponse();
    private MerchantListResponse merchantListResponse_1 = new MerchantListResponse();
    private OfferResponse offerResponse = new OfferResponse();
    private OfferPreviewResponse offerPreviewResponse = new OfferPreviewResponse();
    private NewFlowOfferNew newflow_offer_details = new NewFlowOfferNew();
    private MerchantListResponse merchantListResponse_category = new MerchantListResponse();
    private OfferPreviewResponse myoOfferPreviewResponse = new OfferPreviewResponse();
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
    private BigDecimal bpoints;
    private OfferFinalResponse offerFinalResponse;
    private OffersNewFlow offerNewFlowFinalResponse;
    private ArrayList<String> bannerDetails;
    private List<OfferDetails> offerDetails;
    private BranchDetailsPojo branchDetailsPojo = new BranchDetailsPojo();
    private String branchName;
    private String branchLocation;
    private String country_flag;
    private String country_code;
    private String country_name;
    private String topup_mobile_number;
    private String offer_type;
    private int position_offer;
    private InternationalRechargeFinalResponsePojo internationalRechargeFinalResponsePojo = new InternationalRechargeFinalResponsePojo();
    private boolean isUserLoggedIn = false;
    private final HashMap<String, UserInterfaceBackgroundProcessing> processingHolder = new HashMap<String, UserInterfaceBackgroundProcessing>();
    private TransactionHistoryResponse transactionHistoryResponse = new TransactionHistoryResponse();
    private TransactionLimitResponse transactionLimitResponse = new TransactionLimitResponse();
    private CategoryDetailsListPojo categoryDetailsListPojo = new CategoryDetailsListPojo();
    private InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo = new InternationalRechargeInitiationResponsePojo();

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
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

    public NewFlowOfferNew getNewflow_offer_details() {
        return newflow_offer_details;
    }

    public void setNewflow_offer_details(NewFlowOfferNew newflow_offer_details) {
        this.newflow_offer_details = newflow_offer_details;
    }

    public BigDecimal getBpoints() {
        return bpoints;
    }

    public void setBpoints(BigDecimal bpoints) {
        this.bpoints = bpoints;
    }

    public OffersNewFlow getOfferNewFlowFinalResponse() {
        return offerNewFlowFinalResponse;
    }

    public void setOfferNewFlowFinalResponse(OffersNewFlow offerNewFlowFinalResponse) {
        this.offerNewFlowFinalResponse = offerNewFlowFinalResponse;
    }

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

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

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

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOffer_merchantName() {
        return offer_merchantName;
    }

    public void setOffer_merchantName(String offer_merchantName) {
        this.offer_merchantName = offer_merchantName;
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

    public InternationalRechargeInitiationResponsePojo getInternationalRechargeInitiationResponsePojo() {
        return internationalRechargeInitiationResponsePojo;
    }

    public void setInternationalRechargeInitiationResponsePojo(InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo) {
        this.internationalRechargeInitiationResponsePojo = internationalRechargeInitiationResponsePojo;
    }

    public CategoryDetailsListPojo getCategoryDetailsListPojo() {
        return categoryDetailsListPojo;
    }

    public void setCategoryDetailsListPojo(CategoryDetailsListPojo categoryDetailsListPojo) {
        this.categoryDetailsListPojo = categoryDetailsListPojo;
    }

    public List<CategoryDetailsListPojo> getCategoryDetails() {
        return categoryDetails;
    }

    public void setCategoryDetails(List<CategoryDetailsListPojo> categoryDetails) {
        this.categoryDetails = categoryDetails;
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

    public String addUserInterfaceProcessor(UserInterfaceBackgroundProcessing uiProcessor) {
        String random = UUID.randomUUID().toString();
        processingHolder.put(random, uiProcessor);
        return random;
    }

    public UserInterfaceBackgroundProcessing getUserInterfaceProcessor(String processid) {
        return processingHolder.get(processid);
    }

    public UserInterfaceBackgroundProcessing removeUserInterfaceProcessor(String processid) {
        return processingHolder.remove(processid);
    }

    public int getSizeOfUserInterfaceProcessors() {
        return processingHolder.size();
    }

    public CustomerLoginRequestReponse getCustomerLoginRequestReponse() {
        Log.e("BookeeyKill", "Response: " + customerLoginRequestReponse);
        return customerLoginRequestReponse;
    }

    public void setCustomerLoginRequestReponse(CustomerLoginRequestReponse customerLoginRequestReponse) {
        this.customerLoginRequestReponse = customerLoginRequestReponse;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        Log.e("CoreLoggedInKill", "" + isUserLoggedIn);
        this.isUserLoggedIn = isUserLoggedIn;
        System.out.println("Logged in");
        if (isUserLoggedIn) {
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

    public void initializeUserLoggedInStatusAlarmManager() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, SyncService.class);
        alarmIntent.putExtra("type", SyncService.TYPE_USER_LOGGED_IN_STATUS);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 2 * 60 * 1000, pending);
    }

    public void cancelUserLoggedInStatusAlarmManager() {
        Intent alarmIntent = new Intent(this, SyncService.class);
        boolean alarmUp = (PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp) {
            AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
            alarmMgr.cancel(pending);
        }
    }

    public String getThisDeviceUniqueAndroidId() {
        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_id = android_id.length() % 2 == 1 ? android_id + "FA0A1" : android_id;
        //android_id = "0000000345678229190";
        //android_id="9a050ddd94da20aa";
        //android_id="c05da65cd8907d4f";
        Log.e("deviceID Core: ", "" + android_id);
        CustomSharedPreferences.saveStringData(getApplicationContext(), android_id, CustomSharedPreferences.SP_KEY.DEVICE_ID);
        return android_id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //initObjectGraph(new FingerprintModule(this));
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

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

}