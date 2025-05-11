package com.example.ex9

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // ✅ fix this

        val sms = findViewById<EditText>(R.id.edit_Text) // ✅ fix this
        val alert = findViewById<Button>(R.id.alert_Button)
        val clear = findViewById<Button>(R.id.clear_Button)

        alert.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(sms.text.toString())
                .setPositiveButton("OK") { dialog, which ->
                    Toast.makeText(this, "Alert Dialog Closed", Toast.LENGTH_SHORT).show()

                    val smsIntent = Intent(this, SmsAlert::class.java) // ✅ fix Intent
                    smsIntent.putExtra("message", sms.text.toString())
                    val alertBox = builder.create()
                    alertBox.show()
                }
        }

        clear.setOnClickListener {
            sms.setText("")
            Toast.makeText(this, "Message cleared", Toast.LENGTH_SHORT).show()
        }
    }
}
