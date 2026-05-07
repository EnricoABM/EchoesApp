package com.nohana.projetoiot.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nohana.projetoiot.viewmodel.AnimalViewModel
import com.nohana.projetoiot.viewmodel.NfcViewModel

class AnimalViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnimalViewModel(context) as T
    }
}