package com.example.myapplication.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myapplication.database.UserDao
import com.example.myapplication.database.UserDatabase
import com.example.myapplication.database.UserResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUsersDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getDatabase(application)
        mUsersDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<UserResponse>> = mUsersDao.getAllUsers()

    fun insert(note: UserResponse) {
        executorService.execute { mUsersDao.insert(note) }
    }

    fun delete(note: UserResponse) {
        executorService.execute { mUsersDao.delete(note) }
    }

}