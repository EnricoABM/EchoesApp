package com.nohana.projetoiot.view.aparelhos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nohana.projetoiot.view.aparelhoconfig.AparelhoConfigActivity
import com.nohana.projetoiot.R
import com.nohana.projetoiot.controller.AparelhoController
import com.nohana.projetoiot.view.nfc.ReaderNfcActivity

class AparelhosFragment : Fragment(R.layout.fragment_aparelhos) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.tela_aparelhos_recycler)
        val adapter = AparelhoAdapter(AparelhoController().devices)

        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter = adapter

        val button = view.findViewById<Button>(R.id.tela_aparelhos_botao)

        button.setOnClickListener {
            val intent = Intent(requireContext(), AparelhoConfigActivity::class.java)
            startActivity(intent)
        }

        val nfcButton = view.findViewById<FloatingActionButton>(R.id.tela_aparelhos_botao_nfc)
        nfcButton.setOnClickListener {
            val intent = Intent(requireContext(), ReaderNfcActivity::class.java)
            startActivity(intent)
        }
    }

}