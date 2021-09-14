package com.vadhara7.mysocialnetwork.repositories

import android.net.Uri
import com.vadhara7.mysocialnetwork.data.entities.User
import com.vadhara7.mysocialnetwork.other.Resource

interface MainRepository {
    suspend fun createPost(imageUri: Uri, text: String): Resource<Any>
    suspend fun getUsers(uids: List<String>): Resource<List<User>>
    suspend fun getUser(uid: String): Resource<User>
}