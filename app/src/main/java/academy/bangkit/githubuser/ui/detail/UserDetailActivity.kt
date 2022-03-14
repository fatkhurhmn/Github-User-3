package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.databinding.ActivityUserDetailBinding
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.lang.StringBuilder

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        setUserDetail()
        initUserDetail()
    }

    private fun initToolbar() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        with(binding.userDetailToolbar) {
            title = username
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun setUserDetail() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        username?.let { userDetailViewModel.setUserDetail(it) }
    }

    private fun initUserDetail() {
        userDetailViewModel.getUserDetail().observe(this) { user ->
            with(binding) {
                detailTvName.text = user.name
                detailTvFollowers.text = user.followers.toString()
                detailTvFollowing.text = user.following.toString()
                detailTvRepository.text = user.publicRepos.toString()

                val description = StringBuilder().apply {
                    with(user) {
                        if (location != null) {
                            append(location)
                        }
                        if (company != null) {
                            append("\n" + company)
                        }
                        if (blog != "") {
                            append("\n" + blog)
                        }
                    }
                }

                if (description.isNotEmpty()) {
                    detailTvDescription.text = description
                }

                detailImgAvatar.loadImage(user.avatarUrl)
            }
        }
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500).placeholder(R.drawable.ic_default_photo))
            .centerCrop()
            .into(this)
    }

    companion object {
        const val EXTRA_USERNAME = "extra username"
    }
}