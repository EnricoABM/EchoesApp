//package com.example.projetoic.service
//
//import android.media.AudioAttributes
//import android.media.MediaPlayer
//import com.nohana.projetoiot.model.Disease
//
//class DiseaseMidiaPlayer {
//    private var mediaPlayer: MediaPlayer
//    private var isPlaying = false
//
//    constructor() {
//        this.mediaPlayer = MediaPlayer().apply {
//            setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .build()
//            )
//        }
//    }
//
//    fun play(disease: Disease?) {
//
//        mediaPlayer.reset()
//        mediaPlayer.setDataSource(disease?.audioUrl)
//        mediaPlayer.isLooping = true
//        mediaPlayer.prepareAsync()
//
//        mediaPlayer.setOnPreparedListener {
//            mediaPlayer.start()
//            isPlaying = true
//        }
//    }
//
//    fun startOrPause(): Boolean {
//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying) {
//                mediaPlayer.pause()
//            } else {
//                mediaPlayer.start()
//            }
//            isPlaying = !isPlaying
//        }
//        return isPlaying
//    }
//
//    fun stop() {
//        mediaPlayer.stop()
//    }
//
//}