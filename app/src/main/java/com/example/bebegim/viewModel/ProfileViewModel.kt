package com.example.bebegim.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bebegim.room.DAO.UsersDao
import com.example.bebegim.room.Users
import kotlinx.coroutines.launch

class ProfileViewModel(private val usersDao: UsersDao) : ViewModel() {

    private val _user = mutableStateOf<Users?>(null)
    val user: State<Users?> get() = _user

    fun loadUserByEmail(email: String) {
        viewModelScope.launch {
            _user.value = usersDao.getUserByEmail(email)
        }
    }
}