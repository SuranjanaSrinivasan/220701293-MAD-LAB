package com.example.ex10

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val alarmTimePicker: TimePicker = findViewById(R.id.timePicker)
        val btSetAlarm: Button = findViewById(R.id.btSetAlarm)
        val btStopAlarm: Button = findViewById(R.id.btnStopAlarm)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        btSetAlarm.setOnClickListener {
            Toast.makeText(applicationContext, "Alarm ON...!", Toast.LENGTH_LONG).show()

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.hour)
            calendar.set(Calendar.MINUTE, alarmTimePicker.minute)

            // Create intent for AlarmReceiver
            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(
                this.applicationContext, 2, intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Adjust time to the next valid time in minutes
            val time: Long = calendar.timeInMillis - (calendar.timeInMillis % 60000)

            // Use setExact to ensure it triggers exactly at the set time
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }

        btStopAlarm.setOnClickListener {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(applicationContext, "Alarm OFF...!", Toast.LENGTH_LONG).show()
        }
    }
}
