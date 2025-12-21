package com.smartwifi.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SmartWifi Manager",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Status: Active", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Current Network: Searching...")
                Text(text = "Signal Quality: --")
                Text(text = "Internet Status: Checking...")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = "Active Mode: Stationary (Home/Office)")
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = { /* TODO: Toggle Service */ }) {
            Text("Stop Service")
        }
    }
}
