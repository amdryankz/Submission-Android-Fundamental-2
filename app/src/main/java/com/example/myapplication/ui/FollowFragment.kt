package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.database.UserResponse
import com.example.myapplication.databinding.FragmentFollowBinding
import com.example.myapplication.viewmodel.DetailViewModel

class FollowFragment(private val isFollowing: Boolean) : Fragment() {

    private var _fragmentFollowBinding: FragmentFollowBinding? = null
    private val binding get() = _fragmentFollowBinding
    private lateinit var listUserAdapter: ListUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentFollowBinding = FragmentFollowBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding?.rvFollow?.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding?.rvFollow?.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        if (isFollowing) {
            detailViewModel.detailFollowing.observe(viewLifecycleOwner) {
                setUserData(it)
            }
        } else {
            detailViewModel.detailFollowers.observe(viewLifecycleOwner) {
                setUserData(it)
            }
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding?.root
    }

    private fun setUserData(user: List<UserResponse>) {
        if (isFollowing) {
            binding?.labelFollow?.text = getString(R.string.no_following)
        } else {
            binding?.labelFollow?.text = getString(R.string.no_followers)
        }
        binding?.labelFollow?.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding?.rvFollow?.apply {
            listUserAdapter = ListUserAdapter(user)
            adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserResponse) {
                }
            })

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

