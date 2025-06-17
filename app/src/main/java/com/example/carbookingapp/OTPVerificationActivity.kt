package com.example.carbookingapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var otpBoxes: List<EditText>
    private lateinit var verifyButton: Button
    private var generatedOTP: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverificationactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        otpBoxes = listOf(
            findViewById(R.id.otp1),
            findViewById(R.id.otp2),
            findViewById(R.id.otp3),
            findViewById(R.id.otp4),
            findViewById(R.id.otp5),
            findViewById(R.id.otp6)
        )

        verifyButton = findViewById(R.id.verifyButton)

        // Set up auto-move
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < otpBoxes.size - 1) {
                        otpBoxes[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        otpBoxes[i - 1].requestFocus()
                    }
                }
            })
        }

        val sharedPref = getSharedPreferences("MyAppPrefs",Context.MODE_PRIVATE)
        val contactNumber = sharedPref.getString("CONTACT_NUMBER", null)
        var thistext = findViewById<TextView>(R.id.textView12)
        thistext.text = "Enter the OTP sent to +91-$contactNumber"

        generatedOTP = (100000..999999).random().toString()
//        Handler(Looper.getMainLooper()).postDelayed({
//            createNotification(generatedOTP)
//        }, 5000)
        createNotification(generatedOTP)
        Toast.makeText(this,"Check Notification",Toast.LENGTH_LONG)
        verifyButton.setOnClickListener {
            val enteredOtp = otpBoxes.joinToString("") { it.text.toString() }
            if (enteredOtp == generatedOTP) {
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel("otp_channel","OTP CHANNEL", NotificationManager.IMPORTANCE_DEFAULT   )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
    private fun createNotification(otp: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.carbookinglogo)
        val builder = NotificationCompat.Builder(this, "otp_channel")
            .setSmallIcon(R.drawable.otplogo) // Replace with your icon
            .setLargeIcon(largeIcon)
            .setContentTitle("Your OTP Code")
            .setContentText("OTP code: $otp")
            .setWhen(System.currentTimeMillis())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, builder.build())
    }
}


