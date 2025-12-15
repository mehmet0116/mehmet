package com.mete.egitici.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mete.egitici.models.UserProfile

class UserViewModel : ViewModel() {
    
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile
    
    fun loadUserProfile(userId: Int) {
        // Load user profile from database
        // This is a placeholder implementation
    }
    
    fun updatePoints(points: Int) {
        _userProfile.value?.let { profile ->
            _userProfile.value = profile.copy(points = profile.points + points)
        }
    }
}
