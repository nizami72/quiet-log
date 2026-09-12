package com.quietlog.app.di

import android.content.Context
import androidx.room.Room
import com.quietlog.app.data.local.DatabaseKeyProvider
import com.quietlog.app.data.local.MIGRATION_1_2
import com.quietlog.app.data.local.MIGRATION_2_3
import com.quietlog.app.data.local.QuietLogDatabase
import com.quietlog.app.data.local.dao.AttackDao
import com.quietlog.app.data.local.dao.MedicationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    init {
        // sqlcipher-android does not auto-load its native library; this must happen once,
        // before any SupportOpenHelperFactory is used to open the database.
        System.loadLibrary("sqlcipher")
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): QuietLogDatabase {
        val passphrase = DatabaseKeyProvider.getOrCreatePassphrase(context)
        return Room.databaseBuilder(context, QuietLogDatabase::class.java, "quietlog.db")
            .openHelperFactory(SupportOpenHelperFactory(passphrase))
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideAttackDao(database: QuietLogDatabase): AttackDao = database.attackDao()

    @Provides
    fun provideMedicationDao(database: QuietLogDatabase): MedicationDao = database.medicationDao()
}
