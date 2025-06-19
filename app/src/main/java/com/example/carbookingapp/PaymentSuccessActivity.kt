package com.example.carbookingapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class PaymentSuccessActivity : AppCompatActivity() {
    companion object {
        private const val CHANNEL_ID = "booking_confirmation_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        // Retrieve all booking details from SharedPreferences
        val prefs = getSharedPreferences("BookingPrefs", MODE_PRIVATE)
        val bookingDetails = """
            Pickup: ${prefs.getString("pickupAddress", "")}
            Drop: ${prefs.getString("dropAddress", "")}
            Date ${prefs.getString("date", "")}
            Time ${prefs.getString("time", "")}
            Contact: ${prefs.getString("contact", "")}
            Payment: ${prefs.getString("paymentAmount", "")} via ${prefs.getString("paymentMethod", "")}
        """.trimIndent()

        // Update UI with booking details
        findViewById<TextView>(R.id.bookingDetailsTextView).text = bookingDetails

        // Send confirmation
        sendBookingConfirmation(bookingDetails)
    }

    private fun sendBookingConfirmation(details: String) {
        createNotificationChannel()
        sendNotification(details)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Booking Confirmations"
            val descriptionText = "Notifications for booking confirmations"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(details: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ic_booking_confirmation)
            .setContentTitle("Booking Confirmed!")
            .setContentText("Your ride has been booked successfully")
            .setStyle(NotificationCompat.BigTextStyle().bigText(details))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@PaymentSuccessActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request notification permission if needed (API 33+)
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }
}