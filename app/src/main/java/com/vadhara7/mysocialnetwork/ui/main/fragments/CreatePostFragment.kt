package com.vadhara7.mysocialnetwork.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.canhub.cropper.*
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.FragmentCreatePostBinding
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.main.MainActivity
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.CreatePostViewModel
import com.vadhara7.mysocialnetwork.ui.snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment(R.layout.fragment_create_post) {

    @Inject
    lateinit var glide: RequestManager
    private val viewModel: CreatePostViewModel by viewModels()
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var cropContent: ActivityResultLauncher<String>
    private var curImageUri: Uri? = null

    private val cropActivityResultContract = object : ActivityResultContract<String, Uri?>() {
        override fun createIntent(context: Context, input: String?): Intent {
            return CropImage.activity()
                .setAspectRatio(16, 9)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uriContent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cropContent = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                viewModel.setCurImageUri(it)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.apply {
            btnSetPostImage.setOnClickListener {
                cropContent.launch("image/*")
            }
            ivPostImage.setOnClickListener {
                cropContent.launch("image/*")
            }
            btnPost.setOnClickListener {
                curImageUri?.let { uri ->
                    viewModel.createPost(uri, etPostDescription.text.toString())
                } ?: snackbar(getString(R.string.error_no_image_chosen))
            }
        }

    }

    private fun subscribeToObservers() {

        binding.apply {

            viewModel.curImageUri.observe(viewLifecycleOwner) {
                curImageUri = it
                btnSetPostImage.isVisible = false
                glide.load(curImageUri).into(ivPostImage)
            }
            viewModel.createPostStatus.observe(viewLifecycleOwner, EventObserver(
                onError = {
                    createPostProgressBar.isVisible = false
                    snackbar(it)
                },
                onLoading = { createPostProgressBar.isVisible = true }
            ) {
                createPostProgressBar.isVisible = false
                val activity = requireActivity() as MainActivity
                val homeFragment = HomeFragment()
                activity.makeCurrentFragment(homeFragment)
            })
        }
    }

}