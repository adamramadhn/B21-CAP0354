package com.akiramenaide.capstoneproject.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akiramenaide.capstoneproject.databinding.ActivityRegisterBinding
import com.akiramenaide.capstoneproject.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        auth = FirebaseAuth.getInstance()

        registerBinding.apply {
            btnSignup.setOnClickListener {
                val emailUser = etEmail.text.toString().trim()
                val passUser = etPassword.text.toString().trim()
                if (emailUser.isEmpty()) {
                    etEmail.error = "Please fill the email!"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                    etEmail.error = "Email is not valid!"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }
                if (passUser.isEmpty() || passUser.length < 6) {
                    etPassword.error = "Password must more tha 6 characters"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                regisUser(emailUser, passUser)
            }
        }

        registerBinding.btnRegister.setOnClickListener {
            finish()
        }
    }

    private fun regisUser(emailUser: String, passUser: String) {
        auth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Intent(this, MainActivity::class.java).also { move ->
                    move.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(move)
                }
            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}