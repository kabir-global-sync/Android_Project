package com.example.carbookingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carbookingapp.databinding.ActivityPaymentBinding
import java.text.DecimalFormat
import kotlin.random.Random

class paymentActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    lateinit var binding: ActivityPaymentBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val list=listOf(
            Option("UPI",R.drawable.upilogo),
            Option("Net Banking",R.drawable.netbanklogo),
            Option("Cash On Delivery",R.drawable.codlogo)
        )
        binding.recyclerViewid.adapter = ProfileRecyclerViewAdapter(list)
        binding.recyclerViewid.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        binding.recyclerViewid.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        val randomDouble = Random.nextDouble(20.0, 200.0)
        val formattedNumber = DecimalFormat("#.00").format(randomDouble)
        binding.amountid.text="$$formattedNumber"
    }
}

data class Option(
    val name:String,
    val res:Int
)