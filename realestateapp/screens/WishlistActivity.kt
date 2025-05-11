package com.example.realestateapp.screens

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.realestateapp.R
import com.example.realestateapp.model.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WishlistActivity : AppCompatActivity() {

    private lateinit var wishlistListView: ListView
    private lateinit var wishlist: ArrayList<House>
    private lateinit var adapter: HouseListAdapter
    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wishlist)

        wishlistListView = findViewById(R.id.wishlistListView)
        wishlist = ArrayList()
        adapter = HouseListAdapter(this, wishlist)
        wishlistListView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference.child("wishlist")
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        loadWishlist()
    }

    private fun loadWishlist() {
        database.child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    wishlist.clear()
                    for (houseSnap in snapshot.children) {
                        val house = houseSnap.getValue(House::class.java)
                        house?.let { wishlist.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
