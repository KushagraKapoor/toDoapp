package com.example.todoapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.todoapp.R

import com.google.firebase.database.*

class ProfileFragment : Fragment() {
       private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var databaseReference: DatabaseReference
    private lateinit var profileName: TextView
    private lateinit var profileDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("YOUR_DATABASE_REFERENCE")

        profileName = view.findViewById(R.id.profileName)
        profileDescription = view.findViewById(R.id.email)

        // Fetch user's name from Firebase Authentication
        val userName = user.displayName
        profileName.text = userName

        // Fetch additional user information from Firebase Database
        fetchAdditionalUserInfo()

        return view
    }

    private fun fetchAdditionalUserInfo() {
        // Use the user's UID to query the database for additional information
        val query: Query = databaseReference.orderByChild("uid").equalTo(user.uid)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshot1 in dataSnapshot.children) {
                    // Retrieve the name from the database
                    val nameFromDatabase = dataSnapshot1.child("name").getValue(String::class.java)

                    // Update the UI with the retrieved name
                    if (!nameFromDatabase.isNullOrBlank()) {
                        profileName.text = nameFromDatabase
                    }

                    // You can also retrieve other information if needed and update the UI accordingly
                    val description = dataSnapshot1.child("description").getValue(String::class.java)
                    if (!description.isNullOrBlank()) {
                        profileDescription.text = description
                    }
                }
            }



            override fun onCancelled(databaseError: DatabaseError) {
                // Handle cancelled event
            }
        })
    }


}