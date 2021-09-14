package com.vadhara7.mysocialnetwork.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.FragmentHomeBinding
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.BasePostViewModel
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BasePostFragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override val postProgressBar: ProgressBar
        get() = binding.allPostsProgressBar

    override val basePostViewModel: BasePostViewModel
        get() {
            val vm: HomeViewModel by viewModels()
            return vm
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() = binding.apply {
        rvAllPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }
}