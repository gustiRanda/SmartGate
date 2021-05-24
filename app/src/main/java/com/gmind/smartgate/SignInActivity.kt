package com.gmind.smartgate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gmind.smartgate.databinding.ActivitySignInBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.*

class SignInActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String

    lateinit var databaseReference: DatabaseReference
    lateinit var preferences: Preferences

    private var activitySignInBinding: ActivitySignInBinding? = null
    private val binding get() = activitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")){
            finishAffinity()

            val intent = Intent(this, SignUpPhotoActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignIn?.setOnClickListener {

            username = binding?.etUsername?.text.toString()
            password = binding?.etPassword?.text.toString()

            when {
                username == "" -> {
                    binding?.etUsername?.error = getString(R.string.silahkan_isi_username)
                    binding?.etUsername?.requestFocus()
                }
                password == "" -> {
                    binding?.etPassword?.error = getString(R.string.silahkan_isi_password)
                    binding?.etPassword?.requestFocus()
                }
                else -> {
                    pushLogin(username, password)
                }
            }
        }
        binding?.btnSignUp?.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushLogin(username: String, password: String) {
        databaseReference.child(username).addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, "User Tidak Ditemukan",
                    Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User Tidak Ditemukan",
                        Toast.LENGTH_LONG).show()
                } else {
                    if (user.password.equals(password)) {
                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("username", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("berhasil", user.berhasil.toString())
                        preferences.setValues("gagal", user.gagal.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignInActivity, "Password Anda Salah",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }


        })
    }
}