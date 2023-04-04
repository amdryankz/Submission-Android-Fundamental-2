package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserResponse)

    @Delete
    fun delete(user: UserResponse)

    @Query("SELECT * from UserResponse ORDER BY login ASC")
    fun getAllUsers(): LiveData<List<UserResponse>>
}