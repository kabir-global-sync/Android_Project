package com.example.carbookingapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat

class OTPVerificationActivity : AppCompatActivity() {

    private lateinit var otpBoxes: List<EditText>
    private lateinit var verifyButton: Button
    private var generatedOTP: String = ""
    private val NOTIFICATION_PERMISSION_CODE = 1001

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverificationactivity)

        // Create notification channels
        createNotificationChannels()

        // Check and request notification permission for Android 13+
        checkNotificationPermission()

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize OTP boxes
        otpBoxes = listOf(
            findViewById(R.id.otp1),
            findViewById(R.id.otp2),
            findViewById(R.id.otp3),
            findViewById(R.id.otp4),
            findViewById(R.id.otp5),
            findViewById(R.id.otp6)
        )

        verifyButton = findViewById(R.id.verifyButton)

        // Set up auto-move between OTP boxes
        setupOtpBoxesNavigation()

        // Display contact number
        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val contactNumber = sharedPref.getString("CONTACT_NUMBER", null)
        findViewById<TextView>(R.id.textView12).text = "Enter the OTP sent to +91-$contactNumber"

        // Generate and handle OTP
        generatedOTP = (100000..999999).random().toString()
        Log.d("OTP", "Generated OTP: $generatedOTP")

        // Show immediate notification
        createNotification(generatedOTP)
        Toast.makeText(this, "Check Notification for OTP", Toast.LENGTH_SHORT
        ).show()

        // Set up verify button click listener
        verifyButton.setOnClickListener {
            verifyOtp()
        }

        // Schedule a reminder notification after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            createNotification("Reminder: Your OTP is $generatedOTP")
        }, 5000)
    }

    private fun setupOtpBoxesNavigation() {
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
    }

    private fun verifyOtp() {
        val enteredOtp = otpBoxes.joinToString("") { it.text.toString() }
        if (enteredOtp == generatedOTP) {
            Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
            proceedToPayment()
        } else {
            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // OTP Channel
            val otpChannel = NotificationChannel(
                "otp_channel",
                "OTP Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "For OTP verification codes"
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }

            // Booking Channel
            val bookingChannel = NotificationChannel(
                "booking_channel",
                "Booking Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Booking confirmations and updates"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(otpChannel)
            manager.createNotificationChannel(bookingChannel)
        }
    }

    private fun createNotification(otp: String) {
        // Check if we have notification permission (for Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("Notification", "Notification permission not granted")
            return
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.carbookinglogo)

        val builder = NotificationCompat.Builder(this, "otp_channel")
            .setSmallIcon(R.drawable.otplogo)
            .setLargeIcon(largeIcon)
            .setContentTitle("Your OTP Code")
            .setContentText("OTP code: $otp")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())

        try {
            notificationManager.notify(1, builder.build())
            Log.d("Notification", "Notification sent with OTP: $otp")
        } catch (e: Exception) {
            Log.e("Notification", "Failed to show notification", e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, resend notification
                    createNotification(generatedOTP)
                } else {
                    Toast.makeText(
                        this,
                        "Notification permission denied. You won't receive OTP notifications.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun proceedToPayment() {
        // 1. Create payment intent
        val paymentIntent = Intent(this, PaymentActivity::class.java).apply {
            putExtra("OTP_VERIFIED", true)
            // Add any other payment details you need to pass
        }

        // 2. Add transition animation
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,  // Enter animation
            R.anim.slide_out_left   // Exit animation
        )

        // 3. Start payment activity
        startActivity(paymentIntent, options.toBundle())

        // 4. Finish current activity if needed
        finish()
    }
}