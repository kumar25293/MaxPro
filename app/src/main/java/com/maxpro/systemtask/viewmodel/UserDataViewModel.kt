package com.maxpro.systemtask.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.di.usecase.UserDataUseCase
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.result.BaseResult

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val usecase: UserDataUseCase):ViewModel()
{

    val state= MutableStateFlow<UsersListProcessState>(UsersListProcessState.Init)

    val mState:StateFlow<UsersListProcessState> get() =state


    val searchstate= MutableStateFlow<SearchUsersListProcessState>(SearchUsersListProcessState.Init)

    val mSearchstate:StateFlow<SearchUsersListProcessState> get() =searchstate

    private fun hideLoading(){
        state.value = UsersListProcessState.IsLoading(true)
    }
    private fun setLoading(){
        state.value = UsersListProcessState.IsLoading(false)
    }


    private fun hideSearchLoading(){
        searchstate.value = SearchUsersListProcessState.IsLoading(true)
    }
    private fun setSearchLoading(){
        searchstate.value = SearchUsersListProcessState.IsLoading(false)
    }

    private fun showToast(msg:String){
        state.value = UsersListProcessState.ShowToast(msg)
    }

    fun getUsersList(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String
    ) {
       val job = viewModelScope.launch {
            usecase.execute(pageNum,pageLimit,sortingType,sortBy,reputationLimit,site)
                .onStart {
                    setLoading()

                }
                .catch {
                    hideLoading()
                    showToast("Exception")
                }
                .collect {
                    hideLoading()

                    when(it){
                        is BaseResult.Error -> state.value = UsersListProcessState.Error(it.rawresponse)

                        is BaseResult.Success -> state.value = UsersListProcessState.Success(it.data)
                    }
                }
        }
    }



    fun getUsersListByNmae(
        pageNum: String,
        pageLimit: String,
        sortingType: String,
        sortBy: String,
        reputationLimit: String,
        site: String,name:String
    ) {
        viewModelScope.launch {
            usecase.executeSearch(pageNum,pageLimit,sortingType,sortBy,reputationLimit,site,name)
                .onStart {
                    setSearchLoading()

                }
                .catch {
                    hideSearchLoading()
                    showToast("Exception")
                }
                .collect {
                    hideLoading()

                    when(it){
                        is BaseResult.Error -> searchstate.value = SearchUsersListProcessState.Error(it.rawresponse)

                        is BaseResult.Success -> searchstate.value = SearchUsersListProcessState.Success(it.data)
                    }
                }
        }
    }


}

sealed class UsersListProcessState{
    object Init:UsersListProcessState()
    data class IsLoading(val isloading:Boolean):UsersListProcessState()
    data class ShowToast(val msg:String):UsersListProcessState()
    data class Success(val stateEntity: UserListEntity):UsersListProcessState()
    data class Error(val rawresponse: Response<UserResponseData>):UsersListProcessState()
}


sealed class SearchUsersListProcessState{
    object Init:SearchUsersListProcessState()
    data class IsLoading(val isloading:Boolean):SearchUsersListProcessState()
    data class ShowToast(val msg:String):SearchUsersListProcessState()
    data class Success(val stateEntity: UserListEntity):SearchUsersListProcessState()
    data class Error(val rawresponse: Response<UserResponseData>):SearchUsersListProcessState()
}