package com.mete.egitici.utils

import android.content.Context
import android.media.MediaPlayer
import com.mete.egitici.R

object SoundManager {
    
    private var mediaPlayer: MediaPlayer? = null
    
    fun playSound(context: Context, soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
        mediaPlayer?.start()
    }
    
    fun playCorrectSound(context: Context) {
        // playSound(context, R.raw.correct_answer)
    }
    
    fun playWrongSound(context: Context) {
        // playSound(context, R.raw.wrong_answer)
    }
    
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
