package com.choice.wakeuplanner.di.database

import android.content.Context
import androidx.room.Room
import com.choice.wakeuplanner.BuildConfig
import com.choice.wakeuplanner.data.dao.AlarmDao
import com.choice.wakeuplanner.data.database.AlarmDatabase
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
    fun provideAlarmDatabase(
        @ApplicationContext appContext: Context
    ): AlarmDatabase =
        Room.databaseBuilder(
            appContext,
            AlarmDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).fallbackToDestructiveMigration()
         .build()

    @Provides
    fun provideAlarmDao(db: AlarmDatabase): AlarmDao = db.alarmDao()
}