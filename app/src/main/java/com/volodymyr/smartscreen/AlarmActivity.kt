package com.volodymyr.smartscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import org.jetbrains.anko.AnkoLogger
import java.util.*


class AlarmActivity : Activity(), AnkoLogger {
    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_ON = "on"
        const val EXTRA_OFF = "off"
    }

    @SuppressLint("WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_WEEK)
        var isEnabled = false
        when (day) {
            Calendar.MONDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.MON)
            Calendar.TUESDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.TUE)
            Calendar.WEDNESDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.WED)
            Calendar.THURSDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.THU)
            Calendar.FRIDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.FRI)
            Calendar.SATURDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.SAT)
            Calendar.SUNDAY -> isEnabled = PrefUtils(this).getBoolean(Constants.SUN)
        }

        if (!isEnabled) {
            finish()
            return
        }

        if (intent.extras.isEmpty) {
            throw IllegalArgumentException("")
        } else if (intent.extras.getString(EXTRA_TYPE) == EXTRA_ON) { // unlock screen
            val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
            val wake = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG")
            wake.acquire()
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        } else if (intent.extras.getString(EXTRA_TYPE) == EXTRA_OFF) { // lock screen
            val compName = ComponentName(this, SmartScreenAdminReceiver::class.java)
            val deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val active: Boolean = deviceManger.isAdminActive(compName)
            if (active) {
                deviceManger.lockNow()
            }
        }
        finish()
    }
}