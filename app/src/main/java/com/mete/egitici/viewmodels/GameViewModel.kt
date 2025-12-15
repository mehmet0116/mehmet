package com.mete.egitici.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mete.egitici.models.Game

class GameViewModel : ViewModel() {
    
    private val _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games
    
    fun loadGames() {
        // Load games from repository
        // This is a placeholder implementation
    }
}
