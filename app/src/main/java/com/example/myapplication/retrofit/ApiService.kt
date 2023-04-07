package com.example.myapplication.retrofit

import com.example.myapplication.database.DetailUserResponse
import com.example.myapplication.database.SearchUserResponse
import com.example.myapplication.database.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
//    @Headers("Authorization: token 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")
    fun getUser(): Call<List<UserResponse>>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserResponse>>

    @GET("search/users")
    fun getSearchUser(@Query("q") username: String): Call<SearchUserResponse>
}