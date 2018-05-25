package com.xpping.windows10.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.xpping.windows10.BaseApplication;
import com.xpping.windows10.R;
import com.xpping.windows10.fragment.base.BaseFragment;
import com.xpping.windows10.utils.DensityUtils;
import com.xpping.windows10.widget.ToastView;
import com.windows.explorer.service.FTPServerService;

import org.swiftp.Defaults;
import org.swiftp.Globals;
import org.swiftp.UiUpdater;

import java.io.File;
import java.net.InetAddress;

/*
 *xlzhen 2018/4/27
 * 开启FTP 窗口
 */

public class NetWorkDeviceFragment extends BaseFragment {

    private TextView ipText;
    private TextView instructionText;
    private TextView instructionTextPre;

    private View startStopButton;

    private Activity mActivity;

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: // We are being told to do a UI update
                    // If more than one UI update is queued up, we only need to
                    // do one.
                    removeMessages(0);
                    updateUi();
                    break;
                case 1: // We are being told to display an error message
                    removeMessages(1);
            }
        }
    };
    @Override
    protected void initData() {
        //setActionBarTitle("FTP服务");
// Set the application-wide context global, if not already set
        Context myContext = Globals.getContext();
        if (myContext == null) {
            myContext = mActivity.getApplicationContext();
            if (myContext == null) {
                throw new NullPointerException("Null context!?!?!?");
            }
            Globals.setContext(myContext);
        }

        ipText = getFragmentView().findViewById(R.id.ip_address);
        instructionText = getFragmentView().findViewById(R.id.instruction);
        instructionTextPre = getFragmentView().findViewById(R.id.instruction_pre);
        startStopButton = getFragmentView().findViewById(R.id.start_stop_button);
        startStopButton.setOnClickListener(startStopListener);

        updateUi();
        UiUpdater.registerClient(handler);

        // quickly turn on or off wifi.
        getFragmentView().findViewById(R.id.wifi_state_image).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                android.provider.Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        UiUpdater.registerClient(handler);
        updateUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        UiUpdater.registerClient(handler);
        updateUi();
        // Register to receive wifi status broadcasts
        Log.i(Log.DEBUG+"", "Registered for wifi updates");
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mActivity.registerReceiver(wifiReceiver, filter);
    }

    /*
     * Whenever we lose focus, we must unregister from UI update messages from
     * the FTPServerService, because we may be deallocated.
     */
    @Override
    public void onPause() {
        super.onPause();
        UiUpdater.unregisterClient(handler);
        Log.i(Log.DEBUG+"", "Unregistered for wifi updates");
        mActivity.unregisterReceiver(wifiReceiver);
    }
    @Override
    public void onStop() {
        super.onStop();
        UiUpdater.unregisterClient(handler);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        UiUpdater.unregisterClient(handler);
    }

    /**
     * This will be called by the static UiUpdater whenever the service has
     * changed state in a way that requires us to update our UI. We can't use
     * any Log.i() calls in this function, because that will trigger an
     * endless loop of UI updates.
     */
    public void updateUi() {
        Log.i(Log.DEBUG+"", "Updating UI");

        WifiManager wifiMgr = (WifiManager) mActivity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        boolean isWifiReady = FTPServerService.isWifiEnabled();

        setText(R.id.wifi_state, isWifiReady ? wifiId : getString(R.string.no_wifi_hint));
        ImageView wifiImg = (ImageView) getFragmentView().findViewById(R.id.wifi_state_image);
        BaseApplication.imageLoader.displayImage("drawable://"+(isWifiReady ? R.drawable.wifi_state4 : R.drawable.wifi_state0)
                ,wifiImg);
        boolean running = FTPServerService.isRunning();
        if (running) {
            Log.i(Log.DEBUG+"", "updateUi: server is running");
            // Put correct text in start/stop button
            // Fill in wifi status and address
            InetAddress address = FTPServerService.getWifiIp();
            if (address != null) {
                String port = ":" + FTPServerService.getPort();

                ipText.setText("ftp://" + address.getHostAddress() + (FTPServerService.getPort() == 21 ? "" : port));
            } else {
                // could not get IP address, stop the service
                Context context = mActivity.getApplicationContext();
                Intent intent = new Intent(context, FTPServerService.class);
                context.stopService(intent);
                ipText.setText("");
            }
        }

        startStopButton.setEnabled(isWifiReady);
        TextView startStopButtonText = getFragmentView().findViewById(R.id.start_stop_button_text);
        if (isWifiReady) {
            startStopButtonText.setText(running ? R.string.stop_server : R.string.start_server);
            startStopButtonText.setCompoundDrawablesWithIntrinsicBounds(running ? R.drawable.disconnect
                    : R.drawable.connect, 0, 0, 0);
            startStopButtonText.setTextColor(running ? getResources().getColor(R.color.remote_disconnect_text)
                    : getResources().getColor(R.color.remote_connect_text));
        } else {
            if (FTPServerService.isRunning()) {
                Context context = mActivity.getApplicationContext();
                Intent intent = new Intent(context, FTPServerService.class);
                context.stopService(intent);
            }

            startStopButtonText.setText(R.string.no_wifi);
            startStopButtonText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            startStopButtonText.setTextColor(Color.GRAY);
        }

        ipText.setVisibility(running ? View.VISIBLE : View.INVISIBLE);
        instructionText.setVisibility(running ? View.VISIBLE : View.GONE);
        instructionTextPre.setVisibility(running ? View.GONE : View.VISIBLE);
    }



    private void setText(int id, String text) {
        TextView tv = getFragmentView().findViewById(id);
        tv.setText(text);
    }

    View.OnClickListener startStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            Globals.setLastError(null);
            File chrootDir = new File(Defaults.chrootDir);
            if (!chrootDir.isDirectory())
                return;

            Context context = mActivity.getApplicationContext();
            Intent intent = new Intent(context, FTPServerService.class);

            Globals.setChrootDir(chrootDir);
            if (!FTPServerService.isRunning()) {
                warnIfNoExternalStorage();
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    context.startService(intent);
                }
            } else {
                context.stopService(intent);
            }
        }
    };

    private void warnIfNoExternalStorage() {
        String storageState = Environment.getExternalStorageState();
        if (!storageState.equals(Environment.MEDIA_MOUNTED)) {
            Log.i("Warningduetostorage" , storageState);
            ToastView.getInstaller(mActivity).setText(R.string.storage_warning).show();
        }
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        public void onReceive(Context ctx, Intent intent) {
            Log.i(Log.DEBUG+"", "Wifi status broadcast received");
            updateUi();
        }
    };

    boolean requiredSettingsDefined() {
        SharedPreferences settings = mActivity.getSharedPreferences(Defaults.getSettingsName(), Defaults.getSettingsMode());
        String username = settings.getString("username", null);
        String password = settings.getString("password", null);
        if (username == null || password == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the settings from the FTPServerService if it's running, otherwise
     * load the settings directly from persistent storage.
     */
    SharedPreferences getSettings() {
        SharedPreferences settings = FTPServerService.getSettings();
        if (settings != null) {
            return settings;
        } else {
            return mActivity.getPreferences(Activity.MODE_PRIVATE);
        }
    }

    @Override
    protected void initWidget() {

    }

    @Override
    protected View setContentView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mActivity = getActivity();
        return setFragmentView(inflater.inflate(R.layout.fragment_network_device,container,false));
    }

    @Override
    protected void onClick(View view, int viewId) {

    }

    @Override
    public boolean onBackKey() {
        return true;
    }
}
