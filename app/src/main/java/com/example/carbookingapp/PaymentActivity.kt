package com.example.carbookingapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carbookingapp.databinding.ActivityPaymentBinding
import java.text.DecimalFormat
import kotlin.random.Random

class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private var paymentAmount: Double = 0.0
    private var selectedPaymentMethod: String? = null
    var formattedNumber:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPaymentAmount()
        setupPaymentOptions()
        setupPayButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setupPaymentAmount() {
        paymentAmount = Random.nextDouble(20.0, 200.0)
        formattedNumber = DecimalFormat("#.00").format(paymentAmount)
        binding.amountid.text = "$$formattedNumber"
    }

    private fun setupPaymentOptions() {
        val paymentOptions = listOf(
            PaymentOption("UPI", R.drawable.upilogo),
            PaymentOption("Net Banking", R.drawable.netbanklogo),
            PaymentOption("Cash On Delivery", R.drawable.codlogo)
        )

        val adapter = PaymentOptionAdapter(paymentOptions) { option ->
            selectedPaymentMethod = option.name
            Toast.makeText(this, "${option.name} selected", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewid.apply {
            layoutManager = LinearLayoutManager(this@PaymentActivity)
            addItemDecoration(DividerItemDecoration(this@PaymentActivity, DividerItemDecoration.VERTICAL))
            this.adapter = adapter
        }
    }

    private fun setupPayButton() {
        binding.payButton.setOnClickListener {
            if (selectedPaymentMethod == null) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            processPayment()
        }
    }

    private fun processPayment() {
        binding.progressBar.visibility = View.VISIBLE
        binding.payButton.isEnabled = false

        // Simulate payment processing
        Handler(Looper.getMainLooper()).postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.payButton.isEnabled = true

            // Save payment details
            savePaymentDetails()

            // Proceed to success screen
            startActivity(Intent(this, PaymentSuccessActivity::class.java))
            finish()
        }, 2000)
    }

    private fun savePaymentDetails() {
        val sharedPref = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(Constant.PAYMENT_DETAILS, formattedNumber.toString())
            apply()
        }
    }
}

data class PaymentOption(
    val name: String,
    val iconRes: Int
)

class PaymentOptionAdapter(
    private val options: List<PaymentOption>,
    private val onItemClick: (PaymentOption) -> Unit
) : RecyclerView.Adapter<PaymentOptionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.optionIcon)
        private val name: TextView = itemView.findViewById(R.id.optionName)

        fun bind(option: PaymentOption) {
            icon.setImageResource(option.iconRes)
            name.text = option.name

            itemView.setOnClickListener { onItemClick(option) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount() = options.size
}