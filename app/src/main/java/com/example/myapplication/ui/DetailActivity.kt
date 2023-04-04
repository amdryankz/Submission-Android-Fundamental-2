package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.database.DetailUserResponse
import com.example.myapplication.R
import com.example.myapplication.database.UserResponse
import com.example.myapplication.databinding.ActivityDetailBinding
import com.example.myapplication.viewmodel.DetailViewModel
import com.example.myapplication.viewmodel.FavoriteViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var isFav: Boolean = false

    companion object {
        const val KEY_USERS = "key_users"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel = obtainViewModel(this)
        val user = intent.getParcelableExtra<UserResponse>(KEY_USERS) as UserResponse

        if (detailViewModel.USERNAME.isEmpty()) {
            detailViewModel.USERNAME = user.login
        }

        detailViewModel.detailUser.observe(this) { detail ->
            setUserDetail(detail)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getAllUsers().observe(this) {
            isFav = it.contains(user)
            if (isFav) {
                binding.fabAdd.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.baseline_favorite_24,
                        theme
                    )
                )
            } else {
                binding.fabAdd.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.baseline_favorite_border_24,
                        theme
                    )
                )
            }
        }

        binding.fabAdd.setOnClickListener {
            if (isFav) {
                detailViewModel.delete(user)
                binding.fabAdd.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.baseline_favorite_border_24,
                        theme
                    )
                )
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.is_deleted_from_fav)),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        resources.getString(R.string.undo)
                    ) {
                        detailViewModel.insert(user)
                        Toast.makeText(this, resources.getString(R.string.undo), Toast.LENGTH_SHORT)
                            .show()
                    }.show()
            } else {
                detailViewModel.insert(user)
                binding.fabAdd.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.baseline_favorite_24,
                        theme
                    )
                )
                Snackbar.make(
                    binding.root,
                    StringBuilder(user.login + " ").append(resources.getString(R.string.is_added_to_fav)),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(
                        resources.getString(R.string.see_favorite)
                    ) { startActivity(Intent(this, FavoriteActivity::class.java)) }.show()
            }
        }

        sectionPager()
    }

    private fun setUserDetail(userDetail: DetailUserResponse) {
        binding.apply {
            usernameDetail.text = userDetail.login
            nameDetail.text = userDetail.name
            followingDetail.text = userDetail.following
            followersDetail.text = userDetail.followers
        }
        Glide.with(this)
            .load(userDetail.avatarUrl)
            .into(binding.detailImg)
        supportActionBar?.title = userDetail.login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun sectionPager() {
        val sectionPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = FavoriteViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }
}