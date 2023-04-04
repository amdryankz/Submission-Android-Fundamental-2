package com.example.myapplication.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UserResponse(
    @field:PrimaryKey
    @field:ColumnInfo(name = "login")
    @field:SerializedName("login")
    var login: String,

    @field:SerializedName("avatar_url")
    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,
) : Parcelable

data class DetailUserResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("followers")
    val followers: String,

    @field:SerializedName("following")
    val following: String
)

data class SearchUserResponse(
    @SerializedName("total_count")
    var totalCount: Int,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,

//	@SerializedName("items")
    var items: List<UserResponse>
)
