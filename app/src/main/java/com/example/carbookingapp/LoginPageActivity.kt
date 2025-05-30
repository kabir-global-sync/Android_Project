package com.example.carbookingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
            if(username.equals("Fateh") && password.equals("12345")){
                Toast.makeText(this,"Success!",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Invalid!",Toast.LENGTH_LONG).show()
            }
        }
    }
}
