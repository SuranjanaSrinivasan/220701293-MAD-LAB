package com.example.realestateapp.screens

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.realestateapp.R
import com.example.realestateapp.model.House
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HouseListAdapter(private val context: Context, private val houses: ArrayList<House>) : BaseAdapter() {

    override fun getCount(): Int {
        return houses.size
    }

    override fun getItem(position: Int): Any {
        return houses[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.list_item_house, null)
        val titleText = view.findViewById<TextView>(R.id.houseTitleText)
        val wishlistButton = view.findViewById<ImageButton>(R.id.wishlistButton)

        val house = houses[position]
        titleText.text = "${house.title} - ₹${house.price}"

        // On Clicking the Row ➔ Open BookingActivity
        view.setOnClickListener {
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("houseId", house.id)
            intent.putExtra("houseTitle", house.title)
            intent.putExtra("houseDescription", house.description)
            intent.putExtra("housePrice", house.price)
            intent.putExtra("houseAddress", house.address)
            intent.putExtra("houseType", house.type)
            intent.putExtra("housePhoneNumber", house.phoneNumber)
            context.startActivity(intent)
        }

        wishlistButton.setOnClickListener {
            saveToWishlist(house)
            Toast.makeText(context, "Added to Wishlist ❤️", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun saveToWishlist(house: House) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val wishlistRef = FirebaseDatabase.getInstance().reference.child("wishlist").child(userId)
        wishlistRef.child(house.id).setValue(house)
    }
}
