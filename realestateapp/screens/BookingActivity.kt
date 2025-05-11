package com.example.realestateapp.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.realestateapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.Manifest
import android.content.pm.PackageManager
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BookingActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var addressTextView: TextView
    private lateinit var typeTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var callOwnerButton: Button
    private lateinit var bookButton: Button

    private lateinit var houseId: String
    private lateinit var housePhoneNumber: String
    private lateinit var userId: String
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        priceTextView = findViewById(R.id.priceTextView)
        addressTextView = findViewById(R.id.addressTextView)
        typeTextView = findViewById(R.id.typeTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        callOwnerButton = findViewById(R.id.callOwnerButton)
        bookButton = findViewById(R.id.bookButton)

        // Get data from intent
        houseId = intent.getStringExtra("houseId") ?: ""
        val houseTitle = intent.getStringExtra("houseTitle") ?: ""
        val houseDescription = intent.getStringExtra("houseDescription") ?: ""
        val housePrice = intent.getStringExtra("housePrice") ?: ""
        val houseAddress = intent.getStringExtra("houseAddress") ?: ""
        val houseType = intent.getStringExtra("houseType") ?: ""
        housePhoneNumber = intent.getStringExtra("housePhoneNumber") ?: ""

        // Set data to views
        titleTextView.text = houseTitle
        descriptionTextView.text = houseDescription
        priceTextView.text = "Price: $housePrice"
        addressTextView.text = "Address: $houseAddress"
        typeTextView.text = "Type: $houseType"
        phoneNumberTextView.text = "Owner Phone: $housePhoneNumber"

        // Get current user info
        val currentUser = FirebaseAuth.getInstance().currentUser
        userId = currentUser?.uid ?: ""
        userEmail = currentUser?.email ?: "A user"

        // Set click listeners
        callOwnerButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$housePhoneNumber")
            startActivity(intent)
        }

        bookButton.setOnClickListener {
            bookHouse()
        }
    }

    private fun bookHouse() {
        val bookingRef = FirebaseDatabase.getInstance().reference.child("bookings").push()
        val bookingData = mapOf(
            "houseId" to houseId,
            "userId" to userId
        )

        bookingRef.setValue(bookingData)
            .addOnSuccessListener {
                Toast.makeText(this, "House Booked Successfully!", Toast.LENGTH_SHORT).show()
                sendSmsToOwner()
                finish()
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Booking Failed: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendSmsToOwner() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email ?: "A user"
        val smsBody = "$userEmail has booked your house."

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, ask for it
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 101)
        } else {
            // Permission already granted
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(housePhoneNumber, null, smsBody, null, null)
            Toast.makeText(this, "Owner Notified via SMS!", Toast.LENGTH_SHORT).show()
        }
    }

}

