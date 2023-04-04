package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.UserRepository
import com.example.myapplication.database.DetailUserResponse
import com.example.myapplication.database.UserResponse
import com.example.myapplication.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _detailFollowing = MutableLiveData<List<UserResponse>>()
    val detailFollowing: LiveData<List<UserResponse>> = _detailFollowing

    private val _detailFollowers = MutableLiveData<List<UserResponse>>()
    val detailFollowers: LiveData<List<UserResponse>> = _detailFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mUserRepository: UserRepository = UserRepository(application)

    fun getAllUsers(): LiveData<List<UserResponse>> = mUserRepository.getAllUsers()

    fun insert(user: UserResponse) {
        mUserRepository.insert(user)
    }

    fun delete(user: UserResponse) {
        mUserRepository.delete(user)
    }

    var USERNAME: String = ""
        set(value) {
            field = value
            getGithubDetail()
            getGithubFollowers()
            getGithubFollowing()
        }

    private fun getGithubDetail() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(USERNAME)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    private fun getGithubFollowers() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(USERNAME)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailFollowers.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    private fun getGithubFollowing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(USERNAME)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailFollowing.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}
