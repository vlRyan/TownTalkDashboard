package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val btnlogin = findViewById<Button>(R.id.button)
        btnlogin.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnsignup = findViewById<TextView>(R.id.signup)
        btnsignup.setOnClickListener{
            val intent = Intent (this,SignupPage::class.java)
            startActivity(intent)
        }
    }
}