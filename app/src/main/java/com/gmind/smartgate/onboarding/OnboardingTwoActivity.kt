package com.gmind.smartgate.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.R
import com.gmind.smartgate.SignInActivity
import com.gmind.smartgate.databinding.ActivityOnboardingTwoBinding

class OnboardingTwoActivity : AppCompatActivity() {

    private var activityOnboardingTwoBinding: ActivityOnboardingTwoBinding? = null
    private val binding get() = activityOnboardingTwoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnboardingTwoBinding = ActivityOnboardingTwoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnNext?.setOnClickListener {
            val intent = Intent(this, OnboardingThreeActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSkip?.setOnClickListener {
            finishAffinity()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}