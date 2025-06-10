package com.example.carbookingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@SuppressLint("CustomSplashScreen")
class splashScreen : AppCompatActivity() {
    lateinit var progressBar : ProgressBar
    val progressUpdateInterval: Long=50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressBar = findViewById<ProgressBar>(R.id.progressBarid)
        simulateProgress()
    }
    fun simulateProgress(){
        val handler = Handler(Looper.getMainLooper())
        var progress=0
        val maxProgress=100
        val runnable = object:Runnable{
            override fun run() {
                progress += 5
                if (progress <= maxProgress) {
                    progressBar.progress = progress
                    handler.postDelayed(this, progressUpdateInterval)
                } else {
                    startActivity(Intent(this@splashScreen, LoginPageActivity::class.java))
                    finish()
                }
            }
        }
        handler.post(runnable)
    }
}