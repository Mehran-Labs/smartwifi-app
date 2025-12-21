package com.smartwifi.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.smartwifi.logic.SignalMonitor
import com.smartwifi.logic.InternetLivenessChecker
import com.smartwifi.logic.UserContextMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.util.Log

@AndroidEntryPoint
class SmartWifiService : Service() {

    @Inject lateinit var signalMonitor: SignalMonitor
    @Inject lateinit var internetChecker: InternetLivenessChecker
    @Inject lateinit var userContextMonitor: UserContextMonitor

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SmartWifiService", "Service Started")
        startMonitoring()
        return START_STICKY
    }

    private fun startMonitoring() {
        serviceScope.launch {
            // Main State Machine Loop
            while (true) {
                if (userContextMonitor.isGamingMode()) {
                    Log.d("SmartWifiService", "Gaming Mode: Paused Scanning")
                } else {
                    val rssi = signalMonitor.getRssi()
                    val hasInternet = internetChecker.hasInternetAccess()
                    
                    Log.d("SmartWifiService", "Monitoring: RSSI=$rssi, Internet=$hasInternet")
                    
                    // --- Scenario C: Safety Net (Own Mobile Data) ---
                    if (!hasInternet && rssi < -80) {
                         // Weak signal AND no internet -> Switch to Data
                         Log.i("SmartWifiService", "Scenario C: Safety Net triggered. Switching to Mobile Data.")
                         // TODO: implementation for disabling Wifi to force Data
                    }

                    // --- Scenario B: Travel Mode (Zombie Detection) ---
                    if (rssi > -60 && !hasInternet) {
                        // Strong Signal BUT No Internet -> Zombie Hotspot
                        Log.i("SmartWifiService", "Scenario B: Zombie Hotspot detected. Entering Probation.")
                        // TODO: implementation for disconnecting and scheduling a re-check in 2 minutes
                    }

                    // --- Scenario A: Stationary Mode (Sticky Client) ---
                    // This requires scanning for other networks and comparing RSSI
                    // Placeholder for "Better Network Threshold" logic
                    /*
                    val bestNetwork = wifiScanner.getBestAvailableNetwork()
                    if (signalMonitor.isSignificantlyBetter(bestNetwork.level, rssi)) {
                        Log.i("SmartWifiService", "Scenario A: Better network found. Switching.")
                        // connectTo(bestNetwork)
                    }
                    */
                }
                kotlinx.coroutines.delay(5000) // Check every 5 seconds
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}
