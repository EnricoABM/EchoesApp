package com.nohana.projetoiot.view.aparelhos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nohana.projetoiot.R
import com.nohana.projetoiot.model.Device

class AparelhoAdapter(val list : List<Device>) : RecyclerView.Adapter<AparelhoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AparelhoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aparelho, parent, false)
        return AparelhoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AparelhoViewHolder, position: Int) {
        var aparelho = list[position]

        holder.titulo.text = aparelho.name
        holder.info.text = aparelho.animal.name
        holder.checkBox.isChecked = aparelho.isChecked
    }

    override fun getItemCount(): Int {
        return list.size
    }

}