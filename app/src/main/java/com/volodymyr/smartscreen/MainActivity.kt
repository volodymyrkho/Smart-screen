package com.volodymyr.smartscreen

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CompoundButton
import com.volodymyr.smartscreen.AlarmActivity.Companion.EXTRA_OFF
import com.volodymyr.smartscreen.AlarmActivity.Companion.EXTRA_ON
import com.volodymyr.smartscreen.AlarmActivity.Companion.EXTRA_TYPE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    companion object {
        const val REQUEST_CODE_ENABLE_ADMIN = 0

        var REQUEST_CODE_ON = 101
        var REQUEST_CODE_OFF = 201
    }

    private lateinit var alarmManagerOn: AlarmManager
    private lateinit var alarmManagerOff: AlarmManager
    private val prefUtils = PrefUtils(this)

    @SuppressLint("WakelockTimeout", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManagerOn = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManagerOff = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        button.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = textViewOn.text.subSequence(0, 2).toString().toInt()
            val minute = textViewOn.text.subSequence(3, 5).toString().toInt()
            calendar.set(Calendar.HOUR, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            setAlarmOn(calendar)

            val calendarOff = Calendar.getInstance()
            val hourOff = textViewOff.text.subSequence(0, 2).toString().toInt()
            val minuteOff = textViewOff.text.subSequence(3, 5).toString().toInt()
            calendarOff.set(Calendar.HOUR, hourOff)
            calendarOff.set(Calendar.MINUTE, minuteOff)
            calendarOff.set(Calendar.SECOND, 0)
            setAlarmOff(calendarOff)

        }

        buttonSettings.setOnClickListener {
            // Launch the activity to have the user enable our admin.
            val compName = ComponentName(this, SmartScreenAdminReceiver::class.java)
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "To enable feature")
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
        }

        textViewOn.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val time = getString(R.string.time_format, hourOfDay, minute)
                textViewOn.text = time
                prefUtils.setString(Constants.TIME_ON, time)
            }, calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    false).show()

        }

        textViewOff.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val time = getString(R.string.time_format, hourOfDay, minute)
                textViewOff.text = time
                prefUtils.setString(Constants.TIME_OFF, time)
            }, calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    false).show()
        }
        init()
    }

    override fun onCheckedChanged(view: CompoundButton, isChecked: Boolean) {
        when (view.id) {
            R.id.monCheckBox -> prefUtils.setBoolean(Constants.MON, isChecked)
            R.id.tueCheckBox -> prefUtils.setBoolean(Constants.TUE, isChecked)
            R.id.wedCheckBox -> prefUtils.setBoolean(Constants.WED, isChecked)
            R.id.thuCheckBox -> prefUtils.setBoolean(Constants.THU, isChecked)
            R.id.friCheckBox -> prefUtils.setBoolean(Constants.FRI, isChecked)
            R.id.satCheckBox -> prefUtils.setBoolean(Constants.SAT, isChecked)
            R.id.sunCheckBox -> prefUtils.setBoolean(Constants.SUN, isChecked)
        }
    }

    private fun init() {
        monCheckBox.setOnCheckedChangeListener(this)
        tueCheckBox.setOnCheckedChangeListener(this)
        wedCheckBox.setOnCheckedChangeListener(this)
        thuCheckBox.setOnCheckedChangeListener(this)
        friCheckBox.setOnCheckedChangeListener(this)
        satCheckBox.setOnCheckedChangeListener(this)
        sunCheckBox.setOnCheckedChangeListener(this)

        monCheckBox.isChecked = prefUtils.getBoolean(Constants.MON)
        tueCheckBox.isChecked = prefUtils.getBoolean(Constants.TUE)
        wedCheckBox.isChecked = prefUtils.getBoolean(Constants.WED)
        thuCheckBox.isChecked = prefUtils.getBoolean(Constants.THU)
        friCheckBox.isChecked = prefUtils.getBoolean(Constants.FRI)
        satCheckBox.isChecked = prefUtils.getBoolean(Constants.SAT)
        sunCheckBox.isChecked = prefUtils.getBoolean(Constants.SUN)

        textViewOn.text = prefUtils.getString(Constants.TIME_ON, getString(R.string.hour_on))
        textViewOff.text = prefUtils.getString(Constants.TIME_OFF, getString(R.string.hour_off))
    }

    private fun setAlarmOn(calendar: Calendar) {
        val intent = Intent(this, AlarmActivity::class.java)
        intent.putExtra(EXTRA_TYPE, EXTRA_ON)
        val alarmIntent = PendingIntent.getActivity(this, REQUEST_CODE_ON++, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManagerOn.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_HALF_HOUR, alarmIntent)
    }

    private fun setAlarmOff(calendar: Calendar) {
        val intent = Intent(this, AlarmActivity::class.java)
        intent.putExtra(EXTRA_TYPE, EXTRA_OFF)
        val alarmIntent = PendingIntent.getActivity(this, REQUEST_CODE_OFF++, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManagerOff.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_HALF_HOUR, alarmIntent)
    }
}
