package com.bookeey.wallet.live.mainmenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.wheretopay.MerchantListCatogorieyActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.utils.URLUTF8Encoder;
import wheretopaynew.MerchantListCatogorieyActivityNewUI;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;
import ycash.wallet.json.pojo.wheretopay.MerchnatListRequest;

/**
 * Created by 30099 on 4/4/2016.
 */
public class MerchantSelectionSliderList extends ListFragment {
    public static final String tag = MerchantSelectionSliderList.class.getCanonicalName();
    private ProgressDialog progress;
    private String[] listItems;
    private String[] listItems_second;
    private eComSelectionSliderListProcessor eComSliderProcessor = null;
    String categoryname;
    EfficientAdapter adapter;
    MerchantListResponse merchantListResponse;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getActivity();
        progress = new ProgressDialog(getActivity(), R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getActivity().getApplicationContext()).getCustomerLoginRequestReponse();
        MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    try {
                        GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                        if (null != response && response.getG_status() == 1) {
                            merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                            listItems = new String[merchantListResponse.getCategoryDetails().size()];
                            ((CoreApplication) getActivity().getApplication()).setMerchantListResponse_category(merchantListResponse);
                            for (int i = 0; i < merchantListResponse.getCategoryDetails().size(); i++) {
                                listItems[i] = merchantListResponse.getCategoryDetails().get(i).getCategoryName();
                            }
                            eComSliderProcessor = new eComSelectionSliderListProcessor();
                            adapter = new EfficientAdapter(getActivity(), EfficientAdapter.ListId.ID_ECOM, eComSliderProcessor);
                            setListAdapter(adapter);
                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), response.getG_errorDescription(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();

                            //  Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (Exception e) {
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getActivity(),getActivity().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getActivity().getApplicationContext())).start();
        showIfNotVisible("");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    public void reset() {
        adapter.equals(null);
        adapter.notifyDataSetChanged();
    }

    private void showIfNotVisible(String title) {
        if (!progress.isShowing()) {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        } else {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        }
    }

    public void onListItemClick(ListView l, View v, final int position, long id) {

        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getActivity().getApplicationContext()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        final MerchantListResponse merchantListResponse = ((CoreApplication) getActivity().getApplication()).getMerchantListResponse_category();
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MERCHANTLIST_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        categoryname = merchantListResponse.getCategoryDetails().get(position).getCategoryName();
        merchnatListRequest.setCategoryID(merchantListResponse.getCategoryDetails().get(position).getCategoryCode());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_MERCHANTLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1) {
                        final MerchantListResponse merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                        if (merchantListResponse.getCategoryDetails().size() == 0) {
                            ((CoreApplication) getActivity().getApplication()).setMerchantListResponse_1(merchantListResponse);
                        }
                        ((CoreApplication) getActivity().getApplication()).setCategoryname(categoryname);
                        ((CoreApplication) getActivity().getApplication()).setCategoryId(merchnatListRequest.getCategoryID());
                        if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        if (merchantListResponse.getMerchantDetails().size() == 0) {
                            Toast.makeText(getActivity(), "No merchants available", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        } else {

                            setListShown(true);
                            Intent intent = new Intent(getActivity(), MerchantListCatogorieyActivity.class);
                            startActivity(intent);

                            //For circular images selection
//                            setListShown(true);
//                            Intent intent = new Intent(getActivity(), MerchantListCatogorieyActivityNewUI.class);
//                            startActivity(intent);

                        }
                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        /*Toast.makeText(getActivity(), "No merchnats available", Toast.LENGTH_SHORT).show();
                        return;*/
                        Toast toast = Toast.makeText(getActivity(), response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        getActivity().finish();
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getActivity().getApplicationContext())).start();
        showIfNotVisible("");
    }

    private class eComSelectionSliderListProcessor implements Processor {
        @Override
        public String[] getDisplayable() {
            return listItems;
        }

        @Override
        public String[] getNextLineDisplayable() {
            return listItems_second;
        }

        @Override
        public Integer[] getTypes() {
            return new Integer[]{0, 1, 2};
        }

        @Override
        public boolean isDoubleLine() {
            return false;
        }
    }
}