package com.example.towntalkdashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.firebase.firestore.FirebaseFirestore

class SubmitReportPage : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var uploadMediaButton: LinearLayout
    private lateinit var uploadMediaImage: ImageView

    private var mediaUri: Uri? = null

    companion object {
        const val PICK_MEDIA_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_report_page)

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Get a Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Reference to the "reports" collection
        val reportsCollection = db.collection("reports")
        // Handle the submit button click
        titleEditText = findViewById<EditText>(R.id.topicReportInput)
        descriptionEditText = findViewById<EditText>(R.id.descriptionReportInput)
        uploadMediaButton = findViewById<LinearLayout>(R.id.uploadMediaButton)
        uploadMediaImage = findViewById<ImageView>(R.id.uploadMediaImage)
        submitButton = findViewById<Button>(R.id.submitButton)

        // Handle media upload button click
        uploadMediaButton.setOnClickListener {
            // Open a file picker or camera app to select media
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.type = "image/*" // You can adjust the MIME type to your needs
            startActivityForResult(mediaIntent, PICK_MEDIA_REQUEST)
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                if (mediaUri != null) {
                    // Media is selected, upload it to Firebase Storage or your preferred service
                    // For Firebase Storage upload, refer to Firebase documentation
                    // ...

                    // Once the media is uploaded, you can get the download URL
                    val mediaURL = "your_media_download_url_here"

                    // Create a new report document
                    val report = hashMapOf(
                        "title" to title,
                        "description" to description,
                        "mediaURL" to mediaURL
                    )

                    // Add the report to Firestore
                    reportsCollection.add(report)
                        .addOnSuccessListener { documentReference ->
                            // Report added successfully
                            // Optionally, navigate back to HomeFragment or refresh the list.
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                } else {
                    // Media is not selected, you can proceed without media
                    // Create a new report document
                    val report = hashMapOf(
                        "title" to title,
                        "description" to description
                    )
                    // Add the report to Firestore
                    reportsCollection.add(report)
                        .addOnSuccessListener { documentReference ->
                            // Report added successfully
                            // Optionally, navigate back to HomeFragment or the main activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Close the SubmitReportPage
                        }
                        .addOnFailureListener { e ->
                            // Handle the error
                        }
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            mediaUri = data?.data

            // Update the UI to indicate that media is selected (e.g., change the ImageView)
            uploadMediaImage.setImageResource(R.drawable.baseline_cloud_done_24)
        }
    }
}