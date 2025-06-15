//this page is where the user will perform the booking
package com.example.carbookingapp
import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carbookingapp.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {
    val carTypes = mapOf(
        "Toyota Corolla" to CarDetails("John Doe", "1234567890", "XYZ-1234"),
        "Honda Civic" to CarDetails("Jane Smith", "2345678901", "ABC-5678"),
        "Hyundai Elantra" to CarDetails("Mike Johnson", "3456789012", "DEF-9101"),
        "Toyota Camry" to CarDetails("Sara White", "4567890123", "GHI-2345"),
        "Honda Accord" to CarDetails("Tom Brown", "5678901234", "JKL-3456"),
        "Nissan Altima" to CarDetails("Anna Blue", "6789012345", "MNO-4567"),
        "Chevrolet Malibu" to CarDetails("Peter Black", "7890123456", "PQR-5678"),
        "Kia Forte" to CarDetails("Lucy Gray", "8901234567", "STU-6789"),
        "Volkswagen Jetta" to CarDetails("Emma Green", "9012345678", "VWX-7890"),
        "Ford Fusion" to CarDetails("James Red", "0123456789", "YZA-8901")
    )
    lateinit var binding: ActivityMainBinding
    val carNames = carTypes.keys.toList() // This is now List<String>
    lateinit var autoCompleteTextView: AutoCompleteTextView
    lateinit var adapterItems: ArrayAdapter<String>
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.datepickbuttonid.setOnClickListener {
            showDateTimePicker(true,1)
        }
        binding.datepickbuttonid2.setOnClickListener {
            showDateTimePicker(true,2)
        }
        binding.timepickbuttonid.setOnClickListener {
            showDateTimePicker(false,1)
        }
        binding.timepickbuttonid2.setOnClickListener {
            showDateTimePicker(false,2)
        }
        autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autocompletetextid)
        adapterItems = ArrayAdapter(this, R.layout.list_item, R.id.spinner_text, carNames)
        autoCompleteTextView.setAdapter(adapterItems)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedCar = parent.getItemAtPosition(position).toString()
            val carDetails = carTypes[selectedCar]
            val builder = SpannableStringBuilder()
            carDetails?.let {
                builder.append("Driver: ", StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.append("${it.DriverName}\n")
                builder.append("Phone: ", StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.append("${it.PhoneNumber}\n")
                builder.append("Reg No: ", StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.append(it.RegNo)
                binding.carDetailsViewid.text = builder
            } ?: run {
                binding.carDetailsViewid.text = "No details available for this car"
            }
        }

        binding.droplocationbtnid.setOnClickListener {
            if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                getLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                    102
                )
            }
        }
        binding.makereservationbtnid.setOnClickListener {
            val contactNum = binding.contactinputfieldid.text.toString()
            val sharedPref = getSharedPreferences("MyAppPrefs",Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("CONTACT_NUMBER", contactNum)
            apply()
        }
            val intent = Intent(this, OTPVerificationActivity::class.java)
            startActivity(intent)
        }

    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 102 && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            getLocation()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLocation() {
        val fused = LocationServices.getFusedLocationProviderClient(this)
        val request = LocationRequest.Builder(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            1000
        ).build()

        val callback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                if(location!=null) {
                    val locationDetails = geocoder.getFromLocation(
                        location.latitude, location.longitude, 1
                    )
                    val output = "Locality:"+ locationDetails?.get(0)?.getAddressLine(0)
                    binding.pickupaddressinputfieldid.setText(output)
                }
                super.onLocationResult(result)
            }
        }

        fused.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )
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
                if (id == 1) binding.namefieldId1.text = selectedDate
                else if (id == 2) binding.namefieldId44.text = selectedDate
            }, year, month, day
        ).show()
    }else {
        val hour = calender.get(Calendar.HOUR)
        val minute = calender.get(Calendar.MINUTE)
        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            Log.d("MainActivity","Time: $selectedHour:$selectedMinute")
            val selectedTime = "$selectedHour:$selectedMinute"
            if (id == 1) binding.namefieldId2.text = selectedTime
            else if (id == 2) binding.namefieldId45.text = selectedTime
        }, hour, minute, false).show()
    }
}
