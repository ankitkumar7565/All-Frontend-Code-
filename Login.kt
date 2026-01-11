package com.example.managementtask

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managementtask.Data.SessionManager
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        session = SessionManager(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignUp)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp) // add id in XML
        val forget = findViewById<TextView>(R.id.forget)
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Enter valid email"
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, Registration::class.java))
        }
        forget.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                session.setLogin(true)
                session.saveUser("", email)
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                     startActivity(Intent(this, Dashboard::class.java))
                     finish()
                } else {
                    Toast.makeText(
                        this,
                        task.exception?.message ?: "Login Failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
}