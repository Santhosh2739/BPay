package com.bookeey.wallet.live.registration;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.Help;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.Registration_processing.RegistrationProcessing;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.LocaleHelper;
import ycash.wallet.json.pojo.registration.CustomerRegistrationRequest;
import ycash.wallet.json.pojo.registration.Scandetails;

public class OoredooRegistration extends GenericActivity implements YPCHeadlessCallback {
    private static final int CAMERA_REQUEST = 123;
    private static final int CAMERA_REQUEST_BACK = 124;
    private static final int PICK_FROM_GALLERY = 2;
    private static final int PICK_FROM_GALLERY_BACK = 3;
    private static final int SELECT_PHONE_NUMBER = 24242;

//    EditText registration_confirm_email_id_edit;
    EditText registration_firstname_edit, registration_lastname_edit, registration_email_id_edit,
            registration_mobile_edit, registration_civilid_edit, registration_security_answer_edit;
    ImageView registration_capture_btn_id;
    String mobile_number;
    View ooredoo_registration_view1, ooredoo_registration_view2, ooredoo_registration_view3, ooredoo_registration_view4, ooredoo_registration_view5;
    View ooredoo_registration_view6, ooredoo_registration_view07, ooredoo_registration_view08, ooredoo_registration_view7, ooredoo_registration_view8, ooredoo_registration_view9, ooredoo_registration_view10;
    Scandetails scandetails = null;
    String selectedLanguage = null;
    private String front_civil_id_data = null;
    private String back_civil_id_data = null;
    private LinearLayout registration_front_capture_btn_id, registration_back_capture_btn_id;
    private LinearLayout ooreddoo_registration_png_attachment;
    private CheckBox registration_terms_and_conditions_checkbox;
    //Spinner registration_security_question_spinner;
    /*String security_question_types[] = {"Please select a question", "What is a security question?", "Are answers to security questions case sensitive?", "How do I change my security question on my Gmail account?", "What is of common security?"};
    String question_type;*/
    private Button registration_submit_btn;
    private Uri mFrontCaptureimageUri, mBackCaptureimageUri;
    private EditText registration_referred_by_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Rahman
        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(OoredooRegistration.this, selectedLanguage);
        }
        //Rahman

        setContentView(R.layout.registration);
        showMenu(false);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("REGISTRATION");*/

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.general_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);

        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.registartion_title));

        registration_terms_and_conditions_checkbox = (CheckBox) findViewById(R.id.registration_terms_and_conditions_checkbox);
        ((TextView) findViewById(R.id.user_info_view_tc_tvid)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TermsAndConditions.class);
                startActivity(intent);
            }
        });
        ((TextView) findViewById(R.id.registration_help_text)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Help.class);
                startActivity(intent);
            }
        });

        String data = getIntent().getStringExtra("data");

        scandetails = new Gson().fromJson(data, Scandetails.class);

        ooredoo_registration_view1 = (View) findViewById(R.id.ooredoo_registration_view1);
        ooredoo_registration_view2 = (View) findViewById(R.id.ooredoo_registration_view2);
        ooredoo_registration_view3 = (View) findViewById(R.id.ooredoo_registration_view3);
        ooredoo_registration_view4 = (View) findViewById(R.id.ooredoo_registration_view4);
        ooredoo_registration_view5 = (View) findViewById(R.id.ooredoo_registration_view5);
        ooredoo_registration_view6 = (View) findViewById(R.id.ooredoo_registration_view6);
        ooredoo_registration_view7 = (View) findViewById(R.id.ooredoo_registration_view7);
        ooredoo_registration_view8 = (View) findViewById(R.id.ooredoo_registration_view8);
        ooredoo_registration_view07 = (View) findViewById(R.id.ooredoo_registration_view07);
        ooredoo_registration_view08 = (View) findViewById(R.id.ooredoo_registration_view08);
        ooredoo_registration_view9 = (View) findViewById(R.id.ooredoo_registration_view9);
        ooredoo_registration_view10 = (View) findViewById(R.id.ooredoo_registration_view10);

        ooreddoo_registration_png_attachment = (LinearLayout) findViewById(R.id.ooreddoo_registration_png_attachment);
        registration_front_capture_btn_id = (LinearLayout) findViewById(R.id.registration_front_capture_btn_id);
        registration_back_capture_btn_id = (LinearLayout) findViewById(R.id.registration_capture_back_btn_id);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_firstname_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_lastname_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_civilid_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_email_id_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_confirm_email_id_edit)).getWindowToken(), 0);
        imm.hideSoftInputFromInputMethod(((EditText) findViewById(R.id.registration_mobile_edit)).getWindowToken(), 0);
        registration_firstname_edit = (EditText) findViewById(R.id.registration_firstname_edit);
        registration_lastname_edit = (EditText) findViewById(R.id.registration_lastname_edit);
        registration_email_id_edit = (EditText) findViewById(R.id.registration_email_id_edit);
        registration_mobile_edit = (EditText) findViewById(R.id.registration_mobile_edit);
        registration_civilid_edit = (EditText) findViewById(R.id.registration_civilid_edit);


        registration_referred_by_edit = (EditText) findViewById(R.id.registration_referred_by_edit);


        ooreddoo_registration_png_attachment = (LinearLayout) findViewById(R.id.ooreddoo_registration_png_attachment);


//        registration_confirm_email_id_edit = (EditText) findViewById(R.id.registration_confirm_email_id_edit);
        civilIDInfoPopup();

        /* registration_security_answer_edit = (EditText) findViewById(R.id.registration_security_answer_edit);*/


        registration_email_id_edit.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        registration_email_id_edit.setLongClickable(false);
        registration_email_id_edit.setTextIsSelectable(false);


//        registration_confirm_email_id_edit.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
//
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            public void onDestroyActionMode(ActionMode mode) {
//            }
//
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//        });
//        registration_confirm_email_id_edit.setLongClickable(false);
//        registration_confirm_email_id_edit.setTextIsSelectable(false);


        registration_firstname_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (registration_firstname_edit.hasFocus()) {
                    ooredoo_registration_view1.setBackgroundColor(getResources().getColor(R.color.gold));
                    ooredoo_registration_view2.setBackgroundColor(getResources().getColor(R.color.gold));
                } else if (!registration_firstname_edit.hasFocus()) {
                    ooredoo_registration_view1.setBackgroundColor(Color.parseColor("#000000"));
                    ooredoo_registration_view2.setBackgroundColor(Color.parseColor("#000000"));
                }
            }

        });
        registration_lastname_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (registration_lastname_edit.hasFocus()) {
                    ooredoo_registration_view3.setBackgroundColor(getResources().getColor(R.color.gold));
                    ooredoo_registration_view4.setBackgroundColor(getResources().getColor(R.color.gold));
                } else if (!registration_lastname_edit.hasFocus()) {
                    ooredoo_registration_view3.setBackgroundColor(Color.parseColor("#000000"));
                    ooredoo_registration_view4.setBackgroundColor(Color.parseColor("#000000"));
                }
            }

        });

       /* registration_security_question_spinner = (Spinner) findViewById(R.id.registration_security_question_spinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, security_question_types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registration_security_question_spinner.setAdapter(adapter);
        registration_security_question_spinner.setOnItemSelectedListener(this);*/

        registration_submit_btn = (Button) findViewById(R.id.registration_submit_btn);
        registration_submit_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (registration_firstname_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_first_name), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (registration_lastname_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_last_name), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }


                if (registration_civilid_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_civil_id), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }

                if (registration_civilid_edit.getText().toString().length() != 12) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_valid_civil_id), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }

                if (registration_email_id_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_mail_id), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                final String emailPattern = "[a-zA-Z0-9~.&$@*#%()=':;/?+!_-]+@[a-zA-Z0-9~.&$@*#%()=':;/?+!_-]+[a-zA-Z0-9~.&$@*#%()=':;/?+!_-]+";
                if (!registration_email_id_edit.getText().toString().trim().matches(emailPattern)) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.mail_id_valid_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }

//                if (!registration_confirm_email_id_edit.getText().toString().trim().matches(emailPattern)) {
//                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.confirm_mail_id_valid_msg), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
//                    return;
//                }


//                if (registration_confirm_email_id_edit.getText().toString().length() == 0) {
//                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.Please_enter_confirm_email_id), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
//                    return;
//                }

//                if (!registration_confirm_email_id_edit.getText().toString().equals(registration_email_id_edit.getText().toString())) {
//                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.maild_should_be_same), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
//                    return;
//                }

                if (registration_mobile_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.mobile_bill_L1_enter_mobile_number), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }


                if (front_civil_id_data == null) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.civilform_capturing_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (back_civil_id_data == null) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.civilform_back_capturing_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (!registration_terms_and_conditions_checkbox.isChecked()) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, getResources().getString(R.string.terms_conditions_msg), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }

               /* if (registration_security_question_spinner.getSelectedItemPosition() < 1) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, "Please select security question type", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }
                if (registration_security_answer_edit.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(OoredooRegistration.this, "Please answer the security question", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    return;
                }*/


                if (front_civil_id_data != null && back_civil_id_data != null) {
                    ooreddoo_registration_png_attachment.setVisibility(View.VISIBLE);
                }
                registration_firstname_edit.setError(null);
                registration_lastname_edit.setError(null);
                registration_civilid_edit.setError(null);
                registration_email_id_edit.setError(null);
//                registration_confirm_email_id_edit.setError(null);
                registration_mobile_edit.setError(null);
                ooreddoo_registration_png_attachment.setVisibility(View.VISIBLE);
                registration();
            }
        });
        registration_front_capture_btn_id.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

//                ooreddoo_registration_png_attachment.setVisibility(View.GONE);


                alertDialog();

                /*if (!checkPermissionForCamera()) {
                    Log.i("IF", "if");
                    requestPermissionForCamera(CAMERA_REQUEST);
                } else {
                    Log.i("ELSE", "else");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getBaseContext(), "Your camera can't able to take the data of picture ", Toast.LENGTH_SHORT);
                    }
                }*/
            }
        });
        registration_back_capture_btn_id.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

//                ooreddoo_registration_png_attachment.setVisibility(View.GONE);

                backalertDialog();

                /*if (!checkPermissionForCamera()) {
                    requestPermissionForCamera(CAMERA_REQUEST_BACK);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_BACK);
                    } else {
                        Toast.makeText(getBaseContext(), "Your camera can't able to take the picture data ", Toast.LENGTH_SHORT);
                    }
                }*/
            }
        });
        registration_civilid_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (registration_civilid_edit.hasFocus()) {
                    ooredoo_registration_view5.setBackgroundColor(getResources().getColor(R.color.gold));
                    ooredoo_registration_view6.setBackgroundColor(getResources().getColor(R.color.gold));
                } else if (!registration_civilid_edit.hasFocus()) {
                    ooredoo_registration_view5.setBackgroundColor(Color.parseColor("#000000"));
                    ooredoo_registration_view6.setBackgroundColor(Color.parseColor("#000000"));
                }
            }

        });
//        registration_confirm_email_id_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (registration_confirm_email_id_edit.hasFocus()) {
//                    ooredoo_registration_view07.setBackgroundColor(getResources().getColor(R.color.gold));
//                    ooredoo_registration_view8.setBackgroundColor(getResources().getColor(R.color.gold));
//                } else if (!registration_confirm_email_id_edit.hasFocus()) {
//                    ooredoo_registration_view07.setBackgroundColor(Color.parseColor("#000000"));
//                    ooredoo_registration_view8.setBackgroundColor(Color.parseColor("#000000"));
//                }
//            }
//        });
        registration_email_id_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (registration_email_id_edit.hasFocus()) {
                    ooredoo_registration_view7.setBackgroundColor(getResources().getColor(R.color.gold));
                    ooredoo_registration_view08.setBackgroundColor(getResources().getColor(R.color.gold));
                } else if (!registration_email_id_edit.hasFocus()) {
                    ooredoo_registration_view7.setBackgroundColor(Color.parseColor("#000000"));
                    ooredoo_registration_view08.setBackgroundColor(Color.parseColor("#000000"));
                }
            }

        });

        String mobile_number = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        registration_mobile_edit.setText(mobile_number);

        //Sep 23
//        registration_mobile_edit.setEnabled(false);


        if (registration_firstname_edit.getText().toString().trim().length() != 0) {
            registration_firstname_edit.setError(null);
            return;
        }
        if (registration_lastname_edit.getText().toString().trim().length() != 0) {
            registration_lastname_edit.setError(null);
            return;
        }
        if (registration_civilid_edit.getText().toString().trim().length() != 0) {
            registration_civilid_edit.setError(null);
            return;
        }
//        if (registration_confirm_email_id_edit.getText().toString().trim().length() != 0) {
//            registration_confirm_email_id_edit.setError(null);
//            return;
//        }
        if (registration_email_id_edit.getText().toString().trim().length() != 0) {
            registration_email_id_edit.setError(null);
            return;
        }
        if (scandetails != null) {
            registration_firstname_edit.setText(scandetails.getFirstname());
            registration_lastname_edit.setText(scandetails.getLastname());
            registration_civilid_edit.setText(scandetails.getCivilID());
        }


        ImageView contact_picker = (ImageView) findViewById(R.id.contact_picker);
        contact_picker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, SELECT_PHONE_NUMBER);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("SignUp_Initiated");
    }

    private void civilIDInfoPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OoredooRegistration.this, R.style.MyDialogTheme);
        //Creating dialog box
        builder.setMessage(getResources().getString(R.string.registration_alert))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.registration_alert_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true; // Consumed
                } else {
                    return false; // Not consumed
                }
            }
        });

        AlertDialog dialog = builder.create();
        //Setting the title manually
        builder.setCancelable(false);
        String lan = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (lan != null && !lan.isEmpty()) {
            if (lan.equalsIgnoreCase("en")) {
                dialog.setTitle(Html.fromHtml("<font color='#000000'>Info</font>"));
            } else {
                dialog.setTitle(Html.fromHtml("<font color='#000000'>معلومات</font>"));
            }
        } else {
            dialog.setTitle(Html.fromHtml("<font color='#000000'>Info</font>"));
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black));

    }


    private void alertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_image, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OoredooRegistration.this);
        alertDialog.setView(promptsView);
        alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!checkPermissionForCamera()) {
                    Log.i("IF", "if");
                    requestPermissionForCamera(CAMERA_REQUEST);
                } else {
                    Log.i("ELSE", "else");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getBaseContext(), "Your camera can't able to take the data of picture ", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        alertDialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY);
                }
            }

        });
        alertDialog.show();
    }

    private void backalertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_alert_image, null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OoredooRegistration.this);
        alertDialog.setView(promptsView);
        alertDialog.setPositiveButton(getApplicationContext().getString(R.string.registration_camera), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!checkPermissionForCamera()) {
                    Log.i("IF", "if");
                    requestPermissionForCamera(CAMERA_REQUEST_BACK);
                } else {
                    Log.i("ELSE", "else");
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_BACK);
                    } else {
                        Toast.makeText(getBaseContext(), "Your camera can't able to take the data of picture ", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        alertDialog.setNegativeButton(getApplicationContext().getString(R.string.registration_gallery), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY_BACK);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_FROM_GALLERY_BACK);
                }
            }

        });
        alertDialog.show();
    }

    public void requestPermissionForCamera(int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(OoredooRegistration.this, android.Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(OoredooRegistration.this, new String[]{android.Manifest.permission.CAMERA}, requestCode);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                Log.i("CAMERA_REQUEST", "camera request");
                //create instance of File with same name we created before to get image from storage
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                Uri frontimageUri= data.getData();
                Bitmap mPhoto = Bitmap.createScaledBitmap(photo, 600, 600, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mPhoto.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                byte[] imageData = baos.toByteArray();
                front_civil_id_data = Base64.encodeToString(imageData, Base64.NO_WRAP);
                Toast toast = Toast.makeText(OoredooRegistration.this, "Front side of civil id captured successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
           /* if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap mPhoto= Bitmap.createScaledBitmap(photo,600, 600,true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mPhoto.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                byte[] imageData = baos.toByteArray();
                front_civil_id_data =  Base64.encodeToString(imageData, Base64.NO_WRAP);
                Toast toast= Toast.makeText(OoredooRegistration.this,"Front side of civil id captured successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }*/
            if (requestCode == CAMERA_REQUEST_BACK && resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap mPhoto = Bitmap.createScaledBitmap(photo, 600, 600, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                mPhoto.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                byte[] imageData = baos.toByteArray();
                back_civil_id_data = Base64.encodeToString(imageData, Base64.NO_WRAP);
                Toast toast = Toast.makeText(OoredooRegistration.this, "Back side of civil id captured successfully", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }


            if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
                try {

                    final Uri imageUri = data.getData();
                    try {
                        cropCapturedImage(imageUri);
                    } catch (ActivityNotFoundException aNFE) {
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap mPhoto = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mPhoto.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                    byte[] imageData = baos.toByteArray();
                    front_civil_id_data = Base64.encodeToString(imageData, Base64.NO_WRAP);
                    //image_person.setImageBitmap(selectedImage);
                    Toast toast = Toast.makeText(OoredooRegistration.this, "front side of civil id captured successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            if (requestCode == PICK_FROM_GALLERY_BACK && resultCode == RESULT_OK) {
                try {

                    final Uri imageUri = data.getData();
                    try {
                        cropCapturedImage(imageUri);
                    } catch (ActivityNotFoundException aNFE) {
                        String errorMessage = "Sorry - your device doesn't support the crop action!";
                        Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap mPhoto = BitmapFactory.decodeStream(imageStream);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    mPhoto.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                    byte[] imageData = baos.toByteArray();
                    back_civil_id_data = Base64.encodeToString(imageData, Base64.NO_WRAP);
                    //image_person.setImageBitmap(selectedImage);
                    Toast toast = Toast.makeText(OoredooRegistration.this, "Back side of civil id captured successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            /*if (requestCode == 0 && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();

                //handleCrop(resultCode, data);
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                    byte[] imageData = baos.toByteArray();
                    back_civil_id_data = Base64.encodeToString(imageData, Base64.NO_WRAP);
                    Toast toast = Toast.makeText(OoredooRegistration.this, "Front side of civil id captured successfully", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(OoredooRegistration.this, "Your mobile is not able to capture image", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                }
            }*/


            if (requestCode == SELECT_PHONE_NUMBER && resultCode == RESULT_OK) {
                // Get the URI and query the content provider for the phone number
                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = this.getContentResolver().query(contactUri, projection,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);


                    String phoneNumberWithCountryCode = cursor.getString(numberIndex);

                    if (phoneNumberWithCountryCode.contains("+") && phoneNumberWithCountryCode.contains(" ")) {

                        Pattern complie = Pattern.compile(" ");
                        String[] phonenUmber = complie.split(phoneNumberWithCountryCode);
                        registration_referred_by_edit.setText(phonenUmber[1] + phonenUmber[2]);

                    } else if (phoneNumberWithCountryCode.contains(" ")) {

                        Pattern complie = Pattern.compile(" ");
                        String[] phonenUmber = complie.split(phoneNumberWithCountryCode);
                        registration_referred_by_edit.setText(phonenUmber[0] + phonenUmber[1]);

                    } else if (phoneNumberWithCountryCode.contains("+")) {


                        registration_referred_by_edit.setText(phoneNumberWithCountryCode.substring(4));

                    } else {

                        registration_referred_by_edit.setText(phoneNumberWithCountryCode);
                    }

                }

                cursor.close();
            }


        } catch (Exception e) {


            Toast toast = Toast.makeText(OoredooRegistration.this, "Something went wrong", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();


//            Toast toast1 = Toast.makeText(OoredooRegistration.this, "Something went wrong: "+e.getMessage(), Toast.LENGTH_SHORT);
//            toast1.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//            toast1.show();
//
//            Toast toast2 = Toast.makeText(OoredooRegistration.this, "Something went wrong: "+e.getMessage(), Toast.LENGTH_SHORT);
//            toast2.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//            toast2.show();


        }


        //Rahman
        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(OoredooRegistration.this, selectedLanguage);
        }
        //Rahman


        //Rahman

        if (front_civil_id_data != null && back_civil_id_data != null) {
            ooreddoo_registration_png_attachment.setVisibility(View.VISIBLE);
        }
    }

    private void cropCapturedImage(Uri uri) {
        //call the standard crop action intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(uri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, 0);
    }

    void registration() {
        String firstname = registration_firstname_edit.getText().toString();
        String secondname = registration_lastname_edit.getText().toString();
        String email_id = registration_email_id_edit.getText().toString();
//        String confirm_mail = registration_confirm_email_id_edit.getText().toString();
        String civil_id = registration_civilid_edit.getText().toString();
        mobile_number = registration_mobile_edit.getText().toString();
        CustomSharedPreferences.saveStringData(OoredooRegistration.this, mobile_number, CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);

        //String registration_security_answer = registration_security_answer_edit.getText().toString();

        String deviceID = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
        CustomerRegistrationRequest crr = new CustomerRegistrationRequest();
        crr.setFirstName(firstname);
        crr.setLastName(secondname);
        crr.setMobileNumber(mobile_number);
        crr.setEmailID(email_id);
        crr.setCivilID(civil_id);
        crr.setCivil_ID_Image_Front(front_civil_id_data);
        crr.setCivil_ID_Image_Back(back_civil_id_data);
        crr.setLanguage("English");
        crr.setDeviceIdNumber(deviceID);
//        crr.setNationality(scandetails.getNationality());
//        crr.setGender(scandetails.getGender());


//        log


        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new RegistrationProcessing(crr, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");


    }


    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public boolean checkPermissionForCamera() {
        Log.i("checkPermission", "checkPermissionForCamera");
        int result = ContextCompat.checkSelfPermission(OoredooRegistration.this, android.Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        question_type = registration_security_question_spinner.getSelectedItem().toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/
 /*   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFrontCaptureimageUri != null) {
            outState.putString("frontcameraUri", mFrontCaptureimageUri.toString());
        }
        if(mBackCaptureimageUri !=null){
            outState.putString("backcameraUri", mBackCaptureimageUri.toString());
        }
    }*/
   /* @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("frontcameraUri")) {
            mFrontCaptureimageUri = Uri.parse(savedInstanceState.getString("frontcameraUri"));
        }
        if (savedInstanceState.containsKey("backcameraUri")) {
            mBackCaptureimageUri = Uri.parse(savedInstanceState.getString("backcameraUri"));
        }

    }*/
}