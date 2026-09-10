package com.quietlog.app.di

import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.repository.AttackRepositoryImpl
import com.quietlog.app.data.repository.MedicationRepository
import com.quietlog.app.data.repository.MedicationRepositoryImpl
import com.quietlog.app.data.repository.SettingsRepository
import com.quietlog.app.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAttackRepository(impl: AttackRepositoryImpl): AttackRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(impl: MedicationRepositoryImpl): MedicationRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
