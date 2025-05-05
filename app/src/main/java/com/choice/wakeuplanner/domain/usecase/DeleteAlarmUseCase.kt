package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class DeleteAlarmUseCase constructor(
    private val repository: AlarmRepository
): UseCase<Long, Unit> {
    override suspend operator fun invoke(input: Long): Flow<IResult<Unit>> {
        return repository.deleteAlarm(input)
    }

}