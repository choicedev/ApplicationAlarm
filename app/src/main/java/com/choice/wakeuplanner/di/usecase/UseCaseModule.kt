package com.choice.wakeuplanner.di.usecase

import com.choice.wakeuplanner.domain.repository.AlarmRepository
import com.choice.wakeuplanner.domain.usecase.AlarmUseCase
import com.choice.wakeuplanner.domain.usecase.DeleteAlarmUseCase
import com.choice.wakeuplanner.domain.usecase.DeleteAllAlarmsUseCase
import com.choice.wakeuplanner.domain.usecase.GetAlarmUseCase
import com.choice.wakeuplanner.domain.usecase.ListAlarmUseCase
import com.choice.wakeuplanner.domain.usecase.SaveAlarmUseCase
import com.choice.wakeuplanner.domain.usecase.UpdateActiveAlarmUseCase
import com.choice.wakeuplanner.domain.usecase.UpdateAlarmUseCase
import com.choice.wakeuplanner.presentation.feature.home.model.HomeUiEvent.DeleteAllAlarms
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AlarmUseCaseModule {


    @Provides
    @ViewModelScoped
    fun provideAlarmUseCase(
        repository: AlarmRepository,
    ): AlarmUseCase {
        return AlarmUseCase(
            listAlarmUseCase = ListAlarmUseCase(repository),
            getAlarmUseCase = GetAlarmUseCase(repository),
            saveAlarmUseCase = SaveAlarmUseCase(repository),
            deleteAlarmUseCase = DeleteAlarmUseCase(repository),
            updateActiveAlarmUseCase = UpdateActiveAlarmUseCase(repository),
            updateAlarmUseCase = UpdateAlarmUseCase(repository),
            deleteAllAlarms = DeleteAllAlarmsUseCase(repository)
        )
    }
}