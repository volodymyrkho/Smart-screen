package com.volodymyr.smartscreen

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SmartScreenAdminReceiver : DeviceAdminReceiver() {

    override fun onEnabled(context: Context?, intent: Intent?) {
        super.onEnabled(context, intent)
        Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show()
    }
}