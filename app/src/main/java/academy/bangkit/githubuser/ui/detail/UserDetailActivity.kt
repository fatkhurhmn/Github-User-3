package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.adapter.TabAdapter
import academy.bangkit.githubuser.data.local.entity.UserEntity
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.databinding.ActivityUserDetailBinding
import academy.bangkit.githubuser.utils.ExtensionFunction.loadImage
import academy.bangkit.githubuser.utils.ViewModelFactory
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel: UserDetailViewModel
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDetailViewModel = obtainViewModel(this)

        initToolbar()
        setUserDetail()
        initUserDetail()
        initTabLayout()
        setFavoriteAction()
        showLoading()
        showError()
        showMessage()
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserDetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UserDetailViewModel::class.java)
    }

    private fun initToolbar() {
        val user = intent.getParcelableExtra<UserResponse>(EXTRA_USER)
        with(binding.userDetailToolbar) {
            title = user?.username
            setNavigationOnClickListener { onBackPressed() }
            setOnMenuItemClickListener(this@UserDetailActivity)
        }
    }

    private fun setUserDetail() {
        val user = intent.getParcelableExtra<UserResponse>(EXTRA_USER)
        user?.username?.let { userDetailViewModel.setUserDetail(it) }
    }

    private fun initUserDetail() {
        userDetailViewModel.getUserDetail().observe(this) { user ->
            with(binding) {
                detailTvName.text = user.name
                detailTvFollowers.text = getFormattedNumber(user.followers)
                detailTvFollowing.text = getFormattedNumber(user.following)
                detailTvRepository.text = getFormattedNumber(user.publicRepos)

                val listDesc = arrayListOf<String>()

                with(user) {
                    location?.let { listDesc.add(it) }
                    company?.let { listDesc.add(it) }
                    if (blog != "") {
                        blog?.let { listDesc.add(it) }
                    }
                }

                val description = listDesc.joinToString("\n")

                if (description.isNotEmpty()) {
                    detailTvDescription.text = description
                } else {
                    detailTvDescription.visibility = View.GONE
                }

                detailImgAvatar.loadImage(user.avatarUrl)
            }
        }
    }

    private fun initTabLayout() {
        val tabAdapter = TabAdapter(this)
        val viewPager: ViewPager2 = binding.detailViewPager
        viewPager.adapter = tabAdapter
        val tabLayout: TabLayout = binding.detailTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setFavoriteAction() {
        val userResponse = intent.getParcelableExtra<UserResponse>(EXTRA_USER)
        val user = UserEntity().apply {
            id = userResponse?.id
            username = userResponse?.username
            photo = userResponse?.avatarUrl
        }

        userDetailViewModel.isFavoriteUser(user.id!!).observe(this) { isFavorite ->
            this.isFavorite = isFavorite
            binding.fabFavorite.imageTintList = if (isFavorite) {
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                ColorStateList.valueOf(Color.rgb(255, 74, 74))
            } else {
                binding.fabFavorite.setImageResource(R.drawable.ic_not_favorite)
                ColorStateList.valueOf(Color.rgb(255, 255, 255))
            }
        }

        binding.fabFavorite.apply {
            setOnClickListener {
                if (isFavorite) {
                    userDetailViewModel.deleteFavorite(user)
                } else {
                    userDetailViewModel.addToFavorite(user)
                }
            }
        }
    }

    private fun showMessage() {
        userDetailViewModel.message.observe(this) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading() {
        userDetailViewModel.isLoading.observe(this) { isLoading ->
            with(binding.viewUserDetailLoading) {
                if (isLoading) {
                    root.visibility = View.VISIBLE
                } else {
                    root.visibility = View.GONE
                }
            }
        }
    }

    private fun showError() {
        userDetailViewModel.isError.observe(this) { isError ->
            with(binding) {
                if (isError) {
                    containerUserDetail.visibility = View.GONE
                    viewErrorDetail.root.visibility = View.VISIBLE
                } else {
                    containerUserDetail.visibility = View.VISIBLE
                    viewErrorDetail.root.visibility = View.GONE
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_share -> {
                if (binding.viewErrorDetail.root.visibility == View.GONE) shareContent()
                true
            }

            R.id.btn_open -> {
                if (binding.viewErrorDetail.root.visibility == View.GONE) openInBrowser()
                true
            }

            else -> false
        }
    }

    private fun shareContent() {
        val user = intent.getParcelableExtra<UserResponse>(EXTRA_USER)

        val message = resources.getString(
            R.string.message,
            binding.detailTvName.text,
            user?.username,
            binding.detailTvFollowers.text,
            binding.detailTvFollowing.text,
            binding.detailTvRepository.text,
        )

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }

        val intentShare = Intent.createChooser(sendIntent, null)
        startActivity(intentShare)
    }

    private fun openInBrowser() {
        val user = intent.getParcelableExtra<UserResponse>(EXTRA_USER)
        val url = "https://www.github.com/${user?.username}"
        val intentOpenInBrowser = Intent(Intent.ACTION_VIEW)
        intentOpenInBrowser.data = Uri.parse(url)
        startActivity(intentOpenInBrowser)
    }

    private fun getFormattedNumber(number: Int): String {
        val numberString = when {
            number / 1_0000 >= 1 -> {
                val count = number / 1_000
                String.format("%d%s", count, "K")
            }
            number / 1_000_000 >= 1 -> {
                val count = number / 1_000_000
                String.format("%d%s", count, "M")
            }
            else -> {
                number.toString()
            }
        }
        return numberString
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following,
        )
        const val EXTRA_USER = "extra user"
    }
}