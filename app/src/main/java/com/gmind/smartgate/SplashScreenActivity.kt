package com.gmind.smartgate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.gmind.smartgate.databinding.ActivitySplashScreenBinding
import com.gmind.smartgate.onboarding.OnboardingOneActivity

class SplashScreenActivity : AppCompatActivity() {

    private var activitySplashScreenBinding: ActivitySplashScreenBinding? = null
    private val binding get() = activitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        binding?.ivSplash?.alpha = 0f
//        binding?.ivSplash?.animate()?.setDuration(3000)?.alpha(1f)?.withEndAction{
//            val intent = Intent(this, OnboardingOneActivity::class.java)
//            startActivity(intent)
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            finish()
//        }

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashScreenActivity, OnboardingOneActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}