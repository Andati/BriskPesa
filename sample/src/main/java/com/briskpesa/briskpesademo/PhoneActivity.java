package com.briskpesa.briskpesademo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class PhoneActivity extends AppCompatActivity {
    String phone;
    DBHelper mydb;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        mydb = new DBHelper(this);
        pDialog = new ProgressDialog(this);
    }

    public void verifyPhone(View v){
        //Ask for phone number confirmation
        String msisdn = ((EditText) findViewById(R.id.phone)).getText().toString();

        if(!Utils.isValidPhoneNumber(msisdn)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneActivity.this);
            builder.setTitle("BriskPesa")
                    .setMessage("Please enter a valid phone number.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }

        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        phone = Utils.sanitizePhoneNumber(msisdn);
        mydb.insertUser(phone);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog.isShowing())
                            pDialog.dismiss();

                        startActivity(new Intent(PhoneActivity.this, MainActivity.class));
                        PhoneActivity.this.finish();
                    }
                }, 3000);
            }
        });
    }
}
