package com.gmind.smartgate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmind.smartgate.databinding.ActivityMainBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences
    private lateinit var databaseReference: DatabaseReference

    private var activityMainBinding: ActivityMainBinding? = null
    private val binding get() = activityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        preferences = Preferences(this)
        databaseReference = FirebaseDatabase.getInstance().getReference("User")

        binding?.hello?.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding?.berhasil?.setText(preferences.getValues("berhasil"))

        binding?.gagal?.setText(preferences.getValues("gagal"))

        binding?.logout?.setOnClickListener {
            preferences.setValues("login", "0")
            if (preferences.getValues("login").equals("0")){

                startActivity(Intent(this, SignInActivity::class.java))
            }
        }


    }
}