//this page is where the user will perform the booking
package com.example.carbookingapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var carTypes = listOf("Toyota Corolla",
        "Honda Civic",
        "Hyundai Elantra",
        "Toyota Camry",
        "Honda Accord",
        "Nissan Altima",
        "Chevrolet Malibu",
        "Kia Forte",
        "Volkswagen Jetta",
        "Ford Fusion")
    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var adapterItems: ArrayAdapter<String>
    lateinit var namefieldId1: TextView
    lateinit var namefieldId2: TextView
    lateinit var namefieldId44: TextView
    lateinit var namefieldId45: TextView
    lateinit var datepickbuttonid: Button
    lateinit var datepickbuttonid2: Button
    lateinit var timepickbuttonid: Button
    lateinit var timepickbuttonid2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        namefieldId1 = findViewById<TextView>(R.id.namefieldId1)
        namefieldId2 = findViewById<TextView>(R.id.namefieldId2)
        namefieldId45 = findViewById<TextView>(R.id.namefieldId45)
        namefieldId44 = findViewById<TextView>(R.id.namefieldId44)
        datepickbuttonid = findViewById<Button>(R.id.datepickbuttonid)
        datepickbuttonid2 = findViewById<Button>(R.id.datepickbuttonid2)

        timepickbuttonid = findViewById<Button>(R.id.timepickbuttonid)
        timepickbuttonid2 = findViewById<Button>(R.id.timepickbuttonid2)

        datepickbuttonid.setOnClickListener {
            showDateTimePicker(true,1)
        }
        datepickbuttonid2.setOnClickListener {
            showDateTimePicker(true,2)
        }
        timepickbuttonid.setOnClickListener {
            showDateTimePicker(false,1)
        }
        timepickbuttonid2.setOnClickListener {
            showDateTimePicker(false,2)
        }

        autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocompletetextid)
        adapterItems = ArrayAdapter(this, R.layout.list_item, R.id.spinner_text, carTypes)
        autoCompleteTextView.setAdapter(adapterItems)



        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCar = parent.getItemAtPosition(position).toString()
//            selectedItemTextView.text = "You selected: $selectedCar"
            Log.d("MainActivity","Car Type: $selectedCar")
            // TODO: the above thind

        }
    }
}

private fun MainActivity.showDateTimePicker(what: Boolean,id:Int) {
    val calender = Calendar.getInstance()
    if(what) {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                if (id == 1) namefieldId1.text = selectedDate
                else if (id == 2) namefieldId44.text = selectedDate
            }, year, month, day
        ).show()
    }else {
        val hour = calender.get(Calendar.HOUR)
        val minute = calender.get(Calendar.MINUTE)
        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            Log.d("MainActivity","Time: $selectedHour:$selectedMinute")
            val selectedTime = "$selectedHour:$selectedMinute"
            if (id == 1) namefieldId2.text = selectedTime
            else if (id == 2) namefieldId45.text = selectedTime
        }, hour, minute, false).show()
    }
}
