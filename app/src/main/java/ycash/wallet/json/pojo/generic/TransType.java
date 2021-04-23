package ycash.wallet.json.pojo.generic;

/**
 * THIS IS NOT A POJO
 * Co
 *
 * @author mohit
 */
public enum TransType {


    /*------------------------------------LOCAL SERVER--------------------------------------------*/
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.8.178:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.8.178:8089/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.8.178:8089/portal/rechargelimits"),


    //Bookeey network 1
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.8.178:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.8.178:8089/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.8.178:8089/portal/rechargelimits"),


    //Bookeey network 2
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.8.137:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.8.137:8089/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.8.137:8089/portal/rechargelimits"),


//Mobile network
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.43.188:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.43.188:8089/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.43.188:8089/portal/rechargelimits"),


    //Some promotional video Sep 09

    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.8.140:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.8.140:8089/mno/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.8.140:8089/mno/portal/rechargelimits"),


//    Juny 04
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
//    ADDRESS_BASE("http://192.168.200.245:8089/mno/"),
//    LOADMONEY_REQUEST("http://192.168.200.245:8089/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("http://192.168.200.245:8089/portal/rechargelimits"),





    /*-----------------------------------DEMO SERVER----------------------------------------------*/
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)

    //LOCAL

//    ADDRESS_BASE("http://83.96.116.3:8089/mno/"),
//    ADDRESS_BASE("http://192.168.2.27:8089/mno/"),

//    ADDRESS_BASE("http://192.168.43.188:8089/mno/"),

//    ADDRESS_BASE("http://192.168.2.24:8089/mno/"),

    //Demo

//    ADDRESS_BASE("http://83.96.116.3:8089/mno"),
//        ADDRESS_BASE("http://83.96.116.3:8089/mno/"),


//    //New flow LOCAL
//    NEW_ADDRESS_BASE("http:///83.96.116.3:8089/"),
//NEW_ADDRESS_BASE("http://192.168.2.27:8089/"),
//NEW_ADDRESS_BASE("http://192.168.43.188:8089/"),
//    NEW_ADDRESS_BASE("http://192.168.2.24:8089/"),




    /*-----------------------------------UAT TEST SERVER------------------------------------------*/
    //Please comment 79 to 147 lines of code in ServerConnection.class(for secure connection)
   /*ADDRESS_BASE("http://103.195.186.249/mno/"),
   LOADMONEY_REQUEST("http://103.195.186.249/portal/proceed"),
   LOADMONEY_CHECK_REQUEST("http://103.195.186.249/portal/rechargelimits"),*/


    /*-------------------------------------PUBLIC SERVER------------------------------------------*/
    //Please UN_COMMENT 79 to 147 lines of code in ServerConnection.class(for secure connection)
//     ADDRESS_BASE("https://www.bookeey.com/mno/"),
//     LOADMONEY_REQUEST("https://www.bookeey.com/portal/proceed"),
//     LOADMONEY_CHECK_REQUEST("https://www.bookeey.com/portal/rechargelimits"),
    //New flow
//    NEW_ADDRESS_BASE("https://www.bookeey.com/"),


    //DEMO URLS July 12 2020

    ADDRESS_BASE("https://demo.bookeey.com/mno/"),
    LOADMONEY_REQUEST("https://demo.bookeey.com/portal/proceed"),
    LOADMONEY_CHECK_REQUEST("https://demo.bookeey.com/portal/rechargelimits"),
    NEW_ADDRESS_BASE("https://demo.bookeey.com/"),


//    PRODUCTION URLS July 12 2020
//
//    ADDRESS_BASE("https://api.bookeey.com/mno/"),
//    LOADMONEY_REQUEST("https://www.bookeey.com/portal/proceed"),
//    LOADMONEY_CHECK_REQUEST("https://www.bookeey.com/portal/rechargelimits"),
//    NEW_ADDRESS_BASE("https://www.bookeey.com/"),


    //--------------------------------------------Merchant URLS START--------------------------------------------------
    MX_ADDRESS_BASE("http://demo.bookeey.com/mno/ooredooServerRequest"),
    PAY_TO_MERCHANT_COMMIT_REQUEST(MX_ADDRESS_BASE.getURL()),
    PAY_TO_MERCHANT(MX_ADDRESS_BASE.getURL()),

    //--------------------------------------------Merchant URLS END--------------------------------------------------

    //    STATIC QR CODE MODEL
    SCAN_STATIC_QR_CODE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    SCAN_STATIC_QR_CODE_RESPONSE,

    //    STATIC QR CODE CONFIRM
    SCAN_STATIC_QR_CODE_CONFIRMATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    SCAN_STATIC_QR_CODE_CONFIRMATION_RESPONSE,

    SEND_MONEY_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    SEND_MONEY_COMMIT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    //    PAY_TO_MERCHANT(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    SEND_MONEY_TO_BANK(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    GET_USER_DETAILS(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    LOGIN_MERCHANT(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    REGISTER(ADDRESS_BASE.getURL() + "customer/registerMobileWallet"),
    REGISTER_ACTIVATION(ADDRESS_BASE.getURL() + "customer/updateLoginPin"),
    LOGIN_CUSTOMER(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    CHECK_BY_MOBILE_NUMBER(ADDRESS_BASE.getURL() + "customer/checkCustomerMobileNo"),
    PEER_TO_PEER(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PEER_TO_PEER_CONFORMATION(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PEER_TO_PEER_COMMIT(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    TRANSACTION_LIMITS(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    FORGOT_PIN_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    TRAN_HISTORY_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    USER_INFO_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    LOGOUT_CUSTOMER(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    VIEW_PROFILE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    P2M_BARCODEGEN_VALIDATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    //Forgot password device change
    FORGOT_DEVICECHANGEPASSWORD_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    //warbaRequest
    P2M_BARCODEGEN_WARBA_VALIDATION_REQUEST(ADDRESS_BASE.getURL() + "warbaRequest"),


    UPDATE_DEVICE_ID_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    UPDATE_MOBILENUMBER_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    WHERE_TO_PAY_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    WHERE_TO_PAY_LOGO_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    WHERE_TO_PAY_MERCHANTLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    WHERE_TO_PAY_MAPVIEW_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_NEWCOUNT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_ACTIVECOUNT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_DELETE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_BARCODE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_PREVIEW_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_SAVE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_MYACTIVE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INTERNATIONAL_RECHARGE_COUNTRYLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INTERNATIONAL_RECHARGE_DENOMINATIONLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INTERNATIONAL_RECHARGE_L1_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INTERNATIONAL_RECHARGE_L2_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PEERTOPEER_RECEIVER_MOBILELIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INTERNATIONAL_RECHARGE_RECIPIENTLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_NEWDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    P2M_BARCODECLOSE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_ACTIVEDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    APP_GREETING_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PREPAID_CATEGORYLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PREPAID_DENOMINATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PREPAID_REQUESTCARD_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PREPAID_CONFIRM_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    CHANGEPIN_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    PROFILE_UPDATE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    REJECT_EDIT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    REJECT_SUBMIT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    CHANGETPIN_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    DOMESTIC_RECHARGE_IMAGE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    DOMESTIC_RECHARGE_DENOMINATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    DOMESTIC_RECHARGE_L1_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    DOMESTIC_RECHARGE_L2_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    RESET_TPIN_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    VOCHER_IMAGE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    VOCHER_DENOMINATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    VOCHER_RECHARGE_L1_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    VOCHER_RECHARGE_L2_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    VOCHER_STORE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    //INVOICE_LIST_REQUEST("http://192.168.1.123:8089/mno/" + "ooredooServerRequest"),
    INVOICE_LIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INVOICE_LIST_RESPONSE,
    //INVOICE_PAYMENT_REQUEST("http://192.168.1.123:8089/mno/" + "ooredooServerRequest"),
    INVOICE_PAYMENT_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_BANNER_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    OFFER_BANNER_RESPONSE,

    BIO_LOGIN_CUSTOMER_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    BIO_LOGIN_CUSTOMER_RESPONSE,
    INVOICE_DETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    INVOICE_DETAILS_RESPONSE,


    //SHOW PUSH NOTIFICATION
    SHOW_PUSHNOTIFICATION_MESSAGE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    SHOW_PUSHNOTIFICATION_MESSAGE_RESPONSE,


    INVOICE_TRAN_RESPONSE,
    INVOICE_PAYMENT_RESPONSE,
    VOCHER_STORE_RESPONSE,
    DOMESTIC_RECHARGE_DENOMINATION_RESPONSE,
    VOCHER_RECHARGE_L2_RESPONSE,
    VOCHER_RECHARGE_TRAN_RESPONSE,
    VOCHER_RECHARGE_L1_RESPONSE,
    VOCHER_DENOMINATION_RESPONSE,
    VOCHER_IMAGE_RESPONSE,
    CASHBACK,

    RESET_TPIN_RESPONSE,
    DOMESTIC_RECHARGE_IMAGE_RESPONSE,
    DOMESTIC_RECHARGE_L1_RESPONSE,
    DOMESTIC_RECHARGE_L2_RESPONSE,
    DOMESTIC_RECHARGE_TRAN_RESPONSE,

    CHANGETPIN_RESPONSE,
    REJECT_SUBMIT_RESPONSE,

    REJECT_EDIT_RESPONSE,
    PROFILE_UPDATE_RESPONSE,
    OFFER_NEWCOUNT_RESPONSE,
    PREPAID_REQUESTCARD_RESPONSE,
    INTERNATIONAL_RECHARGE_L1_RESPONSE,
    INTERNATIONAL_RECHARGE_RECIPIENTLIST_RESPONSE,
    INTERNATIONAL_RECHARGE_DENOMINATIONLIST_RESPONSE,
    INTERNATIONAL_RECHARGE_COUNTRYLIST_RESPONSE,
    PEERTOPEER_RECEIVER_MOBILELIST_RESPONSE,
    P2M_BARCODECLOSE_RESPONSE,
    PREPAID_DENOMINATION_RESPONSE,
    OFFER_ACTIVECOUNT_RESPONSE,
    LOADMONEY_RESPONSE,
    CHANGEPIN_RESPONSE,
    PREPAID_CONFIRM_RESPONSE,
    PREPAID_CATEGORYLIST_RESPONSE,
    ECOMMERCE_TRAN_RESPONSE,

    INTERNATIONAL_RECHARGE_L2_RESPONSE,
    UPDATE_DEVICE_ID_RESPONSE,

    OFFER_MYACTIVE_RESPONSE,
    WHERE_TO_PAY_LOGO_RESPONSE,
    WHERE_TO_PAY_MAPVIEW_RESPONSE,
    OFFER_BARCODE_RESPONSE,
    OFFER_RESPONSE,
    OFFER_SAVE_RESPONSE,
    WHERE_TO_PAY_RESPONSE,
    WHERE_TO_PAY_MERCHANT_BRANCHVIEW_RESPONSE,
    UPDATE_MOBILENUMBER_RESPONSE,
    P2M_BARCODEGEN_VALIDATION_RESPONSE,
    PEER_TO_PEER_CONFORMATION_RESPONSE,
    PEER_TO_PEER_RESPONSE,
    FORGOT_PIN_RESPONSE,
    FORGOT_DEVICECHANGEPASSWORD_RESPONSE,
    REGISTER_RESPONSE,
    OFFER_PREVIEW_RESPONSE,
    TRANSACTION_LIMITS_RESPONSE,
    REGISTER_ACTIVATION_RESPONSE,
    LOGIN_CUSTOMER_RESPONSE,
    CHECK_BY_MOBILE_NUMBER_RESPONSE,
    TRAN_HISTORY_RESPONSE,
    INTERNAL_SERVER_ERROR,
    INVALID_USER,
    FETCH_BALANCE_RESPONSE,
    SEND_MONEY_REQUEST_RESPONSE,
    SEND_MONEY_COMMIT_REQUEST_RESPONSE,
    PAY_TO_MERCHANT_RESPONSE,

    PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE,
    SEND_MONEY_TO_BANK_RESPONSE,
    USER_INFO_RESPONSE,
    PEER_TO_PEER_COMMIT_RESPONSE,
    WHERE_TO_PAY_CATEGORY_RESPONSE,
    LOGIN_MERCHANT_RESPONSE,
    TXN_LIMITS_DETAILS_RESPONSE,
    LOGOUT_CUSTOMER_RESPONSE,
    VIEW_PROFILE_RESPONSE,
    OFFER_NEWDETAILS_RESPONSE,
    OFFER_DELETE_RESPONSE,
    LOAD_MONEY,
    INTL_RECHARGE,
    OFFER_ACTIVEDETAILS_RESPONSE,
    APP_GREETING_RESPONSE,
    PREPAIDCARDS,
    P2P_RECEIVED,

    //Dec 17 ECom OTPs
    ECOM_OTP_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    ECOM_OTP_RESPONSE,


    //    NewFlow URLs


    //DOMESTIC RECHARGE Production
    NEW_DOMESTIC_RECHARGE_URL("portal/domesticRechargeproceed"),

    //PREPAID VOUCHER
    NEW_VOUCHER_RECHARGE_URL("portal/vocherproceed"),

    //PREPAID VOUCHER
    NEW_INTL_TOPUP_URL("portal/intlTopUpproceed"),


    //LOCAL
//    NEW_ADDRESS_BASE("http://192.168.2.27:8088/mnoadmin"),
//    NEW_DOMESTIC_RECHARGE_URL("/domesticRechargeproceed"),


    //NewFlow DomesticRecharges
    NEW_DOMESTIC_RECHARGE_IMAGE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_DOMESTIC_RECHARGE_IMAGE_RESPONSE(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_DOMESTIC_RECHARGE_DENOMINATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_DOMESTIC_RECHARGE_DENOMINATION_RESPONSE(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_DOMESTIC_RECHARGE_L1_REQUEST(NEW_ADDRESS_BASE.getURL() + NEW_DOMESTIC_RECHARGE_URL.getURL()),
    NEW_DOMESTIC_RECHARGE_L1_RESPONSE(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
//    NEW_DOMESTIC_RECHARGE_L2_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
//    NEW_DOMESTIC_RECHARGE_L2_RESPONSE(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    //NewFlow Voucher
    NEW_VOCHER_IMAGE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_VOCHER_STORE_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_VOCHER_DENOMINATION_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_VOCHER_RECHARGE_L1_REQUEST(NEW_ADDRESS_BASE.getURL() + NEW_VOUCHER_RECHARGE_URL.getURL()),
//    VOCHER_RECHARGE_L2_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),

    //NewFlow Intl topup
    NEW_INTERNATIONAL_RECHARGE_COUNTRYLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_INTERNATIONAL_RECHARGE_COUNTRYLIST_RESPONSE,
    NEW_INTERNATIONAL_RECHARGE_DENOMINATIONLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_INTERNATIONAL_RECHARGE_DENOMINATIONLIST_RESPONSE,
    NEW_INTERNATIONAL_RECHARGE_L1_REQUEST(NEW_ADDRESS_BASE.getURL() + NEW_INTL_TOPUP_URL.getURL()),
    NEW_INTERNATIONAL_RECHARGE_L1_RESPONSE,
    NEW_INTERNATIONAL_RECHARGE_RECIPIENTLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_INTERNATIONAL_RECHARGE_RECIPIENTLIST_RESPONSE,

    //NEW FLOW HISTORY
    GUEST_TRAN_HISTORY_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    GUEST_TRAN_HISTORY_RESPONSE,


    //    NEW_VOCHER_IMAGE_REQUEST,
//    NEW_VOCHER_DENOMINATION_REQUEST,
//    NEW_VOCHER_STORE_REQUEST,
    NEW_VOCHER_IMAGE_RESPONSE,
    NEW_VOCHER_STORE_RESPONSE,
    NEW_VOCHER_DENOMINATION_RESPONSE,
    //    NEW_VOCHER_RECHARGE_L1_REQUEST,
    NEW_VOCHER_L1_RESPONSE,


// NewFlow  DeviceId check for new login

    DEVICEID_LOGIN_CHECK_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    DEVICEID_LOGIN_CHECK_RESPONSE,

    //  NewFlow new Login
    NEW_LOGIN_CUSTOMER_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_LOGIN_CUSTOMER_RESPONSE,

//    LOGIN_CUSTOMER (Old)  - NEW_LOGIN_CUSTOMER_REQUEST(New)


    NEW_WHERE_TO_PAY_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_WHERE_TO_PAY_RESPONSE,

    NEW_WHERE_TO_PAY_CATEGORY_RESPONSE,
    NEW_WHERE_TO_PAY_CATEGORY_REQUEST,
    NEW_WHERE_TO_PAY_MERCHANTLIST_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_WHERE_TO_PAY_MERCHANTLIST_RESPONSE,
    NEW_WHERE_TO_PAY_MAPVIEW_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_WHERE_TO_PAY_MAPVIEW_RESPONSE,
    NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_RESPONSE,

    //Offers Newflow
    NEW_OFFER_ACTIVEDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_OFFER_ACTIVEDETAILS_RESPONSE,


    NEW_OFFER_AllACTIVEDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_OFFER_AllACTIVEDETAILS_RESPONSE,


    //Offers after login Nov 17
    NEW_CUSTOMER_OFFER_ACTIVEDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_CUSTOMER_OFFER_ACTIVEDETAILS_RESPONSE,

    NEW_CUSTOMER_OFFER_AllACTIVEDETAILS_REQUEST(ADDRESS_BASE.getURL() + "ooredooServerRequest"),
    NEW_CUSTOMER_OFFER_AllACTIVEDETAILS_RESPONSE;


    private String server_controller_mapping;

    TransType() {
        this.server_controller_mapping = null;
    }

    TransType(String url) {
        this.server_controller_mapping = url;
    }

    public String getURL() {
        return server_controller_mapping;
    }

    public void setURL(String url) {
        this.server_controller_mapping = url;
    }
}