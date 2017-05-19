package com.gm.a80066158.distributedcomputingtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gm.a80066158.rmiutil.IRMIService;
import com.gm.sdd.client.SDDClient;
import com.gm.sdd.client.SDDDeviceManager;
import com.gm.sdd.common.SDDDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SDD_SEARCH_TYPE = "DistributedComputingTest";
    private static final int SDD_RMI_PORT = 7777;

    private Button button = null;
    private SDDClient sddClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSDDClient();

        initContent();
    }

    private void startSDDClient() {
        Log.i(TAG, "<startSDDClient> start");

        sddClient = new SDDClient(this, SDD_SEARCH_TYPE);
        sddClient.start();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "<button:onClick> start");

                getPrimeNumbers(2, 100000);
            }
        });
    }

    private void getPrimeNumbers(int from, int to) {
        Log.i(TAG, "<getPrimeNumbers> start");

        List<SDDDevice> workerNodeList = SDDDeviceManager.getDeviceList();
        if (null == workerNodeList) {
            Log.w(TAG, "<getPrimeNumbers> workerNodeList is null");
            return;
        }

        int workerNodeCount = workerNodeList.size();
        List<List<Integer>> numSplits = new ArrayList<>();
        for (int i = 0; i < workerNodeCount; i++) {
            List<Integer> numList = new ArrayList<>();
            numSplits.add(numList);
        }

        for (int i = from; i <= to; i++) {
            numSplits.get(i % workerNodeCount).add(i);
        }

        for (int i = 0; i < workerNodeCount; i++) {
            getPrimeNumbersRemote(numSplits.get(i), workerNodeList.get(i).getIp());
        }
    }

    private void getPrimeNumbersRemote(final int from, final int to, final String remoteIp) {
        final CallHandler callHandler = new CallHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = null;
                try {
                    client = new Client(remoteIp, SDD_RMI_PORT, callHandler);
                    IRMIService testService = (IRMIService) client.getGlobal(IRMIService.class);
                    List<Integer> primeNumbers = testService.getPrimeNumber(from, to);
                    Log.i(TAG, "<getPrimeNumbersRemote> " + remoteIp + ", " + from + ", " + to);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getPrimeNumbersRemote(final List<Integer> numList, final String remoteIp) {
        final CallHandler callHandler = new CallHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Client client = null;
                try {
                    client = new Client(remoteIp, SDD_RMI_PORT, callHandler);
                    IRMIService testService = (IRMIService) client.getGlobal(IRMIService.class);
                    List<Integer> primeNumbers = testService.getPrimeNumber(numList);
                    Log.i(TAG, "<getPrimeNumbersRemote> " + remoteIp);
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
