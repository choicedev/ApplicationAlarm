package com.choice.wakeuplanner.domain.usecase

import com.choice.wakeuplanner.core.IResult
import com.choice.wakeuplanner.core.UseCase
import com.choice.wakeuplanner.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteAllAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) : UseCase<Unit, Unit>{

    override suspend fun invoke(input: Unit): Flow<IResult<Unit>> {
        return repository.deleteAllAlarms()
    }

}
