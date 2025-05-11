package com.example.realestateapp.screens

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.realestateapp.R
import com.example.realestateapp.model.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyBookingsActivity : AppCompatActivity() {

    private lateinit var bookingsListView: ListView
    private lateinit var bookingsList: ArrayList<House>
    private lateinit var adapter: HouseListAdapter
    private lateinit var database: DatabaseReference
    private lateinit var housesDatabase: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bookings)

        bookingsListView = findViewById(R.id.bookingsListView)
        bookingsList = ArrayList()
        adapter = HouseListAdapter(this, bookingsList)
        bookingsListView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference.child("bookings")
        housesDatabase = FirebaseDatabase.getInstance().reference.child("houses")
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        loadMyBookings()
    }

    private fun loadMyBookings() {
        database.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookingsList.clear()
                    for (bookingSnap in snapshot.children) {
                        val houseId = bookingSnap.child("houseId").getValue(String::class.java) ?: continue
                        housesDatabase.child(houseId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(houseSnapshot: DataSnapshot) {
                                val house = houseSnapshot.getValue(House::class.java)
                                house?.let { bookingsList.add(it) }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
