package com.example.spizeur.ui.commandInfo


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.spizeur.MainActivity
import com.example.spizeur.R
import com.example.spizeur.models.Address
import com.example.spizeur.models.PaymentInformation
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class CommandInfoActivity : AppCompatActivity() {
    private lateinit var vm: CommandInfoViewModel
    val myCalendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command_info)
        vm = ViewModelProvider(this).get(CommandInfoViewModel::class.java)

        val editText = findViewById<EditText>(R.id.expiration_date_input)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 2 && !s.contains("/")) {
                    s.insert(2, "/")
                } else if (s?.length == 3 && s[2] != '/') {
                    s.insert(2, "/")
                }
            }
        })

        // address information
        val address = vm.getUserAddress()
        if (address == null) {
            findViewById<RadioButton>(R.id.YourAddressButton).isEnabled = false
            findViewById<RadioButton>(R.id.OtherAddressButton).isChecked = true
            findViewById<TextView>(R.id.AddressDetailTextView).text = "No address registered"
        } else {
            findViewById<RadioButton>(R.id.YourAddressButton).isChecked= true
            findViewById<TextView>(R.id.AddressDetailTextView).text = address.address
        }

        // payment information
        val paymentInfo = vm.getUserPaymentInfo()
        if (paymentInfo != null) {
            val cardNumber = paymentInfo.number
            val cardNumberLength = cardNumber.length
            val cardNumberLastFourDigits = cardNumber.substring(cardNumberLength - 4, cardNumberLength)
            findViewById<TextView>(R.id.RegisteredCardTextView).text = "**** **** **** $cardNumberLastFourDigits"
            findViewById<RadioButton>(R.id.RegisteredCardButton).isChecked = true
        } else {
            findViewById<TextView>(R.id.RegisteredCardTextView).text = "No card registered"
            findViewById<RadioButton>(R.id.RegisteredCardButton).isEnabled = false
            findViewById<RadioButton>(R.id.OtherCardButton).isChecked = true
        }

        // edit total text view
        val total = vm.getTotal()
        findViewById<TextView>(R.id.PriceWithoutShippingTextView).text = "Total excl. shipping cost : ${BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN)} €"
        val shippingCost = (total * 0.2).dec()
        findViewById<TextView>(R.id.ShippingCostTextView).text = "Shipping cost : ${BigDecimal(shippingCost).setScale(2, RoundingMode.HALF_EVEN)} €"

        findViewById<TextView>(R.id.TotalCostNumberTextView).text = "${BigDecimal(total + shippingCost).setScale(2, RoundingMode.HALF_EVEN)} €"


        findViewById<Button>(R.id.CommandButton).setOnClickListener {
            command()
            val intent = Intent(this@CommandInfoActivity, MainActivity::class.java)
            startActivity(intent)
        }

        // add function to go back button
        findViewById<ImageButton>(R.id.command_info_share).setOnClickListener {
            val intent = Intent(this@CommandInfoActivity, MainActivity::class.java)
            startActivity(intent)
        }


    }

    private fun updateLabel(editText: EditText) {
        val myFormat = "MM/yy"
        val dateFormat = SimpleDateFormat(myFormat)
        editText.setText(dateFormat.format(myCalendar.time))
    }

    fun command() {
        var address = vm.getUserAddress()
        var paymentInfo = vm.getUserPaymentInfo()
        if (address != null && paymentInfo != null) {
            vm.command(address, paymentInfo)
            return
        }

        if (address == null) {
            val addressText = findViewById<TextView>(R.id.address_input).text.toString()
            val postalCode = findViewById<TextView>(R.id.postal_code_input).text.toString()
            val city = findViewById<TextView>(R.id.city_input).text.toString()
            address = Address(addressText, city, postalCode)
        }

        if (paymentInfo == null) {
            val cardNumber = findViewById<TextView>(R.id.card_number_input).text.toString()
            val expireDateString = findViewById<TextView>(R.id.expiration_date_input).text.toString()
            val expireDate = SimpleDateFormat("MM/yy").parse(expireDateString) ?: Date()
            val code = findViewById<TextView>(R.id.security_code_input).text.toString()
            val name = findViewById<TextView>(R.id.name_card_input).text.toString()
            paymentInfo = PaymentInformation(cardNumber, expireDate, code, name)
        }

        vm.command(address, paymentInfo)


    }





}