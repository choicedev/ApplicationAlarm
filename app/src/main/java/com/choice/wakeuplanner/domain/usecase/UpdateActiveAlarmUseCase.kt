package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateActiveAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
): UseCase<UpdateActiveAlarmUseCase.Params, Unit> {

    data class Params(
        val alarmId: Long,
        val isActive: Boolean
    )

    override suspend operator fun invoke(input: Params): Flow<IResult<Unit>> {
        return repository.updateActiveAlarm(input.alarmId, input.isActive)
    }


}