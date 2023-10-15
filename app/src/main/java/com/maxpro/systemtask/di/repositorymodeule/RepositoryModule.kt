package com.maxpro.systemtask.di.repositorymodeule

import com.maxpro.systemtask.di.NetWorkModule
import com.maxpro.systemtask.di.api.ApiInterface
import com.maxpro.systemtask.di.repository.UserRepository
import com.maxpro.systemtask.di.repositoryImpl.UserRepositoryImpl
import com.maxpro.systemtask.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [NetWorkModule::class])
@InstallIn(SingletonComponent::class)

class RepositoryModule {

    @Singleton
    @Provides
    fun provideStateAPi(@Named(Constants.RETROFIT1) retrofit: Retrofit):ApiInterface{
        return retrofit.create(ApiInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideStateRepository( apiinterface: ApiInterface):UserRepository{
        return UserRepositoryImpl(apiinterface)
    }
}