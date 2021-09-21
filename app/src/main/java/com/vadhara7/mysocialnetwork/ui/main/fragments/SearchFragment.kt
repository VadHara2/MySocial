package com.vadhara7.mysocialnetwork.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.adapters.UserAdapter
import com.vadhara7.mysocialnetwork.databinding.FragmentSearchBinding
import com.vadhara7.mysocialnetwork.other.Constants.SEARCH_TIME_DELAY
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.SearchViewModel
import com.vadhara7.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    @Inject
    lateinit var userAdapter: UserAdapter
    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            setupRecyclerView()
            subscribeToObservers()

            var job: Job? = null
            etSearch.addTextChangedListener { editable ->
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(SEARCH_TIME_DELAY)
                    editable?.let {
                        viewModel.searchUser(it.toString())
                    }
                }
            }
        }


        userAdapter.setOnUserClickListener { user ->
            findNavController()
                .navigate(
                    SearchFragmentDirections.actionGlobalOthersProfileFragment2(user.uid)
                )
        }
    }

    private fun FragmentSearchBinding.subscribeToObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner, EventObserver(
            onError = {
                searchProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = {
                searchProgressBar.isVisible = true
            }
        ) { users ->
            searchProgressBar.isVisible = false
            userAdapter.users = users
        })
    }

    private fun FragmentSearchBinding.setupRecyclerView() = rvSearchResults.apply {
        layoutManager = LinearLayoutManager(requireContext())
        adapter = userAdapter
        itemAnimator = null
    }
}