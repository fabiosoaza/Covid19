package com.example.covid19

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.boletim.view.*


class BoletimAdapter(
    private val boletins: List<Boletim>): RecyclerView.Adapter<BoletimAdapter.VH>() {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

    val v = LayoutInflater.from(parent.context).inflate(R.layout.boletim, parent, false)

    val vh = VH(v)
    vh.itemView.setOnClickListener {
      val boletim = boletins[vh.adapterPosition]
    }
    return vh

  }

  override fun getItemCount(): Int {
  return  boletins.size
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    var (txtMortes,txtNconfirmados,txtData,txtHora)=  boletins[position]
    holder.txtMortes.text=boletins[position].mortes.toString()
    holder.txtNconfirmados.text=boletins[position].confirmados.toString()
    holder.txtData.text= boletins[position].data.toString()
    holder.txtHora.text=boletins[position].hora.toString()

  }

  class VH(itenView: View):RecyclerView.ViewHolder(itenView){
    var txtMortes:TextView = itenView.txtMortes
    var txtNconfirmados:TextView = itenView.txtNConfirmados
    var txtData:TextView = itenView.txtData
    var txtHora:TextView = itenView.txtHora
  }
}
