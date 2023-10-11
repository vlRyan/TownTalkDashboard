package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.graphics.Color
import android.text.SpannableString
import android.text.Spannable


class LoginPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        val combinedText = findViewById<TextView>(R.id.app_title)
        val text = SpannableString("TownTalk")
        text.setSpan(ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(ForegroundColorSpan(Color.RED), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        combinedText.text = text

        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        val signupButton = findViewById<TextView>(R.id.signup_btn)
        signupButton.setOnClickListener{
            val intent = Intent (this,SignupPage::class.java)
            startActivity(intent)
        }




    }
}