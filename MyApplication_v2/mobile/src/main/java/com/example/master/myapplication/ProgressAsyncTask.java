package com.example.master.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by okudo on 2016/12/01.
 */

public class ProgressAsyncTask extends AsyncTask<String, Boolean, Long> {
    private AsyncTaskCallbacks callback = null;
    final String TAG = "ProgressAsyncTask";
    Context context;
    ProgressDialog progressDialog;
    WifiManager wm;
    WifiConfiguration config;
    Boolean isConnect;
    public ProgressAsyncTask(Context context, WifiManager wm, WifiConfiguration config, AsyncTaskCallbacks callback){
        this.context = context;
        this.wm=wm;
        this.config = config;
        this.isConnect = false;
        this.callback=callback;
    }
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Connecting Wi-Fi...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    @Override
    protected Long doInBackground(String... params){
        try{
            Thread.sleep(500);
            for(int i=0;i<10;i++){
                Thread.sleep(1000);
                if(wm.addNetwork(config) != -1) {
                    Log.d(TAG, "add is true");
                    wm.saveConfiguration();
                    wm.updateNetwork(config);
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                    //オンライン，オフライン判定
                    if(networkInfo != null && networkInfo.isConnected()){
                    //if(wm.enableNetwork(config.networkId, true)){
                        //接続成功
                        isConnect = true;
                        break;
                    }
                }
            }

        }catch(InterruptedException e){
            Log.d(TAG,"InterruptedException in doInBackground");
        }
        //publishProgress(1);

        return 123L;
    }

    @Override
    protected void onProgressUpdate(Boolean... values){
        //progressDialog.setProgress(values[0]);
        //if(values[0]){}
    }

    @Override
    protected void onPostExecute(Long result){
        progressDialog.dismiss();
        if(!isConnect){
            Toast toast = Toast.makeText(context, "fail to connect",Toast.LENGTH_LONG);
            toast.show();
        }
        callback.onTaskFinished(isConnect);
    }

    @Override
    protected void onCancelled(){
        super.onCancelled();
        progressDialog.dismiss();
        callback.onTaskCancelled(isConnect);
    }



}
