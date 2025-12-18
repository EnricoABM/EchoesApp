package com.nohana.projetoiot.view.perfil

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.nohana.projetoiot.R

class PerfilFragment : Fragment(R.layout.fragment_perfil) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<Button>(R.id.tela_perfil_botao)

        btn.setOnClickListener {

        }
    }
}