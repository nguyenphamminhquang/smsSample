package com.joaquimley.smsparsing;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final String PREF_USER_MOBILE_PHONE = "pref_user_mobile_phone";
    private static final int SMS_PERMISSION_CODE = 0;
    private EditText txtMobile;
    private EditText txtMessage;
    public static TextView responseTxT;
    private Button btnSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        }

        txtMobile = (EditText)findViewById(R.id.mblTxt);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        responseTxT = (TextView) findViewById(R.id.responseTxT);
        btnSms = (Button)findViewById(R.id.btnSend);
        txtMobile.setText("+12568672374");
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(),null,txtMessage.getText().toString(),null,null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private EditText mNumberEditText;
//    private String mUserMobilePhone;
//    private SharedPreferences mSharedPreferences;
//    SmsManager smsManager = SmsManager.getDefault();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (!hasReadSmsPermission()) {
//            showRequestPermissionsInfoAlertDialog();
//        }
//
//        initViews();
//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mUserMobilePhone = mSharedPreferences.getString(PREF_USER_MOBILE_PHONE, "");
//        if (!TextUtils.isEmpty(mUserMobilePhone)) {
//            mNumberEditText.setText(mUserMobilePhone);
//        }
//    }
//
//    private void initViews() {
//        mNumberEditText = (EditText) findViewById(R.id.et_number);
//        findViewById(R.id.btn_normal_sms).setOnClickListener(this);
//        findViewById(R.id.btn_conditional_sms).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_normal_sms:
//                if (!hasValidPreConditions()) return;
//                checkAndUpdateUserPrefNumber();
//                smsManager.sendTextMessage();
//                SmsHelper.sendDebugSms(String.valueOf(mNumberEditText.getText()), "The broadcast should not show a toast for this");
//                Toast.makeText(getApplicationContext(), R.string.toast_sending_sms, Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }
//
//    /**
//     * Checks if stored SharedPreferences value needs updating and updates \o/
//     */
//    private void checkAndUpdateUserPrefNumber() {
//        if (TextUtils.isEmpty(mUserMobilePhone) && !mUserMobilePhone.equals(mNumberEditText.getText().toString())) {
//            mSharedPreferences
//                    .edit()
//                    .putString(PREF_USER_MOBILE_PHONE, mNumberEditText.getText().toString())
//                    .apply();
//        }
//    }
//
//
//    /**
//     * Validates if the app has readSmsPermissions and the mobile phone is valid
//     *
//     * @return boolean validation value
//     */
//    private boolean hasValidPreConditions() {
//        if (!hasReadSmsPermission()) {
//            requestReadAndSendSmsPermission();
//            return false;
//        }
//
//        if (!SmsHelper.isValidPhoneNumber(mNumberEditText.getText().toString())) {
//            Toast.makeText(getApplicationContext(), R.string.error_invalid_phone_number, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }

    /**
     * Optional informative alert dialog to explain the user why the app needs the Read/Send SMS permission
     */
    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_alert_dialog_title);
        builder.setMessage(R.string.permission_dialog_message);
        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
    }

    /**
     * Runtime permission shenanigans
     */
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.SEND_SMS},
                SMS_PERMISSION_CODE);
    }
}