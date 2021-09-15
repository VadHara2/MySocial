package com.vadhara7.mysocialnetwork.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.FragmentProfileBinding
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.BasePostViewModel
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.ProfileViewModel
import com.vadhara7.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class ProfileFragment : BasePostFragment(R.layout.fragment_profile) {
    protected lateinit var binding: FragmentProfileBinding
    override val postProgressBar: ProgressBar
        get() = binding.profilePostsProgressBar
    override val basePostViewModel: BasePostViewModel
        get() {
            val vm: ProfileViewModel by viewModels()
            return vm
        }

    protected val viewModel: ProfileViewModel
        get() = basePostViewModel as ProfileViewModel

    protected open val uid: String
        get() = FirebaseAuth.getInstance().uid!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setupRecyclerView()
            subscribeToObservers()
            btnToggleFollow.isVisible = false
        }
        viewModel.loadProfile(uid)
    }

    private fun FragmentProfileBinding.setupRecyclerView() = rvPosts.apply {
        adapter = postAdapter
        itemAnimator = null
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun FragmentProfileBinding.subscribeToObservers() {
        viewModel.profileMeta.observe(viewLifecycleOwner, EventObserver(
            onError = {
                profileMetaProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { profileMetaProgressBar.isVisible = true }
        ) { user ->
            profileMetaProgressBar.isVisible = false
            tvUsername.text = user.username
            tvProfileDescription.text = if (user.description.isEmpty()) {
                requireContext().getString(R.string.no_description)
            } else user.description
            glide.load(user.profilePictureUrl).into(ivProfileImage)
        })
    }
}