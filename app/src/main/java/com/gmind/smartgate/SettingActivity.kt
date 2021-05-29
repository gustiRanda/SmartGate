package com.gmind.smartgate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.databinding.ActivitySettingBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingActivity : AppCompatActivity() {

    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var preferences: Preferences

    private var activitySettingBinding: ActivitySettingBinding? = null
    private val binding get() = activitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySettingBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        preferences = Preferences(this)

        binding?.tvName?.text = preferences.getValues("nama")
        binding?.tvMosque?.text = preferences.getValues("masjid")

        binding?.btnLogout?.setOnClickListener {
            preferences.setValues("login", "0")
            preferences.setValues("nama", "")
            preferences.setValues("username", "")
            preferences.setValues("url", "")
            preferences.setValues("berhasil", "0")
            preferences.setValues("gagal", "0")
            preferences.setValues("nomor", "")

            if (preferences.getValues("login").equals("0")){
                firebaseDatabase.goOffline()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }
}