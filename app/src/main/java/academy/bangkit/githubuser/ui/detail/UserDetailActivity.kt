package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.adapter.TabAdapter
import academy.bangkit.githubuser.databinding.ActivityUserDetailBinding
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        initTabLayout()
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
                detailTvFollowers.text = getFormattedNumber(user.followers)
                detailTvFollowing.text = getFormattedNumber(user.following)
                detailTvRepository.text = getFormattedNumber(user.publicRepos)

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

    private fun initTabLayout() {
        val tabAdapter = TabAdapter(this)
        val viewPager: ViewPager2 = binding.detailViewPager
        viewPager.adapter = tabAdapter
        val tabLayout: TabLayout = binding.detailTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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