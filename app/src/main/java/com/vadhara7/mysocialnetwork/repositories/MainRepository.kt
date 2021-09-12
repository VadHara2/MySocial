package com.vadhara7.mysocialnetwork.repositories

import android.net.Uri
import com.vadhara7.mysocialnetwork.other.Resource

interface MainRepository {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>
}