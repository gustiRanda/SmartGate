package com.gmind.smartgate

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Gravity.CENTER
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.LegendLayout
import com.anychart.enums.Orientation
import com.anychart.graphics.vector.image.Align
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

//        binding?.hello?.setOnClickListener {
//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding?.berhasil?.setText(preferences.getValues("berhasil"))
//
//        binding?.gagal?.setText(preferences.getValues("gagal"))


        val pie = AnyChart.pie()

        val berhasil = preferences.getValues("berhasil")?.toInt()
        val gagal = preferences.getValues("gagal")?.toInt()


        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Berhasil", berhasil))
        data.add(ValueDataEntry("Gagal", gagal))

//        val anyChartView = findViewById<View>(R.id.chart) as AnyChartView
//        anyChartView.setChart(pie)

        pie.data(data)

        pie.background()
            .fill("#96a6a6 0.3")
            .stroke("#96a6a6")
            .cornerType("round")
            .corners(10)

        pie.legend()
            .position(Orientation.BOTTOM)
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(com.anychart.enums.Align.BOTTOM)

        binding?.chart?.setChart(pie)
//
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
            if (preferences.getValues("login").equals("0")){

                startActivity(Intent(this, SignInActivity::class.java))
            }
        }


    }
}