package com.gmind.smartgate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private var activityProfileBinding: ActivityProfileBinding? = null
    private val binding get() = activityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProfileBinding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }
}