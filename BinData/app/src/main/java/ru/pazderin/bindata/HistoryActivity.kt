package ru.pazderin.bindata

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import ru.pazderin.bindata.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity(), CellClickListener {

    lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = getIntent()
        var list = intent.getStringArrayListExtra("ListResults") as ArrayList<String>

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MyAdapter(list,this)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.clear.setOnClickListener {
            val intent = getIntent()
            intent.putExtra("Clear","")
            setResult(RESULT_OK,intent)
            finish()
        }

    }

    override fun onClick(element: String) {
        val intent = getIntent()
        intent.putExtra("OldSearch",element)
        setResult(RESULT_OK,intent)
        finish()

    }
}
interface CellClickListener{
    fun onClick(element:String)
}