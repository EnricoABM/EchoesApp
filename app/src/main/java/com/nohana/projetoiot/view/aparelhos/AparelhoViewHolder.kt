package com.nohana.projetoiot.view.aparelhos

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nohana.projetoiot.R

class AparelhoViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val titulo = view.findViewById<TextView>(R.id.item_aparelho_titulo)
    val info = view.findViewById<TextView>(R.id.item_aparelho_info)
    val checkBox = view.findViewById<CheckBox>(R.id.item_aparelho_checkbox)
}