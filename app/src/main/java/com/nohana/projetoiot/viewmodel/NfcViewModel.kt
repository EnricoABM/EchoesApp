package com.nohana.projetoiot.viewmodel

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.nohana.projetoiot.model.animal.Animal
import com.nohana.projetoiot.model.nfc.TagNfc
import kotlinx.coroutines.flow.StateFlow

class NfcViewModel(
    context: Context
): ViewModel() {



    // MediaPlayer para reprodução de áudio da leitura
    var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false


    fun read(intent: Intent) {

    }

    fun write(intent: Intent) {

    }

    fun enabledNfc() {
    }

    fun disableNfc() {
    }

    fun readTag(intent: Intent, animals: StateFlow<List<Animal>>) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
        val ndef = Ndef.get(tag) ?: run {
            throw RuntimeException("Dados Inválidos")
        }

        try {
            ndef.connect()
            val records = ndef.cachedNdefMessage?.records
            ndef.close()

            if (records == null || records.size < 2) {
                throw RuntimeException("Dados Inválidos")
            }

            val animalName = String(records[0].payload).drop(3)
            val positionName = String(records[1].payload).drop(3)

            // Busca o audioUrl no banco via ViewModel
            val audioUrl = this.getAudioUrlForTag(TagNfc(animalName, positionName), animals)

            if (audioUrl.isNullOrBlank()) {
                throw RuntimeException("Nenhum cenário ativo para '$positionName' de '$animalName'")
            }

            playAudio(audioUrl)

        } catch (e: Exception) {
            throw RuntimeException("Erro na leitura: ${e.message}")
        }
    }

    fun getAudioUrlForTag(tag: TagNfc, animals: StateFlow<List<Animal>>): String? {
        val animal = animals.value.find {
            it.name.equals(tag.animal, ignoreCase = true)
        } ?: return null

        val point = animal.listeningPoints.find {
            it.positionName.equals(tag.position, ignoreCase = true)
        } ?: return null

        return point.activeScenario?.audioUrl
    }

    fun playAudio(audioUrl: String) {
        stopPlayback()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(audioUrl)
            isLooping = true
            prepareAsync()
            setOnPreparedListener { start(); this@NfcViewModel.isPlaying = true }
        }
    }

    fun togglePlayback(audioUrl: String): Boolean {
        return if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPlaying = false
            false
        } else if (mediaPlayer != null) {
            mediaPlayer?.start()
            isPlaying = true
            true
        } else {
            playAudio(audioUrl)
            true
        }
    }

    fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
}