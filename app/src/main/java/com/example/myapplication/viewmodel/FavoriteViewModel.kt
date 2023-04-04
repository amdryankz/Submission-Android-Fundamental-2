package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.UserRepository
import com.example.myapplication.database.UserResponse

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)
    fun getAllUsers(): LiveData<List<UserResponse>> = mUserRepository.getAllUsers()
}