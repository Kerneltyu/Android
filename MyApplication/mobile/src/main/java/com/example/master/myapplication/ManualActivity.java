package com.example.master.myapplication;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import processing.android.PFragment;
import processing.core.PApplet;

/** ManualActivity
 * 模型車の制御(四方の移動命令，温度・湿度の計測命令)を行うアクティビティ
 * */

public class ManualActivity extends AppCompatActivity {

    /**
     * インスタンス変数
     */
    private final static String TAG = "TMS";       //name:Temperature Management System
    final static String FORWARD = "F\n";
    final static String RIGHT = "R\n";
    final static String BACKWARD = "B\n";
    final static String LEFT = "L\n";
    final static String IPAddress = "192.168.0.55";
    final static int PortNo = 65432;
    private Socket socket;                          //ソケット
    private InputStream in;                         //入力ストリーム
    private OutputStream out;                       //出力ストリーム
    private Button calcButton;                      //計測ボタン

    ShareData shareData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shareData = ((ShareData) getApplicationContext());

        /** 温度・湿度計測のボタン設定 */
        calcButton = (Button) findViewById(R.id.send_button);

        /**
         車を制御するためのボタン設定
         imageButton1 : 上
         imageButton2 : 下
         imageButton3 : 左
         imageButton4 : 右 */

        ImageButton imageButton1 = (ImageButton) findViewById(R.id.image_button1);
        ImageButton imageButton2 = (ImageButton) findViewById(R.id.image_button2);
        ImageButton imageButton3 = (ImageButton) findViewById(R.id.image_button3);
        ImageButton imageButton4 = (ImageButton) findViewById(R.id.image_button4);

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** メインページに遷移する */
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        /** 方向を表すbit配列(data[])
         * 上 00
         * 下 01
         * 左 10
         * 右 11
         */
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 上方向に進む */
                try {
                    //上方向
                    sendDirection(FORWARD); //方向データを送信

                } catch (Exception e) {
                    e.printStackTrace(); //エラー処理
                }
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** 下方向に進む */
                try {
                    //下方向
                    sendDirection(BACKWARD); //方向データを送信

                } catch (Exception e) {
                    e.printStackTrace(); //エラー処理
                }
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** 左方向に進む */
                try {
                    //左方向
                    sendDirection(LEFT); //方向データを送信

                } catch (Exception e) {
                    e.printStackTrace(); //エラー処理
                }
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /** 右方向に進む */
                try {
                    //右方向
                    sendDirection(RIGHT); //方向データを送信

                } catch (Exception e) {
                    e.printStackTrace(); //エラー処理
                }
            }
        });

        Thread thread = new Thread() {
            public void run() {
                Log.d(TAG, "Thread run()");
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(IPAddress, PortNo));
                    out = socket.getOutputStream();
                    //Log.d("TMS","Receive");
                    in = socket.getInputStream();
                    String content;
                    BufferedReader inBuf = new BufferedReader(new InputStreamReader(in));
                    while (true) {
                        try {
                            content = inBuf.readLine();
                            if (content != null) {
                                Message msg = new Message();
                                msg.what = 0x345;
                                msg.obj = content;
                                Log.d("MainActivity", "Received:" + content);
                                //handler.sendMessage(msg);
                                String[] parts = content.split(",");
                                shareData.setTempAndHumi(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    Log.v("connect", e.toString());
                }
            }
        };
        thread.start();
    }



    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    public void onPause() {
        /*try {
            socket.close();
            socket = null;
        } catch (Exception e) {
        }*/
        super.onPause();
    }

    @Override
    public void onDestroy(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /** データ送信(模型車の進行方向の制御) */
    private void sendDirection(String message){
        Log.d(TAG,"sendDirection");
        byte[] send = message.getBytes();
        try{
            //データを送信する
            if(socket!=null && socket.isConnected()){
                Log.d(TAG,message);
                out.write(send);
                out.flush();
            }
        }
        catch(Exception e){
            Log.d(TAG,"送信失敗");
            e.printStackTrace();
        }
    }
}
