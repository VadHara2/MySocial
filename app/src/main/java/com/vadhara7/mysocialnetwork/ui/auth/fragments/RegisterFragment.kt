package com.vadhara7.mysocialnetwork.ui.auth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.FragmentRegisterBinding
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.auth.AuthViewModel
import com.vadhara7.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        subscribeToObservers()

        binding.apply {

            btnRegister.setOnClickListener {
                viewModel.register(
                    etEmail.text.toString(),
                    etUsername.text.toString(),
                    etPassword.text.toString(),
                    etRepeatPassword.text.toString()
                )
            }


            tvLogin.setOnClickListener {
                if(findNavController().previousBackStackEntry != null) {
                    findNavController().popBackStack()
                } else findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                )
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                binding.registerProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { binding.registerProgressBar.isVisible = true }
        ) {
            binding.registerProgressBar.isVisible = false
            snackbar(getString(R.string.success_registration))
        })
    }

}