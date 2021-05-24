package com.gmind.smartgate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignUpPhotoActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_USER = "extra_user"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo)
    }
}