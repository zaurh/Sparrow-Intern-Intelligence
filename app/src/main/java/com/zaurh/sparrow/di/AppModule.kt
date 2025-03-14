package com.zaurh.sparrow.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zaurh.sparrow.data.repository.AuthRepositoryImpl
import com.zaurh.sparrow.data.repository.ChatRepositoryImpl
import com.zaurh.sparrow.data.repository.CommentRepositoryImpl
import com.zaurh.sparrow.data.repository.PostRepositoryImpl
import com.zaurh.sparrow.data.repository.UserRepositoryImpl
import com.zaurh.sparrow.domain.repository.AuthRepository
import com.zaurh.sparrow.domain.repository.ChatRepository
import com.zaurh.sparrow.domain.repository.CommentRepository
import com.zaurh.sparrow.domain.repository.PostRepository
import com.zaurh.sparrow.domain.repository.UserRepository
import com.zaurh.sparrow.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository =
        AuthRepositoryImpl(
            auth = auth,
            firestore = firestore
        )

    @Singleton
    @Provides
    fun provideUserRepository(firestore: FirebaseFirestore): UserRepository = UserRepositoryImpl(
        firestore = firestore
    )

    @Singleton
    @Provides
    fun providePostRepository(
        firestore: FirebaseFirestore,
        userRepository: UserRepository
    ): PostRepository = PostRepositoryImpl(firestore, userRepository)

    @Singleton
    @Provides
    fun provideCommentRepository(
        firestore: FirebaseFirestore,
        userRepository: UserRepository
    ): CommentRepository = CommentRepositoryImpl(firestore, userRepository)

    @Singleton
    @Provides
    fun provideChatRepository(
        firestore: FirebaseFirestore
    ): ChatRepository = ChatRepositoryImpl(firestore)



}