package com.example.towntalkdashboard

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.towntalkdashboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_report -> replaceFragment(ReportFragment())
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