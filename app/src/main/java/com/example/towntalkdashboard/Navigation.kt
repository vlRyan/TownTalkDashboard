package com.example.towntalkdashboard

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import com.example.towntalkdashboard.databinding.ActivityNavigationBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Navigation : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)

        val user = FirebaseAuth.getInstance().currentUser
        val bottomNavigationView = binding.bottomNavigationView

        if (user?.email == LoginPage.ADMIN_EMAIL) {
            // Admin user, load admin layout
            val menuInflater = MenuInflater(this)
            menuInflater.inflate(R.menu.admin_bottom_navigation, bottomNavigationView.menu)
        } else {
            // Regular user, load regular layout
            bottomNavigationView.inflateMenu(R.menu.user_bottom_navigation)
        }

        replaceFragment(HomePage())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomePage())
                R.id.navigation_report -> {
                    val intent = Intent(this, UserSubmitReport::class.java)
                    startActivity(intent)
                }
                R.id.navigation_read_report -> replaceFragment(AdminCheckReport())
                R.id.user_navigation_profile -> replaceFragment(UserProfilePage())
                R.id.admin_navigation_profile -> replaceFragment(AdminProfilePage())
                else ->{

                }
            }
                true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}