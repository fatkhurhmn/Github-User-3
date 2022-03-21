package academy.bangkit.githubuser.ui.home

import academy.bangkit.githubuser.adapter.UserAdapter
import academy.bangkit.githubuser.databinding.ActivityHomeBinding
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.ui.detail.UserDetailActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private val userAdapter: UserAdapter by lazy { UserAdapter(true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchUser()
        initListUsers()
        showListUsers()
        navigateToUserDetail()
        hideToTypeIcon()
        showLoading()
        showNoResult()
        showError()
        showMessage()
    }

    private fun searchUser() {
        with(binding) {
            searchViewUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(username: String?): Boolean {
                    username?.let {
                        homeViewModel.setListUsers(username)
                    }
                    searchViewUser.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun initListUsers() {
        homeViewModel.getListUsers().observe(this) { listUsers ->
            listUsers?.let {
                if (listUsers.isNotEmpty()) {
                    userAdapter.setUser(listUsers)
                }
            }
        }
    }

    private fun showListUsers() {
        with(binding.rvListUsers) {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = userAdapter
        }
    }

    private fun navigateToUserDetail() {
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                val userDetailIntent = Intent(this@HomeActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USERNAME, user.username)
                startActivity(userDetailIntent)
            }
        })
    }

    private fun hideToTypeIcon() {
        homeViewModel.isTyped.observe(this) { isLoading ->
            if (isLoading) {
                binding.tvHideToType.visibility = View.GONE
            }
        }
    }

    private fun showLoading() {
        homeViewModel.isLoading.observe(this) { isLoading ->
            with(binding) {
                if (isLoading) {
                    viewSearchLoading.root.visibility = View.VISIBLE
                    rvListUsers.visibility = View.GONE
                    viewNoResults.root.visibility = View.GONE
                    viewError.root.visibility = View.GONE
                } else {
                    viewSearchLoading.root.visibility = View.GONE
                    rvListUsers.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showNoResult() {
        homeViewModel.isNoResult.observe(this) { isNoResult ->
            with(binding) {
                if (isNoResult) {
                    viewNoResults.root.visibility = View.VISIBLE
                    rvListUsers.visibility = View.GONE
                } else {
                    viewNoResults.root.visibility = View.GONE
                }
            }
        }
    }

    private fun showError() {
        homeViewModel.isError.observe(this) { isError ->
            with(binding) {
                if (isError) {
                    viewError.root.visibility = View.VISIBLE
                    viewSearchLoading.root.visibility = View.GONE
                    rvListUsers.visibility = View.GONE
                } else {
                    viewError.root.visibility = View.GONE
                }
            }
        }
    }

    private fun showMessage() {
        homeViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.homeContainer, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}