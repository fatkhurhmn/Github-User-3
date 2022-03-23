package academy.bangkit.githubuser.ui.favorite

import academy.bangkit.githubuser.adapter.UserAdapter
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.databinding.ActivityFavoriteBinding
import academy.bangkit.githubuser.ui.detail.UserDetailActivity
import academy.bangkit.githubuser.utils.ViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val userAdapter: UserAdapter by lazy { UserAdapter(true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(this)

        getFavoriteUser()
        showListUsers()
        navigateToUserDetail()
    }

    private fun getFavoriteUser() {
        favoriteViewModel.getFavoriteUser().observe(this) { listFavorite ->
            val favorites = ArrayList<UserResponse>()
            for (favorite in listFavorite) {
                val user = UserResponse(
                    id = favorite.id!!,
                    username = favorite.username!!,
                    avatarUrl = favorite.photo!!
                )
                favorites.add(user)
            }
            with(binding) {
                if (favorites.isNotEmpty()) {
                    rvFavorites.visibility = View.VISIBLE
                    tvNoFavorites.visibility = View.GONE
                    userAdapter.setUser(favorites)
                } else {
                    rvFavorites.visibility = View.GONE
                    tvNoFavorites.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showListUsers() {
        with(binding.rvFavorites) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = userAdapter
            setHasFixedSize(true)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun navigateToUserDetail() {
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserResponse) {
                val userDetailIntent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
                startActivity(userDetailIntent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getFavoriteUser()
    }
}