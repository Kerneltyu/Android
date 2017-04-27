package com.example.okudo.tcp_guest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by okudo on 2017/01/13.
 */

public class ConnectThread implements Runnable {
    final String IPAddress ="192.168.0.21";
    final int tcpPort = 65432;
    //ServerSocket serverSocket;
    Socket connectedSocket;
    ServerSocket receiveTcpSocket;
    Handler handler;
    public Handler revHandler;
    OutputStream os = null;
    InputStream is = null;

    public ConnectThread(Handler handler){
        this.handler = handler;
    }

    public void run(){
        new Thread() {
            @Override
            public void run() {
                //TextView tv = (TextView)findViewById(R.id.textView2);
                try {
                    String content = null;
                    receiveTcpSocket = new ServerSocket(tcpPort);
                    receiveTcpSocket.accept();
                    //os = connectedSocket.getOutputStream();
                    //is = connectedSocket.getInputStream();
                    byte [] buffer = new byte[1024];
                    int len=-1;
/*
                    Looper.prepare();
                    revHandler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            if(msg.what == 0x345){
                                try{
                                    os.write((msg.obj.toString()+"\r\n").getBytes("utf-8"));
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    len = is.read(buffer);
                        Message msg = new Message();
                        msg.what = 0x123;
                        msg.obj = buffer;
                        handler.sendMessage(msg);
                        Log.d("MainActivity", "Received:" );
                    Looper.loop();
*/
                    while(true) {
                        len = is.read(buffer);
                        Message msg = new Message();
                        msg.what = 0x123;
                        msg.obj = buffer;
                        handler.sendMessage(msg);
                        Log.d("MainActivity", "Received:" );
                    }

                }catch(SocketTimeoutException e1){
                    System.out.println("TIME OUT!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
