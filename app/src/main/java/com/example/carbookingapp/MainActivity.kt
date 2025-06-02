//this page is where the user will perform the booking
package com.example.carbookingapp

import android.app.DatePickerDialog
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
    lateinit var datepicktextid: TextView
    lateinit var datepickbuttonid: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        datepicktextid = findViewById<TextView>(R.id.datepicktextid)
        datepickbuttonid = findViewById<Button>(R.id.datepickbuttonid)
        datepickbuttonid.setOnClickListener {
            showDateTimePicker()
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

private fun MainActivity.showDateTimePicker() {
    val calender = Calendar.getInstance()
    val year = calender.get(Calendar.YEAR)
    val month = calender.get(Calendar.MONTH)
    val day = calender.get(Calendar.DAY_OF_MONTH)
    DatePickerDialog(this,
        {_,selectedYear,selectedMonth,selectedDay->
        val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
        datepicktextid.text = selectedDate
    },year,month,day).show()
}
