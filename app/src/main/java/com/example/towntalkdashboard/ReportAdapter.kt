package com.example.towntalkdashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale


class ReportAdapter(private val onReadMoreClick: (Report) -> Unit) : ListAdapter<ReportAdapter.Report, ReportAdapter.ReportViewHolder>(ReportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_report_item, parent, false)
        return ReportViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = getItem(position)
        holder.bind(report)
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val mediaImageView: ImageView = itemView.findViewById(R.id.mediaImageView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val readMoreTextView: TextView = itemView.findViewById(R.id.readMoreTextView)

        fun bind(report: Report) {
            titleTextView.text = report.title
            descriptionTextView.text = report.description
            dateTextView.text = report.date

            if (report.mediaURL != null) {
                mediaImageView.visibility = View.VISIBLE
                Glide.with(itemView)
                    .load(report.mediaURL)
                    .placeholder(R.drawable.baseline_image_24) // Placeholder image
                    .error(R.drawable.baseline_image_24) // Error image (if loading fails)
                    .into(mediaImageView)
            } else {
                mediaImageView.visibility = View.GONE
            }

            // Handle "Read more" button click
            readMoreTextView.setOnClickListener {
                onReadMoreClick(report)
            }
        }
    }

    data class Report(
        val id: String,
        val title: String,
        val description: String,
        val mediaURL: String?,
        val date: String
    ) {
        companion object {
            fun fromDocumentSnapshot(documentSnapshot: DocumentSnapshot): Report {
                val id = documentSnapshot.id
                val title = documentSnapshot.getString("title") ?: ""
                val description = documentSnapshot.getString("description") ?: ""
                val mediaURL = documentSnapshot.getString("mediaURL")
                val timestamp = documentSnapshot.getDate("timestamp")
                val formattedDate = formatDate(timestamp)
                return Report(id, title, description, mediaURL, formattedDate)
            }

            private fun formatDate(date: Date?): String {
                date?.let {
                    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    return sdf.format(date)
                }
                return ""
            }
        }
    }

    class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem
        }
    }
}
