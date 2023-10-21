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

class ReportAdapter : ListAdapter<ReportAdapter.Report, ReportAdapter.ReportViewHolder>(ReportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
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

        fun bind(report: Report) {
            titleTextView.text = report.title
            descriptionTextView.text = report.description

            // Load and display media using Glide (or another image loading library)
            if (report.mediaURL != null) {
                Glide.with(itemView.context)
                    .load(report.mediaURL)
                    .placeholder(R.drawable.baseline_image_24) // Placeholder image
                    .error(R.drawable.baseline_image_24) // Error image (if loading fails)
                    .into(mediaImageView)
            } else {
                mediaImageView.setImageResource(R.drawable.baseline_image_24)
            }
        }
    }

    data class Report(
        val title: String,
        val description: String,
        val mediaURL: String?
    )

    class ReportDiffCallback : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
            return oldItem == newItem
        }
    }
}
