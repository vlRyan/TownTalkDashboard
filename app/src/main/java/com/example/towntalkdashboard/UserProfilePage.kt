package com.example.towntalkdashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.towntalkdashboard.databinding.FragmentUserProfilePageBinding
import com.google.firebase.auth.FirebaseAuth

class UserProfilePage : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentUserProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserProfilePageBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        val goToMyReport = binding.numberOfReport // Use binding to find the view
        goToMyReport.setOnClickListener {
            val intent = Intent(requireContext(), UserReportPage::class.java)
            startActivity(intent)
        }

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), LoginPage::class.java)
            startActivity(intent)

            requireActivity().finish()
        }

        return view
    }
}