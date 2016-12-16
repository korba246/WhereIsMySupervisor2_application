package com.example.korba.gdziejestmojpromotor2.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by korba on 16.12.16.
 */

public class RouterService {

    public String getRouterBSSID(Context myContext) {

        WifiManager wifiManager = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo;
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                String ssid = wifiInfo.getSSID();
                String bssid = wifiInfo.getBSSID();
                return bssid;
            }
        }else return "WiFiDisabled";
        return "NoConnection";



    }

    public Boolean Reconnect(Context myContext){
        String bssid;
        WifiManager wifiManager = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            // Level of current connection
            int rssi = wifiManager.getConnectionInfo().getRssi();
            String bssi = wifiManager.getConnectionInfo().getSSID();
            int mlevel = WifiManager.calculateSignalLevel(rssi, 10);

            // Level of a Scan Result
            List<ScanResult> wifiList = wifiManager.getScanResults();
            for (ScanResult scanResult : wifiList) {
                int level = WifiManager.calculateSignalLevel(scanResult.level, 10);
                if (scanResult.SSID.equals("PWR-WiFi") && level > mlevel) {
                    WifiConfiguration wifiConfig = new WifiConfiguration();
                    wifiConfig.SSID = String.format("\"%s\"", scanResult.SSID);
                    wifiConfig.BSSID = String.format("\"%s\"", scanResult.BSSID);
                    wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    int netId = wifiManager.addNetwork(wifiConfig);
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(netId, true);
                    wifiManager.reconnect();
                    break;
                }
            }
            return true;
        }else return false;
    }
}