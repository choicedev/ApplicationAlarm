package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.model.Alarm
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateAlarmUseCase @Inject constructor(
    private val repository: AlarmRepository
) : UseCase<Alarm, Unit> {
    override suspend fun invoke(input: Alarm): Flow<IResult<Unit>> {
        return repository.update(input)
    }

}
