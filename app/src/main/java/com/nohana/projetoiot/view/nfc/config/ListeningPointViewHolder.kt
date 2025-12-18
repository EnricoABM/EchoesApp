package com.nohana.projetoiot.view.nfc.config

import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nohana.projetoiot.R

class ListeningPointViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    var position = view.findViewById<TextView>(R.id.item_posicao_nome)
    var diseases = view.findViewById<Spinner>(R.id.item_posicao_doencas)
}