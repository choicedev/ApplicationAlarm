package com.choice.wakeuplanner.di.repository

import android.content.Context
import com.choice.wakeuplanner.data.dao.AlarmDao
import com.choice.wakeuplanner.data.repository.AlarmRepositoryImpl
import com.choice.wakeuplanner.domain.kt.AlarmScheduler
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import com.choice.wakeuplanner.scheduler.AlarmSchedulerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmUseCaseModule {

    @Provides
    @Singleton
    fun provideAlarmRepository(
        dao: AlarmDao,
        alarmScheduler: AlarmScheduler
    ): AlarmRepository { return AlarmRepositoryImpl(dao, alarmScheduler) }


    @Provides
    @Singleton
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): AlarmScheduler {
        return AlarmSchedulerImpl(context)
    }
}