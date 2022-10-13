package com.example.wortgenerator;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WordService extends Service {

    private Timer timer = new Timer();
    private static final long UPDATE_INTERVAL = 5000;
    private final IBinder mBinder = new MyBinder();
    private ArrayList<String> list = new ArrayList<String>();
    private String[] fixedList = {"Eins", "Zwei", "Drei", "Vier", "Fünf", "Sechs"};
    private int index = 0;

    public void onCreate() {
        super.onCreate();
        pollForUpdates();
    }

    private void pollForUpdates() {
        //Starte einen asynchronen Task
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Ändere die Liste
                if (list.size() >= 6) {
                    list.remove(0);
                }
                list.add(fixedList[index++]);
                if (index >= fixedList.length) {
                    index = 0;
                }
            }
        }, 0 , UPDATE_INTERVAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        WordService getService() {
            return WordService.this;
        }
    }

    public List<String> getWordList() {
        return list;
    }
}
