package com.example.battleship.ui

import androidx.lifecycle.ViewModel
import com.example.battleship.data.firebase.FirebaseSource
import com.example.battleship.data.models.Player

class MainViewModel : ViewModel() {

    fun getMyPlayer(): Player {
        val currentUser = FirebaseSource.currentUser()
        return Player(currentUser?.displayName.toString(), 0)
    }
}