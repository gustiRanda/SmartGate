package com.gmind.smartgate

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gmind.smartgate.databinding.ActivitySignUpPhotoBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpPhotoActivity : AppCompatActivity() {

    private var activitySignUpPhotoBinding: ActivitySignUpPhotoBinding? = null
    private val binding get() = activitySignUpPhotoBinding

    var statusAdd: Boolean = false
    lateinit var filePath: Uri

    lateinit var firebaseStorage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    lateinit var user: User

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySignUpPhotoBinding = ActivitySignUpPhotoBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        preferences = Preferences(this)
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = firebaseStorage.reference

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("User")

        user = intent.getParcelableExtra(EXTRA_USER)!!

        binding?.tvHello?.text = "Selamat Datang\n" + user.nama

        binding?.ivAdd?.setOnClickListener {
            if (statusAdd){
                statusAdd = false
                binding?.btnSave?.visibility = View.GONE
//                binding?.ivAdd?.setImageResource(R.drawable.ic_btn_add)
//                binding?.ivProfile?.setImageResource(R.drawable.avatar)
            } else{
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }
        }

        binding?.btnSave?.setOnClickListener {

        }



        binding?.btnPass?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}