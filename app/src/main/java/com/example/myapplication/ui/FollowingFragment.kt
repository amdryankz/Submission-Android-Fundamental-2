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
import com.example.myapplication.databinding.FragmentFollowingBinding
import com.example.myapplication.viewmodel.DetailViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)

        val detailViewModel = ViewModelProvider(requireActivity())[DetailViewModel::class.java]

        detailViewModel.detailFollowing.observe(viewLifecycleOwner) {
            setUserFollowingData(it)
        }

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return binding.root
    }

    private fun setUserFollowingData(user: List<UserResponse>) {
        binding.labelFollowing.visibility = if (user.isEmpty()) View.VISIBLE else View.GONE
        binding.rvFollowing.apply {
            binding.rvFollowing.layoutManager = LinearLayoutManager(context)
            val listUserAdapter = ListUserAdapter(user)
            binding.rvFollowing.adapter = listUserAdapter

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