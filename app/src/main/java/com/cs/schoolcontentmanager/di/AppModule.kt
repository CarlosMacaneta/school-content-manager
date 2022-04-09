package com.cs.schoolcontentmanager.di

import com.cs.schoolcontentmanager.utils.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton fun providesStorageRef() = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton fun providesUploadDbRef() = FirebaseDatabase.getInstance().getReference(Constants.UPLOADS)

}