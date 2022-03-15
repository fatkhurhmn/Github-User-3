package academy.bangkit.githubuser.ui.followers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.githubuser.adapter.UserAdapter
import academy.bangkit.githubuser.databinding.FragmentFollowersBinding
import academy.bangkit.githubuser.ui.detail.UserDetailActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val followersViewModel by viewModels<FollowersViewModel>()
    private val userAdapter: UserAdapter by lazy { UserAdapter(false) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListFollowers()
        initListFollowers()
        showListFollowers()
        showLoading()
    }

    private fun setListFollowers() {
        val username =
            (activity as UserDetailActivity).intent.getStringExtra(UserDetailActivity.EXTRA_USERNAME)
        username?.let { followersViewModel.setUserFollowers(it) }
    }

    private fun initListFollowers() {
        followersViewModel.getUserFollowers().observe(viewLifecycleOwner) { listFollowers ->
            listFollowers?.let {
                if (listFollowers.isNotEmpty()) {
                    userAdapter.setUser(listFollowers)
                }
            }
        }
    }

    private fun showListFollowers() {
        with(binding.rvListFollowers) {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    private fun showLoading() {
        followersViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            with(binding.viewLoadingFollowers) {
                if (isLoading) {
                    root.visibility = View.VISIBLE
                } else {
                    root.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}