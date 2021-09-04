package com.vadhara7.mysocialnetwork.ui.auth.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.FragmentLoginBinding
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.auth.AuthViewModel
import com.vadhara7.mysocialnetwork.ui.main.MainActivity
import com.vadhara7.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.apply {

            btnLogin.setOnClickListener {
                viewModel.login(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }

            tvRegisterNewAccount.setOnClickListener {
                if (findNavController().previousBackStackEntry != null) {
                    findNavController().popBackStack()
                } else findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                )
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.loginProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { binding.loginProgressBar.isVisible = true }
        ) {
            binding.loginProgressBar.isVisible = false
            Intent(requireContext(), MainActivity::class.java).also {
                startActivity(it)
                requireActivity().finish()
            }
        })
    }
}