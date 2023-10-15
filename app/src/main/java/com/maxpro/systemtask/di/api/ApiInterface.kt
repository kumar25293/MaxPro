package com.maxpro.systemtask.di.api

import com.maxpro.systemtask.model.UserResponseData
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    //    @GET("/search")
//        @GET
//        suspend fun getUserList(@Url url:String, @Query("country") country: String) : Response<List<UserList>>

    @GET("2.3/users")
    suspend fun getUserList(@Query("page")  currentPage:String,@Query("pagesize")  pageSize:String,
                            @Query("order")  sortingorder:String,@Query("sort")  sortBy:String,@Query("min")  reputationvalue:String,
                            @Query("site")  sitename:String ): Response<UserResponseData>

    @GET("2.3/users")
    suspend fun getUserListByName(@Query("page")  currentPage:String,@Query("pagesize")  pageSize:String,
                            @Query("order")  sortingorder:String,@Query("sort")  sortBy:String,@Query("min")  reputationvalue:String,
                            @Query("site")  sitename:String ,@Query("inname")  name:String ): Response<UserResponseData>
}


