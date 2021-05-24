package com.gmind.smartgate.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.R
import com.gmind.smartgate.SignInActivity
import com.gmind.smartgate.databinding.ActivityOnboardingOneBinding
import java.util.prefs.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preferences: com.gmind.smartgate.utils.Preferences

    private var activityOnboardingOneBinding: ActivityOnboardingOneBinding? = null
    private val binding get() = activityOnboardingOneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOnboardingOneBinding = ActivityOnboardingOneBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        preferences = com.gmind.smartgate.utils.Preferences(this)

        if (preferences.getValues("onboarding").equals("1")){
            finishAffinity()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding?.btnNext?.setOnClickListener {
            val intent = Intent(this, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSkip?.setOnClickListener {
            preferences.setValues("onboarding", "1")
            finishAffinity()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}