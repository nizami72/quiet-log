package com.quietlog.app.di

import android.content.Context
import androidx.room.Room
import com.quietlog.app.data.local.QuietLogDatabase
import com.quietlog.app.data.local.dao.AttackDao
import com.quietlog.app.data.local.dao.MedicationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuietLogDatabase =
        Room.databaseBuilder(context, QuietLogDatabase::class.java, "quietlog.db").build()

    @Provides
    fun provideAttackDao(database: QuietLogDatabase): AttackDao = database.attackDao()

    @Provides
    fun provideMedicationDao(database: QuietLogDatabase): MedicationDao = database.medicationDao()
}
