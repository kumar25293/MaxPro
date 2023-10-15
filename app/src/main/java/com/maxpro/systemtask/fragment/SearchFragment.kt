package com.maxpro.systemtask.fragment

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.maxpro.systemtask.R
import com.maxpro.systemtask.customdialog.CustomProgressDialog
import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.model.UserDetails
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.utils.Constants
import com.maxpro.systemtask.viewmodel.SearchUsersListProcessState
import com.maxpro.systemtask.viewmodel.UserDataViewModel
import com.sample.recyclerviewpaging.adapter.UserListAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.Response


class SearchFragment : Fragment() {
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }


    var pagecount:Int=1
    var searchpageLimit =30
    var sortingType ="asc"
    var sortBy ="reputation"
    var site ="stackoverflow"
    var reputationLimit ="500"

    var searchQuery =""
    private lateinit var searchRecyclerView: RecyclerView
    private  var  listAdapter: UserListAdapter?=null

    lateinit var linearlayoutManager : LinearLayoutManager
//    var userlist: List<UserDetails>?=null

    lateinit var loadmorebutton: MaterialButton
    lateinit  var usersviewmodel: UserDataViewModel

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersviewmodel = ViewModelProvider(requireActivity()).get(UserDataViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.search_fragment, container, false)
        searchRecyclerView = view.findViewById(R.id.search_recycleView)
        loadmorebutton =view.findViewById(R.id.search_loadMore)
        loadmorebutton.setOnClickListener {
            loadMore()
        }

        return view
    }



    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        linearlayoutManager = LinearLayoutManager(requireContext())
        linearlayoutManager.orientation = LinearLayoutManager.VERTICAL
        val bundle = arguments
        searchQuery =bundle?.getString("query","").toString()

        if(!searchQuery.isNullOrEmpty()) {
            getUserListByName(searchQuery)
        }

        //For get the arraylist

//        val bundle = arguments
//
//        userlist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            bundle?.getParcelableArrayList("searchlist", UserDetails::class.java)
//        } else {
//            bundle?.getParcelableArrayList("searchlist")
//        }
//
//        println("Search user list === ${userlist?.size}")

//        listAdapter?.setListData(userlist!!,0)
//        searchRecyclerView.apply {
//
//            layoutManager = LinearLayoutManager(activity)
//            adapter = listAdapter
//            layoutManager=linearlayoutManager
////            listAdapter?.setListData(list,0)
//        }
    }

    private fun getUserListByName(name:String){
        if(Constants.isNetworkAvailable(requireContext())) {
            Constants.showsProgressDialog("Searching User List",progressDialog)
            usersviewmodel.getUsersListByNmae(
                pagecount.toString(),
                searchpageLimit.toString(),
                sortingType,
                sortBy,
                reputationLimit,
                site,
                name
            )
        }
        else{
            Constants.showErrorDialog("Please Check your internet connection",requireContext())
        }
        observeUserListByName()

    }

    fun observeUserListByName(){
        usersviewmodel.mSearchstate
            .onEach { state -> handleSearchchange(state)}
            .launchIn(lifecycleScope)
    }


    private fun handleSearchchange(state: SearchUsersListProcessState){
        println("User list State  === $state")

        when(state){
            is SearchUsersListProcessState.IsLoading-> {
                if (state.isloading) {
                    Constants.showsProgressDialog("Searching",progressDialog)
                } else {
                    Constants.dismissProgressDialog(progressDialog)
                }
            }
            is SearchUsersListProcessState.Success ->handleSearchsuccess(state.stateEntity)
            is SearchUsersListProcessState.Error ->handleSearchError(state.rawresponse)
            else -> {}
        }
    }

    private fun handleSearchsuccess(userEntity: UserListEntity){

        println("search list success value  ${userEntity.userlist}")
        if (!userEntity.userlist.isNullOrEmpty()&& userEntity.userlist?.size!! > 0) {
        if(listAdapter==null) {
            listAdapter = UserListAdapter(requireContext())

                listAdapter?.setListData(userEntity.sortingList()!!, 0)
                searchRecyclerView.apply {

                    layoutManager = LinearLayoutManager(activity)
                    adapter = listAdapter
                    layoutManager = linearlayoutManager
                }
                Constants.dismissProgressDialog(progressDialog)
            } else {
                listAdapter?.setListData(
                    userEntity.sortingList(),
                    listAdapter?.usersList?.size as Int
                )

            }
        }
        else{
            Constants.showErrorDialog("Users not found",requireContext())
        }

    }


    private fun handleSearchError(stateEntity: Response<UserResponseData>){
        println("User search list Error value ${stateEntity.raw().body.toString()}")
        pagecount=-1
        Constants.showErrorDialog("Please try again later",requireContext())

    }

    private fun loadMore(){
        pagecount++
        getUserListByName(searchQuery)
        Log.d("Search UserList", "Load new list1111")
    }


}