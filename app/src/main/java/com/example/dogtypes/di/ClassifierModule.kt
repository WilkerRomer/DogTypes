package com.example.dogtypes.di

import com.example.dogtypes.machinelearning.ClassifierRepository
import com.example.dogtypes.machinelearning.ClassifierTasks
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassifierModule {

    @Binds
    abstract fun bindClassifierTasks(
        classifierRepository: ClassifierRepository
    ): ClassifierTasks

}