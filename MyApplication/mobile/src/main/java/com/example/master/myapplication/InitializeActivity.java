package com.example.master.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class InitializeActivity extends AppCompatActivity {
    private boolean connected = false;
    BroadcastReceiver connectivityActionReceiver;
    Intent intent;
    ConnectivityManager cm;
    NetworkInfo ni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        ni = cm.getActiveNetworkInfo();
        connected = (ni != null) && ni.isConnected();

        connectivityActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("Receiver","changedConnectivity");
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                try {
                    String mTypename = ni.getTypeName();
                    boolean connected = (ni != null) && ni.isConnected() && mTypename.equals("WIFI");
                    onConnectedChanged(connected);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
        };
        registerReceiver(connectivityActionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        intent = new Intent(getApplication(), ConnectActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        //finish();

    }
    private void onConnectedChanged(boolean connected){
        Log.d("Receiver","onConnectedChanged");
        if(connected == this.connected) return;
        this.connected = connected;
        if(!connected ){
            Intent intent = new Intent(getApplication(),ReConnectActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, ni.getType() + "Connected", Toast.LENGTH_LONG).show();
            intent = new Intent(getApplication(), StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            startActivity(intent);
            //finish();
        }
    }
    private boolean isConnected(){
        return connected;
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(connectivityActionReceiver);
        super.onDestroy();
    }

}
