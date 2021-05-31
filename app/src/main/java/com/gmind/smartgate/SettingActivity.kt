package com.gmind.smartgate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmind.smartgate.databinding.ActivitySettingBinding
import com.gmind.smartgate.utils.Preferences

class SettingActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    private var activitySettingBinding: ActivitySettingBinding? = null
    private val binding get() = activitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        preferences = Preferences(this)


        binding?.ivProfile?.let {
            Glide.with(this)
                    .load(preferences.getValues("url"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(it)
        }
        binding?.tvName?.text = preferences.getValues("nama")
        binding?.tvMosque?.text = preferences.getValues("masjid")
    }
}