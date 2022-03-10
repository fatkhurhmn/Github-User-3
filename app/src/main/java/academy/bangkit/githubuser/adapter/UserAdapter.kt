package academy.bangkit.githubuser.adapter

import academy.bangkit.githubuser.R
import academy.bangkit.githubuser.databinding.UserItemBinding
import academy.bangkit.githubuser.model.User
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var listUsers = ArrayList<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setUser(listUsers: ArrayList<User>) {
        with(this.listUsers) {
            clear()
            addAll(listUsers)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ListViewHolder {
        val userItemBinding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(userItemBinding)
    }

    override fun onBindViewHolder(holder: UserAdapter.ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount() = listUsers.size

    inner class ListViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvItemUsername.text = user.username
                tvItemId.text = itemView.resources.getString(R.string.id_user, user.id.toString())
                imgItemPhoto.loadImage(user.avatarUrl)
            }
        }
    }

    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }
}