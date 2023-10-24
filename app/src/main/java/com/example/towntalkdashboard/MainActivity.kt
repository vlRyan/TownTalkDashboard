package com.example.towntalkdashboard

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import com.example.towntalkdashboard.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
            bottomNavigationView.inflateMenu(R.menu.bottom_navigation)
        }

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_report -> {
                    val intent = Intent(this, SubmitReportPage::class.java)
                    startActivity(intent)
                }
                R.id.navigation_read_report -> replaceFragment(AdminCheckReport())
                R.id.navigation_profile -> replaceFragment(ProfileFragment())
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