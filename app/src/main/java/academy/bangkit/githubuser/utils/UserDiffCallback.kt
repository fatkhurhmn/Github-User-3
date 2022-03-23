package academy.bangkit.githubuser.utils

import academy.bangkit.githubuser.data.remote.response.UserResponse
import androidx.recyclerview.widget.DiffUtil

class UserDiffCallback(
    private val mOldUserList: List<UserResponse>,
    private val mNewUserList: List<UserResponse>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = mOldUserList.size

    override fun getNewListSize(): Int = mNewUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        mOldUserList[oldItemPosition].id == mNewUserList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = mOldUserList[oldItemPosition]
        val newUser = mNewUserList[newItemPosition]
        return oldUser.username == newUser.username && oldUser.avatarUrl == newUser.avatarUrl
    }
}