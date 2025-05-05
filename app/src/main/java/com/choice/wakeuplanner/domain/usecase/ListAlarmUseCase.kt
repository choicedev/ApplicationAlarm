package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class ListAlarmUseCase constructor(
    private val repository: AlarmRepository
): UseCase<Unit, List<Alarm>> {

    override suspend operator fun invoke(input: Unit): Flow<IResult<List<Alarm>>> {
        return repository.getAlarms()
    }

}