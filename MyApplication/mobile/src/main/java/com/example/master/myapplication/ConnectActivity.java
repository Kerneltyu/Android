package com.example.master.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.master.myapplication.R;

import java.util.List;

/**
 * コネクトページ
 */

public class ConnectActivity extends Activity{
    final String TAG = "ConnectActivity";
    private boolean connected = false;
    BroadcastReceiver connectivityActionReceiver;
    Intent intent;
    ConnectivityManager cm;
    NetworkInfo ni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent;
        //Wi-Fiネットワークの検索セクション------//
/*
        if(!wm.isWifiEnabled()){
            wm.setWifiEnabled(true);
        }
        //"\"Lm9y62pTdcj7\""
        // Lm9y62pTdcj7
*/
        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();

        try {
            if (ni.isConnected() && ni.getType() != 0) {
                Toast.makeText(this, ni.getType() + "Connected", Toast.LENGTH_LONG).show();
                intent = new Intent(getApplication(), StartActivity.class);
                startActivity(intent);
                finish();
            } else {}
        }catch(NullPointerException e){
            Toast.makeText(this, "Not connected", Toast.LENGTH_LONG).show();
        }

        //接続できたら
        //接続できなかった場合も記述すること

    }
}