package academy.bangkit.githubuser.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(

    @field:SerializedName("login")
    val username: String = "",

    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("avatar_url")
    val avatarUrl: String = "",
) : Parcelable
