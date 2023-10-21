package com.example.towntalkdashboard

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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

        val db = FirebaseFirestore.getInstance()
        // Reference to the "reports" collection
        val reportsCollection = db.collection("reports")

        titleEditText = findViewById<EditText>(R.id.topicReportInput)
        descriptionEditText = findViewById<EditText>(R.id.descriptionReportInput)
        uploadMediaButton = findViewById<LinearLayout>(R.id.uploadMediaButton)
        uploadMediaImage = findViewById<ImageView>(R.id.uploadMediaImage)
        submitButton = findViewById<Button>(R.id.submitButton)

        // Handle media upload button click
        uploadMediaButton.setOnClickListener {
            // Open a file picker or camera app to select media
            val mediaIntent = Intent(Intent.ACTION_GET_CONTENT)
            mediaIntent.type = "image/*"
            startActivityForResult(mediaIntent, PICK_MEDIA_REQUEST)
        }

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()

            // Check if media URI is not null
            if (mediaUri != null) {
                val storageRef = FirebaseStorage.getInstance().reference
                val mediaFileName = "picture"
                val mediaRef = storageRef.child(mediaFileName)
                mediaRef.putFile(mediaUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        // Media uploaded successfully; get the download URL
                        mediaRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                val mediaURL = uri.toString()

                                // Now, you have the actual media URL

                                // Create a new report document and add it to Firestore
                                val report = hashMapOf(
                                    "title" to title,
                                    "description" to description,
                                    "mediaURL" to mediaURL,
                                    "timestamp" to FieldValue.serverTimestamp()
                                )

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
                            }
                            .addOnFailureListener { e ->
                                // Handle the error in getting the download URL
                            }
                    }
                    .addOnFailureListener { e ->
                        // Handle the error in uploading media
                    }
            } else {
                // Media is not selected, you can proceed without media
                // Create a new report document
                val report = hashMapOf(
                    "title" to title,
                    "timestamp" to FieldValue.serverTimestamp(),
                    "description" to description
                )
                // Add the report to Firestore
                reportsCollection.add(report)
                    .addOnSuccessListener { documentReference ->
                        // Report added successfully
                        // Optionally, navigate back to HomeFragment or the main activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        // Handle the error
                        Log.e("MediaUpload", "Error uploading media: $e")
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