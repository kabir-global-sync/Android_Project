package com.example.carbookingapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.carbookingapp.databinding.ActivityLoginPageBinding

class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener{
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            if(username == "Fateh" && password == "12345"){
                Toast.makeText(this,"Success!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Invalid!",Toast.LENGTH_LONG).show()
            }
        }
        val infoButton = findViewById<ImageButton>(R.id.infoalertid)
        infoButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Password Requirements")
                .setMessage(
                    "• At least 8 characters\n" +
                            "• One uppercase letter\n" +
                            "• One lowercase letter\n" +
                            "• One number\n" +
                            "• One special character (!@#\$%^&*)"
                )
                .setPositiveButton("OK", null)
                .show()
        }

    }
}
