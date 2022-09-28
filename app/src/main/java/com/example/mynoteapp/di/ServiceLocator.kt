package com.example.mynoteapp.di

import android.content.Context
import com.example.mynoteapp.data.local.database.AppDatabase
import com.example.mynoteapp.data.local.database.dao.NoteDao
import com.example.mynoteapp.data.local.database.datasource.NoteDataSource
import com.example.mynoteapp.data.local.database.datasource.NoteDataSourceImpl
import com.example.mynoteapp.data.local.preference.UserPreference
import com.example.mynoteapp.data.local.preference.UserPreferenceDataSource
import com.example.mynoteapp.data.local.preference.UserPreferenceDataSourceImpl
import com.example.mynoteapp.data.repository.LocalRepository
import com.example.mynoteapp.data.repository.LocalRepositoryImpl

object ServiceLocator {

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference(context)
    }

    fun provideUserPreferenceDataSource(context: Context): UserPreferenceDataSource {
        return UserPreferenceDataSourceImpl(provideUserPreference(context))
    }

    fun provideAppDatabase(appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    fun provideNoteDao(appContext: Context): NoteDao {
        return provideAppDatabase(appContext).noteDao()
    }

    fun provideNoteDataSource(appContext: Context): NoteDataSource {
        return NoteDataSourceImpl(provideNoteDao(appContext))
    }


    fun provideLocalRepository(context: Context): LocalRepository {
        return LocalRepositoryImpl(
            provideUserPreferenceDataSource(context),
            provideNoteDataSource(context)
        )
    }

}