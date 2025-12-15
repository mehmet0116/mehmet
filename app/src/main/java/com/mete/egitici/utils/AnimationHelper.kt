package com.mete.egitici.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.View
import com.mete.egitici.R

/**
 * Helper class for managing UI animations
 */
class AnimationHelper(private val context: Context) {
    
    /**
     * Fade in animation
     */
    fun fadeIn(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        animation.duration = duration
        view.startAnimation(animation)
    }
    
    /**
     * Fade out animation
     */
    fun fadeOut(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        animation.duration = duration
        view.startAnimation(animation)
    }
    
    /**
     * Slide in from right animation
     */
    fun slideInFromRight(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        animation.duration = duration
        view.startAnimation(animation)
    }
    
    /**
     * Slide out to right animation
     */
    fun slideOutToRight(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
        animation.duration = duration
        view.startAnimation(animation)
    }
    
    /**
     * Bounce animation for success feedback
     */
    fun bounce(view: View) {
        view.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
    
    /**
     * Shake animation for error feedback
     */
    fun shake(view: View) {
        view.animate()
            .translationX(-10f)
            .setDuration(50)
            .withEndAction {
                view.animate()
                    .translationX(10f)
                    .setDuration(50)
                    .withEndAction {
                        view.animate()
                            .translationX(-10f)
                            .setDuration(50)
                            .withEndAction {
                                view.animate()
                                    .translationX(0f)
                                    .setDuration(50)
                                    .start()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }
    
    /**
     * Pulse animation for attention
     */
    fun pulse(view: View) {
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .alpha(0.8f)
            .setDuration(500)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .setDuration(500)
                    .start()
            }
            .start()
    }
    
    /**
     * Rotate animation for loading or refresh
     */
    fun rotate(view: View, degrees: Float = 360f, duration: Long = 1000) {
        view.animate()
            .rotation(degrees)
            .setDuration(duration)
            .start()
    }
    
    /**
     * Scale up animation
     */
    fun scaleUp(view: View, scale: Float = 1.5f, duration: Long = 300) {
        view.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(duration)
            .start()
    }
    
    /**
     * Scale down animation
     */
    fun scaleDown(view: View, duration: Long = 300) {
        view.animate()
            .scaleX(1.0f)
            .scaleY(1.0f)
            .setDuration(duration)
            .start()
    }
    
    /**
     * Star collection animation - for rewards
     */
    fun collectStar(view: View, onComplete: () -> Unit = {}) {
        view.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .alpha(0.0f)
            .setDuration(500)
            .withEndAction {
                onComplete()
                view.scaleX = 1.0f
                view.scaleY = 1.0f
                view.alpha = 1.0f
            }
            .start()
    }
}
