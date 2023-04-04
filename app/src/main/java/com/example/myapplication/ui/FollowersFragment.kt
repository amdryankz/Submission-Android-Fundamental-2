package com.example.myapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.database.UserResponse
import com.example.myapplication.databinding.FragmentFollowersBinding
import com.example.myapplication.viewmodel.DetailViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollower.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.detailFollowers.observe(viewLifecycleOwner) {
            setUserFollowerData(it)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun setUserFollowerData(user: List<UserResponse>) {
        binding.labelFollower.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollower.apply {
            binding.rvFollower.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.rvFollower.adapter = listUserAdapter

            listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: UserResponse) {
                    val detailViewModel =
                        ViewModelProvider(requireActivity())[DetailViewModel::class.java]

                    detailViewModel.USERNAME = data.login
                }
            })

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}