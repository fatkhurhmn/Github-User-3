package academy.bangkit.githubuser.adapter

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.databinding.UserItemBinding
import academy.bangkit.githubuser.utils.ExtensionFunction.loadImage
import academy.bangkit.githubuser.utils.UserDiffCallback
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val isCanClick: Boolean) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var listUsers = ArrayList<UserResponse>()
    private lateinit var onItemClickCallback: OnItemClickCallback


    fun setUser(listUsers: ArrayList<UserResponse>) {
        val diffCallback = UserDiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        with(this.listUsers) {
            clear()
            addAll(listUsers)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ListViewHolder {
        val userItemBinding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ListViewHolder, position: Int) {
        val user = listUsers[position]
        holder.bind(user)
        if (isCanClick) {
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }
        }
    }

    override fun getItemCount() = listUsers.size

    inner class ListViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse) {
            with(binding) {
                tvItemUsername.text = user.username
                tvItemId.text = itemView.resources.getString(R.string.id_user, user.id.toString())
                imgItemPhoto.loadImage(user.avatarUrl)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserResponse)
    }
}