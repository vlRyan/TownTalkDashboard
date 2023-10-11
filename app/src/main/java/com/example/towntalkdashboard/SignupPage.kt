package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignupPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        val backLogin = findViewById<Button>(R.id.back_btn)
        backLogin.setOnClickListener {
            val intent = Intent (this, LoginPage::class.java)
            startActivity(intent)
        }


    }
}