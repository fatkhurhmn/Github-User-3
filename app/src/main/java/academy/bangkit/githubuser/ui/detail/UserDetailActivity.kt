package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.adapter.TabAdapter
import academy.bangkit.githubuser.databinding.ActivityUserDetailBinding
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {

    private lateinit var binding: ActivityUserDetailBinding
    private val userDetailViewModel by viewModels<UserDetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        setUserDetail()
        initUserDetail()
        initTabLayout()
        setFavoriteAction()
        showLoading()
        showError()
        showMessage()
    }

    private fun initToolbar() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        with(binding.userDetailToolbar) {
            title = username
            setNavigationOnClickListener { onBackPressed() }
            setOnMenuItemClickListener(this@UserDetailActivity)
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
        binding.fabFavorite.setOnClickListener {
            Snackbar.make(binding.detailContainer, "Favorite", Snackbar.LENGTH_SHORT).show()
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
                shareContent()
                true
            }

            R.id.btn_open -> {
                openInBrowser()
                true
            }

            else -> false
        }
    }

    private fun shareContent() {
        val username = intent.getStringExtra(EXTRA_USERNAME)

        val message = resources.getString(
            R.string.message,
            binding.detailTvName.text,
            username,
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
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val url = "https://www.github.com/$username"
        val intentOpenInBrowser = Intent(Intent.ACTION_VIEW)
        intentOpenInBrowser.data = Uri.parse(url)
        startActivity(intentOpenInBrowser)
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500).placeholder(R.drawable.ic_default_photo))
            .centerCrop()
            .into(this)
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
        const val EXTRA_USERNAME = "extra username"
    }
}