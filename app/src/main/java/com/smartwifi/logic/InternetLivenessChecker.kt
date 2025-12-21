package com.smartwifi.logic

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class InternetLivenessChecker @Inject constructor() {

    suspend fun hasInternetAccess(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()
                true
            } catch (e: IOException) {
                false
            }
        }
    }
    
    // Logic for Zombie detection and Probation can be added here
}
