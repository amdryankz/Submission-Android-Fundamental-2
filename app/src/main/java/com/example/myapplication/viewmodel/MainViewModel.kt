package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.database.SearchUserResponse
import com.example.myapplication.database.UserResponse
import com.example.myapplication.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel : ViewModel() {
    private val _user = MutableLiveData<List<UserResponse>>()
    val user: LiveData<List<UserResponse>> = _user

    private val _searchUser = MutableLiveData<List<UserResponse>>()
    val searchUser: LiveData<List<UserResponse>> = _searchUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var filtered = mutableListOf<UserResponse>()

    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        getGithubUser()
    }

    private fun getGithubUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser()
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getSearch(searchUser: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(searchUser)
        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchUser.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    var newText: String = ""
        set(value) {
            field = value
            search()
        }

    fun search() {
        filtered.clear()
        val filteredtext = newText.lowercase(Locale.getDefault())
        if (filteredtext.isNotEmpty()) {
            getSearch(filteredtext)
        }
    }
}