package com.vadhara7.mysocialnetwork.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.firebase.auth.FirebaseAuth
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.data.entities.Post
import com.vadhara7.mysocialnetwork.databinding.ItemPostBinding
import javax.inject.Inject


class PostAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, layoutPosition:Int){
            binding.apply {
                glide.load(post.imageUrl).into(ivPostImage)
                glide.load(post.authorProfilePictureUrl).into(ivAuthorProfileImage)
                tvPostAuthor.text = post.authorUsername
                tvPostText.text = post.text
                val likeCount = post.likedBy.size
                tvLikedBy.text = when {
                    likeCount <= 0 -> "No likes"
                    likeCount == 1 -> "Liked by 1 person"
                    else -> "Liked by $likeCount people"
                }
                val uid = FirebaseAuth.getInstance().uid!!
                ibDeletePost.isVisible = uid == post.authorUid
                ibLike.setImageResource(if(post.isLiked) {
                    R.drawable.ic_like
                } else R.drawable.ic_like_white)

                tvPostAuthor.setOnClickListener {
                    onUserClickListener?.let { click ->
                        click(post.authorUid)
                    }
                }
                ivAuthorProfileImage.setOnClickListener {
                    onUserClickListener?.let { click ->
                        click(post.authorUid)
                    }
                }
                tvLikedBy.setOnClickListener {
                    onLikedByClickListener?.let { click ->
                        click(post)
                    }
                }
                ibLike.setOnClickListener {
                    onLikeClickListener?.let { click ->
                        if(!post.isLiking) click(post, layoutPosition)
                    }
                }
                ibComments.setOnClickListener {
                    onCommentsClickListener?.let { click ->
                        click(post)
                    }
                }
                ibDeletePost.setOnClickListener {
                    onDeletePostClickListener?.let { click ->
                        click(post)
                    }
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<Post>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding: ItemPostBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_post, parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post, holder.layoutPosition)
    }

    private var onLikeClickListener: ((Post, Int) -> Unit)? = null
    private var onUserClickListener: ((String) -> Unit)? = null
    private var onDeletePostClickListener: ((Post) -> Unit)? = null
    private var onLikedByClickListener: ((Post) -> Unit)? = null
    private var onCommentsClickListener: ((Post) -> Unit)? = null

    fun setOnLikeClickListener(listener: (Post, Int) -> Unit) {
        onLikeClickListener = listener
    }

    fun setOnUserClickListener(listener: (String) -> Unit) {
        onUserClickListener = listener
    }

    fun setOnDeletePostClickListener(listener: (Post) -> Unit) {
        onDeletePostClickListener = listener
    }

    fun setOnLikedByClickListener(listener: (Post) -> Unit) {
        onLikedByClickListener = listener
    }

    fun setOnCommentsClickListener(listener: (Post) -> Unit) {
        onCommentsClickListener = listener
    }
}