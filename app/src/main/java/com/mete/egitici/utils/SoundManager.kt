package com.mete.egitici.utils

import android.content.Context
import android.media.MediaPlayer
import com.mete.egitici.R
import kotlin.random.Random

object SoundManager {
    
    private var mediaPlayer: MediaPlayer? = null
    
    fun playSound(context: Context, soundResId: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundResId)
            mediaPlayer?.start()
        } catch (e: IllegalStateException) {
            // MediaPlayer in invalid state
        } catch (e: IllegalArgumentException) {
            // Invalid resource
        }
    }
    
    fun playWelcomeSound(context: Context) {
        try {
            val resId = context.resources.getIdentifier("welcome_sound", "raw", context.packageName)
            if (resId != 0) {
                playSound(context, resId)
            }
        } catch (e: Exception) {
            // Welcome sound not found, continue without sound
        }
    }
    
    fun playCorrectSound(context: Context) {
        try {
            val resId = context.resources.getIdentifier("correct_answer", "raw", context.packageName)
            if (resId != 0) {
                playSound(context, resId)
            }
        } catch (e: Exception) {
            // Correct answer sound not found, continue without sound
        }
    }
    
    fun playWrongSound(context: Context) {
        try {
            val resId = context.resources.getIdentifier("wrong_answer", "raw", context.packageName)
            if (resId != 0) {
                playSound(context, resId)
            }
        } catch (e: Exception) {
            // Wrong answer sound not found, continue without sound
        }
    }
    
    /**
     * Get a random motivational message for correct answers
     */
    fun getCorrectAnswerMessage(context: Context): String {
        val messages = arrayOf(
            context.getString(R.string.msg_bravo_mete),
            context.getString(R.string.msg_excellent_mete),
            context.getString(R.string.msg_great_job_mete),
            context.getString(R.string.msg_super_mete),
            context.getString(R.string.msg_well_done_mete)
        )
        return messages[Random.nextInt(messages.size)]
    }
    
    /**
     * Get a random motivational message for wrong answers
     */
    fun getWrongAnswerMessage(context: Context): String {
        val messages = arrayOf(
            context.getString(R.string.msg_keep_trying_mete),
            context.getString(R.string.msg_try_once_more_mete),
            context.getString(R.string.msg_dont_give_up_mete),
            context.getString(R.string.msg_almost_there_mete),
            context.getString(R.string.msg_you_can_do_it_mete)
        )
        return messages[Random.nextInt(messages.size)]
    }
    
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
