package com.maxpro.systemtask.activity


import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.button.MaterialButton
import com.maxpro.systemtask.R
import com.maxpro.systemtask.customdialog.CustomProgressDialog
import com.maxpro.systemtask.di.entity.UserListEntity
import com.maxpro.systemtask.fragment.SearchFragment
import com.maxpro.systemtask.model.UserDetails
import com.maxpro.systemtask.model.UserResponseData
import com.maxpro.systemtask.utils.Constants
import com.maxpro.systemtask.viewmodel.SearchUsersListProcessState
import com.maxpro.systemtask.viewmodel.UserDataViewModel
import com.maxpro.systemtask.viewmodel.UsersListProcessState
import com.sample.recyclerviewpaging.adapter.UserListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import retrofit2.Response


@AndroidEntryPoint

class MainActivity : AppCompatActivity() {

    private val usersviewmodel: UserDataViewModel by viewModels()
    var pagecount: Int = 1
    var pageLimit = 20
    var sortingType = "asc"
    var sortBy = "reputation"
    var site = "stackoverflow"
    var reputationLimit = "500"

    private lateinit var userRecyclerView: RecyclerView

    private lateinit var noUsersFound: TextView

    private var listAdapter: UserListAdapter? = null
    lateinit var layoutManager: LinearLayoutManager

    private var isLoading: Boolean = false

    private var isSearchClicked: Boolean = false

    private lateinit var loadingprogress: ProgressBar
    private lateinit var searchEditText: AppCompatEditText
    private lateinit var clearQueryImageView: ImageView
    private lateinit var voiceSearchImageView: ImageView
    private lateinit var searchImageView: ImageView

    private lateinit var fragmentContainer: FrameLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var loadmorebutton: MaterialButton

    private val progressDialog by lazy { CustomProgressDialog(this@MainActivity) }

    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(this@MainActivity)
    }


    var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

            result?.let {
                if (it) {

                    Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT)
                        .show()

                    Handler(Looper.getMainLooper()).post {
                        // Dismiss progress bar after 4 seconds
                        startListenSpeech()
                    }
                }

            }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            //showing dialog and then closing the application..
            val fragmentList: List<Fragment> = supportFragmentManager.fragments
            if (fragmentList?.size!! > 1) {
                val fragment = supportFragmentManager?.findFragmentByTag("SearchFragment")
                if (fragment is SearchFragment) {
                    supportFragmentManager?.beginTransaction()?.remove(fragment)?.commit()
                    fragmentContainer.visibility = View.GONE
                    swipeRefreshLayout.visibility = View.VISIBLE
                    return
                }
            } else {
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRecyclerView = findViewById(R.id.user_list)
        noUsersFound = findViewById(R.id.tv_no_user_found)
        loadingprogress = findViewById(R.id.loading_progress)
        searchEditText = findViewById(R.id.search_edit_text)
        clearQueryImageView = findViewById(R.id.clear_search_query)
        voiceSearchImageView = findViewById(R.id.voice_search_query)
        searchImageView = findViewById(R.id.search_image_view)
        loadmorebutton = findViewById(R.id.loadMore)
        fragmentContainer = findViewById(R.id.frameLayout_container)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        initClickEvents()
        getusersList()
        observeUsersList()

//        addScrollerListener()

        // Enable onback press in Android 33
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    fun initClickEvents() {

        swipeRefreshLayout.setOnRefreshListener {
            pagecount = 1

            Handler(Looper.getMainLooper()).post(Runnable {
                swipeRefreshLayout.isRefreshing = false
                getusersList()
            })
        }

        searchImageView.setOnClickListener {
            val query = searchEditText.text.toString().toString()
            if (!query.isNullOrEmpty()) {
                if (!isSearchClicked) {
//                    filterWithQuery(query)
                    addFragment(query)
                    toggleImageView(query)
                }
            }
        }

        loadmorebutton?.setOnClickListener {

            loadMore()
        }
//        editText?.doOnTextChanged { text, _, _, _ ->
////            val query = text.toString().lowercase(Locale.getDefault())
//            val query = text.toString()
//
//        }

        voiceSearchImageView.setOnClickListener {
            requestPermission()

        }

        clearQueryImageView.setOnClickListener {
            searchEditText.setText("")
            voiceSearchImageView.visibility = View.VISIBLE
        }
    }

    fun filterWithQuery(query: String) {

//        getUserListByName(query)

    }

    override fun onDestroy() {
        if (null != speechRecognizer) {
            speechRecognizer.destroy();
        }
        super.onDestroy()
    }

    fun startListenSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {

                }

                override fun onBeginningOfSpeech() {

                }

                override fun onRmsChanged(rmsdB: Float) {

                }

                override fun onBufferReceived(buffer: ByteArray?) {

                }

                override fun onEndOfSpeech() {

                }

                override fun onError(error: Int) {

                }

                override fun onResults(results: Bundle?) {

                    results?.let {
                        val value = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        searchEditText.setText(value?.get(0))
                        showVoiceSearchDialog(value?.get(0).toString())
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {

                }

                override fun onEvent(eventType: Int, params: Bundle?) {

                }

            })

            speechRecognizer.startListening(intent)
        }
    }


//    private fun filterWithQueryLocal(query: String) {
//        if (query.isNotEmpty()) {
//            val filteredList: List<UserDetails> = onQueryChanged(query)
//            attachAdapter(filteredList)
//            toggleRecyclerView(filteredList)
//        } else if (query.isEmpty()) {
//            attachAdapter(sportsList)
//        }
//    }

    private fun toggleImageView(query: String) {
        if (query.isNotEmpty()) {
            clearQueryImageView.visibility = View.VISIBLE
            voiceSearchImageView.visibility = View.INVISIBLE
        } else if (query.isEmpty()) {
            clearQueryImageView.visibility = View.INVISIBLE
            voiceSearchImageView.visibility = View.VISIBLE
        }
    }


    private fun attachAdapter(list: List<UserDetails>) {
        listAdapter = UserListAdapter(this@MainActivity)
        listAdapter?.setListData(list, 0)
        userRecyclerView?.setHasFixedSize(true);
        userRecyclerView.adapter = listAdapter
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        userRecyclerView.layoutManager = layoutManager
        toggleRecyclerView(list)
    }

    private fun toggleRecyclerView(userlist: List<UserDetails>) {
        if (userlist.isEmpty()) {
            userRecyclerView.visibility = View.INVISIBLE
            noUsersFound.visibility = View.VISIBLE
        } else {
            userRecyclerView.visibility = View.VISIBLE
            noUsersFound.visibility = View.INVISIBLE
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activityResultLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
            } else {
                startListenSpeech()
            }

//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf<String>(Manifest.permission.RECORD_AUDIO),
//                    1000
//                )
//            }
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun getusersList() {
        if (Constants.isNetworkAvailable(this@MainActivity)) {
            Constants.showsProgressDialog("Fetching User List", progressDialog)
            usersviewmodel.getUsersList(
                pagecount.toString(),
                pageLimit.toString(),
                sortingType,
                sortBy,
                reputationLimit,
                site
            )
        } else {
            pagecount -= 1
            Constants.showErrorDialog("Please Check your internet connection", this@MainActivity)
        }

    }

//    private fun getUserListByName(name:String){
//        if(Constants.isNetworkAvailable(this@MainActivity)) {
//            Constants.showsProgressDialog("Searching User List")
//            isSearchClicked =true
//            usersviewmodel.getUsersListByNmae(
//                pagecount.toString(),
//                searchpageLimit.toString(),
//                sortingType,
//                sortBy,
//                reputationLimit,
//                site,
//                name
//            )
//        }
//        else{
//            isSearchClicked =false
//            showErrorDialog("Please Check your internet connection")
//        }
//        observeUserListByName()
//
//    }

    fun observeUsersList() {
        usersviewmodel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handlechange(state) }
            .launchIn(lifecycleScope)
    }


//    fun observeUserListByName(){
//        usersviewmodel.mSearchstate
//            .onEach { state -> handleVoiceSearchchange(state)}
//            .launchIn(lifecycleScope)
//    }

//    fun dismissProgressDialog(){
//        Handler(Looper.getMainLooper()).postDelayed({
//            // Dismiss progress bar after 4 seconds
//            progressDialog.stop()
//        }, 1000)
//    }
//
//    fun showsProgressDialog(title:String){
//        Handler(Looper.getMainLooper()).post {
//            // Dismiss progress bar after 4 seconds
//            progressDialog.start(title)
//            progressDialog.start()
//        }
//    }

    private fun handlechange(state: UsersListProcessState) {
        println("User list State  === $state")
        loadingprogress.visibility = View.GONE
        isLoading = false
        when (state) {
            is UsersListProcessState.IsLoading -> {

                if (state.isloading) {
                } else {
                    Constants.dismissProgressDialog(progressDialog)
                }
            }
            is UsersListProcessState.Success -> handlesuccess(state.stateEntity)
            is UsersListProcessState.Error -> handleError(state.rawresponse)
            else -> {}
        }
    }

    private fun handlesuccess(userEntity: UserListEntity) {

        if (!userEntity.userlist.isNullOrEmpty() && userEntity.userlist?.size!! > 0) {
            if (listAdapter == null) {

                attachAdapter(userEntity.sortingList())
                Constants.dismissProgressDialog(progressDialog)
            } else {
                listAdapter?.setListData(
                    userEntity.sortingList(),
                    listAdapter?.usersList?.size as Int
                )
                Constants.dismissProgressDialog(progressDialog)

            }
        }
        else{
            Constants.showErrorDialog("Users not found",this@MainActivity)
        }

    }


    private fun handleError(stateEntity: Response<UserResponseData>) {
        pagecount = -1
        Constants.dismissProgressDialog(progressDialog)
        println("User list Error value ${stateEntity.raw().body.toString()}")

        Constants.showErrorDialog("Please try again later", this@MainActivity)

    }


    fun showVoiceSearchDialog(name: String) {
        val builder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)
        val dialogView: View = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.voice_search_confirm_view, null, false)
        val buttonyes = dialogView.findViewById<Button>(R.id.button_yes)
        val buttonno = dialogView.findViewById<Button>(R.id.button_no)
        val tvSearchQuery: TextView = dialogView.findViewById<TextView>(R.id.tv_search_query)
        val tvtitle: TextView = dialogView.findViewById<TextView>(R.id.tv_confirm_msg)

        tvtitle.visibility = View.VISIBLE
        tvtitle.text = "Do You want search below user name ?"

        tvSearchQuery.text = name

        builder.setView(dialogView)

        val alertDialog = builder.create()
        buttonyes.setOnClickListener {
//            getUserListByName(name)
            addFragment(name)
            alertDialog.dismiss()
        }
        buttonno.setOnClickListener {
            searchEditText.setText("")
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun loadMore() {
        searchEditText.setText("")
        pagecount++
        getusersList()
        Log.d("UserList", "Load new list1111")
    }


    fun addFragment(query: String) {
        val bundle = Bundle()
//        bundle.putParcelableArrayList("searchlist", userEntity.userlist as ArrayList<out Parcelable?>?)
        bundle.putString("query", query)
        var fragment = SearchFragment()
        fragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout_container, fragment, "SearchFragment")
        fragmentTransaction.addToBackStack("SearchFragment")
        fragmentTransaction.commit()
        swipeRefreshLayout.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
    }


}