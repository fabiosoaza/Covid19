package com.example.covid19

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.GridLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

  private var boletins = mutableListOf<Boletim>()
  private var adapter = BoletimAdapter(boletins)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    readJson(this)
    boletins.reverse()
    initRecyclerView()
  }


  fun readJson(context: Context){
    var json: String?=null
    try {
      val inputStream: InputStream= context.assets.open("data.json")
      json = inputStream.bufferedReader().use { it.readText() }
     // txtValue.text=json.toString()
      var jsonArray =JSONArray(json)
      for (i in 0 .. jsonArray.length()-1){
        var js = jsonArray.getJSONObject(i)
        val dia = formatarData(js.getString("boletim").substring(0,10))
        val hora = js.getString("boletim").substring(11,18)
        val boletim = Boletim(js.getInt("Suspeitos"),
          js.getInt("Confirmados"),
          js.getInt("Descartados"),
          js.getInt("Monitoramento"),
          js.getInt("Descartados"),
          js.getInt("Sdomiciliar"),
          js.getInt("Shopitalar"),
          js.getInt("mortes"),dia,hora)

        boletins.add(boletim)
      }
    }
    catch (e : IOException){
    Log.e("Erro", "Impossivel ler JSON")
    }


  }

  private fun initRecyclerView(){

    rvDados.adapter=adapter
   // val layoutManager = GridLayoutManager(this,1)
    val layoutManager = LinearLayoutManager(this)
    rvDados.layoutManager=layoutManager
  }

  fun formatarData(data: String): String {
    val diaString =data
    var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var date = LocalDate.parse(diaString)
    var formattedDate = date.format(formatter)
    return formattedDate
  }

}
