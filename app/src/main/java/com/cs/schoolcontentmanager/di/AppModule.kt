package com.cs.schoolcontentmanager.di

import com.cs.schoolcontentmanager.utils.Constants.COURSES
import com.cs.schoolcontentmanager.utils.Constants.UPLOADS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton fun providesStorageRef() = Firebase.storage.reference

    @Provides
    @Singleton fun providesUploadDbRef() = FirebaseDatabase.getInstance().getReference(UPLOADS)

    @Provides
    @Singleton fun providesAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton fun providesCoursesDocument() = Firebase.firestore.collection(COURSES)
}