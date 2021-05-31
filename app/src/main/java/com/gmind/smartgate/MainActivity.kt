package com.gmind.smartgate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmind.smartgate.databinding.ActivityMainBinding
import com.gmind.smartgate.model.User
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {


    lateinit var username: String


    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var preferences: Preferences

    companion object {
        const val CHANNEL_ID = "01"
        val CHANNEL_NAME: CharSequence = "Smart Gate "
    }
    private var NotificationID: Int = 0

    private var activityMainBinding: ActivityMainBinding? = null
    private val binding get() = activityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
//        firebaseDatabase.goOnline()
        databaseReference = firebaseDatabase.getReference("User")

        preferences = Preferences(this)

        binding?.tvUser?.text = preferences.getValues("username")
        binding?.tvMosque?.text = preferences.getValues("masjid")

//        binding?.ivSetting?.setOnClickListener {
//            val intent = Intent(this, SettingActivity::class.java)
//            startActivity(intent)
//        }

        binding?.mainToolbar?.ivSetting?.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        binding?.ivProfile?.let {
            Glide.with(this)
                .load(preferences.getValues("url"))
                .apply(RequestOptions.circleCropTransform())
                .into(it)
        }

        checkData()

        setChart()

        databaseReference.child(username).child("gagal").addChildEventListener(object : ChildEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                Log.d("TAG", "gagal dataChange")
//                notification()
//            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "gagal added")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "gagal change")
                notification()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("TAG", "gagal removed")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "gagal moved")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "gagal canceled")
            }

        })


        binding?.logout?.setOnClickListener {
            preferences.setValues("login", "0")
            preferences.setValues("nama", "")
            preferences.setValues("username", "")
            preferences.setValues("url", "")
//            preferences.setValues("berhasil", "0")
//            preferences.setValues("gagal", "0")
            preferences.setValues("nomor", "")

            if (preferences.getValues("login").equals("0")){
                firebaseDatabase.goOffline()
                startActivity(Intent(this, SignInActivity::class.java))
            }
        }
    }

    private fun notification() {
        val buider = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.avatar)
                .setContentTitle(getString(R.string.silahkan_ini_nama))
                .setContentText(getString(R.string.ayo_mulai))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            buider.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }

        val notification = buider.build()

        notificationManager.notify(NotificationID, notification)
    }

    private fun setChart() {
        val pie = AnyChart.pie()

        val berhasil = preferences.getValues("berhasil")?.toInt()
        val gagal = preferences.getValues("gagal")?.toInt()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Berhasil", berhasil))
        data.add(ValueDataEntry("Gagal", gagal))

        if (berhasil == 0 && gagal == 0){
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_LONG).show()
        }

        pie.data(data)

        pie.title("Data Jamaah Masjid")

        pie.background()
            .fill("#f2fcff")
//        #b5e550
                .stroke("#000000")
                .cornerType("round")
                .corners(10)

        pie.legend()
                .position(Orientation.BOTTOM)
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(com.anychart.enums.Align.BOTTOM)


        binding?.chart?.setChart(pie)
    }


    private fun checkData() {
        username = preferences.getValues("username").toString()

        databaseReference.child(username).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)

                preferences.setValues("berhasil", user?.berhasil.toString())
                preferences.setValues("gagal", user?.gagal.toString())
                preferences.setValues("url", user?.url.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "" + error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        firebaseDatabase.goOnline()
    }

    override fun onPause() {
        super.onPause()
        firebaseDatabase.goOffline()
    }
    override fun onBackPressed() {
        super.onBackPressed()
//        firebaseDatabase.goOffline()
        finishAffinity()
    }
}