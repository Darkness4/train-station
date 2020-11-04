package com.example.trainstationapp.core.usecase

import kotlinx.coroutines.flow.Flow
import com.example.trainstationapp.core.result.Result

interface UseCase<in Params, out Type> {
    suspend operator fun invoke(params: Params): Result<Type>
}

interface FlowUseCase<in Params, out Type> {
    operator fun invoke(params: Params): Flow<Result<Type>>
}
