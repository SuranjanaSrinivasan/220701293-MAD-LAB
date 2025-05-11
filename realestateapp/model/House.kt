package com.example.realestateapp.model

data class House(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val address: String = "",
    val type: String = "",
    val ownerId: String = "",
    val imageUrl: String = "",
    val phoneNumber: String = "" // ✅ Added owner's phone number
)
