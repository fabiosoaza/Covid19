package com.example.covid19

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object BoletimHttp {
    const val Json_URL = "https://raw.githubusercontent.com/ramonsl/ws-covid/master/db.json"

    fun hasConnection(ctx: Context): Boolean {
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }


    private fun readBoletins(json: JSONArray): List<Boletim> {
        val boletins = mutableListOf<Boletim>()
        try {
            for (i in 0 until json.length()) {
                var js = json.getJSONObject(i)
                val dia = formatarData(js.getString("boletim").substring(0, 10))
                val hora = js.getString("boletim").substring(11, 18)
                val boletim = Boletim(
                    js.getInt("Suspeitos"),
                    js.getInt("Confirmados"),
                    js.getInt("Descartados"),
                    js.getInt("Monitoramento"),
                    js.getInt("Descartados"),
                    js.getInt("Sdomiciliar"),
                    js.getInt("Shopitalar"),
                    js.getInt("mortes"), dia, hora
                )

                boletins.add(boletim)
            }
        } catch (e: IOException) {
            Log.e("Erro", "Impossivel ler JSON")
        }
        return boletins
    }

    fun formatarData(data: String): String {
        val diaString = data
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var date = LocalDate.parse(diaString)
        var formattedDate = date.format(formatter)
        return formattedDate
    }


    fun loadBoletim(): List<Boletim>? {
        try {

            val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url(Json_URL)
                .get()
                .build()

            val response: Response = client.newCall(request).execute()
            val json = JSONArray(response.body?.string())
            return readBoletins(json)

        } catch (e: Exception) {
            Log.e("ERRO", e.message)
            e.printStackTrace()
        }
        return null
    }

}
