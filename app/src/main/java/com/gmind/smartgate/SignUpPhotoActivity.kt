package com.gmind.smartgate

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gmind.smartgate.databinding.ActivitySignUpPhotoBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*

class SignUpPhotoActivity : AppCompatActivity(), PermissionListener {

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
                binding?.btnSave?.visibility = View.INVISIBLE
                binding?.ivAdd?.setImageResource(R.drawable.ic_btn_add)
                binding?.ivProfile?.setImageResource(R.drawable.avatar)
            } else{
                ImagePicker.with(this)
                    .cameraOnly()
                    .start()
            }
        }

        binding?.btnSave?.setOnClickListener {
            if (filePath != null){
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Mengupload...")
                progressDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())
                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Foto Berhasil Diupload", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            saveToFirebase(it.toString())
                        }
                    }

                    .addOnFailureListener{
                        e -> progressDialog.dismiss()
                        Toast.makeText(this, "Gagal" + e.message, Toast.LENGTH_SHORT).show()
                    }

                    .addOnProgressListener {
                        taskSnapshot -> val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressDialog.setMessage("Mengupload Foto " + progress.toInt() + "%")
                    }
            }
        }



        binding?.btnSkip?.setOnClickListener {
            finishAffinity()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveToFirebase(url: String) {
        databaseReference.child(user.username!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user.url = url
                databaseReference.child(user.username!!).setValue(user)

                preferences.setValues("nama", user.nama.toString())
                preferences.setValues("user", user.username.toString())
                preferences.setValues("berhasil", "0")
                preferences.setValues("gagal", "0")
                preferences.setValues("url", "")
                preferences.setValues("email", user.email.toString())
                preferences.setValues("login", "1")
                preferences.setValues("url", url)

                finishAffinity()

                val intent = Intent(this@SignUpPhotoActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpPhotoActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
                .cameraOnly()
                .start()
    }

    override fun onPermissionRationaleShouldBeShown(
            permission: PermissionRequest?,
            token: PermissionToken?
    ) {
        TODO("Not yet implemented")
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda Tidak Bisa Menambahkan Photo Profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik Tombol Upload Nanti Aja", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            statusAdd = true

            filePath = data?.data!!
            binding?.ivProfile?.let {
                Glide.with(this)
                    .load(filePath)
                    .apply(RequestOptions.circleCropTransform())
                    .into(it)
            }

            binding?.btnSave?.visibility = View.VISIBLE
            binding?.ivAdd?.setImageResource(R.drawable.ic_btn_delete)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            //error
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {

            //tdk error tp gagal
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}