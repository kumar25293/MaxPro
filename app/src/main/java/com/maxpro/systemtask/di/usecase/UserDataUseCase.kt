package com.maxpro.systemtask.di.usecase

import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.di.repository.UserRepository
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.result.BaseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class UserDataUseCase @Inject constructor(private val stateRepository: UserRepository) {
    suspend fun execute(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String
    ): Flow<BaseResult<UserListEntity, Response<UserResponseData>>> {
        return stateRepository.getUserList(
            pageNum,
            pageLimit,
            sortingType,
            sortBy,
            reputationLimit,
            site
        )
    }


    suspend fun executeSearch(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String,name:String
    ): Flow<BaseResult<UserListEntity, Response<UserResponseData>>> {
        return stateRepository.getUserListByName(
            pageNum,
            pageLimit,
            sortingType,
            sortBy,
            reputationLimit,
            site,name
        )
    }
}