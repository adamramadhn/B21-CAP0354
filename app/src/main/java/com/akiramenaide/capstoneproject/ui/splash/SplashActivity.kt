package com.akiramenaide.capstoneproject.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.akiramenaide.capstoneproject.databinding.ActivitySplashBinding
import com.akiramenaide.capstoneproject.ui.auth.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splashTime)
    }
//    private var delay = 2000L
//    override fun onCreate(savedInstanceState: Bundle?) {
//        supportActionBar?.hide()
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
//        Handler(mainLooper).postDelayed({
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }, delay)
//    }
}