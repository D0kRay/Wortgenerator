package com.example.wortgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ServiceConsumer extends Activity {

    private WordService s;
    private ArrayList<String> values;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceconsumer);
        doBindService();
        values = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            s = ((WordService.MyBinder) iBinder).getService();
            Toast.makeText(ServiceConsumer.this, "Verbunden", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            s = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(this, WordService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    public void showServiceData(View view) {
        //onClick Methode
        if (s != null) {
            List<String> wordList = s.getWordList();
            values.clear();
            values.addAll(wordList);

            //Informiere den Observer, dass die Daten geändert wurden und der View sollte aktualisiert werden
            adapter.notifyDataSetChanged();
        }
    }
}