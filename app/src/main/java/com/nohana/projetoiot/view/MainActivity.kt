package com.nohana.projetoiot.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nohana.projetoiot.R
import com.nohana.projetoiot.view.aparelhos.AparelhosFragment
import com.nohana.projetoiot.view.perfil.PerfilFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav : BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val icAparelhos = AparelhosFragment()
        val icPerfil = PerfilFragment()

        // Navigation Bar
        this.setCurrentFragment(icAparelhos)

        nav.selectedItemId = R.id.icone_aparelhos

        nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.icone_aparelhos -> setCurrentFragment(icAparelhos)
                R.id.icone_perfil -> setCurrentFragment(icPerfil)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment).commit()
        }
    }
}