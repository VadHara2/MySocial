package com.vadhara7.mysocialnetwork.repositories

import com.google.firebase.auth.AuthResult
import com.vadhara7.mysocialnetwork.other.Resource

class DefaultAuthRepository : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<AuthResult> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<AuthResult> {
        TODO("Not yet implemented")
    }
}