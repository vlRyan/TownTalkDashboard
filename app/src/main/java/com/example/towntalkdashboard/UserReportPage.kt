package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserReportPage : AppCompatActivity() {
    private lateinit var reportRecyclerView: RecyclerView
    private lateinit var reportAdapter: ReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_report_page)

        reportRecyclerView = findViewById(R.id.reportRecyclerView)

        val backButton = findViewById<Button>(R.id.backBtn)
        backButton.setOnClickListener {
            val intent = Intent (this, Navigation::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView with ReportAdapter
        reportAdapter = ReportAdapter {
            // Handle "Read more" click, if needed
        }

        reportRecyclerView.adapter = reportAdapter
        reportRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display user's reports
        fetchUserReports()
    }

    private fun fetchUserReports() {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val userEmail = currentUser.email
            val db = FirebaseFirestore.getInstance()
            val reportsCollection = db.collection("reports")

            reportsCollection
                .whereEqualTo("userEmail", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val reports = mutableListOf<ReportAdapter.Report>()
                    for (document in querySnapshot.documents) {
                        val report = ReportAdapter.Report.fromDocumentSnapshot(document)
                        reports.add(report)
                    }
                    Log.d("UserReportPage", "Number of reports: ${reports.size}")
                    reportAdapter.submitList(reports)
                }
                .addOnFailureListener { e ->
                    // Handle the error
                    Log.e("UserReportPage", "Error retrieving reports: $e")
                }
        }
    }
}