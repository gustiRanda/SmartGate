package com.gmind.smartgate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmind.smartgate.databinding.ActivityMainBinding
import com.gmind.smartgate.utils.Preferences
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {


    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private lateinit var preferences: Preferences

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

        binding?.tvUser?.text = preferences.getValues("nama")
        binding?.tvMosque?.text = preferences.getValues("masjid")

        binding?.ivSetting?.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

//        binding?.ivProfile?.let {
//            Glide.with(this)
//                .load(preferences.getValues("url"))
//                .apply(RequestOptions.circleCropTransform())
//                .into(it)
//        }
//        val database = FirebaseDatabase.getInstance()
//        val myRef = database.getReference("message")
//
//        myRef.setValue("Hello, World!")
//
//        databaseReference.child("berhasil").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue(User::class.java)
//                Log.d("TAG", "Value is: $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.w("TAG", "Failed to read value.", error.toException())
//            }
//        })


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
            .fill("#c6ffc1")
//        #b5e550
//            .stroke("#000000")
//                .cornerType("round")
//                .corners(10)

        pie.legend()
            .position(Orientation.BOTTOM)
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(com.anychart.enums.Align.BOTTOM)


        binding?.chart?.setChart(pie)

//        val anyChartView = binding?.chart
//
//        val pie = AnyChart.pie()
//
//        pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
//            override fun onClick(event: Event) {
//                Toast.makeText(
//                    this@MainActivity,
//                    event.data["x"].toString() + ":" + event.data["value"],
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//
//        val data: MutableList<DataEntry> = ArrayList()
//        data.add(ValueDataEntry("Apples", 6371664))
//        data.add(ValueDataEntry("Pears", 789622))
//        data.add(ValueDataEntry("Bananas", 7216301))
//        data.add(ValueDataEntry("Grapes", 1486621))
//        data.add(ValueDataEntry("Oranges", 1200000))
//
//        pie.data(data)
//
//        pie.title("Fruits imported in 2015 (in kg)")
//
//        pie.labels().position("outside")
//
//        pie.legend().title().enabled(true)
//        pie.legend().title()
//            .text("Retail channels")
//            .padding(0.0, 0.0, 10.0, 0.0)
//
//        pie.legend()
//            .position("center-bottom")
//            .itemsLayout(LegendLayout.HORIZONTAL)
//            .align(com.anychart.enums.Align.CENTER)
//
//        anyChartView?.setChart(pie)

        binding?.logout?.setOnClickListener {
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