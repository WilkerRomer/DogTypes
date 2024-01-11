package com.example.dogtypes.di

import com.example.dogtypes.auth.AuthRepository
import com.example.dogtypes.auth.AuthTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthTasksModule {

    @Binds
    abstract fun bindDogTasks(
        authRepository: AuthRepository
    ): AuthTasks
}