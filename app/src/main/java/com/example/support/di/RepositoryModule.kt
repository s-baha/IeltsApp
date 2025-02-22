package com.example.support.di
import com.example.support.data.repository.AuthRepository
import com.example.support.data.repository.FirstGameRepository
import com.example.support.data.repository.SecondGameRepository
import com.example.support.data.repository.ThirdGameRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    // Firebase Auth
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideDatabaseReference(): DatabaseReference = Firebase.database.reference

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        database: DatabaseReference
    ): AuthRepository {
        return AuthRepository(firebaseAuth, database)
    }

    @Provides
    @Singleton
    fun provideFirstGameRepository(database: DatabaseReference): FirstGameRepository {
        return FirstGameRepository(database)
    }

    @Provides
    @Singleton
    fun provideSecondGameRepository(database: DatabaseReference): SecondGameRepository {
        return SecondGameRepository(database)
    }

    @Provides
    @Singleton
    fun provideThirdGameRepository(database: DatabaseReference): ThirdGameRepository {
        return ThirdGameRepository(database)
    }
}