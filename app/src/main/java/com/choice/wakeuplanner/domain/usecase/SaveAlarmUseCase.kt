package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class SaveAlarmUseCase constructor(
    private val repository: AlarmRepository
): UseCase<Alarm, Unit>{

    override suspend operator fun invoke(input: Alarm): Flow<IResult<Unit>> {
        return repository.insertAlarm(input)
    }


}