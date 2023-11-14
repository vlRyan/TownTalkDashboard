package com.example.towntalkdashboard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class HomeReadReportPage : AppCompatActivity() {
    private lateinit var removeButton: Button
    private lateinit var editButton: Button
    private lateinit var responseEditText: EditText
    private var isEditing = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_read_report_page)

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
        }

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val dateTextView = findViewById<TextView>(R.id.dateTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val mediaImageView = findViewById<ImageView>(R.id.mediaImageView)

        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val description = intent.getStringExtra("description")
        val mediaURL = intent.getStringExtra("mediaURL")

        titleTextView.text = title
        dateTextView.text = date
        descriptionTextView.text = description

        removeButton = findViewById(R.id.remove_post_button)
        editButton = findViewById(R.id.edit_post_button)
        responseEditText = findViewById(R.id.response)

        if (!mediaURL.isNullOrEmpty()) {
            mediaImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(mediaURL)
                .into(mediaImageView)
        }

        if (mediaURL != null) {
            mediaImageView.visibility = View.VISIBLE
        }

        val container = findViewById<LinearLayout>(R.id.container)
        // Fetch barangay response from Firestore and update the TextView
        val db = FirebaseFirestore.getInstance()
        val reportsCollection = db.collection("reports")

        reportsCollection
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document in querySnapshot) {
                    val barangayResponse = document.getString("barangayResponse")
                    if (barangayResponse != null) {
                        responseEditText.setText(barangayResponse)
                        container.visibility = View.VISIBLE
                    }
                }
            }
            .addOnFailureListener { e: Exception ->
                // Handle the error
            }

        val user = FirebaseAuth.getInstance().currentUser
        if (user?.email == LoginPage.ADMIN_EMAIL) {
            // Admin user, load admin layout
            container.visibility = View.VISIBLE

            editButton.visibility = View.VISIBLE
            removeButton.visibility = View.VISIBLE
        } else {
            // Regular user, load regular layout
        }

        responseEditText.isFocusable = false

        editButton.setOnClickListener {
            if (!isEditing) {
                // Enable editing of the response EditText
                responseEditText.isEnabled = true
                responseEditText.isFocusable = true
                responseEditText.isFocusableInTouchMode = true
                responseEditText.requestFocus()

                // Change the button text to "Save" to indicate saving changes
                editButton.text = "Save"

                isEditing = true
            } else {
                // Save the modified text to Firestore
                val modifiedResponse = responseEditText.text.toString()
                updateReportWithResponse(title, modifiedResponse)

                // Disable editing after saving changes
                responseEditText.isEnabled = false
                responseEditText.isFocusable = false
                responseEditText.isFocusableInTouchMode = false

                responseEditText.clearFocus()

                // Change the button text back to "Edit"
                editButton.text = "Edit"

                isEditing = false
            }
        }

        removeButton.setOnClickListener {
            deleteReport()

            val intent = Intent (this, Navigation::class.java)
            startActivity(intent)
        }
    }

    private fun deleteReport() {
        val db = FirebaseFirestore.getInstance()
        val reportsCollection = db.collection("reports")

        val title = intent.getStringExtra("title")

        // Delete the report
        reportsCollection.whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val reportId = document.id
                    reportsCollection.document(reportId).delete()
                        .addOnSuccessListener {
                            // Report deleted successfully
                            finish() // Finish the activity after deleting the report
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }

    private fun updateReportWithResponse(title: String?, barangayResponse: String) {
        val db = FirebaseFirestore.getInstance()
        val reportsCollection = db.collection("reports")

        reportsCollection
            .whereEqualTo("title", title)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val reportId = document.id

                    // Update the "barangayResponse" field with the modified response
                    reportsCollection.document(reportId)
                        .update("barangayResponse", barangayResponse)
                        .addOnSuccessListener {
                            // Report updated successfully
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle the error
            }
    }
}
