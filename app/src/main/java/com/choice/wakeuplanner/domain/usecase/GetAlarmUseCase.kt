package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmUseCase constructor(
    private val repository: AlarmRepository
): UseCase<Long, Alarm> {
    override suspend operator fun invoke(input: Long): Flow<IResult<Alarm>> {
        return repository.getAlarmById(input)
    }

}