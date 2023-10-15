package com.maxpro.systemtask.di.repositoryImpl

import com.maxpro.systemtask.di.api.ApiInterface
import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.di.repository.UserRepository
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.result.BaseResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(private val apiinterface: ApiInterface) :
    UserRepository {


    override suspend fun getUserList(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String
    ): Flow<BaseResult<UserListEntity, Response<UserResponseData>>> {
        return flow {

            try {
                val response = apiinterface.getUserList(
                    pageNum,
                    pageLimit,
                    sortingType,
                    sortBy,
                    reputationLimit,
                    site
                )
                println("User List Response success == ${response.isSuccessful}")
                println("User List  == ${response.body()}")

                if (response.isSuccessful) {
                    val body = response.body()!!


                    val userdataentity = UserListEntity(body.data)
                    val templist =userdataentity?.userlist?.sortedWith(compareBy({ it.displayName }))

                    println("user List Size == ${userdataentity?.userlist?.size}")
                    emit(BaseResult.Success(userdataentity))
                } else {
//                    val err: Response<List<UserDetails>> = response
                    emit(BaseResult.Error(/*err*/response))
                }

            } catch (e: Exception) {
                println("State Response Exception == ${e.toString()}")
            }
        }


    }

    override suspend fun getUserListByName(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String,
        name: String
    ): Flow<BaseResult<UserListEntity, Response<UserResponseData>>> {
        return flow {

            try {

                val response = apiinterface.getUserListByName(
                    pageNum,
                    pageLimit,
                    sortingType,
                    sortBy,
                    reputationLimit,
                    site,name
                )
                println("User List Response success == ${response.isSuccessful}")
                println("User List  == ${response.body()}")

                if (response.isSuccessful) {
                    val body = response.body()!!


                    val userdataentity = UserListEntity(body.data)
                    val templist =userdataentity?.userlist?.sortedWith(compareBy({ it.displayName }))

                    println("user List Size == ${userdataentity?.userlist?.size}")
                    emit(BaseResult.Success(userdataentity))
                } else {
//                    val err: Response<List<UserDetails>> = response
                    emit(BaseResult.Error(/*err*/response))
                }

            } catch (e: Exception) {
                println("State Response Exception == ${e.toString()}")
            }
        }
    }
}