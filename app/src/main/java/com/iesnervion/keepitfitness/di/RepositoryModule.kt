package com.iesnervion.keepitfitness.di

import com.iesnervion.keepitfitness.data.remote.FirebaseAuthRepositoryImpl
import com.iesnervion.keepitfitness.data.remote.FirestoreUserRepositoryImpl
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Bindeo la interfaz AuthRepository con la implementación FirebaseAuthRepositoryImpl,
     * para que Dagger pueda inyectar la implementación en los lugares donde se necesite.
     * @return FirebaseAuthRepositoryImpl
     */
    @Binds
    abstract fun bindAuthRepository(authRepository: FirebaseAuthRepositoryImpl): AuthRepository

    /**
     * Bindeo la interfaz UserRepository con la implementación FirestoreUserRepositoryImpl,
     * para que Dagger pueda inyectar la implementación en los lugares donde se necesite.
     * @return FirestoreUserRepositoryImpl
     */
    @Binds
    abstract fun bindUserRepository(userRepository: FirestoreUserRepositoryImpl): UserRepository
}