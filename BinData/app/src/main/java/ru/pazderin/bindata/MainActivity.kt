package ru.pazderin.bindata

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.pazderin.bindata.databinding.ActivityMainBinding
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(){

    lateinit var binding: ActivityMainBinding
    private var launcher:ActivityResultLauncher<Intent>?=null
    var pref:SharedPreferences? = null
    private val history = History()
    val APP_PREFERENCE = "BinDATA"
    val APP_PREFERENCE_LIST = "JSON_LIST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(APP_PREFERENCE,Context.MODE_PRIVATE)
        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        if (pref?.contains(APP_PREFERENCE_LIST)!!){
            val json = pref?.getString(APP_PREFERENCE_LIST,"")
            history.historyList = Gson().fromJson(json,itemType)
        }

        binding.btnSubmit.setOnClickListener {
            if(!checkInputData()){
                var data = getData()
                if(data.equals("")){
                    binding.inputBin.error = getString(R.string.error_message2)
                    history.historyList.add(binding.inputBin.text.toString())
                }
                else{
                    var intent =Intent(this,ShowDataActivity::class.java)
                    intent.putExtra("JSON",data)
                    history.historyList.add(binding.inputBin.text.toString())
                    binding.inputBin.setText("")
                    startActivity(intent)
                }
            }
        }

        binding.btnHistory.setOnClickListener {
            var intent = Intent(this,HistoryActivity::class.java)
            intent.putStringArrayListExtra("ListResults",history.historyList)
            binding.inputBin.setText("")
            launcher?.launch(intent)
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result:ActivityResult->
            if (result.resultCode == RESULT_OK){
                if (result.data?.hasExtra("Clear")!!){
                    clearHistory()
                }
                else{
                    Log.v("MyLog","Have Result")
                    binding.inputBin.setText(result.data?.getStringExtra("OldSearch"))
                }
            }
        }

    }


    private fun checkInputData():Boolean{
        if (binding.inputBin.text.isNullOrEmpty()){
            binding.inputBin.error = getString(R.string.error_message)
        }
        return binding.inputBin.text.isNullOrEmpty()
    }

//45717360

    private fun getData():String{
        var data = ""
        val t : Thread = Thread({
            try{
                val url = URL("https://lookup.binlist.net/${binding.inputBin.text.toString()}")
                val connection = url.openConnection() as HttpURLConnection
                data = connection.inputStream.bufferedReader().readText()
            }
            catch (e:Exception){
                data = ""
            }

        })
        t.start()
        t.join()
        return data
    }

    fun clearHistory(){
        val editor = pref?.edit()
        editor?.clear()
        editor?.apply()
        history.historyList = arrayListOf()
    }

    fun saveHistory(){
        val editor = pref?.edit()
        var json = Gson().toJson(history.historyList)
        editor?.putString(APP_PREFERENCE_LIST,json)
        editor?.apply()
    }


    override fun onStop() {
        super.onStop()
        saveHistory()
    }
}

