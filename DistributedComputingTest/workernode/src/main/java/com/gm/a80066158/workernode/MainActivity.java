package com.gm.a80066158.workernode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.gm.a80066158.rmiutil.IProgress;
import com.gm.a80066158.rmiutil.RMIService;
import com.gm.sdd.server.SDDServer;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements IProgress {

    private static final String TAG = "MainActivity";
    private static final String SDD_DEVCIE_TYPE = "DistributedComputingTest";

    private SDDServer sddServer = null;
    private TextView mInfoTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();

        startService();

        startSDDServer();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        mInfoTextView = (TextView) findViewById(R.id.info);
    }

    private void startService() {
        Log.i(TAG, "<startService> start");

        RMIService rmiService = new RMIService();
        rmiService.setProgressListener(this);
    }

    private void startSDDServer() {
        Log.i(TAG, "<startSDDServer> start");

        sddServer = new SDDServer(this);
        sddServer.setDeviceType(SDD_DEVCIE_TYPE);
        sddServer.start();
    }

    @Override
    public void progressUpdate(final float progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mInfoTextView.setText((progress * 100) + "%100");
            }
        });
    }
}
