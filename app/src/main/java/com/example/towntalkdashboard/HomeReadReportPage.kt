package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth

class HomeReadReportPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_read_report_page)

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            val intent = Intent (this, Navigation::class.java)
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

        if (!mediaURL.isNullOrEmpty()) {
            mediaImageView.visibility = View.VISIBLE
            Glide.with(this)
                .load(mediaURL)
                .into(mediaImageView)
        }

        val user = FirebaseAuth.getInstance().currentUser
        if (user?.email == LoginPage.ADMIN_EMAIL) {
            // Admin user, load admin layout
            val container = findViewById<LinearLayout>(R.id.container)
            container.visibility = View.VISIBLE

            val barangayResponseText = findViewById<TextView>(R.id.barangay_response)
            val responseText = findViewById<TextView>(R.id.response)
            val editPostButton = findViewById<Button>(R.id.edit_post_button)
            val removePostButton = findViewById<Button>(R.id.remove_post_button)
            barangayResponseText.visibility = View.VISIBLE
            responseText.visibility = View.VISIBLE
            editPostButton.visibility = View.VISIBLE
            removePostButton.visibility = View.VISIBLE
        } else {
            // Regular user, load regular layout

        }
    }
}