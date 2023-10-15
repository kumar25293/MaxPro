package com.maxpro.systemtask.di.repository

import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.result.BaseResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRepository {
    suspend fun getUserList( pageNum: String,
                             pageLimit: String,
                             sortingType: String,
                             sortBy: String,
                             reputationLimit: String,
                             site: String): Flow<BaseResult<UserListEntity, Response<UserResponseData>>>

    suspend fun getUserListByName( pageNum: String,
                             pageLimit: String,
                             sortingType: String,
                             sortBy: String,
                             reputationLimit: String,
                             site: String,name:String): Flow<BaseResult<UserListEntity, Response<UserResponseData>>>
}