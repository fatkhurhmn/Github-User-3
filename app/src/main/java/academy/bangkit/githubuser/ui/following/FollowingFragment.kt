package academy.bangkit.githubuser.ui.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import academy.bangkit.githubuser.adapter.UserAdapter
import academy.bangkit.githubuser.databinding.FragmentFollowingBinding
import academy.bangkit.githubuser.ui.detail.UserDetailActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val followingViewModel by viewModels<FollowingViewModel>()
    private val userAdapter: UserAdapter by lazy { UserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListFollowers()
        initListFollowers()
        showListFollowers()
    }

    private fun setListFollowers() {
        val username =
            (activity as UserDetailActivity).intent.getStringExtra(UserDetailActivity.EXTRA_USERNAME)
        username?.let { followingViewModel.setUsersFollowing(it) }
    }

    private fun initListFollowers() {
        followingViewModel.getUserFollowing().observe(viewLifecycleOwner) { listFollowing ->
            listFollowing?.let {
                if (listFollowing.isNotEmpty()) {
                    userAdapter.setUser(listFollowing)
                }
            }
        }
    }

    private fun showListFollowers() {
        with(binding.rvListFollowing) {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}