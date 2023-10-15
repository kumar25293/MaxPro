package com.maxpro.systemtask.di.entity

import com.maxpro.systemtask.model.UserDetails

data class UserListEntity  (
    var userlist: List<UserDetails>?=null
) {

    fun sortingList(): List<UserDetails> {
        return userlist?.sortedWith(compareBy { it.displayName })!!
    }
}
