package com.example.covid19

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.view.MenuInflater as MenuInflater


class MainActivity : AppCompatActivity() {

  private  var  asyncTask : BoletimTask? =null
  private var boletinsList = mutableListOf<Boletim>()
  private var adapter = BoletimAdapter(boletinsList)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(findViewById(R.id.my_tool))
    carregaDados()
    initRecyclerView()
  }


  fun carregaDados(){
    if (boletinsList.isNotEmpty()){
      showProgress(false)
    }else{
      if(asyncTask==null){
        if (BoletimHttp.hasConnection(this)){
          starDonwload()
        }else{
          progressBar.visibility =View.GONE
          txtMsg.text = "Erro"
        }
      }else if(asyncTask?.status == AsyncTask.Status.RUNNING){
        showProgress(true)
      }
    }
  }
  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.menu_refresh -> {

      carregaDados()
      // Toast.makeText(this,"Print action",Toast.LENGTH_LONG).show()
      true
    }


    else -> {

      super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu,menu)
    return super.onCreateOptionsMenu(menu)
  }

  private fun initRecyclerView(){
    rvDados.adapter=adapter
    // val layoutManager = GridLayoutManager(this,1)
    val layoutManager = LinearLayoutManager(this)
    rvDados.layoutManager=layoutManager
  }


  private fun starDonwload(){
    if (asyncTask?.status!=AsyncTask.Status.RUNNING){
      asyncTask =BoletimTask()
      asyncTask?.execute()
    }
  }
  private fun updateBoletns(result: List<Boletim>?){
    if (result!=null){
      boletinsList.clear()
      boletinsList.addAll(result.reversed())
    }else{
      txtMsg.text = "Erro ao Carregar"
    }
    adapter.notifyDataSetChanged()
    asyncTask=null
  }

  fun showProgress(show: Boolean){
    if(show){
      txtMsg.text= "Carregando...."
    }
    txtMsg.visibility =if (show) View.VISIBLE else View.GONE
    progressBar.visibility =if (show) View.VISIBLE else View.GONE
  }

  inner class BoletimTask: AsyncTask<Void, Void, List<Boletim>?>() {
    override fun onPreExecute() {
      super.onPreExecute()
      showProgress(true)

    }

    override fun doInBackground(vararg p0: Void?): List<Boletim>? {
      return BoletimHttp.loadBoletim()

    }

    override fun onPostExecute(bo: List<Boletim>?) {
      super.onPostExecute(bo)
      showProgress(false)
      updateBoletns(bo)

    }

  }

}
