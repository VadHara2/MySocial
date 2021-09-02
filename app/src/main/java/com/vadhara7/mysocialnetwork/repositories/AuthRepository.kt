package com.vadhara7.mysocialnetwork.repositories

import com.google.firebase.auth.AuthResult
import com.vadhara7.mysocialnetwork.other.Resource

interface AuthRepository {

    suspend fun register(email: String, username: String, password: String): Resource<AuthResult>

    suspend fun login(email: String, password: String): Resource<AuthResult>
}