package com.iesnervion.keepitfitness.di

import com.iesnervion.keepitfitness.data.remote.FirebaseAuthRepositoryImpl
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepository: FirebaseAuthRepositoryImpl): AuthRepository
}