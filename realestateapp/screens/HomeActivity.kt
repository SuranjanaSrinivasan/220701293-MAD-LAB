package com.example.realestateapp.screens

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.realestateapp.R
import com.example.realestateapp.model.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var houseListView: ListView
    private lateinit var addHouseButton: Button
    private lateinit var viewMyBookingsButton: Button
    private lateinit var viewWishlistButton: Button
    private lateinit var searchView: SearchView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var fullHouseList: ArrayList<House> // Full complete list
    private lateinit var displayedHouseList: ArrayList<House> // Filtered list shown
    private lateinit var adapter: HouseListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("houses")

        houseListView = findViewById(R.id.houseListView)
        addHouseButton = findViewById(R.id.addHouseButton)
        viewMyBookingsButton = findViewById(R.id.viewMyBookingsButton)
        viewWishlistButton = findViewById(R.id.viewWishlistButton)
        searchView = findViewById(R.id.searchView)

        fullHouseList = ArrayList()
        displayedHouseList = ArrayList()

        adapter = HouseListAdapter(this, displayedHouseList)
        houseListView.adapter = adapter

        addHouseButton.setOnClickListener {
            startActivity(Intent(this, AddHouseActivity::class.java))
        }

        viewMyBookingsButton.setOnClickListener {
            startActivity(Intent(this, MyBookingsActivity::class.java))
        }

        viewWishlistButton.setOnClickListener {
            startActivity(Intent(this, WishlistActivity::class.java))
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        loadHouses()
    }

    private fun loadHouses() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fullHouseList.clear()
                for (houseSnap in snapshot.children) {
                    val house = houseSnap.getValue(House::class.java)
                    house?.let { fullHouseList.add(it) }
                }

                // Always show full list initially
                displayedHouseList = ArrayList(fullHouseList)
                adapter = HouseListAdapter(this@HomeActivity, displayedHouseList)
                houseListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })
    }

    private fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            displayedHouseList = ArrayList(fullHouseList)
        } else {
            val filteredList = ArrayList<House>()
            for (house in fullHouseList) {
                if (house.address.contains(query, ignoreCase = true)) {
                    filteredList.add(house)
                }
            }
            displayedHouseList = filteredList
        }

        adapter = HouseListAdapter(this, displayedHouseList)
        houseListView.adapter = adapter
    }
}
