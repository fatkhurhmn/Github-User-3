package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.databinding.ActivityUserDetailBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
    }

    private fun initToolbar() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        with(binding.userDetailToolbar) {
            title = username
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra username"
    }
}