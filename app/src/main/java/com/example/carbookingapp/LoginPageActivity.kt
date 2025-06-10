package com.example.carbookingapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.carbookingapp.databinding.ActivityLoginPageBinding

class LoginPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener{
            val username = binding.editUsername.text.toString()
            val password = binding.editPassword.text.toString()
            val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*])[A-Za-z\\d!@#\$%^&*]{8,}$")
//            if(username.length>=7 && password.matches(passwordRegex)){
            if(username.length>=7 && password=="1234"){
//                Toast.makeText(this,"Success!",Toast.LENGTH_LONG).show()
                val intent = Intent(this@LoginPageActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"Invalid Credentials!",Toast.LENGTH_LONG).show()
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
