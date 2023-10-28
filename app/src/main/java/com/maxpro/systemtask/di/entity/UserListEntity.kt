package com.maxpro.systemtask.di.entity

import com.maxpro.systemtask.model.UserDetails

data class UserListEntity  (
    var userlist: List<UserDetails>?=null
) {

    fun sortingList(): List<UserDetails> {
        // check sorting with single value
       
        val list =userlist?.sortedBy {
            it?.displayName
        }

        // check descending sorting with single value
        val list1 =userlist?.sortedByDescending {
            it?.displayName
        }
        return userlist?.sortedWith(compareBy { it.displayName })!! // used to check sorting with multiple values
    }
}
