package com.example.towntalkdashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.towntalkdashboard.databinding.FragmentAdminProfilePageBinding
import com.google.firebase.auth.FirebaseAuth

class AdminProfilePage : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentAdminProfilePageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAdminProfilePageBinding.inflate(inflater, container, false)
        val view = binding.root

        firebaseAuth = FirebaseAuth.getInstance()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), LoginPage::class.java)
            startActivity(intent)

            requireActivity().finish()
        }

        return view
    }
}