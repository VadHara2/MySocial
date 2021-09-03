package com.vadhara7.mysocialnetwork.di

import com.vadhara7.mysocialnetwork.repositories.AuthRepository
import com.vadhara7.mysocialnetwork.repositories.DefaultAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object AuthModule {

    @ActivityScoped
    @Provides
    fun provideAuthRepository() = DefaultAuthRepository() as AuthRepository
}