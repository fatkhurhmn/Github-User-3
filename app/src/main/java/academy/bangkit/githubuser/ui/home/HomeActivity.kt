package academy.bangkit.githubuser.ui.home

import academy.bangkit.githubuser.adapter.UserAdapter
import academy.bangkit.githubuser.databinding.ActivityHomeBinding
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchUser()
        initListUsers()
        showListUsers()
        hideToTypeIcon()
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

    private fun hideToTypeIcon() {
        homeViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.tvHideToType.visibility = View.GONE
            }
        }
    }
}