package com.cs.schoolcontentmanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.cs.schoolcontentmanager.data.datasource.FileDataSource
import com.cs.schoolcontentmanager.data.datasource.FileDatabase
import com.cs.schoolcontentmanager.data.repository.FileRepositoryImpl
import com.cs.schoolcontentmanager.domain.repository.FileRepository
import com.cs.schoolcontentmanager.domain.usecase.*
import com.cs.schoolcontentmanager.utils.Constants.COURSES
import com.cs.schoolcontentmanager.utils.Constants.UPLOADS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.theming.ThemeMode
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton fun providesFileDatabase(app: Application): FileDatabase =
        Room.databaseBuilder(
            app,
            FileDatabase::class.java,
            FileDatabase.DB_NAME
        ).build()

    @Provides
    @Singleton fun providesStorageRef() = Firebase.storage.reference

    @Provides
    @Singleton fun providesUploadDbRef() = FirebaseDatabase.getInstance().getReference(UPLOADS)

    @Provides
    @Singleton fun providesAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton fun providesCoursesDocument() = Firebase.firestore.collection(COURSES)

    @Provides
    @Singleton fun providesFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Provides
    @Singleton fun providesFileRepository(
        dsFile: FileDataSource,
        db: FileDatabase
    ): FileRepository = FileRepositoryImpl(dsFile, db.fileDao)

    @Provides
    @Singleton fun providesFileUseCases(repository: FileRepository): FileUseCases =
        FileUseCases(
            CreateFile(repository),
            UploadFile(repository),
            GetFiles(repository),
            SearchCloudFiles(repository),
            SearchLocalFileByName(repository),
            DownloadFile(repository),
            GetCourses(repository),
            GetLocalFiles(repository),
            DeleteFile(repository)
        )

    @Provides
    @Singleton fun providesPdfViewerActivity(@ApplicationContext context: Context):
            PdfActivityConfiguration =
        PdfActivityConfiguration.Builder(context)
            .disableAnnotationEditing()
            .disableBookmarkEditing()
            .disableDocumentEditor()
            .disableAnnotationList()
            .disableBookmarkList()
            .disableDocumentInfoView()
            .disableFormEditing()
            .disableOutline()
            .hideDocumentTitleOverlay()
            .hideSettingsMenu()
            .showSignHereOverlay(false)
            .themeMode(ThemeMode.DEFAULT)
            .build()
}