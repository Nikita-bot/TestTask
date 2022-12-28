package ru.pazderin.bindata

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ru.pazderin.bindata.classes.Wrapper
import ru.pazderin.bindata.databinding.ActivityShowDataBinding


class ShowDataActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        var intent = getIntent()
        var data = intent.getStringExtra("JSON")
        var wrapper = Gson().fromJson(data,Wrapper::class.java)

        binding.apply {
            scheme.text = if(wrapper.scheme.isNullOrEmpty()) "?" else wrapper.scheme
            type.text = if(wrapper.type.isNullOrEmpty()) "?" else wrapper.type
            bank.text = if(wrapper.bank?.name.isNullOrEmpty()) "?" else wrapper.bank?.name +
                    ","+if(wrapper.bank?.city.isNullOrEmpty()) "?" else wrapper.bank?.city +
                    "\n"+ if(wrapper.bank?.url.isNullOrEmpty()) "?" else wrapper.bank?.url
            phone.text = if(wrapper.bank?.phone.isNullOrEmpty()) "?" else wrapper.bank?.phone
            brand.text = if(wrapper.brand.isNullOrEmpty()) "?" else wrapper.brand
            prepaid.text = if(wrapper.prepaid == null) "?" else wrapper.prepaid.toString()
            cardNumber.text = "Lenght: "+if(wrapper.number?.length == null) "?" else {wrapper.number?.length} +
                    "\nLuhn: " + if(wrapper.number?.luhn == null) "?" else wrapper.number?.luhn
            country.text = if(wrapper.country?.name.isNullOrEmpty()) "?" else{wrapper.country?.name}+
                    "\n"+"Latitude: "+ if(wrapper.country?.latitude == null) "?" else {wrapper.country?.latitude} +
                    "\n"+"Longitude: "+ if(wrapper.country?.longitude == null)"?" else wrapper.country?.longitude
        }

        binding.bank.setOnClickListener {
            if(!wrapper.bank?.url.isNullOrEmpty()){
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://${wrapper.bank?.url}"))
                startActivity(intent)
            }
        }

        binding.phone.setOnClickListener {
            if(!wrapper.bank?.phone.isNullOrEmpty()){
                var phoneIntent = Intent(Intent.ACTION_DIAL)
                phoneIntent.data = Uri.parse("tel:" + "${binding.phone.text}")
                startActivity(phoneIntent)
            }
        }

        binding.country.setOnClickListener {
            if(wrapper.country?.latitude != null && wrapper.country?.longitude != null){
                val mapIntent = Intent(Intent.ACTION_VIEW)
                mapIntent.data = Uri.parse("geo:" + "${wrapper.country?.latitude},${wrapper.country?.longitude}")
                startActivity(mapIntent)
            }
        }

    }
}