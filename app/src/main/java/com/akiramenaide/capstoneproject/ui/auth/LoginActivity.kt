package com.akiramenaide.capstoneproject.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akiramenaide.capstoneproject.databinding.ActivityLoginBinding
import com.akiramenaide.capstoneproject.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        auth = FirebaseAuth.getInstance()
        loginBinding.apply {
            btnLogin.setOnClickListener {
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
                loginUser(emailUser, passUser)
            }
        }

        loginBinding.btnRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
        loginBinding.btnForgotPassword.setOnClickListener {
            Intent(this, ResetPasswordActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun loginUser(emailUser: String, passUser: String) {
        auth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(this) {
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
        //jika sudah pernah login maka akan langsung masuk ke home
        if (auth.currentUser != null) {
            Intent(this, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}