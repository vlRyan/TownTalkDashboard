package com.example.towntalkdashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.text.style.ForegroundColorSpan
import android.graphics.Color
import android.text.SpannableString
import android.text.Spannable
import android.widget.Toast
import com.example.towntalkdashboard.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth


class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var firebaseAuth: FirebaseAuth

    companion object {
        const val ADMIN_EMAIL = "admin@gmail.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val combinedText = findViewById<TextView>(R.id.appTitle)
        val text = SpannableString("TownTalk")
        text.setSpan(ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(ForegroundColorSpan(Color.RED), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        combinedText.text = text

        firebaseAuth = FirebaseAuth.getInstance()

        val signupButton = findViewById<TextView>(R.id.signupBtn)
        signupButton.setOnClickListener{
            val intent = Intent (this,SignupPage::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener{
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user?.email == ADMIN_EMAIL) {
                                // This is the admin user
                                val intent = Intent(this, Navigation::class.java)
                                startActivity(intent)

                                Toast.makeText(this, "Welcome Admin", Toast.LENGTH_SHORT).show()
                            } else {
                                // This is a regular user
                                val intent = Intent(this, Navigation::class.java)
                                startActivity(intent)

                                Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, Navigation::class.java)
            startActivity(intent)
        }
    }
}