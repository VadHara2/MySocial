package com.vadhara7.mysocialnetwork.ui.main.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.vadhara7.mysocialnetwork.adapters.PostAdapter
import com.vadhara7.mysocialnetwork.adapters.UserAdapter
import com.vadhara7.mysocialnetwork.other.EventObserver
import com.vadhara7.mysocialnetwork.ui.main.dialogs.DeletePostDialog
import com.vadhara7.mysocialnetwork.ui.main.dialogs.LikedByDialog
import com.vadhara7.mysocialnetwork.ui.main.viewmodels.BasePostViewModel
import com.vadhara7.mysocialnetwork.ui.snackbar
import javax.inject.Inject


abstract class BasePostFragment(
    layoutId: Int
) : Fragment(layoutId) {
    @Inject
    lateinit var glide: RequestManager
    @Inject
    lateinit var postAdapter: PostAdapter
    protected abstract val postProgressBar: ProgressBar
    protected abstract val basePostViewModel: BasePostViewModel
    private var curLikedIndex: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        postAdapter.apply {

            setOnLikeClickListener { post, i ->
                curLikedIndex = i
                post.isLiked = !post.isLiked
                basePostViewModel.toggleLikeForPost(post)
            }

            setOnDeletePostClickListener { post ->
                DeletePostDialog().apply {
                    setPositiveListener {
                        basePostViewModel.deletePost(post)
                    }
                }.show(childFragmentManager, null)
            }

            setOnLikedByClickListener { post ->
                basePostViewModel.getUsers(post.likedBy)
            }

        }


    }

    private fun subscribeToObservers() {
        basePostViewModel.apply {

            posts.observe(viewLifecycleOwner, EventObserver(
                onError = {
                    postProgressBar.isVisible = false
                    snackbar(it)
                },
                onLoading = {
                    postProgressBar.isVisible = true
                }
            ) { posts ->
                postProgressBar.isVisible = false
                postAdapter.posts = posts
            })

            likePostStatus.observe(viewLifecycleOwner, EventObserver(
                onError = {
                    curLikedIndex?.let { index ->
                        postAdapter.posts[index].isLiking = false
                        postAdapter.notifyItemChanged(index)
                    }
                    snackbar(it)
                },
                onLoading = {
                    curLikedIndex?.let { index ->
                        postAdapter.posts[index].isLiking = true
                        postAdapter.notifyItemChanged(index)
                    }
                }
            ) { isLiked ->
                curLikedIndex?.let { index ->
                    val uid = FirebaseAuth.getInstance().uid!!
                    postAdapter.posts[index].apply {
                        this.isLiked = isLiked
                        isLiking = false
                        if (isLiked) {
                            likedBy += uid
                        } else {
                            likedBy -= uid
                        }
                    }
                    postAdapter.notifyItemChanged(index)
                }
            })

            deletePostStatus.observe(viewLifecycleOwner, EventObserver(
                onError = { snackbar(it) }
            ) { deletedPost ->
                postAdapter.posts -= deletedPost
            })

            likedByUsers.observe(viewLifecycleOwner, EventObserver(
                onError = { snackbar(it) }
            ) { users ->
                val userAdapter = UserAdapter(glide)
                userAdapter.users = users
                LikedByDialog(userAdapter).show(childFragmentManager, null)
            })

        }
    }
}