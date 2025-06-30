package com.example.bebegim.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bebegim.room.Babies
import com.example.bebegim.room.DAO.BabiesDao
import com.example.bebegim.room.DAO.UsersDao
import com.example.bebegim.room.Users
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    usersDao: UsersDao,
    babiesDao: BabiesDao,
    userId: String
) : ViewModel() {
    val user: StateFlow<Users?> = usersDao.getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val babies: StateFlow<List<Babies>> = babiesDao.getAllBabies()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}