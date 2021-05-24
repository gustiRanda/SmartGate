package com.gmind.smartgate.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.R
import com.gmind.smartgate.SignInActivity
import com.gmind.smartgate.databinding.ActivityOnboardingThreeBinding

class OnboardingThreeActivity : AppCompatActivity() {

    private var activityOnboardingThreeBinding: ActivityOnboardingThreeBinding? = null
    private val binding get() = activityOnboardingThreeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnboardingThreeBinding = ActivityOnboardingThreeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSkip?.setOnClickListener {
            finishAffinity()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}