package com.maurya.memoease.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.android.components.ActivityComponent

//
//@Module
//@InstallIn(ActivityComponent::class)
//object AppModule {
//
//    @Provides
//    @Singleton
//    fun provideNoteDao(application: Application): noteDao {
//        return roomDatabase.getDatabase(application).noteDao()
//    }
//}
