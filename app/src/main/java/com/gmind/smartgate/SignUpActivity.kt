package com.gmind.smartgate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gmind.smartgate.SignUpPhotoActivity.Companion.EXTRA_USER
import com.gmind.smartgate.databinding.ActivitySignUpBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.*


class SignUpActivity : AppCompatActivity() {

    lateinit var username: String
    lateinit var password: String
    lateinit var name: String
    lateinit var number: String
    lateinit var mosque: String

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var database: DatabaseReference

    private lateinit var preferences: Preferences

    private var activitySignUpBinding: ActivitySignUpBinding? = null
    private val binding get() = activitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
//        firebaseDatabase.goOnline()
//        database = FirebaseDatabase.getInstance().reference
        databaseReference = firebaseDatabase.getReference("User")

        preferences = Preferences(this)

        binding?.btnSignUp?.setOnClickListener {
            username = binding?.etUsername?.text.toString()
            password = binding?.etPassword?.text.toString()
            name = binding?.etName?.text.toString()
            number = binding?.etNumber?.text.toString()
            mosque = binding?.etMosqueName?.text.toString()

            if (username == ""){
                binding?.etUsername?.error = getString(R.string.silahkan_isi_username)
                binding?.etUsername?.requestFocus()
            } else if (password == "") {
                binding?.etPassword?.error = getString(R.string.silahkan_isi_password)
                binding?.etPassword?.requestFocus()
            } else if (name == ""){
                binding?.etName?.error = getString(R.string.silahkan_ini_nama)
                binding?.etName?.requestFocus()
            } else if (number == ""){
                binding?.etNumber?.error = getString(R.string.silahkan_isi_nomor)
                binding?.etNumber?.requestFocus()
            } else if (mosque == ""){
                binding?.etMosqueName?.error = getString(R.string.silahkan_isi_nama_masjid)
                binding?.etMosqueName?.requestFocus()
            } else{
                val usernameStatus = username.indexOf(".")
                if (usernameStatus >= 0){
                    binding?.etUsername?.error = getString(R.string.username_tidak_titik)
                    binding?.etUsername?.requestFocus()
                } else{
                    saveUser(username, password, name, number, mosque)
                }
            }
        }
    }

    private fun saveUser(
            username: String,
            password: String,
            name: String,
            number: String,
            mosque: String
    ) {
        val user = User()
        user.username = username
        user.password = password
        user.nama = name
        user.nomor = number
        user.masjid = mosque

        checkUsername(username, user)

    }

    private fun checkUsername(username: String, data: User) {
        databaseReference.child(username).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user == null) {
                    databaseReference.child(username).setValue(data)

                    databaseReference.child(data.username.toString()).child("berhasil").setValue("0")
                    databaseReference.child(data.username.toString()).child("gagal").setValue("0")

                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("username", data.username.toString())
                    preferences.setValues("masjid", data.masjid.toString())
                    preferences.setValues("url", "")
                    preferences.setValues("berhasil", "0")
                    preferences.setValues("gagal", "0")
                    preferences.setValues("nomor", data.nomor.toString())
                    preferences.setValues("login", "1")

                    val intent = Intent(this@SignUpActivity, SignUpPhotoActivity::class.java).putExtra(EXTRA_USER, data)
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpActivity, getString(R.string.user_sudah_digunakan), Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}