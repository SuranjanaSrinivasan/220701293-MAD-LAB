package com.example.realestateapp.screens

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.realestateapp.R
import com.example.realestateapp.model.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddHouseActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var typeSpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var phoneNumberEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priceEditText = findViewById(R.id.priceEditText)
        addressEditText = findViewById(R.id.addressEditText)
        typeSpinner = findViewById(R.id.typeSpinner)
        submitButton = findViewById(R.id.submitButton)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)


        val types = arrayOf("Rent", "Sale")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        submitButton.setOnClickListener {
            uploadHouse()
        }
    }

    private fun uploadHouse() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val price = priceEditText.text.toString()
        val address = addressEditText.text.toString()
        val type = typeSpinner.selectedItem.toString()
        val phoneNumber = phoneNumberEditText.text.toString()


        if (title.isEmpty() || description.isEmpty() || price.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val houseId = UUID.randomUUID().toString()
        val ownerId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        val house = House(
            id = houseId,
            title = title,
            description = description,
            price = price,
            address = address,
            type = type,
            ownerId = ownerId,
            imageUrl = "", // (still empty now)
            phoneNumber = phoneNumber // ✅ Save phone number
        )

        FirebaseDatabase.getInstance().reference.child("houses").child(houseId).setValue(house)
            .addOnSuccessListener {
                Toast.makeText(this, "House Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to Add House", Toast.LENGTH_SHORT).show()
            }
    }
}
